/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80036 (8.0.36)
 Source Host           : localhost:3306
 Source Schema         : bookstore_new

 Target Server Type    : MySQL
 Target Server Version : 80036 (8.0.36)
 File Encoding         : 65001

 Date: 08/06/2024 14:10:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_auth
-- ----------------------------
DROP TABLE IF EXISTS `user_auth`;
CREATE TABLE `user_auth`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `user_auth_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_auth
-- ----------------------------
INSERT INTO `user_auth` VALUES (1, 'genshinstart', 'genshin', 1);
INSERT INTO `user_auth` VALUES (2, 'honkaistart', 'honkai', 2);
INSERT INTO `user_auth` VALUES (3, 'zrp0064', 'zrp', 3);
INSERT INTO `user_auth` VALUES (4, 'admin', 'admin', 4);
INSERT INTO `user_auth` VALUES (5, 'kaituozhe', 'firefly', 8);

SET FOREIGN_KEY_CHECKS = 1;
