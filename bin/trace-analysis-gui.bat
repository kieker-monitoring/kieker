@echo off 

REM @author Nils Christian Ehmke

setlocal enabledelayedexpansion

SET JAVAARGS=-Dkieker.common.logging.Log=JDK -Djava.util.logging.config.file=./logging.properties -Xms56m -Xmx1024m
SET MAINCLASSNAME=kieker.tools.traceAnalysis.TraceAnalysisGUI

REM Get the directory of this file and change the working directory to it.
cd %~dp0

REM Set every variable we will need for the execution.
SET BINDIR=%cd%

SET CLASSPATH=%BINDIR%
for /F "delims=" %%i in ('dir /B /S "%BINDIR%\..\lib\*.jar"') do (
SET CLASSPATH=!CLASSPATH!;%%i
)
for /F "delims=" %%i in ('dir /B /S "%BINDIR%\..\dist\*.jar"') do (
SET CLASSPATH=!CLASSPATH!;%%i
)

java %JAVAARGS% -cp "%CLASSPATH%" %MAINCLASSNAME%
@echo on 