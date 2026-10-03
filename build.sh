#!/usr/bin/env bash
# Builds upb-game.jar: compiled classes + sources + resources, runnable (BugWorld) and usable as a library.
set -euo pipefail
cd "$(dirname "$0")"

JAVA_RELEASE=11
OUT=build/jar
JAR=upb-game.jar

rm -rf build "$JAR"
mkdir -p "$OUT"
javac --release "$JAVA_RELEASE" -encoding UTF-8 -d "$OUT" $(find src -name "*.java")
cp -r src/. "$OUT"/
cp -r resources/images resources/sounds "$OUT"/
jar --create --file "$JAR" --main-class edu.upb.lp.game.Main -C "$OUT" .
echo "Built $JAR (Java $JAVA_RELEASE+)"
