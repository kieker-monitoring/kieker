@echo off 

REM @author Nils Christian Ehmke

setlocal enabledelayedexpansion

SET JAVAARGS=-Dkieker.common.logging.Log=JDK -Djava.util.logging.config.file=./logging.properties -Xms56m -Xmx1024m
SET MAINCLASSNAME=kieker.tools.trace.analysis.TraceAnalysisGUI

REM Get the directory of this file and change the working directory to it.
cd %~dp0

REM Set every variable we will need for the execution.
SET BINDIR=%cd%

start javaw %JAVAARGS% -cp "%BINDIR%\..\lib\*";"%BINDIR%\..\build\libs\*";"%BINDIR%" %MAINCLASSNAME%
@echo on 