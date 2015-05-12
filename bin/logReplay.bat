@echo off 

REM @author Nils Christian Ehmke

setlocal enabledelayedexpansion

SET JAVAARGS=-Dkieker.common.logging.Log=JDK -Djava.util.logging.config.file=./logging.properties
SET MAINCLASSNAME=kieker.tools.logReplayer.FilesystemLogReplayerStarter

REM Get the directory of this file and change the working directory to it.
cd %~dp0

REM Set some variables we will need for the execution.
SET BINDIR=%cd%

SET CLASSPATH=%BINDIR%

REM Now start the tool, but don't forget to deliver the parameters.
java %JAVAARGS% -cp "%BINDIR%\..\lib\*";"%BINDIR%\..\build\libs/*";"%BINDIR%" %MAINCLASSNAME% %*

REM Don't close the window immediately.
@echo on
PAUSE