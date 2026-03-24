package com.mario.entity.creature;

import com.mario.entity.type.ManEatingFlowerSpriteType;
import com.mario.util.Background;
import com.mario.util.StaticValue;

/**
 * 食人花敌人：在固定纵向区间内上下往返，并循环播放两帧动画
 */
public class ManEatingFlower extends Enemy {
    // 运动与动画参数
    private static final int MOVE_RANGE = 30;  // 上下运动范围
    private static final int MOVE_STEP = 1;  // 运动步长
    private static final int LOOP_SLEEP_MS = 50;  // 循环睡眠时间
    private static final int FRAME_SWITCH_TICKS = 6;  // 帧切换时间

    // 上下运动边界（topLimitY <= y <= bottomLimitY）
    private int topLimitY;
    private int bottomLimitY;
    // 当前是否向上移动
    private boolean movingUp = true;
    // 父类构造会启动线程；用该标记确保边界初始化后再执行主循环
    private volatile boolean initialized;

    public ManEatingFlower() {
    }

    /**
     * 创建食人花敌人对象
     *
     * @param x 食人花敌人 x 坐标
     * @param y 食人花敌人 y 坐标
     * @param toRight 食人花敌人运动方向
     * @param background 食人花敌人所在背景
     * @param type 食人花敌人图片类型
     */
    public ManEatingFlower(int x, int y, boolean toRight, Background background, ManEatingFlowerSpriteType type) {
        super(x, y, toRight, background);
        // 以初始 y 作为下边界，向上延伸固定距离作为上边界
        this.bottomLimitY = y;
        this.topLimitY = y - MOVE_RANGE;
        setShow(StaticValue.man_eating_flower.get(type.getSpriteIndex()));
        this.initialized = true;
    }

    @Override
    public void death() {
        // 食人花死亡逻辑（待补充）
    }

    @Override
    public void run() {
        int frameTick = 0;
        boolean showFirstFrame = true;

        // 等待构造函数完成边界初始化
        while (!initialized) {
            Thread.yield();
        }

        while (true) {
            // 在上下边界之间往返移动
            if (movingUp) {
                setY(Math.max(getY() - MOVE_STEP, topLimitY));
                if (getY() <= topLimitY) {
                    movingUp = false;
                }
            } else {
                setY(Math.min(getY() + MOVE_STEP, bottomLimitY));
                if (getY() >= bottomLimitY) {
                    movingUp = true;
                }
            }

            // 固定 tick 后切换一帧，形成两帧循环动画
            frameTick++;
            if (frameTick >= FRAME_SWITCH_TICKS) {
                frameTick = 0;
                showFirstFrame = !showFirstFrame;
                if (showFirstFrame) {
                    setShow(StaticValue.man_eating_flower.get(ManEatingFlowerSpriteType.FRAME_1.getSpriteIndex()));
                } else {
                    setShow(StaticValue.man_eating_flower.get(ManEatingFlowerSpriteType.FRAME_2.getSpriteIndex()));
                }
            }

            try {
                Thread.sleep(LOOP_SLEEP_MS);  // 循环睡眠时间
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
