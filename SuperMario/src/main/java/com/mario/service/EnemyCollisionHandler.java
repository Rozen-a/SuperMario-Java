package com.mario.service;

import com.mario.entity.creature.Enemy;
import com.mario.entity.creature.Mario;
import com.mario.entity.creature.Mushroom;
import com.mario.entity.scene.Background;
import com.mario.controller.GameStateController;
import com.mario.util.MusicPlayer;



/**
 * 敌人碰撞处理器：
 * - 踩中蘑菇顶部：蘑菇死亡；
 * - 其余碰撞：触发游戏失败。
 */
public class EnemyCollisionHandler {
    /**
     * 检测并处理马里奥与当前场景敌人的碰撞。
     *
     * @param background 当前场景（用于获取敌人列表）
     * @param mario 马里奥对象
     * @param gameStateController 游戏状态控制器（用于触发失败结算）
     */
    public void checkCollision(Background background, Mario mario, GameStateController gameStateController) {
        // 任一关键对象不可用或游戏已结束时，不再执行碰撞检测
        if (gameStateController == null || gameStateController.isGameEnded()
                || background == null || mario == null || mario.getShow() == null) {
            return;
        }

        // 读取马里奥碰撞箱
        int marioX = mario.getX();
        int marioY = mario.getY();
        int marioW = mario.getShow().getWidth();
        int marioH = mario.getShow().getHeight();
        int marioBottom = marioY + marioH;

        for (Enemy enemy : background.getEnemyList()) {
            // 无效敌人或未加载贴图时跳过
            if (enemy == null || enemy.getShow() == null) {
                continue;
            }
            // 蘑菇死亡后已不参与碰撞
            if (enemy instanceof Mushroom && ((Mushroom) enemy).isDead()) {
                continue;
            }
            
            // 读取敌人碰撞箱
            int enemyX = enemy.getX();
            int enemyY = enemy.getY();
            int enemyW = enemy.getShow().getWidth();
            int enemyH = enemy.getShow().getHeight();

            // 检查是否发生碰撞
            if (!isRectOverlap(marioX, marioY, marioW, marioH, enemyX, enemyY, enemyW, enemyH)) {
                continue;
            }

            // 踩头判定：必须是蘑菇，且马里奥处于下落状态，并接触蘑菇上沿附近
            boolean stompOnTop = enemy instanceof Mushroom
                    && mario.getYSpeed() > 0
                    && marioBottom <= enemyY + 12;
            if (stompOnTop) {
                // 踩中后消灭蘑菇，并给马里奥一个向上回弹，积分加2
                enemy.death();
                MusicPlayer.playSound("EnemyDeath");
                mario.addScore(2);
                mario.setY(enemyY - marioH);
                mario.setYSpeed(-10);
                return;
            }

            // 其余碰撞统一判定为马里奥死亡
            gameStateController.triggerGameOver();
            return;
        }
    }

    /**
     * 轴对齐包围盒（AABB）重叠检测。
     *
     * @return true 表示两个矩形重叠（发生碰撞）
     */
    private boolean isRectOverlap(int x1, int y1, int w1, int h1,
                                  int x2, int y2, int w2, int h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
    }
}
