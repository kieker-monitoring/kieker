#DO NOT REMOVE IN TRUNK
output_fn="C:\\Users\\jwa\\Projects\\Monitoring\\OverheadEvaluation\\results-several.pdf"

configs.labels=c("Method Time (T)","Instrumentation (I)","Collecting (C)","Writing (W)")
configs.count=4
experiments.labels=c("Intel-S","Intel-A","AMD-S","AMD-A","T2-S","T2-A","T2P-S","T2P-A")
experiments=length(experiments.labels)

values <- array(dim=c(experiments,configs.count,5),dimnames=list(experiments.labels,configs.labels,c("mean","ci95%","25%","50%","75%")))
values["Intel-S",,] <- c(
c(500.7626,500.8171,501.8406,509.6133),
c(000.0015,000.0014,000.0019,000.0230),
c(500.7000,500.7800,501.7740,508.9720),
c(500.7010,500.7890,501.7850,509.1350),
c(500.7060,500.7960,501.8080,509.3210))
values["Intel-A",,] <- c(
c(500.7626,500.8171,501.8406,504.6068),
c(000.0015,000.0014,000.0019,000.0028),
c(500.7000,500.7800,501.7740,504.3580),
c(500.7010,500.7890,501.7850,504.4600),
c(500.7060,500.7960,501.8080,504.5880))

values["AMD-S",,] <- c(
c(500.5010,500.6115,501.4409,509.7723),
c(000.0009,000.0013,000.0051,000.0189),
c(500.4590,500.5420,501.2530,509.1290),
c(500.4930,500.5590,501.3050,509.2590),
c(500.4940,500.5920,501.3600,509.4120))
values["AMD-A",,] <- c(
c(500.5010,500.6115,501.4409,507.4486),
c(000.0009,000.0013,000.0051,000.0228),
c(500.4590,500.5420,501.2530,505.1780),
c(500.4930,500.5590,501.3050,505.8570),
c(500.4940,500.5920,501.3600,509.2770))

values["T2-S",,] <- c(
c(501.1622,502.6744,505.2425,585.3089),
c(000.0008,000.0011,000.0067,000.0318),
c(501.0900,502.6270,505.1590,583.3990),
c(501.1800,502.6270,505.1600,583.9410),
c(500.4940,500.5920,501.3600,584.6650))
values["T2-A",,] <- c(
c(501.1622,502.6744,505.2425,514.7573),
c(000.0008,000.0011,000.0067,000.0363),
c(501.0900,502.6270,505.1590,514.3850),
c(501.1800,502.6270,505.1600,514.5660),
c(500.4940,500.5920,501.3600,509.4120))

values["T2P-S",,] <- c(
c(501.1737,502.6732,505.3519,585.1670),
c(000.0007,000.0010,000.0196,000.0387),
c(501.1070,502.6440,505.1770,583.3270),
c(501.1970,502.6440,505.2670,583.7800),
c(501.1970,502.6450,505.2680,584.3230))
values["T2P-A",,] <- c(
c(501.1737,502.6732,505.3519,519.1713),
c(000.0007,000.0010,000.0196,000.0521),
c(501.1070,502.6440,505.1770,518.7450),
c(501.1970,502.6440,505.2670,518.9260),
c(501.1970,502.6450,505.2680,519.1070))


pdf(output_fn, width=8, height=5, paper="special")
plot.new()
plot.window(xlim=c(0.5,experiments+0.5),ylim=c(500,520))
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
    labeltext=formatC(values[experiments.labels[ce],cc,"50%"]-values[experiments.labels[ce],cc-1,"50%"],format="f",digits=1)
      rect(ce-(strwidth(labeltext)*0.5),values[experiments.labels[ce],cc,"50%"]-strheight(labeltext),ce+(strwidth(labeltext)*0.5),values[experiments.labels[ce],cc,"50%"],col="white",border="black")
      text(ce,values[experiments.labels[ce],cc,"50%"],labels=labeltext,cex=0.75,col="black",pos=1,offset=0.1)
  }
}
axis(1, at=c(1:experiments), labels=experiments.labels,)
axis(2)
invisible(dev.off())
