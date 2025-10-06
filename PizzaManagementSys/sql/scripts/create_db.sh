# #!/bin/bash
# DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# createdb -h 127.0.0.1 $USER"_project_phase_3_DB"
# psql -h 127.0.0.1 -p $PGPORT $USER"_project_phase_3_DB" < $DIR/../src/create_tables.sql
# psql -h 127.0.0.1 -p $PGPORT $USER"_project_phase_3_DB" < $DIR/../src/create_indexes.sql
# psql -h 127.0.0.1 -p $PGPORT $USER"_project_phase_3_DB" < $DIR/../src/load_data.sql
#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
DB_NAME="pizza_management"

# Create DB
createdb -h 127.0.0.1 -U postgres $DB_NAME

# Load SQL scripts
psql -h 127.0.0.1 -U postgres -d $DB_NAME -f $DIR/../src/create_tables.sql
psql -h 127.0.0.1 -U postgres -d $DB_NAME -f $DIR/../src/create_indexes.sql
psql -h 127.0.0.1 -U postgres -d $DB_NAME -f $DIR/../src/load_data.sql
