package backend;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.HashMap;
import java.util.Set;
import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
/**
 * The class that enables communication between the application and the H2 database.
 */
public class DatabaseManagerH2 
{
	    /**
	     * Maximum time during which products are reserved for a customer
	     */
	    private final long reservationLength = 600;
	    
	    /**
	     * Credit with which a customer begins with (for testing purposes)
	     */
	    private final double initialCredit = 10000;
	    
	    /**
	     * Product manager that helps to synchronize in-app products with the database
	     */
	    private ProductManager productManager;
	    
	    /**
	     * Customer manager that contains a list of all customers with an account
	     */
	    private CustomerManager customerManager;
	   
	    public DatabaseManagerH2()
	    {
	    	initializeDatabase();
	    	//dropAllSchemas();
	    	this.productManager = new ProductManager(this);
	    	this.customerManager = new CustomerManager();
	    }
	    
	    public ProductManager getProductManager()
	    {
	    	return productManager;
	    }
	    
	    public CustomerManager getCustomerManager()
	    {
	    	return customerManager;
	    }
	   
	    /**
	     * Checks if the database already exists before trying to create it.
	     * @return
	     */
	    public boolean isDatabaseInitialized() {
	        String url = "jdbc:h2:~/projectdbTest";
	    	//String url = "jdbc:h2:./projectdbTest";
	        String user = "sa";
	        String password = "";

	        try
	        {
	        	Class.forName("org.h2.Driver");
	        	Connection connection = DriverManager.getConnection(url, user, password);
	             Statement statement = connection.createStatement();
	            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME ='ADMINDATABASE'");
	            if (resultSet.next()) {
	                int tableCount = resultSet.getInt(1);
	                return tableCount > 0;
	            }
	        } 
	            catch (SQLException | ClassNotFoundException e) {
	            e.printStackTrace();
	        }

	        return false;
	    }
	    
	    /**
	     * Initializes the database on the local machine if it doesn't exist.
	     */
	    public void initializeDatabase() 
	    {
	        String url = "jdbc:h2:~/projectdbTest"; 
	    	//String url = "jdbc:h2:./projectdbTest";
	        String user = "sa";
	        String password = "";
	        
	        
	        if (isDatabaseInitialized())
	        {
	        	return;
	        }
	        

	        try
	        {
	        	Class.forName("org.h2.Driver");
	        	Connection connection = DriverManager.getConnection(url, user, password);
	            Statement statement = connection.createStatement();
	            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("backup.sql");

	            if (inputStream == null) 
	            {
	                throw new FileNotFoundException("Le fichier backup.sql est introuvable dans le classpath.");
	            }

	            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
	            StringBuilder sqlScript = new StringBuilder();
	            String line;
	            while ((line = reader.readLine()) != null) 
	            {
	                if (!line.trim().isEmpty() && !line.trim().startsWith("--")) 
	                {
	                    sqlScript.append(line).append("\n");
	                }
	            }

	            // Divide and execute sql queries
	            String[] sqlCommands = sqlScript.toString().split(";");
	            for (String command : sqlCommands) 
	            {
	                if (!command.trim().isEmpty()) 
	                {
	                    statement.execute(command.trim());
	                }
	            }
	            System.out.println("The dataBase has been initialized.");
	            addProductsToDatabase();
	        }
	        catch (Exception e) 
	        {
	            e.printStackTrace();
	        }
	        
	    }
	    
	    
	    /**
	     * Establishes a connection with the H2 database.
	     * @return A connection with the H2 database.
	     */
	    public Connection getH2Connection()
	    {
	        try 
	        {
	            Class.forName("org.h2.Driver");
	            String url = "jdbc:h2:~/projectdbTest";
	        	//String url = "jdbc:h2:./projectdbTest";
	            String user = "sa";
	            String password = "";
	            return DriverManager.getConnection(url, user, password);
	        } 
	        catch (SQLException | ClassNotFoundException e) 
	        {
	            System.out.println("Error when trying to getH2Connection to the database" + e.getMessage());
	            throw new RuntimeException(e);
	        }
	    }
	    
	    /**
	     * Removes the database from the local machine.
	     */
	    public void dropAllSchemas() {
            String url = "jdbc:h2:~/projectdbTest";
	    	//String url = "jdbc:h2:./projectdbTest";
            String user = "sa";
            String password = "";
	    	
	        try (Connection connection = DriverManager.getConnection(url, user, password);
	             Statement statement = connection.createStatement()) {

	            // Query all schemas except the system schemas
	            ResultSet resultSet = statement.executeQuery(
	                "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME NOT IN ('INFORMATION_SCHEMA', 'PUBLIC')"
	            );

	            // Drop each schema
	            while (resultSet.next()) {
	                String schemaName = resultSet.getString("SCHEMA_NAME");
	                System.out.println("Dropping schema: " + schemaName);
	                statement.execute("DROP SCHEMA " + schemaName + " CASCADE");
	            }

	            System.out.println("All schemas dropped successfully.");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    /**
	     * Enum used for various returns in functions, mostly when checking constraints in the database.
	     */
	    public enum QueryStatus
	    {
	    	INSERT_SUCCESSFUL,
	    	LOGIN_SUCCESSFUL,
	    	EMAIL_ERROR,
	    	USERNAME_ERROR,
	    	UNKNOWN_ERROR,
	    	LOGIN_ERROR,
	    	WRONG_EMAIL_PATTERN,
	    	EMPTY_NAME,
	    	EMPTY_FIRST_NAME,
	    	EMPTY_EMAIL,
	    	EMPTY_USERNAME,
	    	EMPTY_PASSWORD,
	    	WRONG_CREDIT_CODE
	    	
	    }
	    
	    
	    /**
	     * Generates a salt value with which the customer's password is encoded.
	     * @param password The password as it was typed by the user.
	     * @return The hashed password using this salt value.
	     */
	    private String hashPassword(String password) 
	    {
	        return BCrypt.hashpw(password, BCrypt.gensalt(12)); 
	    }
	    
	    /**
	     * Matches the plain password with the hashed password.
	     * @param password The password as it was typed.
	     * @param hashedPassword The hashed password to compare the plain one to.
	     * @return True if the typed password matches the hashed one, false otherwise.
	     */
	    private boolean checkPassword(String password, String hashedPassword) 
	    {
	        return BCrypt.checkpw(password, hashedPassword);
	    }
	    
	    /**
	     * Inserts a new admin profile in the database.
	     */
	    public void insertAdmin()
	    {
	    	String insertionQuery = "INSERT INTO ADMINDATABASE.ADMINS(adminUsername, adminPassword) VALUES (?,?)";
	    	
	        try
	        {
	        	Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(insertionQuery);
	        	//Replace the placeholders "?" with the values to insert
	        	statement.setString(1, "Username");
	        	statement.setString(2, hashPassword("Password"));
	        	
	        	//Execute the update 
	        	statement.executeUpdate();
	        }

	        
	        catch (SQLException e) 
	        {
	        	String errorMessage = e.getMessage();
	        	System.out.println(errorMessage);
	        	
	        }
	    	
	    
	    }
	    
	    /**
	     * Inserts the customer in the database and creates a new customerProfile 
	     * 
	     * @param name The customer's first name
	     * @param firstName The customer's last name
	     * @param email The customer's email
	     * @param username The customer's username
	     * @param password The customer's password
	     * @return Information indicating whether the insertion was successful or details about potential errors
	     */
	    public QueryStatus insertNewClient(String name, String firstName, String email, String username, String password)
	    {
	    	String insertionQuery = "INSERT INTO ADMINDATABASE.CLIENTS(lastname, firstname, email, username, userPassword, credit, vip) VALUES (?,?,?,?,?,?,?)";

	    	//An email validator matches the email against an elaborated regex 
	    	EmailValidator emailValidator = EmailValidator.getInstance();
	    	if (!emailValidator.isValid(email))
	    		return QueryStatus.WRONG_EMAIL_PATTERN;
	 
	    	
	    	
	        try  
	        {
	        	//Connects to the database
	        	Connection conn = getH2Connection();
	        	
	        	//Prepares the String query by placing placeholders
	            PreparedStatement statement = conn.prepareStatement(insertionQuery);
	            
	            //Replace the placeholders "?" with the values to insert
	            statement.setString(1, name);  
	            statement.setString(2, firstName);   
	            statement.setString(3, email); 
	            statement.setString(4, username); 
	            statement.setString(5, hashPassword(password)); 
	            statement.setDouble(6, initialCredit);
	            statement.setBoolean(7, false);
	            statement.executeUpdate();
	            
	            //Creates a new in-app customer profile with all their personal information
	            customerManager.setCurrentCustomer(new CustomerProfile(name, firstName, email, username, password, initialCredit,false, new BasketManager(productManager)));
	        	
	            return QueryStatus.INSERT_SUCCESSFUL;
	        }


	        catch (SQLException e) 
	        {
	        	String errorMessage = e.getMessage();
	        	System.out.println(errorMessage);
	        	//Returns various information on the nature of the error used to notify the user
	            if (errorMessage.contains("email"))
	            	return QueryStatus.EMAIL_ERROR;
	            if (errorMessage.contains("username"))
	        		return QueryStatus.USERNAME_ERROR;
	        	return QueryStatus.UNKNOWN_ERROR;
	        	
	        }
	    	
	    }
	    
	    /**
	     * Tries to find an admin existing profile with their username and password.
	     * @param username The username typed by the admin.
	     * @param password The password typed by the admin.
	     * @return Information indicating whether the login was successful or details about possible errors.
	     */
	    public QueryStatus loginAdmin(String username, String password)
	    {
	    	removePremiumStatuses();
	    	//Creates a new product filter that will load the products in their current state in the database
	    	productManager = new ProductManager(this);
	    	
	    	//SQL query trying to find an admin account matching this user and password
	    	String selectionQuery = "SELECT adminPassword FROM ADMINDATABASE.ADMINS WHERE adminUsername = ?";
	        
	        try 
	        {
	        	Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(selectionQuery);

	            //Replace the placeholders "?" with the values to insert
	     	   	statement.setString(1, username);    
	     	   	
	     	   	//Gets all the tuples in the database that match this admin username
	            ResultSet result = statement.executeQuery();
	            if (result.next()) 
	            {
	                String hashedPassword = result.getString("adminPassword");
	                
	                //Check if the password entered matches the hashed password in the database
	                if (checkPassword(password, hashedPassword))
	             	   return QueryStatus.LOGIN_SUCCESSFUL;
	                //If password incorrect
	                return QueryStatus.LOGIN_ERROR;

	            } 
	            //This admin username doesn"t exist
	            else 
	            {
	                return QueryStatus.LOGIN_ERROR;
	            }
	        }


	           catch (SQLException e) 
	           {
	        	   	System.out.println(e.getMessage());
	           		return QueryStatus.UNKNOWN_ERROR;
	           }
	    }
	    
	    /**
	     * Retrieve the information about the customer from their username and password.
	     * @param username The username typed by the customer.
	     * @param password The password typed by the customer.
	     * @return Information indicating whether the login was successful or details about possible errors.
	     */
	    public QueryStatus loginClientAndRetrieveInfos(String username, String password) 
	    {
	    	removePremiumStatuses();
	    	//Retrieve all the personal information based on the primary key username
	    	String selectionQuery = "SELECT lastname, firstname, email, credit, userPassword, vip FROM ADMINDATABASE.CLIENTS WHERE username = ?";
	        
	        try 
	        {
	        	Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(selectionQuery);

	            //Replace the placeholders "?" with the values to insert
	            statement.setString(1, username);    

	            //All tuples matching this username (at most 1 because username is a primary key in the table Clients)
	            ResultSet result = statement.executeQuery();
	            
	            if (result.next()) 
	            {
	                String firstName = result.getString("firstname");
	                String lastName = result.getString("lastname");
	                String email = result.getString("email");
	                double credit = result.getDouble("credit");
	                String hashedPassword = result.getString("userPassword");
	                boolean vip = result.getBoolean("vip");
	                
	                //Check if the password entered matches the hashed password in the database
	                if (!checkPassword(password, hashedPassword))
	             	   return QueryStatus.LOGIN_ERROR;
	                
	                //Retrieve the customer's basket if it has been saved in the database
		            selectionQuery = "SELECT productID, quantity FROM ADMINDATABASE.BASKET WHERE clientUsername = ?";
		            PreparedStatement preparedStatement = conn.prepareStatement(selectionQuery);
		            preparedStatement.setString(1, username); 
		            Map<Integer, Integer> basket = new HashMap<>();
		            ResultSet basketResult = preparedStatement.executeQuery();
		            while(basketResult.next())
		            {
		            	int productID = basketResult.getInt("productID");
		            	int quantity = basketResult.getInt("quantity");
		            	basket.put(productID, quantity);
		            }
		            //New customer profile to not have to start a query every time the user wants to check their personal information
		            CustomerProfile customerProfile = new CustomerProfile(lastName, firstName, email, username, password, credit,vip, new BasketManager(basket, productManager));
		               
		            
		            //Retrieve the customer's achievements
		            customerProfile.setAchievements(getCustomerAchievements(username));
		            
		            //If the customer has a reservation to complete, sets the remaining time for this reservation 
		            long reservationTimeLeft = checkForExistingReservation(customerProfile);
		            customerProfile.setReservationTimeLeft(reservationTimeLeft);
		            customerProfile.setHasOngoingReservation(reservationTimeLeft > 0);
		            customerProfile.setHasReservationToComplete(reservationTimeLeft > 0);
		            
		            customerManager.setCurrentCustomer(customerProfile);
		            
	                return QueryStatus.LOGIN_SUCCESSFUL;

	            } 
	            else 
	            {
	                return QueryStatus.LOGIN_ERROR;
	            }
	        }

	           
	        catch (SQLException e) 
	        {
	        	System.out.println(e.getMessage());
	           	return QueryStatus.UNKNOWN_ERROR;
	        }

	    }
	    
	    
	    public List<Achievement> getCustomerAchievements(String username)
	    {
	    	List<Achievement> achievements = new ArrayList<>();
	    	
	    	String selectQuery = "SELECT achievement, reward, picture, achievementDate FROM ADMINDATABASE.ACHIEVEMENTS WHERE clientUsername = ?";
	    	try
	    	{
	    		Connection conn = getH2Connection();
	    		PreparedStatement statement = conn.prepareStatement(selectQuery);
	    		
	    		statement.setString(1, username);
	    		
	    		ResultSet result = statement.executeQuery();
	    		
	    		while(result.next())
	    		{
	    			String description = result.getString("achievement");
	    			double reward = result.getDouble("reward");
	    			Blob blob = result.getBlob("picture");
	    			Timestamp achievementDate = result.getTimestamp("achievementDate");
		            byte[] imageBytes = blob.getBytes(1, (int) blob.length()); 
		            ImageIcon imageIcon = new ImageIcon(imageBytes); 
		            
		            Achievement achievement = new Achievement(description, reward, imageIcon, achievementDate);
		            achievements.add(achievement);
	    		}
	    	}
	    	catch (SQLException e)
	    	{
	    		e.printStackTrace();
	    	}
	    	
	    	Collections.sort(achievements, Comparator.comparing(Achievement::getAchievementDate));
	    	return achievements;
	    }
	    
	    
	    
	    /**
	     * Adds the products to the database
	     * @param name The name of the product.
	     * @param description The description of the product.
	     * @param brand The brand of the product.
	     * @param price The price of the product.
	     * @param stock The initial stock of the product.
	     * @param imagePath A path to a png file, with low resolution and weighing less than 50ko, with an image of the product.
	     * @param colors A string representing the colors of the products, separated by commas or blank spaces.
	     * @param discount A value between 0 and 1 indicating the proportion of the price that is reduced (example : 0,2 for 20% discount).
	     */
	    public void addToProductTable(String name, String description, String brand, double price, int stock, String imagePath, String colors, double discount)
	    {
	    	String insertionQuery = "INSERT INTO ADMINDATABASE.PRODUCTS(productName, productDescription, productBrand, price, inStock, image, available, colors, discount) VALUES (?,?,?,?,?,?,?,?,?)";
	        try
	        {
	        	Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(insertionQuery);
	            
	            //Replace the placeholders "?" with the values to insert
	            statement.setString(1, name);  
	            statement.setString(2, description);  
	            statement.setString(3, brand);  
	            statement.setDouble(4, price);  
	            statement.setInt(5, stock);  
	            
	            //Creates a binary stream from the file obtained from the original image path
	            /*
	            File imageFile = new File(imagePath);
	            FileInputStream fis = new FileInputStream(imageFile);
	            statement.setBinaryStream(6, fis, (int) imageFile.length());
	            */
	            
	            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(imagePath);
	            if (inputStream == null) 
	            {
	                throw new FileNotFoundException("Resource not found: " + imagePath);
	            }
	            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	            byte[] data = new byte[1024];
	            int bytesRead;
	            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
	                buffer.write(data, 0, bytesRead);
	            }
	            buffer.flush();

	            byte[] imageBytes = buffer.toByteArray();

	            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
	            statement.setBinaryStream(6, byteArrayInputStream, imageBytes.length);
	            
	            
	            
	            
	            
	            statement.setBoolean(7, true);
	            statement.setString(8, colors);
	            statement.setDouble(9, discount);
	            statement.executeUpdate();

	            System.out.println("Image inserted successfully!");
	        }

	        catch (SQLException| IOException e) 
	        {
	        	System.out.println(e.getMessage());
	        }
	    }
	    

	    /**
	     * Adds the products to the database.
	     * Is used for drag and dropped files that are not present in the repository and whose paths are unknown.
	     * @param name The name of the product.
	     * @param description The description of the product.
	     * @param brand The brand of the product.
	     * @param price The price of the product.
	     * @param stock The initial stock of the product.
	     * @param imageFile A png file, with low resolution and weighing less than 50ko, representing the product.
	     * @param colors A string representing the colors of the products, separated by commas or blank spaces.
	     * @param discount A value between 0 and 1 indicating the proportion of the price that is reduced (example : 0,2 for 20% discount).
	     * @return The primary key generated during the insertion that identifies the product.
	     */
	    public int addToProductTable(String name, String description, String brand, double price, int stock, File imageFile, String colors, double discount)
	    {
	    	String insertionQuery = "INSERT INTO ADMINDATABASE.PRODUCTS(productName, productDescription, productBrand, price, inStock, image, available, colors, discount) VALUES (?,?,?,?,?,?,?,?,?)";
	        try
	        {
	        	Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(insertionQuery,  Statement.RETURN_GENERATED_KEYS);
	            
	            //Replace the placeholders "?" with the values to insert
	            statement.setString(1, name);  
	            statement.setString(2, description);  
	            statement.setString(3, brand);  
	            statement.setDouble(4, price);  
	            statement.setInt(5, stock);  
	            
	            FileInputStream fis = new FileInputStream(imageFile);
	            
	            statement.setBinaryStream(6, fis, (int) imageFile.length());
	            statement.setBoolean(7, true);
	            statement.setString(8, colors);
	            statement.setDouble(9, discount);
	            statement.executeUpdate();
	            
	            //Returns the value of the auto-incremented primary key if the insert is successful to synchronize in-app data.
	            ResultSet generatedKeys = statement.getGeneratedKeys();
	            if (generatedKeys.next()) 
	            {
	                    int generatedProductID = generatedKeys.getInt(1); 
	                    return generatedProductID;
	            }
	            System.out.println("Image inserted successfully!");
	        }

	                  
	        catch (SQLException |FileNotFoundException e) 
	        {
	           	System.out.println(e.getMessage());
	        }
	   	   	return -1;
	    }
	    
	    /**
	     * Gets all the products in the database and their remaining stock.
	     * @return A map containing the identifiers of the products as entries and the products themselves as values.
	     */
	    public Map<Integer,Product> getProducts()
	    {
	    	Map<Integer,Product> resultingProducts = new HashMap<>();
	    	String selectionQuery = "SELECT id, productName, productDescription, productBrand, price, inStock, image, available, colors, discount FROM ADMINDATABASE.PRODUCTS";

	        try 
	        {
	        	Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(selectionQuery);
	            ResultSet result = statement.executeQuery();
	            
	            //For each product in the database
	            while (result.next()) 
	            {
	         	   int id = result.getInt("id");
	         	   String name = result.getString("productName");
	         	   String description = result.getString("productDescription");
	         	   String brand = result.getString("productBrand");
	         	   double price = result.getDouble("price");
	         	   int stock = result.getInt("inStock");
	         	   Blob blob = result.getBlob("image");
	         	   boolean available = result.getBoolean("available");
	         	   String colors = result.getString("colors");
	         	   double discount = result.getDouble("discount");
	               byte[] imageBytes = blob.getBytes(1, (int) blob.length()); 
	               ImageIcon imageIcon = new ImageIcon(imageBytes); 
	               
	               //A new product instance is created containing all the information about the product at this given instant - will be updated regularly when needed
	               Product product = new Product(id, name, description, brand, price, stock, imageIcon, available, colors, discount);
	               resultingProducts.put(id, product);
	            } 
	        }

	           
	        catch (SQLException e) 
	        {
	        	System.out.println(e.getMessage());
	        }
	        return resultingProducts;
	    }
	    
	    /**
	     * Selects the products that match the applied keywords in this product filter.
	     * Updates in the product manager the list of products that match these keywords and thus are eligible to matching more criteria.
	     * @param productFilter A filter that contains the keywords against which we want to match the products.
	     */
	    public void getProductsWithOrKeywords(ProductFilter productFilter)
	    {
	    	List<Integer> resultingProducts = new ArrayList<>();
	    	List<String> orKeywords = productFilter.getOrKeywords();
	    	int numberOfKeywords = orKeywords.size();
	    	
	    	//Some keywords have been applied.
	    	if (numberOfKeywords > 0)
	    	{
	    		StringBuilder selectionQuery = new StringBuilder("SELECT id FROM (SELECT id, (");
	       		
	    		//If they are inclusive, then a certain value is added for every match in the database for the name, brand or description of the product.
	    		if (productFilter.isOr())
	    		{
	    			//For every keyword, the product obtains more relevance if :
	    			//1) the keyword matches perfectly the name, brand or description of the product
	    			//2) the keyword is present in one of these columns (less relevance).
	        		for (int i = 0; i < numberOfKeywords - 1; i++)
	        		{
	        			//selectionQuery.append("IF(MATCH(productName, productDescription, productBrand) AGAINST (?), ?, 0) + IF(productName ILIKE ?, ?, 0) + IF(productBrand ILIKE ?, ?, 0) + IF(productDescription ILIKE ?, ?, 0) + ");
	        			selectionQuery.append("CASE WHEN productName ILIKE ? THEN ? ELSE 0 END + CASE WHEN productBrand ILIKE ? THEN ? ELSE 0 END + CASE WHEN productDescription ILIKE ? THEN ? ELSE 0 END + ");
	        		}
	        		//selectionQuery.append("IF(MATCH(productName, productDescription, productBrand) AGAINST (?), ?, 0) + IF(productName ILIKE ?, ?, 0) + IF(productBrand ILIKE ?, ?, 0) + IF(productDescription ILIKE ?, ?, 0)) AS relevance FROM ADMINDATABASE.PRODUCTS) AS RelevanceTable WHERE relevance >= ?");
	        		selectionQuery.append("CASE WHEN productName ILIKE ? THEN ? ELSE 0 END + CASE WHEN productBrand ILIKE ? THEN ? ELSE 0 END + CASE WHEN productDescription ILIKE ? THEN ? ELSE 0 END) AS relevance FROM ADMINDATABASE.PRODUCTS) AS RelevanceTable WHERE relevance >= ?");
	    		}
	    		//The product must match all the keywords
	    		else
	    		{
	    			//For each keyword, if it appears in one of the columns, it gets a strictly positive value.
	    			//Otherwise, it gets a zero value.
	    			//The product of all of these values guarantees that all keywords are matched.
	        		for (int i = 0; i < numberOfKeywords - 1; i++)
	        		{
	        			//selectionQuery.append("(IF(MATCH(productName, productDescription, productBrand) AGAINST (?), ?, 0) + IF(productName ILIKE ?, ?, 0) + IF(productBrand ILIKE ?, ?, 0) + IF(productDescription ILIKE ?, ?, 0)) * ");
	        			selectionQuery.append("(CASE WHEN productName ILIKE ? THEN ? ELSE 0 END + CASE WHEN productBrand ILIKE ? THEN ? ELSE 0 END + CASE WHEN productDescription ILIKE ? THEN ? ELSE 0 END) * ");
	        		}
	        		//selectionQuery.append("(IF(MATCH(productName, productDescription, productBrand) AGAINST (?), ?, 0) + IF(productName ILIKE ?, ?, 0) + IF(productBrand ILIKE ?, ?, 0) + IF(productDescription ILIKE ?, ?, 0))) AS relevance FROM ADMINDATABASE.PRODUCTS) AS RelevanceTable WHERE relevance > ?");
	        		selectionQuery.append("(CASE WHEN productName ILIKE ? THEN ? ELSE 0 END + CASE WHEN productBrand ILIKE ? THEN ? ELSE 0 END + CASE WHEN productDescription ILIKE ? THEN ? ELSE 0 END)) AS relevance FROM ADMINDATABASE.PRODUCTS) AS RelevanceTable WHERE relevance > ?");
	    		}

	        	try
	        	{
	        		Connection conn = getH2Connection();
	        		PreparedStatement statement = conn.prepareStatement(selectionQuery.toString());
	        		int count = 1;
	        		
	        		//If the filter is exclusive, any value greater than 0 guarantees a match for all keywords
	        		//Otherwise, any value that is more than 1 guarantees it.
	        		//In this case, any match, even partial, gives 1 or more, so this is not really needed, but can be adjusted if needed that way.
	        		double relevanceThreshold = productFilter.isOr() ? 1f : 0;
	    			 
	        		for (int i = 0; i < numberOfKeywords; i++)
	        		{
		           		double value = 2f;
		       				
		           		//Sets a different relevance value if the match is partial of full (though not relevant here because every positive value guarantees selection).
	            		//statement.setString(count++ , orKeywords.get(i));
	            		//statement.setDouble(count++, value);
	            		statement.setString(count++, '%'+orKeywords.get(i)+'%');
	            		statement.setDouble(count++, value/2);
	            		statement.setString(count++, '%'+orKeywords.get(i)+'%');
	            		statement.setDouble(count++, value/2);
	            		statement.setString(count++, '%'+orKeywords.get(i)+'%');
	            		statement.setDouble(count++, value/2);
	        		}
	        		
	        		//Sets the minimum relevance needed to be selected.
	        		statement.setDouble(count++, relevanceThreshold);
	        		
	                ResultSet result = statement.executeQuery();
	                while (result.next()) 
	                {
	             	   int id = result.getInt("id");
	                   resultingProducts.add(id);
	                    
	                } 
	                
	                //Sets the products matching these keywords to be the products returned by the select query.
	                productManager.setProductsMatchingKeywords(resultingProducts);
	        	
	        	}
	        	catch (SQLException e)
	        	{
	        		System.out.println(e.getMessage());
	        	}
	    	
	    	}
	    	else
	    	{	
	    		//If no keywords, all products are eligible to be selected with more narrow filters
	    		productManager.setAllProductsMatchingKeywords();
	    	}
	    	

	    	
	    }
	    
	    
	    /**
	     * Selects the products in the database that match some criteria.
	     * @param productFilter A filter that contains the criteria the product needs to meet.
	     * @return The list of products the match these criteria.
	     */
	    public List<Product> getProductsWithFilters(ProductFilter productFilter)
	    {
	    	List<Product> resultingProducts = new ArrayList<>();
	    	
	    	//Splits the line of keywords into singular keywords that are going to be matched individually against the database
	    	String[] currentKeywords = productFilter.getKeywords().split("[,; |]+");
	    	
	    	//All the colors that have been selected by either the admin or the customer to filter out products
	    	Map<Integer, String> colors = productFilter.getColors();
	    	int numberOfKeywords = currentKeywords.length;
	    	String selectionQuery;
	    	
	    	//Small subtlety which is that the function split called on the empty string still returns an array of size 1 containing it.
	    	boolean withKeywords = !currentKeywords[0].equals("");
	    	
	    	//If the search bar is empty, which means the user is not typing a new keyword
	    	if (!withKeywords)
	    	{
	    		//Selects products whose discounted price is less than the selected maximum price, that respect stock, availability and discount criteria.
	    		selectionQuery = "SELECT id FROM ADMINDATABASE.PRODUCTS WHERE price * (1 - discount) <= ? AND inStock > ? AND available = ? AND discount >= ?";
				
	    		//If some colors have been selected
	    		if (colors.size() > 0)
				{
	    			//The product must match at least one of them (note the OR keyword in the SQL query).
					selectionQuery += " AND (";
					for (int i = 0; i < colors.size() - 1; i++)
					{
						//selectionQuery += "MATCH(colors) AGAINST (?) OR ";
						selectionQuery += "colors LIKE ? OR ";
					}
					//selectionQuery += "MATCH(colors) AGAINST (?))";
					selectionQuery += "colors LIKE ?)";
					
				}
	    		
	    		//If the products need to be ordered by price
	    		if (productFilter.isOrderByPrice())
					selectionQuery += " ORDER BY price * (1 - discount) ASC, productName ASC";
				else
					selectionQuery += " ORDER BY productName ASC";
	    	}

	    	//There is a keyword or a group of keywords typed by the user in the search bar
	    	else
	    	{
	    		selectionQuery = "SELECT id FROM (SELECT id, price, inStock, available, colors, discount, (";
	    		
	    		//For every keyword, a better score is given to the product if the keyword appears either in the name, description or brand of the product.
	    		for (int i = 0; i < numberOfKeywords - 1; i++)
	    		{
	    			//selectionQuery += "IF(MATCH(productName, productDescription, productBrand) AGAINST (?), ?, 0) + IF(productName ILIKE ?, ?, 0) + IF(productBrand ILIKE ?, ?, 0) + IF(productDescription ILIKE ?, ?, 0) + ";
	    			selectionQuery += "CASE WHEN productName ILIKE ? THEN ? ELSE 0 END + CASE WHEN productBrand ILIKE ? THEN ? ELSE 0 END + CASE WHEN productDescription ILIKE ? THEN ? ELSE 0 END + ";
	    		}
	    		//selectionQuery += "IF(MATCH(productName, productDescription, productBrand) AGAINST (?), ?, 0) + IF(productName ILIKE ?, ?, 0) + IF(productBrand ILIKE ?, ?, 0) + IF(productDescription ILIKE ?, ?, 0)) AS relevance FROM ADMINDATABASE.PRODUCTS) AS RelevanceTable WHERE price * (1 - discount) <= ? AND inStock > ? AND available = ? AND relevance >= ? AND discount >= ?";
	    		selectionQuery += "CASE WHEN productName ILIKE ? THEN ? ELSE 0 END + CASE WHEN productBrand ILIKE ? THEN ? ELSE 0 END + CASE WHEN productDescription ILIKE ? THEN ? ELSE 0 END) AS relevance FROM ADMINDATABASE.PRODUCTS) AS RelevanceTable WHERE price * (1 - discount) <= ? AND inStock > ? AND available = ? AND relevance >= ? AND discount >= ?";
	    		
	    		//Must match at least one color.
	    		if (colors.size() > 0)
	    		{
	    			selectionQuery += " AND (";
	    			for (int i = 0; i < colors.size() - 1; i++)
	    			{
	    				//selectionQuery += "MATCH(colors) AGAINST (?) OR ";
	    				selectionQuery += "colors LIKE ? OR ";
	    			}
	    			//selectionQuery += "MATCH(colors) AGAINST (?))";
	    			selectionQuery += "colors LIKE ?)";
	    					
	    		}	
	    		//Is either ordered by price or not.
				if (productFilter.isOrderByPrice())
					selectionQuery += " ORDER BY price * (1 - discount) ASC, relevance DESC";
				else
					selectionQuery += " ORDER BY relevance DESC";
	    	}
	        try 
	        {
	        	Connection conn = getH2Connection();
	        	PreparedStatement statement = conn.prepareStatement(selectionQuery);
		       	
	        	if (withKeywords)
		       	{
		       		int count = 1;
		       		//Minimum score with respect to the keywords that needs to be respected for the product to be selected
		       		double relevanceThreshold = 1;
		           	for (int i = 0; i < numberOfKeywords; i++)
		       		{
		           		//All the keywords are given the same weights except the very small ones (less than two letters)
		           		//Exception if the keyword is the last of the list : in this case, it is probably still being typed or is probably relevant an not an article.
		           		//So we give it a good score even if it is short.
		           		double value = (i == numberOfKeywords - 1) || currentKeywords[i].length() > 2 ? 2f : 0.5f;
		           		//statement.setString(count++ , currentKeywords[i]);
		           		//statement.setDouble(count++, value);
		           		statement.setString(count++, '%'+currentKeywords[i]+'%');
		           		statement.setDouble(count++, value/2);
		           		statement.setString(count++, '%'+currentKeywords[i]+'%');
		           		statement.setDouble(count++, value/2);
		           		statement.setString(count++, '%'+currentKeywords[i]+'%');
		           		statement.setDouble(count++, value/2);
		       		}
		       		
		       		statement.setDouble(count++, productFilter.getBudget());
		       		statement.setInt(count++, productFilter.getStock());
		       		statement.setBoolean(count++, productFilter.getAvailable());
		       		statement.setDouble(count++, relevanceThreshold);
		       		statement.setDouble(count++, productFilter.getDiscount()/100);
		    		
		       		//For each selected color in the filter
		       		for (String color : colors.values())
		    		{
		    			//statement.setString(count++, color);
		       			statement.setString(count++, '%'+color+'%');
		    		}	
		    	

	          	   	ResultSet result = statement.executeQuery();
	          	   	while (result.next()) 
	          	   	{
	             	   int id = result.getInt("id");
	             	   resultingProducts.add(productManager.getProductById(id));
	          	   	}
		       	}
	        	else
	        	{
	        		int count = 1;
	        		statement.setDouble(count++, productFilter.getBudget());
	        		statement.setInt(count++, productFilter.getStock());
	        		statement.setBoolean(count++, productFilter.getAvailable());
	        		statement.setDouble(count++, productFilter.getDiscount()/100);
		    		for (String color : colors.values())
		    		{
		    			statement.setString(count++, '%'+color+'%');
		    		}	
	          	   	List<Integer> productsMatchingKeywords = productManager.getProductsMatchingKeywords();
	          	   	ResultSet result = statement.executeQuery();
	          	   	while (result.next()) 
	          	   	{
	            	   int id = result.getInt("id");
	            	   if (productsMatchingKeywords.contains(id))
	            		   resultingProducts.add(productManager.getProductById(id));
	          	   	}
		       	

	           } 
	        }
	        
	        
	        catch (SQLException e) 
	        {
	     	   	System.out.println(e.getMessage());
	        }

	        return resultingProducts;
	    }
	    
	    //Will be deleted in the final version : the products will not be stored in the resources of the project but rather in the database
	    
	    /**
	     * Adds some products to the database.
	     */
	    public void addProductsToDatabase()
	    {
	    	addToProductTable("PSG football", "Official and certified Paris-Saint-Germain football (2023 season)", "PSG", 2999, 25, "balle_psg.png", "blue, red", 0);
	    	addToProductTable("OM Jersey", "Olympique de Marseille official home jersey", "Puma", 4900, 20, "maillot.png", "white, blue", 0);
	    	addToProductTable("Bib", "Multi-purpose red bib, adult size", "Décathlon", 399, 100, "chasuble.png", "red", 0);
	    	addToProductTable("Hyperact Starter Gloves", "Football goalkeeping gloves", "Uhlsport", 5499, 30, "gants_foot.png", "blue, black", 0);
	    	addToProductTable("Gilbert Omega rugby ball", "Gilbert rugby ball, Omega model", "Gilbert", 3800, 60, "balle_rugby.png", "white, blue, black", 0);
	    	addToProductTable("100XBase Swimming Goggles", "Blue-pinkish swimming goggles, 100XBase. For children.", "Nabaji", 1449, 80, "lunettes.png", "magenta, cyan", 0);
	    	addToProductTable("Olympique de Marseille football", "Official and certified OM football (2022 season)", "Olympique de Marseille", 1779, 34, "balle_om.png", "black, blue", 0);
	    	addToProductTable("Training cones", "Football training cones. Several colours are available : blue, red, yellow and white.", "Kappa", 850, 60, "plots.png", "white, blue, yellow, red", 0);
	    	addToProductTable("Black shorts", "Adidas sportswear : Black Shorts. Essentials for Men.", "Adidas", 1590, 55, "short.png", "black", 0);
	    	addToProductTable("Hockey Stick", "This type of stick is mainly used for discovering the sport and learning how to handle the puck.", "Fischer", 4500, 45, "crosse.png", "black, red", 0);
	    	addToProductTable("Fencing Kit", "Fencing starter pack. Contains: fencing protective kit, mask, battle ready sword", "Start Fencing Today", 7999, 10, "Fencing_kit.png", "black, white", 0);
	    	addToProductTable("Sports Vinyl Dumbbells", "Vinyl dumbbells, ranging from .5 to 6 kilogrammes. Fitness weights", "FXR Sports", 2999, 150, "dumbbells.png", "blue, red, black, white, green, yellow", 0);
	    	addToProductTable("Endurance Treadmill", "Maximise your resources with a treadmill that’s simple for new users and powerful for enthusiasts.", "Matrix Fitness", 18000, 10, "treadmill.png", "black, white, yellow", 0);
	    	addToProductTable("Tennis racket", "Perfect for players who want to spend time with friends and family.", "Wilson", 4500, 29, "tennis_racket.png", "black, green, white", 0);
	    	addToProductTable("First EVO rowing shell", "The FIRST EVO is a French-made boat, ideal for beginners or young passengers.", "L'Atelier Composite", 20000, 3, "aviron.png", "black, green", 0);
	    	addToProductTable("4-Tennis balls can", "Official Roland-Grappos balls can. Approved for competition by the USTA and ITF.", "Wilson", 999, 31, "tennis_balls.png", "black, yellow", 0);
	    	addToProductTable("Girls Figure Ice Skates", "These Ice Skates provide strength and comfort for recreational skaters !", "Jackson Classics", 11500, 115, "ice_skate.png", "white", 0);
	    	addToProductTable("Badminton Shuttlecocks", "These shuttlecocks offer a reliable option for outdoor play. Made from cork.", "KES", 879, 93, "bad_shut.png", "black, white", 0);
	    }
	    
	    /**
	     * Checks if all the products in the customer's basket are available in the desired quantities.
	     * @return An array whose first element indicates the number of products that are not available anymore and whose second element indicates the number of products that have been reserved by other customers.
	     */
	    public int[] checkIfBasketAvailable()
	    {
	    	//Delete the old reservations by the same customer
	    	String deleteQuery = "DELETE FROM ADMINDATABASE.RESERVATION WHERE reservationTime < DATEADD('MINUTE', -?, CURRENT_TIMESTAMP) OR clientUsername = ?";
	        try (Connection conn = getH2Connection();
	             PreparedStatement statement = conn.prepareStatement(deleteQuery)) 
	           	{
	        		//Replace the placeholders "?" with the values to insert
	        		statement.setLong(1, reservationLength/60);
	        		statement.setString(2, customerManager.getCurrentCustomer().getUsername());
	        		statement.executeUpdate();
	            } 
	 
	            catch (SQLException e) {
	           	   	System.out.println(e.getMessage());
	            }
	    	
	    	//First number represents the number of products that have been set to unavailable
	        //Second number represents the number of products that have been reserved by other customers in the meantime.
	        int[] successFlags = new int[2];
	    	StringBuilder selectionQuery = new StringBuilder("SELECT p.id as productID, p.available as available, COALESCE(SUM(r.quantity),0) as totalQuantity, p.inStock as inStock FROM ADMINDATABASE.PRODUCTS p LEFT JOIN ADMINDATABASE.RESERVATION r ON p.id = r.productID WHERE p.id IN (");
	    	
	    	Map<Integer, Integer> basket = customerManager.getCurrentCustomer().getBasketManager().getBasket();
	    	Set<Integer> productIDs = basket.keySet();
	    	int basketSize = customerManager.getCurrentCustomer().getBasketManager().getBasketSize();
	    	for (int i = 0; i < basketSize - 1; i++)
	    	{
	 	       //For every product in the customer's basket, we add it to the products whose stock and availability we want to check
	    		selectionQuery.append("?, ");
	    	}
	    	selectionQuery.append("?) GROUP BY p.id");
	    	

	        try
	        {
	        	Connection conn = getH2Connection();
	       		PreparedStatement statement = conn.prepareStatement(selectionQuery.toString());
	    		int count = 1;
	    	    
	    		for (int productID : productIDs)
	    	    {
	    		   statement.setInt(count, productID);
	    		   count++;
	    	    }
	            
	    	    ResultSet result = statement.executeQuery();
	            
	    	    while (result.next())
	            {
	            	int productID = result.getInt("productID");
	            	int quantity = result.getInt("totalQuantity");
	            	int stock = result.getInt("inStock");
	            	boolean available = result.getBoolean("available");
	            	if (basket.containsKey(productID))
	            	{
	            		//Update the stock of the products
	                	int tryToBuy = basket.get(productID);
	                	//If the product has been rendered unavailable, remove it from the basket 
	                	if (!available)
	                	{
	                		productManager.getProductById(productID).setAvailable(available);
	                		basket.remove(productID);
	                		successFlags[0] += tryToBuy;
	                	}
	                	else if (quantity == stock)
	                	{
	                		basket.remove(productID);
	                		successFlags[1] += tryToBuy;
	                	}
	                	else if (quantity + tryToBuy > stock)
	                	{
	                		basket.put(productID, stock - quantity);
	                		successFlags[1] += tryToBuy;
	                	}
	            	}
	            }
	        }

	           
	           catch (SQLException e) {
	        	   	System.out.println(e.getMessage());
	           }
	        return successFlags;
	    }
	    

	    /**
	     * Synchronizes in-app information with the information in the database.
	     * Should be called regularly in a separate thread.
	     */
	    public void updateProducts()
	    {
	    	//Frees reserved products when some reservations expire.
	    	deleteOldReservations();

	    	String selectionQuery = "SELECT id, inStock, available, colors FROM ADMINDATABASE.PRODUCTS";

	        try
	        {
	        	Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(selectionQuery);
	            
	            ResultSet result = statement.executeQuery();
	            
	            while (result.next()) 
	            {
	         	   int id = result.getInt("id");
	         	   int stock = result.getInt("inStock");
	         	   boolean available = result.getBoolean("available");
	         	   productManager.getProductById(id).setAvailable(available);
	               productManager.getProductById(id).setStock(stock);
	               productManager.getProductById(id).setColors(result.getString("colors"));
	            } 
	        }

	           
	           
	        catch (SQLException e) 
	        {
	        	System.out.println(e.getMessage());
	        }

	    }
	    

	    /**
	     * Deletes reservations by customers that are older than the maximum reservation length.
	     */
	    public void deleteOldReservations()
	    {
	    	//Deletes from the reservation table the ones that have expired to free the products
	    	String deleteQuery = "DELETE FROM ADMINDATABASE.RESERVATION WHERE reservationTime < DATEADD('MINUTE', -10, CURRENT_TIMESTAMP)";
	        
	    	try
	    	{
	    		Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(deleteQuery);
	        	statement.executeUpdate();
	    	}

	 
	        catch (SQLException e) 
	    	{
	           	System.out.println(e.getMessage());
	        }
	    	
	    }
	    //Is called when the customer validates their basket : inserts the products into the table Reservation

	    /**
	     * Starts a reservation for the current customer that will last for as many seconds as the constant field reservationLength indicates.
	     * Other customers will not be able to buy these reserved products and admins won't be able to modify their properties.
	     */
	    public void beginReservation()
	    {
	    	//Sets the time remaining for the customer's reservation
	    	CustomerProfile customerProfile = customerManager.getCurrentCustomer();
	    	customerProfile.setReservationTimeLeft(reservationLength);
	    	customerProfile.setHasOngoingReservation(true);
	    	
	    	StringBuilder insertionQuery = new StringBuilder("INSERT INTO ADMINDATABASE.RESERVATION(productID, quantity, clientUsername, reservationTime) VALUES ");
	    	
	    	/*
	    	 * For every product in the customer's basket, a tuple is inserted in the reservation table containing
	    	 * 1) The product identifier.
	    	 * 2) The quantity of this product.
	    	 * 3) The username of the customer buying it.
	    	 * 4) The timestamp at which the reservation has begun.
	    	 */
	    	for (int i = 0; i < customerProfile.getBasketManager().getBasketSize()-1; i++ )
	    		insertionQuery.append("(?,?,?,?),");
	    	insertionQuery.append("(?,?,?,?)");
	    	Timestamp currentTime = new Timestamp(System.currentTimeMillis());
	    	
	        try
	        {
	        	Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(insertionQuery.toString());
	        	int count = 1;
	        	
	        	//Replaces the placeholders with the values to insert.
	        	for (Entry<Integer, Integer> entry : customerProfile.getBasketManager().getBasket().entrySet())
	        	{
	        		statement.setInt(count++, entry.getKey());  
	                statement.setInt(count++, entry.getValue());   
	                statement.setString(count++, customerProfile.getUsername()); 
	                statement.setTimestamp(count++, currentTime); 
	        	}
	            
	            statement.executeUpdate();

	            saveBasketInDatabase();
	        }

	        catch (SQLException e) {
	        	String errorMessage = e.getMessage();
	        	System.out.println(errorMessage);
	        }
	    }
	    
	    /**
	     * Deletes the current reservation from the customer.
	     */
	    public void cancelReservation()
	    {
	    	CustomerProfile customerProfile = customerManager.getCurrentCustomer();
	    	customerProfile.setHasOngoingReservation(false);
	    	customerProfile.setHasReservationToComplete(false);
	    	customerProfile.setReservationTimeLeft(0);
	    	
	    	String deleteQuery = "DELETE FROM ADMINDATABASE.RESERVATION WHERE clientUsername = ?";
	        
	    	try
	    	{
	    		Connection conn = getH2Connection();
	         	PreparedStatement statement = conn.prepareStatement(deleteQuery);
	    		
	         	//Replaces the placeholder with the value to insert
	         	statement.setString(1, customerProfile.getUsername());
	    		statement.executeUpdate();
	    	}

	        catch (SQLException e) 
	    	{
	           	System.out.println(e.getMessage());
	        }
	    }
	    
	    /**
	     * Looks for a potential incomplete reservation by the customer.
	     * @return The remaining reservation time expressed in seconds.
	     */
	    public long checkForExistingReservation(CustomerProfile customerProfile)
	    {
	    	String selectionQuery = "SELECT reservationTime FROM ADMINDATABASE.RESERVATION WHERE clientUsername = ? AND reservationTime > DATEADD('MINUTE', -10, CURRENT_TIMESTAMP)";
	        try
	        {
	        	Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(selectionQuery);
	     	    statement.setString(1, customerProfile.getUsername());
	            ResultSet result = statement.executeQuery();
	            
	            //If an existing reservation has been found
	            if (result.next()) 
	            {
	         	   Timestamp beginTime = result.getTimestamp("reservationTime");
	         	   Instant beginInstant = beginTime.toInstant();
	         	   Instant now = Instant.now();
	         	   
	         	   //Time that has gone by since the beginning of the reservation in seconds
	         	   Duration duration = Duration.between(beginInstant, now);
	         	   long secondsDifference = duration.getSeconds();
	         	   return reservationLength - secondsDifference;
	            }

	        }

	        catch (SQLException e) 
	        {
	        	System.out.println(e.getMessage());
	        }
	        return 0;
	     
	    }
	    
	    /**
	     * Puts in the table Basket the products that are currently in the customer's basket (customerProfile > basketManager > basket).
	     */
	    public void saveBasketInDatabase()
	    {
	    	CustomerProfile customerProfile = customerManager.getCurrentCustomer();
	    	
	    	//Deletes the previous basket from the database
	    	String deleteQuery = "DELETE FROM ADMINDATABASE.BASKET WHERE clientUsername = ?";
	        try
	        {
	        	Connection conn = getH2Connection();
	         	PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery); 
	    		deleteStatement.setString(1, customerProfile.getUsername());
	    		deleteStatement.executeUpdate();
	        }

	        catch (SQLException e) 
	        {
	           	System.out.println(e.getMessage());
	        }
	    	
	        //For every product in the customer's basket, add it to the table basket with its quantity
	    	if (customerProfile.getBasketManager().getBasketSize()>0)
	    	{
	        	StringBuilder insertionQuery = new StringBuilder("INSERT INTO ADMINDATABASE.BASKET(productID, quantity, clientUsername) VALUES ");
	        	
	        	for (int i = 0; i < customerProfile.getBasketManager().getBasketSize()-1; i++ )
	        		insertionQuery.append("(?,?,?),");
	        	insertionQuery.append("(?,?,?)");
	        	
	            try
	            {
	            	Connection conn = getH2Connection();
	                PreparedStatement statement = conn.prepareStatement(insertionQuery.toString());
	            	int count = 1;
	            	
	            	//Replace the placeholders with the values to insert.
	            	for (Entry<Integer, Integer> entry : customerProfile.getBasketManager().getBasket().entrySet())
	            	{
	            		statement.setInt(count++, entry.getKey());  
	                    statement.setInt(count++, entry.getValue());   
	                    statement.setString(count++, customerProfile.getUsername()); 
	            	}
	                
	                statement.executeUpdate();
	            }

	            catch (SQLException e) 
	            {
	            	String errorMessage = e.getMessage();
	            	System.out.println(errorMessage);
	            }
	    	}
	    }

	    /**
	     * Checks if a credit code is valid and returns its value.
	     * @param creditCode The credit code the user wants to use.
	     * @return 0 if the credit code is invalid, else the value of the credit code.
	     */
	    public double useCreditCode(String creditCode)
	    {
	    	String selectionQuery = "SELECT codeValue FROM ADMINDATABASE.CREDITCODE WHERE creditCode = ?";

	        try
	        {
	        	Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(selectionQuery);
	     	    statement.setString(1, creditCode);
	            ResultSet result = statement.executeQuery();
	            if (result.next()) 
	            {
	         	   double value = result.getDouble("codeValue");
	         	   
	         	   //Deletes the credit code to prevent it from being used again.
	         	   String deleteQuery = "DELETE FROM ADMINDATABASE.CREDITCODE WHERE creditCode = ?";
	         	   PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery);
	         	   deleteStatement.setString(1, creditCode);
	         	   deleteStatement.executeUpdate();
	         	   return value;
	            }
	        }

	           
	        catch (SQLException e) 
	        {
	        	System.out.println(e.getMessage());
	        }
	        return 0;
	    }
	    
	    /**
	     * Updates the credit attribute in the Clients table with the value passed as a parameter.
	     * @param value The new credit of the customer.
	     */
	    public void updateCustomerCredit(double value)
	    {
	    	customerManager.getCurrentCustomer().addCredit(value);
	    	String updateQuery = "UPDATE ADMINDATABASE.CLIENTS SET credit = credit + ? WHERE username = ?";
	    	
	    	try
	    	{
	    		Connection conn = getH2Connection();
	    		PreparedStatement statement = conn.prepareStatement(updateQuery);
	    		
	    		//Replaces the placeholders with the values to insert.
	    		statement.setDouble(1, value);
	    		statement.setString(2, customerManager.getCurrentCustomer().getUsername());
	    		statement.executeUpdate();
	    	}
	    	catch (SQLException e)
	    	{
	    		System.out.println(e.getMessage());
	    	}
	    }

	    /**
	     * Modifies personal information about a customer in the database.
	     * @param lastName The customer's last name.
	     * @param firstName The customer's first name.
	     * @param email The customer's email.
	     * @param username The customer's username.
	     * @param password The customer's password.
	     * @return Information indicating whether the update was successful or details about the potential errors.
	     */
	    public QueryStatus updateCustomerProfile(String lastName, String firstName, String email, String username, String password)
	    {
	    	CustomerProfile customerProfile = customerManager.getCurrentCustomer();
	    	
	    	//Validates the email using a very elaborated regex.
	    	EmailValidator emailValidator = EmailValidator.getInstance();
	    	if (lastName.equals(""))
	    		return QueryStatus.EMPTY_NAME;
	    	if (firstName.equals(""))
	    		return QueryStatus.EMPTY_FIRST_NAME;
	    	if (email.equals(""))
	    		return QueryStatus.EMPTY_EMAIL;
	    	if (username.equals(""))
	    		return QueryStatus.EMPTY_USERNAME;
	    	if (password.equals(""))
	    		return QueryStatus.EMPTY_PASSWORD;
	    	if (!emailValidator.isValid(email))
	    		return QueryStatus.WRONG_EMAIL_PATTERN;

	    	
	    	//If a credit code has been used, checks if a credit code exists and get its value.

	    	String updateQuery = "UPDATE ADMINDATABASE.CLIENTS SET lastName = ?, firstName = ?, email = ?, username = ?, userPassword = ? WHERE username = ?";

	        try 
	        {
	        	Connection conn = getH2Connection();
	        	PreparedStatement statement = conn.prepareStatement(updateQuery);
	        	
	        	//Replaces placeholders with the values to insert.
	        	statement.setString(1, lastName);
	        	statement.setString(2, firstName);
	        	statement.setString(3, email);
	        	statement.setString(4, username);
	        	statement.setString(5, hashPassword(password));
	     	   	statement.setString(6, customerProfile.getUsername());
	            statement.executeUpdate();
	            
	            //Refreshes the customer profile with the new information in the application.
	            customerProfile.setName(lastName);
	            customerProfile.setFirstName(firstName);
	            customerProfile.setEmail(email);
	            customerProfile.setPassword(password);
	            customerProfile.setUsername(username);
	            
	            return QueryStatus.INSERT_SUCCESSFUL;
	        }
	             
	        catch (SQLException e) 
	        {
	        	String errorMessage = e.getMessage();
	        	System.out.println(errorMessage);
	            if (errorMessage.contains("email"))
	            	return QueryStatus.EMAIL_ERROR;
	            if (errorMessage.contains("username"))
	        		return QueryStatus.USERNAME_ERROR;
	        	return QueryStatus.UNKNOWN_ERROR;
	        }
	    }

	    /**
	     * Inserts a new order made by a customer in the table ADMINDATABASE.CUSTOMERORDERS and its content in the table OrderContent.
	     * @param address The address where the customer wants their order delivered.
	     * @param estimatedTime The theoretical time at which the order is supposed to arrive.
	     */
	    public void insertOrder(String address, double estimatedTime)
	    {
	    	CustomerProfile customerProfile = customerManager.getCurrentCustomer();
	    	
	        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

	        long secondsToAdd = (long) (estimatedTime * 3600);
	        
	        Instant estimatedDeliveryInstant = currentTimestamp.toInstant().plusSeconds(secondsToAdd);
	        Timestamp estimatedDeliveryTime = Timestamp.from(estimatedDeliveryInstant);
	        
	        //Compute a random time to induce an error on the delivery time.
	        double range = secondsToAdd * 0.2;
	        Random random = new Random();
	        double offset = range * (2 * random.nextDouble() - 1);
	        Instant realDeliveryInstant = currentTimestamp.toInstant().plusSeconds(secondsToAdd + (long)offset);
	        Timestamp realDeliveryTime = Timestamp.from(realDeliveryInstant);
	        
	        Instant now = Instant.now();
	        Timestamp presentTime = Timestamp.from(now);
	        
	        
	    	String insertionQuery = "INSERT INTO ADMINDATABASE.CUSTOMERORDERS(clientUsername, address, price, orderTime, estimatedDeliveryTime, deliveryTime, orderStatus) VALUES (?,?,?,?,?,?,?)";

	        try
	        {
	        	Connection conn = getH2Connection();
	        	
	            PreparedStatement statement = conn.prepareStatement(insertionQuery.toString(), Statement.RETURN_GENERATED_KEYS);
	        	
	            //Replaces the placeholders with the values to insert.
	            statement.setString(1, customerProfile.getUsername());
	        	statement.setString(2, address);
	        	statement.setDouble(3, customerProfile.getBasketManager().getTotalPrice());
	        	statement.setTimestamp(4, presentTime);
	        	statement.setTimestamp(5, estimatedDeliveryTime);
	        	statement.setTimestamp(6, realDeliveryTime);
	        	statement.setString(7, "Delivery in progress");
	            statement.executeUpdate();
	            
	            //Get the generated primary key for the new order
	            ResultSet generatedKeys = statement.getGeneratedKeys();
	            if (generatedKeys.next()) 
	            {
	                    int generatedOrderID = generatedKeys.getInt(1); 
	                    
	                    //Add the new order to the customer's orders and to the orderManager's correspondence map
	                    Order order = new Order(generatedOrderID, address, customerProfile.getBasketManager().getTotalPrice(), presentTime, estimatedDeliveryTime, realDeliveryTime, "Delivery in progress");
	                    order.setOrderContent(customerProfile.getBasketManager().getBasket());
	                    
	                    //A map containing for each product its price at the time it was ordered.
	                    Map<Integer, Double> pricesWhenOrdered = new HashMap<>();
	                    
	                    //Puts in the previous map the current price of the product with its discount.
	                    for (int productID : customerProfile.getBasketManager().getBasket().keySet())
	                    {
	                    	Product product = productManager.getProductById(productID);
	                    	pricesWhenOrdered.put(productID, product.getPrice()*(1 - product.getDiscount()));
	                    }
	                    order.setPricesWhenOrdered(pricesWhenOrdered);
	                    insertOrderContent(generatedOrderID);
	                    updateCustomerCredit(-customerProfile.getBasketManager().getTotalPrice());
	                    updateProductStocksAfterOrder(generatedOrderID);
	                    emptyBasket();
	                    cancelReservation();
	            }

	        }

	        
	        
	        catch (SQLException e) {
	        	String errorMessage = e.getMessage();
	        	System.out.println(errorMessage);
	        }
	        
	    }    

	    /**
	     * Deletes the content of the customer's basket BOTH in the application and in the database.
	     */
	    public void emptyBasket()
	    {
	    	CustomerProfile customerProfile = customerManager.getCurrentCustomer();
	    	
	    	String deleteQuery = "DELETE FROM ADMINDATABASE.BASKET WHERE clientUsername = ?";
	        try
	        {
	        	Connection conn = getH2Connection();
	         	PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery);
	    		
	         	//Replaces the placeholder with the value to insert.
	         	deleteStatement.setString(1, customerProfile.getUsername());
	    		deleteStatement.executeUpdate();
	    		customerProfile.getBasketManager().emptyBasket();
	        }

	        catch (SQLException e) 
	        {
	           	System.out.println(e.getMessage());
	        }
	        
	    }
	    
	    /**
	     * Inserts the content of an order : the ordered products, their quantities and their price at this given instant with potential discounts.
	     * @param orderID The identifier of the order.
	     */
	    public void insertOrderContent(int orderID)
	    {
	    	CustomerProfile customerProfile = customerManager.getCurrentCustomer();
	    	
	    	StringBuilder insertionQuery = new StringBuilder("INSERT INTO ADMINDATABASE.ORDERCONTENT(orderID, productID, quantity, priceWhenOrdered) VALUES ");
	    	
	    	/*
	    	 * For each product in the customer's basket, insert in the table OrderContent a tuple containing :
	    	 * 1) The identifier of the order
	    	 * 2) The identifier of the product
	    	 * 3) The quantity for this product
	    	 * 4) The product's current price with its potential discounts
	    	 */
	    	for (int i = 0; i < customerProfile.getBasketManager().getBasketSize()-1; i++ )
	    		insertionQuery.append("(?,?,?,?),");
	    	insertionQuery.append("(?,?,?,?)");
	    	
	        try
	        {
	        	Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(insertionQuery.toString());
	        	int count = 1;
	        	
	        	//Replaces the placeholders with the values to insert.
	        	for (Entry<Integer, Integer> entry : customerProfile.getBasketManager().getBasket().entrySet())
	        	{
	        		Product product = productManager.getProductById(entry.getKey());
	        		statement.setInt(count++, orderID);  
	                statement.setInt(count++, entry.getKey());   
	                statement.setInt(count++, entry.getValue()); 
	                statement.setDouble(count++, product.getPrice()*(1 - product.getDiscount())); 
	        	}
	            statement.executeUpdate();
	        }

	        catch (SQLException e) {
	        	String errorMessage = e.getMessage();
	        	System.out.println(errorMessage);
	        }
	    }

	    
	    public List<Order> getCustomerOrders(String username)
	    {
	    	if (username.equals("Default"))
	    	{
	    		username = customerManager.getCurrentCustomer().getUsername();
	    	}
	    	
		    String updateQuery = "UPDATE ADMINDATABASE.CUSTOMERORDERS SET orderStatus = CASE WHEN CURRENT_TIMESTAMP >= deliveryTime THEN ? WHEN CURRENT_TIMESTAMP >= estimatedDeliveryTime AND CURRENT_TIMESTAMP < deliveryTime THEN ? ELSE ? END WHERE clientUsername = ?";
		    try
		    {
		    	Connection conn = getH2Connection();
	            PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
	            
	            //Replaces the placeholders with the values to update.
	            updateStatement.setString(1, "Delivery completed");
	            updateStatement.setString(2, "Late");
	            updateStatement.setString(3, "Delivery in progress");
	            updateStatement.setString(4, username);
	            updateStatement.executeUpdate();
	    	}
	
	        catch (SQLException e) 
	    	{
	            System.out.println(e.getMessage());
	    	}
	    	
	    	
	    	Map<Integer, Order> orders = new HashMap<Integer, Order>();
	    
	    	String selectionQuery = "SELECT o.orderID, o.address, o.price, o.orderTime, o.estimatedDeliveryTime, o.deliveryTime, o.orderStatus, c.productID, c.quantity, c.priceWhenOrdered FROM ADMINDATABASE.CUSTOMERORDERS o JOIN ADMINDATABASE.ORDERCONTENT c ON o.orderID = c.orderID WHERE clientUsername = ?";

	        try
	        {
	        	Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(selectionQuery);
	     	    
	            //Replaces the placeholder with the value to insert.
	            statement.setString(1, username);
	            ResultSet result = statement.executeQuery();
	            
	            //For every order content and its associated order that has been found in the database.
	            while (result.next()) 
	            {
	         	   int orderID = result.getInt("orderID");
	         	   String address = result.getString("address");
	         	   double price = result.getDouble("price");
	         	   Timestamp orderTime = result.getTimestamp("orderTime");
	         	   Timestamp estimatedDeliveryTime = result.getTimestamp("estimatedDeliveryTime");
	         	   Timestamp deliveryTime = result.getTimestamp("deliveryTime");
	         	   String status = result.getString("orderStatus");
	         	   double priceWhenOrdered = result.getDouble("priceWhenOrdered");
	         	   int productID = result.getInt("productID");
	         	   int quantity = result.getInt("quantity");
	         	   
	         	   Order order;
	         	   
	         	   //If the associated order has already been added to the order manager.
	         	   if (orders.containsKey(orderID))
	         	   {
	         		   order = orders.get(orderID);
	         	   }
	         	   //Otherwise, create a new instance of Order and add it to the order manager.
	         	   else
	         	   {
	         		   order = new Order(orderID, address, price, orderTime, estimatedDeliveryTime, deliveryTime, status);
	         		   orders.put(orderID, order);
	         	   }
	         	   //Add to this order instance the product and quantity.
	         	   order.addProduct(productID, quantity, priceWhenOrdered);
	         	   
	            }
	        }
	        catch (SQLException e) 
	        {
	        	System.out.println(e.getMessage());
	        }
	        
	    	List<Order> sortedOrders = new ArrayList<>();
	    	for (Order order : orders.values())
	    	{
	    		sortedOrders.add(order);
	    	}
	        //Sort the orders by order time to display them chronologically.
	        sortedOrders.sort((order1, order2) -> {
	            Timestamp time1 = order1.getOrderTime();
	            Timestamp time2 = order2.getOrderTime();
	            return time1.compareTo(time2);  
	        });
	    	
	    	return sortedOrders;
	    }
	    
		/**
		 * Updates the status of the customer's orders to inform them in real time if their orders are in delivery, have been delivered, or are late.
		 * @return The number of orders whose statuses have been updated.
		 */
	    public int updateCustomerOrders(String username)
	    {
	    	if (username.equals("Default"))
	    	{
	    		username = customerManager.getCurrentCustomer().getUsername();
	    	}
	    	int updatedOrders = 0;
	    	//First selection query to get the orders that need to be modified in the application.
	    	String selectQuery = "SELECT orderID, CURRENT_TIMESTAMP >= deliveryTime as delivered FROM ADMINDATABASE.CUSTOMERORDERS WHERE clientUsername = ? AND ((CURRENT_TIMESTAMP >= deliveryTime AND orderStatus != ?) OR (CURRENT_TIMESTAMP >= estimatedDeliveryTime AND CURRENT_TIMESTAMP < deliveryTime AND orderStatus != ?) OR (CURRENT_TIMESTAMP < estimatedDeliveryTime AND orderStatus != ?));";
	    	try
	    	{
	    		Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(selectQuery);
				
	            //Replaces the placeholders with the values to select.
	            statement.setString(1, username);
				statement.setString(2, "Delivery completed");
				statement.setString(3, "Late");
				statement.setString(4, "Delivery in progress");
	            ResultSet result = statement.executeQuery();
	            while (result.next())
	            {
	            	updatedOrders++;
	            }
	            
	            if (updatedOrders == 0)
	            {
	            	return 0;
	            }
	            
	            //Second query to actually update them in the database.
	            String updateQuery = "UPDATE ADMINDATABASE.CUSTOMERORDERS SET orderStatus = CASE WHEN CURRENT_TIMESTAMP >= deliveryTime THEN ? WHEN CURRENT_TIMESTAMP >= estimatedDeliveryTime AND CURRENT_TIMESTAMP < deliveryTime THEN ? ELSE ? END WHERE clientUsername = ?";
	            PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
	            
	            //Replaces the placeholders with the values to update.
	            updateStatement.setString(1, "Delivery completed");
	            updateStatement.setString(2, "Late");
	            updateStatement.setString(3, "Delivery in progress");
	            updateStatement.setString(4, username);
	            updateStatement.executeUpdate();
		    	return updatedOrders;
	    	}

	               
	        catch (SQLException e) 
	    	{
	            System.out.println(e.getMessage());
		    	return updatedOrders;
	        }
	    	

	    }
	    
	    /**
	     * Sets a new basket for the customer and saves it in the database.
	     * @param newBasket A map containing as entries the identifiers of the products and as values their corresponding quantities.
	     */
	    public void setBasket(Map<Integer, Integer> newBasket)
	    {
	    	customerManager.getCurrentCustomer().getBasketManager().setBasket(newBasket);
	    	saveBasketInDatabase();
	    }
	    
	    /**
	     * Attempts to delete a product if it has been reserved or bought. Otherwise, its status is just changed to unavailable to prevent it from being seen by customers.
	     * @param productID The identifier of the product to remove.
	     * @return True if the product has been deleted, false if it has only been set to unavailable.
	     */
	    public boolean deleteProduct(int productID)
	    {
	    	//Tries to delete the product
	    	String deleteQuery = "DELETE FROM ADMINDATABASE.PRODUCTS WHERE id = ?";
	        try 
	        {
	        	Connection conn = getH2Connection();
	        	PreparedStatement statement = conn.prepareStatement(deleteQuery);

	        	statement.setInt(1, productID);
	        	statement.executeUpdate();
	        	
	        	//If the delete query was successful, then product is removed from both the database and the application.
	        	productManager.removeProduct(productID);
	        	return true;
	        }
	        //Otherwise, the product appears somewhere in the basket, reservation or order of a customer and couldn't be deleted.
	        catch (SQLException e) 
	        {
	            propagateChangesWhenProductNotAvailable(productID);
	        }
	        return false;

	    }
	    
	    /**
	     * If a product couldn't be deleted, sets it to unavailable both in the database and in the application.
	     * @param productID The identifier of the product.
	     */
	    public void propagateChangesWhenProductNotAvailable(int productID)
	    {
	   	   	try 
	   	   	{
	   	   		Connection conn = getH2Connection();
	   	   		//First step : set the product to be unavailable in the database.
	       	   	String updateQuery = "UPDATE ADMINDATABASE.PRODUCTS SET available = ? WHERE id = ?";
	       	   	PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
	       	   	updateStatement.setBoolean(1, false);
	       	   	updateStatement.setInt(2, productID);
	       	   	updateStatement.executeUpdate();
	       	   	productManager.getProductById(productID).setAvailable(false);
	    		
	       	   	//Then, delete the product from customer's baskets if they are not in the middle of a reservation.
	       	   	String deleteQuery = "DELETE FROM ADMINDATABASE.BASKET b WHERE b.productID = ? AND NOT EXISTS (SELECT r.productID FROM ADMINDATABASE.RESERVATION r WHERE r.productID = b.productID AND r.clientUsername = b.clientUsername)";
	        	PreparedStatement statement = conn.prepareStatement(deleteQuery);
	    		statement.setInt(1, productID);
	    		statement.executeUpdate();
	   	   	}
	   	   	catch (SQLException se)
	   	   	{
	   	   		System.out.println(se.getMessage());
	   	   	}
	    }

	    /**
	     * Modifies the properties of one product, in the database and in the application.
	     * @param productID The identifier of the product.
	     * @param name The new name of the product.
	     * @param description The new description of the product.
	     * @param brand The new brand of the product.
	     * @param price The new price of the product.
	     * @param stock The new stock of the product.
	     * @param available The new availability of the product.
	     * @param colors The new colors of the product.
	     * @param imageFile The new image representing the product.
	     * @param discount The new discount for the product.
	     * @return True if the update was successful, false if the product is present in a customer's reservation.
	     */
	    public boolean updateOneProduct(int productID, String name, String description, String brand, double price, int stock, boolean available, String colors, File imageFile, double discount)
	    {
	    	String selectQuery = "SELECT quantity FROM ADMINDATABASE.RESERVATION WHERE productID = ?";
	    	try
	    	{
	    		Connection conn = getH2Connection();
	    		PreparedStatement statement = conn.prepareStatement(selectQuery);
	    		statement.setInt(1, productID);
	    		ResultSet result = statement.executeQuery();
	    		//If a reservation contains this product, the admin cannot modify it. 
	    		if (result.next())
	    			return false;
	    		
	    		//Otherwise, the properties of the product are updated.
	    		String updateQuery = "UPDATE ADMINDATABASE.PRODUCTS SET productName = ?, productDescription = ?, productBrand = ?, price = ?, inStock = ?, available = ?, colors = ?, discount = ?";
	    		if (imageFile != null)
	    			updateQuery += ", image = ?";
	    		updateQuery += "WHERE id = ?";
	    		statement = conn.prepareStatement(updateQuery);
	         	
	    		int count = 1;
	    		//Replaces placeholders with the values to modify.
	    		statement.setString(count++, name);
	         	statement.setString(count++, description);
	         	statement.setString(count++, brand);
	         	statement.setDouble(count++, price);
	         	statement.setInt(count++, stock);
	         	statement.setBoolean(count++, available);
	         	statement.setString(count++, colors);
	         	statement.setDouble(count++, discount);
	         	if (imageFile != null)
	         	{
	                FileInputStream fis = new FileInputStream(imageFile);
	                statement.setBinaryStream(count++, fis, (int) imageFile.length());
	         	}

	         	statement.setInt(count++, productID);
	            statement.executeUpdate();
	            
	            if (!available)
	            	propagateChangesWhenProductNotAvailable(productID);
	            
	            return true;
	    	}
	    	
	        catch (SQLException | FileNotFoundException e) {
	    	   	System.out.println(e.getMessage());
	       }
	    	
	    
	    	return false;
	  
	           

	    }

	    /**
	     * Decreases the stock of all products in a completed order according to their ordered respective quantities.
	     * @param orderID The identifier of the order.
	     */
	    public void updateProductStocksAfterOrder(int orderID)
	    {
	    	String updateQuery = "UPDATE ADMINDATABASE.PRODUCTS p SET p.inStock = p.inStock - (SELECT o.quantity FROM ADMINDATABASE.ORDERCONTENT o WHERE p.id = o.productID AND o.orderID = ?) WHERE EXISTS (SELECT 1 FROM ADMINDATABASE.ORDERCONTENT o WHERE p.id = o.productID AND o.orderID = ?);";

	        try
	        {
	        	Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(updateQuery);
	     	    statement.setInt(1, orderID);
	     	    statement.setInt(2, orderID);
	            statement.executeUpdate();
	        }

	        catch (SQLException e) 
	        {
	        	System.out.println(e.getMessage());
	        }
	    }
	    
	    /**
	     * Gets all customers matching the desired filter and store them in the customer manager.
	     * @param filter A filter containing criteria based upon which the customers are selected from the database.
	     */
	    public void getCustomersWithFilters(CustomerFilter filter)
	    {
	    	//Resets the customers matching the previous criteria.
	    	customerManager.resetCustomers();
	    	
	    	//Separates all keywords used to find customers.
	    	String[] currentKeywords = filter.getKeywords().split("[,; |]+");
	    	int numberOfKeywords = currentKeywords.length;
	    	StringBuilder selectionQuery = new StringBuilder();
	    	boolean withKeywords = !currentKeywords[0].equals("");
	    	
	    	//If there are no keywords used.
	    	if (!withKeywords)
	    	{
	    		selectionQuery.append("SELECT lastName, firstName, email, username, credit, vip FROM ADMINDATABASE.CLIENTS WHERE credit >= ?");
	        	if (filter.getVip() == -1)
	        		selectionQuery.append(" AND vip = false");
	        	else if (filter.getVip() == 1)
	        		selectionQuery.append(" AND vip = true");
	    		if (filter.getOrderByCredit() && filter.getOrderByName())
	        		selectionQuery.append(" ORDER BY credit DESC, lastName ASC");
	        	else if (filter.getOrderByCredit())
	        		selectionQuery.append(" ORDER BY credit DESC");
	        	else if (filter.getOrderByName())
	        		selectionQuery.append(" ORDER BY lastName ASC");
	    	}
	    	
	    	else
	    	{
	    		selectionQuery.append("SELECT lastName, firstName, email, username, credit, vip FROM (SELECT lastName, firstName, email, username, credit, vip, (");
	    		
	    		//For every keyword, a better score is given to the customer if the keyword appears either in the customer's name, first name or username.
	    		for (int i = 0; i < numberOfKeywords - 1; i++)
	    		{
	    			//selectionQuery.append("IF(MATCH(lastName, firstName, username) AGAINST (?), ?, 0) + IF(lastName ILIKE ?, ?, 0) + IF(firstName ILIKE ?, ?, 0) + IF(username ILIKE ?, ?, 0) + ");
	    			selectionQuery.append("CASE WHEN lastName ILIKE ? THEN ? ELSE 0 END + CASE WHEN firstName ILIKE ? THEN ? ELSE 0 END + CASE WHEN username ILIKE ? THEN ? ELSE 0 END + ");
	    		}
	    		//selectionQuery.append("IF(MATCH(lastName, firstName, username) AGAINST (?), ?, 0) + IF(lastName ILIKE ?, ?, 0) + IF(firstName ILIKE ?, ?, 0) + IF(username ILIKE ?, ?, 0)) AS relevance FROM Clients) AS RelevanceTable WHERE credit >= ? AND relevance >= ?");
	    		selectionQuery.append("CASE WHEN lastName ILIKE ? THEN ? ELSE 0 END + CASE WHEN firstName ILIKE ? THEN ? ELSE 0 END + CASE WHEN username ILIKE ? THEN ? ELSE 0 END) AS relevance FROM ADMINDATABASE.CLIENTS) AS RelevanceTable WHERE credit >= ? AND relevance >= ?");
	        	if (filter.getVip() == -1)
	        		selectionQuery.append(" AND vip = false");
	        	else if (filter.getVip() == 1)
	        		selectionQuery.append(" AND vip = true");
	    		if (filter.getOrderByCredit() && filter.getOrderByName())
	        		selectionQuery.append(" ORDER BY credit DESC, lastName ASC, relevance DESC");
	        	else if (filter.getOrderByCredit())
	        		selectionQuery.append(" ORDER BY credit DESC, relevance DESC");
	        	else if (filter.getOrderByName())
	        		selectionQuery.append(" ORDER BY lastName ASC, relevance DESC");
	        	else
	        		selectionQuery.append(" ORDER BY relevance DESC");
	    	}

	    	try
	    	{
	    		Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(selectionQuery.toString());
	            
	            
	        	if (withKeywords)
	        	{
	        		int count = 1;
	        		double relevanceThreshold = 1f;
	            	for (int i = 0; i < numberOfKeywords; i++)
	        		{
	            		//If the keyword is the last one, it is probably still being typed and thus matters less for the relevance.
	            		double value = (i == numberOfKeywords - 1) ? 2f : 1f;
	            		//A full match gives more relevance points than a partial one (hence the difference between value and value/2).
	            		//statement.setString(count++, currentKeywords[i]);
	            		//statement.setDouble(count++, value);
	            		statement.setString(count++, '%'+currentKeywords[i]+'%');
	            		statement.setDouble(count++, value/2);
	            		statement.setString(count++, '%'+currentKeywords[i]+'%');
	            		statement.setDouble(count++, value/2);
	            		statement.setString(count++, '%'+currentKeywords[i]+'%');
	            		statement.setDouble(count++, value/2);
	        		}
	        		
	        		statement.setDouble(count++, filter.getCredit());
	        		statement.setDouble(count++, relevanceThreshold);
	        	}
	        	
	        	else
	        	{
	        		statement.setDouble(1, filter.getCredit());
	        	}
	            
	            ResultSet result = statement.executeQuery();
	           
	            //For every customer matching these criteria, creates a new Customer profile containing their information.
	            while (result.next())
	            {
	            	String lastName = result.getString("lastName");
	            	String firstName = result.getString("firstName");
	            	String email = result.getString("email");
	            	String username = result.getString("username");
	            	double credit = result.getDouble("credit");
	            	boolean vip = result.getBoolean("vip");
	            	customerManager.addCustomer(new CustomerProfile(lastName, firstName, email, username, credit, vip));
	            }
	    	}
	    	catch (SQLException e)
	    	{
	    		System.out.println(e.getMessage());
	    	}
	    }

	    /**
	     * Modifies the personal information of a customer in the database.
	     * @param customer The updated customer profile containing the information that needs to be put in the database.
	     */
	    public void updateCustomerAccount(CustomerProfile customer)
	    {
	    	String updateQuery = "UPDATE ADMINDATABASE.CLIENTS SET vip = ? WHERE username = ?";
	        try 
	        {
	            Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(updateQuery);
	            //Replaces placeholders with the values to update.
	            statement.setBoolean(1, customer.isVip());
	            statement.setString(2, customer.getUsername());
	            statement.executeUpdate();
	        }

	        catch (SQLException e) 
	        {
	           	System.out.println(e.getMessage());
	        }
	    }

	    /**
	     * Deletes a customer's account from the database, along with all their baskets, reservations and orders.
	     * @param customer The customer profile that need to be deleted from the database.
	     */
	    public void deleteCustomerAccount(CustomerProfile customer)
	    {
	    	String updateQuery = "DELETE FROM ADMINDATABASE.CLIENTS WHERE username = ?";
	        try 
	        {
	            Connection conn = getH2Connection();
	            PreparedStatement statement = conn.prepareStatement(updateQuery);
	            statement.setString(1, customer.getUsername());
	            statement.executeUpdate();
	        }

	        catch (SQLException e) 
	        {
	           	System.out.println(e.getMessage());
	        }
	    }
	    
	    /**
	     * Launches a discount of specified value for all products matching the given criteria in the database.
	     * @param colors A map containing as entries the identifier of the color and as values their names. If the product matches one of these colors, it satisfies this filter.
	     * @param priceMin The minimum price for the product (in €).
	     * @param priceMax The maximum price for the product (in €).
	     * @param discount The value of the discount (between 0 and 1 : 0 corresponds to no discount and 1 to a 100% discount).
	     * @param brands The brands targeted by the discount, separated by commas or blank spaces. An empty string means all brands are selected.
	     */
	    public void launchGlobalDiscount(Map<Integer, String> colors, double priceMin, double priceMax, double discount, String[] brands)
	    {
	    	//Stores the identifiers of the products affected by this discount to then update them in the database and in the application.
	    	List<Integer> affectedProducts = new ArrayList<>();
	    	
	    	StringBuilder selectQuery = new StringBuilder("SELECT id FROM ADMINDATABASE.PRODUCTS WHERE price >= ? AND price <= ?");
	    	
	    	//If some targeted colors have been selected.
	    	if (colors.size() > 0)
	    	{
				selectQuery.append(" AND (");
				for (int i = 0; i < colors.size() - 1; i++)
				{
					//selectQuery.append("MATCH(colors) AGAINST (?) OR ");
					selectQuery.append("colors ILIKE ? OR ");
				}
				//selectQuery.append("MATCH(colors) AGAINST (?))");
				selectQuery.append("colors ILIKE ?)");
	    	}
	    	
	    	//If some specific brands have been selected.
	    	if (brands.length > 0)
	    	{
	    		if (!brands[0].equals(""))
	    		{
	    			selectQuery.append(" AND productBrand IN(");
	    			for (int i = 0; i < brands.length - 1; i++)
	    			{
	    				selectQuery.append("?,");
	    			}
	    			selectQuery.append("?)");
	    		}
	    	}
	    	try
	    	{
	    		Connection conn = getH2Connection();
	    		PreparedStatement selectStatement = conn.prepareStatement(selectQuery.toString());
	    		int count = 1;
	    		
	    		//Replaces placeholders with the values to select.
	    		selectStatement.setDouble(count++, priceMin);
	    		selectStatement.setDouble(count++, priceMax);
	    		for (String color : colors.values())
	    		{
	    			//selectStatement.setString(count++, color);
	    			selectStatement.setString(count++, '%'+color+'%');
	    		}
	    		for (String brand : brands)
	    		{
	    			if (!brand.equals(""))
	    				selectStatement.setString(count++, brand);
	    		}
	    		
	    		ResultSet result = selectStatement.executeQuery();
	    		
	    		//Stores the identifier of every affected product 
	    		while(result.next())
	    		{
	    			affectedProducts.add(result.getInt("id"));
	    		}
	    		
	    		//Updates every product affected by the update both in the database and in the application.
	    		if (affectedProducts.size() > 0)
	    		{
	    			StringBuilder updateQuery = new StringBuilder("UPDATE ADMINDATABASE.PRODUCTS SET discount = ? WHERE id IN (");
	    			for (int i = 0; i < affectedProducts.size() - 1; i++)
	    			{
	    				updateQuery.append("?,");
	    			}
	    			updateQuery.append("?)");
	    			
	    			PreparedStatement updateStatement = conn.prepareStatement(updateQuery.toString());
	    			count = 1;
	    			updateStatement.setDouble(count++, discount);
	    			for (int productID : affectedProducts)
	    			{
	    				updateStatement.setInt(count++, productID);
	    				//Update in the application
	    				productManager.getProductById(productID).setDiscount(discount);
	    			}
	    			//Update in the database.
	    			updateStatement.executeUpdate();
	    		}
	    		
	    		
	    		
	    	}
	    	catch (SQLException e)
	    	{
	    		System.out.println(e.getMessage());
	    	}
	    }
	    
	    /**
	     * Inserts an achievement made by the customer
	     * @param description The description of the customer's achievement.
	     * @param imageFile The file that was dropped by the customer to represent their achievement.
	     * @param icon The icon representing the customer's achievement.
	     * @return
	     */
	    public double insertAchievement(String description, File imageFile, ImageIcon icon)
	    {

	    	String insertQuery = "INSERT INTO ADMINDATABASE.ACHIEVEMENTS(clientUsername, achievement, reward, picture, achievementDate) VALUES(?,?,?,?,?)";
	    	
	    	try 
	    	{
	            Random random = new Random();
	            double reward = description.length() * random.nextDouble();
	            if (reward == 0)
	            	return 0;
		    	if (customerManager.getCurrentCustomer().isVip())
		    		reward *= 2;
	            
	    		Connection conn = getH2Connection();
	    		PreparedStatement statement = conn.prepareStatement(insertQuery);
	    		
	    		statement.setString(1, customerManager.getCurrentCustomer().getUsername());
	            statement.setString(2, description);
	            
		    	if (imageFile == null)
		    	{
		            statement.setDouble(3, reward);
		            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("default.png");
		            if (inputStream == null) 
		            {
		                throw new FileNotFoundException("Resource not found: " + "default.png");
		            }
		            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		            byte[] data = new byte[1024];
		            int bytesRead;
		            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) 
		            {
		                buffer.write(data, 0, bytesRead);
		            }
		            buffer.flush();

		            byte[] imageBytes = buffer.toByteArray();

		            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
		            statement.setBinaryStream(4, byteArrayInputStream, imageBytes.length);
		            
		            ByteArrayInputStream imageInputStream = new ByteArrayInputStream(imageBytes);
		            BufferedImage img = ImageIO.read(imageInputStream);
		    		icon = new ImageIcon(img);
		    	}
		    	else
		    	{
		    		reward *= 2;
		            statement.setDouble(3, reward);
		            FileInputStream fis = new FileInputStream(imageFile);
		            statement.setBinaryStream(4, fis, (int) imageFile.length());
		    	}

	            Timestamp now = new Timestamp(System.currentTimeMillis());
	            statement.setTimestamp(5, now);

	            statement.executeUpdate();
	            customerManager.getCurrentCustomer().addCredit(reward);
	            customerManager.getCurrentCustomer().addAchievement(new Achievement(description, reward, icon, now));
	            updateCustomerCredit(reward);
	            return reward;
	    	}
	    	catch(SQLException | IOException e)
	    	{
	    		e.printStackTrace();
	    		return 0;
	    	}	
	    }
	    
	    
	    /**
	     * Checks if the customer becomes a premium member after uploading an achievement. After 5 achievements, a customer becomes premium unless the administrator removed their status.
	     * @return True if the customer becomes a premium member and false if they are already a premium member, or don't have enough achievements, or have had their status removed.
	     */
	    public boolean checkIfCustomerBecomesPremium()
	    {
	    	CustomerProfile customer = customerManager.getCurrentCustomer();
	    	if (customer.getAchievements().size() < 5 || customer.isVip())
	    	{
	    		return false;
	    	}

	    	String updateQuery = "UPDATE ADMINDATABASE.CLIENTS SET vip = ? WHERE username = ?";
	    	
	    	try
	    	{
	    		Connection conn = getH2Connection();
	    		PreparedStatement statement = conn.prepareStatement(updateQuery);
	    		
	    		statement.setBoolean(1, true);
	    		statement.setString(2, customer.getUsername());
	    		
	    		statement.executeUpdate();
	    		
	    		customer.setVip(true);
	    	}
	    	catch(SQLException e)
	    	{
	    		e.printStackTrace();
	    	}
	    	
	    	return true;
	    }
	    
	    
	    /**
	     * Removes the premium status of customers who have not posted achievements in the last 10 days.
	     */
	    public void removePremiumStatuses()
	    {
	    	String updateQuery = "UPDATE ADMINDATABASE.CLIENTS c SET c.vip = ? WHERE NOT EXISTS (SELECT 1 FROM ADMINDATABASE.ACHIEVEMENTS a WHERE a.clientUsername = c.username AND a.achievementDate > DATEADD('DAY', -10, CURRENT_TIMESTAMP))";
	    	try 
	    	{
	    		Connection conn = getH2Connection();
	    		PreparedStatement statement = conn.prepareStatement(updateQuery);
	    		statement.setBoolean(1, false);
	    		statement.executeUpdate();
	    	}
	    	catch (SQLException e)
	    	{
	    		e.printStackTrace();
	    	}
	    }
	    
}


