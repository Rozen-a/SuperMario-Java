package com.mario.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * 菜单相关界面的公共绘制工具。
 */
public final class ScreenDrawSupport {
    private ScreenDrawSupport() {
    }

    /**
     * 绘制通用按钮
     * 
     * @param graphics 绘制上下文
     * @param bounds 按钮边界矩形
     * @param text 按钮文本
     */
    public static void drawButton(Graphics graphics, Rectangle bounds, String text, int text_size) {
        graphics.setColor(new Color(255, 255, 255, 210));  // 按钮背景颜色
        graphics.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 20, 20);  // 填充按钮背景
        graphics.setColor(new Color(50, 50, 50));  // 按钮边框颜色
        graphics.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 20, 20);  // 绘制按钮边框
        graphics.setFont(new Font("SansSerif", Font.BOLD, text_size));  // 设置字体
        drawCenteredString(graphics, text, bounds.x, bounds.y + bounds.height / 2, bounds.width);  // 绘制按钮文本
    }

    /**
     * 在指定区域水平居中绘制文本
     * 
     * @param graphics 绘制上下文
     * @param text 要绘制的文本
     * @param x 基线X坐标
     * @param y 基线Y坐标（文本中心y值）
     * @param width 文本宽度
     */
    public static void drawCenteredString(Graphics graphics, String text, int x, int y, int width) {
        FontMetrics metrics = graphics.getFontMetrics();  // 获取字体元数据
        int drawX = x + (width - metrics.stringWidth(text)) / 2;  // 计算文本绘制位置
        int drawY = y + metrics.getAscent() - metrics.getHeight() / 2;  // 计算文本绘制位置
        graphics.drawString(text, drawX, drawY);  // 绘制文本
    }
}
