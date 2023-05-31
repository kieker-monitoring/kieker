import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.core.filter.EvaluatorFilter
import ch.qos.logback.classic.boolex.GEventEvaluator

import static ch.qos.logback.core.spi.FilterReply.DENY
import static ch.qos.logback.core.spi.FilterReply.NEUTRAL

appender("FILE", FileAppender) {
  filter(ThresholdFilter) {
    level=TRACE
  }
  file = "kieker.log"
  append = true
  encoder(PatternLayoutEncoder) {
    pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
  }
}

appender("STDOUT", ConsoleAppender) {
  filter(ThresholdFilter) {
    level=INFO
  }
  encoder(PatternLayoutEncoder) {
    pattern = "<> %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
  }
  target="System.out"
}

root(DEBUG, ["STDOUT", "FILE"])



