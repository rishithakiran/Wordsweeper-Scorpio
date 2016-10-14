# Wordsweeper-Scorpio


## Building
Using **JDK 8**, in your IDE of choice, set the source directory to be /src. Additionally, ensure that your working directory is set to /resources, as it contains both the files need for XML and for tests to run correctly.

## Running
The main class of the server is `com.scorpio.ServerLauncher`, and it will listen on port `11425` by default. 

*Special caveat for this submission:* In order to ensure that this server works with the mock client, a `createGameRequest` will *always* lead to a game being created with the ID "somePlace". This is because the mock client does not support `joinGameRequest` with a user-settable game ID. Given this, any subsequent `createGameRequests` will fail with a game already exists error. This is not a limitation of the server, but one of the input from the mock client (server is fully capable of generating UUID's for each new game).

## Testing
All tests are in the `com.scorpio.test` package, and as mentioned above, rely upon XML documents from /resources being in the working directory.These tests are written with JUnit 4.
Within the test package, tests are divided into unit and integration, withe former testing the functionality of individual classes, and the latter testing complete workflows.


