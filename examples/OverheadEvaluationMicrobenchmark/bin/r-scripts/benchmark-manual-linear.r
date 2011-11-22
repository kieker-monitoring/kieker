#DO NOT REMOVE IN TRUNK
output_fn="C:\\Users\\jwa\\Projects\\Kieker\\software\\kieker\\trunk\\examples\\OverheadEvaluationMicrobenchmark\\tmp\\results-linear.pdf"

configs.labels=c("Method Time (T)","Instrumentation (I)","Collecting (C)","Writing (W)")
configs.count=4
experiments.labels=c(1,2,4,8,16,32,64)
experiments=length(experiments.labels)

values <- array(dim=c(experiments,configs.count,5),dimnames=list(experiments.labels,configs.labels,c("mean","ci95%","25%","50%","75%")))
values[1,,] <- c( ## Recurion: 1
#c(500.7892,500.7650,501.7699,504.7132),
#c(000.0006,000.0004,000.0026,000.0054),
#c(500.6910,500.6590,501.6520,504.4650),
#c(500.8190,500.6980,501.7040,504.5900),
#c(500.8270,500.8620,501.8630,504.7370))
c(500.7626,500.8171,501.8406,504.6068),
c(000.0015,000.0014,000.0019,000.0028),
c(500.7000,500.7800,501.7740,504.3580),
c(500.7010,500.7890,501.7850,504.4600),
c(500.7060,500.7960,501.8080,504.5880))
values[2,,] <- c( ## Recurion: 2
c(500.7905,500.9130,502.9062,505.9226),
c(000.0002,000.0034,000.0034,000.0078),
c(500.6920,500.8050,502.7620,505.5120),
c(500.8230,500.8940,502.8920,505.6660),
c(500.8420,500.9360,502.9650,505.8840))
values[3,,] <- c( ## Recurion: 4
c(500.9100,501.0987,504.8962,507.9385),
c(000.0041,000.0033,000.0053,000.0101),
c(500.6980,501.0070,504.7810,507.5510),
c(500.8240,501.0730,504.8270,507.6820),
c(500.8300,501.1440,504.9320,507.8430))
values[4,,] <- c( ## Recurion: 8
c(500.7406,501.4662,509.1025,512.6045),
c(000.0004,000.0042,000.0071,000.0274),
c(500.7610,501.3570,508.9410,512.0820),
c(500.7670,501.4360,509.0520,512.2340),
c(500.7750,501.5060,509.1060,512.4040))
values[5,,] <- c( ## Recurion: 16
c(500.8535,502.4446,517.4832,522.2983),
c(000.0009,000.0058,000.0099,000.0176),
c(500.8540,502.3180,517.2740,521.1140),
c(500.8550,502.3920,517.3470,521.4600),
c(500.8600,502.4700,517.4360,521.9460))
values[6,,] <- c( ## Recurion: 32
c(500.7918,504.1720,534.2520,542.3501),
c(000.0003,000.0087,000.0131,000.1992),
c(500.6460,504.0190,533.9890,538.9990),
c(500.8320,504.0990,534.0830,539.7550),
c(500.8370,504.1680,534.1930,545.2410))
values[7,,] <- c( ## Recurion: 64
c(500.9180,507.8319,567.8758,578.3981),
c(000.0011,000.0137,000.0231,000.4133),
c(500.7760,507.6500,567.4040,573.7970),
c(500.8940,507.7230,567.5710,574.7270),
c(500.9900,507.7920,567.7390,576.8900))

pdf(output_fn, width=8, height=5, paper="special")
plot.new()
plot.window(xlim=c(0.5,max(experiments.labels)+0.5),ylim=c(500,580))
title(xlab="Experiment",ylab="Execution time (µs)")
legend.labels=c("Writing (W)","Instrumentation (I)","Collecting (C)","Method Time (T)")
legend("topleft",inset=c(0.05,0.05),legend=legend.labels,fill=TRUE,angle=c(45,45,135,135),density=c(30,10,20,5),title="Overhead (median with quartiles) of ...",ncol=2)
for (ce in experiments.labels) {
  #median
  rect(ce-0.4,values[(1:length(experiments.labels))[experiments.labels==ce],3,"50%"],ce+0.4,values[(1:length(experiments.labels))[experiments.labels==ce],4,"50%"],col="white",border="black")
  rect(ce-0.4,values[(1:length(experiments.labels))[experiments.labels==ce],3,"50%"],ce+0.4,values[(1:length(experiments.labels))[experiments.labels==ce],4,"50%"],angle=45,density=30)
  rect(ce-0.4,values[(1:length(experiments.labels))[experiments.labels==ce],2,"50%"],ce+0.4,values[(1:length(experiments.labels))[experiments.labels==ce],3,"50%"],col="white",border="black")
  rect(ce-0.4,values[(1:length(experiments.labels))[experiments.labels==ce],2,"50%"],ce+0.4,values[(1:length(experiments.labels))[experiments.labels==ce],3,"50%"],angle=135,density=20)
  rect(ce-0.4,values[(1:length(experiments.labels))[experiments.labels==ce],1,"50%"],ce+0.4,values[(1:length(experiments.labels))[experiments.labels==ce],2,"50%"],col="white",border="black")
  rect(ce-0.4,values[(1:length(experiments.labels))[experiments.labels==ce],1,"50%"],ce+0.4,values[(1:length(experiments.labels))[experiments.labels==ce],2,"50%"],angle=45,density=10)
  rect(ce-0.4,0,ce+0.4,values[(1:length(experiments.labels))[experiments.labels==ce],1,"50%"],col="white",border="black")
  rect(ce-0.4,0,ce+0.4,values[(1:length(experiments.labels))[experiments.labels==ce],1,"50%"],angle=135,density=5)
  for (cc in (1:configs.count)) {
    lines(c(ce+1-0.3,ce+1+0.3),c(values[(1:length(experiments.labels))[experiments.labels==ce],cc,"75%"],values[(1:length(experiments.labels))[experiments.labels==ce],cc,"75%"]),col="black")
    lines(c(ce+1,ce+1),c(values[(1:length(experiments.labels))[experiments.labels==ce],cc,"25%"],values[(1:length(experiments.labels))[experiments.labels==ce],cc,"75%"]),col="black")
    lines(c(ce+1-0.3,ce+1+0.3),c(values[(1:length(experiments.labels))[experiments.labels==ce],cc,"25%"],values[(1:length(experiments.labels))[experiments.labels==ce],cc,"25%"]),col="black")
  }
}
for (ce in c(1,8,16,32,64)) {
  for (cc in (2:configs.count)) {
    labeltext=formatC(values[(1:length(experiments.labels))[experiments.labels==ce],cc,"50%"]-values[(1:length(experiments.labels))[experiments.labels==ce],cc-1,"50%"],format="f",digits=1)
      #rect(ce-1-(strwidth(labeltext)*0.5),values[(1:length(experiments.labels))[experiments.labels==ce],cc,"50%"]-strheight(labeltext),1+ce+(strwidth(labeltext)*0.5),values[(1:length(experiments.labels))[experiments.labels==ce],cc,"50%"],col="white",border="black")
      text(ce-0.5,values[(1:length(experiments.labels))[experiments.labels==ce],cc,"50%"],labels=labeltext,cex=0.75,col="black",pos=2,offset=0.1)
  }
}
axis(1,at=experiments.labels,labels=experiments.labels,)
axis(2)
invisible(dev.off())
