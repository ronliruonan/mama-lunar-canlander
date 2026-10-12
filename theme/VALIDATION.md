# 动态农历主题：2026-10-09 验证记录

本记录区分已观察事实、原因假设和未完成测试。锁屏本轮只做农历日期，不做节气；应用已有桌面卡片及其节气功能没有改动。

用户已同意调整最终目标：优先只应用锁屏模块，使用用户选择的原图作为背景，动态显示大字农历，保留默认桌面、图标等其他配置。当前完整资源测试包尚未实现这一应用组合；也未验证在系统原生锁屏上仅叠加一行文字。官方 Wallpaper 文档（13522）说明省略 src 会查找包内默认图片，不会自动继承用户当前壁纸。

## 结论与使用边界

主题引擎可在 OnePlus MT2110 上显示动态农历文字；这不等于主题能够长期应用。2026-10-09 的日期专用候选已由用户在实体手机确认显示“农历／八月廿九”。当日一次试用随后出现普通的到期恢复提示。本地导出、ZIP 完整和编辑器必做资源全部完成，均不能证明获得永久使用授权。

最新完整资源版已通过官方编辑器 6.0.0 导出并进入真机试用，正常重启后仍显示正确日期，用户提供了实体截图。本次到期行为仍待记录。尚未完成跨午夜、闰月、多条通知和长期使用验证，因此不能交付给父母日常使用。

## 本次核对的环境

- Mac mini M4；已安装的 Intel 编辑器 4.5.0 保留。
- 从 OPPO 官方下载页取得 6.0.0 ARM 版，临时运行于 `/private/tmp/lunar-editor6/HeyTapThemeEditor.app`。不是把 4.5.0 的历史状态当作当前支持状态。
- 编辑器原工程数据在升级前备份到 `/private/tmp/lunar-editor-data-before6-20261009`。临时目录不作为长期工程备份；可重建输入和导出成品保存在本项目。
- 真机 ADB 已重新核实：MT2110，Android 14 / ColorOS 14，1080×2400，时区 `Asia/Shanghai`；主题商店 `com.heytap.themestore` 版本 `17.17.0beta4`。
- 主题商店“所有文件访问权限”仅获准本次临时使用；17:48 已通过系统设置关闭，随后 ADB 只读核对 `Uid mode: MANAGE_EXTERNAL_STORAGE: deny`。关闭界面留存于 `evidence/2026-10-09/themestore-all-files-disabled.png`。

## 官方资料核对

以下资料于 2026-10-09 查阅。网页正文加载困难时，通过官网公开文档接口读取同一文档内容；没有登录或使用账户 Cookie。

| 问题 | 官方依据及适用范围 |
| --- | --- |
| 是否必须全局主题 | [ColorOS 14 模板（10097）](https://open.oppomobile.com/documentation/page/info?id=10097)区分桌面主题与全局主题。当前选择桌面主题，包含锁屏、壁纸、图标及必做资源，`IsGlobalTheme=0`。无需为了本次测试填满全局系统模块。 |
| 是否有锁屏单模块测试 | [锁屏制作教程（10996）](https://open.oppomobile.com/documentation/page/info?id=10996)提供 USB／二维码预览，编辑器为锁屏生成带必需默认图标的主题测试包。这是官方调试入口，文档没有承诺永久本地应用；默认素材不作为本项目最终原创成品。 |
| 最新编辑器及打包检查 | [编辑器下载（11312）](https://open.oppomobile.com/documentation/page/info?id=11312)提供 6.0.0 Mac ARM 版。[新版教程（13296）](https://open.oppomobile.com/documentation/page/info?id=13296)说明必做素材检查及本机导出／手机预览；可以导出文件不代表能通过平台发布检查。 |
| `NNNN` 是否仅显示日期 | [DateTime（13524）](https://open.oppomobile.com/documentation/page/info?id=13524)定义 `NNNN` 在节气当天可能显示节气。本轮用户只要日期，因此改用农历变量拼接文本。历史布局保留用于对照。 |
| 日期专用变量 | [全局变量（13604）](https://open.oppomobile.com/documentation/page/info?id=13604)、[VarArray（13605）](https://open.oppomobile.com/documentation/page/info?id=13605)、[Text（13518）](https://open.oppomobile.com/documentation/page/info?id=13518)：月／日变量从 1 开始，数组索引从 0 开始；闰月用 `#month_lunar_leap`。 |
| 长期正规路线 | [设计师注册（10788）](https://open.oppomobile.com/documentation/page/info?id=10788)、[资质申请（13419）](https://open.oppomobile.com/documentation/page/info?id=13419)、[作品上传（10791）](https://open.oppomobile.com/documentation/page/info?id=10791)：自有身份、原创资源、设计师资格和作品审核是正式发布路径；当前没有登录、申请、发布或购买。 |
| 免费发布是否必然可用 | [免费／收费规则（13453）](https://open.oppomobile.com/documentation/page/info?id=13453)包含免费作品资格和额度规则，不能保证新账号能免费发布。[独立锁屏 SKU（11911）](https://open.oppomobile.com/documentation/page/info?id=11911)涉及母主题身份及价格关联，也不是任意本地包永久使用的承诺。 |
| 最终布局要求 | [审核规范（10100）](https://open.oppomobile.com/documentation/page/info?id=10100)需避免通知、指纹等区域冲突，并考虑系统时间显示设置。当前测试时钟固定为 `HH:mm`，发布前仍须适配 12／24 小时设置。 |

没有找到官方承诺“家庭自制主题导入后可永久免费使用”的明确条款，也没有取得此设备固定试用时长的官方依据。不要将一次观察到的分钟数当成统一规则。没有绕过试用或授权限制。

## 资源与导出证据

历史 `mama-lunar-coloros14.theme` 保留。其 `IsGlobalTheme=1`、空图标 ZIP 和占位模块确实存在；递归 ZIP CRC／XML 解析检查可以通过。历史“resource file is corrupted”提示的真正原因仍未证明，不能仅凭空图标认定。

日期专用候选为 `candidates/lunar-date-only.xml`：上部时间和公历，下部绿色大字农历；背景不含固定日期。月份和日期由主题引擎计算，主题无法调用应用的 `CalendarRepository`。当前仅在北京时间设备环境验证，异地时区是否仍按北京时间换日未证明。

输入图标由 `tools/prepare_test_assets.py` 用几何图形和文字绘制，背景复用项目原创稻穗图。`required-assets.json` 与 `icons/allApps.xml` 是必做项／应用包名兼容元数据，不含借用的主题身份或图案。图标为功能测试素材，视觉质量还未按正式发布标准验收。

| 包 | 编辑器及结果 | SHA-256 |
| --- | --- | --- |
| `exports/爸妈农历日期测试_6.theme` | 4.5.0；桌面必做项仍有一项未登记，作为日期候选测试。实体手机已确认当日日期。 | `68abcdfefca574825e74ed6617d56fe0be883a64534354428c1877e852dac4fd` |
| `exports/爸妈农历日期验证_6.theme` | 6.0.0 ARM；桌面必做 104/104、锁屏 2/2，打包清单七类全部完成，无缺失必做项提示。 | `ed24bd5946053fb2e950b7ea3da0952496bff4bd66552b8b91e091a1b89d53d0` |

两个包均由官方编辑器导出，使用自有 `UUID=PackageName=6E16BF03961E440F959B087DF09A54F2`、作者 `LunarCal`，`IsGlobalTheme=0`。`EditorVersion=6` 是主题规则字段，不能解释为只支持 ColorOS 6。

最新版补齐一加相机素材登记及编辑器生成的颜色元数据。与前一包递归比较，锁屏布局、背景和动态日期内容保持一致；新增 `assets/colors.xml`，更新图标映射、系统颜色、主题信息，没有删掉原有有效载荷。见 `evidence/2026-10-09/editor6-required-items.txt`、两个编辑器导出报告及 `exports/lunar-date-editor6-audit.json`。CRC／XML 检查只证明归档结构可读，不证明引擎稳定性、平台审核或授权。

重建输入（需要 Pillow；示例使用本机字体）：

```sh
python3 theme/tools/prepare_test_assets.py \
  --spec theme/candidates/required-assets.json \
  --package-map theme/candidates/inputs/icons/allApps.xml \
  --font '/System/Library/Fonts/STHeiti Medium.ttc'
```

脚本只生成素材与锁屏输入 ZIP；最终 `.theme` 必须再经官方编辑器导出，不能把脚本生成的 ZIP 冒充成品。

## 真机测试矩阵

| 检查 | 结果 |
| --- | --- |
| 日期准确性 | 2026-09-28 历史 `NNNN` 真机为八月十八；2026-10-09 日期变量候选由用户确认八月廿九。与[香港天文台 2026 年历](https://www.hko.gov.hk/tc/gts/time/calendar/pdf/files/2026.pdf)一致。 |
| 模拟器准确性 | 历史 9 月 28 日模拟器显示十九；10 月 9 日旧编辑器 `NNNN` 预览也有偏差。不能用编辑器日期作为农历正确性的证据。 |
| 候选包试用到期 | 约 16:52 应用，16:59 解锁观察到 `Trial Expired`，正文为 `The trial period for this resource has ended. The default resource will be restored.`；已恢复默认。此轮没有出现历史“corrupted”措辞，仍不能断定原问题已彻底解决。 |
| 最新完整资源包及重启 | 17:28 左右通过手机 Download 中的 `lunar-date-complete.theme` 进入试用；17:32 执行正常重启。用户随后提供 17:34 的实体截图，公历 2026 年 10 月 9 日、农历八月廿九，见 `evidence/2026-10-09/complete-theme-after-reboot.jpg`。 |
| 最新完整资源包到期观察 | 截至 17:50，桌面仍使用测试主题，重新打开主题商店未观察到到期或损坏提示。本轮没有再次点击试用。尚不能确定重启是否影响计时，也没有完整自然到期结果；短时有效不证明长期授权。 |
| 无通知布局及系统语言 | 上述截图没有通知卡片，农历字完整，未与底部快捷按钮重叠。星期为 `Friday`，用户确认父母设备使用中文、允许保留随系统语言显示；不强制改成中文。中文设备实际表现仍应验收。 |
| 跨午夜／农历换月 | 未验证。2026-10-09→10-10 是八月廿九→九月初一，适合后续自然跨日测试；当前短时试用不足以完成。未通过改手机时间延长或规避试用。 |
| 闰月及春节等边界 | 月日数组和闰月前缀已实现，尚未获主题引擎真机边界验证。应用单元测试不能替代主题验证。 |
| 通知区域 | 布局在中部预留区域，但有／无通知、两条／多条、指纹和最长农历文字的实体遮挡验证未完成。没有发送私信或读取私人通知做测试。 |
| 长期使用 | 未通过；已观察到本地候选试用到期。需获得官方长期授权路径后再做稳定性验收。 |

本次未修改应用 Kotlin／Android 资源。基线 `testDebugUnitTest lintDebug assembleDebug` 已成功；该结果只涵盖现有 Android 应用，不涵盖主题商店授权或主题引擎。

## 下一步与可行替代

1. 本轮重启观察和临时文件权限清理已完成。继续记录完整资源版自然到期时的原文及恢复行为；不要反复重新试用来伪装长期有效。自然跨午夜仍需合法有效的主题使用状态。
2. 若继续主题路线，由用户登录核对自有账号的设计师资格和免费作品发布资格；任何身份资料、协议接受、发布均需另行说明并由用户授权。满足条件后，经官方审核及授权获取，才能测试长期跨日、重启和通知场景。
3. 若免费长期主题渠道不适用，可回到应用自动绘制农历锁屏壁纸：主动入口明确说明替换锁屏壁纸，使用原创背景，复用北京时间农历计算，只绘制日期，不新增节气。该功能目前没有实现；ColorOS 后台调度仍需实测，不能承诺零点绝对即时刷新。现有桌面小部件继续作为已可用方案。
