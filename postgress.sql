# --- !Ups

create table timeseries (
  id SERIAL,
  name VARCHAR NOT NULL,
  label VARCHAR NOT NULL,
  value VARCHAR NOT NULL
);
#insert into timeseries (name,label,value) values ( 'London', '1462690680','10.1');
#insert into timeseries (name,label,value) values  ('London','1462691780','10.3');
#insert into timeseries (name,label,value) values ( 'London', '1462692880','10.2');
#insert into timeseries (name,label,value) values ( 'London', '1462693980','10.5');
#insert into timeseries (name,label,value) values ( 'Darlington','1462690680','6.1');
#insert into timeseries (name,label,value) values ( 'Darlington','1462691680','6.5');
#insert into timeseries (name,label,value) values ( 'Darlington','1462692680','7');
#insert into timeseries (name,label,value) values ( 'Darlington','1462693680','7.2');

# --- !Downs

drop table timeseries ;

