package com.mario.controller;

import com.mario.entity.creature.Mario;
import com.mario.entity.scene.Tower;
import com.mario.entity.scene.Background;
import com.mario.service.FlagSequence;
import com.mario.util.MusicPlayer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * 游戏状态控制器：
 * - 管理通关/失败状态；
 * - 封装通关判定；
 * - 统一处理“停止循环 + 弹窗 + 退出”。
 */
public class GameStateController {
    private final JFrame owner;  // 弹窗所属窗口
    private final Mario mario;  // 需要冻结状态的主角对象
    private Timer gameTimer;  // 主循环计时器（用于停止游戏更新）
    private boolean gameCompleted;  // 是否已达成通关
    private boolean gameOver;  // 是否已触发失败

    /**
     * 创建游戏状态控制器。
     *
     * @param owner 绑定的主窗口
     * @param mario 马里奥对象
     */
    public GameStateController(JFrame owner, Mario mario) {
        this.owner = owner;
        this.mario = mario;
    }

    /**
     * 注入主循环计时器，便于在结算时停止循环。
     */
    public void setGameTimer(Timer gameTimer) {
        this.gameTimer = gameTimer;
    }

    public boolean isGameCompleted() {
        return gameCompleted;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameEnded() {
        return gameCompleted || gameOver;
    }

    /**
     * 关卡重开/切关时重置状态标记。
     */
    public void reset() {
        gameCompleted = false;
        gameOver = false;
    }

    /**
     * 检查是否满足通关条件：
     * 1) 旗子流程完成；2) 马里奥到达城堡中心。
     */
    public void checkWinCondition(Background background, FlagSequence flagSequence) {
        // 已结算或必要对象为空时不做判定
        if (isGameEnded() || mario == null || background == null || flagSequence == null) {
            return;
        }
        // 必须先完成触旗流程
        if (!flagSequence.isFinished()) {
            return;
        }

        // 城堡或马里奥贴图不可用时无法计算中心点
        Tower tower = background.getTower();
        if (tower == null || tower.getShow() == null || mario.getShow() == null) {
            return;
        }

        // 马里奥中心点到达/超过城堡中心点即判定通关
        int marioCenterX = mario.getX() + mario.getShow().getWidth() / 2;
        int towerCenterX = tower.getX() + tower.getShow().getWidth() / 2;
        if (marioCenterX < towerCenterX) {
            return;
        }

        gameCompleted = true;
        MusicPlayer.playBGM("Win");
        stopAndExit("闯关成功");
    }

    /**
     * 触发失败结算（可被碰撞等多个系统复用）。
     */
    public void triggerGameOver() {
        if (isGameEnded()) {
            return;
        }
        gameOver = true;
        MusicPlayer.playBGM("GameOver");
        stopAndExit("游戏结束");
    }

    /**
     * 通用结算流程：冻结角色、停止主循环、弹窗提示并退出程序。
     *
     * @param message 弹窗提示内容
     */
    private void stopAndExit(String message) {
        freezeMario();
        if (gameTimer != null) {
            gameTimer.stop();
        }
        JOptionPane.showMessageDialog(owner, message);
        owner.dispose();
        System.exit(0);
    }

    /**
     * 冻结马里奥状态，避免结算弹窗期间仍可移动。
     */
    private void freezeMario() {
        if (mario == null) {
            return;
        }
        mario.setScriptedMode(true);
        mario.setLeftPressed(false);
        mario.setRightPressed(false);
        mario.setRunPressed(false);
        mario.setXSpeed(0);
        mario.setYSpeed(0);
    }
}
