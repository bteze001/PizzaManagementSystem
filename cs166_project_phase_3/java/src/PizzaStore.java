/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class PizzaStore {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of PizzaStore
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public PizzaStore(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end PizzaStore

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      int defaultWidth = 40;

      int[] colWidths = new int[numCol];

      for (int i = 1; i <= numCol; i++) {
         colWidths[i - 1] = Math.max(rsmd.getColumnName(i).length(), defaultWidth);
      }

      // Build header row with dividers
      StringBuilder header = new StringBuilder();
      header.append("|");
      
      for (int i = 1; i <= numCol; i++) {
         header.append(String.format(" %-" + colWidths[i - 1] + "." + colWidths[i - 1] + "s |", rsmd.getColumnName(i)));
      }
      
      System.out.println(header.toString());

      // Build a divider line that separates header from rows and between rows
      StringBuilder divider = new StringBuilder();
      divider.append("+");
      for (int i = 1; i <= numCol; i++) {
         for (int j = 0; j < colWidths[i - 1] + 2; j++) { 
            divider.append("-");
         }
         divider.append("+");
      }
      System.out.println(divider.toString());

      while (rs.next()){
         StringBuilder row = new StringBuilder();
         row.append("|");
         for (int i = 1; i <= numCol; i++){
            row.append(String.format(" %-" + colWidths[i - 1] + "." + colWidths[i - 1] + "s |", rs.getString(i)));
         }
         System.out.println(row.toString());
         System.out.println(divider.toString()); // divider after each row
         rowCount++;
      }
      stmt.close();
      return rowCount;

      // iterates through the result set and output them to standard out.
      // boolean outputHeader = true;
      // while (rs.next()){
		//  if(outputHeader){
		// 	for(int i = 1; i <= numCol; i++){
		// 	System.out.print(rsmd.getColumnName(i) + "\t");
		// 	}
		// 	System.out.println();
		// 	outputHeader = false;
		//  }
      //    for (int i=1; i<=numCol; ++i)
      //       System.out.print (rs.getString (i) + "\t");
      //    System.out.println ();
      //    ++rowCount;
      // }//end while
      // stmt.close();
      // return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
		for (int i=1; i<=numCol; ++i)
			record.add(rs.getString (i));
        result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            PizzaStore.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      PizzaStore esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the PizzaStore object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new PizzaStore (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. View Profile");
                System.out.println("2. Update Profile");
                System.out.println("3. View Menu");
                System.out.println("4. Place Order"); //make sure user specifies which store
                System.out.println("5. View Full Order ID History");
                System.out.println("6. View Past 5 Order IDs");
                System.out.println("7. View Order Information"); //user should specify orderID and then be able to see detailed information about the order
                System.out.println("8. View Stores"); 

                //**the following functionalities should only be able to be used by drivers & managers**
                System.out.println("9. Update Order Status");

                //**the following functionalities should ony be able to be used by managers**
                System.out.println("10. Update Menu");
                System.out.println("11. Update User");

                System.out.println(".........................");
                System.out.println("20. Log out");
                switch (readChoice()){
                   case 1: viewProfile(esql, authorisedUser); break;
                   case 2: updateProfile(esql, authorisedUser); break;
                   case 3: viewMenu(esql); break;
                   case 4: placeOrder(esql,authorisedUser); break;
                   case 5: viewAllOrders(esql,authorisedUser); break;
                   case 6: viewRecentOrders(esql,authorisedUser); break;
                   case 7: viewOrderInfo(esql); break;
                   case 8: viewStores(esql); break;
                   case 9: updateOrderStatus(esql); break;
                   case 10: updateMenu(esql); break;
                   case 11: updateUser(esql, authorisedUser); break;



                   case 20: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user
    **/
   public static void CreateUser(PizzaStore esql){

      try {

         System.out.print("Create Username: ");
         String login = in.readLine();
         System.out.print("Create Password: ");
         String password = in.readLine();
         System.out.print("Enter your phone number: ");
         String phone = in.readLine();

         String query = "INSERT INTO Users(login, password, role, favoriteItems, phoneNum) VALUES ('"
                        + login + "', '" + password + "', 'customer', NULL, '" + phone + "')";

         esql.executeUpdate(query);
         System.out.println("User created successfully!");
      
      } catch(Exception e) {

         System.err.println(e.getMessage());
      }
   }//end CreateUser


   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(PizzaStore esql){
      
      try {

         System.out.print("Enter your username: ");
         String username = in.readLine();
         System.out.print("Enter your password: ");
         String password = in.readLine();

         String query = "SELECT u.login FROM Users u WHERE u.login = '" + username + "' AND u.password = '" + password + "'";

         List<List<String>> result = esql.executeQueryAndReturnResult(query);

         if(!result.isEmpty()) {

            System.out.println("Login Successful! Welcome " + username);
            return username;
         }

         else {

             System.out.println("Invalid login!");
             return null;
         }
      }

      catch(Exception e) {

         System.err.println(e.getMessage());

      }

      return null;
      
   }//end

// Rest of the functions definition go in here

   public static void viewProfile(PizzaStore esql, String username) {
      
      try {

         String query = "SELECT * FROM Users u WHERE u.login = '" + username + "'";
         esql.executeQueryAndPrintResult(query);
      }

      catch (Exception e) {
         
         System.err.println(e.getMessage());
      }
   }//end viewProfile

   public static void updateProfile(PizzaStore esql, String username) {

      try {

         System.out.println ("Update Profile Menu");
         System.out.println ("1. Change Password");
         System.out.println ("2. Change Phone Number");
         System.out.println ("3. Change Favorite Item");
         System.out.println ("4. Exit");

         int choice = readChoice();

         switch(choice) {

            case 1: 

               System.out.println ("Enter your new password: ");
               String newPassword = in.readLine();

               String passwordQuery = "Update Users SET password = '" + newPassword + "' WHERE login = '" + username + "'";
               esql.executeUpdate(passwordQuery);

               System.out.println ("Password changed successsfully!");

               break;

            case 2: 

               System.out.println ("Enter your new phone number ");
               String newPhone = in.readLine();

               String phoneQuery = "Update Users SET phoneNum = '" + newPhone + "' WHERE login = '" + username + "'";
               esql.executeUpdate(phoneQuery);

               System.out.println ("Phone number changed successsfully!");

               break;

            case 3:

               System.out.println ("Enter your new favorite items (separate by comma): ");
               String newFavorite = in.readLine();

               String favoriteQuery = "Update Users SET favoriteItems = '" + newFavorite + "' WHERE login = '" + username + "'";
               esql.executeUpdate(favoriteQuery);

               System.out.println ("Favorite Items changed successsfully!");

               break;

            case 4:

               System.out.println ("Returning to main menu...");

               break;

            case 5:

               System.out.println ("Invalid choice! Please choose agian");

         }
      }

      catch (Exception e) {
         
         System.err.println(e.getMessage());
      }

   }//end updateProfile

   public static void updateUser(PizzaStore esql, String username) {

      try {

         String checkQuery = "SELECT role FROM Users WHERE login = '" + username + "'";

         List<List<String>> result = esql.executeQueryAndReturnResult(checkQuery);

         if (result.isEmpty() || !result.get(0).get(0).trim().equalsIgnoreCase("manager")) {

            System.out.println("You are not authorized to update user login or role.");

            return;
         }

         System.out.println ("Manager Update Profile");
         System.out.println ("1. Update user login");
         System.out.println ("2. Update user role");
         System.out.println ("3. Exit");

         int choice = readChoice();

         switch(choice) {

            case 1: 

               System.out.println("Enter the current login of the user to update: ");
               String currentLogin = in.readLine();

               System.out.println("Enter the new login: ");
               String newLogin = in.readLine();

              String updateLoginQuery = "UPDATE Users SET login = '" + newLogin + "' WHERE login = '" + currentLogin + "'";

               esql.executeUpdate(updateLoginQuery);

               System.out.println("User login updated successfully from '" + currentLogin + "' to '" + newLogin + "'");

               break;

            case 2:

               System.out.println("Enter the current login of the user to update the role: ");
               String login = in.readLine();

               System.out.println("Enter the new role: ");
               String newRole = in.readLine();

               String updateRoleQuery = "UPDATE Users SET role = '" + newRole + "' WHERE login = '" + login + "'";


               esql.executeUpdate(updateRoleQuery);

               System.out.println("User role updated successfully to '" + newRole + "'");

               break;

            case 3:
               
               System.out.println ("Returning to main menu...");

               break;

            case 4:

               System.out.println ("Invalid choice! Please choose agian");
         }
      }

      catch (Exception e) {
         
         System.err.println(e.getMessage());
      }
   }//end updateUser

   public static void viewMenu(PizzaStore esql) {

      try {

         System.out.println ("View Menu");
         System.out.println ("1. View all items");
         System.out.println ("2. Filter items by type");
         System.out.println ("3. Filter items by price");
         System.out.println ("4. Sort items by price (lowest to highest)");
         System.out.println ("5. Sort items by price (highest to lowest)");
         System.out.println ("6. Exit");

         int choice = readChoice();

         switch(choice) {

            case 1:

               String query = "SELECT * FROM Items";
               esql.executeQueryAndPrintResult(query);

               break;

            case 2:
               System.out.println ("Choose Item type");
               System.out.println ("1. Entrees");
               System.out.println ("2. Drinks");
               System.out.println ("3. Sides");

               int typeChoice = readChoice();

               switch(typeChoice) {

                  case 1:

                     String entreeQuery = "SELECT * FROM Items WHERE TRIM(LOWER(typeOfItem)) = 'entree'";
                     esql.executeQueryAndPrintResult(entreeQuery);

                     break;

                  case 2:

                     String drinkQuery = "SELECT * FROM Items WHERE TRIM(LOWER(typeOfItem)) = 'drinks'";
                     esql.executeQueryAndPrintResult(drinkQuery);

                     break;

                  case 3:

                     String sideQuery = "SELECT * FROM Items WHERE TRIM(LOWER(typeOfItem)) = 'sides'";
                     esql.executeQueryAndPrintResult(sideQuery);

                     break;
               }

               break;
            
            case 3: 

               System.out.println ("Enter the maximum price: ");

               double maxPrice = Double.parseDouble(in.readLine());

               String priceQuery = "SELECT * FROM Items WHERE price <= " + maxPrice;
               esql.executeQueryAndPrintResult(priceQuery);

               break;

            case 4: 

               String sortQuery1 = "SELECT * FROM Items ORDER BY price ASC";
               esql.executeQueryAndPrintResult(sortQuery1);

               break;

            case 5: 

               String sortQuery2 = "SELECT * FROM Items ORDER BY price DESC";
               esql.executeQueryAndPrintResult(sortQuery2);

               break;

            case 6: 

               System.out.println ("Returning to main menu ...");

               break;

            case 7:

               System.out.println ("Invalid choice! Please choose agian");
               
         }

      }

      catch (Exception e) {
         
         System.err.println(e.getMessage());
      }
   }//end viewMenu

   public static void viewAllOrders(PizzaStore esql, String username) {
    try {
        String query = "SELECT * FROM FoodOrder f WHERE f.login = '" + username + "'";
        
        List<List<String>> result = esql.executeQueryAndReturnResult(query);

        if (result.isEmpty()) {
            System.out.println("You have no previous orders.");
        } else {
            System.out.println("Your Order History:");
            for (List<String> row : result) {
                System.out.println("Order ID: " + row.get(0));
                System.out.println("User: " + row.get(1));
                System.out.println("Store ID: " + row.get(2));  
                System.out.println("Total Price: $" + row.get(3));  
                System.out.println("Order Date and Time: " + row.get(4));
                System.out.println("Order Status: " + row.get(5));
                System.out.println("--------------------------------------------------");
            } 
        }
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
   }//end viewAllOrders

   public static void placeOrder(PizzaStore esql, String username) {
    try {
        System.out.println("Enter the store ID where you want to place your order: ");
        int storeID = Integer.parseInt(in.readLine());

        List<String> items = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        double totalPrice = 0.0;

        while (true) {
            System.out.println("Enter item name or hit enter to finish: ");
            String itemName = in.readLine().trim();
            if (itemName.equalsIgnoreCase("done") || itemName.equalsIgnoreCase("")) break;

            String itemQuery = "SELECT price FROM Items WHERE itemName = '" + itemName + "'";
            List<List<String>> itemResult = esql.executeQueryAndReturnResult(itemQuery);

            if (itemResult.isEmpty()) {
                System.out.println("Item not found! Please enter a valid item name.");
                continue;
            }

            double itemPrice = Double.parseDouble(itemResult.get(0).get(0));

            System.out.println("Enter quantity: ");
            int quantity = Integer.parseInt(in.readLine());

            items.add(itemName);
            quantities.add(quantity);
            totalPrice += itemPrice * quantity;
        }

        if (items.isEmpty()) {
            System.out.println("No items were added to the order. Cancelling order.");
            return;
        }

        String orderIDQuery = "SELECT COALESCE(MAX(orderID), 0) + 1 FROM FoodOrder";
        List<List<String>> orderIDResult = esql.executeQueryAndReturnResult(orderIDQuery);
        int orderID = Integer.parseInt(orderIDResult.get(0).get(0));

         //add to table
        String insertOrderQuery = "INSERT INTO FoodOrder(orderID, login, storeID, totalPrice, orderTimestamp, orderStatus) " +
                                  "VALUES (" + orderID + ", '" + username + "', " + storeID + ", " + totalPrice + ", NOW(), 'Placed')";
        esql.executeUpdate(insertOrderQuery);

        for (int i = 0; i < items.size(); i++) {
            String insertItemQuery = "INSERT INTO ItemsInOrder(orderID, itemName, quantity) " +
                                     "VALUES (" + orderID + ", '" + items.get(i) + "', " + quantities.get(i) + ")";
            esql.executeUpdate(insertItemQuery);
        }

        System.out.println("Order placed successfully! Order ID: " + orderID);
        System.out.println("Total Price: $" + String.format("%.2f", totalPrice));

    } catch (Exception e) {
        System.err.println("Error placing order: " + e.getMessage());
    }
   }

   public static void viewRecentOrders(PizzaStore esql, String username) {
      try {
        String query = "SELECT * FROM FoodOrder f WHERE f.login = '" + username + "' LIMIT 5";
        
        List<List<String>> result = esql.executeQueryAndReturnResult(query);

        if (result.isEmpty()) {
            System.out.println("You have no previous orders.");
        } else {
            System.out.println("Your Order History:");
            for (List<String> row : result) {
                System.out.println("Order ID: " + row.get(0));
                System.out.println("User: " + row.get(1));
                System.out.println("Store ID: " + row.get(2));  
                System.out.println("Total Price: $" + row.get(3));  
                System.out.println("Order Date and Time: " + row.get(4));
                System.out.println("Order Status: " + row.get(5));
                System.out.println("--------------------------------------------------");
            } 
        }
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
   }//end viewRecentOrder
   public static void viewOrderInfo(PizzaStore esql) {}
   public static void updateOrderStatus(PizzaStore esql) {}
   public static void viewStores(PizzaStore esql) {}
   public static void updateMenu(PizzaStore esql, String username) {

      try {

         String checkQuery = "SELECT role FROM Users WHERE login = '" + username + "'";

         List<List<String>> result = esql.executeQueryAndReturnResult(checkQuery);

         if (result.isEmpty() || !result.get(0).get(0).trim().equalsIgnoreCase("manager")) {

            System.out.println("You are not authorized to update user login or role.");

            return;
         }

         System.out.println ("Enter the item name to update: ");

         String itemName = in.readLine();

         String itemInfo = "SELECT * FROM Items WHERE itemName = '" + itemName + "'";

         esql.executeQueryAndPrintResult(itemInfo);

         System.out.println ("Choose item information to update");
         System.out.println ("1. Item Name");
         System.out.println ("2. Ingredients");
         System.out.println ("3. Item Type");
         System.out.println ("4. Price");
         System.out.println ("5. Description");
         System.out.println ("6. Exit");
         
         int choice = readChoice();

         switch(choice) {

            case 1: 

               System.out.println ("Enter new item name");
               String newName = in.readLine();

               String nameQuery = "Update Items SET itemName = '" + newName + "' WHERE itemName = '" + itemName + "'";
               esql.executeUpdate(nameQuery);

               System.out.println ("Item name successfully updated to '" + newName + "'");

               break;

            case 2: 

               System.out.println ("Enter new ingredients");
               String newIng = in.readLine();

               String ingQuery = "Update Items SET ingredients = '" + newIng + "' WHERE itemName = '" + itemName + "'";
               esql.executeUpdate(ingQuery);

               

               System.out.println ("Item ingredients successfully updated to '" + newIng + "'");

               break;

            case 3: 

               System.out.println ("Enter new item type");
               String newType = in.readLine();

               String typeQuery = "Update Items SET typeOfItem = '" + newType + "' WHERE itemName = '" + itemName + "'";
               esql.executeUpdate(typeQuery);

               System.out.println ("Item type successfully updated to '" + newType + "'");

               break;

            case 4: 

               System.out.println ("Enter new price");
               String price = in.readLine();

               String priceQuery = "Update Items SET price = '" + price + "' WHERE itemName = '" + itemName + "'";
               esql.executeUpdate(priceQuery);

               System.out.println ("Item price successfully updated to '" + price + "'");

               break;

            case 5: 

               System.out.println ("Enter new description");
               String description = in.readLine();

               String descQuery = "Update Items SET price = '" + description + "' WHERE itemName = '" + itemName + "'";
               esql.executeUpdate(descQuery);

               System.out.println ("Item description successfully updated to '" + description + "'");

               break;

            case 6: 

               System.out.println ("Returning to main menu...");

               break;

            case 7:

                System.out.println ("Invalid choice! Please choose agian");
         }
      }

      catch (Exception e) {
         
         System.err.println(e.getMessage());
      }
   }

}

