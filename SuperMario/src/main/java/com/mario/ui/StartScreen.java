package com.mario.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * 开始界面：
 * - 管理开始菜单与操作说明页的显示状态；
 * - 负责按钮点击判定与界面绘制
 */
public class StartScreen {
    /*
     * 开始界面动作枚举，定义了界面可能的响应结果
     */
    public enum Action {
        NONE,  // 无有效操作
        REPAINT,  // 刷新界面
        START_GAME,  // 开始游戏
        EXIT_GAME  // 退出游戏
    }

    /**
     * 开始界面页状态枚举，定义了当前显示的界面页
     */
    private enum PageState {
        MENU,  // 菜单页
        INSTRUCTIONS  // 操作说明页
    }

    // 按钮边界框
    private static final Rectangle START_BUTTON_BOUNDS = new Rectangle(340, 220, 220, 50);
    private static final Rectangle INSTRUCTIONS_BUTTON_BOUNDS = new Rectangle(340, 300, 220, 50);
    private static final Rectangle EXIT_BUTTON_BOUNDS = new Rectangle(340, 380, 220, 50);
    private static final Rectangle BACK_BUTTON_BOUNDS = new Rectangle(340, 480, 220, 50);

    // 菜单页渲染器
    private final MenuPageRenderer menuPageRenderer = new MenuPageRenderer();
    // 操作说明页渲染器
    private final InstructionsPageRenderer instructionsPageRenderer = new InstructionsPageRenderer();

    // 开始界面是否可见
    private boolean visible = true;
    // 当前显示的界面页
    private PageState pageState = PageState.MENU;

    /**
     * 开始界面是否可见
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * 隐藏开始界面并重置到菜单页，便于下次重新显示
     */
    public void hide() {
        visible = false;
        pageState = PageState.MENU;
    }

    /**
     * 处理开始界面的鼠标点击
     *
     * @param mouseX 鼠标 x 坐标
     * @param mouseY 鼠标 y 坐标
     * @return 点击后的界面动作
     */
    public Action handleClick(int mouseX, int mouseY) {
        // 如果界面不可见，直接返回无有效操作
        if (!visible) {
            return Action.NONE;
        }

        // 处理菜单页点击
        if (pageState == PageState.MENU) {
            if (START_BUTTON_BOUNDS.contains(mouseX, mouseY)) {
                return Action.START_GAME;
            }
            if (INSTRUCTIONS_BUTTON_BOUNDS.contains(mouseX, mouseY)) {
                pageState = PageState.INSTRUCTIONS;
                return Action.REPAINT;
            }
            if (EXIT_BUTTON_BOUNDS.contains(mouseX, mouseY)) {
                return Action.EXIT_GAME;
            }
            return Action.NONE;
        }

        // 处理返回按钮点击
        if (BACK_BUTTON_BOUNDS.contains(mouseX, mouseY)) {
            pageState = PageState.MENU;
            return Action.REPAINT;
        }
        return Action.NONE;
    }

    /**
     * 绘制当前页面
     *
     * @param graphics 目标画笔
     * @param observer 图片观察者
     */
    public void draw(Graphics graphics, Component observer) {
        if (!visible) {
            return;
        }

        if (pageState == PageState.MENU) {
            menuPageRenderer.draw(
                    graphics,
                    observer,
                    START_BUTTON_BOUNDS,
                    INSTRUCTIONS_BUTTON_BOUNDS,
                    EXIT_BUTTON_BOUNDS
            );
        } else {
            instructionsPageRenderer.draw(graphics, observer, BACK_BUTTON_BOUNDS);
        }
    }
}
