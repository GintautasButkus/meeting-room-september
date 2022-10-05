## Meeting Room for September

>#### Create a web application to manage internal meetings using Java Spring Boot.

## Instructions:
- Before launching the application change/enter directory pathway in *application.properties* file where the json file will be created. Pathway example: *C:\\Users\\example\\Desktop\\*
- Once done application can be start as a Spring Boot application. To manipulate the application itself Swagger UI can be accessed at http://localhost:8080/swagger-ui/.


## Requirements:
#### Rest API endpoint to create a new meeting. All the meeting data should be stored in a JSON file. Application should retain data between restarts. Meeting model should contain the following properties:
- Name
- ResponsiblePerson
- Description
- Category (Fixed values - CodeMonkey / Hub / Short / TeamBuilding)
- Type (Fixed values - Live / InPerson)
- StartDate
- EndDate

#### Rest API endpoint to delete a meeting. Only the person responsible can delete the meeting.

#### Rest API endpoint to add a person to the meeting.
- Command should specify who is being added and at what time.
- If a person is already in a meeting which intersects with the one being added, a warning message should be given.
- Prevent the same person from being added twice.

#### Rest API endpoint to remove a person from the meeting.
- If a person is responsible for the meeting, he can not be removed.

#### Rest API endpoint to list all the meetings. Add the following parameters to filter the data:
- Filter by description (if the description is “Jono Java meetas”, searching for java should return this entry)
- Filter by responsible person
- Filter by category
- Filter by type
- Filter by dates (e.g meetings that will happen starting from 2022-01-01 / meetings that will happen between 2022-01-01 and 2022-02-01)
- Filter by the number of attendees (e.g show meetings that have over 10 people attending)

Focus on good code design and good practices. Besides the completion of functional requirements, we require: OOP design, unit tests and code should adhere to Java coding conventions.
