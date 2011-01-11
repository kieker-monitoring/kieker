results_fn="C:\\Users\\jwa\\Projects\\Kieker\\software\\kieker\\trunk\\examples\\OverheadEvaluationMicrobenchmark\\tmp\\results-mcWarmupThreaded-8\\results.csv"
results.df=read.table(results_fn, header=TRUE, sep=";", stringsAsFactors=FALSE)
results.df["rt_msec"]=results.df["duration_nsec"]/(1000)
curvals=t(subset(results.df, (order_index==0)&(threadid=="Thread-0"))["rt_msec"])
  
bin = function(x, bin_width) {
  bin_num = length(x)/bin_width
  res = vector(mode="double",length=bin_num)
  for(i in (1:bin_num)) {
    res[i] = mean(x[(((i-1)*bin_width)+1):(i*bin_width)])
  }
  return(res)
}

myplotcolors=c("gray5", "gray10", "gray15", "gray20", "gray5", "gray10", "gray15", "gray20", "gray25", "gray30", "gray35", "gray40", "gray45", "gray50", "gray55", "gray60", "gray65", "gray70", "gray75", "gray80", "gray85", "gray90", "gray95", "gray100")
plot(x=NULL,xlim=c(0,100), ylim=c(500,510))
for (i in (0:7)) {
  cv=bin(t(subset(results.df, (order_index==0)&(threadid==paste("Thread-",i,sep="")))["rt_msec"]), 1000)
  lines(cv,type="l",col=myplotcolors[i])
}
