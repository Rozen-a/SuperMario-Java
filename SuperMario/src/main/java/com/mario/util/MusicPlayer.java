package com.mario.util;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 音乐播放器工具类。
 *
 * 职责：
 * - 从 classpath 的 /music 目录加载音频；
 * - 复用已加载的 Clip，避免重复创建带来的开销；
 * - 提供背景音乐循环播放与单次音效播放能力。
 */
public class MusicPlayer {
    // 缓存池：按文件名缓存 Clip，避免反复解码同一音频
    private static final Map<String, Clip> audioCache = new HashMap<>();
    // 当前正在播放的背景音乐 Clip（同一时刻只保留一条 BGM）
    private static Clip currentBGM;

    private MusicPlayer() {
        // 工具类不需要实例化
    }

    /**
     * 从 classpath 加载音频并写入缓存。
     *
     * @param musicName 音频文件名（不含 .wav 后缀）
     * @return 加载成功返回 Clip，失败返回 null
     */
    private static Clip loadClip(String musicName) {
        if (audioCache.containsKey(musicName)) {
            return audioCache.get(musicName);
        }

        String resourcePath = "/music/" + musicName + ".wav";
        try (InputStream inputStream = MusicPlayer.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }

            // 部分音频解码器对 mark/reset 有依赖，先包一层缓冲流更稳妥
            try (BufferedInputStream bufferedIn = new BufferedInputStream(inputStream);
                 AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn)) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);

                audioCache.put(musicName, clip);
                return clip;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 播放背景音乐（自动循环）
     *
     * @param musicName 文件名（不含.wav后缀）
     */
    public static void playBGM(String musicName) {
        // 播放新 BGM 前先停止旧 BGM，确保不会重叠播放
        stopBGM();
        currentBGM = loadClip(musicName);
        if (currentBGM != null) {
            currentBGM.setFramePosition(0); // 从头开始
            currentBGM.loop(Clip.LOOP_CONTINUOUSLY); // 无限循环
        }
    }

    /**
     * 播放单次音效（如跳跃、吃金币）
     *
     * @param musicName 文件名（不含.wav后缀）
     */
    public static void playSound(String musicName) {
        Clip clip = loadClip(musicName);
        if (clip != null) {
            // 每次音效都从开头播；若该音效正在播放则打断重播
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    /**
     * 停止当前背景音乐（不清理缓存，便于后续快速恢复播放）。
     */
    public static void stopBGM() {
        if (currentBGM != null && currentBGM.isRunning()) {
            currentBGM.stop();
        }
    }
}
