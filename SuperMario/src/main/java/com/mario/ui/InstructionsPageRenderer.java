package com.mario.ui;

import com.mario.constant.StaticValue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * 操作说明页绘制器。
 */
public class InstructionsPageRenderer {
    /**
     * 绘制操作说明页。
     */
    public void draw(Graphics graphics, Component observer, Rectangle backButtonBounds) {
        // 绘制背景
        graphics.drawImage(StaticValue.Start_background, 0, 0, observer);
        graphics.setColor(new Color(0, 0, 0, 150));
        graphics.fillRect(0, 0, 900, 600);

        // 绘制标题
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("SansSerif", Font.BOLD, 50));
        ScreenDrawSupport.drawCenteredString(graphics, "操作说明", 0, 120, 900);

        // 绘制操作说明主体区域
        graphics.setColor(new Color(255, 255, 255, 70));  // 背景颜色
        graphics.fillRoundRect(200, 170, 500, 260, 20, 20); // 背景区域

        ScreenDrawSupport.drawButton(graphics, new Rectangle(230, 190, 80, 50), "← / A", 24);
        ScreenDrawSupport.drawButton(graphics, new Rectangle(230, 270, 80, 50), "→ / D", 24);
        ScreenDrawSupport.drawButton(graphics, new Rectangle(460, 190, 50, 50), "R", 24);
        ScreenDrawSupport.drawButton(graphics, new Rectangle(455, 270, 100, 50), "Shift", 24);
        ScreenDrawSupport.drawButton(graphics, new Rectangle(280, 350, 200, 50), "↑ / W / Space", 24);

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("SansSerif", Font.BOLD, 24));
        ScreenDrawSupport.drawCenteredString(graphics, "向左移动", 300, 215, 150);
        ScreenDrawSupport.drawCenteredString(graphics, "向右移动", 300, 295, 150);
        ScreenDrawSupport.drawCenteredString(graphics, "重新开始本关", 530, 215, 150);
        ScreenDrawSupport.drawCenteredString(graphics, "加速移动", 550, 295, 150);
        ScreenDrawSupport.drawCenteredString(graphics, "跳跃", 480, 375, 150);

        // 绘制返回按钮
        ScreenDrawSupport.drawButton(graphics, backButtonBounds, "返回", 24);
    }
}
