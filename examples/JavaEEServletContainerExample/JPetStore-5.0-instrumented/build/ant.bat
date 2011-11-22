
call setenv.bat

%JAVA_HOME%\bin\java -classpath %BUILD_CP% org.apache.tools.ant.Main -buildfile build.xml %1
