#!/bin/bash

export CONNECTION_URL="jdbc:postgresql://localhost:5432/adm-db-demo"
export USERNAME=postgres
export PASSWORD=postgres
export CONTEXTS="demo"

./deploy.sh;
