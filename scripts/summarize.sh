#!/usr/bin/env bash
# Usage: summarize.sh path/to/cucumber.json
# Prints key=value lines (total, passed, failed, status) suitable for $GITHUB_OUTPUT.
set -uo pipefail
JSON="${1:?path to cucumber.json required}"

if [ ! -s "$JSON" ]; then
  printf 'total=0\npassed=0\nfailed=0\nstatus=ERROR\n'
  exit 0
fi

jq -r '
  [ .[].elements[] | select(.type == "scenario") ] as $s
  | ($s | length) as $total
  | ($s | map(select([.steps[].result.status] | all(. == "passed"))) | length) as $passed
  | "total=\($total)",
    "passed=\($passed)",
    "failed=\($total - $passed)",
    "status=\(if $total == 0 then "ERROR" elif $total == $passed then "PASSED" else "FAILED" end)"
' "$JSON"
