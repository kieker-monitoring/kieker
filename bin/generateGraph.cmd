rem java -cp %kieker% teetime.framework.ConfigurationStarter kieker.analysisteetime.config.DependencyGraphConfiguration 

echo Execute from within Kieker's root directory.

set kieker=build\libs\kieker-1.14-emf.jar
set kiekerLogDir=kieker-analysis/test-resources/kieker-20170805-132418-9229368724068-UTC--KIEKER
set graphOutputDir=%kiekerLogDir%
java -cp %kieker% kieker.analysisteetime.config.DependencyGraphConfiguration %kiekerLogDir% %graphOutputDir%
