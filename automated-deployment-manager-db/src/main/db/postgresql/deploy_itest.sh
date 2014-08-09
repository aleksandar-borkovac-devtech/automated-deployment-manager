#!/bin/bash
export CONNECTION_URL="jdbc:postgresql://localhost:5432/adm-db-itest"
export USERNAME=postgres
export PASSWORD=postgres
export CONTEXTS="itest"

./deploy.sh;
