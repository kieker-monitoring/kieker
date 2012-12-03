#DO NOT REMOVE IN TRUNK

rm(list=ls(all=TRUE))
output_fn="tmp/results-linear.pdf"

configs.labels=c("Method Time (T)","Instrumentation (I)","Collecting (C)","Writing (W)")
configs.count=4
experiments.labels=c(1,2,4,8,16,32)
experiments=length(experiments.labels)

values <- array(dim=c(experiments,configs.count,5),dimnames=list(experiments.labels,configs.labels,c("mean","ci95%","25%","50%","75%")))
values[1,,] <- c( ## Recurion: 1
c(500.9148,500.7178,502.4994,506.1791),
c(000.0053,000.0072,000.0116,000.0132),
c(500.7400,500.6940,502.4630,505.9500),
c(500.7720,500.6970,502.4700,506.1120),
c(500.7780,500.7010,502.4860,506.2810))
values[2,,] <- c( ## Recurion: 2
c(500.7466,500.9628,503.9345,509.7061),
c(000.0015,000.0106,000.0169,000.0289),
c(500.7110,500.9290,503.7250,506.9960),
c(500.7160,500.9350,503.7380,507.1100),
c(500.7730,500.9520,503.7610,507.2550))
values[3,,] <- c( ## Recurion: 4
c(500.5895,501.6030,506.6734,516.7760),
c(000.0006,000.0194,000.0212,000.0318),
c(500.5740,501.3870,506.6070,512.2710),
c(500.5780,501.4010,506.6300,512.8730),
c(500.5790,501.4180,506.6590,513.6340))
values[4,,] <- c( ## Recurion: 8
c(500.7533,502.1930,512.2916,525.5919),
c(000.0012,000.0212,000.0272,000.0330),
c(500.6800,501.8580,512.0390,521.6750),
c(500.6920,501.9060,512.0770,522.4610),
c(500.8120,501.9570,512.1300,523.4330))
values[5,,] <- c( ## Recurion: 16
c(500.7701,503.7855,522.9221,547.4414),
c(000.0010,000.0312,000.0381,000.0537),
c(500.7610,503.3290,522.7040,540.4120),
c(500.7620,503.4100,522.7600,542.8270),
c(500.7620,503.4660,522.8350,548.4320))
values[6,,] <- c( ## Recurion: 32
c(500.8480,505.9303,545.1048,594.3592),
c(000.0003,000.0438,000.0533,000.0574),
c(500.8330,505.7180,544.5120,585.2360),
c(500.8520,505.7610,544.5730,590.5410),
c(500.8530,505.8290,544.6690,598.3160))
#values[7,,] <- c( ## Recurion: 64
#c(500.8252,511.4201,589.9483,664.7972),
#c(000.0005,000.0604,000.0788,000.1128),
#c(500.8060,511.0150,589.2420,651.5450),
#c(500.8080,511.1540,589.3710,659.1720),
#c(500.8120,511.2450,589.5330,670.4540))

pdf(output_fn, width=8, height=5, paper="special")
plot.new()
plot.window(xlim=c(0.5,max(experiments.labels)+0.5),ylim=c(500,600))
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
for (ce in experiments.labels) { #c(1,8,16,32,64)
  for (cc in (2:configs.count)) {
    labeltext=formatC(values[(1:length(experiments.labels))[experiments.labels==ce],cc,"50%"]-values[(1:length(experiments.labels))[experiments.labels==ce],cc-1,"50%"],format="f",digits=1)
      #rect(ce-1-(strwidth(labeltext)*0.5),values[(1:length(experiments.labels))[experiments.labels==ce],cc,"50%"]-strheight(labeltext),1+ce+(strwidth(labeltext)*0.5),values[(1:length(experiments.labels))[experiments.labels==ce],cc,"50%"],col="white",border="black")
      text(ce-0.5,values[(1:length(experiments.labels))[experiments.labels==ce],cc,"50%"],labels=labeltext,cex=0.75,col="black",pos=2,offset=0.1)
  }
}
axis(1,at=experiments.labels,labels=experiments.labels,)
axis(2)
invisible(dev.off())
