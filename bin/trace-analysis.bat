REM This wrapper script needs still improvement.
REM
REM @author Nils Christian Ehmke

REM Don't show everything on the screen.
@echo off 

REM Get the directory of this file and change the working directory to it.
cd %~dp0

REM Set every variable we will need for the execution.
set BINDIR=%cd%
set CLASSPATH=%BINDIR%\..\lib\commons-logging-1.1.1.jar;%BINDIR%\..\lib\commons-cli-1.2.jar;%BINDIR%\..\dist\kieker-1.2-SNAPSHOT.jar
set JAVAARGS="-Dlog4j.configuration=./log4j.properties -Xms56m -Xmx1024m"
set MAINCLASSNAME=kieker.tools.traceAnalysis.TraceAnalysisTool

REM Now start the tool, but don't forget to deliver the parameters.
java %JAVAARGS% -cp "%CLASSPATH%" %MAINCLASSNAME% %*

@echo on
REM Don't close the window immediately.
PAUSE