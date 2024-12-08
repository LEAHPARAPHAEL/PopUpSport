package ui.admin;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import backend.CustomerProfile;
import ui.GUI;
import ui.tools.*;
/**
 * A panel that displays a customer's personal information and credit.
 */
public class CustomerInfoPanel extends DescendantPanel
{
	private CustomerProfile customer;
	private boolean isSaving;
	
	public CustomerInfoPanel(GUI gui, CustomerProfile customer)
	{
		super(new GridBagLayout(), gui);
		setOpaque(false);
		this.customer = customer;
		NonOpaqueJLabel username = new NonOpaqueJLabel(customer.getUsername(), SwingUtilities.CENTER);
		username.setFont(new Font("Serif", Font.BOLD, 20));
		username.setPreferredSize(new Dimension(0,0));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.1;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(username, gbc);
		
		RoundedButton back = new RoundedButton("Back");
		back.setBackground(GUI.RED);
		back.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.2;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(back, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.8;
		gbc.weighty = 0.1;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		add(new NonOpaqueJLabel(), gbc);
		
		NonOpaqueJLabel firstName = new NonOpaqueJLabel("First name : "+ customer.getFirstName());
		firstName.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 0.6;
		gbc.weighty = 0.05;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(firstName, gbc);
		
		NonOpaqueJLabel lastName = new NonOpaqueJLabel("Last name : "+customer.getName());
		lastName.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.weightx = 0.6;
		gbc.weighty = 0.05;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(lastName, gbc);
		
		NonOpaqueJLabel email = new NonOpaqueJLabel("Email : "+customer.getEmail());
		email.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.weightx = 0.6;
		gbc.weighty = 0.05;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(email, gbc);
		
		NonOpaqueJLabel credit = new NonOpaqueJLabel(String.format("<html>Credit : %.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span></html>", customer.getCredit()));
		credit.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.weightx = 0.6;
		gbc.weighty = 0.05;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(credit, gbc);
		
		RoundedButton orders = new RoundedButton("Orders");
		orders.setBackground(GUI.ORANGE);
		orders.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.weightx = 0.3;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(orders, gbc);
		
		RoundedButton achievements = new RoundedButton("Achievements");
		achievements.setBackground(GUI.BLUE);
		achievements.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 7;
		gbc.weightx = 0.3;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(achievements, gbc);
		
		achievements.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				AlternatingCustomersPanel parent = (AlternatingCustomersPanel)SwingUtilities.getAncestorOfClass(AlternatingCustomersPanel.class, orders);
				parent.alternatePanels(new AchievementPanelAdmin(gui, customer));
			}
		});
		
		
		NonOpaquePanel vipPanel = new NonOpaquePanel(new GridBagLayout());
		
		NonOpaqueRadio vipY = new NonOpaqueRadio("Yes");
		vipY.setPreferredSize(new Dimension(0,0));
		NonOpaqueRadio vipN = new NonOpaqueRadio("No");
		vipN.setPreferredSize(new Dimension(0,0));
		ButtonGroup buttonGroupCredit = new ButtonGroup();
		buttonGroupCredit.add(vipY);
		buttonGroupCredit.add(vipN);
		if (customer.isVip())
			vipY.setSelected(true);
		else
			vipN.setSelected(true);
		
		
		NonOpaqueJLabel vip = new NonOpaqueJLabel("Premium", SwingConstants.CENTER);
		vip.setPreferredSize(new Dimension(0,0));
		vip.setFont(new Font("Serif", Font.BOLD, 16));
		vip.setForeground(GUI.ORANGE);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0.5;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		vipPanel.add(vip, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		vipPanel.add(vipY, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		vipPanel.add(vipN, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.weightx = 0.6;
		gbc.weighty = 0.1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(vipPanel, gbc);
		
		RoundedButton saveChanges = new RoundedButton("Save");
		saveChanges.setBackground(GUI.GREEN);
		saveChanges.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 8;
		gbc.weightx = 0.3;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(saveChanges, gbc);
		
		RoundedButton deleteAccount = new RoundedButton("Delete account");
		deleteAccount.setBackground(GUI.RED);
		deleteAccount.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 8;
		gbc.weightx = 0.3;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(deleteAccount, gbc);
		
		
		Runnable onTimeUp = () -> {
			MainAdminPanel main = (MainAdminPanel)SwingUtilities.getAncestorOfClass(MainAdminPanel.class, orders);
			if (isSaving)
			{
				main.showInfoPanelForTheNext(10, "Customer account successfully modified !");
			}
			else
			{
				main.showInfoPanelForTheNext(10, "Customer account successfully deleted !");
				AlternatingCustomersPanel parent = (AlternatingCustomersPanel)SwingUtilities.getAncestorOfClass(AlternatingCustomersPanel.class, orders);
				parent.setCurrentCustomer(null);
				parent.alternatePanels(PanelID.CUSTOMERS);
			}
		};
		
		LoadingBar loadingBar = new LoadingBar(onTimeUp);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 9;
		gbc.weightx = 0.6;
		gbc.weighty = 0.1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(loadingBar, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 10;
		gbc.weightx = 0.6;
		gbc.weighty = 0.2;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		add(new NonOpaqueJLabel(), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.2;
		gbc.weighty = 0.8;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridheight = 9;
		add(new NonOpaqueJLabel(), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 2;
		gbc.weightx = 0.2;
		gbc.weighty = 0.8;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridheight = 9;
		add(new NonOpaqueJLabel(), gbc);
		
		
		saveChanges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				isSaving = true;
				loadingBar.load();
				customer.setVip(vipY.isSelected());
				gui.getDatabaseManager().updateCustomerAccount(customer);
			}
		});
		
		orders.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				AlternatingCustomersPanel parent = (AlternatingCustomersPanel)SwingUtilities.getAncestorOfClass(AlternatingCustomersPanel.class, orders);
				parent.alternatePanels(PanelID.ORDER);
			}
		});
		
		deleteAccount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				isSaving = false;
				loadingBar.load();
				gui.getDatabaseManager().deleteCustomerAccount(customer);
			}
		});
		
		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				AlternatingCustomersPanel parent = (AlternatingCustomersPanel)SwingUtilities.getAncestorOfClass(AlternatingCustomersPanel.class, orders);
				parent.alternatePanels(PanelID.CUSTOMERS);
			}
			
		});
		
		
		
		
		

	}
}

