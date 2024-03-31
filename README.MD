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
* Description: Updates the base fee for a given city and vehicle type combination.
* Method: PUT
* URL: ```/api/setBaseFee/{city}/{vehicleType}?fee={amount}```
* URL parameters
   * `city` - The city name. At the moment three cities is supported (TALLINN, TARTU, PÄRNU).
   * `vehicleType` - The type of vehicle. At the moment there is three types (CAR, BIKE, SCOOTER).
* Query parameters
    * `fee` - The new base fee to set. In the `amount` value to be placed. Should be minimum value of 1.
* Response: Returns success boolean and message whether operation were successfully updated or not.

3. Get all base fees
* Description: Retrieves a list of all city and vehicle fee configurations from the database.
* Method: GET
* URL: ```api/getCitiesAndFees```
* Response: Returns city name, vehicle type, and the fee amount.
* Example Response:
```json
  {
  "city": "Tallinn",
  "vehicleType": "CAR",
  "amount": 4.0
  } 
```

## Extra fees
### Phenomenon extra fees
1. Get All Weather Phenomenon Fees
* Description: Retrieves a list of all weather phenomenon fees.
* Method: GET
* URL: `/api/phenomenonFees`
* Response: Returns a list of phenomenon fees.
* Example response: 
```json
{
  "id": 1,
  "name": "Light snow shower",
  "adjustmentAmount": 1.0
},
{
  "id": 2,
  "name": "Moderate snow shower",
  "adjustmentAmount": 1.0
}
```

2. Update Weather Phenomenon Fee
* Description: Updates the adjustment amount for a specific weather phenomenon by ID.
* Method: PUT
* URL: `/api/phenomenonFees/{id}`
* URL parameters
    * `id` - The ID of the weather phenomenon fee to update.
* Response:
    * `200 OK`: Returns the updated weather phenomenon fee.
    * `404 Not Found`: If no weather phenomenon fee with the specified ID exists.
    * `400 Bad Request`: If the new adjustment amount is invalid.
* Request body example:
```json
    {
      "adjustmentAmount": 2.5
    }
```

### Temperature extra fees
1. Get All Temperature Fees
* Description: Retrieves all temperature extra fees.
* Method: GET
* URL: `/api/temperatureFees`
* Response: Returns a list of temperature fees.
* Example response:
```json
{
  "id": 1,
  "minValue": -50.0,
  "maxValue": -10.0,
  "adjustmentAmount": 1.0
},
{
  "id": 2,
  "minValue": -10.0,
  "maxValue": 0.0,
  "adjustmentAmount": 0.5
}
```

2. Create a New Temperature Fee
* Description: Creates a new temperature extra fee.
* Method: POST
* URL: `/api/temperatureFees`
* Response:
    * `201 CREATED`: Created with the created temperature extra fee.
    * `400 Bad Request`: with an error message if the request fails validation.
* Request body example:
```json
    {
      "minValue": -10,
      "maxValue": 5,
      "adjustmentAmount": 0.5
    }
```
3. Update an Existing Temperature Fee
* Description: Updates an existing temperature extra fee by its ID.
* Method: PUT
* URL: `/api/temperatureFees/{id}`
* URL parameters
    * `id` - The ID of the weather temperature fee to update.
* Response:
    * `200 OK`: Returns the updated weather temperature fee.
    * `404 Not Found`: If no weather temperature fee with the specified ID exists.
    * `400 Bad Request`: With an error message if the update request fails validation.
* Request body example:
```json
    {
      "minValue": 0,
      "maxValue": 5,
      "adjustmentAmount": 0.5
    }
```
4. Delete a Temperature Fee
* Description: Deletes a temperature extra fee by its ID.
* Method: DELETE
* URL: `/api/temperatureFees/{id}`
* URL parameters
    * `id` - The ID of the weather temperature fee to delete.
* Response:
    * `200 OK`: Indicating successful deletion.
    * `404 Not Found`: If the temperature fee with the specified ID does not exist.


### Wind extra fees
1. Get All Wind Fees
* Description: Retrieves all wind extra fees.
* Method: GET
* URL: `/api/windFees`
* Response: Returns a list of wind fees.
* Example response:
```json
{
  "id": 1,
  "minValue": -50.0,
  "maxValue": -10.0,
  "adjustmentAmount": 1.0
},
{
  "id": 2,
  "minValue": -10.0,
  "maxValue": 0.0,
  "adjustmentAmount": 0.5
}
```

2. Create a New Wind Fee
* Description: Creates a new wind extra fee.
* Method: POST
* URL: `/api/windFees`
* Response:
  * `201 CREATED`: Created with the created wind extra fee.
  * `400 Bad Request`: with an error message if the request fails validation.
* Request body example:
```json
    {
      "minValue": -10,
      "maxValue": 5,
      "adjustmentAmount": 0.5
    }
```
3. Update an Existing Wind Fee
* Description: Updates an existing wind extra fee by its ID.
* Method: PUT
* URL: `/api/windFees/{id}`
* URL parameters
  * `id` - The ID of the weather wind fee to update.
* Response:
  * `200 OK`: Returns the updated weather wind fee.
  * `404 Not Found`: If no weather wind fee with the specified ID exists.
  * `400 Bad Request`: With an error message if the update request fails validation.
* Request body example:
```json
    {
      "minValue": 0,
      "maxValue": 5,
      "adjustmentAmount": 0.5
    }
```
4. Delete a Wind Fee
* Description: Deletes a wind extra fee by its ID.
* Method: DELETE
* URL: `/api/windFees/{id}`
* URL parameters
  * `id` - The ID of the weather wind fee to delete.
* Response:
  * `200 OK`: Indicating successful deletion.
  * `404 Not Found`: If the wind fee with the specified ID does not exist.


