# Terra Traveler schema
 
# --- !Ups

insert into role (role) values ('BIZ');
insert into role (role) values ('NORM');

insert into "user" (user_name, password, role, email) values ('Homer', 'secret', 'BIZ', 'richard@aqume.com');
insert into "user" (user_name, password, role, email) values ('MJ', 'secret', 'NORM', 'melissa@sample.com');
insert into "user" (user_name, password, role, email) values ('Yubba', 'secret', 'NORM', 'yura@sample.com');

insert into user_profile (user_id, first_name, last_name, gender, nationality, portrait_url, bio) 
	values (1, 'Richard', 'Walker', 'male', 'Merkin', 'http://www.codemonkey.com', 'How did I accomplish so little?');
insert into user_profile (user_id, first_name, last_name, gender, nationality, portrait_url, bio) 
	values (2, 'Melissa', 'Morgan', 'female', 'England', 'http://www.melissa.com', 'I am very nice!');
insert into user_profile (user_id, first_name, last_name, gender, nationality, portrait_url, bio) 
	values (3, 'Yura', 'Yakov', 'male', 'Russian', 'http://www.yura.com', 'Working to make Siberia a warmer place!');
	
insert into location (id, city)
	values (1, 'Warsaw');
	
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