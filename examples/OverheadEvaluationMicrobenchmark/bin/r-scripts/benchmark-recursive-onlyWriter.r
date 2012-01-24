data_fn="tmp/"
folder_fn="results-S1"
results_fn=paste(data_fn,folder_fn,"/results.csv",sep="")
output_fn=paste(data_fn,folder_fn,"/results-bars.pdf",sep="")
outtxt_fn=paste(data_fn,folder_fn,"/results-text.txt",sep="")

configs.loop=10
configs.recursion=c(1)
configs.labels=c("Writing Data")
configs.count=length(configs.labels)
results.count=2000000
results.skip =1000000

## "[ recursion , config , loop ]"
meanvalues <- array(dim=c(length(configs.recursion),configs.count,configs.loop,2),dimnames=list(configs.recursion,configs.labels,c(1:configs.loop),c("mean","ci95%")))
medianvalues <- array(dim=c(length(configs.recursion),configs.count,configs.loop,3),dimnames=list(configs.recursion,configs.labels,c(1:configs.loop),c("md25%","md50%","md75%")))
resultsBIG <- array(dim=c(length(configs.recursion),configs.count,configs.loop*(results.count-results.skip)),dimnames=list(configs.recursion,configs.labels,c(1:(configs.loop*(results.count-results.skip)))))
for (cr in configs.recursion) {
  for (cc in (1:configs.count)) {
    for (cl in (1:configs.loop)) {
      results_fn_temp=paste(results_fn, "-", cl, "-", cr, "-", cc, ".csv", sep="")
      results=read.csv2(results_fn_temp,nrows=(results.count-results.skip),skip=results.skip,quote="",colClasses=c("NULL","integer"),comment.char="",col.names=c("thread_id","duration_nsec"),header=FALSE)
      resultsBIG[cr,cc,c(((cl-1)*(results.count-results.skip)+1):(cl*(results.count-results.skip)))] <- results[["duration_nsec"]]/(1000)
      meanvalues[(1:length(configs.recursion))[configs.recursion==cr],cc,cl,"mean"] <- mean(results[["duration_nsec"]])/(1000)
      meanvalues[(1:length(configs.recursion))[configs.recursion==cr],cc,cl,"ci95%"] <- qnorm(0.975)*sd(results[["duration_nsec"]])/1000/sqrt(length(results[["duration_nsec"]]))
      medianvalues[(1:length(configs.recursion))[configs.recursion==cr],cc,cl,] <- quantile(results[["duration_nsec"]],probs=c(0.25,0.5,0.75))/1000
      rm(results,results_fn_temp)
    }
  }
}

pdf(output_fn, width=8, height=5, paper="special")
plot.new()
plot.window(xlim=c(min(configs.recursion)-0.5,max(configs.recursion)+0.5),ylim=c(500,max(meanvalues[,,,"mean"])))
axis(1,at=configs.recursion)
axis(2)
title(xlab="Recursion Depth (Number of Executions)",ylab="Execution Time (µs)")
for (cr in (1:length(configs.recursion))) {
  cc=1
  printvalues = matrix(nrow=5,ncol=1,dimnames=list(c("mean","ci95%","md25%","md50%","md75%"),c(1:configs.count)))
  printvalues["mean",cc] = mean(resultsBIG[cr,cc,]) #mean(meanvalues[cr,cc,,"mean"])
  printvalues["ci95%",cc] = qnorm(0.975) * sd(resultsBIG[cr,cc,]) / sqrt(length(resultsBIG[cr,cc,])) #mean(meanvalues[cr,cc,,"ci95%"])
  printvalues[c("md25%","md50%","md75%"),cc] = quantile(resultsBIG[cr,cc,],probs=c(0.25,0.5,0.75)) #mean(medianvalues[cr,cc,,"md25%"])
  #printvalues["md50%",cc]=mean(medianvalues[cr,cc,,"md50%"])
  #printvalues["md75%",cc]=mean(medianvalues[cr,cc,,"md75%"])
  #meanvalues
  rect(cr-0.3,0,cr+0.5,printvalues["mean",1])
  lines(c(cr+0.41,cr+0.49),c(printvalues["mean",cc]+printvalues["ci95%",cc],printvalues["mean",cc]+printvalues["ci95%",cc]),col="red")
  lines(c(cr+0.45,cr+0.45),c(printvalues["mean",cc]-printvalues["ci95%",cc],printvalues["mean",cc]+printvalues["ci95%",cc]),col="red")
  lines(c(cr+0.41,cr+0.49),c(printvalues["mean",cc]-printvalues["ci95%",cc],printvalues["mean",cc]-printvalues["ci95%",cc]),col="red")
  #median
  rect(cr-0.4,0,cr+0.4,printvalues["md50%",1],col="white",border="black")
  rect(cr-0.4,0,cr+0.4,printvalues["md50%",1],angle=135,density=5)
  lines(c(cr-0.39,cr-0.31),c(printvalues["md75%",cc],printvalues["md75%",cc]),col="red")
  lines(c(cr-0.35,cr-0.35),c(printvalues["md25%",cc],printvalues["md75%",cc]),col="red")
  lines(c(cr-0.39,cr-0.31),c(printvalues["md25%",cc],printvalues["md25%",cc]),col="red")
  labeltext=formatC(printvalues["md50%",cc]-printvalues["md50%",cc-1],format="f",digits=1)
    rect(cr-(strwidth(labeltext)*0.5),printvalues["md50%",cc]-strheight(labeltext),cr+(strwidth(labeltext)*0.5),printvalues["md50%",cc],col="white",border="black")
    text(cr,printvalues["md50%",cc],labels=labeltext,cex=0.75,col="black",pos=1,offset=0.1)
  resultstext=formatC(printvalues,format="f",digits=4,width=8)
  print(resultstext)
  write.table(resultstext,file=outtxt_fn,append=TRUE,quote=FALSE,sep="\t",col.names=FALSE)
}
invisible(dev.off())
