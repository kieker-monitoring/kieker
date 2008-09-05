##
n.requesttypes=12
n.accesslogs=3

##
fn.noinstr="20080905-132521-noinstr-accesslog-main_filtered.csv-pathids"
fn.instr081="20080828-174456-tpmon081-accesslog-main_filtered.csv-pathids"
fn.instr090="20080903-061257-tpmon090-accesslog-main_filtered.csv-pathids"
# 20080902-225445-tpmon090-accesslog-main_filtered.csv-pathids

##
label.noinstr="No instrumentation"
label.instr081="Tpmon v.0.81"
label.instr090="Tpmon v.0.90"

col.noinstr="gray"
col.instr081="blue"
col.instr090="red"


## read data files
accesslog.read = function (fn) {
	accesslog_raw <- read.table (file=fn, header=TRUE, sep=' ', quote='"\'', dec='.',  na.strings = "NA", nrows = -1, skip =  0, row.names = NULL, fill = FALSE, strip.white = FALSE, blank.lines.skip = TRUE, comment.char="#",stringsAsFactors=FALSE)

	## convert time string hh:mm:ss to double representing the minute of day
	convertTime = function  (timeStr){
	    hms = as.numeric(unlist(strsplit(as.character(timeStr),":")))
	    hms[1]*60+hms[2]+(hms[3]/60)
	}
	accesslog_raw["time_min"]=sapply(accesslog_raw[["time"]],convertTime)

	startMin=min(accesslog_raw[["time_min"]]) 
	accesslog_raw["exp_min"]=accesslog_raw[["time_min"]]-startMin
	accesslog_raw
}

data.noinstr=accesslog.read(fn.noinstr)
data.instr081=accesslog.read(fn.instr081)
data.instr090=accesslog.read(fn.instr090)

## TODO: make sure, that request types have the same order
## maybe the easiest is to do this with a bash script using sed
## hopefully R is smart and sorts alphabetically

## TODO: divide into two plots
data.noinstr.1 = subset(data.noinstr, pathid<=6)
data.noinstr.2 = subset(data.noinstr, pathid>6)
rm(data.noinstr)
data.instr081.1 = subset(data.instr081, pathid<=6)
data.instr081.2 = subset(data.instr081, pathid>6)
rm(data.instr081)
data.instr090.1 = subset(data.instr090, pathid<=6)
data.instr090.2 = subset(data.instr090, pathid>6)
rm(data.instr090)

## Normalize scales


## box plots
accesslog.boxplot = function (accesslog, at=NULL, col=NULL, xaxt=NULL){
	attach(accesslog)
	bp=boxplot(duration_ms~path, at=at, add=TRUE, col=col, xaxt=xaxt)
	means=aggregate.data.frame(duration_ms, list(path=pathid), mean)[["x"]]
        points(at, means,pch=18,cex=2)
	detach(accesslog)
}

par(mfrow=c(2,1)) # xpd=T, mar=par()$mar+c(0,0,0,4)
title="Response time comparison between different Tpmon versions"

xscale=1:18 # 1:(n.requesttypes*n.accesslogs)
## TODO: scale
pdf("results.pdf", width=12, height=6, paper="special")

plot(xscale, xscale,type="n", xlab="", ylab="Response time (ms)", ylim=c(0,17), xaxt="n", main=title) #xaxt="n"
accesslog.boxplot(data.noinstr.1, col=col.noinstr, at=seq(from=1, length.out=6, by=3), xaxt="n")
accesslog.boxplot(data.instr081.1, col=col.instr081, at=seq(from=2, length.out=6, by=3))
accesslog.boxplot(data.instr090.1, col=col.instr090, at=seq(from=3, length.out=6, by=3), xaxt="n")
mtext("Request type", side=1, line=2)
legend("top", legend=c(label.noinstr,label.instr081,label.instr090, "mean"), pch=c(22,22,22,18), pt.bg=c(col.noinstr,col.instr081,col.instr090,"black"), pt.cex=2, ncol=4, bg="white")

plot(xscale, xscale,type="n", xlab="", ylab="Response time (ms)", ylim=c(0,15), xaxt="n", main=title) #xaxt="n"
accesslog.boxplot(data.noinstr.2, col=col.noinstr, at=seq(from=1, length.out=6, by=3), xaxt="n")
accesslog.boxplot(data.instr081.2, col=col.instr081, at=seq(from=2, length.out=6, by=3))
accesslog.boxplot(data.instr090.2, col=col.instr090, at=seq(from=3, length.out=6, by=3), xaxt="n")
mtext("Request type", side=1, line=2)
legend("top", legend=c(label.noinstr,label.instr081,label.instr090, "mean"), pch=c(22,22,22,18), pt.bg=c(col.noinstr,col.instr081,col.instr090,"black"), pt.cex=2, ncol=4, bg="white")

dev.off()

