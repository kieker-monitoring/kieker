output_fn="C:\\Users\\jwa\\Projects\\Kieker\\software\\kieker\\branches\\1.3-refactoring\\examples\\OverheadEvaluationMicrobenchmark\\tmp\\results.pdf"

configs.labels=c("Instrumentation (I)","Collecting (C)","Writing (W)")
configs=4
experiments=7
meanvalues <- matrix(ncol=configs,nrow=experiments,byrow=TRUE,data=c(
  500.7133,500.7144,501.8507,517.4516,
  500.7122,500.8143,501.8234,504.5404,
  500.7118,501.2488,501.8193,503.6949,
  500.7186,500.7138,501.8248,504.5454,
  500.7090,500.8117,501.7937,504.5650,
  500.7138,500.7141,501.8552,510.0038,
  500.7121,501.2233,501.8233,510.1165
));
medianvalues <- matrix(ncol=configs,nrow=experiments,byrow=TRUE,data=c(
  500.71,500.710,501.797,516.817,
  500.71,500.806,501.791,504.465,
  500.71,500.806,501.789,502.945,
  500.71,500.710,501.789,504.472,
  500.71,500.806,501.790,504.476,
  500.71,500.710,501.800,509.487,
  500.71,500.806,501.794,509.296
));

pdf(output_fn, width=8, height=5, paper="special")
plot.new()
plot.window(xlim=c(0.5,experiments+0.5),ylim=c(min(medianvalues,meanvalues)-0.5,max(medianvalues,meanvalues)+0.5))
axis(1)
axis(2)
title(xlab="Experiment",ylab="Execution time (µs)")
angle=rep(c(45,135),configs/2,length.out=configs-1)
density=1:(configs-1)*10
legend("topright",inset=c(0.05,0.15),legend=c(rev(configs.labels),"(mean values)"),fill=TRUE,angle=rev(angle),density=c(rev(density),0),title="Overhead (median) of ...",ncol=2)
for (i in 1:experiments) {
  for (j in 1:(configs-1)) {
    rect(i-0.35,meanvalues[i,j],i+0.5,meanvalues[i,j+1])
  }
  for (j in 1:(configs-1)) {
    rect(i-0.4,medianvalues[i,j],i+0.4,medianvalues[i,j+1],col="white",border="black")
    rect(i-0.4,medianvalues[i,j],i+0.4,medianvalues[i,j+1],angle=angle[j],density=density[j])
    labeltext=format(medianvalues[i,j+1]-medianvalues[i,j],digits=1,nsmall=1)
    rect(i-(strwidth(labeltext)*0.5),medianvalues[i,j+1]-strheight(labeltext),i+(strwidth(labeltext)*0.5),medianvalues[i,j+1],col="white",border="black")
    text(i,medianvalues[i,j+1],labels=labeltext,cex=0.75,col="black",pos=1,offset=0.1)
  }
}
rm(i,j,labeltext,density,angle)
dev.off()
