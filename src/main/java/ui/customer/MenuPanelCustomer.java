package ui.customer;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import ui.GUI;
import ui.tools.DescendantPanel;
import ui.tools.PanelID;

/**
 * The menu panel used by the customer to switch the current panel to a variety of panels.
 */
public class MenuPanelCustomer extends DescendantPanel
{
    public MenuPanelCustomer(GUI gui)
    {
    	super(new GridBagLayout(), gui);

    	//The left side of the menu bar contains an access to the shopping section of the application.
    	JMenuBar menuBar = new JMenuBar();
    	menuBar.setPreferredSize(new Dimension(0,0));
        
    	JMenu shop = new JMenu("Shopping");
    	JMenu info = new JMenu("Who are we?");
    	JMenuItem infos = new JMenuItem("Our goal");
        JMenuItem onlineShopping = new JMenuItem("Online shop");

        shop.add(onlineShopping);
        info.add(infos);
        menuBar.add(shop);
        menuBar.add(info);
        
        GridBagConstraints menuGbc = new GridBagConstraints();
        menuGbc.gridx = 0;
        menuGbc.gridy = 0;
        menuGbc.fill = GridBagConstraints.BOTH;
        menuGbc.weightx = 0.6;
        menuGbc.weighty = 1;
        add(menuBar, menuGbc); 
        
        
        //The right side of the menu bar contains the customer's profile, basket, orders as well as the button to log out.
        JMenuBar account = new JMenuBar();
        account.setPreferredSize(new Dimension(0,0));
        
        JMenu accountInfo = new JMenu("My account");
        JMenuItem profile = new JMenuItem("Profile");
        JMenuItem basket = new JMenuItem("Basket");
        JMenuItem orders = new JMenuItem("My orders");
        JMenuItem achievements = new JMenuItem("My achievements");
        JMenuItem logout = new JMenuItem("Log out"); 
        
        JMenu credit = new JMenu(String.format("<html>Credit : %.2f <span style='font-weight:bold; font-size:10px;'>\u2359</span> </html>", gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getCredit()));
        credit.setForeground(GUI.GREEN);
        JMenuItem creditInfo = new JMenuItem("<html>You can add credit to<br>your account by<br>uploading new<br>achievements !</html>");
        credit.add(creditInfo);
        JMenu premium = new JMenu(gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().isVip() ? "Premium" : "       ");
        premium.setForeground(GUI.ORANGE);
        premium.setFont(new Font("Serif", Font.BOLD, 20));
        JMenuItem premiumInfo = new JMenuItem("<html>A premium account<br>allows you to get more<br>rewards from your<br>achievements !</html>");
        premium.add(premiumInfo);
        
        accountInfo.add(profile);
        accountInfo.add(basket);
        accountInfo.add(orders);
        accountInfo.add(achievements);
        accountInfo.add(logout);
        account.add(premium);
        account.add(credit);
        account.add(accountInfo);
        
        menuGbc = new GridBagConstraints();
        menuGbc.gridx = 1;
        menuGbc.gridy = 0;
        menuGbc.fill = GridBagConstraints.BOTH;
        menuGbc.weightx = 0.4;
        menuGbc.weighty = 1;
        add(account, menuGbc); 
        
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
        
        
    }
	

}
