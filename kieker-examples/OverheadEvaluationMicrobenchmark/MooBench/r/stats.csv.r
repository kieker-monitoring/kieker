############################################
# R - script to collect all moobench results
############################################

# these values are here only as documentation. The parameters are set by benchmark.sh
#rm(list=ls(all=TRUE))
#data_fn="data/"
#folder_fn="results-benchmark-binary"
#results_fn=paste(data_fn,folder_fn,"/raw",sep="")
#outtxt_fn=paste(data_fn,folder_fn,"/results-text.txt",sep="")
#results_fn="raw"
#outtxt_fn="results-text.txt"

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

# number of Kieker writer configurations 
numberOfWriters <- length(configs.labels)

##########
# Setup variables
numberOfValues <- configs.loop*(results.count-results.skip)
numbers <- c(1:(numberOfValues))
resultDimensionNames <- list(configs.labels, numbers)

# result values
resultsBIG <- array(dim=c(numberOfWriters, numberOfValues), dimnames=resultDimensionNames)

##########
# Create result

## "[ recursion , config , loop ]"

recursion_depth <- configs.recursion
numOfRowsToRead <- results.count-results.skip

for (writer_idx in (1:numberOfWriters)) {
   recordsPerSecond = c()
   rpsLastDuration = 0
   rpsCount = 0
   file_idx <- writer_idx - 1

   # loop
   for (loop_counter in (1:configs.loop)) {
      results_fn_filepath <- paste(results_fn, "-", loop_counter, "-", recursion_depth, "-", file_idx, ".csv", sep="")
      results <- read.csv2(results_fn_filepath, nrows=numOfRowsToRead, skip=results.skip, quote="", colClasses=c("NULL","numeric"), comment.char="", col.names=c("thread_id", "duration_nsec"), header=FALSE)
      trx_idx <- c(1:numOfRowsToRead)
      resultsBIG[writer_idx,trx_idx] <- results[["duration_nsec"]]
   }
}

qnorm_value <- qnorm(0.975)

# print results
printDimensionNames <- list(c("mean","ci95%","md25%","md50%","md75%","max","min"), c(1:numberOfWriters))
# row number == number of computed result values, e.g., mean, min, max
printvalues <- matrix(nrow=7, ncol=numberOfWriters, dimnames=printDimensionNames)

for (writer_idx in (1:numberOfWriters)) {
   idx_mult <- c(1:numOfRowsToRead)

   valuesBIG <- resultsBIG[writer_idx,idx_mult]

   printvalues["mean",writer_idx] <- mean(valuesBIG)
   printvalues["ci95%",writer_idx] <- qnorm_value*sd(valuesBIG)/sqrt(length(valuesBIG))
   printvalues[c("md25%","md50%","md75%"),writer_idx] <- quantile(valuesBIG, probs=c(0.25, 0.5, 0.75))
   printvalues["max",writer_idx] <- max(valuesBIG)
   printvalues["min",writer_idx] <- min(valuesBIG)
}
resultstext <- formatC(printvalues,format="f",digits=4,width=8)

print(resultstext)

write(paste("Recursion Depth: ", recursion_depth),file=outtxt_fn,append=TRUE)
write("response time",file=outtxt_fn,append=TRUE)
write.table(resultstext,file=outtxt_fn,append=TRUE,quote=FALSE,sep="\t",col.names=FALSE)

concResult <- ""
headResult <- ""
# write the first n-1 elements preceded by a comma (,)
for (writer_idx in (1:(numberOfWriters-1))) {
   headResult <- paste(headResult, configs.labels[writer_idx], ",")
   concResult <- paste(concResult, printvalues["mean",writer_idx], ",")
}
# write the last without a comma
headResult <- paste(headResult, configs.labels[numberOfWriters])
concResult <- paste(concResult, printvalues["mean", numberOfWriters])
  
write(headResult,file=outcsv_fn,append=TRUE)
write(concResult,file=outcsv_fn,append=TRUE)

# end
