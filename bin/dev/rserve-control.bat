@ECHO OFF

IF "%1"=="start" (
  ECHO "Trying to start Rserve..."
  start R CMD BATCH RserveStart.R RserveStart.log --vanilla
)

IF "%1"=="stop" (
  "Trying to stop Rserve..."
  TASKKILL /F /IM Rserve.exe /T
)

IF "%1"=="" (
  :: Print usage
  ECHO "Usage: %0 (start|stop)"
)
