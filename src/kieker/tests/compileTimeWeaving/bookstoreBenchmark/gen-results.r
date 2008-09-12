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
accesslog.boxplot = function (accesslog, at=NULL, col=NULL, xaxt=NULL){
	attach(accesslog)
        resp_ms=resp_ns/(1000*1000)
	bp=boxplot(resp_ms~opname, at=at, add=TRUE, col=col, xaxt=xaxt)
	means=aggregate.data.frame(resp_ms, list(path=opname), mean)[["x"]]
        print(means)
        points(at, means,pch=18,cex=2)
	detach(accesslog)
}

title="Tpmon benchmark results"

xscale=1:9 # 1:(n.requesttypes*n.accesslogs)
pdf("tmp/benchmark-results.pdf", width=12, height=6, paper="special")

plot(xscale, xscale,type="n", xlab="", ylab="Response time (ms)", ylim=c(0,17), xaxt="n", main=title) #xaxt="n"
accesslog.boxplot(data.ref, col=col.ref, at=seq(from=1, length.out=3, by=3), xaxt="n")
accesslog.boxplot(data.cur, col=col.cur, at=seq(from=2, length.out=3, by=3))
mtext("Request type", side=1, line=2)
legend("topleft", legend=c(label.ref,label.cur, "mean"), pch=c(22,22,22,18), pt.bg=c(col.ref,col.cur,"black"), pt.cex=2, ncol=3, bg="white")
dev.off()

