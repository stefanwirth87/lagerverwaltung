CREATE DATABASE  IF NOT EXISTS `projekt` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `projekt`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: projekt
-- ------------------------------------------------------
-- Server version	5.7.15-log

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
-- Table structure for table `artikel`
--

DROP TABLE IF EXISTS `artikel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artikel` (
  `ArtikelNr` int(11) NOT NULL AUTO_INCREMENT,
  `Hersteller` varchar(45) DEFAULT NULL,
  `Model` varchar(45) DEFAULT NULL,
  `Beschreibung` varchar(256) DEFAULT NULL,
  `Anzahl` int(11) DEFAULT NULL,
  `Preis` float(11,2) DEFAULT NULL,
  `LagerID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ArtikelNr`),
  KEY `lager_idx` (`LagerID`),
  CONSTRAINT `lager` FOREIGN KEY (`LagerID`) REFERENCES `lager` (`LagerID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10000042 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artikel`
--

LOCK TABLES `artikel` WRITE;
/*!40000 ALTER TABLE `artikel` DISABLE KEYS */;
INSERT INTO `artikel` VALUES (10000001,'Haribo','Gummibaerchen','Suesswaren',14,1.00,14),(10000002,'Haribo','Cola-Flaschen','Suesswaren',0,2.00,0),(10000003,'Falken','235/40','Sommer',42,11.00,3),(10000004,'Hankook','125/20','Winter',89,1.00,4),(10000005,'Stadler','HB Bleistift','Beistift Härte: HB',26,0.80,1),(10000006,'Zirndoerfer','Keller','Getraenk',42,20.00,6),(10000007,'Fischer','Das Helle','Getraenk',7,13.00,7),(10000008,'Oettinger','Export','Getraenk',97,7.00,8),(10000009,'LG','monitor 24\"','24\" Monitor',1,99.99,9),(10000010,'MSI','GTX 980','Grafikkarte mit Nvidia Grafikchip',4,99.99,10),(10000011,'Sony','Playstation 4','next gen Spielekonsole',8,99.99,11),(10000012,'Trolli','Saure Würmer','Gummiwürmer',79,1.00,12),(10000013,'Samsung','Galay S5','Smartphone, Handy, Telefonieren',1,99.99,13),(10000014,'Acer','Aspire R7','Laptop',2,99.99,27),(10000020,'Haribo','Qaxi Frösche','Gummifroesche',11,2.00,15),(10000021,'Samsung','RV530','Laptop',3,99.99,16),(10000022,'Brother','MFC-5490CN','Multifunktionsdrucker',4,99.99,17),(10000023,'Avery','Plano Supirior','Mehrzweckpapier, Druckerpapier, 500 Blatt',100,5.00,18),(10000025,'Rudolf Quelle','Wasser Medium','Medium Wasser Kiste 20x1l',18,3.00,19),(10000026,'MSI','MB-954-H3','Mainboard, Intel, 1105',2,99.99,20),(10000027,'Cisco','1948','Router der nicht funktioniert',4,99.99,21),(10000028,'Leitz','Tacker','Tackern wie ein Meister',17,7.00,22),(10000029,'Festo','Stichsäge','Stichsäge für Schreiner',8,99.99,23),(10000030,'Makita','ST340','Vorsichtig behandeln',19,60.00,24),(10000031,'Logitech','M705','Maus mit langer Akkulaufzeit',7,40.00,5),(10000032,'ESTW','Kugelschreiber','Standard',20,2.00,25),(10000033,'Hohes C','Orangensaft','Braune Flasche',200,12345.00,26),(10000034,'Canon','CLI-512','Schwarze Tintenpatrone',15,11.99,2),(10000037,'Volvic Tee','Grüntee-Granatapfel','Volvic Tee mit Grüntee-Extrakt & Granatapfel geschmack',30,0.75,28),(10000038,'Volvic Tee','Grüntee-Zitrone','Volvic Tee mit Grüntee-Extrakt & Zitronen geschmack',25,0.75,29),(10000039,'Schmitt','Gute-Laune-Pad','MausPad der guten Laune für Studenten',55,5.95,30),(10000040,'Apple','Watch Serie 2','Apple Watch Serie 2 Aluminiumgehäuse, Silber, mit Sportarmband, Weiß\n',30,319.00,31),(10000041,'Apple','iPhone 7+, 128GB, schwarz','Apple iPhone 7+ mit 128GB in schwarz',28,1150.00,32);
/*!40000 ALTER TABLE `artikel` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`Jan`@`%`*/ /*!50003 TRIGGER `Projekt`.`Artikel_AFTER_INSERT` AFTER INSERT ON `Artikel` FOR EACH ROW
BEGIN

UPDATE `Projekt`.`Log_Test` SET `einfuegen`=`einfuegen` + 1 WHERE `Log_Test_ID`='1';

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`Jan`@`%`*/ /*!50003 TRIGGER `Projekt`.`Artikel_AFTER_UPDATE` AFTER UPDATE ON `Artikel` FOR EACH ROW
BEGIN

UPDATE `Projekt`.`Log_Test` SET `aendern`=`aendern` + 1 WHERE `Log_Test_ID`='1';

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`Jan`@`%`*/ /*!50003 TRIGGER `Projekt`.`Artikel_AFTER_DELETE` AFTER DELETE ON `Artikel` FOR EACH ROW
BEGIN

UPDATE `Projekt`.`Log_Test` SET `loeschen`=`loeschen` + 1 WHERE `Log_Test_ID`='1';

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `bestellungen`
--

DROP TABLE IF EXISTS `bestellungen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bestellungen` (
  `Bestell_ID` int(11) NOT NULL AUTO_INCREMENT,
  `KundenNr` int(11) NOT NULL,
  `Status_ID` int(11) NOT NULL,
  `Zeit` datetime NOT NULL,
  PRIMARY KEY (`Bestell_ID`),
  KEY `kunden_idx` (`KundenNr`),
  KEY `status` (`Status_ID`),
  CONSTRAINT `kunden` FOREIGN KEY (`KundenNr`) REFERENCES `kunden` (`Kunden_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `status` FOREIGN KEY (`Status_ID`) REFERENCES `status` (`Status_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10000000 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bestellungen`
--

LOCK TABLES `bestellungen` WRITE;
/*!40000 ALTER TABLE `bestellungen` DISABLE KEYS */;
INSERT INTO `bestellungen` VALUES (10000000,null,null,'2016-01-01 01:01:01');
/*!40000 ALTER TABLE `bestellungen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kunden`
--

DROP TABLE IF EXISTS `kunden`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kunden` (
  `Kunden_ID` int(11) NOT NULL AUTO_INCREMENT,
  `Anrede` varchar(45) DEFAULT NULL,
  `Nachname` varchar(45) DEFAULT NULL,
  `Vorname` varchar(45) DEFAULT NULL,
  `Straße` varchar(45) DEFAULT NULL,
  `Hausnummer` varchar(10) DEFAULT NULL,
  `Postleitzahl` int(11) DEFAULT NULL,
  `Ort` varchar(45) DEFAULT NULL,
  `Telefon` varchar(45) DEFAULT NULL,
  `EMail` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Kunden_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kunden`
--

LOCK TABLES `kunden` WRITE;
/*!40000 ALTER TABLE `kunden` DISABLE KEYS */;
INSERT INTO `kunden` VALUES (1,'Herr','Test','Test','Pseudostraße','1',99999,'Test','02147483647','test@test.de');
/*!40000 ALTER TABLE `kunden` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lager`
--

DROP TABLE IF EXISTS `lager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lager` (
  `LagerID` int(11) NOT NULL,
  `Zustand` int(11) DEFAULT '0',
  PRIMARY KEY (`LagerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lager`
--

LOCK TABLES `lager` WRITE;
/*!40000 ALTER TABLE `lager` DISABLE KEYS */;
INSERT INTO `lager` VALUES (0,1);
/*!40000 ALTER TABLE `lager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `lagerbestand_ausverkauft`
--

DROP TABLE IF EXISTS `lagerbestand_ausverkauft`;
/*!50001 DROP VIEW IF EXISTS `lagerbestand_ausverkauft`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `lagerbestand_ausverkauft` AS SELECT 
 1 AS `ArtikelNr`,
 1 AS `Hersteller`,
 1 AS `Model`,
 1 AS `Anzahl`,
 1 AS `Preis`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `lagerbestand_gesamt`
--

DROP TABLE IF EXISTS `lagerbestand_gesamt`;
/*!50001 DROP VIEW IF EXISTS `lagerbestand_gesamt`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `lagerbestand_gesamt` AS SELECT 
 1 AS `ArtikelNr`,
 1 AS `Hersteller`,
 1 AS `Model`,
 1 AS `Anzahl`,
 1 AS `Preis`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `lagerbestand_vorhanden`
--

DROP TABLE IF EXISTS `lagerbestand_vorhanden`;
/*!50001 DROP VIEW IF EXISTS `lagerbestand_vorhanden`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `lagerbestand_vorhanden` AS SELECT 
 1 AS `ArtikelNr`,
 1 AS `Hersteller`,
 1 AS `Model`,
 1 AS `Anzahl`,
 1 AS `Preis`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `log_test`
--

DROP TABLE IF EXISTS `log_test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_test` (
  `Log_Test_ID` int(11) NOT NULL AUTO_INCREMENT,
  `User_ID` int(11) DEFAULT NULL,
  `einfuegen` int(11) DEFAULT NULL,
  `aendern` int(11) DEFAULT NULL,
  `loeschen` int(11) DEFAULT NULL,
  PRIMARY KEY (`Log_Test_ID`),
  KEY `User_ID_idx` (`User_ID`),
  CONSTRAINT `Log_Test_ibfk_1` FOREIGN KEY (`User_ID`) REFERENCES `user` (`User_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_test`
--

LOCK TABLES `log_test` WRITE;
/*!40000 ALTER TABLE `log_test` DISABLE KEYS */;
INSERT INTO `log_test` VALUES (null,null,null,null,null);
/*!40000 ALTER TABLE `log_test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manuelles_ausbuchen`
--

DROP TABLE IF EXISTS `manuelles_ausbuchen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `manuelles_ausbuchen` (
  `MA_ID` int(11) NOT NULL AUTO_INCREMENT,
  `User_ID` int(11) DEFAULT NULL,
  `ArtikelNr` int(11) DEFAULT NULL,
  `Menge` int(11) DEFAULT NULL,
  `Datum` datetime DEFAULT NULL,
  `Kommentar` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`MA_ID`),
  KEY `ArtieklNr_idx` (`ArtikelNr`),
  CONSTRAINT `ArtieklNr` FOREIGN KEY (`ArtikelNr`) REFERENCES `artikel` (`ArtikelNr`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manuelles_ausbuchen`
--

LOCK TABLES `manuelles_ausbuchen` WRITE;
/*!40000 ALTER TABLE `manuelles_ausbuchen` DISABLE KEYS */;
INSERT INTO `manuelles_ausbuchen` VALUES ();
/*!40000 ALTER TABLE `manuelles_ausbuchen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `positionen_bestellt`
--

DROP TABLE IF EXISTS `positionen_bestellt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `positionen_bestellt` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ArtikelNr` int(11) NOT NULL,
  `Anzahl` int(11) NOT NULL,
  `BestellNr` int(11) NOT NULL,
  `Ausgebucht` varchar(45) NOT NULL DEFAULT 'false',
  `Anzahl_Ausgebucht` int(11) NOT NULL DEFAULT '0',
  `U_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `artikel_idx` (`ArtikelNr`),
  KEY `bestellung_idx` (`BestellNr`),
  KEY `user_idx` (`U_ID`),
  CONSTRAINT `artikel` FOREIGN KEY (`ArtikelNr`) REFERENCES `artikel` (`ArtikelNr`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `bestellt` FOREIGN KEY (`BestellNr`) REFERENCES `bestellungen` (`Bestell_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user` FOREIGN KEY (`U_ID`) REFERENCES `user` (`User_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=142 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `positionen_bestellt`
--

LOCK TABLES `positionen_bestellt` WRITE;
/*!40000 ALTER TABLE `positionen_bestellt` DISABLE KEYS */;
INSERT INTO `positionen_bestellt` VALUES (null,null,null,null,'true',null,null);
/*!40000 ALTER TABLE `positionen_bestellt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status` (
  `Status_ID` int(11) NOT NULL,
  `Status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Status_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (1,'in Bearbeitung'),(2,'Abgeschlossen'),(3,'Cancel');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `User_ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) DEFAULT NULL,
  `Passwort` varchar(45) DEFAULT NULL,
  `Berechtigung` int(11) DEFAULT NULL,
  PRIMARY KEY (`User_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Test','Test',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'projekt'
--

--
-- Dumping routines for database 'projekt'
--
/*!50003 DROP PROCEDURE IF EXISTS `Lagerplatz_freigeben` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`lagerverwaltung`@`%` PROCEDURE `Lagerplatz_freigeben`(n INT)
BEGIN
    
    

set @s = (select anzahl from Artikel where Artikelnr = n);


    
IF @s = 0 THEN 
    
UPDATE `Projekt`.`Lager` 
    
INNER JOIN `Projekt`.`Artikel`
    
SET `Lager`.`Zustand`='0' 
    
WHERE `Artikel`.`LagerID`= `Lager`.`LagerID` 
    
AND `Artikel`.`ArtikelNr` = n;


UPDATE `Projekt`.`Artikel` 
    
SET `Artikel`.`LagerID`='0' 
        
WHERE `Artikel`.`ArtikelNr` = n;

    

END IF;

	

  
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `Manuelles_ausbuchen` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`lagerverwaltung`@`%` PROCEDURE `Manuelles_ausbuchen`(u INT, a INT, m INT , t VARCHAR(200))
BEGIN
  
	INSERT INTO `Projekt`.`Manuelles_Ausbuchen` (`User_ID`, `ArtikelNr`, `Menge`, `Datum`, `Kommentar`) VALUES (u, a, m, now(), t);
   
   
    set @s = (select anzahl from Artikel where Artikelnr = a);
    
    SET @s = @s - m;
    
    UPDATE `Projekt`.`Artikel` 
    
	INNER JOIN `Projekt`.`Manuelles_Ausbuchen`
    
    INNER JOIN `Projekt`.`Lager`
    
    set `Artikel`.`Anzahl` = @s
    
	WHERE `Artikel`.`LagerID`= `Lager`.`LagerID` 
    
	AND `Artikel`.`ArtikelNr` = a;
    
    call Lagerplatz_freigeben(a);
    
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `Position_ausbuchen` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`lagerverwaltung`@`%` PROCEDURE `Position_ausbuchen`(a INT, b INT, m INT, u INT)
BEGIN
    
    set @s = (select Anzahl from Positionen_Bestellt where Artikelnr = a and BestellNr = b);

    UPDATE `Projekt`.`Positionen_Bestellt` 
    SET `Positionen_Bestellt`.`Anzahl_Ausgebucht`= `Positionen_Bestellt`.`Anzahl_Ausgebucht` + m
    WHERE `Positionen_Bestellt`.`ArtikelNr` = a 
    AND `Positionen_Bestellt`.`BestellNr` = b;
    
    set @m = (select Anzahl_Ausgebucht from Positionen_Bestellt where ArtikelNr = a and BestellNr = b);


    UPDATE `Projekt`.`Positionen_Bestellt` 
    SET `Positionen_Bestellt`.`U_ID` = u
    WHERE `Positionen_Bestellt`.`ArtikelNr` = a 
    AND `Positionen_Bestellt`.`BestellNr` = b;
    
    
    set @z = (select anzahl from Artikel where Artikelnr = a);
    SET @z = @z - m;
    UPDATE `Projekt`.`Artikel` 
	INNER JOIN `Projekt`.`Manuelles_Ausbuchen`
    INNER JOIN `Projekt`.`Lager`
    set `Artikel`.`Anzahl` = @z
	WHERE `Artikel`.`LagerID`= `Lager`.`LagerID` 
	AND `Artikel`.`ArtikelNr` = a;


    IF @s = @m THEN 
    UPDATE `Projekt`.`Positionen_Bestellt` 
    SET `Positionen_Bestellt`.`Ausgebucht`='true' 
    WHERE `Positionen_Bestellt`.`ArtikelNr` = a 
    AND `Positionen_Bestellt`.`BestellNr` = b;
    END IF;
    
    set @b_a_g = (select count(BestellNr) from Positionen_Bestellt where BestellNr = b);
    set @b_a_t = (select count(BestellNr) from Positionen_Bestellt where BestellNr = b and Ausgebucht = 'true');
    
    IF @b_a_g = @b_a_t THEN
    UPDATE `Projekt`.`Bestellungen`
    SET `Bestellungen`.`Status_ID`='2'
    WHERE `Bestellungen`.`Bestell_ID` = b;
    END IF;
    
    call Lagerplatz_freigeben(a);

  END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `lagerbestand_ausverkauft`
--

/*!50001 DROP VIEW IF EXISTS `lagerbestand_ausverkauft`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`lagerverwaltung`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `lagerbestand_ausverkauft` AS select `artikel`.`ArtikelNr` AS `ArtikelNr`,`artikel`.`Hersteller` AS `Hersteller`,`artikel`.`Model` AS `Model`,`artikel`.`Anzahl` AS `Anzahl`,`artikel`.`Preis` AS `Preis` from `artikel` where (`artikel`.`Anzahl` = 0) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `lagerbestand_gesamt`
--

/*!50001 DROP VIEW IF EXISTS `lagerbestand_gesamt`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`lagerverwaltung`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `lagerbestand_gesamt` AS select `artikel`.`ArtikelNr` AS `ArtikelNr`,`artikel`.`Hersteller` AS `Hersteller`,`artikel`.`Model` AS `Model`,`artikel`.`Anzahl` AS `Anzahl`,`artikel`.`Preis` AS `Preis` from `artikel` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `lagerbestand_vorhanden`
--

/*!50001 DROP VIEW IF EXISTS `lagerbestand_vorhanden`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`lagerverwaltung`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `lagerbestand_vorhanden` AS select `artikel`.`ArtikelNr` AS `ArtikelNr`,`artikel`.`Hersteller` AS `Hersteller`,`artikel`.`Model` AS `Model`,`artikel`.`Anzahl` AS `Anzahl`,`artikel`.`Preis` AS `Preis` from `artikel` where (`artikel`.`Anzahl` <> 0) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-06  8:49:25
