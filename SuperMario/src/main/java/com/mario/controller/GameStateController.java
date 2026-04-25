package com.mario.controller;

import com.mario.entity.creature.Mario;
import com.mario.entity.scene.Background;
import com.mario.entity.scene.Tower;
import com.mario.service.FlagSequence;
import com.mario.util.MusicPlayer;

/**
 * 游戏状态控制器：
 * - 管理通关/失败状态；
 * - 封装通关判定；
 * - 统一处理结算时的角色冻结；
 * - 不直接负责界面绘制与按钮交互，这部分由 Frame 和结算 UI 处理。
 */
public class GameStateController {
    /**
     * 游戏结算结果枚举。
     */
    public enum ResultType {
        NONE,
        GAME_OVER,
        GAME_COMPLETED
    }

    private final Mario mario;  // 需要冻结状态的主角对象
    private ResultType resultType = ResultType.NONE;  // 当前结算结果

    /**
     * 创建游戏状态控制器。
     *
     * @param mario 马里奥对象
     */
    public GameStateController(Mario mario) {
        this.mario = mario;
    }

    /**
     * 获取当前结算结果。
     *
     * @return 当前结算状态，NONE 表示游戏仍在进行中
     */
    public ResultType getResultType() {
        return resultType;
    }

    /**
     * 检查游戏是否已达成通关。
     */
    public boolean isGameCompleted() {
        return resultType == ResultType.GAME_COMPLETED;
    }

    /**
     * 检查游戏是否已触发失败。
     */
    public boolean isGameOver() {
        return resultType == ResultType.GAME_OVER;
    }

    /**
     * 检查游戏是否已结束（通关/失败）。
     */
    public boolean isGameEnded() {
        return resultType != ResultType.NONE;
    }

    /**
     * 关卡重开、重新挑战或回到菜单前重置结算状态。
     */
    public void reset() {
        resultType = ResultType.NONE;
    }

    /**
     * 检查是否满足通关条件：
     * 1) 旗子流程完成；2) 马里奥到达城堡中心。
     * 满足条件后只标记结果并冻结角色，由外层决定如何展示结算 UI。
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

        // 马里奥中心点在城堡门内即判定通关
        // 城堡门的范围为：
        // x 轴：(城堡中心点 - 15, 城堡中心点 + 15)
        // y 轴：(城堡最低点 -45, 城堡最低点)
        int marioCenterX = mario.getX() + mario.getShow().getWidth() / 2;
        int marioCenterY = mario.getY() + mario.getShow().getHeight() / 2;
        int towerCenterX = tower.getX() + tower.getShow().getWidth() / 2;
        int towerBottomY = tower.getY() + tower.getShow().getHeight();
        // System.out.println("marioCenterX: " + marioCenterX);
        // System.out.println("marioCenterY: " + marioCenterY);
        // System.out.println("towerCenterX: " + towerCenterX);
        // System.out.println("towerBottomY: " + towerBottomY);
        // 防止马里奥贴图在门中的部分过小，缩小判定范围
        if (marioCenterX < towerCenterX - 5 || marioCenterX > towerCenterX + 5) {
            return;
        }
        if (marioCenterY < towerBottomY - 40 || marioCenterY > towerBottomY) {
            return;
        }
        
        resultType = ResultType.GAME_COMPLETED;
        // 进入结算态后立即冻结角色，确保画面停留在达成目标的瞬间。
        freezeMario();
        MusicPlayer.playBGM("Win");
    }

    /**
     * 触发失败结算（可被碰撞等多个系统复用）。
     * 失败后同样只切换状态，不直接退出程序。
     */
    public void triggerGameOver() {
        if (isGameEnded()) {
            return;
        }
        resultType = ResultType.GAME_OVER;
        // 失败后冻结角色，供上层绘制“游戏结束”覆盖层。
        freezeMario();
        MusicPlayer.playBGM("GameOver");
    }

    /**
     * 冻结马里奥状态，避免结算弹窗期间仍可移动。
     */
    private void freezeMario() {
        if (mario == null) {
            return;
        }
        mario.setScriptedMode(true);
        mario.resetMotionState();
    }
}
