package com.mario.level;

import com.mario.entity.scene.Background;

/**
 * 关卡地图加载器
 */
public interface LevelMapLoader {
    /**
     * 向指定背景加载关卡地图元素
     *
     * @param background 目标背景
     */
    void load(Background background);
}
