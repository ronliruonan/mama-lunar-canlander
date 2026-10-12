#!/usr/bin/env python3
"""Prepare original input assets, NOT a final .theme; export with HeyTap editor.

Requires Pillow and the editor's current items_6.json specification. No vendor
artwork is downloaded. Text icons are a functional test design, not release art.
"""
import argparse
import json
from pathlib import Path
import zipfile
import xml.etree.ElementTree as ET

from PIL import Image, ImageDraw, ImageFont, ImageOps


ROOT = Path(__file__).resolve().parents[1]
GREEN = "#103424"
CREAM = "#F3EAD8"


def save_zip(folder, destination):
    with zipfile.ZipFile(destination, "w", zipfile.ZIP_DEFLATED) as archive:
        for path in sorted(folder.rglob("*")):
            if path.is_file():
                archive.write(path, path.relative_to(folder))


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--spec", type=Path, required=True)
    parser.add_argument("--font", type=Path, required=True)
    parser.add_argument("--package-map", type=Path, required=True,
                        help="Editor-generated allApps.xml: package names only, no artwork or theme identity")
    args = parser.parse_args()
    output = ROOT / "candidates" / "inputs"
    output.mkdir(parents=True, exist_ok=True)
    icons = output / "icons"
    specification = json.loads(args.spec.read_text())["items"]
    provenance = []
    for category in specification:
        if category["category_id"] not in (2561, 2566):
            continue
        for item in category["items"]:
            if item.get("necessary") != 1 or item.get("item_type") not in (1, 7) or not item.get("resource_path"):
                continue
            label = item["name"]
            text = {"手机搬家": "搬家", "软件商店": "应用", "主题商店": "主题",
                    "文件管理": "文件", "OPPO社区": "社区", "一加社区": "一加",
                    "云服务": "云端", "腾讯手机管家": "管家", "搜狗输入法": "搜狗",
                    "淘宝闪购": "闪购"}.get(label, label[:2])
            image = Image.new("RGBA", (240, 240))
            draw = ImageDraw.Draw(image)
            draw.rounded_rectangle((9, 9, 231, 231), radius=54, fill=GREEN)
            draw.rounded_rectangle((20, 20, 220, 220), radius=45, outline="#B8C8AA", width=3)
            font = ImageFont.truetype(str(args.font), 71)
            draw.text((120, 117), text, font=font, anchor="mm", fill=CREAM)
            # Original rice stalk detail, drawn geometrically.
            draw.line((76, 195, 164, 195), fill="#B8C8AA", width=3)
            for x in range(84, 160, 16):
                draw.ellipse((x, 182, x+9, 193), fill="#B8C8AA")
            relative = Path(item["resource_path"])
            path = icons / relative.relative_to("icons") if relative.parts[0] == "icons" else output / relative
            path.parent.mkdir(parents=True, exist_ok=True)
            image.save(path)
            provenance.append({"path": item["resource_path"], "label": label, "text": text})
    for name in ("ic_launcher_clock", "ic_launcher_calender"):
        folder = icons / "fancy_icons" / name
        images = folder / "images" / "drawable-xxhdpi"
        images.mkdir(parents=True, exist_ok=True)
        bg = Image.new("RGBA", (240, 240))
        draw = ImageDraw.Draw(bg)
        draw.rounded_rectangle((9, 9, 231, 231), radius=54, fill=GREEN)
        if name.endswith("clock"):
            draw.ellipse((39, 39, 201, 201), outline=CREAM, width=4)
            for x, y in ((120, 48), (192, 120), (120, 192), (48, 120)):
                draw.ellipse((x-3, y-3, x+3, y+3), fill=CREAM)
            bg.save(images / "clock_day_bg.png")
            for filename, top, width in (("clock_day_hour.png", 74, 8), ("clock_day_minute.png", 53, 5)):
                hand = Image.new("RGBA", (240, 240))
                hd = ImageDraw.Draw(hand)
                hd.line((120, 120, 120, top), fill=CREAM, width=width)
                hd.ellipse((114, 114, 126, 126), fill=CREAM)
                hand.save(images / filename)
            xml = '''<Icon version="1" width="240" height="240" frameRate="0" useVariableUpdater="DateTime.Minute">
<Image src="clock_day_bg.png" />
<Image src="clock_day_hour.png" centerX="120" centerY="120" angle="#hour12*30+#minute/2" />
<Image src="clock_day_minute.png" centerX="120" centerY="120" angle="#minute*6" />
</Icon>'''
        else:
            draw.text((120, 60), "日历", font=ImageFont.truetype(str(args.font), 35), anchor="mm", fill=CREAM)
            bg.save(images / "day_festival.png")
            line = Image.new("RGBA", (240, 240))
            ImageDraw.Draw(line).line((40, 88, 200, 88), fill=CREAM, width=2)
            line.save(images / "line.png")
            xml = '''<Icon version="1" width="240" height="240" frameRate="0" useVariableUpdater="DateTime.Day">
<Image src="day_festival.png" /><Image src="line.png" />
<Text x="120" y="154" align="center" alignV="center" size="87" color="#F3EAD8" textExp="#date" />
</Icon>'''
        (folder / "manifest.xml").write_text(xml)
        fallback = bg.copy()
        fd = ImageDraw.Draw(fallback)
        if name.endswith("clock"):
            fd.line((120, 74, 120, 120, 167, 120), fill=CREAM, width=6)
        else:
            fd.text((120, 155), "历", font=ImageFont.truetype(str(args.font), 76), anchor="mm", fill=CREAM)
        fallback.save(icons / "res" / "drawable-xxhdpi" / (name+".png"))
        provenance.append({"path": "icons/res/drawable-xxhdpi/"+name+".png",
                           "label": "时钟" if name.endswith("clock") else "日历",
                           "text": "原创动态图标的静态备用图"})
    # Launcher mask is an original alpha shape. The speech icon above also lives
    # in the launcher module, so it needs a separate bulk-import ZIP.
    launcher = output / "com.android.launcher" / "res" / "drawable-xxhdpi"
    launcher.mkdir(parents=True, exist_ok=True)
    mask = Image.new("RGBA", (240, 240))
    ImageDraw.Draw(mask).rounded_rectangle((9, 9, 231, 231), radius=54, fill="white")
    mask.save(launcher / "new_iphone_style_mask.png")
    with zipfile.ZipFile(output / "supplementary-assets.zip", "w", zipfile.ZIP_DEFLATED) as archive:
        for path in sorted((output / "com.android.launcher").rglob("*.png")):
            archive.write(path, path.relative_to(output))
    # Platform package-name mappings are compatibility metadata, not visual assets.
    package_map = ET.Element("iconinfo")
    available = {path.name for path in icons.rglob("*.png")} | {"ic_launcher_clock.png", "ic_launcher_calender.png"}
    for item in ET.parse(args.package_map).getroot().findall("icon"):
        if item.get("name") in available:
            ET.SubElement(package_map, "icon", item.attrib)
    ET.ElementTree(package_map).write(icons / "allApps.xml", encoding="utf-8", xml_declaration=True)
    save_zip(icons, output / "original-icons.zip")
    background = Image.open(ROOT / "assets" / "lockscreen-background.png")
    ImageOps.fit(background, (1440, 3168)).save(output / "wallpaper-1440x3168.png")
    lock = output / "lockstyle"
    # Reuse only our previously exported ratio backgrounds and lockInfo structure.
    with zipfile.ZipFile(ROOT / "exports" / "lockscreen.zip") as old:
        for name in old.namelist():
            if name.endswith("manifest.xml"):
                data = (ROOT / "candidates" / "lunar-date-only.xml").read_bytes()
            elif name.startswith("advance/default_lock_wallpaper") or name == "advance/lockInfo.xml":
                data = old.read(name)
            else:
                continue
            path = lock / name
            path.parent.mkdir(parents=True, exist_ok=True)
            path.write_bytes(data)
    save_zip(lock, output / "lunar-date-only-lockscreen.zip")
    (output / "icon-provenance.json").write_text(json.dumps(provenance, ensure_ascii=False, indent=2)+"\n")
    print(f"Prepared {len(provenance)} original static icons, 5 clock/calendar images, wallpapers and lockscreen input ZIP.")


if __name__ == "__main__":
    main()
