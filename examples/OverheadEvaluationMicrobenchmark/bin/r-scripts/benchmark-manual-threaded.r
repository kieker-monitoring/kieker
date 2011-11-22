#DO NOT REMOVE IN TRUNK
output_fn="C:\\Users\\jwa\\Projects\\Kieker\\software\\kieker\\trunk\\examples\\OverheadEvaluationMicrobenchmark\\tmp\\results-threaded.pdf"

configs.labels=c("Method Time (T)","Instrumentation (I)","Collecting (C)","Writing (W)")
configs.count=4
experiments.labels=c("A1","A2","A3","A4","A5")
experiments=length(experiments.labels)
#meanvalues<-matrix(ncol=configs.count,nrow=experiments,byrow=TRUE,data=c(
#  500.7489,500.7764,501.7337,503.1575,
#  500.7836,501.0098,501.9279,503.4877,
#  500.7750,501.0741,502.1604,503.6037,
#  500.7604,501.0875,502.1860,517.7758,
#  620.6239,626.3150,626.0106,640.3472
#));

values <- array(dim=c(experiments,configs.count,5),dimnames=list(experiments.labels,configs.labels,c("mean","ci95%","25%","50%","75%")))
values["A1",,] <- c( ## Threads: 1
c(500.7489,500.7764,501.7337,503.1575),
c(000.0005,000.0008,000.0015,000.0028),
c(500.6890,500.6450,501.6350,502.8700),
c(500.7220,500.7250,501.6670,502.9410),
c(500.8340,500.8480,501.7160,503.0360))
values["A2",,] <- c( ## Threads: 2
c(501.0485,500.9249,501.9057,503.3581),
c(000.0021,000.0014,000.0014,000.0016),
c(500.7790,500.7190,501.7410,503.2210),
c(500.8180,500.8730,501.7860,503.2870),
c(500.8190,500.8860,501.8280,503.3500))
values["A3",,] <- c( ## Threads: 3
c(500.7666,500.9818,502.1668,503.6246),
c(000.0008,000.0029,000.0050,000.0992),
c(500.6320,500.8690,502.0110,503.0900),
c(500.6470,500.9200,502.0430,503.2490),
c(500.7900,500.9370,502.0750,503.3300))
values["A4",,] <- c( ## Threads: 4
c(500.7412,500.9246,502.1236,515.9702),
c(000.0006,000.0023,000.0040,000.0248),
c(500.6440,500.8190,501.9420,503.2000),
c(500.6450,500.8810,502.0220,503.2910),
c(500.6630,500.9270,502.0670,503.4690))
values["A5",,] <- c( ## Threads: 5
c(623.6752,626.3103,626.4919,637.3185),
c(001.0527,001.0621,001.0663,001.0632),
c(500.6430,500.7990,501.9280,502.2600),
c(500.6440,500.8700,501.9980,503.1700),
c(500.6450,500.9150,502.0610,503.3190))

pdf(output_fn, width=8, height=5, paper="special")
plot.new()
plot.window(xlim=c(0.5,experiments+0.5),ylim=c(0,5))
title(xlab="Experiment",ylab="Execution time (µs)")
legend.labels=c("Writing (W)","Collecting (C)","Instrumentation (I)")
legend("topleft",inset=c(0.05,0.05),legend=legend.labels,fill=TRUE,angle=c(45,135,45),density=c(30,20,10),title="Overhead (mean) of ...",ncol=1)
for (ce in 1:experiments) {
  #meanvalues
  rect(ce-0.3,values[experiments.labels[ce],3,"mean"]-values[experiments.labels[ce],1,"mean"],ce+0.5,values[experiments.labels[ce],4,"mean"]-values[experiments.labels[ce],1,"mean"],col="white",border="black")
  rect(ce-0.3,values[experiments.labels[ce],2,"mean"]-values[experiments.labels[ce],1,"mean"],ce+0.5,values[experiments.labels[ce],3,"mean"]-values[experiments.labels[ce],1,"mean"],col="white",border="black")
  rect(ce-0.3,0,ce+0.5,values[experiments.labels[ce],2,"mean"]-values[experiments.labels[ce],1,"mean"],col="white",border="black")
  for (cc in (2:configs.count)) {
    lines(c(ce+0.41,ce+0.49),c(values[experiments.labels[ce],cc,"mean"]-values[experiments.labels[ce],1,"mean"]+values[experiments.labels[ce],cc,"ci95%"],values[experiments.labels[ce],cc,"mean"]-values[experiments.labels[ce],1,"mean"]+values[experiments.labels[ce],cc,"ci95%"]),col="red")
    lines(c(ce+0.45,ce+0.45),c(values[experiments.labels[ce],cc,"mean"]-values[experiments.labels[ce],1,"mean"]+values[experiments.labels[ce],cc,"ci95%"],values[experiments.labels[ce],cc,"mean"]-values[experiments.labels[ce],1,"mean"]-values[experiments.labels[ce],cc,"ci95%"]),col="red")
    lines(c(ce+0.41,ce+0.49),c(values[experiments.labels[ce],cc,"mean"]-values[experiments.labels[ce],1,"mean"]-values[experiments.labels[ce],cc,"ci95%"],values[experiments.labels[ce],cc,"mean"]-values[experiments.labels[ce],1,"mean"]-values[experiments.labels[ce],cc,"ci95%"]),col="red")
  }
  #meanvalues
  rect(ce-0.4,values[experiments.labels[ce],3,"50%"]-values[experiments.labels[ce],1,"50%"],ce+0.4,values[experiments.labels[ce],4,"50%"]-values[experiments.labels[ce],1,"50%"],col="white",border="black")
  rect(ce-0.4,values[experiments.labels[ce],3,"50%"]-values[experiments.labels[ce],1,"50%"],ce+0.4,values[experiments.labels[ce],4,"50%"]-values[experiments.labels[ce],1,"50%"],angle=45,density=30)
  rect(ce-0.4,values[experiments.labels[ce],2,"50%"]-values[experiments.labels[ce],1,"50%"],ce+0.4,values[experiments.labels[ce],3,"50%"]-values[experiments.labels[ce],1,"50%"],col="white",border="black")
  rect(ce-0.4,values[experiments.labels[ce],2,"50%"]-values[experiments.labels[ce],1,"50%"],ce+0.4,values[experiments.labels[ce],3,"50%"]-values[experiments.labels[ce],1,"50%"],angle=135,density=20)
  rect(ce-0.4,0,ce+0.4,values[experiments.labels[ce],2,"50%"]-values[experiments.labels[ce],1,"50%"],col="white",border="black")
  rect(ce-0.4,0,ce+0.4,values[experiments.labels[ce],2,"50%"]-values[experiments.labels[ce],1,"50%"],angle=45,density=10)
  for (cc in (2:configs.count)) {
    lines(c(ce-0.39,ce-0.31),c(values[experiments.labels[ce],cc,"75%"]-values[experiments.labels[ce],1,"50%"],values[experiments.labels[ce],cc,"75%"]-values[experiments.labels[ce],1,"50%"]),col="red")
    lines(c(ce-0.35,ce-0.35),c(values[experiments.labels[ce],cc,"25%"]-values[experiments.labels[ce],1,"50%"],values[experiments.labels[ce],cc,"75%"]-values[experiments.labels[ce],1,"50%"]),col="red")
    lines(c(ce-0.39,ce-0.31),c(values[experiments.labels[ce],cc,"25%"]-values[experiments.labels[ce],1,"50%"],values[experiments.labels[ce],cc,"25%"]-values[experiments.labels[ce],1,"50%"]),col="red")
  }
}
axis(1, at=c(1:experiments), labels=experiments.labels,)
axis(2)
invisible(dev.off())
