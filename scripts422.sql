-- 4.2.2 Создание таблиц по описанию "человек - машина"

CREATE TABLE car (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    price NUMERIC(12, 2) NOT NULL
);

CREATE TABLE person (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    age INT NOT NULL,
    has_driver_license BOOLEAN NOT NULL,
    car_id BIGINT REFERENCES car (id)
);
