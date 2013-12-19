# Terra Traveler schema

# --- !Ups

CREATE SEQUENCE role_id_seq;
CREATE TABLE role (
    id integer NOT NULL DEFAULT nextval('role_id_seq'),
    "role" VARCHAR(20) UNIQUE NOT NULL,
    PRIMARY KEY (id)
);

CREATE SEQUENCE loc_id_seq;
CREATE TABLE location (
	id INTEGER NOT NULL DEFAULT nextval('loc_id_seq'),
	city VARCHAR(45),
	country VARCHAR(45),
	address_1 VARCHAR(45),
	address_2 VARCHAR(45),
	address_3 VARCHAR(45),
	postal_code VARCHAR(20),
	latitude FLOAT,
	longitude FLOAT,
	altitude INTEGER,
	"desc" VARCHAR(256),
	url VARCHAR(60),
	PRIMARY KEY(id)
);

CREATE SEQUENCE user_id_seq;
CREATE TABLE "user" (
	id INTEGER NOT NULL DEFAULT nextval('user_id_seq'),
	created TIMESTAMP NOT NULL DEFAULT current_timestamp,
	last_active TIMESTAMP,
	last_login TIMESTAMP,
	user_name VARCHAR(45) UNIQUE,
	email VARCHAR(60),
	password VARCHAR(20),
	"role" VARCHAR(20) REFERENCES "role"("role") ON DELETE CASCADE,
	primary_loc INTEGER REFERENCES location("id") ON DELETE CASCADE,
	PRIMARY KEY(id)
);

CREATE SEQUENCE group_id_seq;
CREATE TABLE "group" (
	id INTEGER NOT NULL DEFAULT nextval('group_id_seq'),
	organizer INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	group_name VARCHAR(60),
	"desc" VARCHAR(256),
	min_size INTEGER,
	max_size INTEGER,
	PRIMARY KEY(id)
);

CREATE SEQUENCE event_id_seq;
CREATE TABLE event (
	id INTEGER NOT NULL DEFAULT nextval('event_id_seq'),
	"date" TIMESTAMP,
	loc_id INTEGER NOT NULL REFERENCES location(id) ON DELETE CASCADE, 
	"desc" VARCHAR(256),
	min_size INTEGER,
	max_size INTEGER,
	rsvp_tot INTEGER,
	wait_list_tot INTEGER,
	PRIMARY KEY(id)
);

CREATE SEQUENCE place_id_seq;
CREATE TABLE place (
	id INTEGER NOT NULL DEFAULT nextval('place_id_seq'),
	loc_id INTEGER NOT NULL REFERENCES location(id) ON DELETE CASCADE,
	"name" VARCHAR(60),
	"desc" VARCHAR(256),
	"type" VARCHAR(14),
	url VARCHAR(85),
	PRIMARY KEY(id)
);

CREATE TABLE preferences (
	id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	pref_1 INTEGER,
	pref_2 INTEGER,
	pref_3 INTEGER,
	PRIMARY KEY(id)
);

CREATE SEQUENCE user_prof_id_seq;
CREATE TABLE user_profile (
	id INTEGER NOT NULL DEFAULT nextval('user_prof_id_seq'),
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	first_name VARCHAR(45),
	last_name VARCHAR(45),
	gender CHAR(8),
	birthdate TIMESTAMP,
	nationality VARCHAR(45),
	portrait_url VARCHAR(80),
	bio VARCHAR(256),
	story VARCHAR(256),
	PRIMARY KEY(id)
);

CREATE TABLE user_contact (
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	contact_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	index INTEGER,
	PRIMARY KEY(user_id, contact_id)
);

CREATE TABLE user_loc (
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	loc_id INTEGER NOT NULL REFERENCES location(id) ON DELETE CASCADE,
	index INTEGER,
	PRIMARY KEY(user_id, loc_id)
);

CREATE TABLE user_group (
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	group_id INTEGER NOT NULL REFERENCES "group"(id) ON DELETE CASCADE,
	PRIMARY KEY(user_id, group_id)
);

CREATE SEQUENCE user_event_id_seq;
CREATE TABLE user_event (
	id INTEGER NOT NULL DEFAULT nextval('user_event_id_seq'),
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	event_id INTEGER NOT NULL REFERENCES event(id) ON DELETE CASCADE,
	status INTEGER,
	PRIMARY KEY(id)
);

CREATE TABLE group_event (
	group_id INTEGER NOT NULL REFERENCES "group"(id) ON DELETE CASCADE,
	event_id INTEGER NOT NULL REFERENCES event(id) ON DELETE CASCADE,
	PRIMARY KEY(group_id, event_id)
);

CREATE SEQUENCE photo_id_seq;
CREATE TABLE photo (
	id INTEGER NOT NULL DEFAULT nextval('photo_id_seq'),
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	url VARCHAR(80),
	"desc" VARCHAR(80),
	index INTEGER,
	PRIMARY KEY(id)
);

CREATE TABLE photo_place (
	photo_id INTEGER NOT NULL REFERENCES photo(id) ON DELETE CASCADE,
	biz_id INTEGER NOT NULL REFERENCES place(id) ON DELETE CASCADE,
	PRIMARY KEY(photo_id, biz_id)
);

CREATE TABLE photo_location (
	photo_id INTEGER NOT NULL REFERENCES photo(id) ON DELETE CASCADE,
	loc_id INTEGER NOT NULL REFERENCES location(id) ON DELETE CASCADE,
	PRIMARY KEY(photo_id, loc_id)
);

CREATE TABLE photo_group (
	photo_id INTEGER NOT NULL REFERENCES photo(id) ON DELETE CASCADE,
	group_id INTEGER NOT NULL REFERENCES "group"(id) ON DELETE CASCADE,
	PRIMARY KEY(photo_id, group_id)
);

CREATE TABLE photo_event (
	photo_id INTEGER NOT NULL REFERENCES photo(id) ON DELETE CASCADE,
	event_id INTEGER NOT NULL REFERENCES event(id) ON DELETE CASCADE,
	PRIMARY KEY(photo_id, event_id)
);

CREATE SEQUENCE disp_id_seq;
CREATE TABLE dispatch (
	id INTEGER NOT NULL DEFAULT nextval('disp_id_seq'),
	disp VARCHAR(256),
	index INTEGER,
	PRIMARY KEY(id)
);

CREATE SEQUENCE int_id_seq;
CREATE TABLE interest (
	id INTEGER NOT NULL DEFAULT nextval('int_id_seq'),
	interest VARCHAR(45),
	PRIMARY KEY(id)
);

CREATE TABLE user_interest (
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	int_id  INTEGER NOT NULL REFERENCES interest(id) ON DELETE CASCADE,
	PRIMARY KEY(user_id, int_id)
);

CREATE SEQUENCE placecat_id_seq;
CREATE TABLE placecategory (
	id INTEGER NOT NULL DEFAULT nextval('placecat_id_seq'),
	category VARCHAR(45),
	PRIMARY KEY(id)
);

CREATE TABLE place_category (
	place_id INTEGER NOT NULL REFERENCES place(id) ON DELETE CASCADE,
	cat_id INTEGER NOT NULL REFERENCES placecategory(id) ON DELETE CASCADE,
	PRIMARY KEY(place_id, cat_id)
);

CREATE SEQUENCE promo_id_seq;
CREATE TABLE promo (
	id INTEGER NOT NULL DEFAULT nextval('promo_id_seq'),
	group_id INTEGER NOT NULL REFERENCES "group"(id) ON DELETE CASCADE,
	event_id INTEGER NOT NULL REFERENCES event(id) ON DELETE CASCADE,
	place_id INTEGER NOT NULL REFERENCES place(id) ON DELETE CASCADE,
	"desc" VARCHAR(156),
	promo_code VARCHAR(20),
	status VARCHAR(16),
	active BOOL,
	start_time TIMESTAMP,
	end_time TIMESTAMP,
	PRIMARY KEY(id)
);

# --- !Downs

DROP TABLE user_contact CASCADE;
DROP TABLE user_profile CASCADE;
DROP TABLE user_group CASCADE;
DROP TABLE group_event CASCADE;
DROP TABLE photo_place CASCADE;
DROP TABLE photo_location CASCADE;
DROP TABLE photo_group CASCADE;
DROP TABLE photo_event CASCADE;
DROP TABLE user_loc CASCADE;
DROP TABLE user_interest CASCADE;
DROP TABLE place_category CASCADE;
DROP TABLE user_event CASCADE;

DROP TABLE role CASCADE;
DROP TABLE "group" CASCADE;
DROP TABLE preferences CASCADE;
DROP TABLE photo CASCADE;
DROP TABLE "dispatch" CASCADE;
DROP TABLE interest CASCADE;
DROP TABLE event CASCADE;
DROP TABLE place CASCADE;
DROP TABLE placecategory CASCADE;
DROP TABLE promo CASCADE;
DROP TABLE "user" CASCADE;
DROP TABLE location CASCADE;
DROP TABLE business CASCADE;

DROP SEQUENCE user_prof_id_seq CASCADE;
DROP SEQUENCE group_id_seq CASCADE;
DROP SEQUENCE photo_id_seq CASCADE;
DROP SEQUENCE disp_id_seq CASCADE;
DROP SEQUENCE loc_id_seq CASCADE;
DROP SEQUENCE int_id_seq CASCADE;
DROP SEQUENCE place_id_seq CASCADE;
DROP SEQUENCE event_id_seq CASCADE;
DROP SEQUENCE placecat_id_seq CASCADE;
DROP SEQUENCE promo_id_seq CASCADE;
DROP SEQUENCE role_id_seq CASCADE;
DROP SEQUENCE user_id_seq CASCADE;
DROP SEQUENCE user_event_id_seq CASCADE;
DROP SEQUENCE role_id_seq;

DROP TABLE play_evolutions;
