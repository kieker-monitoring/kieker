##
n.requesttypes=12
n.accesslogs=3

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

data.noinstr=accesslog.read("acceslog-noInstr.csv")
#data.instr081=accesslog.read("acceslog-tpmon081.csv")
#data.instr090=accesslog.read("acceslog-tpmon090.csv")

## TODO: make sure, that request types have the same order
## maybe the easiest is to do this with a bash script using sed
## hopefully R is smart and sorts alphabetically

## TODO: divide into two plots
data.noinstr.1 = ?select

## Normalize scales

## box plots
accesslog.boxplot = function (accesslog, at=NULL, col=NULL, xaxt=NULL){
	attach(accesslog)
	bp=boxplot(duration_ms~path, at=at, add=TRUE, col=col, xaxt=xaxt)
	detach(accesslog)
}

xscale=1:18 # 1:(n.requesttypes*n.accesslogs)
plot(xscale, xscale,type="n", xlab="", ylab="Response time (ms)", ylim=c(0,50), xaxt="n") #xaxt="n"
accesslog.boxplot(data.noinstr, col="gray", at=seq(from=1, length.out=12, by=3), xaxt="n")
accesslog.boxplot(data.noinstr, col="blue", at=seq(from=2, length.out=12, by=3))
accesslog.boxplot(data.noinstr, col="red", at=seq(from=3, length.out=12, by=3), xaxt="n")
# 