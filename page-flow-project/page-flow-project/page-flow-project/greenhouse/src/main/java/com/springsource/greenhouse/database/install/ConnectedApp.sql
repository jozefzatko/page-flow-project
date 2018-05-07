create table App (id SERIAL,
				name varchar not null unique, 
				slug varchar not null unique,
				description varchar not null,
				organization varchar,
				website varchar,
				apiKey varchar unique,
				secret varchar not null unique,
				callbackUrl varchar,
				primary key (id));

create table AppDeveloper (app bigint,
				member bigint, 
				primary key (app, member),
				foreign key (app) references App(id) on delete cascade,				
				foreign key (member) references Member(id));

create table oauth_client_token (
				token_id VARCHAR(256),
				token INTEGER,
				authentication_id VARCHAR(256),
				user_name VARCHAR(256),
				client_id VARCHAR(256));

create table oauth_access_token (
				token_id VARCHAR(256),
				token INTEGER,
				authentication_id VARCHAR(256),
				user_name VARCHAR(256),
				client_id VARCHAR(256),
				authentication INTEGER,
				refresh_token VARCHAR(256));

create table oauth_refresh_token (
				token_id VARCHAR(256),
				token INTEGER,
				authentication INTEGER
);

create table oauth_code (
				code VARCHAR(256), authentication INTEGER
);
