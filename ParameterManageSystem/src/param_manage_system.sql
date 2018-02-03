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
/*!40000 ALTER TABLE `Project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DBStruct`
--

DROP TABLE IF EXISTS `DBStruct`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DBStruct` (
  `dbstructId` varchar(64) NOT NULL,
  `projectId` varchar(64) NOT NULL,
  `structName` varchar(255) NOT NULL,
  `structId` int(11) NOT NULL,
  `tempTable` tinyint(4) DEFAULT '0',
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`dbstructId`),
  UNIQUE KEY(`projectId`, `structName`),
  UNIQUE KEY(`projectId`, `structId`),
  CONSTRAINT `fk_DBStruct_1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DBStructMember`
--

DROP TABLE IF EXISTS `DBStructMember`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DBStructMember` (
  `dbstructmemberId` varchar(64) NOT NULL,
  `dbstructId` varchar(64) NOT NULL,
  `memberId` int(11) NOT NULL,
  `memberName` varchar(255) NOT NULL,
  `memberType` varchar(45) NOT NULL,
  `memberSize` int(11) NOT NULL,
  `primaryKey` tinyint(4) DEFAULT '0',
  `defaultValue` text,
  `valueRegex` text,
  `refStruct` text,
  `refMember` text,
  `isUnique` tinyint(4) DEFAULT '0',
  `memo` text,
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`dbstructmemberId`),
  UNIQUE KEY(`dbstructId`, `memberId`),
  UNIQUE KEY(`dbstructId`, `memberName`),
  CONSTRAINT `fk_DBStructMember_1` FOREIGN KEY (`dbstructId`) REFERENCES `DBStruct` (`dbstructId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table ``
--

DROP TABLE IF EXISTS `DBStructdefInstance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DBStructdefInstance` (
  `dbstructdefinstanceId` varchar(64) NOT NULL,
  `dbstructmemberId` varchar(64) NOT NULL,
  `instanceIndex` int(11) NOT NULL,
  `defValue` varchar(2048) DEFAULT NULL,
  PRIMARY KEY (`dbstructdefinstanceId`),
  KEY `dbstructmemberId` (`dbstructmemberId`),
  CONSTRAINT `dbstructdefinstance_ibfk_1` FOREIGN KEY (`dbstructmemberId`) REFERENCES `dbstructmember` (`dbstructmemberId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ``
--

LOCK TABLES `DBStructdefInstance` WRITE;
/*!40000 ALTER TABLE `DBStructdefInstance` DISABLE KEYS */;
/*!40000 ALTER TABLE `DBStructdefInstance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `WebStruct`
--

DROP TABLE IF EXISTS `WebStruct`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WebStruct` (
  `webstructId` varchar(64) NOT NULL,
  `projectId` varchar(64) NOT NULL,
  `structName` varchar(255) NOT NULL,
  `structCnName` varchar(255) NOT NULL,
  `classifyName` varchar(255),
  `structId` int(11) NOT NULL,
  `tempTable` tinyint(4) DEFAULT '0',
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`webstructId`),
  UNIQUE KEY(`projectId`, `structName`),
  UNIQUE KEY(`projectId`, `structId`),
  CONSTRAINT `fk_WebStruct_1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WebStructMember`
--

DROP TABLE IF EXISTS `WebStructMember`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WebStructMember` (
  `webstructmemberId` varchar(64) NOT NULL,
  `webstructId` varchar(64) NOT NULL,
  `memberId` int(11) NOT NULL,
  `memberName` varchar(255) NOT NULL,
  `memberCnName` varchar(255) NOT NULL,
  `primaryKey` tinyint(4) DEFAULT '0',
  `defaultValue` text,
  `webType` int(11) NOT NULL,
  `typeDesc` text,
  `unit` text,
  `valueRangeCn` text,
  `valueRange` text,
  `memberLevel` int(11),
  `structLevel` int(11),
  `canAddOrDelete` tinyint(4) DEFAULT '0',
  `isUnique` tinyint(4) DEFAULT '0',
  `rebootEffective` tinyint(4) DEFAULT '0',
  `setStatus` text,
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`webstructmemberId`),
  UNIQUE KEY(`webstructId`, `memberId`),
  UNIQUE KEY(`webstructId`, `memberName`),
  CONSTRAINT `fk_WebStructMember_1` FOREIGN KEY (`webstructId`) REFERENCES `WebStruct` (`webstructId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `SnmpStruct`
--

DROP TABLE IF EXISTS `SnmpStruct`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SnmpStruct` (
  `snmpstructId` varchar(64) NOT NULL,
  `projectId` varchar(64) NOT NULL,
  `structName` varchar(255) NOT NULL,
  `oid` varchar(255) NOT NULL,
  `singleTable` tinyint(4) DEFAULT '0',
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`snmpstructId`),
  UNIQUE KEY(`projectId`, `structName`),
  UNIQUE KEY(`projectId`, `oid`),
  CONSTRAINT `fk_SnmpStruct_1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SnmpStructMember`
--

DROP TABLE IF EXISTS `SnmpStructMember`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SnmpStructMember` (
  `snmpstructmemberId` varchar(64) NOT NULL,
  `snmpstructId` varchar(64) NOT NULL,
  `elementName` varchar(255) NOT NULL,
  `structName` varchar(255) NOT NULL,
  `memberName` varchar(255) NOT NULL,
  `oid` int(11) NOT NULL,
  `primaryKey` tinyint(4) DEFAULT '0',
  `writable` tinyint(4) DEFAULT '1',
  `dataType` varchar(16) NOT NULL,
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`snmpstructmemberId`),
  UNIQUE KEY(`snmpstructId`, `oid`),
  UNIQUE KEY(`snmpstructId`, `elementName`),
  UNIQUE KEY(`snmpstructId`, `structName`, `memberName`),
  CONSTRAINT `fk_SnmpStructMember_1` FOREIGN KEY (`snmpstructId`) REFERENCES `SnmpStruct` (`snmpstructId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-24 23:18:34
