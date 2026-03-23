package com.mario;

import com.mario.util.Background;
import com.mario.util.StaticValue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 窗口类
 */
public class Frame extends JFrame implements KeyListener {
    private List<Background> all_backgrounds = new ArrayList<>();  // 存储所有背景
    private Background now_background = new Background();  // 存储当前背景
    private Image offScreenImage = null;  // 双缓存

    public static void main(String[] args) {
        Frame frame = new Frame();
    }

    public Frame() {
        this.setSize(900, 600);  // 窗口大小设置
        this.setLocationRelativeTo(null);  // 窗口居中显示
        this.setVisible(true);  // 窗口可见性
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 关闭窗口时结束程序
        this.setResizable(false);  // 窗口大小不可变
        this.addKeyListener(this);  // 添加键盘监听
        this.setTitle("超级玛丽 SuperMario");  // 设置窗口名称

        // 初始化所有图片
        StaticValue.init();

        // 创建全部场景
        for (int i = 1; i <= 3; i++) {
            all_backgrounds.add(new Background(i, i == 3));
        }

        // 设置当前背景
        now_background = all_backgrounds.get(0);

        // 绘制图像
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = createImage(900, 600);
        }

        Graphics graphics = offScreenImage.getGraphics();
        graphics.fillRect(0, 0, 900, 600);
        graphics.drawImage(now_background.getBgImage(), 0, 0, this);
        g.drawImage(offScreenImage, 0, 0, this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
