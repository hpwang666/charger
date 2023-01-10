use `test`;



SET NAMES utf8;

CREATE TABLE `sys_log` (
  `id` int  NOT NULL AUTO_INCREMENT,
  `log_type` int DEFAULT NULL COMMENT '日志类型（1登录日志，2操作日志,3设备日志 ）',
  `log_content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日志内容',
  `operate_type` int DEFAULT NULL COMMENT '操作类型',
  `userid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作用户账号',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作用户名称',
  `ip` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'IP',
  `method` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '请求java方法',
  `request_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '请求路径',
  `request_param` longtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '请求参数',
  `request_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '请求类型',
  `cost_time` bigint DEFAULT NULL COMMENT '耗时',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_table_userid` (`userid`) USING BTREE,
  KEY `index_logt_ype` (`log_type`) USING BTREE,
  KEY `index_operate_type` (`operate_type`) USING BTREE,
  KEY `index_log_type` (`log_type`) USING BTREE,
  KEY `idx_sl_userid` (`userid`) USING BTREE,
  KEY `idx_sl_log_type` (`log_type`) USING BTREE,
  KEY `idx_sl_operate_type` (`operate_type`) USING BTREE,
  KEY `idx_sl_create_time` (`create_time`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统日志表';

CREATE TABLE `sys_dict` (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dict_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典名称',
  `dict_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典编码',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述',
  `del_flag` int DEFAULT NULL COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

CREATE TABLE `sys_depart` (
    `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  COMMENT '父部门id',
    `parent_prj_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  COMMENT '父项目id',
    `depart_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门名称',
    `depart_name_py` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '部门名称拼音',
    `depart_name_abbr` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '机构简称',
    `pay_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '支付机构编码',
    `pay_channel` tinyint(1)   DEFAULT NULL COMMENT '支付类型',
    `org_category` tinyint(1)  DEFAULT NULL COMMENT '机构类别 1城市，2集团，3公司，4车场',
    `org_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '机构编码',
    `memo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
    `state` tinyint(1)  NOT NULL COMMENT '状态 1启用，0不启用',
    `del_flag` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '删除状态 0正常，1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

CREATE TABLE `dev_energy` (
  `id` int  NOT NULL AUTO_INCREMENT,
  `serial_num` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设备序列号',
  `parent_id`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '父级设备id',
  `ver` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '设备软件版本',
  `heart_beat_time` datetime DEFAULT NULL COMMENT '心跳时间',
  `online_status` int DEFAULT NULL COMMENT '在线状态（0--在线  1---离线）',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_serial_num` (`serial_num`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='能源设备';


CREATE TABLE `energy` (
  `id` int  NOT NULL AUTO_INCREMENT,
  `serial_num` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设备序列号',
  `update_time` datetime DEFAULT NULL COMMENT '数据采集时间',
  `update_kwh` decimal DEFAULT NULL COMMENT '此次更新的电表读数',
  `total_kwh` decimal DEFAULT NULL COMMENT '总的电表读数',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_serial_num` (`serial_num`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='能源数据';

CREATE TABLE `sys_user_depart` (
    `id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `user_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `depart_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
     PRIMARY KEY (`id`) USING BTREE,
     KEY `index_id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



