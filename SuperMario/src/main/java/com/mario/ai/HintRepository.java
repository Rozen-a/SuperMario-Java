package com.mario.ai;

import java.util.ArrayList;
import java.util.List;

/**
 * 提示规则仓库
 *
 * 负责维护按关卡和区域划分的提示文本
 */
public class HintRepository {
    private static final int AREA_WIDTH = 180;  // 区域宽度
    private static final int MAX_AREA_ID = 5;  // 最大区域编号

    private final List<HintRule> rules = new ArrayList<>();

    /**
     * 创建提示规则仓库并注册默认规则
     */
    public HintRepository() {
        registerDefaultRules();
    }

    /**
     * 根据关卡 位置和推荐类型查找最适合的提示
     *
     * @param levelIndex 当前关卡索引
     * @param x 当前横坐标
     * @param preferredType 推荐提示类型
     * @return 命中的提示规则 若没有则返回 null
     */
    public HintRule findBestRule(int levelIndex, int x, HintType preferredType) {
        // 优先匹配推荐类型 其次回退到通用提示 最后只要区域命中就返回任意提示
        HintRule exactRule = findExactRule(levelIndex, x, preferredType);
        if (exactRule != null) {
            return exactRule;
        }

        HintRule generalRule = findExactRule(levelIndex, x, HintType.GENERAL);
        if (generalRule != null) {
            return generalRule;
        }

        for (HintRule rule : rules) {
            if (rule.matchesArea(levelIndex, x)) {
                return rule;
            }
        }
        return null;
    }

    /**
     * 解析当前位置所属区域编号
     *
     * levelIndex 预留用于未来按关卡调整区域宽度
     *
     * @param levelIndex 当前关卡索引
     * @param x 当前横坐标
     * @return 1 到 5 的区域编号
     */
    public int resolveAreaId(int levelIndex, int x) {
        int safeX = Math.max(0, x);
        int areaId = safeX / AREA_WIDTH + 1;
        return Math.min(MAX_AREA_ID, areaId);
    }

    /**
     * 注册默认提示规则
     */
    private void registerDefaultRules() {
        // 第一关重点覆盖基础移动 敌人 坑洞和可破坏方块
        rules.add(new HintRule(0, 1, 0, 179, HintType.GENERAL, "持续向右前进可更快熟悉地形"));
        rules.add(new HintRule(0, 2, 180, 359, HintType.ENEMY, "等蘑菇靠近后从头顶踩过去更安全"));
        rules.add(new HintRule(0, 3, 360, 539, HintType.JUMP, "按住右方向并提前一点起跳"));
        rules.add(new HintRule(0, 4, 540, 719, HintType.BLOCK, "试着从下方向上顶开前方方块"));
        rules.add(new HintRule(0, 5, 720, 900, HintType.JUMP, "助跑后再跳更容易越过前方障碍"));

        // 第二关使用更通用的区域提示
        rules.add(new HintRule(1, 1, 0, 179, HintType.GENERAL, "先观察前方地形再决定跳跃时机"));
        rules.add(new HintRule(1, 2, 180, 359, HintType.ENEMY, "先处理前方敌人再继续推进会更稳"));
        rules.add(new HintRule(1, 3, 360, 539, HintType.JUMP, "连续失败时可以更早一点起跳"));
        rules.add(new HintRule(1, 4, 540, 719, HintType.BLOCK, "如果路线受阻 可以尝试顶一顶上方方块"));
        rules.add(new HintRule(1, 5, 720, 900, HintType.GENERAL, "保持节奏前进 不要在危险区域停留过久"));

        // 第三关优先给出终局阶段的稳妥建议
        rules.add(new HintRule(2, 1, 0, 179, HintType.GENERAL, "先确认安全落点 再继续前进"));
        rules.add(new HintRule(2, 2, 180, 359, HintType.ENEMY, "遇到敌人时先拉开距离再决定踩击"));
        rules.add(new HintRule(2, 3, 360, 539, HintType.JUMP, "保持助跑节奏能提高跨越成功率"));
        rules.add(new HintRule(2, 4, 540, 719, HintType.BLOCK, "卡住时可以留意是否有可顶开的方块"));
        rules.add(new HintRule(2, 5, 720, 900, HintType.GENERAL, "接近终点时优先保证安全通过"));
    }

    /**
     * 查找与推荐类型完全匹配的规则
     *
     * @param levelIndex 当前关卡索引
     * @param x 当前横坐标
     * @param preferredType 推荐提示类型
     * @return 命中的提示规则 若没有则返回 null
     */
    private HintRule findExactRule(int levelIndex, int x, HintType preferredType) {
        for (HintRule rule : rules) {
            if (rule.matches(levelIndex, x, preferredType)) {
                return rule;
            }
        }
        return null;
    }
}
