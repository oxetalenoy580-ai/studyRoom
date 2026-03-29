create database studyRoom;
use studyRoom;

CREATE TABLE `user` (
  `username` varchar(20) NOT NULL COMMENT '学号，作为用户唯一主键',
  `password` varchar(64) NOT NULL COMMENT '密码（加密存储）',
  `name` varchar(10) NOT NULL COMMENT '姓名',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `create_time` datetime DEFAULT now() COMMENT '注册时间',
  PRIMARY KEY (`username`)  -- 学号作为主键，唯一标识用户
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '管理员ID，自增主键',
  `username` varchar(20) NOT NULL COMMENT '管理员账号',
  `password` varchar(64) NOT NULL COMMENT '密码（加密存储）',
  `name` varchar(10) NOT NULL COMMENT '姓名',
  `create_time` datetime DEFAULT now() COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';


CREATE TABLE `room` (
  `room_id` varchar(10) NOT NULL COMMENT '自习室自定义ID，手动输入（如4401：4教4层01室），主键',
  `room_name` varchar(30) NOT NULL COMMENT '自习室名称（可选，如第四教学楼401自习室）',
  `location` varchar(50) NOT NULL COMMENT '位置楼层（如第四教学楼4层）',
  `total_seats` int NOT NULL COMMENT '总座位数',
  `open_time` time NOT NULL COMMENT '开放时间',
  `close_time` time NOT NULL COMMENT '关闭时间',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '自习室整体状态：0停用，1启用',
  `full_status` tinyint NOT NULL DEFAULT 0 COMMENT '当前时段满员状态：0未满，1已满（所有座位被预约）',
  PRIMARY KEY (`room_id`)  -- 手动输入的自定义ID作为主键，非自增
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='自习室表';


CREATE TABLE `seat` (
  `seat_id` VARCHAR(20) NOT NULL COMMENT '座位业务号（如 404_01、303_02）',
  `room_id` VARCHAR(20) NOT NULL COMMENT '所属自习室编号',
  `seat_number` INT NOT NULL COMMENT '室内序号（1开始）',
  `status` INT NOT NULL DEFAULT 0 COMMENT '状态：0空闲 1已预约 2使用中',
  `create_time` DATETIME DEFAULT NOW(),
  PRIMARY KEY (`seat_id`),
  UNIQUE KEY `uk_room_seat` (`room_id`,`seat_number`),
  KEY `fk_room` (`room_id`),
  CONSTRAINT `fk_seat_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='座位表（业务ID做主键）';


CREATE TABLE `reservation` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '预约ID，自增主键',
  `user_username` varchar(20) NOT NULL COMMENT '预约用户学号，关联user表的username',
  `room_id` varchar(10) NOT NULL COMMENT '所属自习室ID，关联room表的room_id',
  `seat_id` varchar(20) NOT NULL COMMENT '预约座位ID，关联seat表的id',
  `reserve_date` date NOT NULL COMMENT '预约日期',
  `start_time` time NOT NULL COMMENT '预约开始时间',
  `end_time` time NOT NULL COMMENT '预约结束时间',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '预约状态：0待使用，1已使用，2已取消，3逾期',
  `create_time` datetime DEFAULT now() COMMENT '预约创建时间',
  PRIMARY KEY (`id`),
  KEY `user_username` (`user_username`),
  KEY `room_id` (`room_id`),
  KEY `seat_id` (`seat_id`),
  -- 新增外键关联，保证数据一致性
  CONSTRAINT `fk_reserve_user` FOREIGN KEY (`user_username`) REFERENCES `user` (`username`),
  CONSTRAINT `fk_reserve_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`),
  CONSTRAINT `fk_reserve_seat` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`seat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约记录表';

CREATE TABLE `notice` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID，自增主键',
  `title` varchar(50) NOT NULL COMMENT '公告标题',
  `content` text NOT NULL COMMENT '公告内容',
  `create_time` datetime DEFAULT now() COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统公告表';



INSERT INTO `user` (`username`, `password`, `name`, `phone`, `create_time`)
VALUES 
('20240001','123456','张三','13800138000',NOW()),
('20240002','123456','李四','13900139000',NOW()),
('20240003','123456','王五','13700137000',NOW()),
('20240004','123456','赵六','13600136000',NOW()),
('20240005','123456','钱七','13500135000',NOW()),
('20240006','123456','孙八','13400134000',NOW()),
('20240007','123456','周九','13300133000',NOW()),
('20240008','123456','吴十','13200132000',NOW());

INSERT INTO `admin` (`username`, `password`, `name`, `create_time`)
VALUES 
('admin', '123456', '系统管理员', now()),
('manager', '123456', '自习室管理员', now());

INSERT INTO `room` (`room_id`,`room_name`,`location`,`total_seats`,`open_time`,`close_time`,`status`,`full_status`)
VALUES 
('4401','第四教学楼401自习室','第四教学楼4层',50,'08:00:00','22:00:00',1,0),
('4402','第四教学楼402自习室','第四教学楼4层',40,'08:00:00','22:00:00',1,0),
('3301','第三教学楼301自习室','第三教学楼3层',45,'08:00:00','22:00:00',1,0),
('3302','第三教学楼302自习室','第三教学楼3层',30,'08:00:00','22:00:00',1,0),
('2201','第二教学楼201自习室','第二教学楼2层',35,'09:00:00','21:00:00',1,0),
('2202','第二教学楼202自习室','第二教学楼2层',25,'09:00:00','21:00:00',1,0);

INSERT INTO `seat` (`seat_id`, `room_id`, `seat_number`, `status`) VALUES 
('4401_01','4401',1,0),('4401_02','4401',2,0),('4401_03','4401',3,0),('4401_04','4401',4,0),('4401_05','4401',5,0),('4401_06','4401',6,0),('4401_07','4401',7,0),('4401_08','4401',8,0),
('4402_01','4402',1,0),('4402_02','4402',2,0),('4402_03','4402',3,0),('4402_04','4402',4,0),('4402_05','4402',5,0),('4402_06','4402',6,0),('4402_07','4402',7,0),('4402_08','4402',8,0),
('3301_01','3301',1,0),('3301_02','3301',2,0),('3301_03','3301',3,0),('3301_04','3301',4,0),('3301_05','3301',5,0),('3301_06','3301',6,0),('3301_07','3301',7,0),('3301_08','3301',8,0),
('3302_01','3302',1,0),('3302_02','3302',2,0),('3302_03','3302',3,0),('3302_04','3302',4,0),('3302_05','3302',5,0),('3302_06','3302',6,0),('3302_07','3302',7,0),('3302_08','3302',8,0),
('2201_01','2201',1,0),('2201_02','2201',2,0),('2201_03','2201',3,0),('2201_04','2201',4,0),('2201_05','2201',5,0),('2201_06','2201',6,0),('2201_07','2201',7,0),('2201_08','2201',8,0),
('2202_01','2202',1,0),('2202_02','2202',2,0),('2202_03','2202',3,0),('2202_04','2202',4,0),('2202_05','2202',5,0),('2202_06','2202',6,0),('2202_07','2202',7,0),('2202_08','2202',8,0);



INSERT INTO `reservation` (`user_username`,`room_id`,`seat_id`,`reserve_date`,`start_time`,`end_time`,`status`,`create_time`)
VALUES 
('20240001','4401','4401_01','2026-04-10','08:00:00','12:00:00',0,NOW()),
('20240002','4401','4401_02','2026-04-10','08:00:00','12:00:00',1,NOW()),
('20240003','4402','4402_03','2026-04-10','14:00:00','18:00:00',0,NOW()),
('20240004','3301','3301_04','2026-04-10','09:00:00','11:00:00',0,NOW()),
('20240005','3301','3301_05','2026-04-10','13:00:00','17:00:00',2,NOW()),
('20240006','3302','3302_06','2026-04-10','10:00:00','12:00:00',0,NOW()),
('20240007','2201','2201_07','2026-04-10','15:00:00','19:00:00',3,NOW()),
('20240008','2202','2202_08','2026-04-10','09:00:00','13:00:00',0,NOW()),

('20240001','4401','4401_01','2026-04-11','08:00:00','12:00:00',0,NOW()),
('20240002','4402','4402_02','2026-04-11','14:00:00','18:00:00',1,NOW()),
('20240003','3301','3301_03','2026-04-11','09:00:00','11:00:00',0,NOW()),
('20240004','3302','3302_04','2026-04-11','13:00:00','17:00:00',2,NOW()),
('20240005','2201','2201_05','2026-04-11','10:00:00','12:00:00',0,NOW()),
('20240006','2202','2202_06','2026-04-11','15:00:00','19:00:00',3,NOW()),
('20240007','4401','4401_07','2026-04-11','09:00:00','13:00:00',0,NOW()),
('20240008','4402','4402_08','2026-04-11','08:00:00','12:00:00',1,NOW());


INSERT INTO `notice` (`title`, `content`, `create_time`)
VALUES 
('自习室使用规范', '请保持安静，禁止占座，离开时带走个人物品。', now()),
('节假日开放调整', '清明节期间自习室正常开放，开放时间为08:00-22:00。', now());
