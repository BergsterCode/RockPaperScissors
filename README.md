# RockPaperScissors
This is a small REST API where two players can face off in rock paper scissors. The api has four endpoints:

localhost:8080/api/games/

localhost:8080/api/games/{gameID}

localhost:8080/api/games/{gameID}/join

localhost:8080/api/games/{gameID}/move


How to run the API:
In order to run the api you need to make sure that you have Apache maven installed together with a valid java sdk.
Package the API into a jar file by running "mvn clean package" then execute the jar file by running java -jar {jarfile} in a cli tool.

In order to test the API i have provided a list of curl commands below. If you prefer testing via postman you can import the curl commands below.


CURL calls for testing api:

Start game:
curl --location --request POST 'localhost:8080/api/games/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name":"ENTER YOUR NAME HERE"
}'

Check Game state: 
curl --location --request GET 'localhost:8080/api/games/{Enter gameID given from calling startgame here}'

Join game:

curl --location --request POST 'localhost:8080/api/games/{gameID}/join' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name":"ENTER YOUR NAME HERE"
}'

Make a move: 

curl --location --request POST 'localhost:8080/api/games/{gameID}/move' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name":"YOUR NAME HERE",
    "move":"YOUR MOVE HERE"
}'
