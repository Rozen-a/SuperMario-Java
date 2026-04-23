# SuperMario-Java

[![Language](https://img.shields.io/badge/Language-Java-orange.svg)](https://www.java.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Build](https://img.shields.io/badge/Build-Maven-green.svg)](https://maven.apache.org/)

## 📖 项目简介
`SuperMario-Java` 是一个基于 Java Swing 开发的 2D 平台动作游戏，是对经典任天堂游戏《超级玛丽》的致敬与重现。本项目旨在通过 Java 编程实现游戏的核心逻辑，包括角色移动、碰撞检测、关卡加载、音效集成及动画渲染等。

---

## 🚀 核心特性
- **经典重现**：完美模拟马里奥的行走、奔跑、跳跃、踩踏敌人及收集金币等经典动作。
- **精美关卡**：预置 3 个不同难度的关卡，每个关卡都有独立的背景（bg1, bg2）、障碍物（水管、砖块、土壤）和敌人布局。
- **多样化敌人**：
  - **蘑菇怪 (Mushroom)**：基本的陆地敌人。
  - **食人花 (ManEatingFlower)**：潜伏在水管中的定时攻击者。
- **音效沉浸**：集成 JLayer 库，支持背景音乐（BGM）循环播放和即时音效（如 Game Over, Win）。
- **流畅渲染**：采用**双缓冲技术**，消除画面闪烁，并维持 33 FPS 的稳定帧率。

---

## 🛠️ 技术亮点
- **组件化设计**：将 `MarioMovementLogic` (移动逻辑) 与 `MarioCollisionDetector` (碰撞检测) 从 `Mario` 类中解耦，提高代码的可维护性。
- **多线程处理**：马里奥及各类敌人均拥有独立的更新线程，确保逻辑处理与画面渲染的分离。
- **资源中心化**：通过 `StaticValue` 类统一加载并管理所有图片资源，减少 I/O 操作并优化内存。
- **状态管理**：通过 `GameStateController` 统一协调游戏的胜负判定、关卡重置及计时器控制。

---

## 📂 项目结构
```text
SuperMario/
├── src/main/java/com/mario/
│   ├── constant/           # 静态资源与常量 (StaticValue)
│   ├── controller/         # 游戏流程控制 (GameStateController)
│   ├── entity/             # 游戏实体
│   │   ├── creature/       # 生物类 (Mario, Enemy, Mushroom, etc.)
│   │   └── scene/          # 场景元素 (Background, Obstacle, Flag, etc.)
│   ├── enums/              # 枚举类型定义 (ObstacleType, SpriteType, etc.)
│   ├── level/              # 关卡地图加载器 (LevelMapLoader)
│   ├── service/            # 核心业务逻辑 (碰撞处理、移动算法、过场动画)
│   ├── util/               # 通用工具类 (MusicPlayer)
│   └── Frame.java          # 游戏主窗口与入口
└── src/main/resources/     # 外部资源 (Images, Music)
```

---

## 🎮 操作指南
| 按键 | 功能 |
| :--- | :--- |
| `←` / `A` | 向左移动 |
| `→` / `D` | 向右移动 |
| `↑` / `Space` | 跳跃 |
| `Shift` | 加速运行 |
| `R` | 重新开始当前关卡 |

---

## 📦 快速开始
### 1. 环境准备
- **JDK 8+** (推荐使用 OpenJDK 8)
- **Maven 3.6+**

### 2. 克隆与编译
```bash
# 进入项目目录
cd SuperMario

# 编译并下载依赖
mvn clean install
```

### 3. 运行游戏
在 IDE (如 IntelliJ IDEA) 中右键运行 `com.mario.Frame` 的 `main` 方法，或者：
```bash
java -cp target/SuperMario-1.0-SNAPSHOT.jar com.mario.Frame
```

---

## 📜 许可证
本项目遵循 [MIT License](LICENSE) 协议。

## 🤝 致谢
- 感谢任天堂提供的经典游戏设计。
- 音效库支持：[JLayer](http://www.javazoom.net/javalayer/javalayer.html)。

---
**提示**：本项目仅供学习交流使用，请勿用于商业用途。
