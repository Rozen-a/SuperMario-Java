# SuperMario-Java

[![Language](https://img.shields.io/badge/Language-Java-orange.svg)](https://www.java.com/)
[![Build](https://img.shields.io/badge/Build-Maven-green.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 项目简介
这是一个基于 Java Swing 的 2D 平台跳跃游戏项目，核心玩法围绕马里奥移动、跳跃、踩踏敌人、破坏方块、跨关卡推进以及最终通关展开。

当前仓库的实际 Maven 工程位于 `SuperMario/` 子目录，游戏入口类为 `com.mario.Frame`。

## 当前版本特性
- 采用 Java Swing 构建固定窗口游戏界面，使用双缓冲减少闪烁
- 内置 3 个关卡，按顺序推进，最后一关支持触旗过场与进城堡通关判定
- 支持开始菜单、操作说明页、游戏中 HUD、结算覆盖层等完整界面流程
- 实现基础平台动作逻辑，包括左右移动、加速、跳跃、下落、障碍碰撞与边界处理
- 包含两类敌人：蘑菇怪与食人花
- 支持踩踏蘑菇得分、撞碎可破坏砖块得分、失败结算与重新开始本关
- 内置行为跟踪与难点分析系统，会根据玩家卡关表现给出场景提示
- 支持背景音乐与跳跃、击败敌人、破坏方块、胜利、失败等音效

## 功能说明
### 1. 关卡与流程
- 游戏启动后先进入开始界面
- 点击 `Start Game` 后从第 1 关开始
- 马里奥到达关卡右侧边界时会自动进入下一关
- 最后一关需要先完成触旗流程，再进入城堡触发通关
- 游戏失败后可重新开始本关
- 通关后可重新挑战，或返回主菜单

### 2. 操作方式
| 按键 | 功能 |
| :--- | :--- |
| `←` / `A` | 向左移动 |
| `→` / `D` | 向右移动 |
| `↑` / `W` / `Space` | 跳跃 |
| `Shift` | 加速移动 |
| `R` | 重新开始本关 |

### 3. 鼠标交互
| 位置 | 功能 |
| :--- | :--- |
| 开始菜单按钮 | 开始游戏、查看说明、退出游戏 |
| 结算界面按钮 | 重新开始本关 / 重新挑战 / 回到菜单 |
| 游戏右上角 Home 图标 | 返回主菜单 |

### 4. 积分规则
- 踩掉蘑菇怪：`+2`
- 撞碎可破坏方块：`+1`
- 分数会在关卡切换时保留
- 重新开始当前关卡时，当前关卡开始后的得分会被重置

### 5. 智能提示系统
项目包含一套轻量级玩家行为分析模块，主要由以下部分组成：
- `PlayerBehaviorTracker`：持续记录移动、跳跃、死亡、重开等行为
- `DifficultyAnalyzer`：根据停留时间、推进距离、失败跳跃、敌人命中等特征判断是否卡关
- `HintRepository`：按关卡区域提供提示文本
- `HintController`：统一控制提示触发、显示时长与冷却

提示文本会显示在游戏顶部 HUD 中，用于帮助玩家理解当前区域的通过方式。

## 技术实现
- **图形界面**：Java Swing
- **构建工具**：Maven
- **JDK 版本**：`8`
- **渲染方式**：Swing `Timer` 驱动刷新，约 33 FPS
- **碰撞处理**：AABB 碰撞检测 + 水平/垂直位移分离解析
- **资源管理**：图片与音频资源统一放在 `src/main/resources`
- **音频实现**：当前代码主要通过 Java Sound API 播放 `.wav` 资源

> 说明：`pom.xml` 中保留了 `jlayer` 依赖，但当前代码中的音频播放逻辑实际使用的是 `javax.sound.sampled`。

## 项目结构
```text
SuperMario-Java/
├── README.md
└── SuperMario/
    ├── pom.xml
    └── src/
        └── main/
            ├── java/com/mario/
            │   ├── ai/            # 行为跟踪、难点分析、提示控制
            │   ├── constant/      # 静态资源引用
            │   ├── controller/    # 游戏状态控制
            │   ├── entity/        # 角色、敌人、场景元素
            │   ├── enums/         # 枚举定义
            │   ├── level/         # 关卡装载
            │   ├── service/       # 移动、碰撞、过场逻辑
            │   ├── ui/            # 开始页、HUD、结算页绘制
            │   ├── util/          # 音频工具等
            │   └── Frame.java     # 程序入口
            └── resources/
                ├── images/        # 图片资源
                └── music/         # 音频资源
```

## 快速开始
### 1. 环境要求
- JDK 8 或更高版本
- Maven 3.6 或更高版本

### 2. 编译项目
```bash
cd SuperMario
mvn clean compile
```

当前项目已实际验证 `mvn clean compile` 可以通过。

### 3. 运行方式
推荐直接在 IDE 中运行 `com.mario.Frame` 的 `main` 方法。

也可以在完成编译后尝试使用命令行运行：

```bash
cd SuperMario
java -cp target/classes com.mario.Frame
```

## 适合阅读的核心类
- `com.mario.Frame`：程序入口、主循环、输入与界面切换
- `com.mario.entity.creature.Mario`：主角状态与动作控制
- `com.mario.service.MarioMovementLogic`：移动与重力逻辑
- `com.mario.service.MarioCollisionDetector`：障碍物碰撞与破坏方块
- `com.mario.service.EnemyCollisionHandler`：敌人碰撞与踩踏判定
- `com.mario.controller.GameStateController`：胜负状态与结算冻结
- `com.mario.ai.HintController`：智能提示流程控制

## 已知说明
- 该项目当前没有测试代码
- `mvn clean package` 在当前环境下可能因本机 Maven 仓库配置问题拉取测试插件失败，但不影响 `mvn clean compile`
- 根目录不是 Maven 工程目录，请进入 `SuperMario/` 后再执行构建命令

## 许可证
本项目遵循 [MIT License](LICENSE)。

## 致谢
- 感谢经典超级玛丽玩法带来的设计灵感
- 本项目仅用于学习与课程实践交流
