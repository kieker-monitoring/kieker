#rm(list=ls(all=TRUE))
#data_fn="tmp/"
#folder_fn="results-benchmark-binary"
#results_fn=paste(data_fn,folder_fn,"/raw",sep="")
#output_fn=paste(data_fn,folder_fn,"/results-bars.pdf",sep="")
#outtxt_fn=paste(data_fn,folder_fn,"/results-text.txt",sep="")

configs.threads=1
#configs.loop=10
#configs.recursion=c(10)
#configs.labels=c("No Probe","Inactive Probe","Collecting Data","Writing Data")
configs.count=length(configs.labels)
#results.count=2000000
#results.skip=1000000

#bars.minval=500
#bars.maxval=600

## "[ recursion , config , loop ]"
resultsBIG <- array(dim=c(length(configs.recursion),configs.count,configs.threads*configs.loop*(results.count-results.skip)),dimnames=list(configs.recursion,configs.labels,c(1:(configs.threads*configs.loop*(results.count-results.skip)))))
for (cr in configs.recursion) {
  for (cc in (1:configs.count)) {
    for (cl in (1:configs.loop)) {
      results_fn_temp=paste(results_fn, "-", cl, "-", cr, "-", cc, ".csv", sep="")
      for (ct in (1:configs.threads)) {
        results=read.csv2(results_fn_temp,nrows=(results.count-results.skip),skip=(ct-1)*results.count+results.skip,quote="",colClasses=c("NULL","numeric"),comment.char="",col.names=c("thread_id","duration_nsec"),header=FALSE)
        resultsBIG[(1:length(configs.recursion))[configs.recursion==cr],cc,c(((cl-1)*configs.threads*(results.count-results.skip)+1):(cl*configs.threads*(results.count-results.skip)))] <- results[["duration_nsec"]]/(1000)
      }
      rm(results,results_fn_temp)
    }
  }
}

pdf(output_fn, width=8, height=5, paper="special")
plot.new()
plot.window(xlim=c(min(configs.recursion)-0.5,max(configs.recursion)+0.5),ylim=c(bars.minval,bars.maxval))
axis(1,at=configs.recursion)
axis(2)
title(xlab="Recursion Depth (Number of Executions)",ylab=expression(paste("Execution Time (",mu,"s)")))
for (cr in configs.recursion) {
  printvalues = matrix(nrow=6,ncol=configs.count,dimnames=list(c("mean","ci95%","md25%","md50%","md75%","through"),c(1:configs.count)))
  for (cc in (1:configs.count)) {
    printvalues["mean",cc]=mean(resultsBIG[(1:length(configs.recursion))[configs.recursion==cr],cc,c(1:(results.count-results.skip))])
    printvalues["ci95%",cc]=qnorm(0.975)*sd(resultsBIG[(1:length(configs.recursion))[configs.recursion==cr],cc,c(1:(results.count-results.skip))])/sqrt(length(resultsBIG[(1:length(configs.recursion))[configs.recursion==cr],cc,c(1:(results.count-results.skip))]))
    printvalues[c("md25%","md50%","md75%"),cc]=quantile(resultsBIG[(1:length(configs.recursion))[configs.recursion==cr],cc,c(1:(results.count-results.skip))],probs=c(0.25,0.5,0.75))
    printvalues["through",cc]=((results.count-results.skip) * 1000*1000) / sum(resultsBIG[(1:length(configs.recursion))[configs.recursion==cr],cc,c(1:(results.count-results.skip))])
  }
  #meanvalues
  for (cc in (configs.count:2)) {
    rect(cr-0.3,printvalues["mean",cc-1],cr+0.5,printvalues["mean",cc])
  }
  rect(cr-0.3,0,cr+0.5,printvalues["mean",1])
  for (cc in (1:configs.count)) {
    lines(c(cr+0.41,cr+0.49),c(printvalues["mean",cc]+printvalues["ci95%",cc],printvalues["mean",cc]+printvalues["ci95%",cc]),col="red")
    lines(c(cr+0.45,cr+0.45),c(printvalues["mean",cc]-printvalues["ci95%",cc],printvalues["mean",cc]+printvalues["ci95%",cc]),col="red")
    lines(c(cr+0.41,cr+0.49),c(printvalues["mean",cc]-printvalues["ci95%",cc],printvalues["mean",cc]-printvalues["ci95%",cc]),col="red")
  }
  #median
  for (cc in (configs.count:2)) {
    rect(cr-0.4,printvalues["md50%",cc-1],cr+0.4,printvalues["md50%",cc],col="white",border="black")
    rect(cr-0.4,printvalues["md50%",cc-1],cr+0.4,printvalues["md50%",cc],angle=45,density=cc*10)
  }
  rect(cr-0.4,0,cr+0.4,printvalues["md50%",1],col="white",border="black")
  rect(cr-0.4,0,cr+0.4,printvalues["md50%",1],angle=45,density=10)
  for (cc in (1:configs.count)) {
    lines(c(cr-0.39,cr-0.31),c(printvalues["md75%",cc],printvalues["md75%",cc]),col="red")
    lines(c(cr-0.35,cr-0.35),c(printvalues["md25%",cc],printvalues["md75%",cc]),col="red")
    lines(c(cr-0.39,cr-0.31),c(printvalues["md25%",cc],printvalues["md25%",cc]),col="red")
  }
  for (cc in (2:configs.count)) {
    labeltext=formatC(printvalues["md50%",cc]-printvalues["md50%",cc-1],format="f",digits=1)
      rect(cr-(strwidth(labeltext)*0.5),printvalues["md50%",cc]-strheight(labeltext),cr+(strwidth(labeltext)*0.5),printvalues["md50%",cc],col="white",border="black")
      text(cr,printvalues["md50%",cc],labels=labeltext,cex=0.75,col="black",pos=1,offset=0.1)
  }
  resultstext=formatC(printvalues,format="f",digits=4,width=8)
  print(resultstext)
  write(paste("Recursion Depth: ", cr),file=outtxt_fn,append=TRUE)
  write.table(resultstext,file=outtxt_fn,append=TRUE,quote=FALSE,sep="\t",col.names=FALSE)
}
invisible(dev.off())
