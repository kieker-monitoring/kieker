forecasters=c("ARIMA", "ARIMA101", "CS", "MEAN", "NAIVE", "ETS" ,"SES", "MEANJAVA", "CROSTON") 
inputPath="output-data/wikiGer24_Oct11_21d-"
outputPath="output-plots/wikiGer24_Oct11_21d-"
fileExtension=".csv"
sequence_count=sequence(504)

calculateMASE <- function(f,y) { # f = vector with forecasts, y = vector with actuals
  if(length(f)!=length(y)){ stop("Vector length is not equal") }
  n <- length(f)
  return(mean(abs((y - f) / ((1/(n-1)) * sum(abs(y[2:n]-y[1:n-1]))))))
}

sanitizedMASE <- function(f,y) {
  # Check for precondition (equal length of both vectors)
  if(length(f) != length(y)) {
    stop("Vector length is not equal")
  }
  data = c()
  n <- length(f)

  # Collect all pairs collecting no NaN values
  for(i in 1:n){
    if(is.nan(f[i]) | is.nan(y[i])){
      # NaN pairs should not be used
      # so do nothing
    } else {
      data <- c(data,i)
    }
  }

  # construct new vectors without the pairs containing NaNs
  newF <- c()
  newY <- c()
  for(i in 1:length(data)) {
    newF <- c(newF,f[data[i]])
    newY <- c(newY,y[data[i]])
  }

  # calculate MASE with sanitized vectors and return it
  return(calculateMASE(newF,newY))
}

for (i in forecasters)
{
  filenamex=paste(inputPath,i,fileExtension,sep="")
  if(file.exists(filenamex)){
    results=read.table(file=filenamex, header=TRUE, sep=";", dec=".")
    printMase=FALSE
    maseString=""
    
    if(results$confidence[1] > 0)
    {
      printMase=TRUE
      start=1
      end=length(results$forecast)
      mase=sanitizedMASE(f=results$forecast[start:end],y=results$pageRequests[start:end])
      maseString = paste(" (MASE: ",mase,")",sep="")
      # print(paste(i,": ",mase,sep=""))
    }
    
    pdf(width=42,file=paste(outputPath, i, ".pdf",sep=""))
    plot(x=sequence_count,y=results$pageRequests,type="l",main=paste(i, maseString, sep=""),ylab="Page Requests",xlab="")
    lines(x=sequence_count,y=results$forecast,type="l",col="red")
    
    colors=c("black", "red")
    legends=c("Measurements", "Forecasts")
    
    if(printMase) {
      lines(x=sequence_count,y=results$confidenceUpper,lty=2,col="gray")
      lines(x=sequence_count,y=results$confidenceLower,lty=2,col="gray")
      colors=c("black", "red", "gray", "gray")
      legends=c("Measurements", "Forecasts (95% CI)", "Upper Confidence (95% CI)", "Lower Confidence (95% CI)")
    }
  
    # abline(h=mase,col="yellow")
    legend(
      "topleft",
      col=colors,
      lty=1,
      legend=legends)
    dev.off()
  }
}

