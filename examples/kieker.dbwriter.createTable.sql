CREATE TABLE OperationExecutionRecords (
  `autoid` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `operationSignature` VARCHAR( 255 ) NOT NULL,
  `sessionId` VARCHAR( 88 ) NOT NULL,
  `traceId` BIGINT NOT NULL,
  `tin` BIGINT( 19 ) UNSIGNED NOT NULL,
  `tout` BIGINT( 19 ) UNSIGNED NOT NULL,
  `hostname` VARCHAR( 40 ) NOT NULL DEFAULT '',
  `eoi` INT( 10 ) NOT NULL DEFAULT '-1',
  `ess` INT( 10 ) NOT NULL DEFAULT '-1',
INDEX (operation(16)), INDEX (traceid), INDEX (tin)
);
