#!/bin/bash

find . -name "desktop.ini" -type f -delete		# remove all desktop.ini files.
# docker stop lms
# docker rm lms
# docker stop lms_project_1
# docker rm lms_project_1
# docker stop lms_db_1
# docker rm lms_db_1

## Create JAR file, where docker will install Java in a container to run it.
# docker-compose down -v
# mvn package
# docker-compose up --build

mvn spring-boot:run

## Go into db container with bash.
# docker exec -it lms_db_1 bash
