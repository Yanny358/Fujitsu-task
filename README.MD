# Fee Calculator API
The Fee Calculator API provides a flexible solution for calculating fees based on various conditions, including city, vehicle type, and current weather conditions. It's designed to dynamically adjust fees for different vehicle types across cities, taking into account real-time weather data.

## Features
*  Calculate fees based on city, vehicle type, and current weather conditions.
*  Update base fee configurations for different cities and vehicle types.
*  Support for cities (TALLINN, TARTU, PÄRNU) and vehicle types (CAR, BIKE, SCOOTER).

## Installing
1. Clone repository
```git clone https://github.com/Yanny358/Fujitsu-task.git```
2. Navigate to the project directory
```cd Fujitsu-task```
3. Build the project
```mvn clean install```
4. Run the application
```mvn spring-boot:run```
5. Application should now be running on ``localhost:8080``
6. To get access to database (H2 in memory db) go to ``localhost:8080/h2-console``. 
* User name: fuji 
* Password: tsu

## Swagger open api docs
* Could be found here ``http://localhost:8080/swagger-ui/index.html`` after running application.

## Usage
### Calculate fee
Calculates the fee based on the specified city and vehicle type, considering current weather conditions.
There is a cron job for fetching data weather from ``https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php``
every hour at 15 minute mark (Example: 17:15:00, next fetching will be at 18:15:00), it could be configured in
`application.properties` file in `src/main/resources` directory. You need to change ``interval-in-cron=0 15 * * * *
`` line for your liking, for example to fetch data every 30 second change it to `interval-in-cron=30 * * * * *
`.

## Endpoints overview
1. Fee calculation
 * Description: Calculates the fee based on the specified city and vehicle type, considering current weather conditions.
   The fee calculation takes into account air temperature, wind speed, and weather phenomena to adjust the base fee dynamically.
 * Method: GET
 * URL: ```/api/calculateFee/{city}/{vehicleType}```
 * URL parameters
    * `city` - The city name. At the moment three cities is supported (TALLINN, TARTU, PÄRNU).
    * `vehicleType` - The type of vehicle. At the moment there is three types (CAR, BIKE, SCOOTER).
 * Response: Returns fee or error message.

2. Updating base fee 
* Updates the base fee for a given city and vehicle type combination.
* Method: POST
* URL: ```/api/setBaseFee/{city}/{vehicleType}?fee={amount}```
* URL parameters
   * `city` - The city name. At the moment three cities is supported (TALLINN, TARTU, PÄRNU).
   * `vehicleType` - The type of vehicle. At the moment there is three types (CAR, BIKE, SCOOTER).
* Query parameters
    * `fee` - The new base fee to set. In the `amount` value to be placed. Should be minimum value of 1.
* Response: Returns success boolean and message whether operation were successfully updated or not.

3. Get all fees
* Description: Retrieves a list of all city and vehicle fee configurations from the database.
* Method: GET
* URL: ```api/getCitiesAndFees```
* Response: Returns city name, vehicle type, and the fee amount.
