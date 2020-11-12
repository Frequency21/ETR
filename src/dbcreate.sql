-- teszt adatbázis a GUI-hoz (ListView, TableView)

# CREATE DATABASE IF NOT EXISTS etr;

USE etr;

CREATE TABLE IF NOT EXISTS `felhasznalo`
(
    etr_kod CHAR(11) PRIMARY KEY,
    vnev    VARCHAR(20)  NOT NULL,
    knev    VARCHAR(50)  NOT NULL,
    email   VARCHAR(100) NOT NULL UNIQUE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;

CREATE TABLE IF NOT EXISTS `oktato`
(
    etr_kod CHAR(11)     NOT NULL,
    tanszek VARCHAR(100) NOT NULL,
    PRIMARY KEY (etr_kod),
    FOREIGN KEY (etr_kod) REFERENCES `felhasznalo` (etr_kod)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;

CREATE TABLE IF NOT EXISTS `hallgato`
(
    etr_kod  CHAR(11)     NOT NULL,
    szak     VARCHAR(100) NOT NULL,
    evfolyam SMALLINT     NOT NULL,
    PRIMARY KEY (etr_kod),
    FOREIGN KEY (etr_kod) REFERENCES `felhasznalo` (etr_kod)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;

# ALTER TABLE kurzus MODIFY COLUMN DROP

CREATE TABLE IF NOT EXISTS `kurzus`
(
    kurzus_kod   CHAR(10)      NOT NULL,
    kredit_ertek SMALLINT      NOT NULL CHECK ( kredit_ertek >= 0 AND kredit_ertek <= 10 ),
    gyakorlat    BOOLEAN       NOT NULL DEFAULT FALSE,
    nev          NVARCHAR(100) NOT NULL UNIQUE,
    PRIMARY KEY (kurzus_kod)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;

CREATE TABLE IF NOT EXISTS `elofeltetele`
(
    kurzus_kod      CHAR(10) NOT NULL,
    kurzus_kod_felt CHAR(10) NOT NULL,
    PRIMARY KEY (kurzus_kod, kurzus_kod_felt),
    FOREIGN KEY (kurzus_kod) REFERENCES kurzus (kurzus_kod),
    FOREIGN KEY (kurzus_kod_felt) REFERENCES kurzus (kurzus_kod)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;

CREATE TABLE IF NOT EXISTS `terem`
(
    epulet_kod  VARCHAR(10) NOT NULL,
    terem_kod   VARCHAR(10) NOT NULL,
    kabinet     BOOLEAN DEFAULT FALSE,
    max_letszam SMALLINT    NOT NULL,
    PRIMARY KEY (epulet_kod, terem_kod)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;

CREATE TABLE IF NOT EXISTS `tanora`
(
    ev          SMALLINT    NOT NULL,
    felev       TINYINT     NOT NULL CHECK ( felev > 0 AND felev < 3 ),
    nap         ENUM ('hétfő', 'kedd', 'szerda', 'csütörtök', 'péntek'),
    kezdes      TIME        NOT NULL CHECK ( HOUR(kezdes) >= 8 AND HOUR(kezdes) <= 20 ),
    vegzes      TIME        NOT NULL CHECK ( HOUR(kezdes) >= 8 AND HOUR(kezdes) <= 20 ),
    max_letszam SMALLINT    NOT NULL CHECK ( max_letszam > 0 ),
    kurzus_kod  CHAR(10)    NOT NULL,
    epulet_kod  VARCHAR(10) NOT NULL,
    terem_kod   VARCHAR(10) NOT NULL,
    CHECK ( kezdes < vegzes ),
    PRIMARY KEY (ev, felev, nap, kezdes, kurzus_kod, epulet_kod, terem_kod),
    FOREIGN KEY (kurzus_kod) REFERENCES kurzus (kurzus_kod),
    FOREIGN KEY (epulet_kod, terem_kod) REFERENCES terem (epulet_kod, terem_kod)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;


# TART(**_OKTATO.etr_kod_**, **_ev_**, **_felev_**, **_nap_**,
#      **_kezdes_**, _kurzuskod_, **_epulet_kod_**, **_terem_kod_**)

CREATE TABLE IF NOT EXISTS `tart`
(
    etr_kod    CHAR(11)    NOT NULL,
    ev         SMALLINT    NOT NULL,
    felev      TINYINT     NOT NULL,
    nap        ENUM ('hétfő', 'kedd', 'szerda', 'csütörtök', 'péntek'),
    kezdes     TIME        NOT NULL,
    epulet_kod VARCHAR(10) NOT NULL,
    terem_kod  VARCHAR(10) NOT NULL,
    kurzus_kod CHAR(10)    NOT NULL,
    PRIMARY KEY (etr_kod, ev, felev, nap, kezdes, epulet_kod, terem_kod),
    FOREIGN KEY (etr_kod) REFERENCES oktato (etr_kod),
    FOREIGN KEY (ev, felev, nap, kezdes, kurzus_kod, epulet_kod, terem_kod)
        REFERENCES tanora (ev, felev, nap, kezdes, kurzus_kod, epulet_kod, terem_kod)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;

# FELVETT(erdemjegy, **_HALLGATO.etr_kod_**, **_ev_**, **_felev_**, **_nap_**,
#         **_kezdes_**, _kurzuskod_, **_epulet_kod_**, **_terem_kod_**)

CREATE TABLE IF NOT EXISTS `felvett`
(
    erdemjegy  SMALLINT DEFAULT NULL CHECK ( erdemjegy is NULL OR erdemjegy > 0 AND erdemjegy < 6),
    etr_kod    CHAR(11)    NOT NULL,
    ev         SMALLINT    NOT NULL,
    felev      TINYINT     NOT NULL,
    nap        ENUM ('hétfő', 'kedd', 'szerda', 'csütörtök', 'péntek'),
    kezdes     TIME        NOT NULL,
    epulet_kod VARCHAR(10) NOT NULL,
    terem_kod  VARCHAR(10) NOT NULL,
    kurzus_kod CHAR(10)    NOT NULL,
    PRIMARY KEY (etr_kod, ev, felev, nap, kezdes, epulet_kod, terem_kod),
    FOREIGN KEY (etr_kod) REFERENCES hallgato (etr_kod),
    FOREIGN KEY (ev, felev, nap, kezdes, kurzus_kod, epulet_kod, terem_kod)
        REFERENCES tanora (ev, felev, nap, kezdes, kurzus_kod, epulet_kod, terem_kod)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;

/*INSERT INTO felhasznalo
VALUES
('KIOYAAT.SZE', 'Kiss', 'Olivér', 'K.Oliver.XCVII@gmail.com'),
('ADDYAAT.SZE', 'Adamov', 'Afrodita', 'picilady97@gmail.com'),
('ARFYAAT.SZE', 'Árpás', 'Ferenc Dávid', 'Arpas.Ferenc@gmail.com'),
('TOPYAAT.SZE', 'Tóth', 'Patrik', 'gaz@citromai.hu'),
('VABYAAT.SZE', 'Vass', 'Bence', 'Vass.Bence@gmail.com');

INSERT INTO hallgato
VALUES
('KIOYAAT.SZE', 'Fizika BSc', '2016'),
('ADAYAAT.SZE', 'Fizika BSc', '2016'),
('ARFYAAT.SZE', 'Fizika BSc', '2016'),
('TOPYAAT.SZE', 'Fizika BSc', '2016'),
('VABYAAT.SZE', 'Fizika BSc', '2016');*/
