CREATE TABLE users(
id int8 DEFAULT nextval('user_sequence') PRIMARY KEY ,
username varchar(255) NOT NULL UNIQUE,
password varchar(255) NOT NULL ,
email varchar(255) NOT NULL UNIQUE,
created_at DATE NOT NULL ,
image_url varchar(255) NOT NULL,
roles varchar(10) NOT NULL
verification_code varchar(255) NOT NULL,
verified boolean NOT NULL
);