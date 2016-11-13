# diffRestful
Restful api to provide end points to compare Base64 binary data.

## rest - api
- <host>/v1/diff/:id/left
    - Add a JSON base64 encoded data on left    
    - POST
    - Expected id comparison parameter, e.g: 1
    - Expected body, e.g: { data: SGVsbG8gV29yZCEhDQo= }
    - return {JSON} e.g: {"output":"left successfully settled"}
    - On the server side a new folder is created on the file system under "comparisons" folder called Id(by url parameter) if not exist. And created a new file under "comparison/<ID>" folder called left with encoded data as content.

- <host>/v1/diff/:id>/right
    - Add a JSON base64 encoded data on right    
    - POST
    - Expected id comparison parameter, e.g: 1
    - Expected body, e.g: { data: SGVsbG8gV29yZCEhDQo= }
    - return {JSON} e.g: {"output":"right successfully settled"}
    - On the server side a new folder is created on the file system under "comparisons" folder called Id(by url parameter) if not exist. And created a new file under "comparison/<ID>" folder called right with encoded data as content.
    
- <host>/v1/diff/:id
    - Get diff-ed result of left/right data inserted via endpoints above
    - GET
    - Expected id comparison parameter, e.g: 1
    - return {JSON} 
        - If equal return {"output":"equals"} 
        - If not of equal size just return {"output":"not of equal size"}
        - If of same size provide insight in where the diffs are {"output":"1cHello Word.."}, in this example the left decoded value is SGVsbG8gV29yZCE= (decoded value: Hello Word!) and right decoded value is SGVsbG8gV29yZC4= (decoded value: Hello Word.)
     - On the server side, left and right files will be decoded and two new files will be created (left_decoded and right_decoded), after that the unix "diff" utility will be forked and a native process will be created (diff -ed left_encoded right encoded) and the result of diff procces will be send back as response.
     
## Running
Run following command under DiffRestfull folder ./gradlew bootRun

## Tests

### Unit tests
Run following command under DiffRestfull folder ./gradlew test. After run it a report folder will be generated at "/build/reports/tests/". And the report will be at: index.html.

### Integration tests
- Requirements: jmeter [jmeter page](http://jmeter.apache.org/)
- Open jmeter and load diff-ed-Integration-tests.jmx under /integration-test folder.

## Suggestions for improvements
- Add a layer to store files in order to make the solution more scalable. E.g: Store files in the cloud (Amazon AWS)
- Tests:
    - Integrate Jmeter Integration test in gradle, in order to run Unit and Integration tests by one command
    - Create performance tests