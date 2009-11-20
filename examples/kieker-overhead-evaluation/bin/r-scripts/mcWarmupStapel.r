results.df=read.table(results_fn,header=TRUE, sep=";", stringsAsFactors=FALSE)
results.df["rt_msec"]=results.df["duration_nsec"]/(1000)
attach(results.df)

m1=mean(subset(results.df, order_index == 0)["rt_msec"])
m2=mean(subset(results.df, order_index == 1)["rt_msec"])-m1
m3=mean(subset(results.df, order_index == 2)["rt_msec"])-m1-m2
m4=mean(subset(results.df, order_index == 3)["rt_msec"])-m1-m2-m3

detach(results.df)
pdf(output_fn, width=2, height=5, paper="special")
  barplot(
    matrix(c(m2,m3,m4),ncol=1),
    ylab="Mean execution time (microseconds)",
    ylim=c(0,2.5)
  )
  text((m2),as.character(round(m2,digits=3)),pos=2)
  text((m2+m3/2),as.character(round(m3,digits=3)),pos=2)
  text((m2+m3+m4/2),as.character(round(m4,digits=3)),pos=2)
  mtext(expression(Delta[B]),side=4,at=(m2/2))
  mtext(expression(Delta[C]),side=4,at=(m2+m3/2))
  mtext(expression(Delta[D]),side=4,at=(m2+m3+m4/2))
dev.off()
