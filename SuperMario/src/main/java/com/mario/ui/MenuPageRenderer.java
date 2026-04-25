package com.mario.ui;

import com.mario.constant.StaticValue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

/**
 * 开始菜单页绘制器。
 */
public class MenuPageRenderer {
    /**
     * 绘制开始菜单页。
     */
    public void draw(Graphics graphics, Component observer, Rectangle startButtonBounds,
                     Rectangle instructionsButtonBounds, Rectangle exitButtonBounds) {
        // 绘制背景
        graphics.drawImage(StaticValue.Start_background, 0, 0, observer);
        graphics.setColor(new Color(0, 0, 0, 120));
        graphics.fillRect(0, 0, 900, 600);

        // 绘制标题
        BufferedImage logo = StaticValue.logo;
        if (logo != null) {
            double scale = 0.2;  // 缩放比例
            int logoW = (int) (logo.getWidth() * scale);
            int logoH = (int) (logo.getHeight() * scale);
            int logoX = (900 - logoW) / 2;  // 居中显示
            graphics.drawImage(logo, logoX, 100, logoW, logoH, observer);
        } else {  // 如果logo加载失败，绘制文本
            graphics.setColor(Color.WHITE);
            ScreenDrawSupport.drawCenteredString(graphics, "超级玛丽", 0, 150, 900);
        }

        // 按钮
        ScreenDrawSupport.drawButton(graphics, startButtonBounds, "开始", 24);
        ScreenDrawSupport.drawButton(graphics, instructionsButtonBounds, "操作说明", 24);
        ScreenDrawSupport.drawButton(graphics, exitButtonBounds, "退出游戏", 24);
    }
}
