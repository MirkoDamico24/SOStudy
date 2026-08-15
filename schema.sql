DROP SCHEMA IF EXISTS `SoStudyDB`;
CREATE SCHEMA `SoStudyDB`;

-- --------------------------------
-- TABLES
-- --------------------------------

DROP TABLE IF EXISTS `SoStudyDB`.`Professor` ;
CREATE TABLE `SoStudyDB`.`Professor` (
    `email` VARCHAR(45) NOT NULL,
    `name` VARCHAR(20) NOT NULL,
    `surname` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`email`))
ENGINE = InnoDB;

DROP TABLE IF EXISTS `SoStudyDB`.`Class` ;
CREATE TABLE `SoStudyDB`.`Class` (
 `code` INT UNSIGNED NOT NULL AUTO_INCREMENT,
 `name` VARCHAR(60) NOT NULL,
  `professor` VARCHAR(45),
    PRIMARY KEY (`code`),
    FOREIGN KEY (`professor`) REFERENCES
        `SoStudyDB`.Professor (`email`)
        ON DELETE NO ACTION
        ON UPDATE CASCADE
     )
ENGINE = InnoDB;

DROP TABLE IF EXISTS `SoStudyDB`.`Student` ;
CREATE TABLE `SoStudyDB`.`Student` (
     `email` VARCHAR(45) NOT NULL,
     `name` VARCHAR(20) NOT NULL,
     `surname` VARCHAR(20) NOT NULL,
     PRIMARY KEY (`email`)
)
ENGINE = InnoDB;

DROP TABLE IF EXISTS `SoStudyDB`.`ClassStudents` ;
CREATE TABLE `SoStudyDB`.`ClassStudents` (
    `student` VARCHAR(45) NOT NULL,
    `class` INT UNSIGNED NOT NULL,
    FOREIGN KEY (`student`) REFERENCES `SoStudyDB`.`Student` (`email`),
    FOREIGN KEY (`class`) REFERENCES `SoStudyDB`.`Class` (`code`)
)
ENGINE = InnoDB;

DROP TABLE IF EXISTS `SoStudyDB`.`Test` ;
CREATE TABLE `SoStudyDB`.`Test` (
    `code` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(60) NOT NULL,
    `dueDate` DATE NOT NULL,
    `dueTime` TIME NOT NULL,
    `duration` VARCHAR(10) NOT NULL,
    `class` INT UNSIGNED NOT NULL,
    PRIMARY KEY (`code`),
   FOREIGN KEY (`class`)
       REFERENCES `SoStudyDB`.`Class` (`code`)
       ON DELETE NO ACTION
       ON UPDATE CASCADE
)
ENGINE = InnoDB;

DROP TABLE IF EXISTS `SoStudyDB`.`Domanda` ;
CREATE TABLE `SoStudyDB`.`Domanda` (
    `code` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `header` VARCHAR(150) NOT NULL,
    `maxScore` INT NOT NULL,
    `type` ENUM('OPENQUESTION', 'CLOSEQUESTION'),
    `test` INT UNSIGNED NOT NULL,
    PRIMARY KEY (`code`),
   FOREIGN KEY (`test`)
       REFERENCES `SoStudyDB`.`Test`(`code`)
       ON DELETE CASCADE
       ON UPDATE CASCADE
)
ENGINE = InnoDB;

DROP TABLE IF EXISTS `SoStudyDB`.`OpzioniDomande` ;
CREATE TABLE `SoStudyDB`.`OpzioniDomande` (
    `code` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `content` VARCHAR(150) NOT NULL,
    `isSolution` BOOLEAN DEFAULT FALSE,
    `question` INT UNSIGNED NOT NULL,
    PRIMARY KEY (`code`),
    FOREIGN KEY (`question`)
        REFERENCES `SoStudyDB`.`Domanda` (`code`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
)
    ENGINE = InnoDB;

DROP TABLE IF EXISTS `SoStudyDB`.`Tentativo` ;
CREATE TABLE `SoStudyDB`.`Tentativo` (
    `testID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `grade` INT UNSIGNED,
    `gradingStatus` ENUM('FULLYGRADED', 'REVISIONING', 'INCOMPLETE') DEFAULT 'INCOMPLETE',
    `handInTime` TIME,
    `handInDate` DATE,
    `test` INT UNSIGNED NOT NULL,
    `student` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`testID`),
    FOREIGN KEY (`test`)
     REFERENCES `SoStudyDB`.`Test`(`code`)
     ON DELETE CASCADE
     ON UPDATE CASCADE,
    FOREIGN KEY (`student`)
        REFERENCES `SoStudyDB`.`Student`(`email`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
)
    ENGINE = InnoDB;

DROP TABLE IF EXISTS `SoStudyDB`.`Rispsote` ;
CREATE TABLE `SoStudyDB`.`Risposte` (
    `code` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `textualContent` VARCHAR(250),
    `integerContent` INT UNSIGNED,
    `score` INT UNSIGNED,
    `attempt` INT UNSIGNED NOT NULL,
    `question` INT UNSIGNED NOT NULL,
    PRIMARY KEY (`code`),
    FOREIGN KEY (`attempt`) REFERENCES `SoStudyDB`.`Tentativo` (`testID`),
    FOREIGN KEY (`question`) REFERENCES `SoStudyDB`.`Domanda` (`code`),
    FOREIGN KEY (`integerContent`) REFERENCES `SoStudyDB`.`OpzioniDomande` (`code`)
)
ENGINE = InnoDB;

DROP TABLE IF EXISTS `SoStudyDB`.`Utenti` ;
CREATE TABLE `SoStudyDB`.`Utenti` (
    `email` VARCHAR(45) NOT NULL,
    `Password` VARCHAR(60) NOT NULL,
    `Ruolo` ENUM('PROFESSOR', 'STUDENT') NOT NULL,
    PRIMARY KEY (`email`)
)
    ENGINE = InnoDB;

DROP USER IF EXISTS 'admin_user'@'localhost';
CREATE USER 'admin_user'@'localhost' IDENTIFIED BY 'admin_user';
GRANT ALL PRIVILEGES ON `SoStudyDB`.* TO 'admin_user'@'localhost';
FLUSH PRIVILEGES;

USE `SoStudyDB`;

-- -----------------------------------------------------
-- 1. Inserimento Utenti (Email e ruoli allineati)
-- -----------------------------------------------------
INSERT INTO `Utenti` (`email`, `Password`, `Ruolo`) VALUES
('mario.rossi@gmail.com', '$2a$10$1ms55w0sVAfS7HR060REWe7Hv2do8sXtcSQ8F5.g5Sb0pHZj7/Gqy', 'PROFESSOR'),
('giuseppe.bianchi@gmail.com', '$2a$10$ep5OKfsCfKU8Y5cF8dP4VOmcxD/MEV..1opKsCqgH1ZoneZ6yj9Mi', 'PROFESSOR'),
('a.neri@studenti.it', MD5('qwerty'), 'STUDENT'),
('m.verdi@studenti.it', MD5('qwerty'), 'STUDENT'),
('g.gialli@studenti.it', MD5('qwerty'), 'STUDENT');

-- -----------------------------------------------------
-- 2. Inserimento Professori
-- -----------------------------------------------------
INSERT INTO `Professor` (`email`, `name`, `surname`) VALUES
('mario.rossi@gmail.com', 'Mario', 'Rossi'),
('giuseppe.bianchi@gmail.com', 'Giuseppe', 'Bianchi');

-- -----------------------------------------------------
-- 3. Inserimento Studenti
-- -----------------------------------------------------
INSERT INTO `Student` (`email`, `name`, `surname`) VALUES
('a.neri@studenti.it', 'Anna', 'Neri'),
('m.verdi@studenti.it', 'Marco', 'Verdi'),
('g.gialli@studenti.it', 'Giulia', 'Gialli');

-- -----------------------------------------------------
-- 4. Inserimento Classi
-- -----------------------------------------------------
INSERT INTO `Class` (`name`, `professor`) VALUES
('Basi di Dati', 'mario.rossi@gmail.com'),
('Sistemi Operativi', 'mario.rossi@gmail.com');

-- -----------------------------------------------------
-- 5. Inserimento Iscrizioni ClassStudents
-- -----------------------------------------------------
INSERT INTO `ClassStudents` (`student`, `class`) VALUES
('a.neri@studenti.it', 1),
('m.verdi@studenti.it', 1),
('a.neri@studenti.it', 2),
('g.gialli@studenti.it', 2);

-- -----------------------------------------------------
-- 6. Inserimento Test
-- -----------------------------------------------------
INSERT INTO `Test` (`name`, `dueDate`, `dueTime`, `duration`, `class`) VALUES
('Parziale SQL', '2023-11-15', '10:00:00', '02:00:00', 1),
('Appello Gennaio SO', '2024-01-20', '14:30:00', '01:30:00', 2);

-- -----------------------------------------------------
-- 7. Inserimento Domande
-- -----------------------------------------------------
INSERT INTO `Domanda` (`header`, `maxScore`, `type`, `test`) VALUES
('Scrivi la query per selezionare tutti gli studenti.', 15, 'OPENQUESTION', 1),
('Qual è il comando corretto per eliminare una tabella in SQL?', 5, 'CLOSEQUESTION', 1),
('Quale tra questi è uno stato valido per un processo?', 10, 'CLOSEQUESTION', 2);

-- -----------------------------------------------------
-- 8. Inserimento Opzioni Domande
-- -----------------------------------------------------
INSERT INTO `OpzioniDomande` (`content`, `isSolution`, `question`) VALUES
('DROP TABLE', TRUE, 2),
('DELETE TABLE', FALSE, 2),
('REMOVE TABLE', FALSE, 2),
('Pronto (Ready)', TRUE, 3),
('Compilato (Compiled)', FALSE, 3),
('Installato (Installed)', FALSE, 3);

-- -----------------------------------------------------
-- 9. Inserimento Tentativi
-- -----------------------------------------------------
INSERT INTO `Tentativo` (`grade`, `gradingStatus`, `handInTime`, `handInDate`, `test`, `student`) VALUES
(28, 'FULLYGRADED', '11:45:00', '2023-11-15', 1, 'a.neri@studenti.it'),
(NULL, 'INCOMPLETE', NULL, NULL, 1, 'm.verdi@studenti.it'),
(NULL, 'REVISIONING', '15:50:00', '2024-01-20', 2, 'a.neri@studenti.it');

-- -----------------------------------------------------
-- 10. Inserimento Risposte (Aggiunto il campo 'question' obbligatorio)
-- -----------------------------------------------------
INSERT INTO `Risposte` (`textualContent`, `integerContent`, `score`, `attempt`, `question`) VALUES
('SELECT * FROM Student;', NULL, 15, 1, 1),
(NULL, 1, 5, 1, 2),
(NULL, 4, NULL, 3, 3);