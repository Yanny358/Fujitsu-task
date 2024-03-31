CREATE TABLE IF NOT EXISTS WEATHER_STATION (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    wmo_code VARCHAR(255),
    air_temperature DOUBLE,
    wind_speed DOUBLE,
    weather_phenomenon VARCHAR(255),
    time_stamp TIMESTAMP
);

CREATE TABLE IF NOT EXISTS CITY_AND_VEHICLE_FEE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    city VARCHAR(255),
    vehicle_type VARCHAR(255),
    amount DOUBLE
);

CREATE TABLE IF NOT EXISTS TEMPERATURE_EXTRA_FEE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    min_value DOUBLE,
    max_value DOUBLE,
    adjustment_amount DOUBLE
);

CREATE TABLE IF NOT EXISTS WIND_EXTRA_FEE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    min_value DOUBLE,
    max_value DOUBLE,
    adjustment_amount DOUBLE
);

CREATE TABLE IF NOT EXISTS PHENOMENON_EXTRA_FEE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    adjustment_amount DOUBLE
);