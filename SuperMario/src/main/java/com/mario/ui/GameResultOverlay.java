package com.mario.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * 游戏结算覆盖层：
 * - 负责绘制死亡/通关界面；
 * - 负责结算按钮的点击判定。
 */
public class GameResultOverlay {
    /**
     * 结算覆盖层类型：
     * NONE 表示不显示结算界面。
     */
    public enum OverlayType {
        NONE,
        GAME_OVER,
        GAME_COMPLETED
    }

    /**
     * 结算界面可触发的按钮动作。
     */
    public enum Action {
        NONE,
        RESTART_LEVEL,
        RESTART_GAME,
        BACK_TO_MENU
    }

    private static final Rectangle PRIMARY_BUTTON_BOUNDS = new Rectangle(320, 340, 260, 52);
    private static final Rectangle SECONDARY_BUTTON_BOUNDS = new Rectangle(320, 420, 260, 52);

    /**
     * 绘制结算覆盖层。
     *
     * @param graphics 画笔
     * @param overlayType 当前覆盖层类型
     * @param score 最终得分
     * @param deathCount 当前累计死亡次数
     */
    public void draw(Graphics graphics, OverlayType overlayType, int score, int deathCount) {
        if (graphics == null || overlayType == OverlayType.NONE) {
            return;
        }

        // 先绘制半透明遮罩，保留当前冻结画面的上下文感。
        graphics.setColor(new Color(0, 0, 0, 150));
        graphics.fillRect(0, 0, 900, 600);

        // 再绘制中间结算面板，用于承载标题、统计信息和按钮。
        graphics.setColor(new Color(255, 255, 255, 200));
        graphics.fillRoundRect(220, 140, 460, 360, 30, 30);
        graphics.setColor(new Color(60, 60, 60));
        graphics.drawRoundRect(220, 140, 460, 360, 30, 30);

        graphics.setFont(new Font("SansSerif", Font.BOLD, 32));
        ScreenDrawSupport.drawCenteredString(graphics, getTitle(overlayType), 220, 205, 460);

        graphics.setFont(new Font("SansSerif", Font.BOLD, 24));
        ScreenDrawSupport.drawCenteredString(graphics, "最终得分: " + score, 220, 255, 460);
        ScreenDrawSupport.drawCenteredString(graphics, "死亡次数: " + deathCount, 220, 300, 460);

        ScreenDrawSupport.drawButton(graphics, PRIMARY_BUTTON_BOUNDS, getPrimaryButtonText(overlayType), 24);
        ScreenDrawSupport.drawButton(graphics, SECONDARY_BUTTON_BOUNDS, "回到菜单", 24);
    }

    /**
     * 处理结算覆盖层按钮点击。
     *
     * @param overlayType 当前覆盖层类型
     * @param mouseX 鼠标 x 坐标
     * @param mouseY 鼠标 y 坐标
     * @return 命中的按钮动作；未命中时返回 NONE
     */
    public Action handleClick(OverlayType overlayType, int mouseX, int mouseY) {
        if (overlayType == OverlayType.NONE) {
            return Action.NONE;
        }
        if (PRIMARY_BUTTON_BOUNDS.contains(mouseX, mouseY)) {
            return overlayType == OverlayType.GAME_OVER ? Action.RESTART_LEVEL : Action.RESTART_GAME;
        }
        if (SECONDARY_BUTTON_BOUNDS.contains(mouseX, mouseY)) {
            return Action.BACK_TO_MENU;
        }
        return Action.NONE;
    }

    /**
     * 根据结算类型返回标题文本。
     */
    private String getTitle(OverlayType overlayType) {
        return overlayType == OverlayType.GAME_OVER ? "游戏结束" : "闯关成功";
    }

    /**
     * 根据结算类型返回主按钮文本。
     */
    private String getPrimaryButtonText(OverlayType overlayType) {
        return overlayType == OverlayType.GAME_OVER ? "重新开始本关" : "重新挑战";
    }
}
