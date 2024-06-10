## Couchbase

### Prerequisites

Follow this process to install Couchbase
- Java version 8+
- Maven
- Java IDE


### Task 1: Connect Spring Boot with Couchbase and create first document (10 points)

Create a spring-boot maven project with at least the following dependencies:

- spring-boot-starter-data-couchbase
- spring-boot-starter-web

Create a Java Representation of User with fields:

```json
{
"id": "9e9a5147-8ebb-4344-a55a-b845aa6e2697",
"email": "john_doe@epam.com",
"fullName": "John Doe",
"birthDate": "1990-10-10",
"gender": "MALE"
}
```
Create a repository for User with ability to

- create a new user
- find user by Id


Implement a service layer
Implement API layer with the following REST endpoints:


- GET /api/v1/user/{id} - to get a user by id

- POST /api/v1/user - to create a user


Create a new user via API method call
Verify that the created user is accessible via API method call


### Task 2: Create index for searching by field (10 points)

Navigate to Couchbase Web Console, to Query tab and execute a query to
create an index for email field.
Add methods to User repository and service with ability to retrieve a user by email
Add the following API endpoint:


- GET /api/v1/user/email/{email} - to get a user by email


Verify that the user is available by email via API method call (do not forget about index)


### Task 3: Embedded objects (10 points)

Create a Java Representation of Sport with the following fields:

```json
{
"id": "9e9a5147-8ebb-4344-a55a-b845aa6e2697",
"sportName": "Tennis",
"sportProficiency": "ADVANCED"
}
```

Add Sports to the User model
Create a method in UserService with ability to add sport to user
Implement the following API endpoint to update user with new sport

- PUT /api/v1/user/{id}/sport


Verify that the Couchbase document is updated after the API method call


### Task 4: Search by embedded object fields (10 points)

Create a method in UserService with the ability to search users by sportName
Add a method for UserRepository with custom query to retrieve users by sportName
Do not forget to create an index in Couchbase
Implement the following API endpoint to retrieve users by sport's name:

- GET /api/v1/user/sport/{sportName}


Verify that some users are returned after the method call


### Task 5: Perform full-text search (10 points)

Create a search index via Couchbase Web Console
Perform a search request via Couchbase Web Console and make sure that you are able to see some
results
Create a Repository for searching user by query
Implement a service layer to return users by a search query
Create a SearchApi with the following API method:


- GET /api/v1/search/user?q= - to search users by query


Verify that some users are returned after the method call