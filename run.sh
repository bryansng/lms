#!/bin/bash

find . -name "desktop.ini" -type f -delete		# remove all desktop.ini files.
docker stop project_project_1
docker rm project_project_1
docker stop project_db_1
docker rm project_db_1
if [ "$1" == "dev" ]; then
	docker-compose up --build
elif [ "$1" == "reset_mysql" ]; then
	## This is required to remove the instance, so that the init.db of mysql can run and create tables and populate the tables.
	## Remember to remove everything in db_data as well.
	## NOTE: Uncomment below only if you edited the schema.
	docker-compose down -v
	mvn package
	docker-compose up --build
elif [ "$1" == "test_locally" ]; then
	mvn spring-boot:run
elif [ "$1" == "test_via_docker" ]; then
	docker build -t spring-boot-app .
	docker run --name project spring-boot-app:latest
else
	# create JAR file, where docker will install Java in a container to run it.
	# mvn package
	# docker-compose up --build
	mvn spring-boot:run
fi

## Go into db container with bash.
# docker exec -it project_db_1 bash
