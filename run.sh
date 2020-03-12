#!/bin/bash

find . -name "desktop.ini" -type f -delete		# remove all desktop.ini files.

## Create JAR file, where docker will install Java in a container to run it.
if [ "$1" == "dev" ]; then
	mvn spring-boot:run
else
	docker-compose down -v
	mvn package
	docker-compose up --build
fi

## Go into db container with bash.
# docker exec -it lms_db_1 bash
