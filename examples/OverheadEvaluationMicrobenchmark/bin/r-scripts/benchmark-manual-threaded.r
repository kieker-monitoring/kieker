output_fn="C:\\Users\\jwa\\Projects\\Kieker\\software\\kieker\\trunk\\examples\\OverheadEvaluationMicrobenchmark\\tmp\\results-threaded.pdf"

configs.labels=c("Instrumentation (I)","Collecting (C)","Writing (W)")
configs.count=4
experiments.labels=c("A1","A2","A3","A4","A5")
experiments=length(experiments.labels)
meanvalues <- matrix(ncol=configs.count,nrow=experiments,byrow=TRUE,data=c(
  500.7489,500.7764,501.7337,503.1575,
  500.7836,501.0098,501.9279,503.4877,
  500.7750,501.0741,502.1604,503.6037,
  500.7604,501.0875,502.1860,517.7758,
  620.6239,626.3150,626.0106,640.3472
));

#pdf(output_fn, width=8, height=5, paper="special")
plot.new()
plot.window(xlim=c(0.5,experiments+0.5),ylim=c(0,25))
title(xlab="Experiment",ylab="Execution time (µs)")
legend("topright",inset=c(0.05,0.15),legend=c(rev(configs.labels),"(mean values)"),fill=TRUE,angle=c(45,135,45,135),density=c(30,20,10,0),title="Overhead (mean) of ...",ncol=2)
for (ce in 1:experiments) {
  #mean
  rect(ce-0.4,0,ce+0.4,meanvalues[ce,2]-meanvalues[ce,1],col="white",border="black")
  rect(ce-0.4,meanvalues[ce,2]-meanvalues[ce,1],ce+0.4,meanvalues[ce,3]-meanvalues[ce,1],col="white",border="black")
  rect(ce-0.4,meanvalues[ce,3]-meanvalues[ce,1],ce+0.4,meanvalues[ce,4]-meanvalues[ce,1],col="white",border="black")
  rect(ce-0.4,0,ce+0.4,meanvalues[ce,2]-meanvalues[ce,1],angle=45,density=30)
  rect(ce-0.4,meanvalues[ce,2]-meanvalues[ce,1],ce+0.4,meanvalues[ce,3]-meanvalues[ce,1],angle=135,density=20)
  rect(ce-0.4,meanvalues[ce,3]-meanvalues[ce,1],ce+0.4,meanvalues[ce,4]-meanvalues[ce,1],angle=45,density=10)
  for (cc in (2:configs.count)) {
    labeltext=formatC(meanvalues[ce,cc]-meanvalues[ce,cc-1],format = "f",digits=1)
      rect(ce-(strwidth(labeltext)*0.5),meanvalues[ce,cc]-strheight(labeltext),ce+(strwidth(labeltext)*0.5),meanvalues[ce,cc],col="white",border="black")
      text(ce,meanvalues[ce,cc],labels=labeltext,cex=0.75,col="black",pos=1,offset=0.1)
  }
}
axis(1, at=c(1:experiments), labels=experiments.labels,)
axis(2)
#invisible(dev.off())
