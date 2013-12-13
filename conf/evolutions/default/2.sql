# Terra Traveler schema
 
# --- !Ups

insert into role (role) values ('BIZ');
insert into role (role) values ('NORM');

insert into "user" (user_name, password, role, email) values ('Homer', 'secret', 'BIZ', 'richard@aqume.com');
insert into "user" (user_name, password, role, email) values ('MJ', 'secret', 'NORM', 'melissa@sample.com');
insert into "user" (user_name, password, role, email) values ('Yubba', 'secret', 'NORM', 'yura@sample.com');

# --- !Downs