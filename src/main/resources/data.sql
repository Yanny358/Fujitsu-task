INSERT INTO CITY_AND_VEHICLE_FEE (CITY, VEHICLE_TYPE, AMOUNT) VALUES ('TALLINN', 'CAR', 4.0);
INSERT INTO CITY_AND_VEHICLE_FEE (CITY, VEHICLE_TYPE, AMOUNT) VALUES ('TALLINN', 'SCOOTER', 3.5);
INSERT INTO CITY_AND_VEHICLE_FEE (CITY, VEHICLE_TYPE, AMOUNT) VALUES ('TALLINN', 'BIKE', 3.0);
INSERT INTO CITY_AND_VEHICLE_FEE (CITY, VEHICLE_TYPE, AMOUNT) VALUES ('TARTU', 'CAR', 3.5);
INSERT INTO CITY_AND_VEHICLE_FEE (CITY, VEHICLE_TYPE, AMOUNT) VALUES ('TARTU', 'SCOOTER', 3.0);
INSERT INTO CITY_AND_VEHICLE_FEE (CITY, VEHICLE_TYPE, AMOUNT) VALUES ('TARTU', 'BIKE', 2.5);
INSERT INTO CITY_AND_VEHICLE_FEE (CITY, VEHICLE_TYPE, AMOUNT) VALUES ('PÄRNU', 'CAR', 3.0);
INSERT INTO CITY_AND_VEHICLE_FEE (CITY, VEHICLE_TYPE, AMOUNT) VALUES ('PÄRNU', 'SCOOTER', 2.5);
INSERT INTO CITY_AND_VEHICLE_FEE (CITY, VEHICLE_TYPE, AMOUNT) VALUES ('PÄRNU', 'BIKE', 2.0);


-- Extra fee based on air temperature (ATEF)
-- Less than -10°C for Scooter or Bike
INSERT INTO TEMPERATURE_EXTRA_FEE (MIN_VALUE, MAX_VALUE, ADJUSTMENT_AMOUNT) VALUES (-50, -10, 1.0);

-- Between -10°C and 0°C for Scooter or Bike
INSERT INTO TEMPERATURE_EXTRA_FEE (MIN_VALUE, MAX_VALUE, ADJUSTMENT_AMOUNT) VALUES (-10, 0, 0.5);

-- Extra fee based on wind speed (WSEF) for Bike
-- Between 10 m/s and 20 m/s                                                                      
INSERT INTO WIND_EXTRA_FEE (MIN_VALUE, MAX_VALUE, ADJUSTMENT_AMOUNT) VALUES (10, 20, 0.5);

-- Extra fee based on weather phenomenon (WPEF) for Scooter or Bike
-- Snow or Sleet
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Light snow shower', 1.0);
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Moderate snow shower', 1.0);
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Heavy snow shower', 1.0);
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Light sleet', 1.0);
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Moderate sleet', 1.0);
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Light snowfall', 1.0);
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Moderate snowfall', 1.0);
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Heavy snowfall', 1.0);
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Blowing snow', 1.0);
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Drifting snow', 1.0);

-- Rain or shower
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Light rain', 0.5);
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Moderate rain', 0.5);
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Heavy rain', 0.5);
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Light shower', 0.5);
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Moderate shower', 0.5);
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Heavy shower', 0.5);

-- Forbidden weather phenomenon
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Glaze', 0.0);      
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Hail', 0.0);   
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Thunderstorm', 0.0);
INSERT INTO PHENOMENON_EXTRA_FEE (NAME, ADJUSTMENT_AMOUNT) VALUES ('Thunder', 0.0);