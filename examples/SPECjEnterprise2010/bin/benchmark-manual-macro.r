output_fn="results-macro.pdf"

values1 <- array(dim=c(2,2,5),dimnames=list(c("nokiek","kieker"),c("mean","ci95%"),c(1:5)))
values1["kieker",,] <- c(
c(0.043,0.0012),
c(0.031,0.0031),
c(0.020,0.0012),
c(0.016,0.0005),
c(0.041,0.0023))
values1["nokiek",,] <- c(
c(0.042,0.0012),
c(0.026,0.0012),
c(0.015,0.0012),
c(0.012,0.0005),
c(0.031,0.0023))

values2 <- array(dim=c(2,2,5),dimnames=list(c("nokiek","kieker"),c("mean","ci95%"),c(1:5)))
values2["kieker",,] <- c(
c(0.036,0.0006),
c(0.018,0.0012),
c(0.012,0.0012),
c(0.009,0.0003),
c(0.026,0.0012))
values2["nokiek",,] <- c(
c(0.036,0.0006),
c(0.016,0.0003),
c(0.010,0.0003),
c(0.008,0.0003),
c(0.023,0.0002))

values3 <- array(dim=c(2,2,5),dimnames=list(c("nokiek","kieker"),c("mean","ci95%"),c(1:5)))
values3["kieker",,] <- c(
c(0.036,0.0005),
c(0.017,0.0004),
c(0.011,0.0012),
c(0.009,0.0012),
c(0.024,0.0002))
values3["nokiek",,] <- c(
c(0.035,0.0004),
c(0.016,0.0003),
c(0.009,0.0002),
c(0.008,0.0002),
c(0.022,0.0001))


pdf(output_fn,width=8,height=5,paper="special")
plot.new()
plot.window(xlim=c(0.55,3.45),ylim=c(0,0.06))
title(xlab="Active Cores",ylab="Response time (s)")
legend.labels=c("CreateVehicleEJB","CreateVehicleWS","","Purchase","Manage","Browse")
legend("topright",inset=c(0.0,0.05),legend=legend.labels,fill=TRUE,angle=c(45),density=c(5,10,0,15,20,25),title="Mean response times (with 95% confidence intervals)",ncol=2)
axis(1, at=c(0:4))
axis(2)

##singlecore
for (ce in c(1:5)) {
  rect(0.36+ce*0.18+0.02, 0, 0.36+ce*0.18+0.1, values1["nokiek","mean",ce],angle=45,density=ce*5)
    ciu=values1["nokiek","mean",ce]+values1["nokiek","ci95%",ce]
    cil=values1["nokiek","mean",ce]-values1["nokiek","ci95%",ce]
    lines(c(0.36+ce*0.18+0.04,0.36+ce*0.18+0.08),c(ciu,ciu))
    lines(c(0.36+ce*0.18+0.06,0.36+ce*0.18+0.06),c(ciu,cil))
    lines(c(0.36+ce*0.18+0.04,0.36+ce*0.18+0.08),c(cil,cil))
  rect(0.36+ce*0.18+0.1, 0, 0.36+ce*0.18+0.18, values1["kieker","mean",ce],angle=135,density=ce*5)
    ciu=values1["kieker","mean",ce]+values1["kieker","ci95%",ce]
    cil=values1["kieker","mean",ce]-values1["kieker","ci95%",ce]
    lines(c(0.36+ce*0.18+0.12,0.36+ce*0.18+0.16),c(ciu,ciu))
    lines(c(0.36+ce*0.18+0.14,0.36+ce*0.18+0.14),c(ciu,cil))
    lines(c(0.36+ce*0.18+0.12,0.36+ce*0.18+0.16),c(cil,cil))
}

##dualcore
for (ce in c(1:5)) {
  rect(1.36+ce*0.18+0.02, 0, 1.36+ce*0.18+0.1, values2["nokiek","mean",ce],angle=45,density=ce*5)
    ciu=values2["nokiek","mean",ce]+values2["nokiek","ci95%",ce]
    cil=values2["nokiek","mean",ce]-values2["nokiek","ci95%",ce]
    lines(c(1.36+ce*0.18+0.04,1.36+ce*0.18+0.08),c(ciu,ciu))
    lines(c(1.36+ce*0.18+0.06,1.36+ce*0.18+0.06),c(ciu,cil))
    lines(c(1.36+ce*0.18+0.04,1.36+ce*0.18+0.08),c(cil,cil))
  rect(1.36+ce*0.18+0.1, 0, 1.36+ce*0.18+0.18, values2["kieker","mean",ce],angle=135,density=ce*5)
    ciu=values2["kieker","mean",ce]+values2["kieker","ci95%",ce]
    cil=values2["kieker","mean",ce]-values2["kieker","ci95%",ce]
    lines(c(1.36+ce*0.18+0.12,1.36+ce*0.18+0.16),c(ciu,ciu))
    lines(c(1.36+ce*0.18+0.14,1.36+ce*0.18+0.14),c(ciu,cil))
    lines(c(1.36+ce*0.18+0.12,1.36+ce*0.18+0.16),c(cil,cil))
}

##triplecore
for (ce in c(1:5)) {
  rect(2.36+ce*0.18+0.02, 0, 2.36+ce*0.18+0.1, values3["nokiek","mean",ce],angle=45,density=ce*5)
    ciu=values3["nokiek","mean",ce]+values3["nokiek","ci95%",ce]
    cil=values3["nokiek","mean",ce]-values3["nokiek","ci95%",ce]
    lines(c(2.36+ce*0.18+0.04,2.36+ce*0.18+0.08),c(ciu,ciu))
    lines(c(2.36+ce*0.18+0.06,2.36+ce*0.18+0.06),c(ciu,cil))
    lines(c(2.36+ce*0.18+0.04,2.36+ce*0.18+0.08),c(cil,cil))
  rect(2.36+ce*0.18+0.1, 0, 2.36+ce*0.18+0.18, values3["kieker","mean",ce],angle=135,density=ce*5)
    ciu=values3["kieker","mean",ce]+values3["kieker","ci95%",ce]
    cil=values3["kieker","mean",ce]-values3["kieker","ci95%",ce]
    lines(c(2.36+ce*0.18+0.12,2.36+ce*0.18+0.16),c(ciu,ciu))
    lines(c(2.36+ce*0.18+0.14,2.36+ce*0.18+0.14),c(ciu,cil))
    lines(c(2.36+ce*0.18+0.12,2.36+ce*0.18+0.16),c(cil,cil))
}

invisible(dev.off())
