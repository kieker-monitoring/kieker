#DO NOT REMOVE IN TRUNK
output_fn="C:\\Users\\jwa\\Projects\\Kieker\\software\\kieker\\trunk\\examples\\OverheadEvaluationMicrobenchmark\\tmp\\results-singelthreaded.pdf"

configs.labels=c("Method Time (T)","Instrumentation (I)","Collecting (C)","Writing (W)")
configs.count=4
experiments.labels=c("S1","S2","A1","A2","A3","A4","A5")
experiments=length(experiments.labels)

values <- array(dim=c(experiments,configs.count,5),dimnames=list(experiments.labels,configs.labels,c("mean","ci95%","25%","50%","75%")))
values["S1",,] <- c( ## SingleThread, 1-Core, SyncFS
c(500.7039,500.8062,501.8528,509.5831),
c(000.0001,000.0001,000.0133,000.0268),
c(500.7000,500.7900,501.7810,508.9520),
c(500.7010,500.7940,501.7900,509.1000),
c(500.7060,500.8040,501.8040,509.2740))
values["S2",,] <- c( ## SingleThread, 2-Core (same core), SyncFS
c(500.7046,500.7976,501.8133,509.6133),
c(000.0001,000.0001,000.0007,000.0230),
c(500.7000,500.7890,501.7790,508.9720),
c(500.7010,500.7930,501.7880,509.1350),
c(500.7060,500.8010,501.8030,509.3210))
values["A1",,] <- c( ## SingleThread, 1-Core, AsyncFS
c(500.7039,500.8062,501.8528,516.8180),
c(000.0001,000.0001,000.0133,000.0239),
c(500.7000,500.7900,501.7810,516.0790),
c(500.7010,500.7940,501.7900,516.2690),
c(500.7060,500.8040,501.8040,516.4950))
values["A2",,] <- c( ## SingleThread, 2-Core (same core), AsyncFS
c(500.7046,500.7976,501.8133,504.5507),
c(000.0001,000.0001,000.0007,000.0045),
c(500.7000,500.7890,501.7790,504.4010),
c(500.7010,500.7930,501.7880,504.4970),
c(500.7060,500.8010,501.8030,504.6090))
values["A3",,] <- c( ## SingleThread, 2-Core (same chip), AsyncFS
c(500.9020,500.9395,502.0425,503.6665),
c(000.0028,000.0026,000.0030,000.0048),
c(500.7000,500.7800,501.7790,502.9510),
c(500.7010,500.7870,501.7920,502.9880),
c(500.7060,500.7930,501.8090,503.0410))
values["A4",,] <- c( ## SingleThread, 2-Core (2 chips), AsyncFS
c(500.7165,500.7509,501.8327,504.4420),
c(000.0005,000.0002,000.0008,000.0027),
c(500.7000,500.6440,501.7810,504.3080),
c(500.7010,500.7860,501.7960,504.3980),
c(500.7060,500.7910,501.8220,504.5140))
values["A5",,] <- c( ## SingleThread, 16-Core (2 chips), AsyncFS
c(500.7626,500.8171,501.8406,504.6068),
c(000.0015,000.0014,000.0019,000.0028),
c(500.7000,500.7800,501.7740,504.3580),
c(500.7010,500.7890,501.7850,504.4600),
c(500.7060,500.7960,501.8080,504.5880))

pdf(output_fn, width=8, height=5, paper="special")
plot.new()
plot.window(xlim=c(0.5,experiments+0.5),ylim=c(500,517))
title(xlab="Experiment",ylab="Execution time (µs)")
legend.labels=c("Writing (W)","Instrumentation (I)  ","(mean values)","Collecting (C)","Method Time (T)")
legend("topright",inset=c(0.0,0.05),legend=legend.labels,fill=TRUE,angle=c(45,45,0,135,135),density=c(30,10,0,20,5),title="Overhead (median with quartiles) of ...",ncol=2)
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
