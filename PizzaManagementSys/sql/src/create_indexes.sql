IF EXISTS DROP INDEX users_login; 
IF EXISTS DROP INDEX foodorder_login; 
IF EXISTS DROP INDEX foodorder_storeID; 


CREATE INDEX users_login ON Users(login);
CREATE INDEX foodorder_login ON FoodOrder(login);
CREATE INDEX foodorder_storeID ON FoodOrder(storeID);
