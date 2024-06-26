-- MySQL Script generated by MySQL Workbench
-- Mon Sep 11 19:22:39 2023
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema dssoluti_residenciales
-- -----------------------------------------------------


-- -----------------------------------------------------
-- Schema dssoluti_residenciales
-- -----------------------------------------------------

USE `dssoluti_residenciales` ;

-- -----------------------------------------------------
-- Table `dssoluti_residenciales`.`residential`
-- -----------------------------------------------------
DROP TABLE `residential`;
CREATE TABLE IF NOT EXISTS `residential` (
  `idresidential` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `address` VARCHAR(200) NULL,
  `phone` VARCHAR(9) NULL,
  `email` VARCHAR(100) NULL,
  `nit` VARCHAR(15) NULL,
  `logo` VARCHAR(400) NULL,
  `status` INT NOT NULL,
  `create_time` DATETIME NULL,
  `create_user` INT NULL,
  `modify_time` DATETIME NULL,
  `modify_user` INT NULL,
  PRIMARY KEY (`idresidential`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) );


-- -----------------------------------------------------
-- Table `dssoluti_residenciales`.`user`
-- -----------------------------------------------------
DROP TABLE `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `iduser` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `username` VARCHAR(16) NULL,
  `password` VARCHAR(256) NULL,
  `email` VARCHAR(255) NULL,
  `phone` VARCHAR(9) NOT NULL,
  `code` VARCHAR(45) NULL,
  `address` VARCHAR(100) NULL,
  `position` VARCHAR(100) NULL,
  `photo` VARCHAR(400) NULL,
  `employee` INT NULL,
  `status` INT NOT NULL,
  `idresidential` INT NOT NULL,
  `create_time` DATETIME NULL,
  `create_user` INT NULL,
  `modify_time` DATETIME NULL,
  `modify_user` INT NULL,
  PRIMARY KEY (`iduser`),
  INDEX `fk_user_residential1_idx` (`idresidential` ASC) ,
  CONSTRAINT `fk_user_residential1`
    FOREIGN KEY (`idresidential`)
    REFERENCES `residential` (`idresidential`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `dssoluti_residenciales`.`role`
-- -----------------------------------------------------
DROP TABLE `role`;
CREATE TABLE IF NOT EXISTS `role` (
  `idrole` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `status` INT NOT NULL,
  `create_time` DATETIME NULL,
  `create_user` INT NULL,
  `modify_time` DATETIME NULL,
  `modify_user` INT NULL,
  PRIMARY KEY (`idrole`));


-- -----------------------------------------------------
-- Table `dssoluti_residenciales`.`role_user`
-- -----------------------------------------------------
DROP TABLE `role_user`;
CREATE TABLE IF NOT EXISTS `role_user` (
  `iduser` INT NOT NULL,
  `idrole` INT NOT NULL,
  INDEX `fk_rol_user_user_idx` (`iduser` ASC) ,
  INDEX `fk_rol_user_rol1_idx` (`idrole` ASC) ,
  PRIMARY KEY (`iduser`, `idrole`),
  CONSTRAINT `fk_rol_user_user`
    FOREIGN KEY (`iduser`)
    REFERENCES `user` (`iduser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_rol_user_rol1`
    FOREIGN KEY (`idrole`)
    REFERENCES `role` (`idrole`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dssoluti_residenciales`.`page`
-- -----------------------------------------------------
DROP TABLE `page`;
CREATE TABLE IF NOT EXISTS `page` (
  `idpage` INT NOT NULL AUTO_INCREMENT,
  `path` VARCHAR(255) NOT NULL,
  `menu` INT NULL,
  `status` INT NULL,
  PRIMARY KEY (`idpage`),
  UNIQUE INDEX `name_UNIQUE` (`path` ASC) );


-- -----------------------------------------------------
-- Table `dssoluti_residenciales`.`role_page`
-- -----------------------------------------------------
DROP TABLE `role_page`;
CREATE TABLE IF NOT EXISTS `role_page` (
  `idrole` INT NOT NULL,
  `idpage` INT NOT NULL,
  INDEX `fk_category_role1_idx` (`idrole` ASC) ,
  INDEX `fk_category_role_page1_idx` (`idpage` ASC) ,
  CONSTRAINT `fk_category_role1`
    FOREIGN KEY (`idrole`)
    REFERENCES `role` (`idrole`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_category_role_page1`
    FOREIGN KEY (`idpage`)
    REFERENCES `page` (`idpage`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `dssoluti_residenciales`.`service`
-- -----------------------------------------------------
DROP TABLE `service`;
CREATE TABLE IF NOT EXISTS `service` (
  `idservice` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NOT NULL,
  `description` VARCHAR(100) NULL,
  `idresidential` INT NOT NULL,
  `status` INT NULL,
  `create_time` DATETIME NULL,
  `create_user` INT NULL,
  `modify_time` DATETIME NULL,
  `modify_user` INT NULL,
  PRIMARY KEY (`idservice`),
  INDEX `fk_service_residential1_idx` (`idresidential` ASC) ,
  CONSTRAINT `fk_service_residential1`
    FOREIGN KEY (`idresidential`)
    REFERENCES `residential` (`idresidential`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `dssoluti_residenciales`.`ticket_type`
-- -----------------------------------------------------
DROP TABLE `ticket_type`;
CREATE TABLE IF NOT EXISTS `ticket_type` (
  `idtype` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`idtype`));


-- -----------------------------------------------------
-- Table `dssoluti_residenciales`.`ticket_status`
-- -----------------------------------------------------
DROP TABLE `ticket_status`;
CREATE TABLE IF NOT EXISTS `ticket_status` (
  `idstatus` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`idstatus`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dssoluti_residenciales`.`ticket`
-- -----------------------------------------------------
DROP TABLE `ticket`;
CREATE TABLE IF NOT EXISTS `ticket` (
  `idticket` INT NOT NULL AUTO_INCREMENT,
  `subject` VARCHAR(100) NULL,
  `description` VARCHAR(400) NOT NULL,
  `creation_date` DATETIME NULL,
  `iduser` INT NOT NULL,
  `idticket_type` INT NOT NULL,
  `idstatus` INT NOT NULL,
  `idresidential` INT NOT NULL,
  `create_time` DATETIME NULL,
  `create_user` INT NULL,
  `modify_time` DATETIME NULL,
  `modify_user` INT NULL,
  PRIMARY KEY (`idticket`),
  INDEX `fk_ticket_user1_idx` (`iduser` ASC) ,
  INDEX `fk_ticket_ticket_type1_idx` (`idticket_type` ASC) ,
  INDEX `fk_ticket_ticket_status1_idx` (`idstatus` ASC) ,
  INDEX `fk_ticket_residential1_idx` (`idresidential` ASC) ,
  CONSTRAINT `fk_ticket_user1`
    FOREIGN KEY (`iduser`)
    REFERENCES `user` (`iduser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ticket_ticket_type1`
    FOREIGN KEY (`idticket_type`)
    REFERENCES `ticket_type` (`idtype`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ticket_ticket_status1`
    FOREIGN KEY (`idstatus`)
    REFERENCES `ticket_status` (`idstatus`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ticket_residential1`
    FOREIGN KEY (`idresidential`)
    REFERENCES `residential` (`idresidential`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dssoluti_residenciales`.`service_request`
-- -----------------------------------------------------
DROP TABLE `service_request`;
CREATE TABLE IF NOT EXISTS `service_request` (
  `idservicerequest` INT NOT NULL AUTO_INCREMENT,
  `subject` VARCHAR(200) NOT NULL,
  `comment` VARCHAR(1000) NULL,
  `start_date` DATETIME NULL,
  `iduser` INT NULL,
  `idemployee` INT NULL,
  `idresidential` INT NOT NULL,
  `status` INT NULL,
  `idservice` INT NOT NULL,
  `create_time` DATETIME NULL,
  `create_user` INT NULL,
  `modify_time` DATETIME NULL,
  `modify_user` INT NULL,
  PRIMARY KEY (`idservicerequest`),
  INDEX `fk_service_user1_idx` (`iduser` ASC) ,
   INDEX `fk_service_employee_idx` (`idemployee` ASC) ,
  INDEX `fk_service_residential1_idx` (`idresidential` ASC) ,
  INDEX `fk_service_request_service1_idx` (`idservice` ASC) ,
  CONSTRAINT `fk_service_user10`
    FOREIGN KEY (`iduser`)
    REFERENCES `user` (`iduser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_service_employee10`
    FOREIGN KEY (`idemployee`)
    REFERENCES `user` (`iduser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_service_residential10`
    FOREIGN KEY (`idresidential`)
    REFERENCES `residential` (`idresidential`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_service_request_service1`
    FOREIGN KEY (`idservice`)
    REFERENCES `service` (`idservice`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `dssoluti_residenciales`.`comment`
-- -----------------------------------------------------
DROP TABLE `comment`;
CREATE TABLE IF NOT EXISTS `comment` (
  `idcomment` INT NOT NULL AUTO_INCREMENT,
  `idticket` INT NOT NULL,
  `iduser` INT NOT NULL,
  `status` INT NOT NULL,
  `attachment` VARCHAR(200) NULL,
  `date` DATETIME NULL,
  `comment` VARCHAR(1000) NULL,
  INDEX `fk_comment_ticket1_idx` (`idticket` ASC) ,
  INDEX `fk_comment_user1_idx` (`iduser` ASC) ,
  PRIMARY KEY (`idcomment`),
  CONSTRAINT `fk_comment_ticket1`
    FOREIGN KEY (`idticket`)
    REFERENCES `ticket` (`idticket`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_comment_user1`
    FOREIGN KEY (`iduser`)
    REFERENCES `user` (`iduser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `dssoluti_residenciales`.`message`
-- -----------------------------------------------------
DROP TABLE `message`;
CREATE TABLE IF NOT EXISTS `message` (
  `idmessage` INT NOT NULL AUTO_INCREMENT,
  `subject` VARCHAR(100) NULL,
  `description` VARCHAR(1000) NULL,
  `start_date` DATETIME NULL,
  `final_date` DATETIME NULL,
  `idresidential` INT NOT NULL,
  `idstatus` INT NOT NULL,
  `iduser` INT NULL,
  `attachment` VARCHAR(100) NULL,
  `type` VARCHAR(1) NULL,
  `create_time` DATETIME NULL,
  `create_user` INT NULL,
  `modify_time` DATETIME NULL,
  `modify_user` INT NULL,
  INDEX `fk_message_residential1_idx` (`idresidential` ASC) ,
  INDEX `fk_message_ticket_status1_idx` (`idstatus` ASC) ,
  INDEX `fk_message_user1_idx` (`iduser` ASC) ,
  PRIMARY KEY (`idmessage`),
  CONSTRAINT `fk_message_residential1`
    FOREIGN KEY (`idresidential`)
    REFERENCES `dssoluti_residenciales`.`residential` (`idresidential`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_message_ticket_status1`
    FOREIGN KEY (`idstatus`)
    REFERENCES `dssoluti_residenciales`.`ticket_status` (`idstatus`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_message_user1`
    FOREIGN KEY (`iduser`)
    REFERENCES `dssoluti_residenciales`.`user` (`iduser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dssoluti_residenciales`.`mailbox`
-- -----------------------------------------------------
DROP TABLE `mailbox`;
CREATE TABLE IF NOT EXISTS `mailbox` (
  `idmailbox` INT NOT NULL AUTO_INCREMENT,
  `iduser` INT NOT NULL,
  `subject` VARCHAR(100) NULL,
  `description` VARCHAR(1000) NULL,
  `attachment` VARCHAR(100) NULL,
  `status` INT NULL,
  `create_time` DATETIME NULL,
  `create_user` INT NULL,
  `modify_time` DATETIME NULL,
  `modify_user` INT NULL,
  PRIMARY KEY (`idmailbox`),
  INDEX `fk_mailbox_user1_idx` (`iduser` ASC) ,
  CONSTRAINT `fk_mailbox_user1`
    FOREIGN KEY (`iduser`)
    REFERENCES `user` (`iduser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `dssoluti_residenciales`.`contact`
-- -----------------------------------------------------
DROP TABLE `contact`;
CREATE TABLE IF NOT EXISTS `contact` (
  `idcontact` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NULL,
  `description` VARCHAR(500) NULL,
  `phone` VARCHAR(12) NULL,
  `idresidential` INT NOT NULL,
   `status` INT NULL,
  `create_time` DATETIME NULL,
  `create_user` INT NULL,
  `modify_time` DATETIME NULL,
  `modify_user` INT NULL,
  INDEX `fk_contact_residential1_idx` (`idresidential` ASC) ,
  PRIMARY KEY (`idcontact`),
  CONSTRAINT `fk_contact_residential1`
    FOREIGN KEY (`idresidential`)
    REFERENCES `residential` (`idresidential`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


-- -----------------------------------------------------
-- INSERTS
-- -----------------------------------------------------
INSERT INTO residential (name,address,phone,email,nit,logo,status,create_time,create_user,modify_time,modify_user) VALUES
	 ('DS','guatemala','12345678','cesar970@gmail.com','','',1,NOW(),1,NULL,NULL),
	 ('demo1','guatemala','12345678','cesar970@gmail.com','','images/logos/logo3.jpeg',1,NOW(),1,NULL,NULL);

INSERT INTO `user` (name,username,password,email,phone,code,address,`position`,photo,employee,status,create_time,create_user,modify_time,modify_user,idresidential) VALUES
	 ('Cesar Joel Bojorquez','usuario1','$2a$10$B2CgcKhdQ3SDevqKqNgc7eNa3.d8J.rfys5IOgqFbRact26jb/vk2','cesar970@gmail.com','','','gau','','',0,1,NOW(),1,NULL,NULL,1),
	 ('Juan ','usuario2','$2a$10$B2CgcKhdQ3SDevqKqNgc7eNa3.d8J.rfys5IOgqFbRact26jb/vk2','cesar970@gmail.com','30391123','','villa nueva','Jardinero','',0,1,NOW(),1,NULL,NULL,2),
	 ('miguel','usuario3','$2a$10$B2CgcKhdQ3SDevqKqNgc7eNa3.d8J.rfys5IOgqFbRact26jb/vk2','cesar970@gmail.com','30391123',NULL,'villa nueva',NULL,'',0,1,NOW(),1,NULL,NULL,2);

INSERT INTO `role` (`idrole`,`name`, `status`, `create_time`, `create_user`) VALUES ('1','ROLE_ADMIN', '1', NOW(), '1');
INSERT INTO `role` (`idrole`, `name`, `status`, `create_time`, `create_user`) VALUES ('2', 'ROLE_EMPLOYEE', '1', NOW(), '1');
INSERT INTO `role` (`idrole`, `name`, `status`, `create_time`, `create_user`) VALUES ('3', 'ROLE_USER', '1', NOW(), '1');

INSERT INTO `role_user` (`iduser`, `idrole`) VALUES ('1', '1');
INSERT INTO `role_user` (`iduser`, `idrole`) VALUES ('2', '2');
INSERT INTO `role_user` (`iduser`, `idrole`) VALUES ('3', '3');

INSERT INTO `ticket_type` (`name`) VALUES ('Gestion');
INSERT INTO `ticket_type` (`name`) VALUES ('Anomalia');

INSERT INTO `ticket_status` (`description`) VALUES ('Creado');
INSERT INTO `ticket_status` (`description`) VALUES ('Abierto');
INSERT INTO `ticket_status` (`description`) VALUES ('En progreso');
INSERT INTO `ticket_status` (`description`) VALUES ('Cerrado');

INSERT INTO page (path,status,menu) VALUES ('/usuario',1,1);
INSERT INTO page (path,status,menu) VALUES ('/crearus',1,0);
INSERT INTO page (path,status,menu) VALUES ('/rol',1,1);
INSERT INTO page (path,status,menu) VALUES ('/modificarrol',1,0);
INSERT INTO page (path,status,menu) VALUES ('/residencial',1,1);
INSERT INTO page (path,status,menu) VALUES ('/modificarres',1,0);
INSERT INTO page (path,status,menu) VALUES ('/empleado',1,1);
INSERT INTO page (path,status,menu) VALUES ('/modificarus',1,0);
INSERT INTO page (path,status,menu) VALUES ('/modificaremp',1,0);
INSERT INTO page (path,status,menu) VALUES ('/listaUsuarios',1,1);
INSERT INTO page (path,status,menu) VALUES ('/asignarol',1,0);
INSERT INTO page (path,status,menu) VALUES ('/usuariores',1,1);
INSERT INTO page (path,status,menu) VALUES ('/modificarusres',1,0);
INSERT INTO page (path,status,menu) VALUES ('/crearusres',1,0);
INSERT INTO page (path,status,menu) VALUES ('/crearemp',1,0);
INSERT INTO page (path,status,menu) VALUES ('/crearnotificacion',1,0);
INSERT INTO page (path,status,menu) VALUES ('/especifica',1,1);
INSERT INTO page (path,status,menu) VALUES ('/general',1,1);
INSERT INTO page (path,status,menu) VALUES ('/modificarespecifica',1,0);
INSERT INTO page (path,status,menu) VALUES ('/modificargeneral',1,0);
INSERT INTO page (path,status,menu) VALUES ('/modificargerstion',1,0);
INSERT INTO page (path,status,menu) VALUES ('/modificarserv',1,0);
INSERT INTO page (path,status,menu) VALUES ('/servicio',1,1);
INSERT INTO page (path,status,menu) VALUES ('/verespecifica',1,0);
INSERT INTO page (path,status,menu) VALUES ('/vergeneral',1,0);
INSERT INTO page (path,status,menu) VALUES ('/enviageneral',1,0);
INSERT INTO page (path,status,menu) VALUES ('/empleadores',1,1);
INSERT INTO page (path,status,menu) VALUES ('/crearempres',1,0);
INSERT INTO page (path,status,menu) VALUES ('/modificarempres',1,1);
INSERT INTO page (path,status,menu) VALUES ('/modificargestion',1,0);
INSERT INTO page (path,status,menu) VALUES ('/creargestion',1,0);
INSERT INTO page (path,status,menu) VALUES ('/verbuzon',1,0);
INSERT INTO page (path,status,menu) VALUES ('/perfil',1,0);
INSERT INTO page (path,status,menu) VALUES ('/userconfigauth',1,0);
INSERT INTO page (path,status,menu) VALUES ('/anomalia',1,1);
INSERT INTO page (path,status,menu) VALUES ('/crearanomalia',1,0);
INSERT INTO page (path,status,menu) VALUES ('/modificaranomalia',1,0);
INSERT INTO page (path,status,menu) VALUES ('/gestion',1,1);
INSERT INTO page (path,status,menu) VALUES ('/solicitud',1,1);
INSERT INTO page (path,status,menu) VALUES ('/',1,0);

