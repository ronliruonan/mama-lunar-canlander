# 主题资质申请：设计草稿，尚不可直接提交

制作日期：2026-10-12。用户确认现有稻穗背景没有原始 PSD，因此此处保存新制作的设计构图源文件，而不是声称恢复了原插画分层。

## 两套作品

- `rice/`：稻穗·大字农历。沿用项目已有水墨 PNG 作为栅格素材；它内部没有原画图层。锁屏 PSD 中农历、标签、时间和公历是四个可编辑文字层。壁纸 PSD 只有该素材图层，不能作为原插画的分层制作证据。
- `landscape/`：山川·清历。新增原创几何构图，纸色、日轮、远山、近山、梯田和稻穗装饰分别为六个组件图层。对应 SVG 构图源文件保存在 `vectors/`，整张背景为 `background.svg`。
- 两套 `icons.psd` 均含十二个原创示例图标，图标和名称各自分层；SVG 与 PNG 在 `icons/`。这些是设计样本，不是完整主题的全部必做图标。
- 每套分别保存 `lockscreen.psd`、`wallpaper.psd`、`icons.psd`，展示图为 `showcase.jpg`。`overview.jpg` 为两套总览，不代替两张单独作品展示图。

PSD 含真实文字层及独立组件图层；组件在 PSD 中为栅格层，对应矢量源文件可继续编辑。文字采用本机字体 `STHeitiTC-Medium`，字体文件不随包分发，接收端如缺少字体须换用合法可用的中文字体。生成库的文字引擎支持存在限制；Photoshop 可能提示更新文字层。尚未在原生 Photoshop 中验证打开、修改和重新保存，不能把独立解码通过当成该项验收。

展示图的“2026年10月9日／八月廿九”和“17:34”仅为设计示例，不是真机新测试；动态日期仍由实际主题 XML 计算。独立背景 `wallpaper.png` 不含日期。最终手机目标仍是只应用锁屏模块，原来的默认桌面及图标是否保留须实测。申请作品展示图标不代表会给父母替换桌面。

## 验证与限制

使用 ag-psd 写入并重新读取结构，再由 psd-tools 独立读取每层并重新合成，与导出 PNG 比较；记录在 `layers.json` 和 `verification.json`。单张展示图按申请页要求控制在 5MB 内。最终 ZIP 只作为可审阅草稿，大小须小于 200MB。

仍需完成：用户视觉审阅、第一套底图源文件要求是否可接受的确认、原生 PSD 编辑验收、两套作品对应的完整官方编辑器主题包、创作者介绍 PPT 和公开资料确认、锁屏独立应用及真机验证。没有上传材料、提交审核、接受协议或发布作品，也不保证平台审核通过。

重建命令（在项目根目录）：

```sh
LUNAR_NODE_MODULES=/path/to/bundled/node_modules \
LUNAR_PSD_MODULES=/path/to/node_modules/containing/ag-psd \
node theme/tools/build_qualification_drafts.cjs
python3 theme/tools/verify_qualification_drafts.py
```

构建依赖 `@napi-rs/canvas` 和 `ag-psd`；独立验证依赖 `psd-tools`、Pillow 和 NumPy。原始稻穗素材为 `theme/assets/lockscreen-background.png`。原生成脚本位于 `theme/tools/`，没有改动原素材或 Android 应用。
