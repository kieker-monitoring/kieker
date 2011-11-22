data_fn="tmp/"
folder_fn="results-benchmark-recursive"
results_fn=paste(data_fn,folder_fn,"/results.csv",sep="")
output_fn=paste(data_fn,folder_fn,"/results-timeseries.pdf",sep="")

configs.loop=10
configs.recursion=10
configs.count=2
configs.labels=c("Collecting Data","Writing Data")
configs.colors=c("red","green")

## We assume same amount of data in each category
results.count=2000000
buckets.count=1000
buckets.size=results.count/buckets.count


results.ts = matrix(nrow=configs.count,ncol=buckets.count,byrow=TRUE,dimnames=list(configs.labels,c(1:buckets.count)))

pdf(output_fn, width=10, height=6.25, paper="special")

for (cr in (1:configs.recursion)) {
  for (cl in (1:configs.loop)) {
    for (cc in (1:configs.count)) {
      results_fn_temp=paste(results_fn, "-", cl, "-", cr, "-", cc, ".csv", sep="")
      results=read.csv2(results_fn_temp,quote="",colClasses=c("NULL","integer"),comment.char="",col.names=c("thread_id","duration_nsec"))
      results["rt_musec"]=results["duration_nsec"]/(1000)
      results$duration_nsec <- NULL
      for (ci in (1:buckets.count)) {
        results.ts[cc,ci] <- mean(results[(((ci-1)*buckets.size)+1):(ci*buckets.size),"rt_musec"])
      }
      rm(results,results_fn_temp)
    }
    ts.plot(
      ts(results.ts[1,],end=results.count,deltat=buckets.size),
      ts(results.ts[2,],end=results.count,deltat=buckets.size),
      gpars=list(ylim=c(500,520),col=configs.colors,xlab="Executions"))
      legend("topright",inset=c(0.01,0.01),legend=c(rev(configs.labels)),lty="solid",col=rev(configs.colors),bg="white",title="Mean execution time of ...",ncol=2)
    title(main=paste("Iteration: ", cl, "  Recursion Depth: ", cr, sep=""),ylab="Execution Time (µs)")
  }
}
invisible(dev.off())
