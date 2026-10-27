"""Summarize actual Surefire/Failsafe XML reports without treating missing tests as a pass."""
from pathlib import Path
import xml.etree.ElementTree as ET,json,os
ROOT=Path(__file__).resolve().parents[1]
summary=[]
for n in range(1,17):
 total=dict(tests=0,failures=0,errors=0,skipped=0)
 for kind in ['surefire-reports','failsafe-reports']:
  for file in (ROOT/f'java/examples/lab{n:02}').glob(f'**/target/{kind}/TEST-*.xml'):
   suite=ET.parse(file).getroot()
   for key in total:total[key]+=int(suite.attrib.get(key,0))
 summary.append({'lab':n,**total,'status':'not-run' if not total['tests'] else 'failed' if total['failures']+total['errors'] else 'passed'})
(ROOT/'java/ci-report.json').write_text(json.dumps(summary,indent=2)+'\n')
lines=['## Java lab verification','', '| Lab | Tests | Failures | Errors | Skipped | Status |','| --- | ---: | ---: | ---: | ---: | --- |']
for row in summary:lines.append('| '+ ' | '.join(str(row[k]) for k in ['lab','tests','failures','errors','skipped','status'])+' |')
text='\n'.join(lines)+'\n';print(text)
if os.getenv('GITHUB_STEP_SUMMARY'):
 with open(os.environ['GITHUB_STEP_SUMMARY'],'a') as stream:stream.write(text)
if any(r['status']!='passed' for r in summary):raise SystemExit('Some labs failed or have no test report')
