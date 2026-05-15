package com.mario.ai;

/**
 * 困难分析器
 *
 * 使用滚动窗口内的行为统计特征来判断玩家是否可能遇到困难
 */
public class DifficultyAnalyzer {
    private static final int DIFFICULT_THRESHOLD = 5;  // 困难阈值
    private static final long STUCK_FRAMES_THRESHOLD = 120;  // 停留时间阈值
    private static final int LOW_PROGRESS_THRESHOLD = 60;  // 进度阈值

    /**
     * 分析当前行为快照
     *
     * @param snapshot 当前行为特征快照
     * @return 困难分析结果
     */
    public DifficultyAnalysisResult analyze(BehaviorSnapshot snapshot) {
        if (snapshot == null) {
            return new DifficultyAnalysisResult(false, 0, HintType.GENERAL, "");
        }

        // 困难分数用于触发提示 权重用于挑选更贴近原因的提示类型
        int score = 0;  // 困难分数
        int jumpWeight = 0;  // 跳跃权重
        int enemyWeight = 0;  // 敌人权重
        int blockWeight = 0;  // 方块权重
        int generalWeight = 0;  // 通用权重
        StringBuilder reasonBuilder = new StringBuilder();

        if (snapshot.getSameAreaDeathCount() >= 2) {
            // 同一区域多次死亡通常表示敌人或跳跃点难以通过
            score += 3;
            enemyWeight += 1;
            jumpWeight += 1;
            generalWeight += 2;
            appendReason(reasonBuilder, "同一区域多次死亡");
        }
        if (snapshot.getSameAreaRestartCount() >= 2) {
            // 同一区域多次重开说明玩家主动放弃当前尝试
            score += 2;
            generalWeight += 2;
            appendReason(reasonBuilder, "同一区域多次重开");
        }
        if (snapshot.getStuckFrames() >= STUCK_FRAMES_THRESHOLD) {
            // 停留时间过长说明推进缓慢 需要更通用提示
            score += 2;
            generalWeight += 2;
            appendReason(reasonBuilder, "停留时间较长");
        }
        if (snapshot.getProgressDistanceRecent() <= LOW_PROGRESS_THRESHOLD) {
            // 最近推进距离不足也表示卡住
            score += 2;
            generalWeight += 1;
            appendReason(reasonBuilder, "最近前进距离较小");
        }
        if (snapshot.getFailedJumpCountRecent() >= 2) {
            // 连续起跳受阻通常与顶头方块或时机相关
            score += 2;
            jumpWeight += 3;
            blockWeight += 2;
            appendReason(reasonBuilder, "连续起跳受阻");
        }
        if (snapshot.getJumpCountRecent() >= 3 && snapshot.getProgressDistanceRecent() <= LOW_PROGRESS_THRESHOLD) {
            // 多次跳跃但没推进 优先给跳跃相关提示
            score += 1;
            jumpWeight += 2;
            appendReason(reasonBuilder, "多次尝试跳跃仍未前进");
        }
        if (snapshot.getEnemyHitCountRecent() >= 1) {
            // 被敌人阻挡后优先推荐应对敌人的提示
            score += 2;
            enemyWeight += 3;
            appendReason(reasonBuilder, "最近被敌人阻挡");
        }
        if (snapshot.getEnemyHitCountRecent() >= 2) {
            score += 1;
            enemyWeight += 1;
        }
        if (snapshot.getFailedJumpCountRecent() >= 2
                && snapshot.getProgressDistanceRecent() <= 40
                && snapshot.getBreakableBlockHitCountRecent() == 0) {
            // 受阻且没有破坏方块行为时 更可能是没意识到可顶开的方块
            blockWeight += 2;
        }
        if (snapshot.getOscillationCountRecent() >= 4) {
            // 来回试探移动代表犹豫或寻找路线
            score += 1;
            generalWeight += 1;
            appendReason(reasonBuilder, "出现来回试探移动");
        }

        HintType recommendedType = selectHintType(jumpWeight, enemyWeight, blockWeight, generalWeight);
        return new DifficultyAnalysisResult(score >= DIFFICULT_THRESHOLD, score, recommendedType,
                reasonBuilder.toString());
    }

    /**
     * 从多个候选权重中选择最匹配的提示类型
     * 优先选择权重最高的类型 若权重相同则按顺序顺序选择
     *
     * @param jumpWeight 跳跃相关权重
     * @param enemyWeight 敌人相关权重
     * @param blockWeight 方块相关权重
     * @param generalWeight 通用权重
     * @return 推荐提示类型
     */
    private HintType selectHintType(int jumpWeight, int enemyWeight, int blockWeight, int generalWeight) {
        int maxWeight = Math.max(Math.max(jumpWeight, enemyWeight), Math.max(blockWeight, generalWeight));
        if (maxWeight <= 0) {
            return HintType.GENERAL;
        }
        if (enemyWeight == maxWeight) {
            return HintType.ENEMY;
        }
        if (jumpWeight == maxWeight) {
            return HintType.JUMP;
        }
        if (blockWeight == maxWeight) {
            return HintType.BLOCK;
        }
        return HintType.GENERAL;
    }

    /**
     * 追加困难原因
     *
     * @param builder 原因拼接器
     * @param reason 单条原因
     */
    private void appendReason(StringBuilder builder, String reason) {
        if (builder.length() > 0) {
            builder.append("、");
        }
        builder.append(reason);
    }
}
