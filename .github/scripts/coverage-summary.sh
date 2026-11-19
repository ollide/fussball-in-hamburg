#!/usr/bin/env bash
# Prints a Markdown coverage summary (overall + per package) from a JaCoCo CSV report.
# Usage: coverage-summary.sh [target/site/jacoco/jacoco.csv]
set -euo pipefail

csv="${1:-target/site/jacoco/jacoco.csv}"

awk -F, '
function pct(covered, missed) {
    return (covered + missed) == 0 ? "–" : sprintf("%.1f%%", 100 * covered / (covered + missed))
}
NR == 1 { next }
{
    pkg = $2
    sub(/^org\.ollide\.fussifinder\.?/, "", pkg)
    if (pkg == "") pkg = "(root)"
    if (!(pkg in lm)) order[++n] = pkg
    lm[pkg] += $8; lc[pkg] += $9; bm[pkg] += $6; bc[pkg] += $7
    LM += $8; LC += $9; BM += $6; BC += $7
}
END {
    print "## Test coverage"
    print ""
    printf "**Lines: %s** (%d/%d) · **Branches: %s** (%d/%d)\n\n", pct(LC, LM), LC, LC + LM, pct(BC, BM), BC, BC + BM
    print "| Package | Lines | Branches |"
    print "|---|---:|---:|"
    for (i = 1; i <= n; i++) {
        p = order[i]
        printf "| %s | %s | %s |\n", p, pct(lc[p], lm[p]), pct(bc[p], bm[p])
    }
}
' "$csv"
