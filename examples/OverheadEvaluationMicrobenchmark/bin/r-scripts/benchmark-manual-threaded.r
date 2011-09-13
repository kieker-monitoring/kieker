output_fn="C:\\Users\\jwa\\Projects\\Kieker\\software\\kieker\\trunk\\examples\\OverheadEvaluationMicrobenchmark\\tmp\\results-threaded.pdf"

configs.labels=c("Instrumentation (I)","Collecting (C)","Writing (W)")
configs.count=4
experiments.labels=c("A1","A2","A3","A4","A5","A6","A7","A8","A9","A10","A11","A12","A13","A14","A15","A16")
experiments=length(experiments.labels)
meanvalues <- matrix(ncol=configs.count,nrow=experiments,byrow=TRUE,data=c(
  500.7403,500.8022,501.8709,503.0770,
  500.8104,501.0284,502.1964,503.4017,
  500.8119,501.0875,502.2291,503.5294,
  500.7691,501.0101,502.2891,505.2849,
  631.4045,621.5181,626.3715,632.5358,
  742.1870,745.1182,743.2778,744.3731,
  870.9860,878.1458,877.9937,875.7901,
  994.4269,997.3210,1000.300,1002.415,
  1105.648,1112.969,1138.606,1124.895,
  1240.022,1234.217,1242.068,1245.227,
  1370.524,1358.120,1358.945,1357.682,
  1464.239,1474.026,1487.865,1478.287,
  1572.011,1621.633,1613.793,1597.357,
  1736.028,1718.324,1746.376,1721.307,
  1826.700,1851.710,1852.145,1869.994,
  1958.633,1960.752,1970.432,1969.139 
));
medianvalues <- matrix(ncol=configs.count,nrow=experiments,byrow=TRUE,data=c(
  500.7010,500.7900,501.7910,503.0210,
  500.8560,500.9520,501.9160,503.1640,
  500.7450,500.8720,502.1230,503.3180,
  500.7450,500.8770,502.1490,503.2500,
  500.7450,500.8810,502.1430,503.2710,
  500.7450,500.8930,502.0710,503.1930,
  500.7460,500.8790,502.1060,503.1920,
  500.7450,500.8750,502.1380,503.2000,
  500.7460,500.8850,502.1460,503.1590,
  500.7470,500.8850,502.1120,503.1590,
  500.7450,500.8870,502.0730,503.1110,
  500.7500,500.8910,502.1060,503.1540,
  500.7460,500.8750,501.9900,503.1580,
  500.7530,500.8830,501.9930,503.0950,
  500.7460,500.8740,502.1160,503.1450,
  500.7530,500.8730,502.1090,503.0650 
));

pdf(output_fn, width=8, height=5, paper="special")
plot.new()
plot.window(xlim=c(0.5,experiments+0.5),ylim=c(min(medianvalues),max(medianvalues)))
title(xlab="Experiment",ylab="Execution time (µs)")
legend("topright",inset=c(0.05,0.15),legend=c(rev(configs.labels),"(mean values)"),fill=TRUE,angle=c(45,135,45,135),density=c(30,20,10,0),title="Overhead (median) of ...",ncol=2)
for (ce in 1:experiments) {
  #meanvalues
# rect(ce-0.35,meanvalues[ce,3],ce+0.5,meanvalues[ce,4])
# rect(ce-0.35,meanvalues[ce,2],ce+0.5,meanvalues[ce,3])
# rect(ce-0.35,meanvalues[ce,1],ce+0.5,meanvalues[ce,2])
# rect(ce-0.35,0,ce+0.5,meanvalues[ce,1])
  #median
  rect(ce-0.4,medianvalues[ce,3],ce+0.4,medianvalues[ce,4],col="white",border="black")
  rect(ce-0.4,medianvalues[ce,2],ce+0.4,medianvalues[ce,3],col="white",border="black")
  rect(ce-0.4,medianvalues[ce,1],ce+0.4,medianvalues[ce,2],col="white",border="black")
  rect(ce-0.4,0,ce+0.4,medianvalues[ce,1],col="white",border="black")
  rect(ce-0.4,medianvalues[ce,3],ce+0.4,medianvalues[ce,4],angle=45,density=30)
  rect(ce-0.4,medianvalues[ce,2],ce+0.4,medianvalues[ce,3],angle=135,density=20)
  rect(ce-0.4,medianvalues[ce,1],ce+0.4,medianvalues[ce,2],angle=45,density=10)
  rect(ce-0.4,0,ce+0.4,medianvalues[ce,1],angle=135,density=5)
  for (cc in (2:configs.count)) {
    labeltext=formatC(medianvalues[ce,cc]-medianvalues[ce,cc-1],format = "f",digits=1)
      rect(ce-(strwidth(labeltext)*0.5),medianvalues[ce,cc]-strheight(labeltext),ce+(strwidth(labeltext)*0.5),medianvalues[ce,cc],col="white",border="black")
      text(ce,medianvalues[ce,cc],labels=labeltext,cex=0.75,col="black",pos=1,offset=0.1)
  }
}
axis(1, at=c(1:experiments), labels=experiments.labels,)
axis(2)
invisible(dev.off())
