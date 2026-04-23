package com.mario;

import com.mario.entity.creature.Mario;
import com.mario.entity.creature.Enemy;
import com.mario.entity.scene.Obstacle;
import com.mario.entity.scene.Flagpole;
import com.mario.entity.scene.Tower;
import com.mario.entity.scene.Background;
import com.mario.service.*;
import com.mario.controller.GameStateController;
import com.mario.constant.StaticValue;
import com.mario.util.MusicPlayer;
import com.mario.entity.scene.Flag;

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
    private int currentBackgroundIndex = 0;  // 当前关卡索引（0-based）
    private Mario mario;  // 马里奥对象
    private Image offScreenImage = null;  // 双缓存
    private Timer gameTimer;  // 主循环计时器（用于统一暂停/结束游戏）
    
    private static final int NEXT_LEVEL_TRIGGER_X = 900;  // 触发下一关的 x 阈值
    private static final int MARIO_START_X = 10;  // 切关后马里奥初始 x
    private static final int MARIO_START_Y = 420;  // 切关后马里奥初始 y

    private final FlagSequence flagSequence = new FlagSequence();  // 触旗过场控制器
    private EnemyCollisionHandler enemyCollisionHandler;  // 敌人碰撞处理器
    private GameStateController gameStateController;  // 通关/失败状态控制器

    /**
     * 程序入口
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        new Frame();
    }

    /**
     * 初始化游戏窗口与场景
     */
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

        // 初始化马里奥
        mario = new Mario(MARIO_START_X, MARIO_START_Y);
        enemyCollisionHandler = new EnemyCollisionHandler();
        gameStateController = new GameStateController(this, mario);

        loadLevel(currentBackgroundIndex);

        // 启动固定帧重绘：
        // 1) 每隔 30ms 触发一次回调（约 33 FPS），用于持续刷新游戏画面；
        // 2) 回调中调用 repaint()，会通知 Swing 在合适时机重新执行 paint(...)；
        // 3) 没有这个定时器时，界面通常只在初始化或事件触发时重绘，动画会停在静态帧。
        gameTimer = new Timer(30, e -> {
            // 统一在主循环里推进关卡切换、过场、碰撞判定与渲染
            switchToNextLevelIfNeeded();
            flagSequence.update(now_background, mario);
            enemyCollisionHandler.checkCollision(now_background, mario, gameStateController);
            gameStateController.checkWinCondition(now_background, flagSequence);
            repaint();
        });
        gameStateController.setGameTimer(gameTimer);
        // 启动计时器后，回调开始周期性执行，角色移动/跳跃状态才能连续显示出来。
        gameTimer.start();

        // 绘制图像
        repaint();

        // 初始化时开始播放 bgm
        MusicPlayer.playBGM("Ground");
    }

    /**
     * 马里奥到达右侧边界时切换到下一关（仅 1、2 关可切换）
     */
    private void switchToNextLevelIfNeeded() {
        if (mario == null || gameStateController.isGameEnded()) {
            return;
        }
        // 最后一关不触发
        if (currentBackgroundIndex >= all_backgrounds.size() - 1) {
            return;
        }
        // 马里奥未到达右侧边界，不触发切换
        if (mario.getX() < NEXT_LEVEL_TRIGGER_X) {
            return;
        }

        // 切换到下一关并加载
        loadLevel(currentBackgroundIndex + 1);
    }

    /**
     * 重置当前关卡（重新加载场景数据并复位马里奥）
     */
    private void resetCurrentLevel() {
        loadLevel(currentBackgroundIndex);
        // 重置关卡时重新播放 bgm
        MusicPlayer.playBGM("Ground");
    }

    /**
     * 加载指定关卡并统一复位场景/角色状态
     *
     * @param index 关卡索引（0-based）
     */
    private void loadLevel(int index) {
        currentBackgroundIndex = index;
        gameStateController.reset();

        // 依据当前关卡序号重建背景，恢复障碍/旗子/旗杆初始状态
        int levelSort = currentBackgroundIndex + 1;
        boolean isLastLevel = currentBackgroundIndex == all_backgrounds.size() - 1;
        Background resetBackground = new Background(levelSort, isLastLevel);
        all_backgrounds.set(currentBackgroundIndex, resetBackground);
        now_background = resetBackground;

        // 复位马里奥状态
        mario.setX(MARIO_START_X);
        mario.setY(MARIO_START_Y);
        mario.setXSpeed(0);
        mario.setYSpeed(0);
        mario.setLeftPressed(false);
        mario.setRightPressed(false);
        mario.setRunPressed(false);
        mario.setScriptedMode(false);
        mario.setBackground(now_background);

        // 复位触旗过场状态
        flagSequence.reset();
    }

    /**
     * 绘制当前帧画面（背景与障碍物）
     *
     * @param g 窗口画笔
     */
    @Override
    public void paint(Graphics g) {
        // 第一次绘制时创建离屏画布，后续复用减少对象创建
        if (offScreenImage == null) {
            offScreenImage = createImage(900, 600);
        }

        // 所有元素先绘制到离屏画布，再一次性贴到窗口，减少闪烁
        Graphics graphics = offScreenImage.getGraphics();
        // 每帧先清空画布，避免残影
        graphics.fillRect(0, 0, 900, 600);

        // 绘制当前场景背景
        graphics.drawImage(now_background.getBgImage(), 0, 0, this);

        // 绘制当前场景的敌人
        for (Enemy enemy : now_background.getEnemyList()) {
            if (enemy.getShow() != null) {
                graphics.drawImage(enemy.getShow(), enemy.getX(), enemy.getY(), this);
            }
        }

        // 在背景上叠加绘制当前场景的所有障碍物
        for (Obstacle obstacle : now_background.getObstacles()) {
            graphics.drawImage(obstacle.getShow(), obstacle.getX(), obstacle.getY(), this);
        }

        // 判空绘制旗杆（部分关卡可能没有）
        Flagpole flagpole = now_background.getFlagpole();
        if (flagpole != null) {
            graphics.drawImage(flagpole.getShow(), flagpole.getX(), flagpole.getY(), this);
        }

        // 判空绘制旗子（部分关卡可能没有）
        Flag flag = now_background.getFlag();
        if (flag != null) {
            graphics.drawImage(flag.getShow(), flag.getX(), flag.getY(), this);
        }

        // 判空绘制城堡（部分关卡可能没有）
        Tower tower = now_background.getTower();
        if (tower != null) {
            graphics.drawImage(tower.getShow(), tower.getX(), tower.getY(), this);
        }

        // 绘制马里奥
        if (mario != null) {
            graphics.drawImage(mario.getShow(), mario.getX(), mario.getY(), this);
        }

        // 将离屏画布整体绘制到窗口
        g.drawImage(offScreenImage, 0, 0, this);
    }

    /**
     * 键盘输入字符事件
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * 键盘按下事件
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (mario == null || gameStateController.isGameEnded()) {
            return;
        }
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) {
            mario.setLeftPressed(true);
        } else if (code == KeyEvent.VK_RIGHT) {
            mario.setRightPressed(true);
        } else if (code == KeyEvent.VK_SHIFT) {
            mario.setRunPressed(true);
        } else if (code == KeyEvent.VK_UP || code == KeyEvent.VK_SPACE) {
            mario.jump();
        } else if (code == KeyEvent.VK_R) {
            resetCurrentLevel();
        }
    }

    /**
     * 键盘释放事件
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (mario == null || gameStateController.isGameEnded()) {
            return;
        }
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) {
            mario.setLeftPressed(false);
        } else if (code == KeyEvent.VK_RIGHT) {
            mario.setRightPressed(false);
        } else if (code == KeyEvent.VK_SHIFT) {
            mario.setRunPressed(false);
        }
    }
}
