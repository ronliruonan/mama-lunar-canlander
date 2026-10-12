#!/usr/bin/env python3
"""Read-only ZIP/XML audit. This does not validate a ColorOS license or engine."""
import argparse
import hashlib
import io
import json
from pathlib import Path
import xml.etree.ElementTree as ET
import zipfile


def inspect_zip(data, label):
    with zipfile.ZipFile(io.BytesIO(data)) as archive:
        result = {"path": label, "entries": len(archive.infolist()),
                  "files": sum(not x.is_dir() for x in archive.infolist()),
                  "crc_error": archive.testzip(), "xml_errors": [], "nested": []}
        for entry in archive.infolist():
            if entry.is_dir():
                continue
            payload = archive.read(entry)
            if entry.filename.endswith(".xml"):
                try:
                    root = ET.fromstring(payload)
                    if entry.filename == "themeInfo.xml":
                        result["identity"] = {name: root.findtext(name) for name in
                                              ("Author", "Summary", "UUID", "PackageName", "EditorVersion", "IsGlobalTheme")}
                except ET.ParseError as error:
                    result["xml_errors"].append({"file": entry.filename, "error": str(error)})
            if zipfile.is_zipfile(io.BytesIO(payload)):
                result["nested"].append(inspect_zip(payload, entry.filename))
        return result


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("theme", type=Path)
    args = parser.parse_args()
    data = args.theme.read_bytes()
    result = inspect_zip(data, args.theme.name)
    result["bytes"] = len(data)
    result["sha256"] = hashlib.sha256(data).hexdigest()
    print(json.dumps(result, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()
