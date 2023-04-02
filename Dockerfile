FROM postgres:15.2

COPY --chown=999 --chmod=600 ./createdb.sh   /docker-entrypoint-initdb.d/10-createdb.sh