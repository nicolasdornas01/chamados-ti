@echo off
chcp 65001 > nul
cd /d "%~dp0"
if not exist bin mkdir bin
javac -encoding UTF-8 -d bin src\*.java
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp "bin;lib\*" Main
pause
