the project uses Play framework Scala project alongside Swagger to provide a simple api to show data being held on 
the server side for a Google Graph implementation. I've taken Google Chart example

https://developers.google.com/chart/interactive/docs/gallery/columnchart

and stored a sample set of data in a Sqlite database, being accessed using a simple REST API.

Clone the code, then type

./activator run

visit from browser either 
	http://127.0.0.1:9000/swagger
to see the swagger documentation for the API
or
	http://127.0.0.1:9000
to see what the chart looks like

You can add more data using either Swagger UI from the POST operation
http://localhost:9000/swagger#!/timeseries/postByName
with name set to energy and body being
{
  "label": "12",
  "value": "10"
}

which generates a curl request of and runs the equivalent
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{
  "label": "12",
  "value": "10"
}' 'http://localhost:9000/timeseries/energy'

Now the graph will change

There is no validation of the data held either on the backend or front end so the graph can break.