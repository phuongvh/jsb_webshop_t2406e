CREATE DATABASE jsb_webshop_t2406e
    DEFAULT CHARACTER SET = 'utf8mb4';
    
USE jsb_webshop_t2406e;

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` enum('ROLE_ADMIN','ROLE_MODERATOR','ROLE_USER') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
INSERT INTO `role` VALUES 
(1,'ROLE_USER'),
(2,'ROLE_MODERATOR'),
(3,'ROLE_ADMIN'),
(4,'ROLE_ADMIN')
;


DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(120) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

INSERT INTO `user` VALUES 
(1,'steve@apple.com','$2a$10$xgVvu/tzZgrklqQHMziAFuqWsVx2Fbgcu2IBnvqVy1xZg1pSu2Mg.','stevejobs'),
(2,'woz@apple.com','$2a$10$vLFJfAoVCzl4SOnq6QEtCuU2b8SUpvnJ3NRaSpp//OASLm6vo/r4y','wozniac'),
(3,'billgates@microsoft.com','$2a$10$yAxpsBIJsWps7/iAGXj5RO.gD7KB94Q6UtA3dYrITd4uaTWOdunUy','billgates'),
(4,'ballmer@microsoft.com','$2a$10$1TRSpHcFj500pWrL7TpQSuAYpA9wfn14.4ehKDa6rFc8Rq89nt7lq','ballmer')
;

DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO `user_roles` VALUES 
(1,1),
(2,1),
(3,1),
(4,1),
(4,2);