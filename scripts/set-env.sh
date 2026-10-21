#!/usr/bin/env bash
# Usage: set-env.sh <MALCODE> <ENV>      e.g.  set-env.sh MAL001 SIT
# Overwrites the "env" field in src/test/resources/tcoe.properties with MAL001_SIT,
# so the framework loads environment_MAL001_SIT.properties.
set -euo pipefail

MAL="${1:?malcode required}"
ENVNAME="${2:?environment required}"
FIELD="${TCOE_FIELD:-env}"
RES="src/test/resources"
TARGET="${RES}/tcoe.properties"
VALUE="${MAL}_${ENVNAME}"

if [ ! -f "${RES}/environment_${VALUE}.properties" ]; then
  echo "ERROR: ${RES}/environment_${VALUE}.properties does not exist" >&2
  exit 1
fi

awk -v f="$FIELD" -v v="$VALUE" '
  BEGIN { FS = OFS = "=" }
  $1 == f { print f "=" v; found = 1; next }
  { print }
  END { if (!found) print f "=" v }
' "$TARGET" > "${TARGET}.tmp" && mv "${TARGET}.tmp" "$TARGET"

echo "Set ${FIELD}=${VALUE} in tcoe.properties"
