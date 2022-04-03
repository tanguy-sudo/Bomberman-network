-- MySQL dump 10.13  Distrib 8.0.27, for Linux (x86_64)
--
-- Host: localhost    Database: bombermanbdd
-- ------------------------------------------------------
-- Server version	8.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `bombermanbdd`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `bombermanbdd` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `bombermanbdd`;

--
-- Table structure for table `game`
--

DROP TABLE IF EXISTS `game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=109 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game`
--

LOCK TABLES `game` WRITE;
/*!40000 ALTER TABLE `game` DISABLE KEYS */;
INSERT INTO `game` VALUES (26,'2022-03-08 22:48:53'),(25,'2022-03-08 22:44:58'),(32,'2022-03-14 10:34:28'),(33,'2022-03-14 10:45:30'),(34,'2022-03-14 10:59:58'),(35,'2022-03-14 12:14:25'),(36,'2022-03-14 13:44:31'),(37,'2022-03-14 13:49:59'),(38,'2022-03-14 13:50:54'),(39,'2022-03-14 13:58:02'),(40,'2022-03-14 14:01:27'),(41,'2022-03-14 14:06:27'),(42,'2022-03-14 14:08:47'),(43,'2022-03-14 14:09:55'),(44,'2022-03-14 14:14:58'),(45,'2022-03-14 14:15:36'),(46,'2022-03-14 14:18:07'),(47,'2022-03-14 14:18:25'),(48,'2022-03-14 14:20:21'),(49,'2022-03-14 14:20:35'),(50,'2022-03-14 14:21:30'),(51,'2022-03-14 14:21:42'),(52,'2022-03-14 14:22:22'),(53,'2022-03-20 21:02:03'),(54,'2022-03-20 21:05:29'),(55,'2022-03-20 21:09:13'),(56,'2022-03-20 21:11:42'),(57,'2022-03-20 21:22:02'),(58,'2022-03-20 21:24:32'),(59,'2022-03-21 08:34:53'),(60,'2022-03-21 08:38:37'),(61,'2022-03-21 08:38:51'),(62,'2022-03-21 08:40:08'),(63,'2022-03-21 08:40:43'),(64,'2022-03-21 08:48:22'),(65,'2022-03-21 08:48:37'),(66,'2022-03-21 08:54:43'),(67,'2022-03-21 10:53:18'),(68,'2022-03-21 10:55:52'),(69,'2022-03-21 10:56:24'),(70,'2022-03-21 10:56:33'),(71,'2022-03-21 10:58:50'),(72,'2022-03-21 11:00:54'),(73,'2022-03-21 12:11:31'),(74,'2022-03-21 12:12:27'),(75,'2022-03-21 12:13:23'),(76,'2022-03-28 15:55:56'),(77,'2022-03-28 15:56:58'),(78,'2022-03-28 15:57:32'),(79,'2022-03-28 15:58:35'),(80,'2022-03-28 15:59:36'),(81,'2022-03-28 15:59:50'),(82,'2022-03-28 16:00:15'),(83,'2022-03-28 16:01:14'),(84,'2022-03-29 12:52:43'),(85,'2022-03-29 13:15:17'),(86,'2022-03-29 13:15:58'),(87,'2022-03-29 13:16:20'),(88,'2022-03-29 13:16:30'),(89,'2022-03-29 13:16:42'),(90,'2022-03-29 13:17:36'),(91,'2022-03-29 13:17:52'),(92,'2022-03-29 13:18:09'),(93,'2022-03-29 13:18:32'),(94,'2022-04-01 08:22:06'),(95,'2022-04-01 08:24:16'),(96,'2022-04-01 08:26:25'),(97,'2022-04-01 08:28:00'),(98,'2022-04-01 08:29:28'),(99,'2022-04-01 08:33:00'),(100,'2022-04-01 08:33:33'),(101,'2022-04-01 08:35:46'),(102,'2022-04-01 08:36:22'),(103,'2022-04-01 08:36:51'),(104,'2022-04-03 19:58:51'),(105,'2022-04-03 19:59:11'),(106,'2022-04-03 19:59:48'),(107,'2022-04-03 20:00:52'),(108,'2022-04-03 20:01:22');
/*!40000 ALTER TABLE `game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `play`
--

DROP TABLE IF EXISTS `play`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `play` (
  `id` int NOT NULL AUTO_INCREMENT,
  `results` varchar(10) DEFAULT NULL,
  `id_game` int NOT NULL,
  `id_user` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_game` (`id_game`),
  KEY `id_user` (`id_user`)
) ENGINE=MyISAM AUTO_INCREMENT=89 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `play`
--

LOCK TABLES `play` WRITE;
/*!40000 ALTER TABLE `play` DISABLE KEYS */;
INSERT INTO `play` VALUES (6,'gagné',25,17),(5,'gagné',25,18),(4,'perdu',26,17),(7,'perdu',26,18),(45,'gagné',32,18),(46,'gagné',32,17),(47,'gagné',33,18),(48,'perdu',35,17),(49,'perdu',37,17),(50,'perdu',53,17),(51,'gagné',54,17),(52,'gagné',56,17),(53,'gagné',56,18),(54,'gagné',56,25),(55,'gagné',58,18),(56,'gagné',58,17),(57,'gagné',58,25),(58,'gagné',58,21),(59,'perdu',61,18),(60,'gagné',63,21),(61,'gagné',62,17),(62,'gagné',62,18),(63,'gagné',64,17),(64,'perdu',65,18),(65,'gagné',67,18),(66,'perdu',68,17),(67,'perdu',69,17),(68,'perdu',70,18),(69,'perdu',71,17),(70,'perdu',76,18),(71,'perdu',77,17),(72,'gagné',83,18),(73,'gagné',94,17),(74,'gagné',94,21),(75,'perdu',96,21),(76,'perdu',96,17),(77,'gagné',97,21),(78,'gagné',97,17),(79,'perdu',98,17),(80,'gagné',99,17),(81,'perdu',100,17),(82,'gagné',101,17),(83,'gagné',102,17),(84,'gagné',102,17),(85,'perdu',103,17),(86,'perdu',104,17),(87,'gagné',108,21),(88,'gagné',108,18);
/*!40000 ALTER TABLE `play` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `couleur_agent` enum('ROUGE','VERT','JAUNE','BLANC','DEFAULT','BLEU') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (17,'tanguy','Bv4FOnZK9qXA9VUncK70dZ2oXxZbU0ttT27V5Dzezjs=','BLEU'),(18,'julien','4jw9f/dvbmI1zgkfL81f01dIZ3eZ0WN6z1uivKNQ4lg=','VERT'),(21,'yoann','tAH3INJarNq5KFxVFzCS12UAPHGQ2jVAEQwIz7t8khc=','DEFAULT'),(25,'guillaume','J0yACSJGJdiTpIrXhIHREw3DGzLIUAszW/z3MWj2+lQ=','BLEU');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-03 22:15:06
