haveFBasics=require(fBasics)
haveXtable=require(xtable)

baseresults.df=read.table(results_fn,header=TRUE, sep=";", stringsAsFactors=FALSE)
baseresults.df["rt_msec"]=baseresults.df["duration_nsec"]/(1000)

configs=unique(baseresults.df$order_index)
configs.count=length(configs)
recdepth=unique(baseresults.df$recursion_depth)
recdepth.count=length(recdepth)

pdf(output_fn, width=10, height=5, paper="special")

for (j in 1:recdepth.count){
    results.df=subset(baseresults.df, baseresults.df$recursion_depth==(j-1))
    attach(results.df)
    at=seq(from=1.5,by=1,length.out=configs.count)
    # Outliers are ugly, cause there are way to many points!!!
    boxplot(rt_msec ~ order_index, data=results.df, ylab="Response time (microseconds)",  xlim=c(1,configs.count+1), col="lightgray", at=at, xaxt="n", outline=FALSE)
    labels=c("1. No instr.", "2. Dummy probe", "3. No logging", "4. Logging")
    axis(side=1, at=at, labels=labels)
    mtext(paste("Monitoring configuration (Recursion Depth: ",j-1,")",sep=""), side=1, line=2)
    #legend("bottomleft", legend=c("mean"), pch=c(18), pt.bg=c("black"), pt.cex=2, ncol=1, bg="white")
    if(haveFBasics){
      legend("topleft", legend=c("95% CI"), pch=21, pt.cex=2, col="red", lwd=1) #bg="white",
    }

    rts_ms_allBasicStats=data.frame()
    for (i in 1:configs.count){
        curVals=subset(results.df, order_index == configs[i])["rt_msec"]
        meanval=mean(curVals)
        points(x=i+0.5,meanval,pch=18,cex=2) # mean
        if (haveFBasics){
            stats=basicStats(curVals, ci=0.95)
            LCLMean=stats["LCL Mean",]
            UCLMean=stats["UCL Mean",]
            bar.xpos=at[i]
            bar.width=0.15
            lines(c(bar.xpos,bar.xpos),c(LCLMean,UCLMean),col = "red")
            lines(c(bar.xpos-bar.width/2,bar.xpos+bar.width/2),c(LCLMean,LCLMean),col = "red")
            lines(c(bar.xpos-bar.width/2,bar.xpos+bar.width/2),c(UCLMean,UCLMean),col = "red")
            points(bar.xpos, meanval,cex=2, col = "red")
#            if(haveXtable){
#                if (i==1){
#                    # init common data frame
#                    rts_ms_allBasicStats=stats
#                } else {
#                    # add to common data frame
#                    rts_ms_allBasicStats=cbind(rts_ms_allBasicStats,stats)
#                }
#            }
        }
    }
#    if(haveXtable){
#        names(rts_ms_allBasicStats)=labels
#        stats.xtable=xtable(rts_ms_allBasicStats,caption="Response time (milliseconds) statistics")
#        print(stats.xtable, type="html", file=paste(results_fn,"-basicStats.html", sep=""))
#        print(stats.xtable, type="latex", file=paste(results_fn,"-basicStats.inc.tex", sep=""))
#    }
    detach(results.df)
}
dev.off()
