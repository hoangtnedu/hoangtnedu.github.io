"""Start the packaged Console/Spring applications; use isolated local processes."""
from pathlib import Path
import subprocess,os,time,socket,json,urllib.request
ROOT=Path(__file__).resolve().parents[1]
results=[]
jar=ROOT/'java/examples/lab02/target/lab02.jar'
run=subprocess.run(['java','-jar',str(jar)],input='SV001\nNguyen Van An\n12\n8\n7\n9\n',text=True,capture_output=True,timeout=15)
assert run.returncode==0 and '8.30 - B' in run.stdout,run.stdout+run.stderr
results.append({'lab':2,'scenario':'packaged Console / invalid grade recovery','passed':True})
for n in range(11,16):
 jar=ROOT/f'java/examples/lab{n}/target/lab{n}.jar'
 with socket.socket() as s:s.bind(('127.0.0.1',0));port=s.getsockname()[1]
 log=jar.parent/'boot-smoke.log'
 with log.open('w') as output:
  process=subprocess.Popen(['java','-jar',str(jar),f'--server.port={port}'],cwd=jar.parent,stdout=output,stderr=subprocess.STDOUT)
  try:
   deadline=time.monotonic()+90
   while True:
    if process.poll() is not None:raise AssertionError(log.read_text())
    try:
     with urllib.request.urlopen(f'http://127.0.0.1:{port}/',timeout=2) as response:
      text=response.read().decode();assert response.status==200 and 'HoangTN' in text
     break
    except (urllib.error.URLError,TimeoutError):
     if time.monotonic()>deadline:raise AssertionError(log.read_text())
     time.sleep(.5)
   results.append({'lab':n,'scenario':'packaged Spring Boot JAR serves home page','passed':True})
   print(f'PASS: packaged Lab {n} Spring Boot JAR')
  finally:
   process.terminate()
   try:process.wait(timeout=15)
   except subprocess.TimeoutExpired:process.kill();process.wait(timeout=5)
(ROOT/'java/examples/lab02/target/suite-smoke-report.json').write_text(json.dumps(results,indent=2)+'\n')
print('All packaged application smoke checks passed.')
