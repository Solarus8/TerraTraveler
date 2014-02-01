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
	geoloc geography(Point,4326) NOT NULL,
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
	primary_loc INTEGER REFERENCES location(id) ON DELETE CASCADE,
	PRIMARY KEY(id)
);

CREATE TABLE user_contact (
	user_id INTEGER NOT NULL REFERENCES "user"(id),
	contact_id INTEGER NOT NULL REFERENCES "user"(id),
	CONSTRAINT use_cont_pkey PRIMARY KEY (user_id, contact_id)
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

CREATE SEQUENCE place_id_seq;
CREATE TABLE place (
	id INTEGER NOT NULL DEFAULT nextval('place_id_seq'),
	loc_id INTEGER NOT NULL REFERENCES location(id) ON DELETE CASCADE,
	"name" VARCHAR(60) NOT NULL,
	"desc" VARCHAR(256),
	cat VARCHAR(14),
	url VARCHAR(85),
	PRIMARY KEY(id)
);

CREATE TABLE third_party (
	id INTEGER NOT NULL UNIQUE,
	name VARCHAR(32),
	PRIMARY KEY(id) 
);

CREATE SEQUENCE place_thirdparty_id_seq;
CREATE TABLE place_thirdparty (
	id INTEGER NOT NULL DEFAULT nextval('place_thirdparty_id_seq'),
	place_id INTEGER REFERENCES place(id) ON DELETE CASCADE,
	third_party_id INTEGER NOT NULL REFERENCES third_party(id) ON DELETE CASCADE,
	third_party_place_id VARCHAR(256) NOT NULL,
	third_party_place_ref VarChar(256),
	PRIMARY KEY(id)
);

CREATE TABLE activity_category (
	id INTEGER NOT NULL UNIQUE,
	category VARCHAR(64) UNIQUE,
	PRIMARY KEY(id)
);

CREATE TABLE activity_type (
	id INTEGER NOT NULL UNIQUE,
	type VARCHAR(64) UNIQUE,
	PRIMARY KEY(id)
);

CREATE TABLE activity_type_category (
	type_id INTEGER NOT NULL REFERENCES activity_type(id) ON DELETE CASCADE,
	cat_id INTEGER NOT NULL REFERENCES activity_category(id) ON DELETE CASCADE,
	PRIMARY KEY(type_id, cat_id)
);

CREATE SEQUENCE event_id_seq;
CREATE TABLE event (
	id INTEGER NOT NULL DEFAULT nextval('event_id_seq'),
	"from" TIMESTAMP,
	"to" TIMESTAMP,
	title VARCHAR(256),
	activity_type_id INTEGER REFERENCES activity_type(id) ON DELETE CASCADE,
	place_id INTEGER NOT NULL REFERENCES place(id) ON DELETE CASCADE, 
	"desc" VARCHAR(1024),
	min_size INTEGER,
	max_size INTEGER,
	rsvp_tot INTEGER,
	wait_list_tot INTEGER,
	PRIMARY KEY(id)
);

CREATE TABLE preferences (
	id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	pref_1 INTEGER,
	pref_2 INTEGER,
	pref_3 INTEGER,
	PRIMARY KEY(id)
);

CREATE SEQUENCE itin_id_seq;
CREATE TABLE itinerary (
	id INTEGER NOT NULL DEFAULT nextval('itin_id_seq'),
	name VARCHAR(80),
	"desc" VARCHAR(256),
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	date_from TIMESTAMP,
	date_to TIMESTAMP,
	PRIMARY KEY(id)
);

CREATE SEQUENCE user_prof_id_seq;
CREATE TABLE user_profile (
	id INTEGER NOT NULL DEFAULT nextval('user_prof_id_seq'),
	user_id INTEGER UNIQUE NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
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

CREATE SEQUENCE tag_id_seq;
CREATE TABLE tag (
	id INTEGER NOT NULL DEFAULT nextval('tag_id_seq'),
	tag VARCHAR(45),
	PRIMARY KEY(id)
);

CREATE SEQUENCE itin_item_id_seq;
CREATE TABLE itinerary_item (
	id INTEGER NOT NULL DEFAULT nextval('itin_item_id_seq'),
	itinerary_id INTEGER NOT NULL REFERENCES itinerary(id) ON DELETE CASCADE,
	loc_id INTEGER REFERENCES location(id) ON DELETE CASCADE,
	place_id INTEGER REFERENCES place(id) ON DELETE CASCADE,
	contact_id INTEGER REFERENCES "user"(id) ON DELETE CASCADE,
	group_id INTEGER REFERENCES "group"(id) ON DELETE CASCADE,
	event_id INTEGER REFERENCES event(id) ON DELETE CASCADE,
	tag_id INTEGER REFERENCES tag(id) ON DELETE CASCADE,
	date_from TIMESTAMP,
	date_to TIMESTAMP,
	index INTEGER,
	PRIMARY KEY(id)
);

CREATE TABLE user_loc (
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	loc_id INTEGER NOT NULL REFERENCES location(id) ON DELETE CASCADE,
	index INTEGER,
	PRIMARY KEY(user_id, loc_id)
);

CREATE SEQUENCE user_group_id_seq;
CREATE TABLE user_group (
	id INTEGER NOT NULL DEFAULT nextval('user_group_id_seq'),
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	group_id INTEGER NOT NULL REFERENCES "group"(id) ON DELETE CASCADE,
	UNIQUE (user_id, group_id),
	status INTEGER,
	PRIMARY KEY(id)
);

CREATE SEQUENCE user_event_id_seq;
CREATE TABLE user_event (
	id INTEGER NOT NULL DEFAULT nextval('user_event_id_seq'),
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	event_id INTEGER NOT NULL REFERENCES event(id) ON DELETE CASCADE,
	UNIQUE (user_id, event_id),
	status INTEGER,
	PRIMARY KEY(id)
);

CREATE SEQUENCE group_event_id_seq;
CREATE TABLE group_event (
	id INTEGER NOT NULL DEFAULT nextval('group_event_id_seq'),
	group_id INTEGER NOT NULL REFERENCES "group"(id) ON DELETE CASCADE,
	event_id INTEGER NOT NULL REFERENCES event(id) ON DELETE CASCADE,
	status INTEGER,
	PRIMARY KEY(id)
);

CREATE SEQUENCE photo_id_seq;
CREATE TABLE photo (
	id INTEGER NOT NULL DEFAULT nextval('photo_id_seq'),
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	url VARCHAR(80) NOT NULL,
	UNIQUE (user_id, url),
	"desc" VARCHAR(80),
	index INTEGER,
	PRIMARY KEY(id)
);

CREATE TABLE photo_place (
	photo_id INTEGER NOT NULL REFERENCES photo(id) ON DELETE CASCADE,
	place_id INTEGER NOT NULL REFERENCES place(id) ON DELETE CASCADE,
	PRIMARY KEY(photo_id, place_id)
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
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	content VARCHAR(256) NOT NULL,
	date_post TIMESTAMP,
	index INTEGER NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE photo_dispatch (
	photo_id INTEGER NOT NULL REFERENCES photo(id) ON DELETE CASCADE,
	dispatch_id INTEGER NOT NULL REFERENCES dispatch(id) ON DELETE CASCADE,
	PRIMARY KEY(photo_id, dispatch_id)
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
	category VARCHAR(45) NOT NULL,
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

CREATE TABLE status (
	id INTEGER NOT NULL,
	status VARCHAR(16) NOT NULL,
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
DROP TABLE itinerary_item CASCADE;
DROP TABLE photo_dispatch CASCADE;

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
DROP TABLE itinerary CASCADE;
DROP TABLE tag CASCADE;
DROP TABLE activity_type CASCADE;
DROP TABLE status CASCADE;
DROP TABLE activity_category CASCADE;
DROP TABLE activity_type_category CASCADE;
DROP TABLE third_party CASCADE;
DROP TABLE place_thirdparty CASCADE;

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
DROP SEQUENCE role_id_seq CASCADE;
DROP SEQUENCE itin_item_id_seq CASCADE;
DROP SEQUENCE itin_id_seq CASCADE;
DROP SEQUENCE user_cont_id_seq CASCADE;
DROP SEQUENCE tag_id_seq CASCADE;
DROP SEQUENCE user_group_id_seq CASCADE;
DROP SEQUENCE photo_place_id_seq CASCADE;
DROP SEQUENCE group_event_id_seq CASCADE;
DROP SEQUENCE place_thirdparty_id_seq CASCADE;

DROP TABLE play_evolutions;
