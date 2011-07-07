results_fn="C:\\Users\\jwa\\Projects\\Kieker\\software\\kieker\\trunk\\examples\\OverheadEvaluationMicrobenchmark\\tmp\\results-benchmark-recursive\\results.csv"
output_fn="C:\\Users\\jwa\\Projects\\Kieker\\software\\kieker\\trunk\\examples\\OverheadEvaluationMicrobenchmark\\tmp\\results-benchmark-recursive\\results.pdf"
results=read.csv2(results_fn,quote="",colClasses=c("integer","integer","integer","NULL","integer"),comment.char="",
                  header=FALSE,col.names=c("iteration","order_index","recursion_depth","thread_id","duration_nsec"),
                  nrows=1*4*5000000,skip=0*4*5000000+1)
rm(results_fn)
results["rt_msec"]=results["duration_nsec"]/(1000)
results$duration_nsec <- NULL
## iteration order_index recursion_depth rt_msec

configs.labels=c("No Probe","Deactivated Probe","Collecting Data","Writing Data")
configs.colors=c("black","red","yellow","green")
configs=unique(results$order_index)
recdepth=unique(results$recursion_depth)
iteration=unique(results$iteration)
results.count=length(results$"rt_msec") / length(configs) / length(recdepth) / length(iteration)

## We assume same amount of data in each category
buckets.count=1000
buckets.size=results.count/buckets.count

pdf(output_fn, width=10, height=6.25, paper="special")
#par(mfrow=c(length(iteration),length(recdepth)))

## We currently overwrite all recursion depths and iterations, but the last ones
for (citer in iteration) {
  for (crec in recdepth) {
    results.ts = matrix(nrow=length(configs),ncol=buckets.count,byrow=TRUE,dimnames=list(configs.labels,c(1:buckets.count)))
    for (cc in configs) {
      results.temp = subset(results, iteration==citer & recursion_depth==crec & order_index==cc, select=c("rt_msec"))$"rt_msec"
      for (ci in (1:buckets.count)) {
        results.ts[cc,ci] <- mean(results.temp[(((ci-1)*buckets.size)+1):(ci*buckets.size)])
      }
    }
    ts.plot(
      ts(results.ts[1,],end=results.count,deltat=buckets.size),
      ts(results.ts[2,],end=results.count,deltat=buckets.size),
      ts(results.ts[3,],end=results.count,deltat=buckets.size),
      ts(results.ts[4,],end=results.count,deltat=buckets.size),
      gpars=list(ylim=c(500,506), 
                 col=configs.colors))
    legend("topright",inset=c(0.01,0.01),legend=c(rev(configs.labels)),lty="solid",col=rev(configs.colors),bg="white",title="Mean execution time of ...",ncol=2)
    title(main=paste("Iteration: ", citer, "  Recursion Depth: ", crec),ylab="Execution Time (µs)")
  }
}
rm(citer,crec,cc,ci,results.temp)
dev.off()
