############################################
# R - script to collect all moobench results
############################################

#rm(list=ls(all=TRUE))
#data_fn="data/"
#folder_fn="results-benchmark-binary"
#results_fn=paste(data_fn,folder_fn,"/raw",sep="")
#outtxt_fn=paste(data_fn,folder_fn,"/results-text.txt",sep="")
#results_fn="raw"
#outtxt_fn="results-text.txt"

configs.threads=1

#########
# These are configuration parameters which are automatically prepended to this file by the benchmark.sh script.
# Therefore, they must not be set here. The following lines only serve as documentation.
#configs.loop=10
#configs.recursion=c(10)
#configs.labels=c("No Probe","Inactive Probe","Collecting Data","Writing Data (ASCII)", "Writing Data (Bin)")
#results.count=2000000
#results.skip=1000000

#bars.minval=500
#bars.maxval=600


##########
# Process configuration
configs.count=length(configs.labels)

##########
# Setup variables
throughput = array(list(),dim=c(length(configs.recursion),configs.count))

##########
# Create result

## "[ recursion , config , loop ]"
numberOfValues <- configs.threads*configs.loop*(results.count-results.skip)
numbers <- c(1:(numberOfValues))
resultDimensionNames <- list(configs.recursion, configs.labels, numbers)
resultsBIG <- array(dim=c(length(configs.recursion), configs.count, numberOfValues), dimnames=resultDimensionNames)

for (recursion_depth in configs.recursion) {
   recursion_depth_idx <- (1:length(configs.recursion))[configs.recursion==recursion_depth]
   for (cc in (1:configs.count)) {
     recordsPerSecond = c()
     rpsLastDuration = 0
     rpsCount = 0
     cc_file <- cc - 1

     # loop
     for (loop_counter in (1:configs.loop)) {
        results_fn_filepath <- paste(results_fn, "-", loop_counter, "-", recursion_depth, "-", cc_file, ".csv", sep="")
 
        # threads
        for (thread_counter in (1:configs.threads)) {
          results <- read.csv2(results_fn_filepath, nrows=(results.count-results.skip), skip=(thread_counter-1)*results.count+results.skip, quote="", colClasses=c("NULL","numeric"), comment.char="", col.names=c("thread_id", "duration_nsec"), header=FALSE)

          lower <- (loop_counter-1)*configs.threads*(results.count-results.skip)+1
          upper <- loop_counter*configs.threads*(results.count-results.skip)
          trx_idx <- c(lower:upper)

          resultsBIG[recursion_depth_idx,cc,trx_idx] <- results[["duration_nsec"]]/(1000)

          # duration
          for (duration in results[["duration_nsec"]]) {
             if (rpsLastDuration + duration >= 1000000000) {
                recordsPerSecond <- c(recordsPerSecond, rpsCount)
                message ("pp ", recordsPerSecond)
                rpsCount = 0
                rpsLastDuration = 1000000000 - rpsLastDuration
             }
             while (duration > 1000000000) {
                message("time ", duration)
                recordsPerSecond <- c(recordsPerSecond, 0)
                duration = duration - 1000000000
             }
             rpsCount = rpsCount + 1
             rpsLastDuration = rpsLastDuration + duration
          }
        }
        rm(results, results_fn_filepath)
      }

      # done
      recordsPerSecond <- c(recordsPerSecond, rpsCount)
      throughput[[recursion_depth_idx, cc]] <- recordsPerSecond
      rm(recordsPerSecond, rpsLastDuration, rpsCount)
   }
}

qnorm_value <- qnorm(0.975)

for (recursion_depth in configs.recursion) {
   printDimensionNames <- list(c("mean","ci95%","md25%","md50%","md75%","max","min"), c(1:configs.count))
   printvalues <- matrix(nrow=7, ncol=configs.count, dimnames=printDimensionNames)
   printthrough <- matrix(nrow=7, ncol=configs.count, dimnames=printDimensionNames)

   for (cc in (1:configs.count)) {
      idx_1 <- (1:length(configs.recursion))[configs.recursion==recursion_depth]
      idx_mult <- c(1:(results.count-results.skip))

      valuesBIG <- resultsBIG[idx_1,cc,idx_mult]
      valuesThroughput <- throughput[[idx_1,cc]]

      printvalues["mean",cc] <- mean(valuesBIG)
      printvalues["ci95%",cc] <- qnorm_value*sd(valuesBIG)/sqrt(length(valuesBIG))
      printvalues[c("md25%","md50%","md75%"),cc] <- quantile(valuesBIG, probs=c(0.25, 0.5, 0.75))
      printvalues["max",cc] <- max(valuesBIG)
      printvalues["min",cc] <- min(valuesBIG)
      printthrough["mean",cc] <- mean(valuesThroughput)
      printthrough["ci95%",cc] <- qnorm_value*sd(valuesThroughput/sqrt(length(valuesThroughput)))
      printthrough[c("md25%","md50%","md75%"),cc] <- quantile(valuesThroughput, probs=c(0.25, 0.5, 0.75))
      printthrough["max",cc] <- max(valuesThroughput)
      printthrough["min",cc] <- min(valuesThroughput)
   }
   resultstext=formatC(printvalues,format="f",digits=4,width=8)
   throughtext=formatC(printthrough,format="f",digits=1,width=12)

   print(resultstext)
   print(throughtext)

   write(paste("Recursion Depth: ", recursion_depth),file=outtxt_fn,append=TRUE)
   write("response time",file=outtxt_fn,append=TRUE)
   write.table(resultstext,file=outtxt_fn,append=TRUE,quote=FALSE,sep="\t",col.names=FALSE)
   write("Throughput",file=outtxt_fn,append=TRUE)
   write.table(throughtext,file=outtxt_fn,append=TRUE,quote=FALSE,sep="\t",col.names=FALSE)
  
   concResult <- ""
   headResult <- ""
   for (cc in (1:(configs.count-1))) {
      headResult <- paste(headResult, configs.labels[cc], ",")
      concResult <- paste(concResult, printvalues["mean",cc], ",")
   }
   headResult <- paste(headResult, configs.labels[configs.count])
   concResult <- paste(concResult, printvalues["mean",configs.count])
  
   write(headResult,file=outcsv_fn,append=TRUE)
   write(concResult,file=outcsv_fn,append=TRUE)
}
