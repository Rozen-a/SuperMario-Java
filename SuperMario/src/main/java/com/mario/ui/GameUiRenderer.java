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

    /**
     * 绘制游戏顶部 UI。
     *
     * @param graphics 画笔
     * @param mario 马里奥对象
     */
    public void draw(Graphics graphics, Mario mario, Component observer) {

        // 绘制当前积分
        if (graphics == null || mario == null) {
            return;
        }
        graphics.setFont(new Font("SansSerif", Font.BOLD, 24));
        graphics.setColor(Color.WHITE);
        graphics.drawString("积分: " + mario.getScore(), 20, 70);

        // 绘制标题
        BufferedImage home = StaticValue.home;
        if (home != null) {
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

    /**
     * 判断点击是否命中 home 图标。
     */
    public boolean isHomeIconClicked(int mouseX, int mouseY) {
        return StaticValue.home != null && HOME_ICON_BOUNDS.contains(mouseX, mouseY);
    }
}
