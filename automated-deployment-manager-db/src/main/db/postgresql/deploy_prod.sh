#!/bin/bash
export CONNECTION_URL="jdbc:postgresql://localhost:5432/adm-db"
export USERNAME=postgres
export PASSWORD=postgres
export CONTEXTS="prod,scratch"

./deploy.sh;
