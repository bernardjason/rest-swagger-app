This example so far has been run locally. Using a simple database that requires no maintenance. With some minor modifications this application can be deployed on the Heroku platform (amongst others see https://www.playframework.com/documentation/2.4.x/DeployingCloud) accessible to the internet for free.


Jobs to address

1. Get a Heroku account

2. Get the Heroku toolbelt. A command line utility for creating and maintaining applications on the Heroku platform. See https://toolbelt.heroku.com/ . Make sure you follow the instructions to log the toolbelt into your Heroku account.

3. Change the application.conf so that it uses Postgres. Using Sqlite has disadvantages too!
Add to conf/application.conf
```
slick.dbs.default.driver = ${?slick.dbs.default.driver}
slick.dbs.default.db.driver = ${?org.postgresql.Driver}
slick.dbs.default.db.url = ${?JDBC_DATABASE_URL}
slick.dbs.default.db.user = ${?JDBC_DATABASE_USERNAME}
slick.dbs.default.db.password = ${?JDBC_DATABASE_PASSWORD}
```

4. Add the Postgres library to the build.sbt dependencies
```
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"
```

You can leave the Sqlite library in there too. The required driver is picked as default to be Sqlite but override with the Heroku Procfile


5. Create a Procfile that gives Heroku some instructions on what to do to start the application. Two important ones are to not the the automatic PLAY evolutions step as the database schema is for Sqlite and needs modification for Postgres. The second item is to define the HTTP port for the application so that the scheduled job can call the REST resources. Procfile also defines the Postgres drive information.
Procfile
```
web: target/universal/stage/bin/rest-swagger-app  -Dhttp.port=${PORT} -Devolutionplugin=disabled -Dplay.evolutions.enabled=false -Dherokuhost=https://secure-retreat-29275.herokuapp.com -Dslick.dbs.default.driver=slick.driver.PostgresDriver\$ -Dslick.dbs.default.db.driver=org.postgresql.Driver
```
6. The database schema between Postgres and Sqlite is different due to the way both Sqlite and Postgres generate the ID field so we cannot use PLAY evolutions easily here. 
Postgres schema
```
create table timeseries (
  id SERIAL,
  name VARCHAR NOT NULL,
  label VARCHAR NOT NULL,
  value VARCHAR NOT NULL
);
```

Basic commands to execute
```
$git init
$git add .
$git commit -m’before push to Herkou’
$heroku create
//output Creating app... done, ⬢ secure-retreat-29275
//output https://secure-retreat-29275.herokuapp.com/ | https://git.heroku.com/secure-retreat-29275.git
$git push heroku master
```

After much scrolling a message similar to this is displayed.
```
remote:        Procfile declares types -> web
remote: -----> Launching...
remote:        Released v4
remote:        https://morning-eyrie-58782.herokuapp.com/ deployed to Heroku
remote: 
remote: Verifying deploy... done.
To https://git.heroku.com/morning-eyrie-58782.git
 * [new branch]      master -> master
```

Now to fix the database using Postgres psql command line. 
```
$heroku pg:psql
$create table timeseries (
$  id SERIAL,
$  name VARCHAR NOT NULL,
$  label VARCHAR NOT NULL,
$  value VARCHAR NOT NULL
$);
$\q
```

Now to restart the application as the database is created
```
$heroku ps:restart
```
To open up the main page on your system’s default browser enter
```
$heroku open
```

Check is something wrong? See the logs with
```
$heroku logs
```
