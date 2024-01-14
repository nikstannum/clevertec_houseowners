--liquibase formatted sql

-- changeset fill tables:semeniuk

INSERT INTO countries ("name")
VALUES ('BELARUS'),
('USA'),
('POLAND'),
('RUSSIA'),
('UKRAINE'),
('LATVIA')
;

INSERT INTO sex("name")
VALUES ('MALE'),
('FEMALE');

INSERT INTO addresses (country_id, city, street, "number")
VALUES
((SELECT id FROM countries WHERE "name" = 'BELARUS'), 'Minsk', 'Lenina', '11 A'),
((SELECT id FROM countries WHERE "name" = 'BELARUS'), 'Minsk', 'Nezalezhnasty', '117/1'),
((SELECT id FROM countries WHERE "name" = 'BELARUS'), 'Minsk', 'Brovky', '22'),
((SELECT id FROM countries WHERE "name" = 'BELARUS'), 'Minsk', 'Gazety "Pravda"', '7/2'),
((SELECT id FROM countries WHERE "name" = 'USA'), 'Vashington', 'Pennsylvania Avenue', '1600'),
((SELECT id FROM countries WHERE "name" = 'POLAND'), 'Krakow', 'Grodzka', '123'),
((SELECT id FROM countries WHERE "name" = 'RUSSIA'), 'Moscow', 'Rublevo-Uspenskoe highway', '777'),
((SELECT id FROM countries WHERE "name" = 'UKRAINE'), 'Kiev', 'Bankovya', '1'),
((SELECT id FROM countries WHERE "name" = 'LATVIA'), 'Riga', 'Rozena', '17'),
((SELECT id FROM countries WHERE "name" = 'USA'), 'New York', 'Brodway', '114');

INSERT INTO houses ("uuid", "area", address_id)
VALUES
(gen_random_uuid(), 152.1, (SELECT get_address_id('BELARUS', 'Minsk', 'Lenina','11 A'))),
(gen_random_uuid(), 117, (SELECT get_address_id('BELARUS', 'Minsk', 'Nezalezhnasty', '117/1'))),
(gen_random_uuid(), 87.6, (SELECT get_address_id('BELARUS', 'Minsk', 'Brovky', '22'))),
(gen_random_uuid(), 56.5, (SELECT get_address_id('BELARUS', 'Minsk', 'Gazety "Pravda"', '7/2'))),
(gen_random_uuid(), 5100, (SELECT get_address_id('USA', 'Vashington', 'Pennsylvania Avenue', '1600'))),
(gen_random_uuid(), 162, (SELECT get_address_id('POLAND', 'Krakow', 'Grodzka', '123'))),
(gen_random_uuid(), 1520, (SELECT get_address_id('RUSSIA', 'Moscow', 'Rublevo-Uspenskoe highway', '777'))),
(gen_random_uuid(), 237.8, (SELECT get_address_id('UKRAINE', 'Kiev', 'Bankovya', '1'))),
(gen_random_uuid(), 95.1, (SELECT get_address_id('LATVIA', 'Riga', 'Rozena', '17'))),
(gen_random_uuid(), 251.1, (SELECT get_address_id('USA', 'New York', 'Brodway', '114')));

INSERT INTO persons ("uuid", "name", surname, sex_id, passport_series, passport_number, house_id)
VALUES
(gen_random_uuid(), 'Ivan', 'Ivanov', (SELECT id FROM sex WHERE "name" = 'MALE'), 'KH', 114789, (SELECT get_house_id('BELARUS', 'Minsk', 'Lenina', '11 A'))),
(gen_random_uuid(), 'Petr', 'Petrov', (SELECT id FROM sex WHERE "name" = 'MALE'), 'KH', 543789, (SELECT get_house_id('BELARUS', 'Minsk', 'Lenina', '11 A'))),
(gen_random_uuid(), 'Sergey', 'Vladimirov', (SELECT id FROM sex WHERE "name" = 'MALE'), 'MP', 114789, (SELECT get_house_id('BELARUS', 'Minsk', 'Nezalezhnasty', '117/1'))),
(gen_random_uuid(), 'Irina', 'Alekseeva', (SELECT id FROM sex WHERE "name" = 'FEMALE'), 'MP', 914789, (SELECT get_house_id('BELARUS', 'Minsk', 'Gazety "Pravda"', '7/2'))),
(gen_random_uuid(), 'John', 'Smith', (SELECT id FROM sex WHERE "name" = 'MALE'), 'KH', 891122, (SELECT get_house_id('POLAND', 'Krakow', 'Grodzka', '123'))),
(gen_random_uuid(), 'Joseph', 'Pilsudskiy', (SELECT id FROM sex WHERE "name" = 'MALE'), 'KH',900911, (SELECT get_house_id('RUSSIA', 'Moscow', 'Rublevo-Uspenskoe highway', '777'))),
(gen_random_uuid(), 'Ivan', 'Smirnov', (SELECT id FROM sex WHERE "name" = 'MALE'), 'MP', 111111, (SELECT get_house_id('UKRAINE', 'Kiev', 'Bankovya', '1'))),
(gen_random_uuid(), 'Vladimir', 'Kovalchuk', (SELECT id FROM sex WHERE "name" = 'MALE'), 'KH', 222222, (SELECT get_house_id('LATVIA', 'Riga', 'Rozena', '17'))),
(gen_random_uuid(), 'Artis', 'Pabrics', (SELECT id FROM sex WHERE "name" = 'MALE'), 'KH', 333333, (SELECT get_house_id('LATVIA', 'Riga', 'Rozena', '17'))),
(gen_random_uuid(), 'Bread', 'Pit', (SELECT id FROM sex WHERE "name" = 'MALE'), 'KH', 444444, (SELECT get_house_id('BELARUS', 'Minsk', 'Lenina', '11 A'))),
(gen_random_uuid(), 'Ruslan', 'Klimov', (SELECT id FROM sex WHERE "name" = 'MALE'), 'KH', 555555, (SELECT get_house_id('BELARUS', 'Minsk', 'Lenina', '11 A'))),
(gen_random_uuid(), 'Natalia', 'Orlova', (SELECT id FROM sex WHERE "name" = 'FEMALE'), 'KH', 666666, (SELECT get_house_id('BELARUS', 'Minsk', 'Lenina', '11 A'))),
(gen_random_uuid(), 'Feofan', 'Dolgoprudnyi', (SELECT id FROM sex WHERE "name" = 'MALE'), 'KH', 777777, (SELECT get_house_id('USA', 'Vashington', 'Pennsylvania Avenue', '1600'))),
(gen_random_uuid(), 'Kirill', 'Chuikov', (SELECT id FROM sex WHERE "name" = 'MALE'), 'KH', 888888, (SELECT get_house_id('USA', 'Vashington', 'Pennsylvania Avenue', '1600'))),
(gen_random_uuid(), 'Alesia', 'Paramonova', (SELECT id FROM sex WHERE "name" = 'FEMALE'), 'KH', 999999, (SELECT get_house_id('USA', 'Vashington', 'Pennsylvania Avenue', '1600')));

INSERT INTO persons_houses (person_id, house_id)
VALUES
((SELECT id FROM persons WHERE passport_series = 'KH' AND passport_number = 114789), (SELECT get_house_id('BELARUS', 'Minsk', 'Lenina', '11 A'))),
((SELECT id FROM persons WHERE passport_series = 'KH' AND passport_number = 543789), (SELECT get_house_id('BELARUS', 'Minsk', 'Lenina', '11 A'))),
((SELECT id FROM persons WHERE passport_series = 'MP' AND passport_number = 114789), (SELECT get_house_id('POLAND', 'Krakow', 'Grodzka', '123'))),
((SELECT id FROM persons WHERE passport_series = 'MP' AND passport_number = 914789), (SELECT get_house_id('POLAND', 'Krakow', 'Grodzka', '123'))),
((SELECT id FROM persons WHERE passport_series = 'KH' AND passport_number = 891122), (SELECT get_house_id('RUSSIA', 'Moscow', 'Rublevo-Uspenskoe highway', '777'))),
((SELECT id FROM persons WHERE passport_series = 'KH' AND passport_number = 900911), (SELECT get_house_id('RUSSIA', 'Moscow', 'Rublevo-Uspenskoe highway', '777'))),
((SELECT id FROM persons WHERE passport_series = 'MP' AND passport_number = 111111), (SELECT get_house_id('UKRAINE', 'Kiev', 'Bankovya', '1'))),
((SELECT id FROM persons WHERE passport_series = 'KH' AND passport_number = 222222), (SELECT get_house_id('UKRAINE', 'Kiev', 'Bankovya', '1'))),
((SELECT id FROM persons WHERE passport_series = 'KH' AND passport_number = 333333), (SELECT get_house_id('UKRAINE', 'Kiev', 'Bankovya', '1'))),
((SELECT id FROM persons WHERE passport_series = 'KH' AND passport_number = 444444), (SELECT get_house_id('LATVIA', 'Riga', 'Rozena', '17'))),
((SELECT id FROM persons WHERE passport_series = 'KH' AND passport_number = 555555), (SELECT get_house_id('LATVIA', 'Riga', 'Rozena', '17'))),
((SELECT id FROM persons WHERE passport_series = 'KH' AND passport_number = 666666), (SELECT get_house_id('USA', 'New York', 'Brodway', '114'))),
((SELECT id FROM persons WHERE passport_series = 'KH' AND passport_number = 777777), (SELECT get_house_id('USA', 'New York', 'Brodway', '114'))),
((SELECT id FROM persons WHERE passport_series = 'KH' AND passport_number = 888888), (SELECT get_house_id('USA', 'New York', 'Brodway', '114'))),
((SELECT id FROM persons WHERE passport_series = 'KH' AND passport_number = 999999), (SELECT get_house_id('USA', 'New York', 'Brodway', '114')));
