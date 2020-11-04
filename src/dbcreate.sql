-- teszt adatbázis a GUI-hoz (ListView, TableView)

CREATE DATABASE IF NOT EXISTS etr;

USE etr;

DROP TABLE IF EXISTS `felhasznalo`;

CREATE TABLE `felhasznalo`
(
    etr_kod  CHAR(11) PRIMARY KEY,
    password VARCHAR(50),
    vnev     NVARCHAR(20) NOT NULL,
    knev     NVARCHAR(20) NOT NULL,
    email    VARCHAR(100) NOT NULL UNIQUE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;

DROP TABLE IF EXISTS `oktato`;

CREATE TABLE `oktato`
(
    etr_kod CHAR(11)      NOT NULL,
    tanszek NVARCHAR(100) NOT NULL,
    PRIMARY KEY (etr_kod),
    FOREIGN KEY (etr_kod) REFERENCES `felhasznalo` (etr_kod)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;

DROP TABLE IF EXISTS `hallgato`;

CREATE TABLE `hallgato`
(
    etr_kod  CHAR(11)      NOT NULL,
    szak     NVARCHAR(100) NOT NULL,
    evfolyam YEAR          NOT NULL,
    PRIMARY KEY (etr_kod),
    FOREIGN KEY (etr_kod) REFERENCES `felhasznalo` (etr_kod)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;

DROP TABLE IF EXISTS `kurzus`;

CREATE TABLE `kurzus`
(
    kurzus_kod   CHAR(10)      NOT NULL,
    kredit_ertek SMALLINT      NOT NULL CHECK ( kredit_ertek >= 0 AND kredit_ertek <= 10 ),
    gyakorlat    BOOLEAN       NOT NULL DEFAULT FALSE,
    nev          NVARCHAR(100) NOT NULL UNIQUE,
    PRIMARY KEY (kurzus_kod)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;

# RAEPUL(**_kurzus_kod_**, **_kurzus_kod_mire_**)

DROP TABLE IF EXISTS `elofeltetele`;

CREATE TABLE `elofeltetele`
(
    kurzus_kod      CHAR(10) NOT NULL,
    kurzus_kod_felt CHAR(10) NOT NULL,
    PRIMARY KEY (kurzus_kod, kurzus_kod_felt),
    FOREIGN KEY (kurzus_kod) REFERENCES kurzus (kurzus_kod),
    FOREIGN KEY (kurzus_kod_felt) REFERENCES kurzus (kurzus_kod)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;



# INSERT INTO contact (name, nick_name, address, home_phone, work_phone, cell_phone, email, birthday, web_site, profession)
# VALUES ('Bruce Wayne', 'batman', 'XYZ Batcave', '9876543210', '6278287326', '9182872363', 'batman@gmail.com',
# '1976/02/03', 'batblog.com', 'Super Hero');
#
# INSERT INTO contact (name, nick_name, address, home_phone, work_phone, cell_phone, email, birthday, web_site, profession)
# VALUES ('Kiss Olivér', 'Oli', 'Röszke', '06708819797', '06708819797', '06708819797', 'K.Oliver.XCVII@gmail.com',
#         '1997/08/21', 'batblog.com', 'Programmer');
#
# INSERT INTO contact (name, nick_name, address, home_phone, work_phone, cell_phone, email, birthday, web_site, profession)
# VALUES ('Adamov Afrodita', 'Ditti', 'Röszke', '06205074638', '06205074638', '06205074638', 'Dittike1997@gmail.com',
#         '1997/09/19', 'batblog.com', 'Sociologist');