#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
createdb -h 127.0.0.1 $USER"_project_phase_3_DB"
psql -h 127.0.0.1 -p $PGPORT $USER"_project_phase_3_DB" < $DIR/../src/create_tables.sql
psql -h 127.0.0.1 -p $PGPORT $USER"_project_phase_3_DB" < $DIR/../src/create_indexes.sql
psql -h 127.0.0.1 -p $PGPORT $USER"_project_phase_3_DB" < $DIR/../src/load_data.sql
