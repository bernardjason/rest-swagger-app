# --- !Ups

create table timeseries (
  id INTEGER PRIMARY KEY,
  name VARCHAR NOT NULL,
  label VARCHAR NOT NULL,
  value VARCHAR NOT NULL
);

# --- !Downs

drop table timeseries ;

