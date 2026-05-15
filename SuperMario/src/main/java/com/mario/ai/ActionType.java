package com.mario.ai;

/**
 * 玩家关键行为类型
 */
public enum ActionType {
    JUMP,  // 跳跃
    FAILED_JUMP,  // 跳跃失败
    ENEMY_HIT,  // 敌人命中
    ENEMY_KILLED,  // 敌人击杀
    BREAKABLE_BLOCK_BROKEN,  // 破坏可破坏方块
    DEATH,  // 死亡
    RESTART  // 重新开始
}
