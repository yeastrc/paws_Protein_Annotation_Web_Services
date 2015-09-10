
CREATE SCHEMA IF NOT EXISTS paws DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE paws ;

-- -----------------------------------------------------
-- Table sequence
-- -----------------------------------------------------
DROP TABLE IF EXISTS sequence ;

CREATE TABLE IF NOT EXISTS sequence (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  sequence MEDIUMTEXT NULL,
  PRIMARY KEY (id))
ENGINE = InnoDB;

CREATE INDEX sequence_sequence ON sequence (sequence(255) ASC);


-- -----------------------------------------------------
-- Table annotation_type
-- -----------------------------------------------------
DROP TABLE IF EXISTS annotation_type ;

CREATE TABLE IF NOT EXISTS annotation_type (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  type VARCHAR(45) NOT NULL,
  PRIMARY KEY (id))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table annotation_data
-- -----------------------------------------------------
DROP TABLE IF EXISTS annotation_data ;

CREATE TABLE IF NOT EXISTS annotation_data (
  sequence_id INT UNSIGNED NOT NULL,
  annotation_type_id INT UNSIGNED NOT NULL,
  ncbi_taxonomy_id INT UNSIGNED NOT NULL,
  run_status ENUM('submitted','complete','fail') NOT NULL,
  last_run_date DATETIME NOT NULL,
  annotation_data LONGTEXT NULL,
  PRIMARY KEY (sequence_id, annotation_type_id, ncbi_taxonomy_id),
  CONSTRAINT annotation_data_sequence_id_fk
    FOREIGN KEY (sequence_id)
    REFERENCES sequence (id)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
  CONSTRAINT annotation_data_annotation_type_id_fk
    FOREIGN KEY (annotation_type_id)
    REFERENCES annotation_type (id)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

CREATE INDEX annotation_data_annotation_type_id_fk_idx ON annotation_data (annotation_type_id ASC);


-- -----------------------------------------------------
-- Table config_system
-- -----------------------------------------------------
DROP TABLE IF EXISTS config_system ;

CREATE TABLE IF NOT EXISTS config_system (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  config_key VARCHAR(45) NOT NULL,
  config_value VARCHAR(4000) NULL,
  comment VARCHAR(4000) NULL,
  version INT UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (id))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table annotation_processing_tracking
-- -----------------------------------------------------
DROP TABLE IF EXISTS annotation_processing_tracking ;

CREATE TABLE IF NOT EXISTS annotation_processing_tracking (
  sequence_id INT UNSIGNED NOT NULL,
  annotation_type_id INT UNSIGNED NOT NULL,
  ncbi_taxonomy_id INT UNSIGNED NOT NULL,
  jobcenter_request_id INT UNSIGNED NOT NULL,
  run_status ENUM('submitted','complete','fail') NOT NULL,
  last_update_date DATETIME NOT NULL,
  PRIMARY KEY (sequence_id, annotation_type_id, ncbi_taxonomy_id, jobcenter_request_id),
  CONSTRAINT annotation_to_process_submission__sequence_id_fk
    FOREIGN KEY (sequence_id)
    REFERENCES sequence (id)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
  CONSTRAINT annotation_to_process_submission__annotation_type_id_fk
    FOREIGN KEY (annotation_type_id)
    REFERENCES annotation_type (id)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

CREATE INDEX annotation_to_process_submission__annotation_type_id_fk_idx ON annotation_processing_tracking (annotation_type_id ASC);

