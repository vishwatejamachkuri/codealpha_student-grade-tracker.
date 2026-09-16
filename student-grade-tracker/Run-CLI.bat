@echo off
title Student Grade Tracker (CLI)
cd /d "%~dp0"
javac -d bin src/*.java
java -cp bin Main
pause
