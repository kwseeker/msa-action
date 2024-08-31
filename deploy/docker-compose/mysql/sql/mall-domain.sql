SET NAMES utf8mb4;
CREATE DATABASE IF NOT EXISTS `msa-action`;
USE `msa-action`;

# 电商领域 (Mall Domain, MD) -------------------------------------------------------------

# 活动表
DROP TABLE IF EXISTS md_activity;
CREATE TABLE md_activity (
    id INT NOT NULL AUTO_INCREMENT COMMENT '活动ID',
    name VARCHAR(32) NOT NULL DEFAULT '' COMMENT '活动名称',
    setting_id INT NOT NULL COMMENT '活动配置ID',
    start_time DATETIME NOT NULL COMMENT '活动开始时间',
    end_time DATETIME NOT NULL COMMENT '活动结束时间',
    creator varchar(64) NULL DEFAULT '' COMMENT '创建者',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater varchar(64) NULL DEFAULT '' COMMENT '更新者',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '活动表';

BEGIN;
INSERT INTO md_activity (id, name, setting_id, start_time, end_time, creator, updater)
VALUES
    (1, '测试活动A', 1, '2024-01-01 00:00:00', '2024-01-01 23:59:59', 'admin', 'admin');
COMMIT;

# 活动配置表，这里暂时不考虑复杂的活动配置，仅仅设置活动赠送的物品ID，库存
DROP TABLE IF EXISTS md_activity_setting;
CREATE TABLE md_activity_setting (
    id INT NOT NULL AUTO_INCREMENT COMMENT '活动配置ID',
    item_id INT NOT NULL COMMENT '物品ID',
    stock INT NOT NULL COMMENT '活动物品库存',
    used_count INT NOT NULL DEFAULT 0 COMMENT '已使用数量',
    creator varchar(64) NULL DEFAULT '' COMMENT '创建者',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater varchar(64) NULL DEFAULT '' COMMENT '更新者',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '活动配置表';

BEGIN;
INSERT INTO md_activity_setting (id, item_id, stock, creator, updater)
VALUES
    (1, 1, 100, 'admin', 'admin');
COMMIT;

# 物品表
DROP TABLE IF EXISTS md_item;
CREATE TABLE md_item (
    id INT NOT NULL AUTO_INCREMENT COMMENT '物品ID',
    name VARCHAR(32) NOT NULL DEFAULT '' COMMENT '物品名称',
    description VARCHAR(32) NOT NULL DEFAULT '' COMMENT '物品描述',
    goods_id INT NOT NULL DEFAULT 0 COMMENT '对应商品ID, 0非卖品',
    creator varchar(64) NULL DEFAULT '' COMMENT '创建者',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater varchar(64) NULL DEFAULT '' COMMENT '更新者',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '物品表';

BEGIN;
INSERT INTO md_item (id, name, description, goods_id, creator, updater)
VALUES
    (1, '测试物品A', '这是测试商品A的描述', 0, 'admin', 'admin');
COMMIT;

# 用户活动参与记录表
DROP TABLE IF EXISTS md_user_activity_record;
CREATE TABLE md_user_activity_record (
    id BIGINT AUTO_INCREMENT COMMENT '物品ID',
    user_id BIGINT NOT NULL COMMENT '参与用户ID',
    activity_id INT NOT NULL COMMENT '参与活动ID',
    count INT NOT NULL DEFAULT 0 COMMENT '参与次数',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX key_user_id (user_id) USING BTREE,
    INDEX key_activity_id (activity_id) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户活动参与记录表';

# 用户物品表
DROP TABLE IF EXISTS md_user_item;
CREATE TABLE md_user_item (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '物品ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    item_id BIGINT NOT NULL COMMENT '物品ID',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX key_user_id (user_id) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户物品表';