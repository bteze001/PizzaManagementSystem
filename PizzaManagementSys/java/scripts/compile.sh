#!/bin/bash
# --------------------------------------------------------------------
# Pizza Management System - Compile & Run Script
# --------------------------------------------------------------------
export JAVA_HOME="$(brew --prefix)/opt/openjdk@17"
export PATH="$JAVA_HOME/bin:$PATH"

# Get current directory (this script's directory)
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# ---- Configuration ----
DB_NAME="pizzamanagement"
DB_USER="postgres"
DB_PORT="5433"
JDBC_JAR="$DIR/../lib/postgresql-42.7.1.jar"
MAIN_CLASS="PizzaStore"

# ---- Step 1: Compile Java source ----
echo "Compiling Java source files..."
javac -d "$DIR/../classes" "$DIR/../src/$MAIN_CLASS.java"

if [ $? -ne 0 ]; then
    echo "❌ Compilation failed. Please check for syntax errors."
    exit 1
fi

echo "✅ Compilation successful."

# ---- Step 2: Run Java program ----
echo "Starting Pizza Management System..."
java -cp "$DIR/../classes:$JDBC_JAR" "$MAIN_CLASS" "$DB_NAME" "$DB_PORT" "$DB_USER"