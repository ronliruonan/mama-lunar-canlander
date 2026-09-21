# 妈妈农历日历：交接说明

最后更新：2026-09-21（Asia/Singapore）

这份文档用于切换 Codex 账号后继续工作。请先完整阅读，再改动项目。

## 项目与目标

- GitHub 仓库：<https://github.com/ronliruonan/mama-lunar-canlander>
- 本机项目目录：`/Users/liruonan/Documents/github/LunarCalendar`
- 当前目标：给父母使用的 Android 大字农历桌面卡片；正在探索锁屏显示农历。
- 设备：Mac mini M4；OnePlus 9RT（型号 MT2110），ColorOS 14 / Android 14，USB 已连接并已开启调试。
- 应用包名：`com.example.lunarcalendar`

## 已完成的桌面卡片

现有 APK 已安装并在真机验证过，桌面小部件能显示：

- 公历日期，例如 `2026年9月19日`
- 农历，例如 `八月初九`
- 星期；周六、周日仅将 `六` 或 `日` 标红
- 当日节气、节日，或下一节气倒计时

实现采用 `lunar-java 1.7.7`，完全本地计算，不需要联网。日期和节气统一以 `Asia/Shanghai`（北京时间）为准。应用已有针对春节、除夕、闰月、清明、中秋、冬至、跨时区换日和跨年午夜的 JVM 测试。

自动刷新方式：开机、改时间、改时区、应用更新时刷新；日常在北京时间下一次零点安排非唤醒闹钟，系统的 30 分钟小部件更新用于补偿。它可能稍晚于零点刷新，但不应计算出错误日期。

关键源码：

- `app/src/main/java/com/example/lunarcalendar/calendar/CalendarRepository.kt`
- `app/src/main/java/com/example/lunarcalendar/widget/LunarWidgetProvider.kt`
- `app/src/main/java/com/example/lunarcalendar/widget/WidgetRefreshScheduler.kt`
- `app/src/test/java/com/example/lunarcalendar/calendar/CalendarRepositoryTest.kt`

最近验证：Gradle 单元测试通过；Android lint 无 error；debug APK 可安装并在 OnePlus 上显示正常。

已构建的 APK 备份：`/Users/liruonan/Documents/Codex/2026-09-18/zhe/outputs/LunarCalendar-debug.apk`

## README 与 Git 状态

- README 已改为直白描述：“专为长辈设计的 Android 大字农历桌面卡片，无需联网即可使用。”
- README 已注明由 `ronliruonan` 与 OpenAI Codex 协作，Codex 是主要代码贡献者。
- README 已含桌面卡片截图：`docs/images/lunar-widget-oneplus.jpg`。
- 用户已用 GitHub Desktop 提交并推送过仓库。后续改动完成后，请让用户使用 GitHub Desktop 复核、提交和推送；此前 CLI 的 GitHub 写入认证不可用。

## 锁屏方案：已经得到的结论

用户想在锁屏上展示自定义农历。现有桌面小部件不能直接放到这台 ColorOS 14 的锁屏；新版 Android 原生锁屏小部件是 Android 16 QPR1 之后的能力，不能假定当前设备支持。

可行路径有两个：

1. 锁屏通知：可以开发，但显示位置、字号和是否可见都由系统及用户通知设置决定；不适合作为大字日历主界面。
2. 每日生成并应用“农历锁屏壁纸”：最贴合长辈需求。Android 的 `WallpaperManager` 能单独设置 `FLAG_LOCK` 锁屏壁纸，但程序不能在 Android 14 上无权限读取和复用用户当前壁纸。因此正式实现应明确让用户选择背景样式或使用应用自带背景，不能静默覆盖原有照片。

## 真机视觉验证与当前设计定稿方向

已经用静态壁纸做了真机试验。早期的居中米白卡片在通知出现时会被两条通知覆盖；把卡片整体下移后，无通知时中间显得空。

目前真机效果最好的版本是：

- 上半屏深森林绿，留给系统时钟和通知。
- 中间为深绿到米白的柔和过渡，带一枝淡金色稻穗。
- 下半屏为暖米白背景，不再有独立圆角卡片。
- 下半部居中显示大字 `八月十一`、`星期一`、分隔线和 `距秋分2天`。
- 底部保留空白给系统相机快捷按钮。
- 正式版周六或周日时，只将 `六` / `日` 标红。

用户已经在真机确认这版“nice”，可作为正式视觉方向。

当前最终静态壁纸文件（不含系统时钟、状态栏或相机图标）：

`/Users/liruonan/.codex/generated_images/01a0b2a0-2582-7dc0-9a6c-b795e5d1ad0d/exec-25ce4180-e15d-4e32-bec4-b30f263a8b3c.png`

注意：该文件只是视觉原型，固定显示 `八月十一 / 星期一 / 距秋分2天`，不会自动更新。切换账号后请先确认此本地路径仍在；若路径失效，可从本次对话中的真机截图重新参考生成。

## 推荐的下一步实现顺序

1. 在仓库创建“锁屏壁纸”功能的独立入口，明确写明“会替换锁屏壁纸”。不要自动执行。
2. 将已确认的深绿—米白—稻穗背景作为应用资源；日期、星期、节气用 Android Canvas 或 Bitmap 在每次刷新时绘制，不能把文字固定在背景图中。
3. 按本机屏幕尺寸和安全区域绘制：上方 55% 不放必要文本；农历和节气放在下方米白区域；底部为相机快捷键留空。
4. 用 `WallpaperManager.setBitmap(..., FLAG_LOCK)` 只设置锁屏背景。设置前检查 `isSetWallpaperAllowed()`；失败时给清晰提示。
5. 复用现有 `CalendarRepository` 的北京时间日期与节气计算。每天更新壁纸时要先重新计算当天内容。
6. 真机测试：无通知、两条通知、多条通知、周六/周日、节气当天、跨越北京时间零点、手机重启后。
7. 未来补充一个“恢复/重新应用”入口；因为 Android 14 限制应用读取旧锁屏壁纸，不能保证自动恢复到用户原本的照片。

## 重要产品边界

- 不要为了农历使用全屏通知或无障碍服务。
- 不要要求联网；离线计算是产品特点。
- 不要声称零点“绝对即时”更新。后台省电可能延迟任务，应在解锁、打开应用和系统时间变化时补刷。
- 用户最看重：爸妈一眼看懂、大字、农历优先、周末醒目、不要干扰微信和短信通知。
- 先保留桌面小部件，即使锁屏壁纸实现成功；桌面卡片仍是稳定、可手动刷新的备选。

## 开发环境提示

- Android SDK：`/Users/liruonan/Library/Android/sdk`
- Android Studio 内置 JDK：`/Applications/Android Studio.app/Contents/jbr/Contents/Home`
- 已知真机序列号：`56cfa1c6`
- 常用构建：在项目目录执行 `./gradlew testDebugUnitTest lintDebug assembleDebug`。
- 安装调试包：`adb -s 56cfa1c6 install -r app/build/outputs/apk/debug/app-debug.apk`
- 若需要操作 Android Studio 或手机镜像，用户此前已允许启用 OnePlus 手机镜像并做真机验证；仍应在每次会影响手机设置或壁纸时明确告知用户正在执行的动作。

## 可直接交给新 Codex 的首条消息

```text
请阅读 /Users/liruonan/Documents/github/LunarCalendar/CODEX_HANDOFF.md，并继续 /Users/liruonan/Documents/github/LunarCalendar 的锁屏农历壁纸功能。先检查当前 Git 状态和现有实现，不要覆盖用户改动。先把已确认的静态视觉方向做成可选锁屏壁纸功能，再在 OnePlus ColorOS 14 真机验证。
```
