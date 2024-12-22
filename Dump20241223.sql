-- MySQL dump 10.13  Distrib 8.0.40, for macos14 (arm64)
--
-- Host: 127.0.0.1    Database: sys
-- ------------------------------------------------------
-- Server version	9.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `device_license`
--

DROP TABLE IF EXISTS `device_license`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_license` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `license_id` bigint NOT NULL,
  `device_id` bigint NOT NULL,
  `activation_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_license` (`license_id`),
  KEY `fk_device` (`device_id`),
  CONSTRAINT `fk_device` FOREIGN KEY (`device_id`) REFERENCES `devices` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_license` FOREIGN KEY (`license_id`) REFERENCES `licenses` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_license`
--

LOCK TABLES `device_license` WRITE;
/*!40000 ALTER TABLE `device_license` DISABLE KEYS */;
INSERT INTO `device_license` VALUES (2,4,1,'2024-11-29 20:05:01.350000'),(3,4,2,'2024-11-29 20:08:37.420000'),(4,6,4,'2024-11-30 14:21:20.351000'),(5,6,7,'2024-11-30 16:14:56.196000'),(6,7,8,'2024-12-05 01:04:23.763000'),(7,17,15,NULL),(8,8,10,'2024-12-12 20:10:23.905000'),(9,12,11,'2024-12-19 23:08:36.805000'),(10,13,12,'2024-12-21 01:40:11.946000'),(11,14,13,'2024-12-22 21:48:19.533000'),(12,15,14,'2024-12-22 22:38:42.243000'),(13,17,15,NULL),(14,18,16,'2024-12-23 01:30:06.664000');
/*!40000 ALTER TABLE `device_license` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `devices`
--

DROP TABLE IF EXISTS `devices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `devices` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `mac_address` varchar(255) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `devices_ibfk_1` (`user_id`),
  CONSTRAINT `devices_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `devices`
--

LOCK TABLES `devices` WRITE;
/*!40000 ALTER TABLE `devices` DISABLE KEYS */;
INSERT INTO `devices` VALUES (1,'MacBook','00:1A:2B:3C:4D:5E',2),(2,'Asus zenbook','00:1A:2B:3C:4D:5A',2),(3,'Lenovo','00:1A:2H:3S:4D:5E',3),(4,'Lenovo','00:1A:2B:3C:4D:5E',3),(5,'Samsung ','00:1A:2B:3C:4D:5K',3),(6,'Samsung','00:1A:2B:3C:4D:5A',3),(7,'Samsung','00:1A:2A:3C:4D:5A',3),(8,'Samsung555','00:1B:2B:3B:4D:5A',4),(9,'Samsung228','01:1A:2A:3C:4C:5A',5),(10,'Samsung2289','02:1A:2A:3C:4C:5A',5),(11,'Iphone1','02:1B:2B:3C:4C:5A',7),(12,'Iphone14','02:1B:2B:3D:4D:5A',7),(13,'Iphone15','02:1B:2B:3B:4D:5A',7),(14,'Iphone15','02:1С:2B:3С:4D:5A',7),(15,'Iphone10','02:1С:2B:3С:4D:5D',NULL),(16,'Iphone11','02:1A:2B:3С:4D:5A',7);
/*!40000 ALTER TABLE `devices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `license_history`
--

DROP TABLE IF EXISTS `license_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `license_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `license_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `change_date` datetime(6) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `license_id` (`license_id`),
  KEY `license_history_ibfk_2` (`user_id`),
  CONSTRAINT `license_history_ibfk_1` FOREIGN KEY (`license_id`) REFERENCES `licenses` (`id`),
  CONSTRAINT `license_history_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `license_history`
--

LOCK TABLES `license_history` WRITE;
/*!40000 ALTER TABLE `license_history` DISABLE KEYS */;
INSERT INTO `license_history` VALUES (4,4,2,'Создана','2024-11-28 00:00:00.000000','Лицензия создана'),(5,5,2,'Создана','2024-11-28 00:00:00.000000','Лицензия создана'),(6,4,2,'Активирована','2024-11-29 20:08:37.466000','Лицензия активирована на устройстве'),(7,6,3,'Создана','2024-11-30 00:00:00.000000','Лицензия создана'),(8,6,3,'Активирована','2024-11-30 14:21:20.374000','Лицензия активирована на устройстве'),(9,6,3,'Активирована','2024-11-30 16:14:56.231000','Лицензия активирована на устройстве'),(10,7,1,'Создана','2024-12-05 00:00:00.000000','Лицензия создана'),(11,7,4,'Активирована','2024-12-05 01:04:23.776000','Лицензия активирована на устройстве'),(12,8,4,'Создана','2024-12-12 00:00:00.000000','Лицензия создана'),(13,8,5,'Активирована','2024-12-12 20:08:30.563000','Лицензия активирована на устройстве'),(14,8,5,'Активирована','2024-12-12 20:10:23.922000','Лицензия активирована на устройстве'),(15,9,5,'Создана','2024-12-13 00:00:00.000000','Лицензия создана'),(16,10,6,'Создана','2024-12-13 00:00:00.000000','Лицензия создана'),(17,11,8,'Создана','2024-12-13 00:00:00.000000','Лицензия создана'),(18,12,7,'Создана','2024-12-19 00:00:00.000000','Лицензия создана'),(19,12,7,'Активирована','2024-12-19 23:08:36.813000','Лицензия активирована на устройстве'),(20,13,7,'Создана','2024-12-21 00:00:00.000000','Лицензия создана'),(21,13,7,'Активирована','2024-12-21 01:40:11.966000','Лицензия активирована на устройстве'),(22,14,7,'Создана','2024-12-22 00:00:00.000000','Лицензия создана'),(23,14,7,'Активирована','2024-12-22 21:48:19.550000','Лицензия активирована на устройстве'),(24,15,7,'Создана','2024-12-22 00:00:00.000000','Лицензия создана'),(25,15,7,'Активирована','2024-12-22 22:38:42.266000','Лицензия активирована на устройстве'),(26,16,7,'Создана','2024-12-22 00:00:00.000000','Лицензия успешно создана'),(27,17,7,'Создана','2024-12-23 00:00:00.000000','Лицензия успешно создана'),(28,18,7,'Создана','2024-12-23 00:00:00.000000','Лицензия успешно создана'),(29,18,7,'Активирована','2024-12-23 01:30:06.676000','Лицензия активирована на устройстве.');
/*!40000 ALTER TABLE `license_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `license_types`
--

DROP TABLE IF EXISTS `license_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `license_types` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `default_duration` int NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `license_types`
--

LOCK TABLES `license_types` WRITE;
/*!40000 ALTER TABLE `license_types` DISABLE KEYS */;
INSERT INTO `license_types` VALUES (1,'Условно-бесплатная',30,'Подписка на 30 дней');
/*!40000 ALTER TABLE `license_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `licenses`
--

DROP TABLE IF EXISTS `licenses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `licenses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `owner_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `type_id` bigint NOT NULL,
  `first_activation_date` datetime(6) NOT NULL,
  `ending_date` datetime(6) NOT NULL,
  `blocked` tinyint(1) DEFAULT NULL,
  `device_count` int DEFAULT NULL,
  `duration` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `owner_id` (`owner_id`),
  KEY `product_id` (`product_id`),
  KEY `type_id` (`type_id`),
  KEY `licenses_ibfk_1` (`user_id`),
  CONSTRAINT `licenses_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `licenses_ibfk_2` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`),
  CONSTRAINT `licenses_ibfk_3` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `licenses_ibfk_4` FOREIGN KEY (`type_id`) REFERENCES `license_types` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `licenses`
--

LOCK TABLES `licenses` WRITE;
/*!40000 ALTER TABLE `licenses` DISABLE KEYS */;
INSERT INTO `licenses` VALUES (4,'F748CB16508345828A7253F5A22D0C16',1,1,1,1,'2024-11-28 00:00:00.000000','2026-12-12 23:59:59.000000',0,0,729,'Приятного пользования нашими продуктами!'),(5,'167CBB6FED064683BD2115C0EA9CB797',2,2,1,1,'2024-11-28 00:00:00.000000','2026-12-12 23:59:59.000000',0,1,729,'Приятного пользования нашими продуктами!'),(6,'E0445845D222452FA1ECA3224DB51EE1',3,3,1,1,'2024-11-30 00:00:00.000000','2026-12-13 23:59:59.000000',0,3,730,'Приятного пользования нашими продуктами!'),(7,'D8AF2361B96743CEB22FAEE51C469856',4,1,1,1,'2024-12-05 00:00:00.000000','2026-12-12 23:59:59.000000',0,2,729,'Приятного пользования нашими продуктами!'),(8,'407D2475CF3D479B9A08E74916104388',5,4,1,1,'2024-12-12 00:00:00.000000','2026-12-12 23:59:59.000000',0,1,729,'Приятного пользования нашими продуктами!'),(9,'9E5456BBF20E4DDC8FF848BE69886697',6,5,1,1,'2024-12-13 00:00:00.000000','2027-12-12 23:59:59.000000',0,2,1094,'Приятного пользования нашими продуктами!'),(10,'277EB210CE854186950C40AA4FBF729D',7,7,1,1,'2024-12-13 00:00:00.000000','2030-02-12 23:59:59.000000',0,3,1881,'Приятного пользования нашими продуктами!'),(11,'4D2FCFF64074434285DE5A517F0B69E7',8,8,1,1,'2024-12-13 00:00:00.000000','2025-01-12 00:00:00.000000',0,2,30,'Приятного пользования нашими продуктами!'),(12,'426563A3A19B4B4A94C65F7C6E9540D2',7,7,1,1,'2024-12-19 00:00:00.000000','2025-01-18 00:00:00.000000',0,2,30,'Приятного пользования нашими продуктами!'),(13,'28E660EFEB7140D8B84682D41F046CDB',7,7,1,1,'2024-12-21 00:00:00.000000','2025-01-20 00:00:00.000000',0,3,30,'Приятного пользования нашими продуктами!'),(14,'D5D3C4CF769047A8B09CEF2C2194D591',7,7,1,1,'2024-12-22 00:00:00.000000','2025-01-21 00:00:00.000000',0,4,30,'Приятного пользования нашими продуктами!'),(15,'AE75869CCDBA4859814EED5F51B0DAA1',7,7,1,1,'2024-12-22 00:00:00.000000','2025-01-21 00:00:00.000000',0,1,30,'Приятного пользования нашими продуктами!'),(16,'9428AE87AFC849F690C628F1DFEA6D73',7,7,1,1,'2024-12-22 00:00:00.000000','2025-01-21 00:00:00.000000',0,2,30,'Приятного пользования!'),(17,'B1D6EFDC1F624DEDBEABF278F763EA6F',7,7,1,1,'2024-12-23 00:00:00.000000','2030-02-12 23:59:59.000000',0,2,1877,'Приятного пользования!'),(18,'CC6303F7EEAD470C8771B840D19B73E0',7,7,1,1,'2024-12-23 00:00:00.000000','2025-01-22 00:00:00.000000',0,0,30,'Приятного пользования!');
/*!40000 ALTER TABLE `licenses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_blocked` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,_binary '\0','Scanner');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tickets`
--

DROP TABLE IF EXISTS `tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tickets` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `server_date` datetime NOT NULL,
  `ticket_lifetime` int DEFAULT NULL,
  `activation_date` datetime(6) DEFAULT NULL,
  `expiration_date` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `device_id` bigint DEFAULT NULL,
  `is_blocked` tinyint(1) NOT NULL,
  `digital_signature` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tickets`
--

LOCK TABLES `tickets` WRITE;
/*!40000 ALTER TABLE `tickets` DISABLE KEYS */;
INSERT INTO `tickets` VALUES (10,'2024-11-29 19:09:07',5,'2024-11-29 19:09:06.544000',NULL,NULL,NULL,0,'05fae831-d045-4e43-81b9-78a16529dc1a'),(11,'2024-11-29 19:19:00',74,NULL,'2025-02-11 00:43:17.000000',2,NULL,0,'2917143f-fb7d-4218-9e25-fdef2a0d8269'),(12,'2024-11-29 19:51:58',5,'2024-11-29 19:51:58.106000',NULL,NULL,NULL,1,'f51948d2-fbfc-4094-a5d1-425fb413e5e6'),(13,'2024-11-29 19:53:33',5,'2024-11-29 19:53:32.532000',NULL,NULL,NULL,1,'b81d33dd-16b6-4aba-bda7-50332253e84d'),(14,'2024-11-29 20:05:02',5,'2024-11-29 20:05:01.516000',NULL,NULL,NULL,1,'5bccdd51-d064-47a8-82b4-3aed558adb2c'),(15,'2024-11-29 20:08:37',5,'2024-11-29 20:08:37.477000','2024-12-31 23:59:59.000000',NULL,NULL,0,'4b620a2d-e8f4-4298-986e-3efe4aa8f4e6'),(16,'2024-11-29 20:13:05',5,'2024-11-29 20:13:04.801000',NULL,NULL,NULL,0,'2e2e0239-0c76-4046-8e49-98e69fbb2afd'),(17,'2024-11-29 20:34:31',5,'2024-11-29 20:34:31.381000',NULL,NULL,NULL,0,'e8f3b701-037a-4959-9367-2e24531b5347'),(18,'2024-11-29 20:36:39',5,'2024-11-29 20:36:39.427000',NULL,NULL,NULL,0,'92467fbb-cdd4-44d1-8a7b-634590bd9974'),(19,'2024-11-29 20:39:54',5,'2024-11-29 20:39:54.211000',NULL,NULL,NULL,0,'ea39f885-9453-4e32-9092-a2f486de3859'),(20,'2024-11-29 20:45:16',5,'2024-11-29 20:45:15.637000',NULL,NULL,NULL,0,'12662a42-7760-4a97-8aa0-1661b96c70d0'),(21,'2024-11-29 20:48:47',5,'2024-11-29 20:48:46.678000','2025-06-11 00:00:00.000000',NULL,NULL,0,'551fe0c0-296f-4add-ba6c-7d85331e760c'),(22,'2024-11-30 14:13:29',5,'2024-11-30 14:13:29.038000',NULL,NULL,NULL,1,'bd35f8ce-0db3-4a01-9038-3256a2a3c58d'),(23,'2024-11-30 14:16:18',5,'2024-11-30 14:16:17.691000',NULL,NULL,NULL,1,'2f02b803-9609-4f0c-97fc-e55cde71c8fa'),(24,'2024-11-30 14:19:28',5,'2024-11-30 14:19:28.090000',NULL,NULL,NULL,1,'a514ca3b-7c41-4159-bcd5-4c82144d35d6'),(25,'2024-11-30 14:20:27',5,'2024-11-30 14:20:27.137000',NULL,NULL,NULL,1,'fafc2e9c-cf64-4268-bdde-9e1aa11d9239'),(26,'2024-11-30 14:20:50',5,'2024-11-30 14:20:49.644000',NULL,NULL,NULL,1,'4d2160f6-50d8-4295-8d22-13133968c5bb'),(27,'2024-11-30 14:21:20',5,'2024-11-30 14:21:20.380000','2024-12-30 00:00:00.000000',NULL,NULL,0,'250051cc-1e46-400e-ac26-5ebec006935a'),(28,'2024-11-30 16:14:56',5,'2024-11-30 16:14:56.238000','2024-12-30 00:00:00.000000',NULL,NULL,0,'8290d255-7eda-436d-80f6-7a9f6de0bd82');
/*!40000 ALTER TABLE `tickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','admin@mail.ru','$2a$12$vKtvXiZlTIJLlRK/KqWan.SXN3u3eMJGaBATHEVGoautYhtCLsora','ADMIN'),(2,'user','user@gmail.com','$2a$12$v5YCIHEhYpMAI3qzzNV1J.PsUlB/heJlIu.Z1EqMSzLeIbT50n./S','USER'),(3,'user5','user5@gmail.com','$2a$12$Zm6jGiMqqPAVb2qydPfWeOEqSt4KOt1u2MKYF0UKniqJl1esnbZt.','USER'),(4,'user6','user6@gmail.com','$2a$12$v5YCIHEhYpMAI3qzzNV1J.PsUlB/heJlIu.Z1EqMSzLeIbT50n./S','USER'),(5,'user10','user10@gmail.com','$2a$12$IikzArV8H6l1yCmNfLHDGOQu3yO1IPkTlT7FXqdZ8PLGeVG54Lexu','USER'),(6,'user2','user2@gmail.ru','$2a$12$68tlUoW/6zfWk5tYtBVpl.lkpOPOz.s5GgCRbYE9CvNLd.cPzxmq2','USER'),(7,'admin1','admin1@mail.ru','$2a$12$GtNwob1K.ElKvlQqtCwsxea4883.2kSosVaWgCUX7LlddipU.xFUC','ADMIN'),(8,'user3','user3@gmail.ru','$2a$12$ivT.7cZORgmjNdvaIEaGl.R8M5gQ7MhhxTwgfqNnXhav8lSZOgqHK','USER');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-23  2:06:38
