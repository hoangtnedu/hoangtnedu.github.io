"""Build the static .NET learning section. Python 3 standard library only.
Edit content/dotnet/*.md or labs.json, then run python3 tools/build_dotnet.py.
The Markdown renderer intentionally supports this course's small syntax subset.
"""
from pathlib import Path
import re,html,json,unicodedata
ROOT=Path(__file__).resolve().parents[1]
SRC=ROOT/'content/dotnet';OUT=ROOT/'dotnet';OUT.mkdir(exist_ok=True)
chapters=['Nền tảng .NET và C#','Lập trình hướng đối tượng với C#','WinForms và kiến trúc ba lớp','Truy cập dữ liệu với ADO.NET','Báo cáo Crystal Reports và Excel','Xử lý lỗi, kiểm thử và đóng gói']
labs=json.loads((SRC/'labs.json').read_text())
E=html.escape

def normalize(s):
 return ''.join(c for c in unicodedata.normalize('NFD',s.lower().replace('đ','d')) if not unicodedata.combining(c))

def inline(s):
 s=E(s)
 s=re.sub(r'`([^`]+)`',r'<code>\1</code>',s)
 s=re.sub(r'\*\*([^*]+)\*\*',r'<strong>\1</strong>',s)
 s=re.sub(r'\[([^\]]+)\]\(([^)]+)\)',r'<a href="\2">\1</a>',s)
 return s

counter=0

def code(s,lang='csharp'):
 global counter
 counter+=1;ident='code-'+str(counter)
 # Remove only outer blank lines; internal code whitespace is kept.
 s=s.strip('\n')
 return f'<div class="code-wrap"><button class="copy" type="button" data-target="{ident}">Sao chép</button><pre><code id="{ident}" class="language-{E(lang)}">{E(s)}</code></pre></div>'

def markdown(s):
 lines=s.splitlines();result=[];p=[];items=[];i=0;h=0
 def flush():
  if p:result.append('<p>'+inline(' '.join(p))+'</p>');p.clear()
  if items:result.append('<ul>'+''.join('<li>'+inline(x)+'</li>' for x in items)+'</ul>');items.clear()
 while i<len(lines):
  line=lines[i]
  if line.startswith('```'):
   flush();lang=line[3:];i+=1;buf=[]
   while i<len(lines) and not lines[i].startswith('```'):buf.append(lines[i]);i+=1
   result.append(code('\n'.join(buf),lang))
  elif line.startswith('## '):
   flush();h+=1;result.append(f'<h2 id="muc-{h}">'+inline(line[3:])+'</h2>')
  elif line.startswith('- '):
   if p:flush()
   items.append(line[2:])
  elif not line.strip():flush()
  else:
   if items:flush()
   p.append(line.strip())
  i+=1
 flush();return '\n'.join(result)

def prose(s):
 blocks=[];buf=[];items=[]
 def flush():
  if buf:blocks.append('<p>'+E(' '.join(buf))+'</p>');buf.clear()
  if items:blocks.append('<ul>'+''.join('<li>'+E(x)+'</li>' for x in items)+'</ul>');items.clear()
 for line in s.splitlines():
  t=line.strip()
  if not t:flush()
  elif t.startswith(''):
   if buf:flush()
   items.append(t.lstrip(' ').strip())
  else:
   if items and not buf:items[-1]+=' '+t
   else:buf.append(t)
 flush();return ''.join(blocks)

def exercises(s,guided):
 # Exercise headers always start at column zero in the extracted lab sources.
 hits=list(re.finditer(r'(?m)^(\d+)\. ([^\n]+)',s));out=[]
 for k,m in enumerate(hits):
  body=s[m.end():hits[k+1].start() if k+1<len(hits) else len(s)].strip()
  out.append('<div class="exercise"><h3>'+E(m.group(0))+'</h3>')
  if 'Code mẫu' in body:
   desc,snippet=body.split('Code mẫu',1)
   # PDF page furniture was removed before import. Keep code as one block.
   snippet=re.sub(r'\n{3,}','\n\n',snippet).strip('\n')
   import textwrap
   snippet=textwrap.dedent(snippet)
   sql=bool(re.match(r'\s*(CREATE|SELECT|INSERT|UPDATE|DELETE|USE)\b',snippet))
   out.append(prose(desc)+code(snippet,'sql' if sql else 'csharp'))
  else:out.append(prose(body))
  out.append('</div>')
 return ''.join(out)

def assessment(s):
 rows=[]
 for line in s.splitlines():
  m=re.match(r'\s*(\d+)\s+(.+?)\s+(\d+[,.]\d+)\s*$',line)
  if m:rows.append('<tr><td>'+E(m[1])+'</td><td>'+E(m[2])+'</td><td>'+E(m[3])+'</td></tr>')
 return '<div class="table-scroll"><table><thead><tr><th>Mục</th><th>Tiêu chí</th><th>Điểm</th></tr></thead><tbody>'+''.join(rows)+'</tbody></table></div>' if rows else prose(s)

def sidebar(active):
 items=[('index.html','Tổng quan lộ trình')]+[(f'chuong-{i}.html',f'Chương {i}. {t}') for i,t in enumerate(chapters,1)]+[('bai-tap.html','Bộ 16 bài thực hành'),('mo-rong-web.html','Mở rộng: Web, EF Core, AI, Azure'),('tai-lieu.html','Tài liệu tham khảo')]
 toc=''.join(f'<li data-search="{E(normalize(t))}"><a class="{ "active" if p==active else ""}" href="{p}">{E(t)}</a></li>' for p,t in items)
 labtoc=''.join(f'<li data-search="{E(normalize(l["title"]))}"><a href="lab-{l["number"]:02}.html">Lab {l["number"]}: {E(l["title"])}</a></li>' for l in labs)
 return '<aside class="sidebar" id="navigation" aria-label="Lộ trình .NET"><div class="profile"><strong>Trần Nguyên Hoàng</strong><p>Ghi chép lập trình · HoangTN</p><a href="../index.html">← Trang chủ</a></div><h2>Lập trình .NET</h2><label class="tiny" for="toc-search">Tìm chương hoặc lab</label><input type="search" id="toc-search" placeholder="ADO.NET, báo cáo, transaction…"><ul class="toc-list">'+toc+'</ul><h2>Thực hành</h2><ul class="toc-list">'+labtoc+'</ul><p class="empty-result" hidden>Không có mục phù hợp.</p></aside>'

def page(path,title,body,desc,prev=None,next=None):
 nav='<nav class="lesson-nav" aria-label="Bài trước và bài sau">'
 if prev:nav+=f'<a href="{prev[0]}">← {E(prev[1])}</a>'
 if next:nav+=f'<a href="{next[0]}">{E(next[1])} →</a>'
 nav+='</nav>'
 local=re.findall(r'<h2 id="([^"]+)">(.+?)</h2>',body)
 contents='<details class="page-toc"><summary>Trong bài này</summary><ol>'+''.join(f'<li><a href="#{a}">{b}</a></li>' for a,b in local)+'</ol></details>' if local else ''
 url='https://hoangtnedu.github.io/dotnet/'+('' if path=='index.html' else path)
 doc=f'''<!doctype html><html lang="vi"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>{E(title)} | HoangTN</title><meta name="author" content="Trần Nguyên Hoàng"><meta name="description" content="{E(desc)}"><meta property="og:title" content="{E(title)} | HoangTN"><meta property="og:description" content="{E(desc)}"><meta property="og:type" content="article"><link rel="canonical" href="{url}"><link rel="icon" href="../assets/favicon.svg" type="image/svg+xml"><link rel="stylesheet" href="../assets/css/site.css"><link rel="stylesheet" href="../assets/css/dotnet.css"><script defer src="../assets/js/site.js"></script><script defer src="../assets/js/dotnet.js"></script></head><body class="dotnet"><a class="skip" href="#noi-dung">Đến nội dung</a><header class="top"><a class="brand" href="../index.html">HoangTN<span>Nghiên cứu và lập trình</span></a><nav aria-label="Điều hướng chính"><a href="../index.html">Bài viết</a><a href="../2026/10/27/floodai-mo-hinh-hoc-sau.html">FloodAI</a><a href="index.html" aria-current="page">Lập trình .NET</a><button class="mobile-nav" type="button" aria-controls="navigation" aria-expanded="false">Mục lục</button></nav></header><div class="progress" aria-hidden="true"></div><div class="layout">{sidebar(path)}<main class="article" id="noi-dung"><p class="eyebrow">HoangTN · Lập trình .NET</p><h1>{E(title)}</h1><div class="meta"><span>Trần Nguyên Hoàng</span><span>Cập nhật 27/10/2026</span></div>{contents}{body}{nav}</main></div><footer>© 2026 Trần Nguyên Hoàng · HoangTN · Ghi chép nghiên cứu và học tập.</footer><button class="top-button" type="button" aria-label="Về đầu trang">↑</button></body></html>'''
 (OUT/path).write_text(doc)

for i,title in enumerate(chapters,1):
 body='<p class="lede">'+E(['Hiểu môi trường thực thi và viết chương trình Console có kiểm tra dữ liệu.','Mô hình hóa bài toán quản lý bằng lớp, hợp đồng và danh sách đối tượng.','Xây dựng giao diện theo sự kiện, kiểm tra dữ liệu và tách trách nhiệm.','Lưu trữ, tìm kiếm và cập nhật dữ liệu SQL Server bằng ADO.NET.','Từ truy vấn đã kiểm chứng đến báo cáo, bộ lọc và tệp xuất.','Hoàn thiện chất lượng ứng dụng và bàn giao một sản phẩm có thể chạy.'][i-1])+'</p>'+markdown((SRC/f'chuong-{i}.md').read_text())
 page(f'chuong-{i}.html',f'Chương {i}. {title}',body,title,('index.html','Lộ trình') if i==1 else (f'chuong-{i-1}.html',f'Chương {i-1}'),('bai-tap.html','Bộ bài tập') if i==6 else (f'chuong-{i+1}.html',f'Chương {i+1}'))

for lab in labs:
 i=lab['number'];c=lab['chapter']
 body=f'<p class="lede">Bài tập Chương {c} · {E(chapters[c-1])}.</p><p><a href="chuong-{c}.html">Đọc lý thuyết liên quan →</a></p>'
 body+='<div class="callout"><p>Mã mẫu minh họa từng bài, có thể cần ghép với lớp, Form hoặc CSDL ở lab trước. Không dán tất cả ví dụ cùng tên lớp vào một tệp. Chuỗi desktop dùng .NET Framework 4.8; chuẩn bị runtime Crystal Reports tương thích cho Lab 13–16.</p></div>'
 if i==13:body+='<p>Dùng tiêu đề cá nhân HoangTN hoặc tiêu đề trung tính khi thiết kế báo cáo.</p>'
 for n,sec in enumerate(lab['sections'],1):
  body+=f'<section id="phan-{n}"><h2 id="muc-{n}">'+E(sec['title'])+'</h2>'
  body+=exercises(sec['text'],n==5) if n in [5,6] else assessment(sec['text']) if n==8 else prose(sec['text'])
  body+='</section>'
 body+='<p class="source">Biên tập từ bộ bài lab được cung cấp. Đọc <a href="tai-lieu.html">nguồn học liệu và tài liệu đối chiếu</a>.</p>'
 page(f'lab-{i:02}.html',f'Lab {i}. {lab["title"]}',body,f'Bài thực hành {i}: '+lab['title'],('bai-tap.html','Danh sách lab') if i==1 else (f'lab-{i-1:02}.html',f'Lab {i-1}'),('bai-tap.html','Tổng kết thực hành') if i==16 else (f'lab-{i+1:02}.html',f'Lab {i+1}'))

rows=''.join(f'<tr><td><a href="lab-{l["number"]:02}.html">Lab {l["number"]}</a></td><td>{E(l["title"])}</td><td><a href="chuong-{l["chapter"]}.html">Chương {l["chapter"]}</a></td></tr>' for l in labs)
body='<p class="lede">16 bài thực hành theo một bài toán quản lý xuyên suốt, từ Console đến ứng dụng WinForms có dữ liệu và báo cáo.</p><div class="callout"><p>Mỗi lab gồm 5 bài có hướng dẫn, 5 bài tự thực hiện, sản phẩm nộp và tiêu chí đánh giá. Số thứ tự giữ theo bộ lab desktop.</p></div><label class="filter-label" for="lab-filter">Lọc bài thực hành</label><input class="lab-filter" id="lab-filter" type="search" placeholder="Nhập tên, số lab hoặc chủ đề…"><div class="table-scroll"><table id="lab-table"><thead><tr><th>Lab</th><th>Nội dung</th><th>Lý thuyết</th></tr></thead><tbody>'+rows+'</tbody></table></div><p id="lab-empty" hidden>Không có bài phù hợp.</p><h2>Chuẩn bị trước khi làm</h2><p>Windows, Visual Studio có .NET desktop development, .NET Framework 4.8 targeting pack; SQL Server và SSMS từ Lab 7; Crystal Reports và runtime tương thích từ Lab 13. Dùng dữ liệu giả lập, README và quản lý phiên bản cho từng bài.</p><h2>Thứ tự thực hành</h2><ol><li>Lab 1–3: Console và mô hình đối tượng.</li><li>Lab 4–6: Giao diện và kiến trúc ba lớp trong bộ nhớ.</li><li>Lab 7–12: CSDL, truy vấn, CRUD và transaction.</li><li>Lab 13–14: Báo cáo và xuất dữ liệu.</li><li>Lab 15–16: Chất lượng, đóng gói và sản phẩm tổng hợp.</li></ol>'
page('bai-tap.html','Bộ 16 bài thực hành .NET',body,'16 lab C#, WinForms, ADO.NET, Crystal Reports và ứng dụng tổng hợp.')

for slug,title in [('mo-rong-web','Lộ trình mở rộng: ASP.NET Core, EF Core, AI và Azure'),('tai-lieu','Tài liệu và nguồn tham khảo')]:
 page(slug+'.html',title,markdown((SRC/(slug+'.md')).read_text()),title,('index.html','Lộ trình'))

cards=''.join(f'<a class="chapter-card" href="chuong-{i}.html"><span>Chương {i:02}</span><h2>{E(t)}</h2><p>'+['CLR, kiểu dữ liệu, điều khiển và ngoại lệ.','Lớp, constructor, đóng gói, kế thừa và LINQ.','Điều khiển, sự kiện, binding và đa Form.','SQL Server, tham số, DataSet và transaction.','Nguồn dữ liệu, tham số, nhóm và xuất tệp.','Logging, cấu hình, kiểm thử và bàn giao.'][i-1]+'</p></a>' for i,t in enumerate(chapters,1))
body='''<p class="lede">Từ những dòng C# đầu tiên đến ứng dụng quản lý WinForms có cơ sở dữ liệu, báo cáo và bộ cài.</p><div class="course-stats"><span><strong>06</strong> chương kiến thức</span><span><strong>16</strong> bài thực hành</span><span><strong>160</strong> bài tập trong lab</span></div><div class="downloads"><a class="download" href="chuong-1.html">Bắt đầu Chương 1 →</a><a class="download" href="bai-tap.html">Xem 16 lab</a></div><h2>Học theo một bài toán xuyên suốt</h2><p>Xây dựng ứng dụng quản lý sinh viên: khai báo đối tượng, thiết kế Form, kết nối SQL Server, thực hiện CRUD, xử lý giao dịch nhiều bảng, tạo báo cáo và bàn giao sản phẩm. Mỗi chương có giải thích, ví dụ, câu hỏi tự kiểm tra và các lab tương ứng.</p><div class="chapter-grid">'''+cards+'''</div><h2>Chuẩn bị môi trường</h2><p>Lộ trình desktop dùng Windows, Visual Studio với workload .NET desktop development và .NET Framework 4.8. Từ Chương 4 cần SQL Server/SSMS; Chương 5 cần Crystal Reports cùng runtime phù hợp. Các mã mẫu là nội dung để thực hành trên máy, không thực thi C# trực tiếp trong trang web này.</p><h2>Nền tảng trước khi học</h2><ul><li>Biết biến, điều kiện, vòng lặp và hàm ở mức cơ bản.</li><li>Đã làm quen cơ sở dữ liệu quan hệ và câu lệnh SQL.</li><li>Chuẩn bị thư mục mã nguồn, dữ liệu mẫu và README cho từng lab.</li></ul><h2>Tiếp nối sang .NET hiện đại</h2><p>Phần <a href="mo-rong-web.html">ASP.NET Core, EF Core, kiểm thử, bảo mật, AI và Azure</a> cung cấp sáu bài mở rộng theo khung Công nghệ .NET. Các bài mở rộng dùng project riêng để giữ môi trường tương thích cho chuỗi WinForms.</p><h2>Học liệu và cách sử dụng</h2><p>Khung chương dựa trên đề cương Lập trình .NET; bài tập được biên tập từ bộ Lab 1–16. Phần giải thích được viết lại theo bài giảng cơ bản, nâng cao và đối chiếu các nguồn kỹ thuật chính thức. Xem <a href="tai-lieu.html">danh mục nguồn và phạm vi tham khảo</a>.</p>'''
page('index.html','Lập trình .NET',body,'6 chương C#, WinForms, ADO.NET, báo cáo và đóng gói; 16 lab thực hành cùng lộ trình mở rộng web.')
print('Built',len(list(OUT.glob('*.html'))),'course pages')

