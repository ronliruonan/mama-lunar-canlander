# 妈妈农历日历：交接说明

最后更新：2026-10-12（Asia/Singapore）

这份文档用于切换 Codex 账号后继续工作。请先完整阅读，再改动项目。

## 2026-10-12 账号进展

用户报告已完成个人开发者认证；随后提供后台截图，确认 ColorOS 主题资质为“未申请”。申请页必填为至少两张 PNG/JPG 展示图（单张 5MB 内）和 PSD 的 ZIP/RAR 压缩包（不大于 200MB）；作品链接未标必填。实际字段与公开规范的材料区别、现有源文件缺口见 `theme/NEXT_STEPS.md`。尚未核对上传配额或免费定价能力，未代用户提交申请。免费额度为 0 本身不代表不能发布免费作品，基础配额可用于免费或付费资源。下文 10 月 9 日的设备和试用结果是历史证据，不能当作当前手机状态。保留新出现的 `output/design/` 用户背景图。

用户确认没有原始稻穗 PSD。已制作 `theme/qualification/draft-2026-10-12/` 两套新构图草稿及六个 PSD，文字可编辑，示例图标分别分层；山川图有 SVG 源文件，原稻穗 PNG 内部未分层的事实明确保留。独立逐层合成检查通过，展示 JPG 为约 0.37MB/0.17MB。尚未完成原生/兼容编辑器编辑验收、两套完整主题包或介绍 PPT，因此只交付审阅草稿，不代用户上传/提交，不保证平台审核接受。用户问是否需要 Photoshop，当前无需安装或购买。

## 2026-10-09 当前接手结果（优先于下文历史记录）

- 本轮锁屏**只做农历日期，不做节气**；已有桌面卡片、历法逻辑及用户改动保留。下文静态原型及备用方案中的节气描述是历史方向。
- 用户已同意最终目标调整为：优先只应用锁屏模块，使用用户选择的原图作背景，动态显示大字农历，保留默认桌面、图标等其他配置。此组合尚未真机验证；当前包内背景不会自动读取手机原壁纸，不能承诺在原生锁屏上仅叠加文字。
- 已重新核对设备、ADB、编辑器和官方文档。OnePlus MT2110 为 Android 14 / ColorOS 14，主题商店版本 `17.17.0beta4`，时区 `Asia/Shanghai`。
- 官方目前提供 6.0.0 Mac ARM 编辑器，本轮从官方来源取得并在 `/private/tmp/lunar-editor6/HeyTapThemeEditor.app` 运行；原 Intel 4.5.0 保留。编辑器原工程数据已备份到临时目录；项目内保存可重建输入及导出包。
- 当前布局为 `theme/candidates/lunar-date-only.xml`，以 `#month_lunar`、`#date_lunar`、`#month_lunar_leap` 拼接日期。官方 `NNNN` 在节气当天可能显示节气，历史布局 `theme/manifest.xml` 保留作对照。
- 日期候选在 2026-10-09 实体手机已由用户确认显示“农历／八月廿九”，与香港天文台年历一致。旧编辑器预览不作为日期准确性依据。
- 最新 `theme/exports/爸妈农历日期验证_6.theme` 由官方 6.0.0 编辑器导出：桌面必做 104/104、锁屏 2/2，七类资源完成，使用自有身份及原创图标／背景，`IsGlobalTheme=0`。不是借用官方主题身份或素材的成品。
- 前一日期候选试用约 16:52 应用、16:59 观察到普通 `Trial Expired`，要求恢复默认；本轮该提示没有历史 `corrupted` 措辞。历史资源损坏原因仍未证明，不能宣称补齐资源已经根治。
- 最新完整资源版约 17:28 进入试用，17:32 正常重启；用户提供了 17:34 的实体截图，仍正确显示八月廿九。本次到期行为仍待补充。无通知布局已有截图，有通知遮挡、跨午夜、闰月和长期使用均未完成。用户允许星期跟随系统语言，不为英文测试手机强制中文。
- 官方有锁屏单模块 USB／二维码预览途径，但没有查到永久免费本地应用承诺。正式路线涉及设计师资格、原创作品审核和授权获取；新账号的免费发布资格须用户登录后核实。没有登录、发布、购买或绕过试用。
- 主题商店“所有文件访问权限”本轮临时使用已结束；17:48 通过系统设置关闭，ADB 只读核对 `Uid mode: MANAGE_EXTERNAL_STORAGE: deny`，关闭截图已保存。17:50 重新打开主题商店未见最新版到期／损坏提示，不能据此推断永久授权或重启对计时的影响。
- 应用基线 `testDebugUnitTest lintDebug assembleDebug` 已成功，本次未改应用代码。详细官方链接、包哈希、假设与测试矩阵见 `theme/VALIDATION.md`。

接下来记录最新版自然到期行为，并由用户登录核对免费长期主题资格；本轮重启和临时权限清理已经完成。若该路线不能满足家庭自用，应用主动启用、自动绘制农历锁屏壁纸是可行备用，但尚未实现，仍要真机验证后台刷新。

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

此前确认的应用内路径有两个：

1. 锁屏通知：可以开发，但显示位置、字号和是否可见都由系统及用户通知设置决定；不适合作为大字日历主界面。
2. 每日生成并应用“农历锁屏壁纸”：最贴合长辈需求。Android 的 `WallpaperManager` 能单独设置 `FLAG_LOCK` 锁屏壁纸，但程序不能在 Android 14 上无权限读取和复用用户当前壁纸。因此正式实现应明确让用户选择背景样式或使用应用自带背景，不能静默覆盖原有照片。

### 2026-09-28：OPPO 动态锁屏主题真机验证

用户目前希望**优先实现农历日期**（例如 `八月十八`），二十四节气暂缓。OPPO 的[动态锁屏农历教程](https://open.oppomobile.com/bbs/forum.php?mod=viewthread&tid=2527)介绍 `DateTime format="农历NNNN"`。从[ColorOS 14 主题模板](https://open.oppomobile.com/documentation/page/info?id=10097)取出的官方示例主题中，`lockscreen/lockstyle` 包含 `advance/manifest.xml` 及多套屏幕比例的 `manifest.xml`。将该字段加进全部 11 份文件并在 OnePlus MT2110 上临时试用后，真机锁屏实时显示了红色的 **`农历八月十八`**。这证明 ColorOS 14 的锁屏引擎能渲染农历日期；测试字段位置与示例图案重叠，只是技术验证，不是可交付设计。跨北京时间零点是否自动更新尚未实测。

**安装环节仍未解决。** 保留官方示例主题原有身份时，主题商店能显示详情和“免费试用”；改用自有 `UUID`、`PackageName`（两者一致）后，详情页空白且没有应用按钮。这表明直接把自制 `.theme` 文件拷贝到“下载”目录，在当前主题商店版本上不能作为已验证的长期安装方案。不能借用官方示例主题身份发布或交给家人使用。需要继续查明官方编辑器的本地预览、主题设计师资格及自有主题上架/长期安装路径。早期[官方主题设计师认证教程](https://open.oppomobile.com/bbs/forum.php?mod=viewthread&tid=2490)说明个人开发者可以申请，但当前规则须重新核实。已从官方站点取得 Mac Intel 版主题编辑器 DMG；本机有 Rosetta，但尝试通过桌面 UI 启动时控制工具超时，尚未完成编辑器验证。

测试结束后已恢复默认主题；用户明确说**不用恢复原先的绿色静态锁屏壁纸**。主题商店“所有文件访问权限”已关闭，手机上的测试 `.theme` 文件已删除。仓库没有保存借用示例主题内容。下一步先做原创、易读的农历日期锁屏，并打通合规且持久的安装方式；如果主题渠道无法满足家庭自用需求，再回到应用每日生成壁纸的备用路线。

**后续进展（同日）：** 已安装并打开 OPPO 主题编辑器 4.5.0，制作了自有身份的原创试验包 `theme/exports/mama-lunar-coloros14.theme`；源文件和验证记录见 `theme/README.md`。这次主题商店能够识别自有主题，显示“爸妈农历主题”和“免费试用”，因此上文“自有身份主题详情页空白”的结论只适用于先前手工改包的做法。试用时 OnePlus ColorOS 14 锁屏正确显示 2026-09-28 的“农历八月十八”，编辑器模拟器却显示“八月十九”。数分钟后主题商店提示试用结束且“resource file is corrupted”，要求恢复默认资源，已按提示恢复。导出包标记为完整主题，但 `icons` 是空 ZIP，其他若干模块也是占位资源；这可能与报错有关，尚未证实。故当前只确认**单日动态农历文字可渲染**，并未解决试用稳定性、跨日刷新或长期安装。测试时临时开启的主题商店“所有文件访问权限”已再次关闭；手机“下载”目录仍有此原创试验包。先排查资源损坏提示并补齐主题资源，再继续跨日测试。

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

## 备用路线：应用每日更新锁屏壁纸

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
请阅读 /Users/liruonan/Documents/github/LunarCalendar/CODEX_HANDOFF.md 和 theme/README.md，并继续锁屏农历日期工作。先检查当前 Git 状态，不要覆盖用户改动。OPPO 主题编辑器 4.5.0 导出的原创主题已在 OnePlus ColorOS 14 真机短暂显示正确农历日期，但试用数分钟后报资源文件损坏并恢复默认资源；优先排查这一问题及长期安装路径。节气暂缓，应用每日更新锁屏壁纸是备用路线。
```
