# --- !Ups

create table "timeseries" (
  "id" INTEGER PRIMARY KEY,
  "name" VARCHAR NOT NULL,
  "label" VARCHAR NOT NULL,
  "value" VARCHAR NOT NULL
 );

insert into "timeseries" ("id","name","label","value") values ( 1,"motivation","6","4");
insert into "timeseries" ("id","name","label","value") values  ( 2,"motivation","7","5");
insert into "timeseries" ("id","name","label","value") values ( 3,"motivation","8","5");
insert into "timeseries" ("id","name","label","value") values ( 4,"motivation","9","6");
insert into "timeseries" ("id","name","label","value") values ( 5,"motivation","10","6");
insert into "timeseries" ("id","name","label","value") values ( 6,"motivation","11","7");
insert into "timeseries" ("id","name","label","value") values ( 7,"motivation","12","8");

insert into "timeseries" ("id","name","label","value") values ( 10,"energy","6","8");
insert into "timeseries" ("id","name","label","value") values ( 11,"energy","7","6");
insert into "timeseries" ("id","name","label","value") values ( 12,"energy","8","4");
insert into "timeseries" ("id","name","label","value") values ( 13,"energy","9","2");

# --- !Downs

drop table "timeseries" if exists;

