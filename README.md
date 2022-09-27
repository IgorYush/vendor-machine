# vendor-machine
Welcome to the vendor-machine wiki!

Instalation

Required docker with postgres. To create postgres db, run this command on termial:

docker run --name local-psql -v local_psql_data:/$HOME/ -p 54320:5432 -e POSTGRES_PASSWORD=vendor123 -d postgres

Than you can find in project folder follow file with name vendor_db.sql, that have all schemes needed for app. This file will create user with username admin and password 123, that can be used for creating and interacting with rest of application.
