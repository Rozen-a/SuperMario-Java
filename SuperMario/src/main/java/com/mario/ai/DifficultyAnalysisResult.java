package com.mario.ai;

/**
 * 困难分析结果
 */
public class DifficultyAnalysisResult {
    private final boolean difficult;
    private final int difficultyScore;
    private final HintType recommendedHintType;
    private final String reason;

    /**
     * 创建困难分析结果
     *
     * @param difficult 是否进入困难状态
     * @param difficultyScore 困难分数
     * @param recommendedHintType 推荐提示类型
     * @param reason 触发原因
     */
    public DifficultyAnalysisResult(boolean difficult, int difficultyScore,
                                    HintType recommendedHintType, String reason) {
        this.difficult = difficult;
        this.difficultyScore = difficultyScore;
        this.recommendedHintType = recommendedHintType;
        this.reason = reason;
    }

    public boolean isDifficult() {
        return difficult;
    }

    public int getDifficultyScore() {
        return difficultyScore;
    }

    public HintType getRecommendedHintType() {
        return recommendedHintType;
    }

    public String getReason() {
        return reason;
    }
}
