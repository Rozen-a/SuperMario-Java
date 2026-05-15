package com.mario.ui;

import com.mario.constant.StaticValue;
import com.mario.entity.creature.Mario;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 游戏运行时界面顶部 UI 绘制器：
 * 统一负责绘制游戏中显示在顶部的 HUD 元素
 */
public class GameUiRenderer {
    // home 图标绘制区域，同时用于鼠标点击命中判定
    private static final Rectangle HOME_ICON_BOUNDS = new Rectangle(820, 40, 40, 40);
    private static final Font SCORE_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final Font HINT_FONT = new Font("SansSerif", Font.BOLD, 18);
    private static final Color HINT_BACKGROUND = new Color(0, 0, 0, 140);
    private static final int SCORE_X = 20;  // 积分绘制 x 坐标
    private static final int SCORE_Y = 70;  // 积分绘制 y 坐标
    private static final int HINT_BOX_X = 200;  // 提示框绘制 x 坐标
    private static final int HINT_BOX_Y = 40;  // 提示框绘制 y 坐标
    private static final int HINT_BOX_WIDTH = 500;  // 提示框宽度
    private static final int HINT_BOX_HEIGHT = 42;  // 提示框高度

    /**
     * 绘制游戏顶部 UI。
     *
     * @param graphics 画笔
     * @param mario 马里奥对象
     */
    public void draw(Graphics graphics, Mario mario, String hintText, Component observer) {
        if (graphics == null || mario == null) {
            return;
        }
        drawScore(graphics, mario);
        drawHint(graphics, hintText);
        drawHomeIcon(graphics, observer);
    }

    /**
     * 判断点击是否命中 home 图标
     */
    public boolean isHomeIconClicked(int mouseX, int mouseY) {
        return StaticValue.home != null && HOME_ICON_BOUNDS.contains(mouseX, mouseY);
    }

    /**
     * 绘制当前积分
     */
    private void drawScore(Graphics graphics, Mario mario) {
        graphics.setFont(SCORE_FONT);
        graphics.setColor(Color.WHITE);
        graphics.drawString("积分: " + mario.getScore(), SCORE_X, SCORE_Y);
    }

    /**
     * 绘制智能提示
     */
    private void drawHint(Graphics graphics, String hintText) {
        if (hintText == null || hintText.trim().isEmpty()) {
            return;
        }

        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(HINT_BACKGROUND);
        graphics2D.fillRoundRect(HINT_BOX_X, HINT_BOX_Y, HINT_BOX_WIDTH, HINT_BOX_HEIGHT, 18, 18);
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(HINT_FONT);
        FontMetrics metrics = graphics2D.getFontMetrics();
        String text = "提示: " + hintText;
        int textX = HINT_BOX_X + 16;
        int textY = HINT_BOX_Y + (HINT_BOX_HEIGHT - metrics.getHeight()) / 2 + metrics.getAscent();
        graphics2D.drawString(text, textX, textY);
        graphics2D.dispose();
    }

    /**
     * 绘制返回主菜单图标
     */
    private void drawHomeIcon(Graphics graphics, Component observer) {
        BufferedImage home = StaticValue.home;
        if (home == null) {
            return;
        }
        graphics.drawImage(
                home,
                HOME_ICON_BOUNDS.x,
                HOME_ICON_BOUNDS.y,
                HOME_ICON_BOUNDS.width,
                HOME_ICON_BOUNDS.height,
                observer
        );
    }
}
