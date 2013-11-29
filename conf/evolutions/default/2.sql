# Terra Traveler schema
 
# --- !Ups

insert into role (role) values ('BIZ');
insert into role (role) values ('NORM');

insert into "user" (user_name, password, role, email) values ('Homer', 'Secret', 'BIZ', 'richard@aqume.com');
insert into "user" (user_name, password, role, email) values ('MJ', 'Secret', 'NORM', 'Melissa@sample.com');
insert into "user" (user_name, password, role, email) values ('Yubba', 'Secret', 'NORM', 'Yuri@sample.com');

# --- !Downs