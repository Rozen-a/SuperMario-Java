package com.mario.ai;

import com.mario.entity.creature.Mario;

/**
 * 玩家行为事件监听器
 *
 * 用于把移动 碰撞 破坏方块等关键事件统一上报给提示系统
 */
public interface BehaviorEventListener {
    /**
     * 记录一次成功起跳
     *
     * @param mario 马里奥对象
     */
    default void onJump(Mario mario) {
    }

    /**
     * 记录一次跳跃受阻
     *
     * @param mario 马里奥对象
     */
    default void onFailedJump(Mario mario) {
    }

    /**
     * 记录一次被敌人击败
     *
     * @param mario 马里奥对象
     */
    default void onEnemyHit(Mario mario) {
    }

    /**
     * 记录一次踩死敌人
     *
     * @param mario 马里奥对象
     */
    default void onEnemyKilled(Mario mario) {
    }

    /**
     * 记录一次破坏可破坏方块
     *
     * @param mario 马里奥对象
     */
    default void onBreakableBlockBroken(Mario mario) {
    }
}
