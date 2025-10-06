/* Replace the location to where you saved the data files*/
\copy Users FROM '/PizzaManagementSystem/PizzaManagementSys/data/users.csv' WITH DELIMITER ',' CSV HEADER;

\copy Items FROM '/PizzaManagementSystem/PizzaManagementSys/data/items.csv' WITH DELIMITER ',' CSV HEADER;

\copy Store FROM '/PizzaManagementSystem/PizzaManagementSys/data/store.csv' WITH DELIMITER ',' CSV HEADER;

\copy FoodOrder FROM '/PizzaManagementSystem/PizzaManagementSys/data/foodorder.csv' WITH DELIMITER ',' CSV HEADER;

\copy ItemsInOrder FROM '/PizzaManagementSystem/PizzaManagementSys/data/itemsinorder.csv' WITH DELIMITER ',' CSV HEADER;
