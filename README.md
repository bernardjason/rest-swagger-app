the project uses Play framework Scala project alongside Swagger to provide a simple api to show data being held on 
the server side for a Google Graph implementation. I've taken Google Chart example

https://developers.google.com/chart/interactive/docs/gallery/columnchart

I've an example of the code running here on Heroku


https://secure-retreat-29275.herokuapp.com/

and called 2 external API's 
http://api.openweathermap.org/data/2.5/weather
and
http://query.yahooapis.com/v1/public/yql

to get data and store it for display on two chart2. The data is stored in a Sqlite database, being accessed using a simple REST API.

Clone the code, then type

./activator run

visit from browser either 
	http://127.0.0.1:9000/
to see the swagger documentation for the API
or
	http://127.0.0.1:9000/weatherchart
	http://127.0.0.1:9000/stockchart
to see what the charts looks like

You can add more data using either Swagger UI from the POST operation
http://127.0.0.1:9000/#!/timeseries/postByName
with name set to ^FTSE and body being
{
  "label": "2016-05-14T06:40:35Z",
  "value": "1000"
}

which generates a curl request of and runs the equivalent
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{
  "label": "2016-05-14T06:40:35Z",
  "value": "1000"
}' 'http://localhost:9000/timeseries/%5EFTSE'

Now the graph will change

There is no validation of the data held either on the backend or front end so the graph can break.

Final note, on Heroku the database I use postgres. Evolutions are disabled as the db scripts for sqlite and postgres are different. Heroku db create script is in heroku.psql
