package ui.customer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import backend.CustomerProfile;
import backend.DatabaseManagerH2.QueryStatus;
import ui.GUI;
import ui.tools.*;

/**
 * The panel that enables the customer to modify their personal information.
 */
public class ProfilePanel extends DescendantPanel
{
	private CustomerProfile customer;
	private QueryStatus queryStatus;
	
	public ProfilePanel(GUI gui)
	{
		super(new GridBagLayout(), gui);
		setOpaque(false);
		this.customer = gui.getDatabaseManager().getCustomerManager().getCurrentCustomer();
		
		//Sets the layout of this panel
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.weightx = 1;
        gbc.weighty = 0.3;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        add(new NonOpaqueJLabel(), gbc);
        
       
        gbc = new GridBagConstraints();
		gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        gbc.weighty = 0.45;
        gbc.gridheight = 9;
        gbc.fill = GridBagConstraints.BOTH;
        add(new NonOpaqueJLabel(), gbc);
       
		
        NonOpaqueJLabel profile = new NonOpaqueJLabel("My profile", SwingUtilities.CENTER);
        profile.setFont(new Font("Serif", Font.BOLD, 20));
        profile.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.2;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        add(profile, gbc);
        
        
        gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 7;
        gbc.weightx = 0.3;
        gbc.weighty = 0.15;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;
        add(new NonOpaqueJLabel(), gbc);
       
        gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(new NonOpaqueJLabel(), gbc);
        
		
		NonOpaqueJLabel yourCredit = new NonOpaqueJLabel("Credit");
		yourCredit.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weightx = 0.1;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(yourCredit, gbc);
        
        NonOpaqueJLabel creditAmount = new NonOpaqueJLabel(String.format("<html>%.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span></html>", gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getCredit()));
		creditAmount.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(creditAmount, gbc);
		
        NonOpaqueJLabel premium = new NonOpaqueJLabel(customer.isVip() ? "Premium account" : "", SwingConstants.CENTER);
        premium.setFont(new Font("Serif", Font.BOLD, 20));
        premium.setForeground(GUI.ORANGE);
        premium.setPreferredSize(new Dimension(0,0));
		gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        gbc.weighty = 0.05;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(premium, gbc);
        
        
		NonOpaqueJLabel name = new NonOpaqueJLabel("Name");
		name.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(name, gbc);
        
		JTextField nameField = new JTextField();
		nameField.setText(customer.getName());
		nameField.setPreferredSize(new Dimension(0,0));
		nameField.setBorder(new LineBorder(Color.LIGHT_GRAY));
        gbc = new GridBagConstraints();
		gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(nameField, gbc);
        
		NonOpaqueJLabel firstName = new NonOpaqueJLabel("First name");
		firstName.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.1;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(firstName, gbc);
        
		JTextField firstNameField = new JTextField();
		firstNameField.setPreferredSize(new Dimension(0,0));
		firstNameField.setText(customer.getFirstName());
		firstNameField.setBorder(new LineBorder(Color.LIGHT_GRAY));
        gbc = new GridBagConstraints();
		gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(firstNameField, gbc);
        
		NonOpaqueJLabel email = new NonOpaqueJLabel("Email");
		email.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 0.1;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(email, gbc);
        
        JTextField emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(0,0));
        emailField.setBorder(new LineBorder(Color.LIGHT_GRAY));
        emailField.setText(customer.getEmail());
        gbc = new GridBagConstraints();
		gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(emailField, gbc);
        
		NonOpaqueJLabel username = new NonOpaqueJLabel("Username");
		username.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 0.1;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(username, gbc);
        
        JTextField usernameField = new JTextField();
        usernameField.setBorder(new LineBorder(Color.LIGHT_GRAY));
        usernameField.setPreferredSize(new Dimension(0,0));
        usernameField.setText(customer.getUsername());
        gbc = new GridBagConstraints();
		gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(usernameField, gbc);
        
		NonOpaqueJLabel password = new NonOpaqueJLabel("Password");
		password.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 0.1;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(password, gbc);
        
        JTextField passwordField = new JTextField();
        passwordField.setBorder(new LineBorder(Color.LIGHT_GRAY));
        StringBuilder formerPassword = new StringBuilder();
        for (int i = 0; i < customer.getPassword().length(); i++)
        	formerPassword.append("*");
        passwordField.setText(formerPassword.toString());
        String encodedPassword = formerPassword.toString();
        passwordField.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(passwordField, gbc);

		RoundedButton back = new RoundedButton("Back");
		back.setPreferredSize(new Dimension(0,0));
		back.setBackground(GUI.RED);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.weightx = 0.4;
        gbc.weighty = 0.05;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(back, gbc);
        
		RoundedButton validate = new RoundedButton("Submit");
		validate.setPreferredSize(new Dimension(0,0));
		validate.setBackground(GUI.GREEN);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.weightx = 0.4;
        gbc.weighty = 0.05;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(validate, gbc);
        
        
        NonOpaqueTextArea nameError = new NonOpaqueTextArea();
        nameError.setEditable(false);
		nameError.setForeground(Color.RED);
        gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(nameError, gbc);
        
        NonOpaqueTextArea firstNameError = new NonOpaqueTextArea();
        firstNameError.setEditable(false);
        firstNameError.setForeground(Color.RED);
        gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(firstNameError, gbc);
        
        NonOpaqueTextArea emailError = new NonOpaqueTextArea();
		emailError.setEditable(false);
        emailError.setForeground(Color.RED);
        gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(emailError, gbc);
        
        
        NonOpaqueTextArea usernameError = new NonOpaqueTextArea();
		usernameError.setEditable(false);
        usernameError.setForeground(Color.RED);
        gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(usernameError, gbc);
        
        NonOpaqueTextArea passwordError = new NonOpaqueTextArea();
        passwordError.setEditable(false);
        passwordError.setForeground(Color.RED);
        gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(passwordError, gbc);
        
        
        Runnable onTimeUp = () -> {
			//If an error occurred, indicate it to the customer in the corresponding field.
        	switch (queryStatus){
			case EMAIL_ERROR:
				emailField.setBorder(new LineBorder(Color.RED));
				emailError.setText("An account with this email address already exists.");
				break;
			case USERNAME_ERROR:
				usernameField.setBorder(new LineBorder(Color.RED));
				usernameError.setText("This username is already taken.");
				break;
			case WRONG_EMAIL_PATTERN:
				emailField.setBorder(new LineBorder(Color.RED));
				emailError.setText("Invalid email address.");
				break;
			case INSERT_SUCCESSFUL:
				MainCustomerPanel mainCustomerPanel = (MainCustomerPanel)SwingUtilities.getAncestorOfClass(MainCustomerPanel.class, back);
				mainCustomerPanel.showInfoPanelForTheNext(5, "Your profile has been successfully modified !");
				break;
			default:
				break;
			}
        };
        
        LoadingBar loadingBar = new LoadingBar(onTimeUp);
        gbc = new GridBagConstraints();
		gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.weightx = 1;
        gbc.weighty = 0.05;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(loadingBar, gbc);
        
        
        //Button that switches back to the basket panel
        back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (loadingBar.isVisible())
				{
					loadingBar.stop();
				}
				MainCustomerPanel mainCustomerPanel = (MainCustomerPanel)SwingUtilities.getAncestorOfClass(MainCustomerPanel.class, back);
				if (mainCustomerPanel != null)
				{
					mainCustomerPanel.switchPanel(PanelID.SHOPPING);
				}
			}
        });
        
        Border defaultBorder = nameField.getBorder();
        
        //Attempts to modify the customer's profile.
        validate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (loadingBar.isVisible())
				{
					return;
				}
				//Resets the borders of all error areas.
				emailField.setBorder(defaultBorder);
				usernameField.setBorder(defaultBorder);
				nameField.setBorder(defaultBorder);
				firstNameField.setBorder(defaultBorder);
				passwordField.setBorder(defaultBorder);
				emailError.setText("");
				nameError.setText("");
				firstNameError.setText("");
				usernameError.setText("");
				passwordError.setText("");
				
				double formerCredit = customer.getCredit();
				
				boolean isValid = true;
				if (nameField.getText().equals(""))
				{
					nameField.setBorder(new LineBorder(Color.RED));
					nameError.setText("This field should not be empty.");
				}
				if (firstNameField.getText().equals(""))
				{
					firstNameField.setBorder(new LineBorder(Color.RED));
					firstNameError.setText("This field should not be empty.");
				}
				if (usernameField.getText().equals(""))
				{
					usernameField.setBorder(new LineBorder(Color.RED));
					usernameError.setText("This field should not be empty.");
				}
				if (emailField.getText().equals(""))
				{
					emailField.setBorder(new LineBorder(Color.RED));
					emailError.setText("This field should not be empty.");
				}
				if (passwordField.getText().equals(""))
				{
					passwordField.setBorder(new LineBorder(Color.RED));
					passwordError.setText("This field should not be empty.");
				}
				
				if (!isValid)
				{
					return;
				}
				
				loadingBar.load();
				queryStatus = gui.getDatabaseManager().updateCustomerProfile(nameField.getText(), firstNameField.getText(), emailField.getText(), usernameField.getText(), passwordField.getText().equals(encodedPassword) ? customer.getPassword(): passwordField.getText());
			}
        });
        
	}
}
