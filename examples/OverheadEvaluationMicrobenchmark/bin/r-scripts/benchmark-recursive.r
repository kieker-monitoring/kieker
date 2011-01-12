#results_fn="C:\\Users\\jwa\\Projects\\Kieker\\software\\kieker\\trunk\\examples\\OverheadEvaluationMicrobenchmark\\tmp\\results-experimental\\results.csv"
#output_fn="C:\\Users\\jwa\\Projects\\Kieker\\software\\kieker\\trunk\\examples\\OverheadEvaluationMicrobenchmark\\tmp\\results-experimental\\results.pdf"
baseresults=read.csv2(results_fn,quote="",colClasses=c("NULL","NULL","integer","integer","NULL","integer"))
baseresults["rt_msec"]=baseresults["duration_nsec"]/(1000)
## order_index recursion_depth duration_nsec rt_msec

configs=unique(baseresults$order_index)
configs.labels=c("Empty Probe","Inactive Probe","Collecting Data","Writing Data")
##_Remove_Empty_Probe
configs=c(0,2,3,4)
configs.labels=c("Inactive Probe","Collecting Data","Writing Data")
##_Remove_Empty_Probe
recdepth=unique(baseresults$recursion_depth)

meanvalues <- matrix(nrow=length(recdepth),ncol=length(configs),byrow=TRUE,dimnames=list(recdepth,configs))
medianvalues <- matrix(nrow=length(recdepth),ncol=length(configs),byrow=TRUE,dimnames=list(recdepth,configs))
for (i in recdepth){
  curvals <- subset(baseresults, recursion_depth==i, select=c("order_index","rt_msec"))
  for (j in configs){
    mcvs <- subset(curvals, order_index==j)[["rt_msec"]]
    meanvalues[(1:length(recdepth))[recdepth==i],(1:length(configs))[configs==j]] <- mean(mcvs)
    medianvalues[(1:length(recdepth))[recdepth==i],(1:length(configs))[configs==j]] <- median(mcvs)
  }
}
rm(results_fn,baseresults,curvals,mcvs,i,j)

pdf(output_fn, width=8, height=5, paper="special")
plot.new()
plot.window(xlim=c(min(recdepth)-0.5,max(recdepth)+0.5),ylim=c(min(medianvalues,meanvalues),max(medianvalues,meanvalues)),ylab="test")
axis(1,at=recdepth)
axis(2)
title(xlab="Recursion Depth (Number of Executions)",ylab="Execution Time (ms)")
density=1:(length(configs)-1)*10
legend("topleft",inset=c(0.05,0.15),legend=rev(configs.labels),fill=TRUE,density=rev(density),title="Overhead of ...")
for (i in recdepth) {
  medianval <- medianvalues[(1:length(recdepth))[recdepth==i],]
  for (j in 1:(length(medianval)-1)) {
    rect(i-0.45,medianval[j],i+0.45,medianval[j+1],density=density[j])
    text(i,medianval[j+1],labels=format(medianval[j+1]-medianval[j],digits=3,nsmall=3),cex=0.75,col="black",pos=1,offset=0.1)
  }
  meanval <- meanvalues[(1:length(recdepth))[recdepth==i],]
  for (j in 1:(length(meanval)-1)) {
    #rect(i-0.45,meanval[j],i+0.45,meanval[j+1],lty="dotted")
  }
}
rm(output_fn,medianval,meanval,i,j,density)
dev.off()
