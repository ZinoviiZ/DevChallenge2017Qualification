# DevChallengeQualification
Application for DevChallenge2017 qualification round, backend nomination. </br>

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
For solving this task I've chosen Spring Framework and Swagger Framework(documentation).
My application has RestFull architecture includes HATEOAS.
For data storing I've chosen mongoDB database because I think for saving documents in DB the best decision use NoSql document-oriented database.
# Documentation
See rest documentation - http://localhost:8080/swagger-ui.html
###Config
SpringApp - Running Spring Boot application.
SwaggerConfig - Swagger framework configuration - framework for documentation.
###AOP
ExceptionHandler advice - cache thrown exceptions and creates according response
RestAspect aspect - logs request and settle time response.
###Exceptions
ParentException
DelegateNofFoundException
PdfParseException
SessionNotFoundsException
VotingNotFoundException
###Builders
DelegateResponseBuilder - build DelegateResponse model
SessionResponseBuilder - build SessionResponse model
VotingResponseBuilder - build VotingResponse model
LinkBuilder - build links for HATEOAS architecture
###Model
Error - application errors.
StatusResponse - statusResponse :)
Delegate, Session, Voting - classes for ORM
VoteValue - delegate voting's value.
VotingResult - voting's result
MessageResponse - generic wrapper for response.
Response model extends ResourceSupport - SpringHateoas class granted Links.
###Parser
Parser - class for parsing String content from pdf.
ParseWorker - thread for parallelize pdf pages process.
PdfData - parse model we're parsing from one voting.
###Repository
SpringData repositories for connection to DB with ORM.
###Controllers
See rest documentation - http://localhost:8080/swagger-ui.html
###Util
Generator - generating id for ORM objects.
###Tests
All tests are in src/test/java/com/dev/challenge directory.
