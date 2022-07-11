create table categories (
  id int8 default nextval('categories_sequence') primary key ,
  name varchar(255) not null,
  icon_url varchar(800) not null
);
