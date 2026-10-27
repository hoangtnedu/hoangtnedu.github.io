"""Exercise the packaged interactive Console application. Python standard library."""
from pathlib import Path
import json
import subprocess

ROOT = Path(__file__).resolve().parents[1]
PROJECT = ROOT / 'java/examples/lab01'
JAR = PROJECT / 'target/lab01-java-console-1.0.0.jar'
CASES = [
    ('even sum', '1\n10\n0\n', '30'),
    ('harmonic sum', '2\n4\n0\n', '2.0833'),
    ('prime', '3\n17\n0\n', 'Nguyen to'),
    ('composite', '3\n21\n0\n', 'Khong nguyen to'),
    ('triangle', '4\n3\n4\n5\n0\n', 'Tam giac vuong'),
    ('fibonacci', '5\n7\n0\n', '0 1 1 2 3 5 8'),
    ('invalid integer then valid choice', 'abc\n1\n10\n0\n', '30'),
    ('invalid positive input then recovery', '1\n0\n1\n2\n0\n', 'Loi: n phai la so nguyen duong'),
    ('fibonacci overflow rejected', '5\n94\n0\n', 'Loi: Can 1 <= n <= 93'),
    ('blank menu input then recovery', '\n1\n2\n0\n', 'Loi:'),
    ('invalid menu option', '99\n0\n', 'Lua chon khong hop le'),
    ('end of input exits', '', 'Chon bai:'),
]

def main():
    if not JAR.is_file():
        raise SystemExit(f'Missing JAR: {JAR}; run mvn verify first')
    results = []
    for name, input_text, expected in CASES:
        run = subprocess.run(
            ['java', '-Duser.language=en', '-Duser.country=US', '-jar', str(JAR)],
            input=input_text, text=True, capture_output=True, timeout=10,
        )
        passed = run.returncode == 0 and expected in run.stdout
        # Recovery cases must complete a later valid operation as well.
        if name in ['invalid positive input then recovery', 'blank menu input then recovery']:
            passed = passed and '2\n' in run.stdout
        results.append({'name': name, 'passed': passed, 'returncode': run.returncode})
        print(('PASS' if passed else 'FAIL') + ': ' + name)
        if not passed:
            print(run.stdout)
            print(run.stderr)
    (PROJECT / 'target/smoke-report.json').write_text(json.dumps(results, indent=2) + '\n')
    if not all(result['passed'] for result in results):
        raise SystemExit('Packaged JAR smoke tests failed')
    print(f'All {len(results)} packaged JAR scenarios passed.')

if __name__ == '__main__':
    main()
