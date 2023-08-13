## Getting Started
Spring boot application is located in [etl/](https://github.com/racicmelanija/shifts/tree/master/etl) folder. It has a Dockerfile and is added to the existing docker-compose, so to run everything together just use:
```bash
$ docker-compose up -d
```
When everything is up and running, start the etl job here: http://localhost:8080/start-etl-job

### Extract
Data is extracted using RestTemplate in [`ShiftsApiClient`](https://github.com/racicmelanija/shifts/blob/master/etl/src/main/java/com/example/etl/client/ShiftsApiClient.java). Since the api is paginated, data is fetched until this condition is not fulfilled anymore: 
```
public boolean hasNextPage() {  
    return links.getNext() != null;  
}
```
#### Retry logic (WIP)
Added retry logic with Resilience4j lib and added it for the method that calls the shifts-api. The idea is to retry on exceptions like TimeoutException, HttpServerErrorException etc. Also, added a conditional retry predicate which evaluates the response entity and looks for 5xx and 429 status codes.
### Transform
Data is being transformed during the 'Extract' phase for each paginated response. Following transformations are applied:
- Response objects are being converted to Entity objects 
- Cost calculations are done for Shift entity
- Start and finish properties are converted to Dates from Unix timestamps in milliseconds
### Load
Data is loaded leveraging Hibernate's batching mechanism. Batch size is set in the `application.yml` and after each batch size entities are being flushed and cleared with entity manager to remove them from memory. This is done in [`LoadData`](https://github.com/racicmelanija/shifts/blob/master/etl/src/main/java/com/example/etl/service/LoadData.java). This can be observed in the console because Hibernate statistics are being printed.

Side note: Id generation is skipped, as per request, and the implemenation and usage of that can be found here: [`SkipGenerationIfIdProvided`](https://github.com/racicmelanija/shifts/blob/master/etl/src/main/java/com/example/etl/config/SkipGenerationIfIdProvided.java) and [`BaseEntity`](https://github.com/racicmelanija/shifts/blob/master/etl/src/main/java/com/example/etl/model/BaseEntity.java).
### Kpi calculations 
All kpi calculations are done after data is loaded to the database through INSERT statements which contain SELECT clauses. This is executed only if there are no kpis for the current data stored in the database.

Example for the `max_allowance_cost_14d` kpi:
````
INSERT INTO kpis (kpi_name, kpi_date, kpi_value)  
SELECT 'max_allowance_cost_14d', CURRENT_TIMESTAMP, MAX(public.allowances.cost)  
FROM allowances
WHERE shift_id IN (SELECT id
		   FROM shifts  
		   WHERE date >= CURRENT_DATE - INTERVAL '14 days' AND date <= CURRENT_DATE);
````
