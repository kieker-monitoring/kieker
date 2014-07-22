#rm(list=ls(all=TRUE))
#data_fn="tmp/"
#folder_fn="results-benchmark-binary"
#results_fn=paste(data_fn,folder_fn,"/raw",sep="")
#outtxt_fn=paste(data_fn,folder_fn,"/results-text.txt",sep="")
#results_fn="raw"
#outtxt_fn="results-text.txt"

configs.threads=1
#configs.loop=10
#configs.recursion=c(10)
#configs.labels=c("No Probe","Inactive Probe","Collecting Data","Writing Data (ASCII)", "Writing Data (Bin)")
configs.count=length(configs.labels)
#results.count=2000000
#results.skip=1000000

#bars.minval=500
#bars.maxval=600


throughput = array(list(),dim=c(length(configs.recursion),configs.count))
## "[ recursion , config , loop ]"
resultsBIG <- array(dim=c(length(configs.recursion),configs.count,configs.threads*configs.loop*(results.count-results.skip)),dimnames=list(configs.recursion,configs.labels,c(1:(configs.threads*configs.loop*(results.count-results.skip)))))
for (cr in configs.recursion) {
  for (cc in (1:configs.count)) {
    recordsPerSecond = c()
    rpsLastTime = 0
    rpsCount = 0
    for (cl in (1:configs.loop)) {
      results_fn_temp=paste(results_fn, "-", cl, "-", cr, "-", cc, ".csv", sep="")
      for (ct in (1:configs.threads)) {
        results=read.csv2(results_fn_temp,nrows=(results.count-results.skip),skip=(ct-1)*results.count+results.skip,quote="",colClasses=c("NULL","numeric"),comment.char="",col.names=c("thread_id","duration_nsec"),header=FALSE)
        resultsBIG[(1:length(configs.recursion))[configs.recursion==cr],cc,c(((cl-1)*configs.threads*(results.count-results.skip)+1):(cl*configs.threads*(results.count-results.skip)))] <- results[["duration_nsec"]]/(1000)
        
        for (timeForRecord in results[["duration_nsec"]]) {
          if (rpsLastTime + timeForRecord >= 1000000000) {
						recordsPerSecond <- c(recordsPerSecond, rpsCount)
						rpsCount = 0
						rpsLastTime = 1000000000 - rpsLastTime
					}
					while (timeForRecord > 1000000000) {
						recordsPerSecond <- c(recordsPerSecond, 0)
						timeForRecord = timeForRecord - 1000000000
					}
					rpsCount = rpsCount + 1
					rpsLastTime = rpsLastTime + timeForRecord
				}
      }
      rm(results,results_fn_temp)
    }
    recordsPerSecond <- c(recordsPerSecond, rpsCount)
    throughput[[(1:length(configs.recursion))[configs.recursion==cr],cc]] <- recordsPerSecond
    rm(recordsPerSecond,rpsLastTime,rpsCount)
  }
}


for (cr in configs.recursion) {
  printvalues = matrix(nrow=7,ncol=configs.count,dimnames=list(c("mean","ci95%","md25%","md50%","md75%","max","min"),c(1:configs.count)))
  printthrough = matrix(nrow=7,ncol=configs.count,dimnames=list(c("mean","ci95%","md25%","md50%","md75%","max","min"),c(1:configs.count)))
  for (cc in (1:configs.count)) {
    printvalues["mean",cc]=mean(resultsBIG[(1:length(configs.recursion))[configs.recursion==cr],cc,c(1:(results.count-results.skip))])
    printvalues["ci95%",cc]=qnorm(0.975)*sd(resultsBIG[(1:length(configs.recursion))[configs.recursion==cr],cc,c(1:(results.count-results.skip))])/sqrt(length(resultsBIG[(1:length(configs.recursion))[configs.recursion==cr],cc,c(1:(results.count-results.skip))]))
    printvalues[c("md25%","md50%","md75%"),cc]=quantile(resultsBIG[(1:length(configs.recursion))[configs.recursion==cr],cc,c(1:(results.count-results.skip))],probs=c(0.25,0.5,0.75))
    printvalues["max",cc]=max(resultsBIG[(1:length(configs.recursion))[configs.recursion==cr],cc,c(1:(results.count-results.skip))])
    printvalues["min",cc]=min(resultsBIG[(1:length(configs.recursion))[configs.recursion==cr],cc,c(1:(results.count-results.skip))])
    printthrough["mean",cc]=mean(throughput[[(1:length(configs.recursion))[configs.recursion==cr],cc]])
    printthrough["ci95%",cc]=qnorm(0.975)*sd(throughput[[(1:length(configs.recursion))[configs.recursion==cr],cc]])/sqrt(length(throughput[[(1:length(configs.recursion))[configs.recursion==cr],cc]]))
    printthrough[c("md25%","md50%","md75%"),cc]=quantile(throughput[[(1:length(configs.recursion))[configs.recursion==cr],cc]],probs=c(0.25,0.5,0.75))
    printthrough["max",cc]=max(throughput[[(1:length(configs.recursion))[configs.recursion==cr],cc]])
    printthrough["min",cc]=min(throughput[[(1:length(configs.recursion))[configs.recursion==cr],cc]])
  }
  resultstext=formatC(printvalues,format="f",digits=4,width=8)
  throughtext=formatC(printthrough,format="f",digits=1,width=12)
  print(resultstext)
  print(throughtext)
  write(paste("Recursion Depth: ", cr),file=outtxt_fn,append=TRUE)
  write("response time",file=outtxt_fn,append=TRUE)
  write.table(resultstext,file=outtxt_fn,append=TRUE,quote=FALSE,sep="\t",col.names=FALSE)
  write("Throughput",file=outtxt_fn,append=TRUE)
  write.table(throughtext,file=outtxt_fn,append=TRUE,quote=FALSE,sep="\t",col.names=FALSE)
  
  concResult <- ""
  headResult <- ""
  for (cc in (1:(configs.count-1))) {
    headResult <- paste(headResult, configs.labels[cc], ",");
	concResult <- paste(concResult, printvalues["mean",cc], ",")
  }
  headResult <- paste(headResult, configs.labels[configs.count])
  concResult <- paste(concResult, printvalues["mean",configs.count])
  
  write(headResult,file=outcsv_fn,append=TRUE)
  write(concResult,file=outcsv_fn,append=TRUE)
}
