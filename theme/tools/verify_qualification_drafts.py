#!/usr/bin/env python3
"""Read PSDs with an independent decoder and verify their component composites."""
import json
from pathlib import Path
import numpy as np
from PIL import Image
from psd_tools import PSDImage

root = Path(__file__).resolve().parents[2] / 'theme/qualification/draft-2026-10-12'
records = []
for variant in ('rice', 'landscape'):
    for name, png in (('lockscreen', 'lockscreen-preview'), ('wallpaper', 'wallpaper'), ('icons', 'icons-preview')):
        file = root / variant / f'{name}.psd'
        document = PSDImage.open(file)
        preview = Image.open(root / variant / f'{png}.png').convert('RGB')
        assert document.size == preview.size
        # Ignore the saved full-document preview: rebuild from separate layers.
        rebuilt = document.composite(force=True, ignore_preview=True).convert('RGB')
        difference = np.abs(np.asarray(preview, dtype=np.int16) - np.asarray(rebuilt, dtype=np.int16))
        mean = float(difference.mean())
        assert mean < 0.1, (str(file), mean)
        types = [layer for layer in document if layer.kind == 'type']
        if name == 'lockscreen':
            assert len(types) == 4
            assert any(layer.text.rstrip('\r\n') == '八月廿九' for layer in types)
        elif name == 'icons':
            assert len(types) == 12
            assert sum(layer.kind == 'pixel' for layer in document) == 13
        elif variant == 'landscape':
            assert len(document) == 6
        record = {'file': str(file.relative_to(root)), 'size': document.size,
                  'layers': len(document), 'text_layers': len(types),
                  'independent_composite_mean_difference': mean,
                  'independent_composite_max_difference': int(difference.max())}
        records.append(record)
        print(json.dumps(record, ensure_ascii=False))
    jpg = root / variant / 'showcase.jpg'
    assert jpg.stat().st_size < 5_000_000
    Image.open(jpg).verify()
(root / 'verification.json').write_text(json.dumps({
    'decoder': 'psd-tools', 'results': records,
    'limits': ['No native Photoshop opening/editing has been verified.',
               'This does not confirm OPPO design qualification approval.',
               'The first wallpaper contains one declared imported raster-art layer.']
}, ensure_ascii=False, indent=2))
