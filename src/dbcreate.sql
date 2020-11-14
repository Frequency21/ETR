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
        ON DELETE CASCADE ON UPDATE CASCADE
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
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;


/*
 Ha az oktatót töröljük, akkor a kurzusfelelős etr_kod-ja legyen null.
 Ha létrehozunk egy kurzust akkor még nincs tárgyfelelőse.
 */
CREATE TABLE IF NOT EXISTS `kurzus`
(
    kurzus_kod   VARCHAR(10)   NOT NULL,
    kredit_ertek TINYINT       NOT NULL CHECK ( kredit_ertek >= 0 AND kredit_ertek <= 10 ),
    gyakorlat    BOOLEAN       NOT NULL DEFAULT FALSE,
    nev          NVARCHAR(100) NOT NULL,
    etr_kod      CHAR(11)               DEFAULT NULL,
    PRIMARY KEY (kurzus_kod),
    FOREIGN KEY (etr_kod) REFERENCES `oktato` (etr_kod)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;

/*
 Ha bármelyik kurzus is törlésre kerül,
 akkor nem érdemes eltárolni
 */
CREATE TABLE IF NOT EXISTS `elofeltetele`
(
    kurzus_kod      VARCHAR(10) NOT NULL,
    kurzus_kod_felt VARCHAR(10) NOT NULL,
    PRIMARY KEY (kurzus_kod, kurzus_kod_felt),
    FOREIGN KEY (kurzus_kod) REFERENCES kurzus (kurzus_kod)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (kurzus_kod_felt) REFERENCES kurzus (kurzus_kod)
        ON DELETE RESTRICT ON UPDATE CASCADE
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
    kurzus_kod  VARCHAR(10) NOT NULL,
    epulet_kod  VARCHAR(10) NOT NULL,
    terem_kod   VARCHAR(10) NOT NULL,
    CHECK ( kezdes < vegzes ),
    PRIMARY KEY (ev, felev, nap, kezdes, epulet_kod, terem_kod),
    UNIQUE (ev, felev, nap, vegzes, epulet_kod, terem_kod),
    FOREIGN KEY (kurzus_kod) REFERENCES kurzus (kurzus_kod)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (epulet_kod, terem_kod) REFERENCES terem (epulet_kod, terem_kod)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;


CREATE TABLE IF NOT EXISTS `tart`
(
    etr_kod    CHAR(11)    NOT NULL,
    ev         SMALLINT    NOT NULL,
    felev      TINYINT     NOT NULL,
    nap        ENUM ('hétfő', 'kedd', 'szerda', 'csütörtök', 'péntek'),
    kezdes     TIME        NOT NULL,
    epulet_kod VARCHAR(10) NOT NULL,
    terem_kod  VARCHAR(10) NOT NULL,
    PRIMARY KEY (etr_kod, ev, felev, nap, kezdes, epulet_kod, terem_kod),
    FOREIGN KEY (etr_kod) REFERENCES oktato (etr_kod)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (ev, felev, nap, kezdes, epulet_kod, terem_kod)
        REFERENCES tanora (ev, felev, nap, kezdes, epulet_kod, terem_kod)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;


CREATE TABLE IF NOT EXISTS `felvett`
(
    erdemjegy  TINYINT DEFAULT NULL CHECK ( erdemjegy is NULL OR erdemjegy > 0 AND erdemjegy < 6),
    etr_kod    CHAR(11)    NOT NULL,
    ev         SMALLINT    NOT NULL,
    felev      TINYINT     NOT NULL,
    nap        ENUM ('hétfő', 'kedd', 'szerda', 'csütörtök', 'péntek'),
    kezdes     TIME        NOT NULL,
    epulet_kod VARCHAR(10) NOT NULL,
    terem_kod  VARCHAR(10) NOT NULL,
    PRIMARY KEY (etr_kod, ev, felev, nap, kezdes, epulet_kod, terem_kod),
    FOREIGN KEY (etr_kod) REFERENCES hallgato (etr_kod),
    FOREIGN KEY (ev, felev, nap, kezdes, epulet_kod, terem_kod)
        REFERENCES tanora (ev, felev, nap, kezdes, epulet_kod, terem_kod)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_hungarian_ci;

INSERT INTO felhasznalo
VALUES
#        fizikusok
#        hallgatók
       ('NAKYAAT.SZE', 'Nagy', 'Kristóf', 'nagy.kristof@gmail.com'),
       ('JUDYAAT.SZE', 'Juhász', 'Dániel', 'juh.dan@gmail.com'),
       ('VUKYAAT.SZE', 'Vukovic', 'Krisztina', 'vukriszt@gmail.com'),
       ('KIOYAAT.SZE', 'Kiss', 'Olivér', 'K.Oliver.XCVII@gmail.com'),
       ('ADAYAAT.SZE', 'Adamov', 'Afrodita', 'picilady97@gmail.com'),
       ('ARFYAAT.SZE', 'Árpás', 'Ferenc Dávid', 'Arpas.Ferenc@gmail.com'),
       ('TOPYAAT.SZE', 'Tóth', 'Patrik', 'gaz@citromai.hu'),
       ('VABYAAT.SZE', 'Vass', 'Bence', 'Vass.Bence@gmail.com'),
#        oktatók
       ('GEAYAAT.SZE', 'Gergely', 'Árpád László', 'gergal@physx.u-szeged.com'),
       ('SZGYAAT.SZE', 'Szabó', 'Gábor', 'gszabo@physx.u-szeged.hu'),
       ('IGFYAAT.SZE', 'Ignácz', 'Ferenc', 'ignacz@physx.u-szeged.hu'),
       ('HOZYAAT.SZE', 'Horváth', 'Zoltán', 'z.horvath@physx.u-szeged.hu')
#        informatikusok
       ;

DELETE FROM hallgato WHERE TRUE;

INSERT INTO hallgato
VALUES
#        fizikusok
       ('NAKYAAT.SZE', 'Fizika BSc', '2016'),
       ('JUDYAAT.SZE', 'Fizika BSc', '2016'),
       ('VUKYAAT.SZE', 'Fizika BSc', '2016'),
       ('KIOYAAT.SZE', 'Fizika BSc', '2016'),
       ('ADAYAAT.SZE', 'Fizika BSc', '2016'),
       ('ARFYAAT.SZE', 'Fizika BSc', '2016'),
       ('TOPYAAT.SZE', 'Fizika BSc', '2016'),
       ('VABYAAT.SZE', 'Fizika BSc', '2016');

DELETE FROM oktato WHERE TRUE;

INSERT INTO oktato
VALUES ('GEAYAAT.SZE', 'Optikai és Kvantumelekronikai Tanszék'),
       ('IGFYAAT.SZE', 'Optikai és Kvantumelekronikai Tanszék'),
       ('HOZYAAT.SZE', 'Optikai és Kvantumelekronikai Tanszék'),
       ('SZGYAAT.SZE', 'Elméleti Fizikai Tanszék')
       ;

delete from kurzus where true;

INSERT INTO kurzus
VALUES ('FBN101E', 4, FALSE, 'Mechanika', 'SZGYAAT.SZE'),
       ('FBN101G', 2, TRUE, 'Mechanika', 'IGFYAAT.SZE'),
       ('FBN202E', 3, FALSE, 'Hullámtan és optika', 'HOZYAAT.SZE'),
       ('FBN202G', 2, TRUE, 'Hullámtan és optika gy.', 'HOZYAAT.SZE'),
       ('FBN304E', 4, FALSE, 'Elektromágnességtan', 'HOZYAAT.SZE'),
       ('FBN304G', 2, TRUE, 'Elektromágnességtan gy.', 'HOZYAAT.SZE'),
       ('FBN311E', 3, FALSE, 'Elméleti Mechanika', 'HOZYAAT.SZE'),
       ('FBN311G', 1, TRUE, 'Elméleti Mechanika', 'HOZYAAT.SZE'),
       ('FBN414E', 2, FALSE, 'Elektromágnesség és relativitáselmélet', 'GEAYAAT.SZE'),
       ('FBN414G', 1, TRUE, 'Elektromágnesség és relativitáselmélet', 'GEAYAAT.SZE')
       ;

delete from elofeltetele where true;

insert into elofeltetele
values ('FBN202E', 'FBN101E'), /* hullámtan <-- mechanika */
       ('FBN304E', 'FBN202E'), /* elektro <-- hullámtan */
       ('FBN414E', 'FBN304E'), /* relativitás <-- elektro */
       ('FBN414E', 'FBN311E'); /* relativitás <-- elm mecha */
       ;

delete from terem where true;

insert into terem
values
#        fizikás termek
       ('DOMFI', '101', false, 150),
       ('BOFI', '203', false, 60),
       ('BOFI', '204', false, 60),
       ('BOFI', '205', false, 60),
       ('BOFI', '206', false, 60)
       ;

delete from tanora where true;

insert into tanora
values
#        fizikás tanórák
       (2016, 1, 'hétfő', '10:00', '12:00', 100, 'FBN101E', 'DOMFI', '101'), /* mecha */
       (2016, 1, 'szerda', '12:00', '14:00', 50, 'FBN101G', 'BOFI', '203'),
       (2016, 1, 'szerda', '10:00', '12:00', 50, 'FBN101G', 'BOFI', '203'),
       (2016, 2, 'kedd', '10:00', '12:00', 100, 'FBN202E', 'DOMFI', '101'), /* hullámtan */
       (2016, 2, 'csütörtök', '14:00', '15:00', 50, 'FBN202G', 'BOFI', '204'),
       (2016, 2, 'csütörtök', '13:00', '14:00', 50, 'FBN202G', 'BOFI', '204'),
       (2017, 1, 'péntek', '10:00', '12:00', 100, 'FBN304E', 'DOMFI', '101'), /* elektro */
       (2017, 1, 'kedd', '14:00', '16:00', 50, 'FBN304G', 'BOFI', '204'),
       (2017, 1, 'szerda', '12:00', '14:00', 50, 'FBN304G', 'BOFI', '206'),
       (2017, 1, 'hétfő', '12:00', '14:00', 100, 'FBN311E', 'DOMFI', '101'), /* elm mecha */
       (2017, 1, 'kedd', '12:00', '14:00', 50, 'FBN311G', 'BOFI', '205'),
       (2017, 1, 'kedd', '12:00', '14:00', 50, 'FBN311G', 'BOFI', '206'),
       (2017, 2, 'szerda', '14:00', '16:00', 50, 'FBN414E', 'BOFI', '205'), /* rel elm */
       (2017, 2, 'hétfő', '12:00', '14:00', 50, 'FBN414G', 'BOFI', '203')
       ;

delete from tart where true;

insert into tart
values
#        fizikás
       ('SZGYAAT.SZE', 2016, 1, 'hétfő', '10:00', 'DOMFI', '101'), /* mecha */
       ('HOZYAAT.SZE', 2016, 1, 'szerda', '12:00', 'BOFI', '203'),
       ('IGFYAAT.SZE', 2016, 1, 'szerda', '10:00', 'BOFI', '203'),
       ('HOZYAAT.SZE', 2016, 2, 'kedd', '10:00', 'DOMFI', '101'), /* hullámtan */
       ('HOZYAAT.SZE', 2016, 2, 'csütörtök', '13:00', 'BOFI', '204'), /* a gyakorlatokat */
       ('IGFYAAT.SZE', 2016, 2, 'csütörtök', '13:00', 'BOFI', '204'), /* ketten tartják */
       ('HOZYAAT.SZE', 2016, 2, 'csütörtök', '14:00', 'BOFI', '204'),
       ('IGFYAAT.SZE', 2016, 2, 'csütörtök', '14:00', 'BOFI', '204'),
       ('HOZYAAT.SZE', 2017, 1, 'péntek', '10:00', 'DOMFI', '101'), /* elektro */
       ('HOZYAAT.SZE', 2017, 1, 'kedd', '14:00', 'BOFI', '204'),
       ('SZGYAAT.SZE', 2017, 1, 'szerda', '12:00','BOFI', '206'),
       ('SZGYAAT.SZE', 2017, 1, 'hétfő', '12:00', 'DOMFI', '101'), /* elm mecha */
       ('IGFYAAT.SZE', 2017, 1, 'kedd', '12:00', 'BOFI', '205'),
       ('HOZYAAT.SZE', 2017, 1, 'kedd', '12:00', 'BOFI', '206'),
       ('GEAYAAT.SZE', 2017, 2, 'szerda', '14:00', 'BOFI', '205'), /* rel elm */
       ('GEAYAAT.SZE', 2017, 2, 'hétfő', '12:00', 'BOFI', '203')
       ;

insert into felvett
values
       (5, 'ARFYAAT.SZE', 2016, 1, 'hétfő', '10:00', 'DOMFI', '101'), /* mecha */
       (5, 'ARFYAAT.SZE', 2016, 1, 'szerda', '12:00', 'BOFI', '203'),
       (5, 'ARFYAAT.SZE', 2016, 2, 'kedd', '10:00', 'DOMFI', '101'), /* hullámtan */
       (5, 'ARFYAAT.SZE', 2016, 2, 'csütörtök', '13:00', 'BOFI', '204'),
       (5, 'ARFYAAT.SZE', 2017, 1, 'péntek', '10:00', 'DOMFI', '101'), /* elektro */
       (5, 'ARFYAAT.SZE', 2017, 1, 'szerda', '12:00','BOFI', '206'),
       (5, 'ARFYAAT.SZE', 2017, 1, 'hétfő', '12:00', 'DOMFI', '101'), /* elm mecha */
       (5, 'ARFYAAT.SZE', 2017, 1, 'kedd', '12:00', 'BOFI', '205'),
       (5, 'ARFYAAT.SZE', 2017, 2, 'szerda', '14:00', 'BOFI', '205'), /* rel elm */
       (5, 'ARFYAAT.SZE', 2017, 2, 'hétfő', '12:00', 'BOFI', '203'),
       (5, 'TOPYAAT.SZE', 2016, 1, 'hétfő', '10:00', 'DOMFI', '101'), /* mecha */
       (4, 'TOPYAAT.SZE', 2016, 1, 'szerda', '12:00', 'BOFI', '203'),
       (4, 'TOPYAAT.SZE', 2016, 2, 'kedd', '10:00', 'DOMFI', '101'), /* hullámtan */
       (3, 'TOPYAAT.SZE', 2016, 2, 'csütörtök', '13:00', 'BOFI', '204'),
       (4, 'TOPYAAT.SZE', 2017, 1, 'péntek', '10:00', 'DOMFI', '101'), /* elektro */
       (4, 'TOPYAAT.SZE', 2017, 1, 'szerda', '12:00','BOFI', '206'),
       (2, 'TOPYAAT.SZE', 2017, 1, 'hétfő', '12:00', 'DOMFI', '101'), /* elm mecha */
       (5, 'TOPYAAT.SZE', 2017, 1, 'kedd', '12:00', 'BOFI', '205'),
       (4, 'TOPYAAT.SZE', 2017, 2, 'szerda', '14:00', 'BOFI', '205'), /* rel elm */
       (5, 'TOPYAAT.SZE', 2017, 2, 'hétfő', '12:00', 'BOFI', '203')
       ;
