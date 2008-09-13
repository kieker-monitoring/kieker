##
n.requesttypes=3
n.accesslogs=2

## Compare reference results (fn.ref) with current results (fn.cur)
fn.ref="src/kieker/tests/compileTimeWeaving/bookstoreBenchmark/benchmark-tpmon-ref.dat"
fn.cur="tmp/benchmark-tpmon-cur.dat"

##
label.ref="Reference run (Tpmon v.0.81)"
label.cur="Current Tpmon version"

col.ref="blue"
col.cur="red"

## read data files
accesslog.read = function (fn) {
	accesslog_raw <- read.table (file=fn, header=TRUE, sep=' ', quote='"\'', dec='.',  na.strings = "NA", nrows = -1, skip =  0, row.names = NULL, fill = FALSE, strip.white = FALSE, blank.lines.skip = TRUE, comment.char="#",stringsAsFactors=FALSE)
}

data.ref=accesslog.read(fn.ref)
data.cur=accesslog.read(fn.cur)

## box plots
accesslog.boxplot = function (accesslog, col=NULL, xaxt=NULL,
add=FALSE, at=NULL){
	attach(accesslog)
        print(unique(opname))
        resp_ms=resp_ns/(1000*1000)
	bp=boxplot(resp_ms~opname, add=add, col=col, xaxt=xaxt, at=at)
        #print(means)
        points(at, mean(resp_ms),pch=18,cex=2)
	detach(accesslog)
}

xscale=1:3 # 1:(n.requesttypes*n.accesslogs)
pdf("tmp/benchmark-results.pdf", width=12, height=6, paper="special")
title="Tpmon benchmark results"
par(mfrow=c(1,3))

curop="Bookstore.searchBook()"
plot(xscale, xscale,type="n", xlab="", ylab="Response time (ms)", ylim=c(0,0.25), xaxt="n", main=curop) #xaxt="n"
accesslog.boxplot(subset(data.ref,opname==curop), col=col.ref, add=TRUE, at=1.5)
accesslog.boxplot(subset(data.cur,opname==curop), col=col.cur, add=TRUE, at=2.5)
mtext("Tpmon version", side=1, line=2)
legend("topleft", legend=c("mean"), pch=c(18), pt.bg=c("black"), pt.cex=2, ncol=1, bg="white")

curop="Catalog.getBook(boolean)"
plot(xscale, xscale,type="n", xlab="", ylab="Response time (ms)", ylim=c(0,0.01), xaxt="n", main=curop) #xaxt="n"
accesslog.boxplot(subset(data.ref,opname==curop), col=col.ref, add=TRUE, at=1.5)
accesslog.boxplot(subset(data.cur,opname==curop), col=col.cur, add=TRUE, at=2.5)
mtext("Tpmon version", side=1, line=2)
legend("topleft", legend=c("mean"), pch=c(18), pt.bg=c("black"), pt.cex=2, ncol=1, bg="white")

curop="CRM.getOffers()"
plot(xscale, xscale,type="n", xlab="", ylab="Response time (ms)", ylim=c(0,0.08), xaxt="n", main=curop) #xaxt="n"
accesslog.boxplot(subset(data.ref,opname==curop), col=col.ref, add=TRUE, at=1.5)
accesslog.boxplot(subset(data.cur,opname==curop), col=col.cur, add=TRUE, at=2.5)
mtext("Tpmon version", side=1, line=2)
legend("topleft", legend=c("mean"), pch=c(18), pt.bg=c("black"), pt.cex=2, ncol=1, bg="white")
dev.off()

