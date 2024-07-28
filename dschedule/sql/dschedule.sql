create database dschedule character set utf8;

CREATE TABLE `dschedule`.`delay_message` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `app_id` VARCHAR(64) NULL,
  `scene` VARCHAR(256) NULL,
  `delay_time` BIGINT(20) NULL,
  `deal_status` TINYINT NULL,
  `retry_time` TINYINT NULL,
  `window_package_time` INT NULL,
  `delay_type` TINYINT NULL,
  `seq_id` VARCHAR(128) NULL,
  `call_back_url` VARCHAR(512) NULL,
  `grey_version` VARCHAR(128) NULL,
  `business_param` TEXT NULL,
  `extra_param` TEXT NULL,
  `retry_interval_time` BIGINT(20) NULL,
  `expire_time` BIGINT(20) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `seq_id_UNIQUE` (`seq_id` ASC) VISIBLE,
  INDEX `app_id_INDEX` (`app_id` ASC, `create_time` ASC) VISIBLE,
  INDEX `retry_interval_time_INDEX` (`retry_interval_time` ASC) VISIBLE,
  INDEX `create_time_INDEX` (`create_time` ASC) VISIBLE,
  INDEX `update_time_INDEX` (`updateTime` ASC) VISIBLE,
  INDEX `scene_INDEX` (`scene` ASC) VISIBLE)
DEFAULT CHARACTER SET = utf8;