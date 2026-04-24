CREATE DATABASE IF NOT EXISTS studyRoom DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE studyRoom;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS notice;
DROP TABLE IF EXISTS seat;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS user;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE user (
  username VARCHAR(20) NOT NULL COMMENT 'student number',
  password VARCHAR(64) NOT NULL COMMENT 'md5 password',
  name VARCHAR(20) NOT NULL COMMENT 'real name',
  phone VARCHAR(11) DEFAULT NULL COMMENT 'phone number',
  create_time DATETIME DEFAULT NOW() COMMENT 'register time',
  PRIMARY KEY (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='student user';

CREATE TABLE admin (
  id INT NOT NULL AUTO_INCREMENT COMMENT 'admin id',
  username VARCHAR(20) NOT NULL COMMENT 'login username',
  password VARCHAR(64) NOT NULL COMMENT 'md5 password',
  name VARCHAR(20) NOT NULL COMMENT 'admin name',
  create_time DATETIME DEFAULT NOW() COMMENT 'create time',
  PRIMARY KEY (id),
  UNIQUE KEY uk_admin_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='admin';

CREATE TABLE room (
  room_id VARCHAR(10) NOT NULL COMMENT 'business room id',
  room_name VARCHAR(30) NOT NULL COMMENT 'room name',
  location VARCHAR(50) NOT NULL COMMENT 'room location',
  total_seats INT NOT NULL DEFAULT 0 COMMENT 'current seat count',
  open_time TIME NOT NULL COMMENT 'open time',
  close_time TIME NOT NULL COMMENT 'close time',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '0 disabled, 1 enabled',
  full_status TINYINT NOT NULL DEFAULT 0 COMMENT '0 not full, 1 full',
  PRIMARY KEY (room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='study room';

CREATE TABLE seat (
  seat_id VARCHAR(20) NOT NULL COMMENT 'seat id',
  room_id VARCHAR(20) NOT NULL COMMENT 'belong room id',
  seat_number INT NOT NULL COMMENT 'seat number in room',
  status TINYINT NOT NULL DEFAULT 0 COMMENT '0 available, 1 reserved',
  create_time DATETIME DEFAULT NOW() COMMENT 'create time',
  PRIMARY KEY (id),
  KEY idx_seat_room_id (room_id),
  CONSTRAINT fk_seat_room FOREIGN KEY (room_id) REFERENCES room (room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='seat';

CREATE TABLE reservation (
  id INT NOT NULL AUTO_INCREMENT COMMENT 'reservation id',
  user_username VARCHAR(20) NOT NULL COMMENT 'student username',
  room_id VARCHAR(10) NOT NULL COMMENT 'room id',
  seat_id INT NOT NULL COMMENT 'seat id',
  reserve_date DATE NOT NULL COMMENT 'reserve date',
  start_time TIME NOT NULL COMMENT 'reserve start time',
  end_time TIME NOT NULL COMMENT 'reserve end time',
  status TINYINT NOT NULL DEFAULT 0 COMMENT '0 pending, 1 used, 2 canceled, 3 expired',
  create_time DATETIME DEFAULT NOW() COMMENT 'create time',
  PRIMARY KEY (id),
  KEY idx_reservation_user (user_username),
  KEY idx_reservation_room (room_id),
  KEY idx_reservation_seat (seat_id),
  CONSTRAINT fk_reservation_user FOREIGN KEY (user_username) REFERENCES user (username),
  CONSTRAINT fk_reservation_room FOREIGN KEY (room_id) REFERENCES room (room_id),
  CONSTRAINT fk_reservation_seat FOREIGN KEY (seat_id) REFERENCES seat (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='reservation';

CREATE TABLE notice (
  id INT NOT NULL AUTO_INCREMENT COMMENT 'notice id',
  title VARCHAR(50) NOT NULL COMMENT 'notice title',
  content TEXT NOT NULL COMMENT 'notice content',
  create_time DATETIME DEFAULT NOW() COMMENT 'publish time',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='notice';

INSERT INTO user (username, password, name, phone, create_time) VALUES
('20240001', 'e10adc3949ba59abbe56e057f20f883e', 'Zhang San', '13800138000', NOW()),
('20240002', 'e10adc3949ba59abbe56e057f20f883e', 'Li Si', '13900139000', NOW()),
('20240003', 'e10adc3949ba59abbe56e057f20f883e', 'Wang Wu', '13700137000', NOW()),
('20240004', 'e10adc3949ba59abbe56e057f20f883e', 'Zhao Liu', '13600136000', NOW());

INSERT INTO admin (username, password, name, create_time) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', 'System Admin', NOW()),
('manager', 'e10adc3949ba59abbe56e057f20f883e', 'Room Manager', NOW());

INSERT INTO room (room_id, room_name, location, total_seats, open_time, close_time, status, full_status) VALUES
('4401', 'Room 4401', 'Teaching Building 4', 8, '08:00:00', '22:00:00', 1, 0),
('4402', 'Room 4402', 'Teaching Building 4', 8, '08:00:00', '22:00:00', 1, 0),
('3301', 'Room 3301', 'Teaching Building 3', 8, '08:00:00', '22:00:00', 1, 0);

INSERT INTO seat (room_id, status, create_time) VALUES
('4401', 0, NOW()), ('4401', 0, NOW()), ('4401', 0, NOW()), ('4401', 0, NOW()),
('4401', 0, NOW()), ('4401', 0, NOW()), ('4401', 0, NOW()), ('4401', 0, NOW()),
('4402', 0, NOW()), ('4402', 0, NOW()), ('4402', 0, NOW()), ('4402', 0, NOW()),
('4402', 0, NOW()), ('4402', 0, NOW()), ('4402', 0, NOW()), ('4402', 0, NOW()),
('3301', 0, NOW()), ('3301', 0, NOW()), ('3301', 0, NOW()), ('3301', 0, NOW()),
('3301', 0, NOW()), ('3301', 0, NOW()), ('3301', 0, NOW()), ('3301', 0, NOW());

INSERT INTO reservation (user_username, room_id, seat_id, reserve_date, start_time, end_time, status, create_time) VALUES
('20240001', '4401', 1, '2026-04-13', '08:00:00', '10:00:00', 0, NOW()),
('20240002', '4401', 2, '2026-04-13', '10:00:00', '12:00:00', 1, NOW()),
('20240003', '4402', 9, '2026-04-14', '14:00:00', '16:00:00', 0, NOW()),
('20240004', '3301', 17, '2026-04-14', '09:00:00', '11:00:00', 2, NOW());

UPDATE seat
SET status = 1
WHERE id IN (
  SELECT seat_id
  FROM reservation
  WHERE status = 0
);

UPDATE room r
SET full_status = CASE
  WHEN EXISTS (SELECT 1 FROM seat s WHERE s.room_id = r.room_id AND s.status = 0) THEN 0
  ELSE 1
END;

INSERT INTO notice (title, content, create_time) VALUES
('Study Room Rules', 'Please keep quiet and leave the room clean.', NOW()),
('Holiday Notice', 'Opening hours remain 08:00-22:00 during the holiday.', NOW());
