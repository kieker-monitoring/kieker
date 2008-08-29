# Version 1.0 (2008/05/29): column vmid was replace by vmname, which defaults to hostname, but may be change during runtime (e.g., via the tpmon-control-servlet)
# Version 0.9 (2008/03/30): new columns executionOrderIndex and executionStackSize that enables
#	to reconstruct messageTraces for distributed systems that have
#	no global clock
# Version 0.8 (2007/10/04): added INDEX (tin) to tune tpan (the log analysis 
# 	tool). Both the traceid and tin index are only to tune data access. 
#	Especially the index on traceid is important for the current (Version 0.1) 
#	implementation of tpan.
# Version 0.7 (vmid included, requestid renamed to traceid, 
#				experimentId renamed to experimentid)

<<<<<<< .mine
CREATE TABLE turbomon5 (
=======

CREATE TABLE turbomon10 (
>>>>>>> .r159
`autoid` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
`experimentid` SMALLINT NOT NULL DEFAULT '0',
`operation` VARCHAR( 160 ) NOT NULL ,
<<<<<<< .mine
`sessionid` VARCHAR( 34 ) NOT NULL ,
`traceid` VARCHAR( 34 ) NOT NULL ,
=======
`sessionid` VARCHAR( 34 ) NOT NULL ,
`traceid` VARCHAR( 34 ) NULL ,
>>>>>>> .r159
`tin` BIGINT( 19 ) UNSIGNED NOT NULL ,
`tout` BIGINT( 19 ) UNSIGNED NOT NULL ,
`vmname` VARCHAR( 40 ) NOT NULL DEFAULT '',
`executionOrderIndex` INT( 10 ) NOT NULL DEFAULT '-1',
`executionStackSize` INT( 10 ) NOT NULL DEFAULT '-1',
INDEX (operation(16)), INDEX (traceid), INDEX (tin)
) ENGINE = MYISAM;

