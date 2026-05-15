package com.mario.ai;

import com.mario.entity.creature.Mario;
import com.mario.entity.scene.Background;

/**
 * 智能提示控制器
 *
 * 统一管理行为跟踪 困难分析 提示生成和提示冷却
 */
public class HintController implements BehaviorEventListener {
    private static final long HINT_SHOW_FRAMES = 180;  // 提示显示时间阈值
    private static final long HINT_COOLDOWN_FRAMES = 180;  // 提示冷却时间阈值
    private static final long MIN_HINT_ANALYSIS_FRAMES = 60;  // 最小分析分析时间阈值

    // 提示控制器状态
    private final PlayerBehaviorTracker tracker = new PlayerBehaviorTracker();
    private final DifficultyAnalyzer analyzer = new DifficultyAnalyzer();
    private final HintRepository repository = new HintRepository();

    private String currentHintText;  // 当前提示文本
    private long hintShowUntilFrame;  // 提示显示结束帧
    private long hintCooldownUntilFrame;  // 提示冷却结束帧
    private int lastHintAreaId = -1;  // 上一个提示区域编号
    private HintType lastHintType = HintType.GENERAL;  // 上一个提示类型

    /**
     * 开始新游戏时清空行为统计和提示状态
     */
    public synchronized void resetForNewGame() {
        tracker.startNewSession();
        clearCurrentHint();
        lastHintAreaId = -1;
        lastHintType = HintType.GENERAL;
        hintCooldownUntilFrame = 0;
    }

    /**
     * 进入新关卡时重置本关的滚动行为窗口
     *
     * @param levelIndex 当前关卡索引
     */
    public synchronized void onLevelStart(int levelIndex) {
        tracker.onLevelStart(levelIndex);
        clearCurrentHint();
    }

    /**
     * 记录一次主动重开关卡
     *
     * @param mario 马里奥对象
     */
    public synchronized void onRestart(Mario mario) {
        if (mario == null) {
            return;
        }
        // 重开通常意味着玩家在当前区域受阻 需要计入困难特征并清空当前提示
        int levelIndex = resolveLevelIndex(mario);
        int areaId = resolveAreaId(levelIndex, mario.getX());
        tracker.onRestart(levelIndex, mario.getX(), mario.getY(), areaId);
        clearCurrentHint();
    }

    /**
     * 记录一次死亡
     *
     * @param mario 马里奥对象
     */
    public synchronized void onDeath(Mario mario) {
        if (mario == null) {
            return;
        }
        // 死亡属于强信号 需要记录并清空提示状态避免信息过期
        int levelIndex = resolveLevelIndex(mario);
        int areaId = resolveAreaId(levelIndex, mario.getX());
        tracker.onDeath(levelIndex, mario.getX(), mario.getY(), areaId);
        clearCurrentHint();
    }

    /**
     * 在主循环中更新提示系统
     *
     * @param mario 马里奥对象
     * @param levelIndex 当前关卡索引
     * @param gameEnded 当前帧是否已进入结算
     */
    public synchronized void update(Mario mario, int levelIndex, boolean gameEnded) {
        if (mario == null) {
            return;
        }

        int areaId = resolveAreaId(levelIndex, mario.getX());
        // 每帧采样位置并维护滚动窗口内的统计
        tracker.onFrame(mario, levelIndex, areaId);
        long currentFrame = tracker.getCurrentFrame();
        // 先处理提示过期 避免过期提示影响后续展示判断
        expireHintIfNeeded(currentFrame);

        // 结算状态或采样数据不足时不做困难分析
        if (gameEnded || currentFrame < MIN_HINT_ANALYSIS_FRAMES) {
            return;
        }

        BehaviorSnapshot snapshot = tracker.buildSnapshot();
        DifficultyAnalysisResult result = analyzer.analyze(snapshot);
        if (!result.isDifficult()) {
            return;
        }
        // 处于冷却中或已有提示显示时不重复弹出
        if (currentFrame < hintCooldownUntilFrame || currentHintText != null) {
            return;
        }

        HintRule rule = repository.findBestRule(levelIndex, snapshot.getCurrentX(), result.getRecommendedHintType());
        if (rule == null) {
            return;
        }
        // 同一区域同类型提示在冷却内不重复触发
        if (rule.getAreaId() == lastHintAreaId && rule.getHintType() == lastHintType
                && currentFrame < hintCooldownUntilFrame) {
            return;
        }

        // 命中规则后开始展示并进入冷却
        currentHintText = rule.getHintText();
        hintShowUntilFrame = currentFrame + HINT_SHOW_FRAMES;
        hintCooldownUntilFrame = currentFrame + HINT_COOLDOWN_FRAMES;
        lastHintAreaId = rule.getAreaId();
        lastHintType = rule.getHintType();
    }

    /**
     * 获取当前提示文本
     *
     * @return 当前显示中的提示 若没有则返回 null
     */
    public synchronized String getCurrentHintText() {
        expireHintIfNeeded(tracker.getCurrentFrame());
        return currentHintText;
    }

    /**
     * 手动清空当前提示
     */
    public synchronized void clearCurrentHint() {
        currentHintText = null;
        hintShowUntilFrame = 0;
    }

    /**
     * 上报一次成功起跳事件
     *
     * @param mario 马里奥对象
     */
    @Override
    public synchronized void onJump(Mario mario) {
        if (mario == null) {
            return;
        }
        int levelIndex = resolveLevelIndex(mario);
        int areaId = resolveAreaId(levelIndex, mario.getX());
        tracker.onJump(levelIndex, mario.getX(), mario.getY(), areaId);
    }

    /**
     * 上报一次跳跃受阻事件
     *
     * @param mario 马里奥对象
     */
    @Override
    public synchronized void onFailedJump(Mario mario) {
        if (mario == null) {
            return;
        }
        int levelIndex = resolveLevelIndex(mario);
        int areaId = resolveAreaId(levelIndex, mario.getX());
        tracker.onFailedJump(levelIndex, mario.getX(), mario.getY(), areaId);
    }

    /**
     * 上报一次被敌人击中事件
     *
     * @param mario 马里奥对象
     */
    @Override
    public synchronized void onEnemyHit(Mario mario) {
        if (mario == null) {
            return;
        }
        int levelIndex = resolveLevelIndex(mario);
        int areaId = resolveAreaId(levelIndex, mario.getX());
        tracker.onEnemyHit(levelIndex, mario.getX(), mario.getY(), areaId);
    }

    /**
     * 上报一次击败敌人事件
     *
     * @param mario 马里奥对象
     */
    @Override
    public synchronized void onEnemyKilled(Mario mario) {
        if (mario == null) {
            return;
        }
        int levelIndex = resolveLevelIndex(mario);
        int areaId = resolveAreaId(levelIndex, mario.getX());
        tracker.onEnemyKilled(levelIndex, mario.getX(), mario.getY(), areaId);
    }

    /**
     * 上报一次破坏可破坏方块事件
     *
     * @param mario 马里奥对象
     */
    @Override
    public synchronized void onBreakableBlockBroken(Mario mario) {
        if (mario == null) {
            return;
        }
        int levelIndex = resolveLevelIndex(mario);
        int areaId = resolveAreaId(levelIndex, mario.getX());
        tracker.onBreakableBlockBroken(levelIndex, mario.getX(), mario.getY(), areaId);
    }

    /**
     * 到时后自动清空提示
     *
     * @param currentFrame 当前帧号
     */
    private void expireHintIfNeeded(long currentFrame) {
        if (currentHintText != null && currentFrame >= hintShowUntilFrame) {
            currentHintText = null;
        }
    }

    /**
     * 解析当前关卡索引
     *
     * @param mario 马里奥对象
     * @return 当前关卡索引
     */
    private int resolveLevelIndex(Mario mario) {
        Background background = mario.getBackground();
        if (background == null) {
            return 0;
        }
        return Math.max(0, background.getSort() - 1);
    }

    /**
     * 解析当前所在区域
     *
     * @param levelIndex 当前关卡索引
     * @param x 当前横坐标
     * @return 当前区域编号
     */
    private int resolveAreaId(int levelIndex, int x) {
        return repository.resolveAreaId(levelIndex, x);
    }
}
