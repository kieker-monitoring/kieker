#DO NOT REMOVE IN TRUNK

rm(list=ls(all=TRUE))
output_fn="tmp/results-several.pdf"

configs.labels=c("Method Time (T)","Instrumentation (I)","Collecting (C)","Writing (W)")
configs.count=4
experiments.labels=c("OER-CSV","OER-Binary","Events-CSV","Events-Binary")
experiments=length(experiments.labels)

values <- array(dim=c(experiments,configs.count,5),dimnames=list(experiments.labels,configs.labels,c("mean","ci95%","25%","50%","75%")))
#values["Events-CSV",,] <- c(
#c(500.7680,500.9227,502.4763,506.1838),
#c(000.0002,000.0068,000.0109,000.0409),
#c(500.7680,500.9120,502.3740,505.9730),
#c(500.7690,500.9140,502.3800,506.0850),
#c(500.7690,500.9170,502.4010,506.1970))
values["Events-CSV",,] <- c(
c(500.7561,500.9033,502.4309,505.9522),
c(000.0003,000.0075,000.0137,000.0310),
c(500.7720,500.8850,502.3910,505.3180),
c(500.7730,500.8870,502.3980,505.4270),
c(500.7780,500.9040,502.4160,505.5410))
values["Events-Binary",,] <- c(
c(500.7817,500.9832,502.5732,505.8009),
c(000.0002,000.0072,000.0128,000.0133),
c(500.7750,500.9660,502.5520,505.6150),
c(500.7750,500.9690,502.5570,505.7310),
c(500.7800,500.9720,502.5710,505.8600))
values["OER-Binary",,] <- c(
c(500.7873,500.9471,501.9237,503.3244),
c(000.0023,000.0044,000.0182,000.0131),
c(500.7390,500.9220,501.8870,503.1810),
c(500.7450,500.9240,501.9020,503.2270),
c(500.7690,500.9300,501.9290,503.3200))
values["OER-CSV",,] <- c(
c(500.5759,500.7973,501.7964,505.0034),
c(000.0003,000.0060,000.0160,000.0210),
c(500.5540,500.7710,501.7500,504.7860),
c(500.5550,500.7750,501.7570,504.8770),
c(500.5560,500.7930,501.7800,504.9550))

pdf(output_fn, width=8, height=5, paper="special")
plot.new()
plot.window(xlim=c(0.5,experiments+0.5),ylim=c(500,510))
title(xlab="Experiment",ylab="Execution time (µs)")
legend.labels=c("Writing (W)","Instrumentation (I)","(mean values)","Collecting (C)","Method Time (T)")
legend("topleft",inset=c(0.05,0.05),legend=legend.labels,fill=TRUE,angle=c(45,45,0,135,135),density=c(30,10,0,20,5),title="Overhead (median with quartiles) of ...",ncol=2,cex=0.8)
for (ce in c(1:experiments)) {
  #meanvalues
  rect(ce-0.2,values[experiments.labels[ce],3,"mean"],ce+0.4,values[experiments.labels[ce],4,"mean"])
  rect(ce-0.2,values[experiments.labels[ce],2,"mean"],ce+0.4,values[experiments.labels[ce],3,"mean"])
  rect(ce-0.2,values[experiments.labels[ce],1,"mean"],ce+0.4,values[experiments.labels[ce],2,"mean"])
  rect(ce-0.2,0,ce+0.4,values[experiments.labels[ce],1,"mean"])
  for (cc in (1:configs.count)) {
    lines(c(ce+0.41,ce+0.49),c(values[experiments.labels[ce],cc,"mean"]+values[experiments.labels[ce],cc,"ci95%"],values[experiments.labels[ce],cc,"mean"]+values[experiments.labels[ce],cc,"ci95%"]),col="black")
    lines(c(ce+0.45,ce+0.45),c(values[experiments.labels[ce],cc,"mean"]+values[experiments.labels[ce],cc,"ci95%"],values[experiments.labels[ce],cc,"mean"]-values[experiments.labels[ce],cc,"ci95%"]),col="black")
    lines(c(ce+0.41,ce+0.49),c(values[experiments.labels[ce],cc,"mean"]-values[experiments.labels[ce],cc,"ci95%"],values[experiments.labels[ce],cc,"mean"]-values[experiments.labels[ce],cc,"ci95%"]),col="black")
  }
  #median
  rect(ce-0.3,values[experiments.labels[ce],3,"50%"],ce+0.3,values[experiments.labels[ce],4,"50%"],col="white",border="black")
  rect(ce-0.3,values[experiments.labels[ce],3,"50%"],ce+0.3,values[experiments.labels[ce],4,"50%"],angle=45,density=30)
  rect(ce-0.3,values[experiments.labels[ce],2,"50%"],ce+0.3,values[experiments.labels[ce],3,"50%"],col="white",border="black")
  rect(ce-0.3,values[experiments.labels[ce],2,"50%"],ce+0.3,values[experiments.labels[ce],3,"50%"],angle=135,density=20)
  rect(ce-0.3,values[experiments.labels[ce],1,"50%"],ce+0.3,values[experiments.labels[ce],2,"50%"],col="white",border="black")
  rect(ce-0.3,values[experiments.labels[ce],1,"50%"],ce+0.3,values[experiments.labels[ce],2,"50%"],angle=45,density=10)
  rect(ce-0.3,0,ce+0.3,values[experiments.labels[ce],1,"50%"],col="white",border="black")
  rect(ce-0.3,0,ce+0.3,values[experiments.labels[ce],1,"50%"],angle=135,density=5)
  for (cc in (1:configs.count)) {
    lines(c(ce-0.39,ce-0.31),c(values[experiments.labels[ce],cc,"75%"],values[experiments.labels[ce],cc,"75%"]),col="black")
    lines(c(ce-0.35,ce-0.35),c(values[experiments.labels[ce],cc,"25%"],values[experiments.labels[ce],cc,"75%"]),col="black")
    lines(c(ce-0.39,ce-0.31),c(values[experiments.labels[ce],cc,"25%"],values[experiments.labels[ce],cc,"25%"]),col="black")
  }
  for (cc in (2:configs.count)) {
    labeltext=formatC(values[experiments.labels[ce],cc,"50%"]-values[experiments.labels[ce],cc-1,"50%"],format="f",digits=2)
      rect(ce-(strwidth(labeltext)*0.5),values[experiments.labels[ce],cc,"50%"]-strheight(labeltext),ce+(strwidth(labeltext)*0.5),values[experiments.labels[ce],cc,"50%"],col="white",border="black")
      text(ce,values[experiments.labels[ce],cc,"50%"],labels=labeltext,cex=0.75,col="black",pos=1,offset=0.1)
  }
}
axis(1, at=c(1:experiments), labels=experiments.labels,)
axis(2)
invisible(dev.off())
