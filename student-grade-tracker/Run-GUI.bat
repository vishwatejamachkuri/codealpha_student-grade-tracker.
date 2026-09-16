@echo off
title Student Grade Tracker (GUI)
cd /d "%~dp0"
javac -d bin src/*.java
java -cp bin GradeTrackerGUI
