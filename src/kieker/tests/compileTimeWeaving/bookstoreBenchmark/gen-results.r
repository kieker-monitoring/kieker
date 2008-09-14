##
# TODO: Plot in loop with all opnames
#       Plot with maximum Y axis (to see all outliers)

##
n.requesttypes=3
n.accesslogs=3

## Compare reference results (fn.ref) with current results (fn.cur)
fn.081="src/kieker/tests/compileTimeWeaving/bookstoreBenchmark/benchmark-tpmon-081.dat"
fn.090="src/kieker/tests/compileTimeWeaving/bookstoreBenchmark/benchmark-tpmon-090.dat"
fn.cur="tmp/benchmark-tpmon-cur.dat"

##
label.081="Tpmon v.0.81"
label.090="Tpmon v.0.90"
label.cur="Tpmon trunk version"

col.081="blue"
col.090="red"
col.cur="green"

## read data files
accesslog.read = function (fn) {
	accesslog_raw <- read.table (file=fn, header=TRUE, sep=' ', quote='"\'', dec='.',  na.strings = "NA", nrows = -1, skip =  0, row.names = NULL, fill = FALSE, strip.white = FALSE, blank.lines.skip = TRUE, comment.char="#",stringsAsFactors=FALSE)
}

data.081=accesslog.read(fn.081);data.081[["resp_ms"]]=data.081[["resp_ns"]]/(1000*1000)
data.090=accesslog.read(fn.090);data.090[["resp_ms"]]=data.090[["resp_ns"]]/(1000*1000)
data.cur=accesslog.read(fn.cur);data.cur[["resp_ms"]]=data.cur[["resp_ns"]]/(1000*1000)

## box plots
accesslog.boxplot = function (accesslog, col=NULL, xaxt=NULL,
add=FALSE, at=NULL){
	attach(accesslog)
        print(unique(opname))
	bp=boxplot(resp_ms~opname, add=add, col=col, xaxt=xaxt, at=at)
        #print(means)
        points(at, mean(resp_ms),pch=18,cex=2)
	detach(accesslog)
}

xscale=1:4 # 1:(n.requesttypes*n.accesslogs)
pdf("tmp/benchmark-results.pdf", width=12, height=6, paper="special")
title="Tpmon benchmark results"
par(mfrow=c(1,3))

curop="Bookstore.searchBook()"
plot(xscale, xscale,type="n", xlab="", ylab="Response time (ms)", ylim=c(0,0.25), xaxt="n", main=curop) #xaxt="n"
accesslog.boxplot(subset(data.081,opname==curop), col=col.081, add=TRUE, at=1.5)
accesslog.boxplot(subset(data.090,opname==curop), col=col.090, add=TRUE, at=2.5)
accesslog.boxplot(subset(data.cur,opname==curop), col=col.cur, add=TRUE, at=3.5)
axis(side=1, at=c(1.5,2.5,3.5), labels=c("0.81", "0.90", "trunk"))
mtext("Tpmon version", side=1, line=2)
legend("bottomleft", legend=c("mean"), pch=c(18), pt.bg=c("black"), pt.cex=2, ncol=1, bg="white")

curop="Catalog.getBook(boolean)"
plot(xscale, xscale,type="n", xlab="", ylab="Response time (ms)", ylim=c(0,0.01), xaxt="n", main=curop) #xaxt="n"
accesslog.boxplot(subset(data.081,opname==curop), col=col.081, add=TRUE, at=1.5)
accesslog.boxplot(subset(data.090,opname==curop), col=col.090, add=TRUE, at=2.5)
accesslog.boxplot(subset(data.cur,opname==curop), col=col.cur, add=TRUE, at=3.5)
axis(side=1, at=c(1.5,2.5,3.5), labels=c("0.81", "0.90", "trunk"))
mtext("Tpmon version", side=1, line=2)
legend("bottomleft", legend=c("mean"), pch=c(18), pt.bg=c("black"), pt.cex=2, ncol=1, bg="white")

curop="CRM.getOffers()"
plot(xscale, xscale,type="n", xlab="", ylab="Response time (ms)", ylim=c(0,0.08), xaxt="n", main=curop) #xaxt="n"
accesslog.boxplot(subset(data.081,opname==curop), col=col.081, add=TRUE, at=1.5)
accesslog.boxplot(subset(data.090,opname==curop), col=col.090, add=TRUE, at=2.5)
accesslog.boxplot(subset(data.cur,opname==curop), col=col.cur, add=TRUE, at=3.5)
axis(side=1, at=c(1.5,2.5,3.5), labels=c("0.81", "0.90", "trunk"))
mtext("Tpmon version", side=1, line=2)
legend("bottomleft", legend=c("mean"), pch=c(18), pt.bg=c("black"), pt.cex=2, ncol=1, bg="white")

## Non-trimmed data including min and max values (y-axis in log scale)

curop="Bookstore.searchBook()"
ylim=c(
 min(c(subset(data.081,opname==curop)$resp_ms,subset(data.090,opname==curop)$resp_ms,subset(data.cur,opname==curop)$resp_ms)),
 max(c(subset(data.081,opname==curop)$resp_ms,subset(data.090,opname==curop)$resp_ms,subset(data.cur,opname==curop)$resp_ms))
)
plot(xscale, seq(from=ylim[1],to=ylim[2],length.out=length(xscale)),type="n", xlab="", ylab="Response time (ms) [log scale]", xaxt="n", main=curop, log="y") #xaxt="n"
accesslog.boxplot(subset(data.081,opname==curop), col=col.081, add=TRUE, at=1.5)
accesslog.boxplot(subset(data.090,opname==curop), col=col.090, add=TRUE, at=2.5)
accesslog.boxplot(subset(data.cur,opname==curop), col=col.cur, add=TRUE, at=3.5)
axis(side=1, at=c(1.5,2.5,3.5), labels=c("0.81", "0.90", "trunk"))
mtext("Tpmon version", side=1, line=2)
legend("bottomleft", legend=c("mean"), pch=c(18), pt.bg=c("black"), pt.cex=2, ncol=1, bg="white")

curop="Bookstore.searchBook()"
ylim=c(
 min(c(subset(data.081,opname==curop)$resp_ms,subset(data.090,opname==curop)$resp_ms,subset(data.cur,opname==curop)$resp_ms)),
 max(c(subset(data.081,opname==curop)$resp_ms,subset(data.090,opname==curop)$resp_ms,subset(data.cur,opname==curop)$resp_ms))
)
plot(xscale, seq(from=ylim[1],to=ylim[2],length.out=length(xscale)),type="n", xlab="", ylab="Response time (ms) [log scale]", xaxt="n", main=curop, log="y") #xaxt="n"
accesslog.boxplot(subset(data.081,opname==curop), col=col.081, add=TRUE, at=1.5)
accesslog.boxplot(subset(data.090,opname==curop), col=col.090, add=TRUE, at=2.5)
accesslog.boxplot(subset(data.cur,opname==curop), col=col.cur, add=TRUE, at=3.5)
axis(side=1, at=c(1.5,2.5,3.5), labels=c("0.81", "0.90", "trunk"))
mtext("Tpmon version", side=1, line=2)
legend("bottomleft", legend=c("mean"), pch=c(18), pt.bg=c("black"), pt.cex=2, ncol=1, bg="white")

curop="CRM.getOffers()"
ylim=c(
 min(c(subset(data.081,opname==curop)$resp_ms,subset(data.090,opname==curop)$resp_ms,subset(data.cur,opname==curop)$resp_ms)),
 max(c(subset(data.081,opname==curop)$resp_ms,subset(data.090,opname==curop)$resp_ms,subset(data.cur,opname==curop)$resp_ms))
)
plot(xscale, seq(from=ylim[1],to=ylim[2],length.out=length(xscale)),type="n", xlab="", ylab="Response time (ms) [log scale]", xaxt="n", main=curop, log="y") #xaxt="n"
accesslog.boxplot(subset(data.081,opname==curop), col=col.081, add=TRUE, at=1.5)
accesslog.boxplot(subset(data.090,opname==curop), col=col.090, add=TRUE, at=2.5)
accesslog.boxplot(subset(data.cur,opname==curop), col=col.cur, add=TRUE, at=3.5)
axis(side=1, at=c(1.5,2.5,3.5), labels=c("0.81", "0.90", "trunk"))
mtext("Tpmon version", side=1, line=2)
legend("bottomleft", legend=c("mean"), pch=c(18), pt.bg=c("black"), pt.cex=2, ncol=1, bg="white")

dev.off()
