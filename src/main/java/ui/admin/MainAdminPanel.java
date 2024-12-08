package ui.admin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.Box;
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
import ui.tools.PanelID;

/**
 * The main panel of the administrator UI containing the menu bar from which all management functionalities are available.
 */
public class MainAdminPanel extends DescendantPanel
{
	private JPanel currentPanel;
	private InfoPanel infoPanel;
	
	public MainAdminPanel(GUI gui)
	{
		super(new GridBagLayout(), gui);
		setBackground(GUI.BACKGROUND_ADMIN);
		//A menu bar that is always there, enabling the admin to manage products, customers or log out.
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorder(null);
		menuBar.setBackground(GUI.PRODUCT_ADMIN);
		menuBar.setPreferredSize(new Dimension(0,0));
		JMenu customers = new JMenu("Customers");
		customers.setBorder(null);
		JMenu products = new JMenu("Products");
		products.setBorder(null);
		JMenuItem manageCustomers = new JMenuItem("Manage customers");
		manageCustomers.setBorder(null);
		manageCustomers.setBackground(GUI.BACKGROUND_ADMIN);
		JMenuItem manageProducts = new JMenuItem("Manage products");
		manageProducts.setBorder(null);
		manageProducts.setBackground(GUI.BACKGROUND_ADMIN);
		JMenuItem globalDiscount = new JMenuItem("Global discount");
		globalDiscount.setBorder(null);
		globalDiscount.setBackground(GUI.BACKGROUND_ADMIN);
		customers.add(manageCustomers);
		products.add(manageProducts);
		products.add(globalDiscount);
		menuBar.add(customers);
		menuBar.add(products);
		
		globalDiscount.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				switchPanel(PanelID.DISCOUNT);
			}
		});
		
		
		manageCustomers.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				switchPanel(PanelID.CUSTOMER_MANAGEMENT);
			}
		});
		
		manageProducts.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				switchPanel(PanelID.PRODUCT_MANAGEMENT);
			}
		});

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		add(menuBar, gbc);
		JMenu admin = new JMenu("Admin");
		admin.setBorder(null);
		
		JMenuItem logout = new JMenuItem("Log out");
		logout.setBorder(null);
		logout.setBackground(GUI.BACKGROUND_ADMIN);
		admin.add(logout);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(admin);
		
		
		logout.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				gui.switchPanel(PanelID.CONNEXION);
			}
			
		});
		
		//By default, the first panel the admin sees when they log in is one where they can manage the products.
		this.currentPanel = new ProductManagementPanel(gui);
		currentPanel.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.9;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		add(currentPanel, gbc);
		
		//A small panel that can appear between the menu panel and the current panel to display a text message.
		this.infoPanel = new InfoPanel();
        infoPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
        infoPanel.setPreferredSize(new Dimension(0,0));
		
	}
	
	/**
	 * If this panel needs to display an info message, reorganizes the displayed panels to see it. Otherwise, the current panel takes all the available space.
	 */
	public void doShowInfoPanel()
	{
		boolean isShowingInfoPanel = !infoPanel.isEmpty();
		
		if (isShowingInfoPanel)
		{
			remove(currentPanel);
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 2;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.8;
	        gbc.gridwidth = 1;
	        gbc.insets = new Insets(5,5,5,5);
	        add(currentPanel, gbc);
	        
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 1;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.1;
	        gbc.gridwidth = 1;
	        gbc.insets = new Insets(5,5,5,5);
	        add(infoPanel, gbc);
		}
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
	        gbc.gridwidth = 1;
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
		infoLabel.setBackground(GUI.INFO_ADMIN);
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
	 * Switches the current central panel to a new panel corresponding to the given panel identifier.
	 * @param nextPanelID The identifier for the next panel.
	 */
	public void switchPanel(PanelID nextPanelID)
	{
		remove(currentPanel);
		currentPanel = gui.PanelFactory(nextPanelID);
		currentPanel.setPreferredSize(new Dimension(0,0));
        doShowInfoPanel();
	}
	
	/**
	 * Switches the current central panel to a new panel.
	 * @param nextPanel The new panel that needs to be displayed.
	 */
	public void switchPanel(JPanel nextPanel)
	{
		remove(currentPanel);
		currentPanel = nextPanel;
		currentPanel.setPreferredSize(new Dimension(0,0));
        doShowInfoPanel();
	}
	
	/**
	 * Switches the current central panel to a new panel corresponding to the identifier and show a message for a given time.
	 * @param duration The time during which the message will be visible.
	 * @param message The message that will be displayed.
	 * @param nextPanelID The identifier for the next panel that needs to be displayed.
	 */
	public void showInfoPanelAndSwitch(int duration, String message, PanelID nextPanelID)
	{
		switchPanel(nextPanelID);
		showInfoPanelForTheNext(duration, message);
	}
	
	
	
}
