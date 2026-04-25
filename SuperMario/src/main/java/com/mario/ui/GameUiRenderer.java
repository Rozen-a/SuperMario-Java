package com.mario.ui;

import com.mario.entity.creature.Mario;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * 游戏运行时界面顶部 UI 绘制器：
 * 统一负责绘制游戏中显示在顶部的 HUD 元素
 */
public class GameUiRenderer {
    /**
     * 绘制游戏顶部 UI。
     *
     * @param graphics 画笔
     * @param mario 马里奥对象
     */
    public void draw(Graphics graphics, Mario mario) {

        // 绘制当前积分
        if (graphics == null || mario == null) {
            return;
        }
        graphics.setFont(new Font("SansSerif", Font.BOLD, 24));
        graphics.setColor(Color.WHITE);
        graphics.drawString("积分: " + mario.getScore(), 20, 70);
    }
}
