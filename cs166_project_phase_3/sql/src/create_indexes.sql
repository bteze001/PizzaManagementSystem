DROP INDEX IF EXISTS users_login;
DROP INDEX IF EXISTS foodorder_login;
DROP INDEX IF EXISTS foodorder_storeID;


CREATE INDEX users_login ON Users(login);
CREATE INDEX foodorder_login ON FoodOrder(login);
CREATE INDEX foodorder_storeID ON FoodOrder(storeID);
