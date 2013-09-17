#rm(list=ls(all=TRUE))
#data_fn="tmp/"
#folder_fn="results-benchmark-binary"
#results_fn=paste(data_fn,folder_fn,"/raw",sep="")
#output_fn=paste(data_fn,folder_fn,"/results-throughput-average.pdf",sep="")

#configs.loop=10
#configs.recursion=c(10)
#configs.labels=c("No Probe","Deactivated Probe","Collecting Data","Writing Data")
#configs.colors=c("black","red","blue","green")
configs.count=length(configs.labels)

## We assume same amount of data in each category
#results.count=20000000
buckets.count=1000
buckets.size=results.count/buckets.count

pdf(output_fn, width=10, height=6.25, paper="special")

for (cr in configs.recursion) {
  results.ts <- array(dim=c(buckets.count,configs.count))
  if (exists("results.temp")) rm(results.temp)
  for (cc in (1:configs.count)) {
    results.bucket <- array(dim=c(buckets.count))
    for (cl in (1:configs.loop)) {
      results_fn_temp=paste(results_fn, "-", cl, "-", cr, "-", cc, ".csv", sep="")
      results=read.csv2(results_fn_temp,quote="",colClasses=c("NULL","numeric"),comment.char="",col.names=c("thread_id","duration_nsec"),nrows=results.count)
      if (exists("results.temp")) {
        results.temp = data.frame(results.temp,results["duration_nsec"]/(1000))
      } else {
        results.temp = data.frame(results["duration_nsec"]/(1000))
      }
      rm(results,results_fn_temp)
    }
    results = rowMeans(results.temp)
    rm(results.temp)
    for (ci in (1:buckets.count)) {
      results.bucket[ci] <- (buckets.size * 1000*1000) / sum(results[(((ci-1)*buckets.size)+1):(ci*buckets.size)])
    }
    results.ts[,cc]=ts(results.bucket,end=results.count,deltat=buckets.size)
    rm(results,results.bucket)
  }
  ts.plot(results.ts,gpars=list(col=configs.colors,xlab="Executions"))
  legend("topright",inset=c(0.01,0.01),legend=c(rev(configs.labels)),lty="solid",col=rev(configs.colors),bg="white",title="Mean throughput of ...",ncol=2)
  title(main=paste("Recursion Depth: ", cr),ylab="Throughput (op/s)")
}
invisible(dev.off())
