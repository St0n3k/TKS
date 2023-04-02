#!/bin/bash
psql --username rent -d rentdb -c "CREATE USER userr WITH PASSWORD 'userr';"
psql --username rent -d rentdb -c "CREATE DATABASE userdb OWNER userr;"