output_fn="C:\\Users\\jwa\\Projects\\Kieker\\software\\kieker\\trunk\\examples\\OverheadEvaluationMicrobenchmark\\tmp\\results-singelthreaded.pdf"

configs.labels=c("Instrumentation (I)","Collecting (C)","Writing (W)")
configs.count=4
experiments.labels=c("S1","S2","A1","A2","A3","A4","A5")
experiments=length(experiments.labels)
meanvalues <- matrix(ncol=configs.count,nrow=experiments,byrow=TRUE,data=c(
  500.7039,500.8062,501.8528,509.5831,  ## SingleThread, 1-Core, SyncFS
  500.7046,500.7976,501.8133,509.6133,  ## SingleThread, 2-Core (same core), SyncFS
  
  500.7039,500.8062,501.8528,516.5481,  ## SingleThread, 1-Core, AsyncFS
  500.7046,500.7976,501.8133,504.5507,  ## SingleThread, 2-Core (same core), AsyncFS
  500.9020,500.9395,502.0425,503.6665,  ## SingleThread, 2-Core (same chip), AsyncFS
  500.7165,500.7509,501.8327,504.4998,  ## SingleThread, 2-Core (2 chips), AsyncFS
  500.7626,500.8171,501.8406,504.6068   ## SingleThread, 16-Core (2 chips), AsyncFS
));
medianvalues <- matrix(ncol=configs.count,nrow=experiments,byrow=TRUE,data=c(
  500.7010,500.7941,501.7895,509.0938,  ## SingleThread, 1-Core, SyncFS
  500.7010,500.7922,501.7766,509.1253,  ## SingleThread, 2-Core (same core), SyncFS
  
  500.7010,500.7941,501.7895,516.0032,  ## SingleThread, 1-Core, AsyncFS
  500.7010,500.7922,501.7766,504.5013,  ## SingleThread, 2-Core (same core), AsyncFS
  500.7010,500.7724,501.7931,502.9865,  ## SingleThread, 2-Core (same chip), AsyncFS
  500.7010,500.7579,501.7950,504.4580,  ## SingleThread, 2-Core (2 chips), AsyncFS
  500.7010,500.7759,501.7688,504.4528   ## SingleThread, 16-Core (2 chips), AsyncFS
));

pdf(output_fn, width=8, height=5, paper="special")
plot.new()
plot.window(xlim=c(0.5,experiments+0.5),ylim=c(500,max(meanvalues,medianvalues)))
title(xlab="Experiment",ylab="Execution time (µs)")
legend("topright",inset=c(0.05,0.15),legend=c(rev(configs.labels),"(mean values)"),fill=TRUE,angle=c(45,135,45,135),density=c(30,20,10,0),title="Overhead (median) of ...",ncol=2)
for (ce in 1:experiments) {
  #meanvalues
  rect(ce-0.35,meanvalues[ce,3],ce+0.5,meanvalues[ce,4])
  rect(ce-0.35,meanvalues[ce,2],ce+0.5,meanvalues[ce,3])
  rect(ce-0.35,meanvalues[ce,1],ce+0.5,meanvalues[ce,2])
  rect(ce-0.35,0,ce+0.5,meanvalues[ce,1])
  #median
  rect(ce-0.4,medianvalues[ce,3],ce+0.4,medianvalues[ce,4],col="white",border="black")
  rect(ce-0.4,medianvalues[ce,2],ce+0.4,medianvalues[ce,3],col="white",border="black")
  rect(ce-0.4,medianvalues[ce,1],ce+0.4,medianvalues[ce,2],col="white",border="black")
  rect(ce-0.4,0,ce+0.4,medianvalues[ce,1],col="white",border="black")
  rect(ce-0.4,medianvalues[ce,3],ce+0.4,medianvalues[ce,4],angle=45,density=30)
  rect(ce-0.4,medianvalues[ce,2],ce+0.4,medianvalues[ce,3],angle=135,density=20)
  rect(ce-0.4,medianvalues[ce,1],ce+0.4,medianvalues[ce,2],angle=45,density=10)
  rect(ce-0.4,0,ce+0.4,medianvalues[ce,1],angle=135,density=5)
  for (cc in (2:configs.count)) {
    labeltext=formatC(medianvalues[ce,cc]-medianvalues[ce,cc-1],format = "f",digits=1)
      rect(ce-(strwidth(labeltext)*0.5),medianvalues[ce,cc]-strheight(labeltext),ce+(strwidth(labeltext)*0.5),medianvalues[ce,cc],col="white",border="black")
      text(ce,medianvalues[ce,cc],labels=labeltext,cex=0.75,col="black",pos=1,offset=0.1)
  }
}
axis(1, at=c(1:experiments), labels=experiments.labels,)
axis(2)
invisible(dev.off())
