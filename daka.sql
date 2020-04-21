-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.16 - MySQL Community Server - GPL
-- 服务器OS:                        Win64
-- HeidiSQL 版本:                  10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for go_to_hell
CREATE DATABASE IF NOT EXISTS `go_to_hell` /*!40100 DEFAULT CHARACTER SET utf8useruser */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `go_to_hell`;

-- Dumping structure for table go_to_hell.user
CREATE TABLE IF NOT EXISTS `user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `num` varchar(8) NOT NULL COMMENT '学号',
  `pwd` varchar(64) NOT NULL COMMENT '加密后的密码',
  `last_collector_wid` varchar(20) DEFAULT '3784' COMMENT '最新的一次wid',
  `teacher` varchar(10) NOT NULL COMMENT '辅导员姓名',
  `location` varchar(100) NOT NULL COMMENT '位置信息',
  `is_at_tian_jin` tinyint(1) NOT NULL,
  `email` varchar(30) NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `user_num_uindex` (`num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table go_to_hell.user: ~0 rows (大约)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
