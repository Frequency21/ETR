CREATE DATABASE IF NOT EXISTS etr;

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
    CONSTRAINT fk_oktato_felhasznalo FOREIGN KEY (etr_kod) REFERENCES `felhasznalo` (etr_kod)
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
    CONSTRAINT fk_hallgato_felhasznalo FOREIGN KEY (etr_kod) REFERENCES `felhasznalo` (etr_kod)
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
    CONSTRAINT fk_kurzus_oktato FOREIGN KEY (etr_kod) REFERENCES `oktato` (etr_kod)
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
    CONSTRAINT fk_elofeltetele_kurzus FOREIGN KEY (kurzus_kod) REFERENCES kurzus (kurzus_kod)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_elofelt_elofelt FOREIGN KEY (kurzus_kod_felt) REFERENCES kurzus (kurzus_kod)
        ON DELETE CASCADE ON UPDATE CASCADE
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
    CONSTRAINT begin_lt_end CHECK ( kezdes < vegzes ),
    PRIMARY KEY (ev, felev, nap, kezdes, epulet_kod, terem_kod),
    UNIQUE (ev, felev, nap, vegzes, epulet_kod, terem_kod),
    CONSTRAINT fk_tanora_kurzus FOREIGN KEY (kurzus_kod) REFERENCES kurzus (kurzus_kod)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_tanora_terem FOREIGN KEY (epulet_kod, terem_kod) REFERENCES terem (epulet_kod, terem_kod)
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
    CONSTRAINT fk_tart_oktato FOREIGN KEY (etr_kod) REFERENCES oktato (etr_kod)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_tart_tanora FOREIGN KEY (ev, felev, nap, kezdes, epulet_kod, terem_kod)
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
    CONSTRAINT fk_felvett_hallgato FOREIGN KEY (etr_kod) REFERENCES hallgato (etr_kod),
    CONSTRAINT fk_felvett_tanora FOREIGN KEY (ev, felev, nap, kezdes, epulet_kod, terem_kod)
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
       ('LAAYAAT.SZE', 'László', 'Alexandra', 'laszlo.alexandra@gmail.com'),
       ('JUBYAAT.SZE', 'Juhász', 'Bence', 'bencebtgk@gmail.com'),
       ('TOBYAAT.SZE', 'Tóth', 'Bálint', 'bobo@gmail.com'),
       ('MUAYAAT.SZE', 'Murvai', 'Adrián', 'macska@gmail.com'),
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
       ('HOZYAAT.SZE', 'Horváth', 'Zoltán', 'z.horvath@physx.u-szeged.hu'),
       ('FELYAAT.SZE', 'Fehér', 'László', 'lfeher@phyx.u-szeged.hu'),
       ('BEMYAAT.SZE', 'Benedict', 'Mihály', 'benedict@physx.u-szeged.hu'),
       ('GYIYAAT.SZE', 'Gyémánt', 'Iván', 'gyemant@physx.u-szeged.hu'),
       ('GOTYAAT.SZE', 'Görbe', 'Tamás Ferenc', 'tfgorbe@physx.u-szeged.hu'),
       ('IGFYABT.SZE', 'Iglói', 'Ferenc', 'igloi@szfki.hu')
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
       ('VABYAAT.SZE', 'Fizika BSc', '2016'),
       ('LAAYAAT.SZE', 'Fizika BSc', '2016'),
       ('JUBYAAT.SZE', 'Fizika BSc', '2016'),
       ('TOBYAAT.SZE', 'Fizika BSc', '2016'),
       ('MUAYAAT.SZE', 'Fizika BSc', '2016')
       ;

DELETE FROM oktato WHERE TRUE;

INSERT INTO oktato
VALUES ('GEAYAAT.SZE', 'Optikai és Kvantumelekronikai Tanszék'),
       ('IGFYAAT.SZE', 'Optikai és Kvantumelekronikai Tanszék'),
       ('HOZYAAT.SZE', 'Optikai és Kvantumelekronikai Tanszék'),
       ('SZGYAAT.SZE', 'Elméleti Fizikai Tanszék'),
       ('FELYAAT.SZE', 'Elméleti Fizikai Tanszék'),
       ('BEMYAAT.SZE', 'Elméleti Fizikai Tanszék'),
       ('GYIYAAT.SZE', 'Elméleti Fizikai Tanszék'),
       ('GOTYAAT.SZE', 'Elméleti Fizikai Tanszék'),
       ('IGFYABT.SZE', 'Elméleti Fizikai Tanszék')
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
       ('FBN414G', 1, TRUE, 'Elektromágnesség és relativitáselmélet', 'GEAYAAT.SZE'),
       ('FMBN108E', 2, FALSE, 'Lineáris algebra fizikusoknak', 'FELYAAT.SZE'),
       ('FMBN108G', 2, TRUE, 'Lineáris algebra fizikusoknak', 'FELYAAT.SZE'),
       ('FBN218E', 2, FALSE, 'Matematikai módszerek a fizikában', 'GOTYAAT.SZE'),
       ('FBN218G', 2, TRUE, 'Matematikai módszerek a fizikában gy.', 'GOTYAAT.SZE'),
       ('FBN513E', 3, FALSE, 'Statisztikus fizika alapjai', 'IGFYABT.SZE'),
       ('FBN513G', 2, TRUE, 'Statisztikus fizika alapjai gyakorlat', 'IGFYABT.SZE')
       ;

delete from elofeltetele where true;

insert into elofeltetele
values ('FBN202E', 'FBN101E'), /* hullámtan <-- mechanika */
       ('FBN304E', 'FBN202E'), /* elektro <-- hullámtan */
       ('FBN414E', 'FBN304E'), /* relativitás <-- elektro */
       ('FBN414E', 'FBN311E'), /* relativitás <-- elm mecha */
       ('FBN513E', 'FBN218E') /* statfiz <-- matmód */
       ;

delete from terem where true;

insert into terem
values
#        fizikás termek
       ('DOMFI', '101', false, 150),
       ('BOFI', '203', false, 60),
       ('BOFI', '204', false, 70),
       ('BOFI', '205', false, 60),
       ('BOFI', '206', false, 70)
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
       (2017, 2, 'hétfő', '8:00', '10:00', 50, 'FBN414G', 'BOFI', '203'),
       (2016, 1, 'hétfő', '10:00', '12:00', 100, 'FMBN108E', 'BOFI', '205'), /* linalg */
       (2016, 1, 'kedd', '16:00', '18:00', 50, 'FMBN108G', 'BOFI', '203'),
       (2016, 1, 'kedd', '8:00', '10:00', 50, 'FMBN108G', 'BOFI', '204'),
       (2017, 1, 'péntek', '13:00', '14:00', 60, 'FBN218E', 'BOFI', '204'), /* matmód */
       (2017, 1, 'csütörtök', '10:00', '12:00', 50, 'FBN218G', 'BOFI', '205'),
       (2018, 1, 'szerda', '18:00', '20:00', 50, 'FBN513E', 'BOFI', '206'), /* statfiz */
       (2018, 1, 'kedd', '12:00', '14:00', 50, 'FBN513G', 'BOFI', '204')
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
       ('GEAYAAT.SZE', 2017, 2, 'hétfő', '8:00', 'BOFI', '203'),
       ('FELYAAT.SZE', 2016, 1, 'hétfő', '10:00', 'BOFI', '205'), /* linalg */
       ('FELYAAT.SZE', 2016, 1, 'kedd', '16:00', 'BOFI', '203'),
       ('FELYAAT.SZE', 2016, 1, 'kedd', '8:00', 'BOFI', '204'),
       ('GOTYAAT.SZE', 2017, 1, 'péntek', '13:00', 'BOFI', '204'), /* matmód */
       ('GOTYAAT.SZE', 2017, 1, 'csütörtök', '10:00', 'BOFI', '205'),
       ('IGFYABT.SZE', 2018, 1, 'szerda', '18:00', 'BOFI', '206'), /* statfiz */
       ('IGFYABT.SZE', 2018, 1, 'kedd', '12:00', 'BOFI', '204')
       ;

insert into felvett
values
#        Ferenc
       (5, 'ARFYAAT.SZE', 2016, 1, 'hétfő', '10:00', 'DOMFI', '101'), /* mecha */
       (5, 'ARFYAAT.SZE', 2016, 1, 'szerda', '12:00', 'BOFI', '203'),
       (5, 'ARFYAAT.SZE', 2016, 2, 'kedd', '10:00', 'DOMFI', '101'), /* hullámtan */
       (5, 'ARFYAAT.SZE', 2016, 2, 'csütörtök', '14:00', 'BOFI', '204'),
       (5, 'ARFYAAT.SZE', 2017, 1, 'péntek', '10:00', 'DOMFI', '101'), /* elektro */
       (5, 'ARFYAAT.SZE', 2017, 1, 'kedd', '14:00', 'BOFI', '204'),
       (5, 'ARFYAAT.SZE', 2017, 1, 'hétfő', '12:00', 'DOMFI', '101'), /* elm mecha */
       (4, 'ARFYAAT.SZE', 2017, 1, 'kedd', '12:00', 'BOFI', '205'),
       (5, 'ARFYAAT.SZE', 2017, 2, 'szerda', '14:00', 'BOFI', '205'), /* rel elm */
       (5, 'ARFYAAT.SZE', 2017, 2, 'hétfő', '8:00', 'BOFI', '203'),
       (4, 'ARFYAAT.SZE', 2016, 1, 'hétfő', '10:00', 'BOFI', '205'), /* linalg */
       (5, 'ARFYAAT.SZE', 2016, 1, 'kedd', '8:00', 'BOFI', '204'),
       (5, 'ARFYAAT.SZE', 2017, 1, 'péntek', '13:00', 'BOFI', '204'), /* matmód */
       (4, 'ARFYAAT.SZE', 2017, 1, 'csütörtök', '10:00', 'BOFI', '205'),
       (4, 'ARFYAAT.SZE', 2018, 1, 'szerda', '18:00', 'BOFI', '206'), /* statfiz */
       (5, 'ARFYAAT.SZE', 2018, 1, 'kedd', '12:00', 'BOFI', '204'),
#        Patrik
       (3, 'TOPYAAT.SZE', 2016, 1, 'hétfő', '10:00', 'DOMFI', '101'), /* mecha */
       (2, 'TOPYAAT.SZE', 2016, 1, 'szerda', '12:00', 'BOFI', '203'),
       (4, 'TOPYAAT.SZE', 2016, 2, 'kedd', '10:00', 'DOMFI', '101'), /* hullámtan */
       (3, 'TOPYAAT.SZE', 2016, 2, 'csütörtök', '14:00', 'BOFI', '204'),
       (4, 'TOPYAAT.SZE', 2017, 1, 'péntek', '10:00', 'DOMFI', '101'), /* elektro */
       (3, 'TOPYAAT.SZE', 2017, 1, 'kedd', '14:00', 'BOFI', '204'),
       (3, 'TOPYAAT.SZE', 2017, 1, 'hétfő', '12:00', 'DOMFI', '101'), /* elm mecha */
       (2, 'TOPYAAT.SZE', 2017, 1, 'kedd', '12:00', 'BOFI', '205'),
       (4, 'TOPYAAT.SZE', 2017, 2, 'szerda', '14:00', 'BOFI', '205'), /* rel elm */
       (5, 'TOPYAAT.SZE', 2017, 2, 'hétfő', '8:00', 'BOFI', '203'),
       (4, 'TOPYAAT.SZE', 2016, 1, 'hétfő', '10:00', 'BOFI', '205'), /* linalg */
       (4, 'TOPYAAT.SZE', 2016, 1, 'kedd', '8:00', 'BOFI', '204'),
       (3, 'TOPYAAT.SZE', 2017, 1, 'péntek', '13:00', 'BOFI', '204'), /* matmód */
       (3, 'TOPYAAT.SZE', 2017, 1, 'csütörtök', '10:00', 'BOFI', '205'),
       (3, 'TOPYAAT.SZE', 2018, 1, 'szerda', '18:00', 'BOFI', '206'), /* statfiz */
       (4, 'TOPYAAT.SZE', 2018, 1, 'kedd', '12:00', 'BOFI', '204')
       ;

/*
hallgato -> felvett -> tanora -> kurzus
    select on etr_code
                select on kurzus_kod
*/

select f.etr_kod, f2.vnev, f2.knev, avg(erdemjegy) as atlag, sum(kredit_ertek * erdemjegy) / sum(kredit_ertek) as KKI,
       sum(kredit_ertek) as `ossz kredit` from felvett as f
inner join tanora t on
f.ev = t.ev and f.felev = t.felev and t.nap = f.nap and t.kezdes = f.kezdes and
f.epulet_kod = t.epulet_kod and f.terem_kod = t.terem_kod
inner join kurzus k on t.kurzus_kod = k.kurzus_kod
left join felhasznalo f2 on f.etr_kod = f2.etr_kod
group by f.etr_kod;

select f.etr_kod,
       f2.vnev,
       f2.knev,
       avg(erdemjegy)                                    as atlag,
       sum(kredit_ertek * erdemjegy) / sum(kredit_ertek) as KKI,
       sum(kredit_ertek)                                 as `ossz kredit`
from felvett as f
         natural join tanora t
         inner join kurzus k on t.kurzus_kod = k.kurzus_kod
         left join felhasznalo f2 on f.etr_kod = f2.etr_kod
where erdemjegy != 1
group by f.etr_kod;

select etr_kod, vnev, knev, avg(erdemjegy) as `hagy. atlag`, sum(kredit_ertek) as `ossz kredit`
from felvett natural join tanora natural join kurzus natural join felhasznalo
group by etr_kod;

select */*hallgato.etr_kod, vnev, knev, tanora.kurzus_kod*/
from felhasznalo
natural join hallgato
natural join felvett
natural join tanora
left join kurzus k on k.kurzus_kod = tanora.kurzus_kod
order by hallgato.etr_kod
;

select * from oktato o
inner join tart t on o.etr_kod = t.etr_kod
inner join tanora tan on
t.ev = tan.ev and t.felev = tan.felev and t.nap = tan.nap and
t.kezdes = tan.kezdes and t.epulet_kod = tan.epulet_kod and t.terem_kod = tan.terem_kod
;

/*
 Melyik oktató milyen órákat tart
 */
select etr_kod as `oktato`, ev, felev, nap, kezdes, vegzes, epulet_kod, terem_kod
from oktato o natural join tart t
natural join tanora tan
order by ev, felev, nap, kezdes
;

/*
 ugyanez, csak left joinnal
 */
select */*tan.kurzus_kod, o.etr_kod as oktató, ev, felev, nap, kezdes, vegzes, epulet_kod, terem_kod*/
from oktato o natural join tart t
natural join tanora tan
left join kurzus k on k.kurzus_kod = tan.kurzus_kod
order by ev, felev, nap, kezdes
;

/*
 Adott évben, félévben és napon hány órát tartottak meg
 */
select ev, felev, nap, count(kezdes) as freq
from oktato o natural join tart t
natural join tanora tan
group by ev, felev, nap
order by ev, felev, nap
;

/*
 Adott évben félévben, napon és órában hány óra kezdődött
 */
select ev, felev, nap, count(kezdes) as freq
from oktato o natural join tart t
natural join tanora tan
group by ev, felev, nap, kezdes
order by ev, felev, nap, kezdes
;

select * from
tanora
where terem_kod = '205' and epulet_kod = 'BOFI'
  and felev = 1 and ev = 2016 and nap = 'hétfő'
  and ((kezdes between '10:00' and '11:00') or vegzes between '10:00' and '12:00');

insert into felvett
values #        Kristóf
       (5, 'NAKYAAT.SZE', 2016, 1, 'hétfő', '10:00', 'DOMFI', '101'), /* mecha */
       (5, 'NAKYAAT.SZE', 2016, 1, 'szerda', '12:00', 'BOFI', '203'),
       (5, 'NAKYAAT.SZE', 2016, 2, 'kedd', '10:00', 'DOMFI', '101'), /* hullámtan */
       (5, 'NAKYAAT.SZE', 2016, 2, 'csütörtök', '14:00', 'BOFI', '204'),
       (5, 'NAKYAAT.SZE', 2017, 1, 'péntek', '10:00', 'DOMFI', '101'), /* elektro */
       (5, 'NAKYAAT.SZE', 2017, 1, 'kedd', '14:00', 'BOFI', '204');

# Listázzuk a nem teljesített előfeltételeket (adott hallgató és kurzus)
SELECT kurzus_kod_felt
FROM elofeltetele
where kurzus_kod = 'FBN414E' and elofeltetele.kurzus_kod_felt NOT IN
                         (SELECT tanora.kurzus_kod
                          FROM felvett natural join tanora
                          WHERE etr_kod = 'NAKYAAT.SZE');