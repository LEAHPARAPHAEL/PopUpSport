package ui.customer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import ui.GUI;
import ui.tools.DescendantPanel;
import ui.tools.InfoPanel;
import ui.tools.NonOpaqueJLabel;
import ui.tools.PanelID;
import ui.tools.SwitchingButton;

/**
 * The main panel containing a menu bar enabling the customer to navigate between shopping to managing their account.
 */
public class MainCustomerPanel extends DescendantPanel
{
	private JPanel currentPanel;
	private Timer reservationTimer;
	private InfoPanel infoPanel;
	private JLabel reservationLabel;
	private JButton reservationButton;
	private JMenu credit;
	
	public MainCustomerPanel(GUI gui)
	{
		super(new GridBagLayout(), gui);
		setBackground(GUI.BACKGROUND_CUSTOMER);
        //The menu panel that will always be present in the customer interface and that allows the customer to shop and look at their profile, orders and basket.

    	//Menu creation
    	JMenuBar menuBar = new JMenuBar();
    	menuBar.setBackground(GUI.PRODUCT_CUSTOMER);
    	menuBar.setBorder(null);
    	menuBar.setPreferredSize(new Dimension(0,0));
    	JMenu shop = new JMenu("Shopping");
    	shop.setBorder(null);
    	JMenu info = new JMenu("Who are we?");
    	info.setBorder(null);
    	JMenuItem infos = new JMenuItem("Our goal");
    	infos.setBorder(null);
    	infos.setBackground(GUI.BACKGROUND_CUSTOMER);
        JMenuItem onlineShopping = new JMenuItem("Online shop");
        onlineShopping.setBorder(null);
        onlineShopping.setBackground(GUI.BACKGROUND_CUSTOMER);
        
        shop.add(onlineShopping);
        info.add(infos);
        menuBar.add(shop);
        menuBar.add(info);

        JMenu accountInfo = new JMenu("My account");
        accountInfo.setBorder(null);
        JMenuItem profile = new JMenuItem("Profile");
        profile.setBackground(GUI.BACKGROUND_CUSTOMER);
        profile.setBorder(null);
        JMenuItem basket = new JMenuItem("Basket");
        basket.setBorder(null);
        basket.setBackground(GUI.BACKGROUND_CUSTOMER);
        JMenuItem orders = new JMenuItem("My orders");
        orders.setBorder(null);
        orders.setBackground(GUI.BACKGROUND_CUSTOMER);
        JMenuItem achievements = new JMenuItem("My achievements");
        achievements.setBackground(GUI.BACKGROUND_CUSTOMER);
        achievements.setBorder(null);
        JMenuItem logout = new JMenuItem("Log out"); 
        logout.setBackground(GUI.BACKGROUND_CUSTOMER);
        this.credit = new JMenu(String.format("<html>Credit : %.2f <span style='font-weight:bold; font-size:10px;'>\u2359</span> </html>", gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getCredit()));
        credit.setForeground(GUI.GREEN);
        JMenuItem creditInfo = new JMenuItem("<html>You can add credit to<br>your account by<br>uploading new<br>achievements !</html>");
        credit.add(creditInfo);
        creditInfo.setBackground(GUI.BACKGROUND_CUSTOMER);
        JMenu premium = new JMenu(gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().isVip() ? "Premium" : "       ");
        premium.setForeground(GUI.ORANGE);
        premium.setFont(new Font("Serif", Font.BOLD, 20));
        JMenuItem premiumInfo = new JMenuItem("<html>A premium account<br>allows you to get more<br>rewards from your<br>achievements !</html>");
        premiumInfo.setBackground(GUI.BACKGROUND_CUSTOMER);
        premium.add(premiumInfo);
        
        menuBar.add(Box.createHorizontalGlue());
        accountInfo.add(profile);
        accountInfo.add(basket);
        accountInfo.add(orders);
        accountInfo.add(achievements);
        accountInfo.add(logout);
        menuBar.add(premium);
        menuBar.add(credit);
        menuBar.add(accountInfo);
       
        
        //Button to switch to the shopping panel
        onlineShopping.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainCustomerPanel mainCustomerPanel = (MainCustomerPanel)SwingUtilities.getAncestorOfClass(MainCustomerPanel.class, menuBar);
				if (mainCustomerPanel != null)
					mainCustomerPanel.switchPanel(PanelID.SHOPPING);
			}
        });
        
        infos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainCustomerPanel mainCustomerPanel = (MainCustomerPanel)SwingUtilities.getAncestorOfClass(MainCustomerPanel.class, menuBar);
				if (mainCustomerPanel != null)
					mainCustomerPanel.switchPanel(PanelID.GOAL);
			}
        });
        
        
        //Button to switch to the basket panel
        basket.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainCustomerPanel mainCustomerPanel = (MainCustomerPanel)SwingUtilities.getAncestorOfClass(MainCustomerPanel.class, menuBar);
				if (mainCustomerPanel != null)
				{
					if (gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().hasOngoingReservation())
						mainCustomerPanel.switchPanel(PanelID.PAYMENT);
					else
						mainCustomerPanel.switchPanel(PanelID.BASKET);
				}
			}
        });
        
        //Button to log out and switch to the connection panel
        logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		    	GUI gui = (GUI) SwingUtilities.getAncestorOfClass(GUI.class, menuBar);
		    	if (gui != null)
		    		gui.switchPanel(PanelID.CONNEXION);
				
			}
		});
        
        achievements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainCustomerPanel mainCustomerPanel = (MainCustomerPanel)SwingUtilities.getAncestorOfClass(MainCustomerPanel.class, menuBar);
				if (mainCustomerPanel != null)
					mainCustomerPanel.switchPanel(PanelID.ACHIEVEMENTS);
			}
		});
        
        
        //If the customer logs out properly, their basket is saved in the database.
        logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				gui.getDatabaseManager().saveBasketInDatabase();
			}
        	
        });
        
        //Button to switch to the editable customer's profile.
        profile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainCustomerPanel mainCustomerPanel = (MainCustomerPanel)SwingUtilities.getAncestorOfClass(MainCustomerPanel.class, menuBar);
				if (mainCustomerPanel != null)
				{
					mainCustomerPanel.switchPanel(PanelID.CREDIT);
				}
			}
        });
        
        //Button to switch to the customer's orders.
        orders.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainCustomerPanel mainCustomerPanel = (MainCustomerPanel)SwingUtilities.getAncestorOfClass(MainCustomerPanel.class, menuBar);
				if (mainCustomerPanel != null)
				{
					mainCustomerPanel.switchPanel(PanelID.ORDER);
				}
			}
        });
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        gbc.insets = new Insets(5,5,5,5);
        add(menuBar, gbc);
        
        //By default, when the customer logs in, the first thing they see is the shopping panel containing the catalog. 
        currentPanel = gui.PanelFactory(PanelID.SHOPPING);
        currentPanel.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 0.9;
        gbc.insets = new Insets(5,5,5,5);
        add(currentPanel, gbc);
        
        //The info panel is a small panel that can display a message between the menu panel and the current panel.
        infoPanel = new InfoPanel();
        infoPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
        infoPanel.setPreferredSize(new Dimension(0,0));

        
        //If the customer has a reservation to complete
        if (gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().hasReservationToComplete())
        	beginReservation();

	}
	
	
	/**
	 * Starts a new reservation for the customer or continues one they had to complete.
	 */
	public void beginReservation()
	{
		//If they don't have to complete a reservation, begin a new reservation
        if (!gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().hasReservationToComplete())
        {
        	gui.getDatabaseManager().beginReservation();
    		remove(currentPanel);
    		currentPanel = gui.PanelFactory(PanelID.PAYMENT);
    		currentPanel.setPreferredSize(new Dimension(0,0));
        }
		doShowInfoPanel();
		
		//A timer that is displayed in the info panel and indicates the remaining time for the reservation
		
		reservationLabel = new JLabel();
		reservationLabel.setOpaque(true);
		reservationLabel.setBackground(GUI.INFO_CUSTOMER);
		
		reservationTimer = new Timer(1000, new ActionListener() {
			
			long reservationTimeLeft = gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getReservationTimeLeft();
			@Override
            public void actionPerformed(ActionEvent e) {
            	reservationTimeLeft--;
            	reservationLabel.setText("Your basket is reserved for the next: " + reservationTimeLeft/60 + ":"+ reservationTimeLeft%60+".");
            	reservationLabel.setHorizontalAlignment(SwingConstants.CENTER);
                if (reservationTimeLeft <= 0) {
                    reservationTimer.stop(); 
                    infoPanel.remove(reservationLabel);
                    doShowInfoPanel();
                    endReservation(false); 
                }
            }
        });
		infoPanel.addComponent(reservationLabel);
		doShowInfoPanel();
		reservationTimer.start();
		
        revalidate();
        repaint();

        
	}
	
	/**
	 * If the reservation time runs out or the reservation is completed, the reservation is deleted from the database and the reservation timer is removed.
	 * @param isCompleted True if the reservation is confirmed and false if it has been cancelled.
	 */
	public void endReservation(boolean isCompleted)
	{
		gui.getDatabaseManager().cancelReservation();
		if (isCompleted)
		{
			reservationTimer.stop();
			infoPanel.remove(reservationLabel);
			doShowInfoPanel();
		}
		else
		{
			remove(currentPanel);
			currentPanel = gui.PanelFactory(PanelID.SHOPPING);
			currentPanel.setPreferredSize(new Dimension(0,0));
	        showInfoPanelForTheNext(10, "Your order has been cancelled.");
		}
	}
	
	/**
	 * Organizes the layout whether the info panel is shown or not.
	 */
	public void doShowInfoPanel()
	{
		boolean isShowingInfoPanel = !infoPanel.isEmpty();
		//If we need to show the info panel, the current panel should take a little less space.
		if (isShowingInfoPanel)
		{
			remove(currentPanel);
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 2;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.8;
	        gbc.insets = new Insets(5,5,5,5);
	        add(currentPanel, gbc);
	        
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 1;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.1;
	        gbc.insets = new Insets(5,5,5,5);
	        add(infoPanel, gbc);
		}
		
		//Otherwise, it should take all the available space under the menu bar.
		else
		{
	    	remove(infoPanel);
	    	remove(currentPanel);
	    	GridBagConstraints gbc = new GridBagConstraints();
	    	gbc.gridx = 0;
	        gbc.gridy = 1;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.9;
	        gbc.insets = new Insets(5,5,5,5);
	        add(currentPanel, gbc);
		}
        revalidate();
        repaint();
	}
	
	/**
	 * Shows the given message on the info panel for the given time.
	 * @param duration The time during which the message will be displayed.
	 * @param message The message to display on the info panel.
	 */
	public void showInfoPanelForTheNext(int duration, String message)
	{

		JLabel infoLabel = new JLabel(message, SwingConstants.CENTER);
		infoLabel.setOpaque(true);
		infoLabel.setBackground(GUI.INFO_CUSTOMER);
		infoPanel.addComponent(infoLabel);
		
		Timer timer = new Timer(50, new ActionListener() {
			int timeLeft = duration * 20; 
			@Override
            public void actionPerformed(ActionEvent e) {
            	timeLeft --;
                if (timeLeft <= 0) {
                	((Timer) e.getSource()).stop();
                	infoPanel.remove(infoLabel);
                	doShowInfoPanel();
                }
            }
        });
		timer.start();
		doShowInfoPanel();
	}
	
	/**
	 * Switches the current panel to a new panel corresponding to the identifier given as an argument.
	 * @param nextPanelID The identifier of the next panel to switch to.
	 */
	public void switchPanel(PanelID nextPanelID)
	{
		remove(currentPanel);
		currentPanel = gui.PanelFactory(nextPanelID);
		currentPanel.setPreferredSize(new Dimension(0,0));
        doShowInfoPanel();
	}
	
	/**
	 * Switches the current panel to a new panel.
	 * @param nextPanel The new panel to switch to.
	 */
	public void switchPanel(JPanel nextPanel)
	{
		remove(currentPanel);
		currentPanel = nextPanel;
		currentPanel.setPreferredSize(new Dimension(0,0));
        doShowInfoPanel();
	}
	
	/**
	 * Switches the current panel to a new panel corresponding to the given identifier and shows a message on the info panel for a certain time.
	 * @param duration The time during which the message will be displayed.
	 * @param message The message to display on the info panel.
	 * @param nextPanelID The identifier of the next panel to display.
	 */
	public void showInfoPanelAndSwitch(int duration, String message, PanelID nextPanelID)
	{
		switchPanel(nextPanelID);
		showInfoPanelForTheNext(duration, message);
	}
	
	public void updateCredit()
	{
		credit.setText(String.format("<html>Credit : %.2f <span style='font-weight:bold; font-size:10px;'>\u2359</span> </html>", gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getCredit()));
	}
	
	

}
