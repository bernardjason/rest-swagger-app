the project uses Play framework Scala project alongside Swagger to provide a simple api to show data being held on 
the server side for a Google Graph implementation. I've taken Google Chart example

https://developers.google.com/chart/interactive/docs/gallery/columnchart

and stored a sample set of data in a Sqlite database, being accessed using a simple REST API.

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
