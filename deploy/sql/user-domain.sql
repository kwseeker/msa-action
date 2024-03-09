CREATE DATABASE IF NOT EXISTS `msa-action`;
USE `msa-action`;

# 用户服务域 (User Domain, UD) -------------------------------------------------------------
# 基于RBAC规范，最简单的授权模型（用户-角色-资源），实际业务可以按需拓展用户组（岗位、部门）、权限、操作等；

DROP TABLE IF EXISTS `ud_dept`;
CREATE TABLE `ud_dept`  (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门ID',
    `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '部门名称',
    `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父部门ID（0顶级部门）',
    `sort` int NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `leader_user_id` bigint NULL DEFAULT NULL COMMENT '负责人',
    `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
    `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
    `status` tinyint NOT NULL COMMENT '部门状态（0正常 1停用）',
    `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    -- `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '部门表';

BEGIN;
INSERT INTO `ud_dept` (`id`, `name`, `parent_id`, `sort`, `leader_user_id`, `phone`, `email`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
    (1, '研发部门', 0, 0, 11, '13711112222', 'kwseeker@gmail.com', 0, 'admin', '2024-01-01 12:00:00', 'admin', '2024-01-01 12:00:00', b'0');
COMMIT;

DROP TABLE IF EXISTS `ud_user`;
CREATE TABLE `ud_user`  (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户账号',
    `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '密码',
    `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户昵称',
    `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
    `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
    -- `post_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '岗位编号数组',
    `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '用户邮箱',
    `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '手机号码',
    `sex` tinyint NULL DEFAULT 0 COMMENT '用户性别（0未知 1男 2女）',
    `avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '头像地址',
    `status` tinyint NOT NULL DEFAULT 0 COMMENT '帐号状态（0正常 1停用）',
    `login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '最后登录IP',
    `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
    `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    -- `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`) USING BTREE,
    -- UNIQUE INDEX `idx_username`(`username` ASC, `update_time` ASC, `tenant_id` ASC) USING BTREE
    UNIQUE INDEX `idx_un_ut`(`username` ASC, `update_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表';

# id: 1-999 预留给测试用户
BEGIN;
INSERT INTO `ud_user` (`id`, `username`, `password`, `nickname`, `remark`, `dept_id`, `email`, `mobile`, `sex`, `avatar`, `status`, `login_ip`, `login_date`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
    (1, 'admin', 'admin', 'admin', '系统管理员', 0, 'admin@gmail.com', '13711111111', 1, '', 0, '127.0.0.1', '2024-01-01 12:00:00', 'admin', '2024-01-01 12:00:00', NULL, '2024-01-01 12:00:00', b'0'),
    (11, 'kwseeker', '123456', 'kwseeker', 'kwseeker', 1, 'kwseeker@gmail.com', '13711112222', 1, '', 0, '127.0.0.1', '2024-01-01 12:00:00', 'admin', '2024-01-01 12:00:00', NULL, '2024-01-01 12:00:00', b'0');
COMMIT;

DROP TABLE IF EXISTS `ud_role`;
CREATE TABLE `ud_role`  (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
    `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色权限字符串',
    `sort` int NOT NULL COMMENT '显示顺序',
    `data_scope` tinyint NOT NULL DEFAULT 1 COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    `data_scope_dept_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '数据范围(指定部门数组)',
    `status` tinyint NOT NULL COMMENT '角色状态（0正常 1停用）',
    `type` tinyint NOT NULL COMMENT '角色类型',
    `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
    `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    -- `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色信息表';

BEGIN;
INSERT INTO `ud_role` (`id`, `name`, `code`, `sort`, `data_scope`, `data_scope_dept_ids`, `status`, `type`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
    # 可访问全部数据
    (1, '超级管理员', 'super_admin', 1, 1, '', 0, 1, '超级管理员', 'admin', '2024-01-01 12:00:00', '', '2024-01-01 12:00:00', b'0'),
    # 只可访问本部门数据权限
    (2, '普通角色', 'common', 2, 3, '', 0, 1, '普通角色', 'admin', '2024-01-01 12:00:00', '', '2024-01-01 12:00:00', b'0'),
    # 可访问自定义数据权限（制定部门的数据）
    (11, '测试角色', 'test', 0, 2, '[1]', 0, 2, '测试角色', 'admin', '2024-01-01 12:00:00', '', '2024-01-01 12:00:00', b'0');
COMMIT;

DROP TABLE IF EXISTS `ud_user_role`;
CREATE TABLE `ud_user_role`  (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增编号',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    -- `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户和角色关联表';

# 通过此表可以给用户分配多个角色
BEGIN;
INSERT INTO `ud_user_role` (`id`, `user_id`, `role_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
    # admin -> super_admin
    (1, 1, 1, 'admin', '2024-01-01 12:00:00', '', '2024-01-01 12:00:00', b'0'),
    # kwseeker -> common
    (2, 11, 2, 'admin', '2024-01-01 12:00:00', '', '2024-01-01 12:00:00', b'0');
COMMIT;

DROP TABLE IF EXISTS `ud_role_menu`;
CREATE TABLE `ud_role_menu`  (
     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增编号',
     `role_id` bigint NOT NULL COMMENT '角色ID',
     `menu_id` bigint NOT NULL COMMENT '菜单ID',
     `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
     -- `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色和菜单关联表';

BEGIN;
INSERT INTO `ud_role_menu` (`id`, `role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
    # super_admin
    (1, 1, 1, 'admin', '2024-01-01 12:00:00', 'admin', '2024-01-01 12:00:00', b'0'),
    (2, 1, 100, 'admin', '2024-01-01 12:00:00', 'admin', '2024-01-01 12:00:00', b'0'),
    (3, 1, 1001, 'admin', '2024-01-01 12:00:00', 'admin', '2024-01-01 12:00:00', b'0'),
    (4, 1, 1002, 'admin', '2024-01-01 12:00:00', 'admin', '2024-01-01 12:00:00', b'0'),
    # common
    (5, 2, 1001, 'admin', '2024-01-01 12:00:00', 'admin', '2024-01-01 12:00:00', b'0');
COMMIT;

DROP TABLE IF EXISTS `ud_menu`;
CREATE TABLE `ud_menu`  (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
    `permission` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '权限标识',
    `type` tinyint NOT NULL COMMENT '菜单类型（）',
    `sort` int NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父菜单ID（分级展示，0顶级菜单）',
    `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '路由地址',
    `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '#' COMMENT '菜单图标',
    `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件路径',
    `component_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件名',
    `status` tinyint NOT NULL DEFAULT 0 COMMENT '菜单状态',
    `visible` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否可见',
    `keep_alive` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否缓存',
    `always_show` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否总是显示',
    `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2000 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单权限表';

# 1-99：顶级菜单， 100-999：次级菜单
BEGIN;
INSERT INTO `ud_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
    (1, '系统管理', '', 1, 10, 0, '/system', '#', NULL, NULL, 0, b'1', b'1', b'1', 'admin', '2024-01-01 12:00:00', 'admin', '2024-01-01 12:00:00', b'0'),
    (100, '用户管理', 'system:user:list', 2, 1, 1, 'user', '#', 'system/user/index', 'SystemUser', 0, b'1', b'1', b'1', 'admin', '2024-01-01 12:00:00', 'admin', '2024-01-01 12:00:00', b'0'),
    (1001, '用户查询', 'system:user:query', 3, 1, 100, '', '#', '', NULL, 0, b'1', b'1', b'1', 'admin', '2024-01-01 12:00:00', 'admin', '2024-01-01 12:00:00', b'0'),
    (1002, '用户新增', 'system:user:create', 3, 2, 100, '', '#', '', NULL, 0, b'1', b'1', b'1', 'admin', '2024-01-01 12:00:00', 'admin', '2024-01-01 12:00:00', b'0');
COMMIT;
