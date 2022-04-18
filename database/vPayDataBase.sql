-- Host: localhost    Database: vpay
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts`
(
    `id`            int                   NOT NULL AUTO_INCREMENT,
    `IBAN`          text COLLATE utf8_bin,
    `IPN`           text COLLATE utf8_bin NOT NULL,
    `bankCode`      text COLLATE utf8_bin,
    `name`          text COLLATE utf8_bin,
    `currency`      text COLLATE utf8_bin,
    `balance`       double DEFAULT NULL,
    `ownerUser`     int                   NOT NULL,
    `statusAccount` text COLLATE utf8_bin,
    `card_id`       int    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts`
    DISABLE KEYS */;
INSERT INTO `accounts`
VALUES (1, 'beVnxdzORcdtZRoYrN10VSW2N', '', '', 'Premium account', 'UAH', 11129.15, 1, 'Enabled', -1),
       (3, 'ClyotxFqwUWF4T5aAWcoEOEXP', '', '', 'IVANOV Premium', 'USD', 1338.95, 2, 'Enabled', 1),
       (5, 'tkZZCxtB4K69bohOz6gtFEsen', '', '', 'IVANOV cash', 'EUR', 913.91, 2, 'Enabled', -1),
       (6, 'UxqV2ZyXC0KfU60E9EQOO8tOK', '', '', 'Ivanov GOLD', 'UAH', 969.7, 2, 'Idle', 8),
       (7, 'ouzgJ73o1e6UYd4WpZB2wtEMj', '', '', 'Main Tymchuk', 'UAH', 11354.97, 41, 'Enabled', 11);
/*!40000 ALTER TABLE `accounts`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cards`
--

DROP TABLE IF EXISTS `cards`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cards`
(
    `id`         int                   NOT NULL AUTO_INCREMENT,
    `name`       text COLLATE utf8_bin,
    `number`     text COLLATE utf8_bin NOT NULL,
    `expDate`    text COLLATE utf8_bin NOT NULL,
    `user_id`    int                   NOT NULL,
    `statusCard` tinyint(1)            NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 12
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cards`
--

LOCK TABLES `cards` WRITE;
/*!40000 ALTER TABLE `cards`
    DISABLE KEYS */;
INSERT INTO `cards`
VALUES (1, 'VISA Gold', '4149 1234 5678 9876', '12/24', 2, 1),
       (2, 'VISA Platinum', '4149 1234 0000 9876', '12/26', 2, 0),
       (3, 'MasterCard Black', '5417 4321 0000 9876', '12/26', 3, 1),
       (4, 'MasterCard Shoping', '5417 0000 0000 9876', '12/23', 3, 1),
       (8, 'Visa Pay card', '4321 4321 4321 4321', '01/24', 2, 1),
       (9, 'MasterCard Ivanov', '4321 7777 8888 1234', '07/22', 2, 1),
       (10, 'Visa SUPER PUPER', '7356 4283 7491 7312', '08/27', 2, 0),
       (11, 'Visa Fishka card', '4321 6666 2938 4732', '07/25', 41, 0);
/*!40000 ALTER TABLE `cards`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments`
(
    `id`            int                   NOT NULL AUTO_INCREMENT,
    `guid`          text COLLATE utf8_bin NOT NULL,
    `ownerUser`     int                   NOT NULL,
    `account_id`    int                   NOT NULL,
    `recipientType` text COLLATE utf8_bin NOT NULL,
    `recipientId`   text COLLATE utf8_bin,
    `datetimeOfLog` text COLLATE utf8_bin NOT NULL,
    `currency`      text COLLATE utf8_bin,
    `commission`    double DEFAULT NULL,
    `total`         double DEFAULT NULL,
    `statusPayment` text COLLATE utf8_bin,
    `sum`           double DEFAULT NULL,
    `currencysum`   text COLLATE utf8_bin,
    `archive`       int    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 51
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments`
    DISABLE KEYS */;
INSERT INTO `payments`
VALUES (31, '8350855510', 2, 3, 'Card', '2222 0000 1111 9999', '2022-04-01 23:20:27', 'USD', 5.75, 120.75, 'Ready', 100,
        'EUR', 1),
       (32, '7146891226', 2, 3, 'Card', '3333 0000 4444 0000', '2022-04-01 23:24:35', 'USD', 5.75, 120.75, 'Ready', 100,
        'EUR', 1),
       (33, '1306777553', 2, 3, 'Card', '1111 0000 2222 0000', '2022-04-01 23:41:48', 'USD', 5.75, 120.75, 'Submitted',
        100, 'EUR', 0),
       (34, '2853849938', 2, 6, 'Account', 'ClyotxFqwUWF4T5aAWcoEOEXP', '2022-04-03 09:25:39', 'UAH', 0, 1, 'Submitted',
        1, 'USD', 1),
       (35, '6674344866', 2, 6, 'Card', '5555 6666 7777 8888', '2022-04-03 09:27:22', 'UAH', 1.52, 31.82, 'Submitted',
        1, 'USD', 0),
       (36, '4659615456', 2, 6, 'Account', 'ClyotxFqwUWF4T5aAWcoEOEXP', '2022-04-03 09:28:06', 'UAH', 0, 1, 'Ready', 1,
        'USD', 1),
       (37, '2117455249', 2, 6, 'Account', 'ClyotxFqwUWF4T5aAWcoEOEXP', '2022-04-03 09:35:20', 'UAH', 0, 1, 'Ready', 1,
        'USD', 1),
       (38, '7576160901', 2, 6, 'Account', 'ClyotxFqwUWF4T5aAWcoEOEXP', '2022-04-03 09:54:04', 'UAH', 0, 30.3,
        'Submitted', 1, 'USD', 0),
       (39, '4665494009', 2, 6, 'Account', 'ClyotxFqwUWF4T5aAWcoEOEXP', '2022-04-03 10:05:46', 'UAH', 0, 30.3,
        'Submitted', 1, 'USD', 1),
       (40, '4871890525', 2, 6, 'Account', 'tkZZCxtB4K69bohOz6gtFEsen', '2022-04-03 10:34:25', 'UAH', 0, 30.3,
        'Submitted', 1, 'USD', 1),
       (41, '8735903585', 2, 6, 'Account', 'tkZZCxtB4K69bohOz6gtFEsen', '2022-04-03 10:36:43', 'UAH', 0, 30.3,
        'Submitted', 1, 'USD', 1),
       (42, '3583642860', 2, 6, 'Account', 'tkZZCxtB4K69bohOz6gtFEsen', '2022-04-03 10:39:34', 'UAH', 0, 30.3,
        'Submitted', 1, 'USD', 1),
       (43, '0421436499', 41, 7, 'Account', 'ClyotxFqwUWF4T5aAWcoEOEXP', '2022-04-03 15:09:20', 'UAH', 0, 303.03,
        'Submitted', 10, 'USD', 0),
       (44, '9492441133', 41, 7, 'Account', 'ouzgJ73o1e6UYd4WpZB2wtEMj', '2022-04-04 12:03:45', 'UAH', 0, 100,
        'Submitted', 100, 'UAH', 0),
       (45, '0480709327', 41, 7, 'Account', 'ouzgJ73o1e6UYd4WpZB2wtEMj', '2022-04-04 12:04:15', 'UAH', 0, 100,
        'Submitted', 100, 'UAH', 0),
       (46, '6113480457', 41, 7, 'Account', 'ouzgJ73o1e6UYd4WpZB2wtEMj', '2022-04-04 12:08:16', 'UAH', 0, 100,
        'Submitted', 100, 'UAH', 0),
       (47, '4661579612', 41, 7, 'Account', 'ouzgJ73o1e6UYd4WpZB2wtEMj', '2022-04-04 12:13:25', 'UAH', 0, 100,
        'Submitted', 100, 'UAH', 0),
       (48, '2568926304', 41, 7, 'Account', 'ouzgJ73o1e6UYd4WpZB2wtEMj', '2022-04-04 12:13:53', 'UAH', 0, 348.48,
        'Submitted', 10, 'EUR', 0),
       (49, '2824056501', 2, 3, 'Account', 'ouzgJ73o1e6UYd4WpZB2wtEMj', '2022-04-05 13:03:45', 'USD', 0, 3.3, 'Ready',
        100, 'UAH', 0),
       (50, '7903450707', 2, 5, 'Account', 'ClyotxFqwUWF4T5aAWcoEOEXP', '2022-04-06 13:14:56', 'EUR', 0, 86.96,
        'Submitted', 100, 'USD', 0);
/*!40000 ALTER TABLE `payments`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles`
(
    `id`         int                   NOT NULL AUTO_INCREMENT,
    `name`       text COLLATE utf8_bin NOT NULL,
    `statusRole` tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles`
    DISABLE KEYS */;
INSERT INTO `roles`
VALUES (1, 'admin', 1),
       (2, 'user', 1);
/*!40000 ALTER TABLE `roles`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users`
(
    `id`          int                   NOT NULL AUTO_INCREMENT,
    `login`       text COLLATE utf8_bin NOT NULL,
    `password`    text COLLATE utf8_bin NOT NULL,
    `statususer`  tinyint(1)            NOT NULL,
    `role`        int  DEFAULT NULL,
    `firstname`   text COLLATE utf8_bin,
    `lastname`    text COLLATE utf8_bin,
    `dateofbirth` date DEFAULT NULL,
    `sex`         text COLLATE utf8_bin,
    `gender`      text COLLATE utf8_bin,
    `photo`       text COLLATE utf8_bin,
    `email`       text COLLATE utf8_bin,
    `local`       text COLLATE utf8_bin,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 43
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users`
    DISABLE KEYS */;
INSERT INTO `users`
VALUES (1, 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 1, 0, 'Volodymyr', 'Tymchuk',
        '1989-05-18', 'Male', 'man', 'admin.jpg', NULL, 'en'),
       (2, 'ivanov', 'ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 0, 1, 'Ivan', 'Ivanov',
        '1989-07-20', 'Male', 'man', 'ivanov.jpg', NULL, 'uk'),
       (3, 'petrov', 'ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 1, 1, 'Petro', 'Petrov',
        '1990-12-31', 'Male', 'man', 'petrov.jpg', NULL, 'uk'),
       (5, 'emma', 'ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 1, 1, 'Ema', 'Nuel', '1992-06-10',
        'Female', 'women', 'emma.jpg', 'ema@ema.com', 'en'),
       (6, 'linda1989', 'ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 1, 0, 'Linda', 'Dallas',
        '1992-06-10', 'Female', 'women', 'linda1989.jpg', 'linda@mail.com', 'en'),
       (7, 'operator', '3ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 1, 0, 'Всеволод',
        'Український', '1990-05-05', 'Male', 'man', 'operator.jpg', NULL, 'en'),
       (8, 'operator25', '3ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 1, 0, 'Байрактар',
        'Український', '1990-05-05', 'Male', 'man', 'operator25.png', NULL, 'en'),
       (40, 'admin1234', '925427659676ada68e35f680a1f4c8da1cee5955d9bd014772d4b798c5bae13a', 0, 0, 'VV', 'V',
        '1990-11-11', 'Male', '', 'admin1234.jpeg', 'malfoy@i.ua', 'en'),
       (41, 'tymchuk', '7c343db16e2d231deed18b7bc2c3eca3cf27e099a3b39e2426da07e450180435', 1, 1, 'Володимир', 'Тимчук',
        '1989-05-18', 'Male', '', '_blank.png', 'malfoy@i.ua', 'en'),
       (42, 'tester', '5c92736af1e8df30e753309db74251280873333226e7abfed1b564b2acb2f715', 1, 1, 'Тестувальник', 'ПЗ',
        '2000-01-01', 'Male', '', '_blank.png', 'tester@i.ua', NULL);
/*!40000 ALTER TABLE `users`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2022-04-16  2:09:49
