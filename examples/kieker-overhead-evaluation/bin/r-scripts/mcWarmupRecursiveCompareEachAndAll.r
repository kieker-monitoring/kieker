haveFBasics=require(fBasics)
haveXtable=require(xtable)

baseresults.df=read.table(results_fn,header=TRUE, sep=";", stringsAsFactors=FALSE)
baseresults.df["rt_msec"]=baseresults.df["duration_nsec"]/(1000)

configs=unique(baseresults.df$order_index)
configs.count=length(configs)
recdepth=unique(baseresults.df$recursion_depth)
recdepth.count=length(recdepth)

pdf(output_fn, width=10, height=5, paper="special")

at=seq(from=1,by=1,length.out=recdepth.count*configs.count)
boxplot(
  rt_msec ~ order_index*recursion_depth, 
  data=baseresults.df, 
  ylab="Response time (microseconds)", 
  xlab="Recursion depth", 
  col="lightgray", 
  at=at, 
  outline=FALSE,
  show.names=FALSE
)
at=seq(from=2.5,by=4,length.out=recdepth.count*configs.count)
labels=c(recdepth,1:(recdepth.count*(configs.count-1)))
axis(1,at,labels)
at=seq(from=0.5,by=4,length.out=recdepth.count*configs.count)
abline(v=at,col="lightgray")
if(haveFBasics){
  legend("topleft", legend="95% CI", pch=18, lty=1, col="red", bg="white")
}else{
  legend("topleft", legend="mean", pch=18, col="red", bg="white")
}

at=seq(from=1,by=1,length.out=recdepth.count*configs.count)
rts_ms_allBasicStats=data.frame()
for (i in 1:recdepth.count){
  curConfigVals=subset(baseresults.df, recursion_depth == recdepth[i])
  for (j in 1:configs.count){
    curVals=subset(curConfigVals, order_index == configs[j])["rt_msec"]
    meanval=mean(curVals)
    points(x=(i-1)*configs.count + j,meanval,pch=18,col="red") # mean
    if (haveFBasics){ # CI
      stats=basicStats(curVals, ci=0.95)
      LCLMean=stats["LCL Mean",]
      UCLMean=stats["UCL Mean",]
      bar.xpos=at[(i-1)*configs.count + j]
      bar.width=0.15
      lines(c(bar.xpos,bar.xpos),c(LCLMean,UCLMean),col="red")
      lines(c(bar.xpos-bar.width/2,bar.xpos+bar.width/2),c(LCLMean,LCLMean),col="red")
      lines(c(bar.xpos-bar.width/2,bar.xpos+bar.width/2),c(UCLMean,UCLMean),col="red")
      if(haveXtable){
        if (i==1 && j==1){
          # init common data frame
          rts_ms_allBasicStats=stats
        } else {
          # add to common data frame
          rts_ms_allBasicStats=cbind(rts_ms_allBasicStats,stats)
        }
      }
    }
  }
}
cors=NULL
for (i in 1:configs.count){
  curConfigVals=subset(baseresults.df, order_index == configs[i])
  means = NULL
  for (j in 1:recdepth.count){
    curVals=subset(curConfigVals, recursion_depth == recdepth[j])["rt_msec"]
    means = c(means, mean(curVals))
  }
  cors=c(cors,cor(recdepth,means))
}

if(haveXtable){
	#names(rts_ms_allBasicStats)=labels
	stats.xtable=xtable(rts_ms_allBasicStats,caption="Response time (microseconds) statistics")
	print(stats.xtable, type="html", file=paste(results_fn,"-basicStats.html", sep=""))  
	print(stats.xtable, type="latex", file=paste(results_fn,"-basicStats.inc.tex", sep=""))  
}
dev.off()
