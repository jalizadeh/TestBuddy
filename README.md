# TestBuddy
Your buddy helps you to simplifiy your test design and test coverage.

## Scope
Covering test cases (positive & negative) is crucial during test design and after that, test development. Covering all test cases and possible scenarios, while an REST request has different parameters, is not easy to anlayze. TestBuddy checks all the parameters and for each parameter, generate a new test case, runs it and will provide a report of the request and response.

First phase, focuses on importing requests from **Postman** collection and saving the test cases as **Example** for each request.

## Test Case Analysis
A parameter of a request can be a string, number, boolean, array, object, etc. Providing correct parameter leads to a positive test, while incorrect one (for any possible reason) can lead to negative test case.

### Scenarios:
- [x] All parameters are OK (+)
- [x] Empty parameter (-)
    - the parameter exists but it is empty
- [x] Invalid parameter (-)
    - the parameter exists and it is invalid
- [x] Missing parameter (-)
    - the parameter does not exist
- [x] Random parameter (-)
    - the parameter is a random value (first part of UUID)
- and more ...

The checked scenarios above are implemented via the **iFilter** interface.


### Example
> Note: This example assumes only with correct parameters, the test will be positive (response is 200 OK)

Considering a request with 3 parameters, will produce 8 test cases (2^3). The following table shows the test cases for each parameter and final result of each combination. Only the first case is *positive* and the rest are *negative*.

|   | 0  | 1  | 2  | 3  | 4  | 5  | 6  | 7  |
| :------------: | :------------: | :------------: | :------------: | :------------: | :------------: | :------------: | :------------: | :------------: |
| P1  | T  | F  | T  | F  | T  | F  | T  | F  |
| P2  | T  | T  | F  | F  | T  | T  | F  | F  |
| P3  | T  | T  | T  | T  | F  | F  | F  | F  |
| Result  | **T**  | **F**  | **F**  | **F**  | **F**  | **F**  | **F**  | **F**  |

## Result
The end result will be auto-generated JSON file of the collection containing all the test cases as Postman`s Example

```
curl -X POST 'localhost:8080/json?delay=1' \
-H 'Content-Type: application/json' \
--data-raw '{
    "filters" : [
        "EMPTY",
        "RANDOM",
        "INVALID",
        "MISSING"
    ]
}'
```

Response

```json
{
    "collectionName": "TestBuddy_20221120_143823",
    "totalTimeMs": 398,
    "totalRequests": 6,
    "totalCalls": 62,
    "totalPositive": 6,
    "totalNegative": 56,
    "requests": [
        {
            "name": "Delete",
            "method": "DELETE",
            "url": "http://localhost:8080/delete",
            "positive": 1,
            "negative": 0
        },
        {
            "name": "Raw Body",
            "method": "POST",
            "url": "http://localhost:8080/rawBody",
            "positive": 1,
            "negative": 28
        },
        {
            "name": "Simple",
            "method": "GET",
            "url": "http://localhost:8080/simple",
            "positive": 1,
            "negative": 0
        },
        {
            "name": "Simple - No Path",
            "method": "GET",
            "url": "http://localhost:8080",
            "positive": 1,
            "negative": 0
        },
        {
            "name": "Update",
            "method": "PUT",
            "url": "http://localhost:8080/update",
            "positive": 1,
            "negative": 0
        },
        {
            "name": "X-Form",
            "method": "POST",
            "url": "http://localhost:8080/xform",
            "positive": 1,
            "negative": 28
        }
    ]
}
```


[Sample generated JSON file to load in Postman](assets/result-full.json)

Generated result is loaded in Postman:

![](assets/result-1.JPG)




## Request Types based on body format

**empty**
- The request doesn`t have body data

**raw**
- The body is plain text
- **&amp;** separated

**x-www-form-urlencoded**
- key-value pairs
- parameters can be disabled (*not implemented yet*)



## TODO

Test Endpoint
- [x] /rawBody for requests with *raw* body
- [x] /xForm for requests with *x-www-form-urlencoded* body
- [ ] RestAssured tests


**Postman Collection Parser**
- [x] Parse Postman collection
- [x] Parse *raw* body
- [x] Parse *x-www-form-urlencoded* body
- [x] Export populated Postman collection
- [x] Add response's headers to PostmanResponse object
- [x] Use requests's header in RestService
- [x] Support all request methods
	- [x] GET
	- [x] POST
	- [x] PUT
	- [x] DELETE
- [x] Prettify result JSON file
- [x] Statistics
- [ ] Load Postman environment file
- [ ] Report / Log
- [ ] Group similar results
- [ ] Detach delay from RestService
- [ ] Fix _postman_previewlanguage

**Filters**
- [x] Selectable filter
- [x] Allow empty filter list (run only positive case)
- [ ] Apply filter on headers
- [ ] Apply filter on query parameters
- [ ] Boundary tests
- [ ] Test inventory (providing desired input data)