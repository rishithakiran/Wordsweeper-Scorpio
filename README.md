# Wordsweeper-Scorpio


## Building
Using **JDK 8**, in your IDE of choice, set the source directory to be /src. Additionally, ensure that your working directory is set to /resources, as it contains both the files need for XML and for tests to run correctly.

## Running
The main class of the server is `com.scorpio.ServerLauncher`, and it will listen on port `11425` by default. 

## Testing
All tests are in the `com.scorpio.test` package, and as mentioned above, rely upon XML documents from /resources being in the working directory.These tests are written with JUnit 4.
Within the test package, tests are divided into unit and integration, withe former testing the functionality of individual classes, and the latter testing complete workflows.


