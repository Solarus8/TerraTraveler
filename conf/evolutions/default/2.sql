# Terra Traveler schema
 
# --- !Ups

insert into role (role) values ('BIZ');
insert into role (role) values ('NORM');

insert into location (id, city, latitude, longitude)
	values (1, 'San Francisco', 37.774929, -122.419416);
insert into location (id, city, latitude, longitude)
	values (2, 'Mountain View', 37.386052, -122.083851);
insert into location (id, city, latitude, longitude)
	values (3, 'Boston', 42.358431, -71.059773);
insert into location (id, city, latitude, longitude)
	values (4, 'Shanghai', 31.239597, 121.486806);
insert into location (id, city, latitude, longitude)
	values (5, 'Warsaw', 52.229676, 21.012229);
insert into location (id, city, latitude, longitude)
	values (6, 'Singapore', 1.352083, 103.819836);
insert into location (id, city, latitude, longitude)
	values (7, 'London', 51.511214, -0.119824);
insert into location (id, city, latitude, longitude)
	values (8, 'Madrid', 40.416775, -3.703790);
insert into location (id, city, latitude, longitude)
	values (9, 'Novosibirsk', 55.016667, 82.933333);
insert into location (id, city, latitude, longitude)
	values (10, 'Tokyo', 35.689487, 139.691706);
insert into location (id, city, latitude, longitude)
	values (11, 'Austin', 30.267153, -97.743061);
Insert into location (id, city, latitude, longitude)
	values (12, 'Guatamala City', 14.624795, -90.532818);
Insert into location (id, city, latitude, longitude)
	values (13, 'San Paulo', -23.550520, -46.633309);
Insert into location (id, city, latitude, longitude)
	values (14, 'Santa Clara', 37.354108, -121.955236);
Insert into location (id, city, latitude, longitude)
	values (15, 'Istanbul', 41.005270, 28.976960);
Insert into location (id, city, latitude, longitude)
	values (16, 'Cork', 51.896892, -8.486316);

insert into "user" (id, user_name, password, role, email, primary_loc) values (1, 'Homer', 'secret', 'BIZ', 'richard@aqume.com', 1);
insert into "user" (id, user_name, password, role, email, primary_loc) values (2, 'MJ', 'secret', 'NORM', 'melissa@sample.com', 1);
insert into "user" (id, user_name, password, role, email, primary_loc) values (3, 'Yubba', 'secret', 'NORM', 'yura@sample.com', 1);
insert into "user" (id, user_name, password, role, email, primary_loc) values (4, 'Jaz', 'secret', 'BIZ', 'jasper@sample.com', 2);
insert into "user" (id, user_name, password, role, email, primary_loc) values (5, 'Cooter', 'secret', 'NORM', 'kellyo@sample.com', 2);
insert into "user" (id, user_name, password, role, email, primary_loc) values (6, 'Baba', 'secret', 'NORM', 'basil888@sample.com', 2);
insert into "user" (id, user_name, password, role, email, primary_loc) values (7, 'gage', 'secret', 'BIZ', 'gg@sample.com', 3);
insert into "user" (id, user_name, password, role, email, primary_loc) values (8, 'MT', 'secret', 'NORM', 'mm1K32@sample.com', 3);
insert into "user" (id, user_name, password, role, email, primary_loc) values (9, 'Boingo', 'secret', 'NORM', 'ttt666@sample.com', 3);
insert into "user" (id, user_name, password, role, email, primary_loc) values (10, 'starchild', 'secret', 'BIZ', 'rr5@sample.com', 4);
insert into "user" (id, user_name, password, role, email, primary_loc) values (11, 'Crunchy', 'secret', 'NORM', 'rr4@sample.com', 4);
insert into "user" (id, user_name, password, role, email, primary_loc) values (12, 'spacey', 'secret', 'NORM', 'rr3@sample.com', 4);
insert into "user" (id, user_name, password, role, email, primary_loc) values (13, 'papo', 'secret', 'BIZ', 'rr2@aqume.com', 5);
insert into "user" (id, user_name, password, role, email, primary_loc) values (14, 'QP', 'secret', 'NORM', 'rrr8@sample.com', 5);
insert into "user" (id, user_name, password, role, email, primary_loc) values (15, 'Yazzo', 'secret', 'NORM', 'rrr7@sample.com', 5);
insert into "user" (id, user_name, password, role, email, primary_loc) values (16, 'KingKong', 'secret', 'BIZ', 'rrr6@sample.com', 6);
insert into "user" (id, user_name, password, role, email, primary_loc) values (17, 'Zippo', 'secret', 'NORM', 'rrr5@sample.com', 6);
insert into "user" (id, user_name, password, role, email, primary_loc) values (18, 'Mike', 'secret', 'NORM', 'rrr4@sample.com', 6);
insert into "user" (id, user_name, password, role, email, primary_loc) values (19, 'Flack', 'secret', 'BIZ', 'rrr3@sample.com', 7);
insert into "user" (id, user_name, password, role, email, primary_loc) values (20, 'Hector', 'secret', 'NORM', 'sss6@sample.com', 7);
insert into "user" (id, user_name, password, role, email, primary_loc) values (21, 'Peet', 'secret', 'NORM', 'sss5@sample.com', 7);
insert into "user" (id, user_name, password, role, email, primary_loc) values (22, 'Casey', 'secret', 'BIZ', 'sss4@sample.com', 8);
insert into "user" (id, user_name, password, role, email, primary_loc) values (23, 'Pooter', 'secret', 'NORM', 'fff4@sample.com', 8);
insert into "user" (id, user_name, password, role, email, primary_loc) values (24, 'Lamphrey', 'secret', 'NORM', 'ggg2@sample.com', 8);

insert into user_profile (user_id, first_name, last_name, gender, nationality, portrait_url, bio) 
	values (1, 'Richard', 'Walker', 'male', 'Merkin', 'http://www.codemonkey.com', 'I yam what I yam');
insert into user_profile (user_id, first_name, last_name, gender, nationality, portrait_url, bio) 
	values (2, 'Melissa', 'Morgan', 'female', 'England', 'http://www.melissa.com', 'I am very nice!');
insert into user_profile (user_id, first_name, last_name, gender, nationality, portrait_url, bio) 
	values (3, 'Yura', 'Yakov', 'male', 'Russian', 'http://www.yura.com', 'Working to make Siberia a warmer place!');
	
insert into event (date, loc_id, "desc", min_size, max_size)
	values ('2014-01-14', '1', 'Pub crawl along Danube', '4', '20');
	
insert into event (date, loc_id, "desc", min_size, max_size)
	values ('2014-01-06', '1', 'Sky diving at Lasha Karnak', '8', '30');
	
insert into event (date, loc_id, "desc", min_size, max_size)
	values ('2014-02-01', '1', 'Picnic at Hanging Rock', '6', '40');

# --- !Downs

delete from user_profile;
delete from "user";
delete from "role";
delete from event;
delete from location;