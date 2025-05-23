
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';


-- Schema parkdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `parkdb` DEFAULT CHARACTER SET utf8 ;
USE `parkdb` ;



-- -----------------------------------------------------
-- Table `parkdb`.`Parking_Lot_Manager`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `parkdb`.`Parking_Lot_Manager` (
                                                              `id` INT NOT NULL AUTO_INCREMENT,
                                                              `username` VARCHAR(45) NOT NULL,
    `email` VARCHAR(45) NOT NULL,
    `password` VARCHAR(256) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `parkdb`.`Parking_Lot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `parkdb`.`Parking_Lot` (
                                                      `id` INT NOT NULL AUTO_INCREMENT,
                                                      `longitude` VARCHAR(256) NOT NULL,
    `latitude` VARCHAR(256) NOT NULL,
    `capacity` DECIMAL NOT NULL,
    `price` DECIMAL NOT NULL,
    `type` VARCHAR(256) NOT NULL,
    `revenue` DECIMAL NOT NULL,
    `Parking_Lot_Manager_id` INT NOT NULL,
    PRIMARY KEY (`id`, `Parking_Lot_Manager_id`),
    INDEX `fk_Parking_Lot_Parking_Lot_Manager1_idx` (`Parking_Lot_Manager_id` ASC) VISIBLE,
    CONSTRAINT `fk_Parking_Lot_Parking_Lot_Manager1`
    FOREIGN KEY (`Parking_Lot_Manager_id`)
    REFERENCES `parkdb`.`Parking_Lot_Manager` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `parkdb`.`Parking_Spot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `parkdb`.`Parking_Spot` (
                                                       `id` INT NOT NULL,
                                                       `status` VARCHAR(45) NOT NULL,
    `Parking_Lot_id` INT NOT NULL,
    PRIMARY KEY (`Parking_Lot_id`, `id`),
    CONSTRAINT `fk_Parking_Spot_Parking_Lot`
    FOREIGN KEY (`Parking_Lot_id`)
    REFERENCES `parkdb`.`Parking_Lot` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `parkdb`.`Driver`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `parkdb`.`Driver` (
                                                 `id` INT NOT NULL AUTO_INCREMENT,
                                                 `username` VARCHAR(45) NOT NULL,
    `email` VARCHAR(45) NOT NULL,
    `license_plate_number` VARCHAR(45) NOT NULL,
    `password` VARCHAR(256) NOT NULL,
    `payment_method` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `parkdb`.`Reserved_Spot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `parkdb`.`Reserved_Spot` (
                                                        `end_time` TIMESTAMP(3) NOT NULL,
    `start_time` TIMESTAMP(3) NOT NULL,
    `arrival_time` TIMESTAMP(3) NULL,
    `leave_time` TIMESTAMP(3) NULL,
    `Driver_id` INT NOT NULL,
    `Parking_Spot_Parking_Lot_id` INT NOT NULL,
    `Parking_Spot_id` INT NOT NULL,
    `price` DECIMAL NOT NULL,
    `penalty` DECIMAL DEFAULT 0,
    PRIMARY KEY (`Driver_id`, `Parking_Spot_Parking_Lot_id`, `Parking_Spot_id`, `start_time`),
    INDEX `fk_Reserved_Spot_Driver1_idx` (`Driver_id` ASC) VISIBLE,
    INDEX `fk_Reserved_Spot_Parking_Spot1_idx` (`Parking_Spot_Parking_Lot_id` ASC, `Parking_Spot_id` ASC) VISIBLE,
    CONSTRAINT `fk_Reserved_Spot_Driver1`
    FOREIGN KEY (`Driver_id`)
    REFERENCES `parkdb`.`Driver` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT `fk_Reserved_Spot_Parking_Spot1`
    FOREIGN KEY (`Parking_Spot_Parking_Lot_id` , `Parking_Spot_id`)
    REFERENCES `parkdb`.`Parking_Spot` (`Parking_Lot_id` , `id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB;


CREATE TRIGGER revenue_update_on_reserve AFTER INSERT ON `parkdb`.`Reserved_Spot`
    FOR EACH ROW
    UPDATE `parkdb`.`Parking_Lot`
    SET revenue = revenue + NEW.price
    WHERE NEW.Parking_Spot_Parking_Lot_id = Parking_Lot.id;


DELIMITER //
CREATE TRIGGER revenue_update_on_penalty_update AFTER UPDATE ON `parkdb`.`Reserved_Spot`
    FOR EACH ROW
BEGIN
    IF NEW.penalty <> OLD.penalty THEN
    UPDATE `parkdb`.`Parking_Lot`
    SET revenue = revenue + NEW.penalty - OLD.PENALTY
    WHERE NEW.Parking_Spot_Parking_Lot_id = Parking_Lot.id;
END IF;
END//
DELIMITER ;



-- -----------------------------------------------------
-- Table `parkdb`.`System_Adminstrator`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `parkdb`.`System_Adminstrator` (
                                                              `id` INT NOT NULL AUTO_INCREMENT,
                                                              `username` VARCHAR(45) NOT NULL,
    `email` VARCHAR(45) NOT NULL,
    `password` VARCHAR(256) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
    ENGINE = InnoDB;




SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

DELIMITER $$
CREATE PROCEDURE GetTopParkingLotRevenues()
BEGIN
    SELECT id, longitude, latitude, revenue
    FROM Parking_Lot
    ORDER BY revenue DESC
    LIMIT 10;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE GetTopUsers()
BEGIN
    SELECT Driver_id AS driverId, COUNT(*) AS reservations
    FROM Reserved_Spot
    GROUP BY Driver_id
    ORDER BY reservations DESC
    LIMIT 10;
END $$
DELIMITER ;
