haveFBasics=require(fBasics)
haveXtable=require(xtable)

results.df=read.table(results_fn,header=TRUE, sep=";", stringsAsFactors=FALSE)
results.df["rt_msec"]=results.df["duration_nsec"]/(1000)

attach(results.df)
configs=unique(order_index)
configs.count=length(configs)

pdf(output_fn, width=8, height=5, paper="special")
at=seq(from=1.5,by=1,length.out=configs.count)
labels=c(
  expression(Delta["A"]),
  expression(Delta["A"]+Delta["B"]),
  expression(Delta["A"]+Delta["B"]+Delta["C"]),
  expression(Delta["A"]+Delta["B"]+Delta["C"]+Delta["D"]))
boxplot(
  rt_msec ~ order_index, 
  data=results.df, 
  ylab="Response time (microseconds)", 
  xlim=c(1,configs.count+1), 
  col="lightgray", 
  at=at, 
  names=c("1. No instr.","2. Empty probe","3. Data collection","4. Writing to disk"),
  outline=FALSE)
mtext(labels,1,at=at,line=2)
mtext("Measurement configuration",1,line=3.5)
if(haveFBasics){
  legend("topleft", legend="95% CI", pch=18, lty=1, col="red", bg="white")
}else{
  legend("topleft", legend="mean", pch=18, col="red", bg="white")
}

rts_ms_allBasicStats=data.frame()
for (i in 1:configs.count){
	curVals=subset(results.df, order_index == configs[i])["rt_msec"]
	meanval=mean(curVals)
  points(x=i+0.5,meanval,pch=18,col="red") # mean
	if (haveFBasics){ # CI
    stats=basicStats(curVals, ci=0.95)
    LCLMean=stats["LCL Mean",]
    UCLMean=stats["UCL Mean",]
    bar.xpos=at[i]
    bar.width=0.15
    lines(c(bar.xpos,bar.xpos),c(LCLMean,UCLMean),col="red")
    lines(c(bar.xpos-bar.width/2,bar.xpos+bar.width/2),c(LCLMean,LCLMean),col="red")
    lines(c(bar.xpos-bar.width/2,bar.xpos+bar.width/2),c(UCLMean,UCLMean),col="red")
		if(haveXtable){
			if (i==1){
				# init common data frame
				rts_ms_allBasicStats=stats
			} else {
				# add to common data frame
				rts_ms_allBasicStats=cbind(rts_ms_allBasicStats,stats)
			}
		}
  }
}
if(haveXtable){
	names(rts_ms_allBasicStats)=labels
	stats.xtable=xtable(rts_ms_allBasicStats,caption="Response time (microseconds) statistics")
	print(stats.xtable, type="html", file=paste(results_fn,"-basicStats.html", sep=""))  
	print(stats.xtable, type="latex", file=paste(results_fn,"-basicStats.inc.tex", sep=""))  
}
detach(results.df)
dev.off()
