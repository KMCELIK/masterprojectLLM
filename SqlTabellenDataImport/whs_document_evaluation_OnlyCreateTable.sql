CREATE TABLE document_evaluation (
  Path varchar(255) NOT NULL,
  sollJson text NOT NULL,
  istJson text NOT NULL,
  Reader varchar(100) NOT NULL,
  mistakes int NOT NULL,
  correctness int NOT NULL,
  logId bigint unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`logId`),
  UNIQUE KEY `logId` (`logId`)
)