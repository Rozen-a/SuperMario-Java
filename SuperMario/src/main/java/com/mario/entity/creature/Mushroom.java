package com.mario.entity.creature;

import com.mario.enums.MushroomSpriteType;
import com.mario.entity.scene.Background;
import com.mario.constant.StaticValue;

/**
 * 蘑菇敌人：在固定水平范围内往返移动，移动时播放行走帧，到边界后短暂停顿。
 */
public class Mushroom extends Enemy {
    // 运动与动画参数
    private static final int MOVE_STEP = 2;  // 运动步长
    private static final int LOOP_SLEEP_MS = 50;  // 循环睡眠时间
    private static final int FRAME_SWITCH_TICKS = 6;  // 帧切换时间
    private static final int ENDPOINT_PAUSE_TICKS = 12;  // 边界停顿时间

    // 左右移动边界（leftLimitX <= x <= rightLimitX）
    private int leftLimitX;
    private int rightLimitX;
    // 动画与停顿计数器
    private int frameTick; // 帧切换计数器
    private int pauseTick; // 边界停顿计数器
    // 行走帧切换标记：true->WALK_1，false->WALK_2
    private boolean walkFrameToggle;
    // 死亡后停止逻辑更新，并显示 SQUISHED
    private volatile boolean dead = false;
    // 父类构造会启动线程，使用该标记确保构造参数初始化完成后再进入主循环
    private volatile boolean initialized;

    public Mushroom() {
    }

    /**
     * 创建蘑菇敌人对象
     *
     * @param x 蘑菇敌人 x 坐标
     * @param y 蘑菇敌人 y 坐标
     * @param toRight 蘑菇敌人运动方向
     * @param background 蘑菇敌人所在背景
     * @param leftLimitX 运动左边界 x 坐标
     * @param rightLimitX 运动右边界 x 坐标
     * @param type 蘑菇敌人图片类型
     */
    public Mushroom(int x, int y, boolean toRight, Background background,
                    int leftLimitX, int rightLimitX, MushroomSpriteType type) {
        super(x, y, toRight, background);
        // 初始化时显式指定活动范围坐标
        this.leftLimitX = leftLimitX;
        this.rightLimitX = rightLimitX;
        setToRight(toRight);
        setShow(StaticValue.mushroom.get(type.getSpriteIndex()));
        this.initialized = true;  
    }

    @Override
    public void death() {
        // 标记死亡并切换为压扁帧
        dead = true;
        setShow(StaticValue.mushroom.get(MushroomSpriteType.SQUISHED.getSpriteIndex()));
        setY(getY() + 15);  // 压扁后调整位置，贴近地面
    }

    /**
     * 是否已死亡（用于外部跳过该蘑菇的碰撞检测）
     */
    public boolean isDead() {
        return dead;
    }

    @Override
    public void run() {
        // 等待构造完成，避免线程先于边界初始化执行
        while (!initialized) {
            Thread.yield();
        }

        while (true) {
            // 死亡后结束线程
            if (dead) {
                return;
            }

            // 到达端点后的停顿阶段：静止并显示站立帧
            if (pauseTick > 0) {
                pauseTick--;
                setShow(StaticValue.mushroom.get(MushroomSpriteType.STAND.getSpriteIndex()));
            } else {
                // 常规移动：按当前方向推进一步
                int nextX = isToRight() ? getX() + MOVE_STEP : getX() - MOVE_STEP;
                // 触达右边界：反向并进入停顿
                if (nextX >= rightLimitX) {
                    setX(rightLimitX);
                    setToRight(false);
                    pauseTick = ENDPOINT_PAUSE_TICKS;
                    setShow(StaticValue.mushroom.get(MushroomSpriteType.STAND.getSpriteIndex()));
                // 触达左边界：反向并进入停顿
                } else if (nextX <= leftLimitX) {
                    setX(leftLimitX);
                    setToRight(true);
                    pauseTick = ENDPOINT_PAUSE_TICKS;
                    setShow(StaticValue.mushroom.get(MushroomSpriteType.STAND.getSpriteIndex()));
                } else {
                    // 移动中循环播放 WALK_1 / WALK_2
                    setX(nextX);
                    frameTick++;
                    if (frameTick >= FRAME_SWITCH_TICKS) {
                        frameTick = 0;
                        walkFrameToggle = !walkFrameToggle;
                    }
                    setShow(StaticValue.mushroom.get(
                            walkFrameToggle
                                    ? MushroomSpriteType.WALK_1.getSpriteIndex()
                                    : MushroomSpriteType.WALK_2.getSpriteIndex()
                    ));
                }
            }

            try {
                Thread.sleep(LOOP_SLEEP_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
