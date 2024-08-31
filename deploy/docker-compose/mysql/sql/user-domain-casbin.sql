SET NAMES utf8mb4;
CREATE DATABASE IF NOT EXISTS `msa-action`;
USE `msa-action`;

# 将用户username限定为唯一索引，使用用户username和角色code作为casbin_rule中角色继承定义，
# 而不是用userId、roleId，这样为了避免 casbin_rule 变的难以维护
CREATE TABLE `ud_casbin_rule` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `ptype` varchar(100) NOT NULL,
    `v0` varchar(100) DEFAULT NULL,
    `v1` varchar(100) DEFAULT NULL,
    `v2` varchar(100) DEFAULT NULL,
    `v3` varchar(100) DEFAULT NULL,
    `v4` varchar(100) DEFAULT NULL,
    `v5` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;

BEGIN;
INSERT INTO `ud_casbin_rule` (ptype,v0,v1,v2,v3,v4,v5)
VALUES
    # 用户->角色
    ('g','admin','super_admin',NULL,NULL,NULL,NULL),
    ('g','admin','common',NULL,NULL,NULL,NULL),
    ('g','admin','test',NULL,NULL,NULL,NULL),
    ('g','kwseeker','common',NULL,NULL,NULL,NULL),
    ('g','kwseeker','test',NULL,NULL,NULL,NULL),
    ('g','tester','test',NULL,NULL,NULL,NULL),
    # 角色->访问对象->动作（权限）
    ('p','super_admin','/user/*','(system:user:list|system:user:query|system:user:create)',NULL,NULL,NULL),
    ('p','common','/user/*','(system:user:list|system:user:query)',NULL,NULL,NULL);
COMMIT;