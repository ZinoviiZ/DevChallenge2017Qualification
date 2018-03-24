# DevChallengeQualification

Application for DevChallenge2017 qualification round, backend nomination. </br>
With that project I've passed qualification round in backend nomination and have been chosen Middle-Senior category.</br>
https://devchallenge.it/

## DevChallenge
All-Ukrainian software development championship among junior, middle and senior programmers. The championship takes place in 3 stages: Qualification, Semifinal and offline Finals in Kyiv. Participants compete in eleven nominations in three directions: Web, Mobile, Game. Each nomination has 2 categories: Pro (Senior, Middle-to-Senior) and Standard (Middle, Junior-to-Middle). At the final, the tasks were from the partner Amazon.

# Run application
Application is deployed on Heroku (wait until the heroku server starts)
```cmd
https://dev-challenge-qualification.herokuapp.com/swagger-ui.html
```
## Build image
```cmd
docker build -t [image-name] .
```
## Run container
```cmd
docker run -it --name [container-name] -p 8080:8080 [image-name]
```
# Information
For solving this task I've chosen Spring Framework and Swagger Framework(documentation).</br>
My application has RestFull architecture includes HATEOAS.</br>
For data storing I've chosen mongoDB database because I think for saving documents in DB the best decision use NoSql document-oriented database.
# Documentation
See rest documentation - http://localhost:8080/swagger-ui.html
### Config
SpringApp - Running Spring Boot application.</br>
SwaggerConfig - Swagger framework configuration - framework for documentation.
### AOP
ExceptionHandler advice - cache thrown exceptions and creates according response</br>
RestAspect aspect - logs request and settle time response.
### Exceptions
ParentException,DelegateNofFoundException, PdfParseException, SessionNotFoundsException, VotingNotFoundException
### Builders
DelegateResponseBuilder - build DelegateResponse model</br>
SessionResponseBuilder - build SessionResponse model</br>
VotingResponseBuilder - build VotingResponse model</br>
LinkBuilder - build links for HATEOAS architecture
### Model
Error - application errors. </br>
StatusResponse - statusResponse :)</br>
Delegate, Session, Voting - classes for ORM</br>
VoteValue - delegate voting's value.</br>
VotingResult - voting's result</br>
MessageResponse - generic wrapper for response.</br>
Response model extends ResourceSupport - SpringHateoas class granted Links.
### Parser
Parser - class for parsing String content from pdf.</br>
ParseWorker - thread for parallelize pdf pages process.</br>
PdfData - parse model we're parsing from one voting.
### Repository
SpringData repositories for connection to DB with ORM.
### Controllers
See rest documentation - http://localhost:8080/swagger-ui.html
### Util
Generator - generating id for ORM objects.
### Tests
All tests are in src/test/java/com/dev/challenge directory.
