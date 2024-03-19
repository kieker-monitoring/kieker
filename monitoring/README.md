# Kieker Monitoring Component

Kiekers monitoring component offers the core monitoring code (including monitoring, queue and writing management) and the monitoring probes for different technologies. By weaving the monitoring probes into the System under Test (SuT), Kieker traces can be produced that later on can be handled by the Kieker analysis component.

```mermaid
graph TD;
	:monitoring:core-->:monitoring:aspectj;
	:monitoring:core-->:monitoring:disl;
	:monitoring:aspectj-->SuT;
	:monitoring:disl-->SuT;
	SuT-->id1[Kieker Traces];
```
