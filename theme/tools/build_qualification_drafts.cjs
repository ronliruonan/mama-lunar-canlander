#!/usr/bin/env node
// Original design compositions, with real PSD text/component layers.
// Existing rice artwork is a declared raster ingredient, not reconstructed art.
const fs = require('node:fs');
const path = require('node:path');
const crypto = require('node:crypto');
const { createRequire } = require('node:module');
const bundled = process.env.LUNAR_NODE_MODULES;
const psdModules = process.env.LUNAR_PSD_MODULES;
if (!bundled || !psdModules) throw new Error('Set LUNAR_NODE_MODULES and LUNAR_PSD_MODULES');
const native = createRequire(path.join(bundled, 'package.json'))('@napi-rs/canvas');
const psd = createRequire(path.join(psdModules, 'package.json'))('ag-psd');
const { createCanvas, loadImage, GlobalFonts, ImageData } = native;
psd.initializeCanvas(createCanvas, undefined, (w, h) => new ImageData(w, h));
const fontFile = '/System/Library/Fonts/STHeiti Medium.ttc';
GlobalFonts.registerFromPath(fontFile, 'LunarHeiti');
const ROOT = path.resolve(__dirname, '../..');
const OUT = path.join(ROOT, 'theme/qualification/draft-2026-10-12');
const W = 1080, H = 2400;
const GREEN = '#103424', CREAM = '#F6F0DF', GOLD = '#BDA064';
const canvas = (w = W, h = H) => createCanvas(w, h);
const ctx = c => c.getContext('2d');
const mkdir = p => fs.mkdirSync(p, { recursive: true });
const color = hex => ({r:parseInt(hex.slice(1,3),16),g:parseInt(hex.slice(3,5),16),b:parseInt(hex.slice(5,7),16)});
const svg = body => `<svg xmlns="http://www.w3.org/2000/svg" width="${W}" height="${H}" viewBox="0 0 ${W} ${H}">${body}</svg>`;

function textLayer(name, text, x, baseline, size, fill, align='left') {
  const c = canvas(), g = ctx(c);
  g.font = `${size}px LunarHeiti`; g.fillStyle = fill; g.textAlign = align;
  g.fillText(text, x, baseline);
  // Point text transform is the baseline, independent of trimmed bitmap bounds.
  return { name, canvas:c, text:{text, transform:[1,0,0,1,x,baseline],
    orientation:'horizontal', shapeType:'point', antiAlias:'smooth',
    style:{font:{name:'STHeitiTC-Medium'},fontSize:size,fillColor:color(fill)},
    paragraphStyle:{justification:align}} };
}

async function shapeLayer(dir, name, body) {
  const content = svg(body), file = `${name}.svg`;
  fs.writeFileSync(path.join(dir,'vectors',file),content);
  const c = canvas(); ctx(c).drawImage(await loadImage(Buffer.from(content)),0,0);
  return { name,canvas:c };
}

function compose(layers, w=W, h=H) {
  const c = canvas(w,h), g = ctx(c);
  for (const layer of [...layers].reverse()) g.drawImage(layer.canvas,layer.left||0,layer.top||0);
  return c;
}

function writePsd(file, layers, w=W, h=H) {
  const composite = compose(layers,w,h);
  // ag-psd serializes children bottom-to-top; our design arrays are top-to-bottom.
  const bytes = psd.writePsdBuffer({width:w,height:h,children:[...layers].reverse(),canvas:composite},
    {trimImageData:true,generateThumbnail:true,noBackground:true});
  fs.writeFileSync(file, bytes);
  const reopened = psd.readPsd(bytes,{skipLayerImageData:true,skipCompositeImageData:true,skipThumbnail:true});
  return { composite, structure:{file:path.relative(OUT,file),bytes:bytes.length,width:w,height:h,
    sha256:crypto.createHash('sha256').update(bytes).digest('hex'),
    layers:reopened.children.map(l=>({name:l.name,kind:l.text?'editable-text':'component-bitmap',text:l.text?.text}))} };
}

const iconNames = ['电话','信息','通讯录','相机','照片','日历','时钟','设置','文件','计算器','浏览器','音乐'];
// Original primitive constructions, without using vendor icon artwork.
const iconPaths = [
  '<path d="M66 49q-15 10 2 41t37 17l12-12-20-17-10 11q-15-9-22-24l9-8-8-8Z"/>',
  '<path d="M48 48h64v48H78l-22 18V96h-8Z"/><path d="M62 65h35M62 80h25"/>',
  '<circle cx="80" cy="60" r="15"/><path d="M48 112v-8q0-28 32-28t32 28v8Z"/>',
  '<path d="M43 59h18l7-12h24l8 12h17v48H43Z"/><circle cx="80" cy="83" r="18"/>',
  '<rect x="44" y="43" width="72" height="74" rx="7"/><circle cx="66" cy="64" r="6"/><path d="m48 108 25-25 15 13 17-22 8 34"/>',
  '<rect x="45" y="49" width="70" height="66" rx="7"/><path d="M45 68h70M62 42v16M99 42v16M62 84h9M87 84h9M62 99h9M87 99h9"/>',
  '<circle cx="80" cy="80" r="36"/><path d="M80 56v25l18 12"/>',
  '<circle cx="80" cy="80" r="22"/><circle cx="80" cy="80" r="7"/><path d="M80 41v12M80 107v12M41 80h12M107 80h12M52 52l9 9M99 99l9 9M52 108l9-9M99 61l9-9"/>',
  '<path d="M43 53h30l12 12h32v46H43Z"/>',
  '<rect x="51" y="41" width="58" height="78" rx="7"/><path d="M62 55h36v17H62Z M63 88h9M88 88h9M63 104h9M88 104h9"/>',
  '<circle cx="80" cy="80" r="36"/><ellipse cx="80" cy="80" rx="16" ry="36"/><path d="M44 80h72M50 62h60M50 98h60"/>',
  '<path d="M69 102V52l40-9v50M69 66l40-9"/><ellipse cx="57" cy="106" rx="12" ry="8"/><ellipse cx="97" cy="97" rx="12" ry="8"/>',
];

async function makeIcons(dir, variant) {
  const size=160,cell=250,iw=1080,ih=1000,layers=[];
  const bg = canvas(iw,ih);ctx(bg).fillStyle=variant==='rice'?CREAM:'#EAEEDF';ctx(bg).fillRect(0,0,iw,ih);
  layers.push({name:'图标画板底色',canvas:bg});
  mkdir(path.join(dir,'icons'));
  for(let i=0;i<iconNames.length;i++){
    const x=85+(i%4)*cell,y=75+Math.floor(i/4)*300;
    const body = `<rect x="9" y="9" width="142" height="142" rx="${variant==='rice'?30:49}" fill="${GREEN}"/>`+
      (variant==='rice'?'<rect x="17" y="17" width="126" height="126" rx="24" fill="none" stroke="#BDA064" stroke-width="1.5"/>':'')+
      `<g fill="none" stroke="${variant==='rice'?CREAM:'#F4DC99'}" stroke-width="5" stroke-linecap="round" stroke-linejoin="round">${iconPaths[i]}</g>`;
    const source=`<svg xmlns="http://www.w3.org/2000/svg" width="160" height="160" viewBox="0 0 160 160">${body}</svg>`;
    fs.writeFileSync(path.join(dir,'icons',`${String(i+1).padStart(2,'0')}.svg`),source);
    const c=canvas(size,size);ctx(c).drawImage(await loadImage(Buffer.from(source)),0,0);
    fs.writeFileSync(path.join(dir,'icons',`${String(i+1).padStart(2,'0')}.png`),c.toBuffer('image/png'));
    layers.unshift({name:`${iconNames[i]}_独立图标`,canvas:c,left:x,top:y});
    const label=canvas(iw,ih),g=ctx(label);g.font='29px LunarHeiti';g.fillStyle=GREEN;g.textAlign='center';g.fillText(iconNames[i],x+80,y+207);
    layers.unshift({name:`${iconNames[i]}_名称`,canvas:label,text:{text:iconNames[i],transform:[1,0,0,1,x+80,y+207],
      style:{font:{name:'STHeitiTC-Medium'},fontSize:29,fillColor:color(GREEN)},paragraphStyle:{justification:'center'}}});
  }
  return writePsd(path.join(dir,'icons.psd'),layers,iw,ih);
}

function drawPhone(g,img,x,y,w,h) {
  g.save();g.beginPath();g.roundRect(x,y,w,h,34);g.clip();g.drawImage(img,x,y,w,h);g.restore();
  g.strokeStyle='#183E2D';g.lineWidth=3;g.beginPath();g.roundRect(x,y,w,h,34);g.stroke();
}

function board(dir,title,subtitle,lock,wall,icons) {
  const c=canvas(1600,2000),g=ctx(c);g.fillStyle='#F9F7EF';g.fillRect(0,0,1600,2000);
  g.fillStyle=GREEN;g.font='64px LunarHeiti';g.fillText(title,82,127);
  g.font='29px LunarHeiti';g.fillStyle='#526258';g.fillText(subtitle,84,182);
  g.font='29px LunarHeiti';g.fillStyle=GREEN;g.fillText('锁屏设计',84,269);g.fillText('无日期背景',880,269);
  drawPhone(g,lock,84,308,660,1467);drawPhone(g,wall,880,308,490,1089);
  g.font='29px LunarHeiti';g.fillText('原创图标设计',880,1487);
  g.drawImage(icons,865,1488,510,472);
  g.fillStyle='#526258';g.font='25px LunarHeiti';
  g.fillText('设计草稿 · 日期为展示示例 · 非真机截图',84,1880);
  g.font='22px LunarHeiti';g.fillText('展示申请作品；最终手机使用目标为独立锁屏模块。',84,1935);
  const file=path.join(dir,'showcase.jpg');fs.writeFileSync(file,c.toBuffer('image/jpeg',92));return c;
}

async function main(){
  mkdir(OUT);const records=[];const boards=[];
  for(const variant of ['rice','landscape']){
    const dir=path.join(OUT,variant);mkdir(path.join(dir,'vectors'));
    let bgLayers=[];
    if(variant==='rice'){
      const img=await loadImage(path.join(ROOT,'theme/assets/lockscreen-background.png'));
      const c=canvas();ctx(c).drawImage(img,0,0,W,H);
      bgLayers=[{name:'稻穗水墨_已有PNG底图_内部未分层',canvas:c}];
    }else{
      const cream=await shapeLayer(dir,'纸色底层',`<rect width="1080" height="2400" fill="${CREAM}"/>`);
      const sun=await shapeLayer(dir,'金色日轮','<circle cx="807" cy="1010" r="72" fill="#D8B46C"/>');
      const far=await shapeLayer(dir,'远山','<path d="M0 1190 170 1118 306 1175 465 1008 660 1156 853 1088 1080 1200v180H0Z" fill="#AEBFAB"/>');
      const mid=await shapeLayer(dir,'近山','<path d="M0 1290 158 1206 295 1260 506 1160 754 1300 918 1228 1080 1296v114H0Z" fill="#607E69"/>');
      const terrace=await shapeLayer(dir,'梯田','<path d="M0 1360q238-70 478 10t602-8v84H0Z" fill="#204D37"/><path d="M0 1405q242-64 478 10t602-8" fill="none" stroke="#C6B37D" stroke-width="4"/>');
      const stalk='<path d="M840 2350q-10-235 66-319 Q948 1970 1006 2102 M866 2200q-60-64-82-115 M887 2118q79-14 123-58" fill="none" stroke="#BDA064" stroke-width="4"/>'+
        Array.from({length:8},(_,i)=>{
          const x=926+10*i,y=2012+1.6*i*i;
          return [-1,1].map(side=>`<ellipse cx="${x+side*9}" cy="${y+side*7}" rx="6" ry="15" transform="rotate(${side*35+i*4} ${x+side*9} ${y+side*7})" fill="#BDA064"/>`).join('');
        }).join('');
      const rice=await shapeLayer(dir,'稻穗线条装饰',stalk);
      bgLayers=[rice,terrace,mid,far,sun,cream];
      fs.writeFileSync(path.join(dir,'background.svg'),svg(`<rect width="1080" height="2400" fill="${CREAM}"/><circle cx="807" cy="1010" r="72" fill="#D8B46C"/>`+
        '<path d="M0 1190 170 1118 306 1175 465 1008 660 1156 853 1088 1080 1200v180H0Z" fill="#AEBFAB"/><path d="M0 1290 158 1206 295 1260 506 1160 754 1300 918 1228 1080 1296v114H0Z" fill="#607E69"/><path d="M0 1360q238-70 478 10t602-8v84H0Z" fill="#204D37"/><path d="M0 1405q242-64 478 10t602-8" fill="none" stroke="#C6B37D" stroke-width="4"/>'+stalk));
    }
    const centered=variant==='landscape';
    const lockLayers=[
      textLayer('农历日期_展示示例_可编辑文字','八月廿九',540,1820,138,GREEN,'center'),
      textLayer('农历标签_可编辑文字','农历',540,1590,52,GREEN,'center'),
      textLayer('公历与星期_展示示例_可编辑文字','2026年10月9日 星期五',centered?540:90,550,48,centered?GREEN:CREAM,centered?'center':'left'),
      textLayer('时间_展示示例_可编辑文字','17:34',centered?540:90,430,164,centered?GREEN:CREAM,centered?'center':'left'),
      ...bgLayers];
    const lock=writePsd(path.join(dir,'lockscreen.psd'),lockLayers);
    const wall=writePsd(path.join(dir,'wallpaper.psd'),bgLayers);
    fs.writeFileSync(path.join(dir,'lockscreen-preview.png'),lock.composite.toBuffer('image/png'));
    fs.writeFileSync(path.join(dir,'wallpaper.png'),wall.composite.toBuffer('image/png'));
    const icons=await makeIcons(dir,variant);fs.writeFileSync(path.join(dir,'icons-preview.png'),icons.composite.toBuffer('image/png'));
    const title=variant==='rice'?'稻穗 · 大字农历':'山川 · 清历';
    boards.push(board(dir,title,variant==='rice'?'水墨田园 / 原创图标 / 农历大字':'几何山川 / 分层原创构图 / 农历大字',lock.composite,wall.composite,icons.composite));
    records.push({variant,title,documents:[lock.structure,wall.structure,icons.structure]});
  }
  const overview=canvas(1600,1000);const g=ctx(overview);boards.forEach((b,i)=>g.drawImage(b,i*800,0,800,1000));
  fs.writeFileSync(path.join(OUT,'overview.jpg'),overview.toBuffer('image/jpeg',92));
  fs.writeFileSync(path.join(OUT,'layers.json'),JSON.stringify({status:'design-draft-not-submitted',font:'STHeitiTC-Medium',
    rasterSource:'theme/assets/lockscreen-background.png; existing flattened artwork',
    notes:['PSD text layers are editable; other visual components are separately rasterized from included SVG sources.',
    'No native Photoshop verification or OPPO approval has been performed.',
    'Preview dates are examples, not baked into wallpaper PNGs.','Icons are 12 design samples, not a complete exported theme.'],works:records},null,2));
  console.log(JSON.stringify(records.map(r=>({variant:r.variant,documents:r.documents.map(d=>({file:d.file,bytes:d.bytes,layers:d.layers.length}))})),null,2));
}
main().catch(e=>{console.error(e);process.exitCode=1;});
