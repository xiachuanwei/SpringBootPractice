# create DATABASE IF NOT EXISTS test default charset utf8 COLLATE utf8_general_ci;
# SET FOREIGN_KEY_CHECKS=0;
# USE test;

-- 用户表
drop table IF EXISTS `t_user`;
create TABLE `t_user` (
  `id`                        INT(11) PRIMARY KEY COMMENT '主键ID',
  `name`                      VARCHAR(16) DEFAULT '' COMMENT '名字',
  `age`                  	  INT(11) DEFAULT NULL COMMENT '年龄',
  `email`                     VARCHAR(32) DEFAULT '' COMMENT '邮箱'

) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';
