package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.border.LineBorder;
import javax.swing.border.Border;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import backend.DatabaseManagerH2;
import ui.admin.CustomerManagementPanel;
import ui.admin.DiscountPanel;
import ui.admin.MainAdminPanel;
import ui.admin.ProductManagementPanel;
import ui.connection.AdminLoginPanel;
import ui.connection.ConnexionPanel;
import ui.connection.CustomerLoginPanel;
import ui.connection.LoginPanel;
import ui.connection.SignUpPanel;
import ui.customer.AchievementPanelCustomer;
import ui.customer.BasketPanel;
import ui.customer.GoalPanel;
import ui.customer.MainCustomerPanel;
import ui.customer.NewAchievementPanel;
import ui.customer.OrderPanel;
import ui.customer.PaymentAndShipmentPanel;
import ui.customer.ProductInfoPanelCustomer;
import ui.customer.ProfilePanel;
import ui.customer.ShoppingPanel;
import ui.tools.PanelID;
import ui.tools.Refreshable;

/**
 * The object managing all the interactions between the user and the application and the only point in the graphic part of the application to communicate with the database manager. 
 */
public class GUI extends JFrame
{
	public static int WINDOW_WIDTH = 800;
	public static int WINDOW_HEIGHT = 800;
	
	/**
	 * The main panel that can switch depending on the user's actions.
	 */
	private JPanel currentPanel;
	
	
	/**
	 * The object responsible for the synchronization between the H2 database and the application.
	 */
	private DatabaseManagerH2 databaseManagerH2;
	
	/**
	 * The scheduler that manages a parallel thread to synchronize the application with data from the database every 30 seconds.
	 */
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);
	public static Color BLUE = new Color(23, 170, 243);
	public static Color RED = new Color(208, 17, 8);
	public static Color GREEN = new Color(25, 165, 53);
	public static Color ORANGE = new Color(235, 181, 34);
	public static Color BACKGROUND_CUSTOMER = new Color(151, 159, 236);
	public static Color PRODUCT_CUSTOMER = new Color(97, 110, 229);
	public static Color BACKGROUND_ADMIN = new Color(228, 145, 117);
	public static Color PRODUCT_ADMIN = new Color(213, 119, 88);
	public static Color INFO_CUSTOMER = new Color(97, 110, 229);
	public static Color INFO_ADMIN = new Color(213, 119, 88);
	public static Border GRAY_BORDER = new LineBorder(Color.LIGHT_GRAY);
    
    public GUI(DatabaseManagerH2 databaseManagerH2)
    {
    	//Sets a universal design for swing and awt components that doesn't depend on the user's operating system.
    	try
    	{
    		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace(); 
    	}
    	
    	this.databaseManagerH2 = databaseManagerH2;
    	this.currentPanel = PanelFactory(PanelID.CONNEXION);
    	add(currentPanel);
    	setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        //Stop the parallel thread when the window is closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopUpdatingDatabase();
            }
        });
        setVisible(true);
       
        startUpdatingProductQuantities();
    }
    
    
    
    
    /**
     * A factory design pattern that, given the identifier of a panel, returns an instance of the associated panel.
     * @param id A value of the enum PanelID identifying the class of panel to instantiate.
     * @return An instance of the class identified by the value id.
     */
    public JPanel PanelFactory(PanelID id)
    {
    	switch (id)
    	{
    	case MENU:
    		return new MainCustomerPanel(this);
    	case PRODUCT_INFO:
    		return new ProductInfoPanelCustomer(this);
    	case MAIN:
    		return new MainCustomerPanel(this);
    	case ADMIN_LOGIN:
    		return new AdminLoginPanel(this);
    	case LOGIN:
    		return new LoginPanel();
    	case CLIENT_LOGIN :
    		return new CustomerLoginPanel(this);
    	case CONNEXION:
    		return new ConnexionPanel();
    	case SIGN_UP:
    		return new SignUpPanel(this);
    	case SHOPPING:
    		return new ShoppingPanel(this);
    	case BASKET:
    		return new BasketPanel(this);
    	case PAYMENT:
    		return new PaymentAndShipmentPanel(this);
    	case CREDIT:
    		return new ProfilePanel(this);
    	case ORDER:
    		return new OrderPanel(this);
    	case ADMIN:
    		return new MainAdminPanel(this);
    	case CUSTOMER_MANAGEMENT:
    		return new CustomerManagementPanel(this);
    	case PRODUCT_MANAGEMENT:
    		return new ProductManagementPanel(this);
    	case DISCOUNT:
    		return new DiscountPanel(this);
    	case GOAL:
    		return new GoalPanel();
    	case ACHIEVEMENTS:
    		return new AchievementPanelCustomer(this);
    	case NEW_ACHIEVEMENT:
    		return new NewAchievementPanel(this);
    	}
		return null;
    
    }
    
    /**
     * Switches panel with a new panel corresponding to the given identifier.
     * @param newPanelID An identifier for the panel to switch to.
     */
    public void switchPanel(PanelID newPanelID)
    {
    	remove(currentPanel);
    	this.currentPanel = PanelFactory(newPanelID);
    	add(currentPanel);
    	revalidate();
    	repaint();
    }
    
    /**
     * Starts a parallel thread that will synchronize the database with the application every 30 seconds.
     */
    private void startUpdatingProductQuantities() 
    {
        scheduler.scheduleAtFixedRate(() -> {try{updateDatabase();}catch(Exception e) {e.printStackTrace();}}, 0, 10, TimeUnit.SECONDS);
    }
    
    /**
     * Removes the reservations made by customers that have expired and gets the current properties of all products in the database, then refreshes all the panels displaying this information.
     */
    private void updateDatabase()
    {
    	databaseManagerH2.updateProducts();
    	recursivelyRefreshAllPanels(this);
    }
    
    /**
     * Ends the parallel thread that synchronizes the application with the database.
     */
    private void stopUpdatingDatabase()
    {
    	scheduler.shutdown();
    }
    
    /**
     * Finds all descendant component implementing the Refreshable interface of the given component and refreshes them to display fresh information.
     * @param component The component from which the recursive search begins.
     */
    public void recursivelyRefreshAllPanels(Component component)
    {
        if (component instanceof Refreshable) {
            ((Refreshable) component).refresh();
        }
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                recursivelyRefreshAllPanels(child);
            }
        }
    }
    
    /**
     * Gets the database manager responsible for the synchronization between the application and the database.
     * @return The object responsible for the synchronization between the application and the database.
     */
    public DatabaseManagerH2 getDatabaseManager()
    {
    	return databaseManagerH2;
    }
    
    

    
    
}
