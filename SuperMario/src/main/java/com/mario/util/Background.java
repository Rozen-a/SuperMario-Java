package com.mario.util;

import java.awt.image.BufferedImage;

/**
 * 背景类
 */
public class Background {
    private BufferedImage bgImage = null;  // 当前场景图片
    private int sort;  // 当前场景序号
    private boolean flag;  // 判断是否到达最后一个场景

    /* Constructor */
    public Background() {
    }

    public Background(int sort, boolean flag) {
        this.sort = sort;
        this.flag = flag;

        // 最后一个场景时切换为第二个背景
        if (flag) {
            bgImage = StaticValue.background1;
        } else {
            bgImage = StaticValue.background2;
        }
    }

    /* Getter and Setter */
    public BufferedImage getBgImage() {
        return bgImage;
    }

    public void setBgImage(BufferedImage bgImage) {
        this.bgImage = bgImage;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
