results_fn="C:\\Users\\jwa\\Projects\\Kieker\\software\\kieker\\trunk\\examples\\OverheadEvaluationMicrobenchmark\\tmp\\results-A5\\results.csv"
output_fn="C:\\Users\\jwa\\Projects\\Kieker\\software\\kieker\\trunk\\examples\\OverheadEvaluationMicrobenchmark\\tmp\\results-A5\\results-bars.pdf"

configs.loop=10
configs.recursion=c(1)
configs.labels=c("Writing Data")
configs.count=length(configs.labels)
results.count=2000000
results.skip =1000000

## "[ recursion , config , loop ]"
meanvalues <- array(dim=c(length(configs.recursion),configs.count,configs.loop),dimnames=list(configs.recursion,configs.labels,c(1:configs.loop)))
medianvalues <- array(dim=c(length(configs.recursion),configs.count,configs.loop),dimnames=list(configs.recursion,configs.labels,c(1:configs.loop)))
for (cr in configs.recursion) {
  for (cc in (1:configs.count)) {
    for (cl in (1:configs.loop)) {
      results_fn_temp=paste(results_fn, "-", cl, "-", cr, "-", cc, ".csv", sep="")
      results=read.csv2(results_fn_temp,nrows=(results.count-results.skip),skip=results.skip,quote="",colClasses=c("NULL","integer"),comment.char="",col.names=c("thread_id","duration_nsec"))
      meanvalues[(1:length(configs.recursion))[configs.recursion==cr],cc,cl] <- mean(results[["duration_nsec"]])/(1000)
      medianvalues[(1:length(configs.recursion))[configs.recursion==cr],cc,cl] <- median(results[["duration_nsec"]])/(1000)
      rm(results,results_fn_temp)
    }
  }
}

pdf(output_fn, width=8, height=5, paper="special")
plot.new()
plot.window(xlim=c(min(configs.recursion)-0.5,max(configs.recursion)+0.5),ylim=c(min(meanvalues),max(meanvalues)))
axis(1,at=configs.recursion)
axis(2)
title(xlab="Recursion Depth (Number of Executions)",ylab="Execution Time (µs)")
for (cr in (1:length(configs.recursion))) {
  rect(cr-0.4,0,cr+0.4,mean(medianvalues[cr,1,]),angle=135,density=0)
  labeltext=format(mean(medianvalues[cr,1,]),digits=1,nsmall=1)
    rect(cr-(strwidth(labeltext)*0.5),mean(medianvalues[cr,1,])-strheight(labeltext),cr+(strwidth(labeltext)*0.5),mean(medianvalues[cr,1,]),col="white",border="black")
    text(cr,mean(medianvalues[cr,1,]),labels=labeltext,cex=0.75,col="black",pos=1,offset=0.1)
  printvalues = matrix(nrow=2,ncol=configs.count,dimnames=list(c("mean","median")),c(1:configs.count))
  for (cc in (1:configs.count)) {
    printvalues["mean",cc] = mean(meanvalues[cr,cc,])
    printvalues["median",cc] = mean(medianvalues[cr,cc,])
  }
  print(printvalues)
}
invisible(dev.off())
