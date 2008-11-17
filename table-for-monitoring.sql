CREATE TABLE tpmondata(
`autoid` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
`experimentid` SMALLINT NOT NULL DEFAULT '0',
`operation` VARCHAR( 160 ) NOT NULL ,
`sessionid` VARCHAR( 34 ) NOT NULL ,
`traceid` VARCHAR( 34 ) NOT NULL ,
`tin` BIGINT( 19 ) UNSIGNED NOT NULL ,
`tout` BIGINT( 19 ) UNSIGNED NOT NULL ,
`vmname` VARCHAR( 40 ) NOT NULL DEFAULT '',
`executionOrderIndex` INT( 10 ) NOT NULL DEFAULT '-1',
`executionStackSize` INT( 10 ) NOT NULL DEFAULT '-1',
INDEX (operation(16)), INDEX (traceid), INDEX (tin)
) ENGINE = MYISAM;

