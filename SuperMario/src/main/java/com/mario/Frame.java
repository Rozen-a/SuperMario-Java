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
import com.mario.ui.GameUiRenderer;
import com.mario.ui.GameResultOverlay;
import com.mario.ui.StartScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    private final StartScreen startScreen = new StartScreen();  // 开始界面,内部初始状态默认为可见
    private final GameUiRenderer gameUiRenderer = new GameUiRenderer();  // 游戏运行时顶部 UI 绘制器
    private final GameResultOverlay gameResultOverlay = new GameResultOverlay();  // 结算覆盖层绘制器
    private int currentLevelStartScore = 0;  // 当前关卡的起始积分
    private int deathCount = 0;  // 当前从菜单开始的一轮挑战中累计的死亡次数
    private GameResultOverlay.OverlayType currentOverlayType = GameResultOverlay.OverlayType.NONE;  // 当前显示中的结算覆盖层类型
    
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
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  // 统一接管关闭逻辑
        this.setResizable(false);  // 窗口大小不可变
        this.addKeyListener(this);  // 添加键盘监听
        // 添加鼠标点击监听
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
        // 添加窗口关闭监听
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitGame();
            }
        });
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
        gameStateController = new GameStateController(mario);

        // 启动固定帧重绘：
        // 1) 每隔 30ms 触发一次回调（约 33 FPS），用于持续刷新游戏画面；
        // 2) 回调中调用 repaint()，会通知 Swing 在合适时机重新执行 paint(...)；
        // 3) 没有这个定时器时，界面通常只在初始化或事件触发时重绘，动画会停在静态帧。
        gameTimer = new Timer(30, e -> {
            if (startScreen.isVisible()) {
                repaint();
                return;
            }
            if (!gameStateController.isGameEnded()) {
                // 统一在主循环里推进关卡切换、过场、碰撞判定与渲染
                switchToNextLevelIfNeeded();
                flagSequence.update(now_background, mario);
                enemyCollisionHandler.checkCollision(now_background, mario, gameStateController);
                gameStateController.checkWinCondition(now_background, flagSequence);
            }

            // 同步结算覆盖层状态
            syncResultOverlayState();
            repaint();
        });
        // 启动计时器后，回调开始周期性执行，角色移动/跳跃状态才能连续显示出来。
        gameTimer.start();

        this.setVisible(true);  // 资源和状态初始化完成后再显示窗口

        // 绘制图像
        repaint();

        // 打开游戏时播放背景音乐
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
        currentLevelStartScore = mario.getScore();
        loadLevel(currentBackgroundIndex + 1);
    }

    /**
     * 重置当前关卡（重新加载场景数据并复位马里奥）。
     * 该入口用于“重新开始本关”，不会清空累计死亡次数。
     */
    private void resetCurrentLevel() {
        loadLevel(currentBackgroundIndex);
        // 重置关卡时重新播放 bgm
        MusicPlayer.playBGM("Ground");
        // 确保窗口获得焦点，响应键盘输入
        requestFocusInWindow();
        repaint();
    }

    /**
     * 从开始界面进入关卡。
     * 每次从菜单开始新一轮游戏时，都会重置死亡次数和分数进度。
     */
    private void startGame() {
        deathCount = 0;
        startScreen.hide();
        currentBackgroundIndex = 0;
        currentLevelStartScore = 0;
        loadLevel(currentBackgroundIndex);
        MusicPlayer.playBGM("Ground");
        requestFocusInWindow();  // 确保窗口获得焦点，响应键盘输入
        repaint();
    }

    /**
     * 返回主菜单，并冻结当前关卡中的角色状态。
     * 这里不主动清空死亡次数，而是在下次点击“开始”时统一重置。
     */
    private void returnToMainMenu() {
        // 冻结当前关卡中的角色状态
        if (mario != null) {
            mario.resetMotionState();
            mario.setScriptedMode(true);
        }
        // 重置游戏状态控制器
        gameStateController.reset();
        currentOverlayType = GameResultOverlay.OverlayType.NONE;
        // 重置当前关卡积分
        currentLevelStartScore = 0;
        // 显示开始界面并回到菜单页
        startScreen.show();
        repaint();
        MusicPlayer.playBGM("Ground");
    }

    /**
     * 将 GameStateController 中的结算结果同步到当前窗口的覆盖层状态
     * 由于定时器会持续回调，这里还负责保证一次死亡只累加一次计数
     */
    private void syncResultOverlayState() {
        // 已显示结算覆盖层或游戏未结束，不更新
        if (currentOverlayType != GameResultOverlay.OverlayType.NONE || !gameStateController.isGameEnded()) {
            return;
        }
        if (gameStateController.isGameOver()) {
            // 仅在首次进入失败结算态时累计一次死亡次数。
            deathCount++;
            currentOverlayType = GameResultOverlay.OverlayType.GAME_OVER;
            return;
        }
        if (gameStateController.isGameCompleted()) {
            currentOverlayType = GameResultOverlay.OverlayType.GAME_COMPLETED;
        }
    }

    /**
     * 处理鼠标点击。
     *
     * @param mouseX 鼠标 x 坐标
     * @param mouseY 鼠标 y 坐标
     */
    private void handleMouseClick(int mouseX, int mouseY) {
        if (startScreen.isVisible()) {
            // 处理开始界面点击
            StartScreen.Action action = startScreen.handleClick(mouseX, mouseY);
            if (action == StartScreen.Action.START_GAME) {
                startGame();
                return;
            }
            if (action == StartScreen.Action.EXIT_GAME) {
                exitGame();
                return;
            }
            if (action == StartScreen.Action.REPAINT) {
                repaint();
            }
            return;
        }

        if (currentOverlayType != GameResultOverlay.OverlayType.NONE) {
            // 结算覆盖层显示期间，点击优先交给覆盖层按钮处理
            GameResultOverlay.Action action = gameResultOverlay.handleClick(currentOverlayType, mouseX, mouseY);
            if (action == GameResultOverlay.Action.RESTART_LEVEL) {
                resetCurrentLevel();
                return;
            }
            if (action == GameResultOverlay.Action.RESTART_GAME) {
                startGame();
                return;
            }
            if (action == GameResultOverlay.Action.BACK_TO_MENU) {
                returnToMainMenu();
            }
            return;
        }

        // 普通运行态下，home 图标仍然保留“返回主菜单”的能力。
        if (mario != null && !gameStateController.isGameEnded()
                && gameUiRenderer.isHomeIconClicked(mouseX, mouseY)) {
            returnToMainMenu();
            return;
        }
    }

    /**
     * 统一退出游戏，确保窗口关闭时进程同时结束。
     */
    private void exitGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        dispose();  // 关闭窗口，释放资源
        System.exit(0);
    }

    /**
     * 加载指定关卡并统一复位场景/角色状态
     *
     * @param index 关卡索引（0-based）
     */
    private void loadLevel(int index) {
        currentBackgroundIndex = index;
        gameStateController.reset();
        // 每次重新加载关卡时都关闭结算覆盖层，恢复到正常游戏态。
        currentOverlayType = GameResultOverlay.OverlayType.NONE;

        // 依据当前关卡序号重建背景，恢复障碍/旗子/旗杆初始状态
        int levelSort = currentBackgroundIndex + 1;
        boolean isLastLevel = currentBackgroundIndex == all_backgrounds.size() - 1;
        Background resetBackground = new Background(levelSort, isLastLevel);
        all_backgrounds.set(currentBackgroundIndex, resetBackground);
        now_background = resetBackground;

        // 复位马里奥状态
        mario.setX(MARIO_START_X);
        mario.setY(MARIO_START_Y);
        mario.resetMotionState();
        mario.setScore(currentLevelStartScore);
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
        // 绘制开始界面（如果可见）
        if (startScreen.isVisible()) {
            startScreen.draw(graphics, this);
        } else {
            // 否则绘制游戏运行界面
            drawGameScreen(graphics);
        }

        // 将离屏画布整体绘制到窗口
        g.drawImage(offScreenImage, 0, 0, this);
        graphics.dispose();
    }

    /**
     * 绘制游戏运行界面。
     */
    private void drawGameScreen(Graphics graphics) {
        // 绘制当前场景背景
        graphics.drawImage(now_background.getBgImage(), 0, 0, this);

        // 绘制当前场景的敌人
        for (Enemy enemy : new ArrayList<>(now_background.getEnemyList())) {
            if (enemy.getShow() != null) {
                graphics.drawImage(enemy.getShow(), enemy.getX(), enemy.getY(), this);
            }
        }

        // 在背景上叠加绘制当前场景的所有障碍物
        for (Obstacle obstacle : new ArrayList<>(now_background.getObstacles())) {
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

        // 顶部 HUD 统一交给独立的 UI 绘制器处理。
        gameUiRenderer.draw(graphics, mario, this);

        // 结算时在冻结画面上层叠加 UI 覆盖层。
        if (currentOverlayType != GameResultOverlay.OverlayType.NONE) {
            gameResultOverlay.draw(graphics, currentOverlayType, mario != null ? mario.getScore() : 0, deathCount);
        }
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
        if (startScreen.isVisible() || mario == null || gameStateController.isGameEnded()) {
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
        if (startScreen.isVisible() || mario == null || gameStateController.isGameEnded()) {
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
