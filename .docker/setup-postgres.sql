create database cafe;
create user cafe_user with encrypted password 'cafe_user';
grant all privileges on database cafe to cafe_user;