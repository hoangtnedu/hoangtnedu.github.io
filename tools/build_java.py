"""Build Java course from Markdown and the 20-session JSON schedule.
Python 3 standard library only. Run: python3 tools/build_java.py
Supported Markdown: paragraphs, ## headings, - lists, links, bold, inline/fenced code.
"""
from pathlib import Path
import html,json,re,unicodedata,xml.etree.ElementTree as ET

ROOT=Path(__file__).resolve().parents[1]
SRC=ROOT/'content/java'
OUT=ROOT/'java'
OUT.mkdir(exist_ok=True)
E=html.escape
chapters=['Tổng quan và phát triển ứng dụng Java','Ứng dụng desktop với Java SE','Ứng dụng đa lớp với Jakarta EE','Phát triển ứng dụng với Spring Framework']
lessons=json.loads((SRC/'lich-trinh.json').read_text())
counter=0

def normalize(s):
    return ''.join(c for c in unicodedata.normalize('NFD',s.lower().replace('đ','d')) if not unicodedata.combining(c))

def inline(s):
    # Protect inline code before applying emphasis and links.
    parts=re.split(r'(`[^`]+`)',s)
    result=[]
    for part in parts:
        if part.startswith('`') and part.endswith('`'):
            result.append('<code>'+E(part[1:-1])+'</code>')
        else:
            part=E(part)
            part=re.sub(r'\*\*([^*]+)\*\*',r'<strong>\1</strong>',part)
            part=re.sub(r'\[([^\]]+)\]\(([^)]+)\)',r'<a href="\2">\1</a>',part)
            result.append(part)
    return ''.join(result)

def code(text,lang):
    global counter
    counter+=1
    ident='code-'+str(counter)
    return f'<div class="code-wrap"><button class="copy" type="button" data-target="{ident}">Sao chép</button><pre><code id="{ident}" class="language-{E(lang)}">{E(text.strip(chr(10)))}</code></pre></div>'

def markdown(text):
    result=[];paragraph=[];items=[];lines=text.splitlines();i=0;heading=0
    def flush():
        if paragraph: result.append('<p>'+inline(' '.join(paragraph))+'</p>');paragraph.clear()
        if items: result.append('<ul>'+''.join('<li>'+inline(x)+'</li>' for x in items)+'</ul>');items.clear()
    while i<len(lines):
        line=lines[i]
        if line.startswith('```'):
            flush();lang=line[3:];buf=[];i+=1
            while i<len(lines) and not lines[i].startswith('```'):buf.append(lines[i]);i+=1
            if i==len(lines):raise ValueError('Unclosed code fence')
            result.append(code('\n'.join(buf),lang))
        elif line.startswith('## '):
            flush();heading+=1;result.append(f'<h2 id="muc-{heading}">'+inline(line[3:])+'</h2>')
        elif line.startswith('- '):
            if paragraph:flush()
            items.append(line[2:])
        elif not line.strip():flush()
        else:
            if items:flush()
            paragraph.append(line.strip())
        i+=1
    flush();return '\n'.join(result)

def sidebar(active):
    links=[('index.html','Tổng quan và lịch trình 20 buổi')]+[(f'chuong-{i}.html',f'Chương {i}. {t}') for i,t in enumerate(chapters,1)]+[('lab-01.html','Lab 1. Console, Maven và JAR'),('bai-tap.html','Bài tập theo buổi'),('project.html','Hướng dẫn review project'),('tai-lieu.html','Tài liệu tham khảo')]
    links += [(f'buoi-{l["number"]:02}.html',f'Buổi {l["number"]}. {l["title"]}') for l in lessons]
    toc=''.join(f'<li data-search="{E(normalize(t))}"><a href="{p}"'+(' class="active" aria-current="page"' if p==active else '')+'>'+E(t)+'</a></li>' for p,t in links)
    return '<aside class="sidebar" id="navigation" aria-label="Lộ trình Java"><div class="profile"><strong>Trần Nguyên Hoàng</strong><p>Ghi chép lập trình · HoangTN</p><a href="../index.html">← Trang chủ</a></div><h2>Công nghệ Java</h2><label class="tiny" for="toc-search">Tìm chương hoặc buổi</label><input type="search" id="toc-search" placeholder="Swing, JDBC, buổi 12…"><ul class="toc-list">'+toc+'</ul><p class="empty-result" hidden>Không có mục phù hợp.</p></aside>'

def page(path,title,body,description,previous=None,following=None):
    nav='<nav class="lesson-nav" aria-label="Bài trước và bài sau">'
    for target,label in [x for x in [previous,following] if x]:nav+=f'<a href="{target}">{E(label)}</a>'
    nav+='</nav>'
    headings=re.findall(r'<h2 id="([^"]+)">(.+?)</h2>',body)
    contents='<details class="page-toc"><summary>Trong bài này</summary><ol>'+''.join(f'<li><a href="#{a}">{b}</a></li>' for a,b in headings)+'</ol></details>' if headings else ''
    url='https://hoangtnedu.github.io/java/'+('' if path=='index.html' else path)
    doc=f'''<!doctype html><html lang="vi"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>{E(title)} | HoangTN</title><meta name="author" content="Trần Nguyên Hoàng"><meta name="description" content="{E(description)}"><meta property="og:title" content="{E(title)} | HoangTN"><meta property="og:description" content="{E(description)}"><meta property="og:type" content="article"><meta property="og:url" content="{url}"><link rel="canonical" href="{url}"><link rel="icon" href="../assets/favicon.svg" type="image/svg+xml"><link rel="stylesheet" href="../assets/css/site.css"><link rel="stylesheet" href="../assets/css/java.css"><script defer src="../assets/js/site.js"></script><script defer src="../assets/js/java.js"></script></head><body class="java"><a class="skip" href="#noi-dung">Đến nội dung</a><header class="top"><a class="brand" href="../index.html">HoangTN<span>Nghiên cứu và lập trình</span></a><nav aria-label="Điều hướng chính"><a href="../index.html">Trang chủ</a><a href="../dotnet/">Lập trình .NET</a><a href="index.html" aria-current="page">Công nghệ Java</a><button class="mobile-nav" type="button" aria-controls="navigation" aria-expanded="false">Mục lục</button></nav></header><div class="progress" aria-hidden="true"></div><div class="layout">{sidebar(path)}<main class="article" id="noi-dung"><p class="eyebrow">HoangTN · Công nghệ Java</p><h1>{E(title)}</h1><div class="meta"><span>Trần Nguyên Hoàng</span><span>Cập nhật 27/10/2026</span></div>{contents}{body}{nav}</main></div><footer>© 2026 Trần Nguyên Hoàng · HoangTN · Ghi chép nghiên cứu và học tập.</footer><button class="top-button" type="button" aria-label="Về đầu trang">↑</button></body></html>'''
    (OUT/path).write_text(doc)

def table():
    rows=''.join(f'<tr><td><a href="buoi-{l["number"]:02}.html">Buổi {l["number"]}</a></td><td>{E(l["chapter"])}<br>{E(l["title"])}</td><td>{E(l["mode"])}</td><td>{E(l["scheduled_output"])}</td></tr>' for l in lessons)
    return '<label class="filter-label" for="lesson-filter">Lọc theo buổi, chương hoặc công nghệ</label><input class="lesson-filter" id="lesson-filter" type="search" placeholder="Ví dụ: Swing, JPA, review…"><p class="source" id="lesson-count" aria-live="polite">20 buổi phù hợp</p><div class="table-scroll"><table id="lesson-table"><thead><tr><th>Buổi</th><th>Nội dung</th><th>Hình thức</th><th>Sản phẩm theo lịch trình</th></tr></thead><tbody>'+rows+'</tbody></table></div><p id="lesson-empty" hidden>Không có buổi phù hợp.</p>'

for i,title in enumerate(chapters,1):
    body=markdown((SRC/f'chuong-{i}.md').read_text())
    page(f'chuong-{i}.html',f'Chương {i}. {title}',body,title,('index.html','← Lộ trình') if i==1 else (f'chuong-{i-1}.html',f'← Chương {i-1}'),('project.html','Review project →') if i==4 else (f'chuong-{i+1}.html',f'Chương {i+1} →'))

for slug,title in [('lab-01','Lab 1. Java SE Console, Maven và JAR'),('project','Project: bốn buổi review và bàn giao'),('tai-lieu','Tài liệu và nguồn tham khảo')]:
    body=markdown((SRC/(slug+'.md')).read_text())
    if slug=='lab-01':
        body+='<h2 id="ma-nguon">Mã nguồn để đọc và sao chép</h2>'
        for name,lang in [('pom.xml','xml'),('src/main/java/io/github/hoangtnedu/java/So.java','java'),('src/main/java/io/github/hoangtnedu/java/App.java','java')]:
            body+='<h3>'+E(name)+'</h3>'+code((OUT/'examples/lab01'/name).read_text(),lang)
    page(slug+'.html',title,body,title,('index.html','← Lộ trình'))

for l in lessons:
    n=l['number'];c=int(l['chapter'][-1]) if n<=16 else None
    theory=f'chuong-{c}.html' if c else 'project.html'
    note='Năm bài tập Lab 1 lấy từ tệp lab được cung cấp; xem hướng dẫn đầy đủ và mã nguồn.' if n in [1,2] else 'Bài thực hành bổ sung được biên soạn theo nội dung và sản phẩm trong lịch trình.' if n<=16 else 'Buổi review theo lịch trình; đối chiếu hướng dẫn project và ghi lại góp ý sau mỗi lần review.'
    md=f'''## Nội dung theo lịch trình

{l['topic']}

Hình thức: {l['mode']}. Hoạt động: {l['activity']}

## Trọng tâm cần hiểu

{l['explanation']}

## Các bước thực hiện

'''+ '\n'.join('- '+s for s in l['steps'])+f'''

## Kiểm tra kết quả

{l['checks']}

## Sản phẩm cần đạt

Theo lịch trình gốc: {l['scheduled_output']}

Gói thực hành: {l['deliverable']}

## Tự giải thích sau buổi học

- Mô tả luồng dữ liệu của chức năng vừa làm và chỉ ra lớp chịu trách nhiệm từng bước.
- Nêu một ca lỗi đã thử, kết quả thực tế và cách khắc phục.
- Ghi phần còn chưa hoàn thành cùng hành động tiếp theo.

## Đọc và thực hành liên quan

- [Lý thuyết liên quan]({theory})
- [Lab 1: Console và Maven](lab-01.html)
- [Nguồn tham khảo](tai-lieu.html)
'''
    body='<div class="callout"><p>'+E(note)+'</p></div>'+markdown(md)
    page(f'buoi-{n:02}.html',f'Buổi {n}. {l["title"]}',body,l['topic'],('index.html','← Lộ trình') if n==1 else (f'buoi-{n-1:02}.html',f'← Buổi {n-1}'),('project.html','Tổng kết project →') if n==20 else (f'buoi-{n+1:02}.html',f'Buổi {n+1} →'))

cards=''.join(f'<a class="chapter-card" href="chuong-{i}.html"><span>Chương {i:02}</span><h2>{E(t)}</h2><p>'+['Buổi 1–2 · Java Platform, JDK/JVM, Maven, JAR.','Buổi 3–6 · Swing, sự kiện, EDT, JDBC và MVC.','Buổi 7–12 · CDI, Servlet/JSP, Faces, JPA và bảo mật.','Buổi 13–16 · Boot, MVC, Thymeleaf, Data JPA, Security.'][i-1]+'</p></a>' for i,t in enumerate(chapters,1))
intro='<p class="lede">Từ ứng dụng Java Console đến desktop và web nhiều lớp, học theo lịch trình 20 buổi với mã minh họa, bài tập và sản phẩm sau từng buổi.</p><div class="course-stats"><span><strong>04</strong> chương lý thuyết</span><span><strong>20</strong> buổi học và review</span><span><strong>05</strong> bài toán Lab 1</span></div><div class="downloads"><a class="download" href="chuong-1.html">Bắt đầu Chương 1 →</a><a class="download" href="lab-01.html">Làm Lab 1</a><a class="download" href="bai-tap.html">Bài tập theo buổi</a></div><h2 id="lo-trinh">Lộ trình kiến thức</h2><div class="chapter-grid">'+cards+'</div><h2 id="moi-truong">Chuẩn bị trước khi học</h2><p>Nắm biến, điều kiện, vòng lặp, lớp và SQL cơ bản. Java SE dùng JDK 17 hoặc 21 và Maven 3.x. Phần Jakarta EE chọn runtime Web Profile tương thích; phần Spring dùng project riêng theo cấu hình Boot 3.5.x được nêu trong bài. Các ví dụ Java chạy trên máy người học.</p><h2 id="lich-trinh">Lịch trình 20 buổi</h2>'+table()+'<h2 id="project">Bốn buổi cuối: review project</h2><p>Buổi 17: phân tích và thiết kế. Buổi 18: kiến trúc và chức năng lõi. Buổi 19: chất lượng, bảo mật và báo cáo. Buổi 20: demo và bàn giao. Xem <a href="project.html">yêu cầu và sản phẩm từng lần review</a>.</p><h2 id="hoc-lieu">Học liệu và bài tập</h2><p>Khung dựa trên lịch trình giảng dạy. Năm bài Console giữ theo Lab 1 đã cung cấp; nhiệm vụ thực hành các buổi còn lại được biên soạn bổ sung và ghi rõ nguồn gốc. Lý thuyết viết lại từ giáo trình, sách tham khảo và đối chiếu tài liệu kỹ thuật chính thức. Xem <a href="tai-lieu.html">danh mục nguồn</a>.</p>'
page('index.html','Công nghệ Java',intro,'4 chương Công nghệ Java theo 20 buổi: Maven, Swing, JDBC, Jakarta EE, Spring và review project.')

tasks='<p class="lede">Làm Lab 1 trước, sau đó thực hành theo từng buổi để phát triển ứng dụng desktop và web.</p><div class="callout"><p>Lab 1 gồm 5 bài toán từ tệp lab được cung cấp. Các nhiệm vụ ở buổi 3–16 là phần bổ sung theo lịch trình. Buổi 17–20 dành cho review project.</p></div><div class="downloads"><a class="download" href="lab-01.html">Mở Lab 1 và mã nguồn →</a><a class="download" href="project.html">Hướng dẫn project</a></div>'+table()+'<h2 id="nop-bai">Cách ghi kết quả thực hành</h2><p>Mỗi buổi lưu mã nguồn, cấu hình mẫu, dữ liệu giả lập và README. Ghi ca thử, kết quả mong đợi/thực tế, ảnh hoặc log phù hợp. Không đưa mật khẩu, dữ liệu cá nhân hoặc thư mục build vào repository.</p>'
page('bai-tap.html','Bài tập và sản phẩm theo từng buổi',tasks,'Lab 1 với năm bài Console và bài thực hành bổ sung theo lịch trình Java.')

# Update only this section's sitemap entries; retain every other existing URL.
ns='http://www.sitemaps.org/schemas/sitemap/0.9'
ET.register_namespace('',ns)
tree=ET.parse(ROOT/'sitemap.xml');urls=tree.getroot()
for item in list(urls):
    if (item.findtext('{'+ns+'}loc') or '').startswith('https://hoangtnedu.github.io/java/'):
        urls.remove(item)
for p in sorted(OUT.glob('*.html')):
    item=ET.SubElement(urls,'{'+ns+'}url')
    ET.SubElement(item,'{'+ns+'}loc').text='https://hoangtnedu.github.io/java/'+('' if p.name=='index.html' else p.name)
    ET.SubElement(item,'{'+ns+'}lastmod').text='2026-10-27'
ET.indent(tree,space='  ')
tree.write(ROOT/'sitemap.xml',encoding='utf-8',xml_declaration=True)
print('Built',len(list(OUT.glob('*.html'))),'Java pages')
