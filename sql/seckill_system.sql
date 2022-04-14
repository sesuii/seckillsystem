/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.43.163_3306
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : 192.168.43.163:3306
 Source Schema         : seckill_system

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 14/04/2022 11:26:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for access_rule
-- ----------------------------
DROP TABLE IF EXISTS `access_rule`;
CREATE TABLE `access_rule`  (
  `id` bigint NOT NULL COMMENT ' 主键',
  `age_limit` int NOT NULL DEFAULT -1 COMMENT ' 年龄限制 默认为-1无限制',
  `workstatus_limit` int NOT NULL DEFAULT -1 COMMENT '工作状态限制 默认-1无限制 1为工作稳定',
  `over_count_limit` int NOT NULL DEFAULT -1 COMMENT ' 逾期次数限制默认-1无限制',
  `over_total_amount` decimal(10, 2) NOT NULL DEFAULT -1.00 COMMENT '逾期总金额限制 默认-1无限制',
  `over_day` int NOT NULL DEFAULT -1 COMMENT '逾期天数限制',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT ' 创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '准入规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of access_rule
-- ----------------------------
INSERT INTO `access_rule` VALUES (0, 18, 1, 1, 1.00, 1, '2022-04-13 18:01:31');

-- ----------------------------
-- Table structure for bank_account
-- ----------------------------
DROP TABLE IF EXISTS `bank_account`;
CREATE TABLE `bank_account`  (
  `id` bigint NOT NULL COMMENT 'id',
  `account_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '银行账户名',
  `account_balance` decimal(10, 2) NULL DEFAULT NULL COMMENT '账户余额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '银行账户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bank_account
-- ----------------------------
INSERT INTO `bank_account` VALUES (1, '三湘银行', 10000300.00);

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `goods_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `goods_img` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品图片',
  `goods_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '商品价格',
  `goods_stock` int NOT NULL DEFAULT 0 COMMENT '库存',
  `detail_message` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品详细介绍',
  `create_time` datetime NULL DEFAULT NULL COMMENT ' 创建时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '银行产品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (1, '香蕉', 'photo', 100.00, 121, '一种水果', '2022-03-15 10:23:27', 0);

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL COMMENT ' 用户ID',
  `goods_id` bigint NULL DEFAULT NULL COMMENT '商品ID',
  `goods_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT ' 商品名称',
  `goods_count` int NOT NULL DEFAULT 0 COMMENT '商品数量',
  `goods_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '商品价格',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '订单状态 0新建 1已完成 2取消支付',
  `create_time` datetime NULL DEFAULT NULL COMMENT ' 创建时间',
  `pay_time` datetime NULL DEFAULT NULL COMMENT ' 支付时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `order_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单详情表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_info
-- ----------------------------

-- ----------------------------
-- Table structure for sk_goods
-- ----------------------------
DROP TABLE IF EXISTS `sk_goods`;
CREATE TABLE `sk_goods`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `goods_id` bigint NULL DEFAULT NULL COMMENT '商品ID',
  `sk_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '秒杀价格',
  `stock` int NULL DEFAULT NULL COMMENT '秒杀数量',
  `start_date_time` datetime NULL DEFAULT NULL COMMENT '秒杀开始时间',
  `end_date_time` datetime NULL DEFAULT NULL COMMENT ' 秒杀结束时间',
  `limited_rule_id` bigint NULL DEFAULT NULL COMMENT ' 准入规则id',
  `subscribe` tinyint(3) UNSIGNED ZEROFILL NOT NULL COMMENT '是否需要预约 默认为0',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `goods_id`(`goods_id` ASC) USING BTREE,
  INDEX `limited_rule_id`(`limited_rule_id` ASC) USING BTREE,
  CONSTRAINT `sk_goods_ibfk_1` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sk_goods_ibfk_2` FOREIGN KEY (`limited_rule_id`) REFERENCES `access_rule` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '秒杀产品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sk_goods
-- ----------------------------
INSERT INTO `sk_goods` VALUES (3, 1, 10.00, 1, '2022-04-13 00:14:48', '2022-04-15 00:14:54', 0, 001);

-- ----------------------------
-- Table structure for sk_order
-- ----------------------------
DROP TABLE IF EXISTS `sk_order`;
CREATE TABLE `sk_order`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `goods_id` bigint NOT NULL COMMENT '商品ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_userId_goodsId`(`user_id` ASC, `goods_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '秒杀订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sk_order
-- ----------------------------
INSERT INTO `sk_order` VALUES (16, 26, 16, 1);
INSERT INTO `sk_order` VALUES (17, 27, 2, 1);

-- ----------------------------
-- Table structure for sk_user
-- ----------------------------
DROP TABLE IF EXISTS `sk_user`;
CREATE TABLE `sk_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT ' 用户ID',
  `mobile_phone` char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ' 用户手机号',
  `pwd` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户密码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '秒杀用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sk_user
-- ----------------------------

-- ----------------------------
-- Table structure for trade_record
-- ----------------------------
DROP TABLE IF EXISTS `trade_record`;
CREATE TABLE `trade_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT ' 交易id',
  `order_id` bigint NOT NULL COMMENT ' 交易订单号',
  `remitter_id` bigint NOT NULL COMMENT '汇款方id',
  `payee_id` bigint NOT NULL COMMENT '收款方id',
  `trading_time` datetime NULL DEFAULT NULL COMMENT '交易时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_id`(`order_id` ASC) USING BTREE,
  CONSTRAINT `trade_record_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '交易记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of trade_record
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `realname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT ' 真实姓名',
  `pwd` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ' 密码',
  `mobile_phone` char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `identity_id` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT ' 最后一次修改时间',
  `last_login_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后登录ip',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_identity`(`identity_id` ASC, `mobile_phone` ASC) USING BTREE COMMENT '身份证号码和手机号唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2, 'song', '$2a$10$uLq3Cvg7Y/2BjPmT6GrLk.jCywFkaCRETuUzfcMBScXUKiPVU.lRS', '1801234', '1234567812', '2022-03-25 00:52:35', '2022-04-06 13:24:42', '0.0.0.0', 0);
INSERT INTO `user` VALUES (15, '徐佳佳', '$2a$10$UJlTgmkz2gmRmc/zmXuLHevn6RbwCf1ZAF9Gi3/bJ9jhxj9k/Su5a', '17870007136', '362502200206216828', '2022-03-26 22:50:50', '2022-03-26 22:50:50', '0.0.0.0', 0);
INSERT INTO `user` VALUES (16, 'song', '$2a$10$PiOdHRMv0L2kKf7gg/Bb.u9oAhvC.U6LiE8La/uku.t8AKal9WQ9i', '1230001', '378847823974812', '2022-04-06 13:00:03', '2022-04-06 13:00:03', '0.0.0.0', 0);
INSERT INTO `user` VALUES (17, 'roche', '$2a$10$4OGujF47bdwTgWkNJtmhb.MN6POMLPrGtdZDZRTPd7FwOZnH8G0lq', '133123456', '374832789472893', '2022-04-06 13:45:23', '2022-04-06 13:45:23', '0.0.0.0', 0);

-- ----------------------------
-- Table structure for user_financial
-- ----------------------------
DROP TABLE IF EXISTS `user_financial`;
CREATE TABLE `user_financial`  (
  `id` bigint NOT NULL COMMENT '用户id',
  `card_number` bigint NULL DEFAULT NULL COMMENT ' 银行卡号',
  `balance` decimal(10, 2) UNSIGNED ZEROFILL NOT NULL COMMENT '账户余额',
  `integrity_degree` int NULL DEFAULT NULL COMMENT '诚信指数 正常为0 逾期为负数 良好为正数',
  `work_status` int NULL DEFAULT NULL COMMENT '工作状态 待业为-1 未知为0 稳定为1',
  `over_count` int NOT NULL COMMENT ' 逾期次数',
  `over_amount` decimal(10, 2) UNSIGNED ZEROFILL NOT NULL COMMENT '逾期金额',
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `user_financial_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户资产表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_financial
-- ----------------------------
INSERT INTO `user_financial` VALUES (2, 435344654, 00022832.00, 1, 1, 0, 00000000.00);
INSERT INTO `user_financial` VALUES (15, 454654654654, 00000432.00, 0, 1, 1, 00000000.00);
INSERT INTO `user_financial` VALUES (16, 4356547668, 00000987.00, 2, 1, 1, 00000000.00);
INSERT INTO `user_financial` VALUES (17, 46537754, 00000021.00, 1, 1, 1, 00000000.00);

-- ----------------------------
-- Triggers structure for table goods
-- ----------------------------
DROP TRIGGER IF EXISTS `goods_insert_trig`;
delimiter ;;
CREATE TRIGGER `goods_insert_trig` BEFORE INSERT ON `goods` FOR EACH ROW BEGIN
SET NEW.create_time = NOW(); 
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table order_info
-- ----------------------------
DROP TRIGGER IF EXISTS `order_insert_trig`;
delimiter ;;
CREATE TRIGGER `order_insert_trig` BEFORE INSERT ON `order_info` FOR EACH ROW BEGIN
SET NEW.create_time = NOW();
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table user
-- ----------------------------
DROP TRIGGER IF EXISTS `user_insert_trig`;
delimiter ;;
CREATE TRIGGER `user_insert_trig` BEFORE INSERT ON `user` FOR EACH ROW BEGIN
SET NEW.create_time = NOW();
SET NEW.last_update_time = NOW();
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
