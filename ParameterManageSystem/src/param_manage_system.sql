-- MySQL dump 10.16  Distrib 10.1.25-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: param_manage_system
-- ------------------------------------------------------
-- Server version	10.1.25-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `DBStruct`
--

DROP TABLE IF EXISTS `DBStruct`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DBStruct` (
  `projectId` varchar(64) NOT NULL,
  `structName` varchar(45) NOT NULL,
  `structId` int(11) NOT NULL,
  `tempTable` tinyint(4) DEFAULT '0',
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`projectId`,`structId`),
  CONSTRAINT `fk_DBStruct_1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DBStruct`
--

LOCK TABLES `DBStruct` WRITE;
/*!40000 ALTER TABLE `DBStruct` DISABLE KEYS */;
INSERT INTO `DBStruct` VALUES ('142129c1-8798-11e7-8482-525400bbd1a8','DeviceInfo',60001,0,'2017-08-24 00:23:28'),('142129c1-8798-11e7-8482-525400bbd1a8','test',60002,0,'2017-08-24 00:30:26');
/*!40000 ALTER TABLE `DBStruct` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DBStructElement`
--

DROP TABLE IF EXISTS `DBStructElement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DBStructElement` (
  `projectId` varchar(64) NOT NULL,
  `structId` int(11) NOT NULL,
  `elementId` int(11) NOT NULL,
  `elementName` varchar(45) NOT NULL,
  `elementType` varchar(45) NOT NULL,
  `elementSize` int(11) NOT NULL,
  `primaryKey` tinyint(4) DEFAULT '0',
  `defaultValue` text,
  `valueRegex` text,
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`projectId`,`structId`,`elementId`),
  CONSTRAINT `fk_DBStructElement_1` FOREIGN KEY (`projectId`, `structId`) REFERENCES `DBStruct` (`projectId`, `structId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DBStructElement`
--

LOCK TABLES `DBStructElement` WRITE;
/*!40000 ALTER TABLE `DBStructElement` DISABLE KEYS */;
INSERT INTO `DBStructElement` VALUES ('142129c1-8798-11e7-8482-525400bbd1a8',60001,600001001,'testElement','Char',64,0,NULL,NULL,'2017-08-24 00:24:28'),('142129c1-8798-11e7-8482-525400bbd1a8',60001,600001002,'elmt2','Uint32',4,0,NULL,NULL,'2017-08-24 00:26:18');
/*!40000 ALTER TABLE `DBStructElement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Project`
--

DROP TABLE IF EXISTS `Project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Project` (
  `projectId` varchar(64) NOT NULL,
  `projectName` varchar(255) NOT NULL,
  `comment` varchar(256) DEFAULT NULL,
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`projectId`),
  UNIQUE KEY `projectName` (`projectName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Project`
--

LOCK TABLES `Project` WRITE;
/*!40000 ALTER TABLE `Project` DISABLE KEYS */;
INSERT INTO `Project` VALUES ('142129c1-8798-11e7-8482-525400bbd1a8','test','','2017-08-23 00:14:50');
/*!40000 ALTER TABLE `Project` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-24 23:18:34
