--liquibase formatted sql

-- changeset create table countries:semeniuk
CREATE TABLE IF NOT EXISTS countries (
	id SERIAL PRIMARY KEY,
	"name" TEXT UNIQUE NOT NULL
);

-- changeset create table addresses:semeniuk
CREATE TABLE IF NOT EXISTS addresses (
	id BIGSERIAL PRIMARY KEY,
	country_id INTEGER NOT NULL REFERENCES countries,
	city VARCHAR (20) NOT NULL,
	street VARCHAR (30) NOT NULL,
	"number" VARCHAR (6) NOT NULL,
	UNIQUE (country_id, city, street, "number")
);

-- changeset create table houses:semeniuk
CREATE TABLE IF NOT EXISTS houses (
	id BIGSERIAL PRIMARY KEY,
	"uuid" UUID NOT NULL,
	"area" NUMERIC(6,1) NOT NULL,
	address_id INTEGER UNIQUE NOT NULL REFERENCES addresses,
	create_date TIMESTAMP (3) WITHOUT time ZONE DEFAULT  (now() at time zone 'utc'),
	CONSTRAINT "area" CHECK ("area" > 0)
);

-- changeset create table sex:semeniuk
CREATE TABLE IF NOT EXISTS sex (
	id SERIAL PRIMARY KEY,
	"name" VARCHAR (10) UNIQUE NOT NULL
);

-- changeset create table persons:semeniuk
CREATE TABLE IF NOT EXISTS persons (
	id BIGSERIAL PRIMARY KEY,
	"uuid" UUID NOT NULL,
	"name" VARCHAR (20) NOT NULL,
	surname VARCHAR (20) NOT NULL,
	sex_id INTEGER NOT NULL REFERENCES sex,
	passport_series VARCHAR (2) NOT NULL,
	passport_number INTEGER NOT NULL,
	create_date TIMESTAMP (3) WITHOUT time ZONE DEFAULT (now() at time zone 'utc'),
	update_date TIMESTAMP (3) WITHOUT time ZONE DEFAULT (now() at time zone 'utc'),
	house_id BIGINT NOT NULL REFERENCES houses,
	CONSTRAINT passport_number CHECK (passport_number >= 100000 AND passport_number <= 999999),
	UNIQUE (passport_series, passport_number)
);

-- changeset create table persons_houses:semeniuk
CREATE TABLE IF NOT EXISTS persons_houses (
	person_id BIGINT NOT NULL REFERENCES persons,
	house_id BIGINT NOT NULL REFERENCES houses,
	UNIQUE (person_id, house_id)
);

-- changeset create function now_utc:semeniuk
CREATE OR REPLACE FUNCTION now_utc()
	RETURNS timestamp
		RETURN now() at time zone 'utc';

-- changeset splitStatements:false
CREATE OR REPLACE FUNCTION func_persons_update_date()
	RETURNS TRIGGER AS $$
BEGIN
	IF NEW."name" IS DISTINCT FROM OLD."name"
	OR NEW.surname IS DISTINCT FROM OLD.surname
	OR NEW.sex_id != OLD.sex_id
	OR NEW.passport_series IS DISTINCT FROM OLD.passport_series
	OR NEW.passport_number != OLD.passport_number
	THEN
		NEW.update_date = now_utc();
	END IF;
	RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE OR REPLACE TRIGGER trigger_persons_update_date
	BEFORE UPDATE
	ON persons
	FOR EACH ROW
	EXECUTE PROCEDURE func_persons_update_date();

CREATE OR REPLACE FUNCTION get_house_id(country_name TEXT, _city VARCHAR(20), _street VARCHAR(30), house_number VARCHAR(6))
RETURNS BIGINT AS $$
DECLARE
    house_id BIGINT;
BEGIN
    SELECT h.id INTO house_id
    FROM houses h
    JOIN addresses a ON h.address_id = a.id
    JOIN countries c ON a.country_id = c.id
    WHERE c."name" = country_name AND a.city = _city AND a.street = _street AND a."number" = house_number;

    IF house_id IS NULL THEN
        RAISE EXCEPTION 'Дом с указанными параметрами не найден: страна %, город %, улица %, номер %', country_name, _city, _street, house_number;
    END IF;

    RETURN house_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_address_id(country_name TEXT, _city VARCHAR(20), _street VARCHAR(30), house_number VARCHAR(6))
RETURNS BIGINT AS $$
DECLARE
    address_id BIGINT;
BEGIN
    SELECT a.id INTO address_id
    FROM addresses a
    JOIN countries c ON a.country_id = c.id
    WHERE c."name" = country_name AND a.city = _city AND a.street = _street AND a.number = house_number;

    IF address_id IS NULL THEN
        RAISE EXCEPTION 'Адрес с указанными параметрами не найден: страна %, город %, улица %, номер %', country_name, _city, _street, house_number;
    END IF;

    RETURN address_id;
END;
$$ LANGUAGE plpgsql;



