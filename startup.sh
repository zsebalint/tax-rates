#!/bin/bash

./gradlew clean bootBuildImage --imageName=bz/tax_rates

docker-compose -f ./docker-compose/docker-compose.yaml up -d