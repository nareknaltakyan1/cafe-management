To start a mysql database in a docker container, run:

docker-compose -f src/main/docker/mysql.yml up -d

To stop it and remove the container, run:

docker-compose -f src/main/docker/mysql.yml down
