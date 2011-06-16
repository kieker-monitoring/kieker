results_fn="C:\\Users\\jwa\\Projects\\Kieker\\software\\kieker\\trunk\\examples\\OverheadEvaluationMicrobenchmark\\tmp\\results-benchmark-recursive\\results.csv"
#output_fn="C:\\Users\\jwa\\Projects\\Kieker\\software\\kieker\\trunk\\examples\\OverheadEvaluationMicrobenchmark\\tmp\\results-benchmark-recursive\\results.pdf"
results=read.csv2(results_fn,quote="",colClasses=c("NULL","integer","integer","integer","NULL","integer"))
rm(results_fn)
results["rt_msec"]=results["duration_nsec"]/(1000)
results$duration_nsec <- NULL
## iteration order_index recursion_depth rt_msec

configs=unique(results$order_index)
configs.labels=c("No Probe","Deactivated Probe","Collecting Data","Writing Data")
recdepth=unique(results$recursion_depth)
results.count=length(results$"rt_msec") / length(configs) / length(recdepth)
iteration=unique(results$iteration)

## We assume same amount of data in each category
buckets.count=1000
buckets.size= results.count / buckets.count

## We currently overwrite all recursion depths and iterations, but the last ones
for (citer in iteration) {
  for (crec in recdepth) {
    results.ts = matrix(nrow=length(configs),ncol=buckets.count,byrow=TRUE,dimnames=list(configs.labels,c(1:buckets.count)))
    for (cc in configs) {
      results.temp = subset(results, iteration==citer & recursion_depth==crec & order_index==cc, select=c("rt_msec"))$"rt_msec"
      results.ts[cc,1] <- mean(results.temp[1:(1.5*buckets.size)])
      for (ci in (2:(buckets.count-1))) {
        results.ts[cc,ci] <- mean(results.temp[(((ci-1)*buckets.size)+1-(0.5*buckets.size)):((ci*buckets.size)+(0.5*buckets.size))])
      }
      results.ts[cc,buckets.count] <- mean(results.temp[(((buckets.count-1)*buckets.size)+1-(0.5*buckets.size)):(buckets.count*buckets.size)])
    }
  }
}
rm(citer,crec,cc,ci,results.temp)

ts.plot(
  ts(results.ts[1,],end=results.count,deltat=buckets.size),
  ts(results.ts[2,],end=results.count,deltat=buckets.size),
  ts(results.ts[3,],end=results.count,deltat=buckets.size),
  ts(results.ts[4,],end=results.count,deltat=buckets.size),
  gpars=list(ylim=c(500,506), 
             col=c("black","red","yellow","green")))
