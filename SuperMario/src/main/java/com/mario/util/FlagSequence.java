package com.mario.util;

import com.mario.entity.creature.Mario;
import com.mario.entity.scene.Flag;

/**
 * 触旗过场控制器：
 * - 马里奥触碰旗子后，先与旗子一同下落；
 * - 旗子落到指定高度停止，马里奥继续加速下落到地面；
 * - 过程中禁用常规移动逻辑（脚本模式）。
 */
public class FlagSequence {
    private static final int FLAG_STOP_Y = 390;   // 旗子停止高度
    private static final int GROUND_Y = 420;      // 地面高度

    private boolean running;            // 过场是否进行中
    private boolean slideWithFlag;      // 是否处于与旗子同步下落阶段
    private int marioFallSpeed;         // 旗子停止后马里奥的加速下落速度

    private int expand = 10;  // 旗帜碰撞箱扩展像素值

    /**
     * 每帧更新触旗过场。
     *
     * @param background 当前场景（用于获取旗子）
     * @param mario      马里奥
     */
    public void update(Background background, Mario mario) {
        if (background == null || mario == null) {
            return;
        }
        Flag flag = background.getFlag();
        if (flag == null) {
            return;
        }

        // 尚未进入过场时，检测是否触碰旗子
        if (!running && isMarioTouchingFlag(mario, flag)) {
            running = true;
            slideWithFlag = true;
            marioFallSpeed = 0;
            // 进入脚本模式并清空当前速度/输入
            mario.setScriptedMode(true);
            mario.setLeftPressed(false);
            mario.setRightPressed(false);
            mario.setRunPressed(false);
            mario.setXSpeed(0);
            mario.setYSpeed(0);
        }
        if (!running) {
            return;
        }

        // 阶段一：与旗子同步下落，直到旗子到达 FLAG_STOP_Y
        if (slideWithFlag) {
            if (flag.getY() < FLAG_STOP_Y) {
                int nextFlagY = Math.min(FLAG_STOP_Y, flag.getY() + 5);
                int deltaY = nextFlagY - flag.getY();
                flag.setY(nextFlagY);
                mario.setY(mario.getY() + deltaY);
                // 让马里奥贴近旗子右侧，数值按当前资源约束微调
                mario.setX(flag.getX() - 20);
                mario.setXSpeed(0);
                mario.setYSpeed(0);
            } else {
                slideWithFlag = false;
            }
            return;
        }

        // 阶段二：旗子停止后，马里奥加速落地
        if (mario.getY() < GROUND_Y) {
            marioFallSpeed = Math.min(marioFallSpeed + 1, 12);
            mario.setY(Math.min(GROUND_Y, mario.getY() + marioFallSpeed));
            mario.setYSpeed(marioFallSpeed);
            return;
        }

        // 过场结束，恢复常规更新
        mario.setY(GROUND_Y);
        mario.setYSpeed(0);
        mario.setScriptedMode(false);
        running = false;
    }

    /**
     * 判断马里奥是否与旗子发生接触（AABB 碰撞）。
     *
     * @param mario 马里奥
     * @param flag  旗子
     * @return true 表示接触，false 表示未接触
     */
    private boolean isMarioTouchingFlag(Mario mario, Flag flag) {
        // 保护：没有贴图时无法计算宽高，直接认为未接触
        if (mario.getShow() == null || flag.getShow() == null) {
            return false;
        }
        // 读取双方矩形（左上角坐标 + 宽高）
        int marioX = mario.getX();
        int marioY = mario.getY();
        int marioW = mario.getShow().getWidth();
        int marioH = mario.getShow().getHeight();
        // 旗子碰撞箱扩大
        int flagX = flag.getX() - expand;
        int flagY = flag.getY() - expand;
        int flagW = flag.getShow().getWidth() + expand * 2;
        int flagH = flag.getShow().getHeight() + expand * 2;
        // AABB 判定：x、y 两轴的投影都存在交集即视为接触
        return marioX < flagX + flagW && marioX + marioW > flagX
                && marioY < flagY + flagH && marioY + marioH > flagY;
    }

    /**
     * 关卡切换或重新开始时，重置过场状态。
     */
    public void reset() {
        running = false;
        slideWithFlag = false;
        marioFallSpeed = 0;
    }
}
