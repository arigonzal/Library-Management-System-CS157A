-- -----------------------------------------------------
-- Schema LibrarySystem
-- -----------------------------------------------------

CREATE SCHEMA IF NOT EXISTS `LibrarySystem` ;
USE `LibrarySystem` ;

-- -----------------------------------------------------
-- Table `LibrarySystem`.`Books`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `LibrarySystem`.`Books` (
  `Book_ID` INT NOT NULL AUTO_INCREMENT,
  `Title` VARCHAR(45) NOT NULL,
  `Author` VARCHAR(30) NOT NULL,
  `PageCount` INT NOT NULL,
  `Genre` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`Book_ID`));


-- -----------------------------------------------------
-- Table `LibrarySystem`.`Members`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `LibrarySystem`.`Members` (
  `Member_ID` INT NOT NULL AUTO_INCREMENT,
  `FirstName` VARCHAR(45) NOT NULL,
  `LastName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Member_ID`));


-- -----------------------------------------------------
-- Table `LibrarySystem`.`BorrowRecords`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `LibrarySystem`.`BorrowRecords` (
  `Book_ID` INT NOT NULL,
  `Member_ID` INT NOT NULL,
  `DueDate` DATE NOT NULL,
  PRIMARY KEY (`Book_ID`),
  INDEX `fk_BorrowRecords_Books_idx` (`Book_ID` ASC) VISIBLE,
  INDEX `fk_BorrowRecords_Members1_idx` (`Member_ID` ASC) VISIBLE,
  CONSTRAINT `fk_BorrowRecords_Books`
    FOREIGN KEY (`Book_ID`)
    REFERENCES `LibrarySystem`.`Books` (`Book_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BorrowRecords_Members1`
    FOREIGN KEY (`Member_ID`)
    REFERENCES `LibrarySystem`.`Members` (`Member_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `LibrarySystem`.`Fines`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `LibrarySystem`.`Fines` (
  `Member_ID` INT NOT NULL,
  `Amount` DOUBLE NOT NULL DEFAULT 0.0,
  `LastFineDate` DATE NOT NULL DEFAULT '1899-01-01',
  PRIMARY KEY (`Member_ID`),
  INDEX `fk_Fines_Members1_idx` (`Member_ID` ASC) VISIBLE,
  CONSTRAINT `fk_Fines_Members1`
    FOREIGN KEY (`Member_ID`)
    REFERENCES `LibrarySystem`.`Members` (`Member_ID`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT);


DELIMITER $$


CREATE DEFINER = CURRENT_USER TRIGGER `LibrarySystem`.`Members_AFTER_INSERT` AFTER INSERT ON `Members` FOR EACH ROW
BEGIN
INSERT INTO Fines (Member_ID) VALUES (NEW.Member_ID);
END$$


CREATE DEFINER = CURRENT_USER TRIGGER `LibrarySystem`.`Members_AFTER_DELETE` AFTER DELETE ON `Members` FOR EACH ROW
BEGIN
DELETE FROM Fines WHERE Member_ID = OLD.Member_ID;
END$$


DELIMITER ;