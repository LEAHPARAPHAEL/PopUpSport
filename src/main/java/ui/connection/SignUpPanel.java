package ui.connection;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import ui.tools.NonOpaqueTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import backend.DatabaseManagerH2.QueryStatus;
import ui.GUI;
import ui.tools.DescendantPanel;
import ui.tools.LoadingBar;
import ui.tools.NonOpaqueJLabel;
import ui.tools.PanelID;
import ui.tools.RoundedButton;
import ui.tools.SwitchingButton;

/**
 * The panel that enables a new customer to create an account.
 */
public class SignUpPanel extends DescendantPanel
{
	private QueryStatus queryStatus;
	
	public SignUpPanel(GUI gui)
	{
		super(new GridBagLayout(), gui);
		setBackground(GUI.BACKGROUND_CUSTOMER);

		//Sets the layout of the panel
		NonOpaqueJLabel signup = new NonOpaqueJLabel("Sign up", SwingConstants.CENTER);
		signup.setFont(new Font("Serif", Font.BOLD, 25));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.3;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(signup, gbc);
        
        gbc = new GridBagConstraints();
		gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        gbc.weighty = 0.3;
        gbc.gridheight = 6;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(new JLabel(), gbc);
       
		
        gbc = new GridBagConstraints();
		gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 1;
        gbc.weighty = 0.3;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(new JLabel(), gbc);
		

        
        
		JLabel name = new JLabel("Name");
		name.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(name, gbc);
        
		JTextField nameField = new JTextField();
		nameField.setBorder(GUI.GRAY_BORDER);  
		nameField.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(nameField, gbc);
        
		JLabel firstName = new JLabel("First name");
		firstName.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(firstName, gbc);
        
		JTextField firstNameField = new JTextField();
		firstNameField.setBorder(GUI.GRAY_BORDER);  
		firstNameField.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(firstNameField, gbc);
        
		JLabel email = new JLabel("Email");
		email.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(email, gbc);
        
        JTextField emailField = new JTextField();
        emailField.setBorder(GUI.GRAY_BORDER);  
        emailField.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(emailField, gbc);
        
		JLabel username = new JLabel("Username");
		username.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(username, gbc);
        
        JTextField usernameField = new JTextField();
        usernameField.setBorder(GUI.GRAY_BORDER);  
        usernameField.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(usernameField, gbc);
        
		JLabel password = new JLabel("Password");
		password.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(password, gbc);
        
        JTextField passwordField = new JTextField();
        passwordField.setBorder(GUI.GRAY_BORDER);        
        passwordField.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(passwordField, gbc);
        

        
        SwitchingButton exit = new SwitchingButton("Back", PanelID.CONNEXION);
        exit.setPreferredSize(new Dimension(0,0));
        exit.setBackground(GUI.RED);
        gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(exit, gbc);
        
		RoundedButton validate = new RoundedButton("Submit");
		validate.setMargin(new Insets(0, 0, 0, 0));
		validate.setPreferredSize(new Dimension(0,0));
		validate.setBackground(GUI.GREEN);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(validate, gbc);
        
        NonOpaqueTextArea nameError = new NonOpaqueTextArea();
        nameError.setEditable(false);
		nameError.setForeground(Color.RED);
        gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(nameError, gbc);
        
        NonOpaqueTextArea firstNameError = new NonOpaqueTextArea();
        firstNameError.setEditable(false);
        firstNameError.setForeground(Color.RED);
        gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(firstNameError, gbc);
        
        NonOpaqueTextArea emailError = new NonOpaqueTextArea();
		emailError.setEditable(false);
        emailError.setForeground(Color.RED);
        gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.weightx = 0.2;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(emailError, gbc);
        
        
        NonOpaqueTextArea usernameError = new NonOpaqueTextArea();
		usernameError.setEditable(false);
        usernameError.setForeground(Color.RED);
        gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.weightx = 0.2;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(usernameError, gbc);
        
        NonOpaqueTextArea passwordError = new NonOpaqueTextArea();
        passwordError.setEditable(false);
        passwordError.setForeground(Color.RED);
        gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.weightx = 0.2;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(passwordError, gbc);
        
        
        Runnable onTimeUp = () -> {
			switch (queryStatus) {
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
		    	gui.switchPanel(PanelID.MAIN);
		    	break;
		    default:
		    	break;
			}
        };
        
        LoadingBar loadingBar = new LoadingBar(onTimeUp);
        gbc = new GridBagConstraints();
		gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 1;
        gbc.weighty = 0.05;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(loadingBar, gbc);
        
        
        exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (loadingBar.isVisible())
				{
					loadingBar.stop();
				}
			}
        	
        });
        
        Border defaultBorder = nameField.getBorder();
        
     
	    
	    validate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (loadingBar.isVisible())
				{
					return;
				}
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
				
				String clientName = nameField.getText();
				String clientFirstName = firstNameField.getText();
				String clientEmail = emailField.getText();
				String clientUsername = usernameField.getText();
				String clientPassword = passwordField.getText();
				
				boolean valid = true;
				if(clientName.equals(""))
				{
					nameField.setBorder(new LineBorder(Color.RED));
					nameError.setText("This field should not be empty.");
					valid = false;
				}
				if(clientFirstName.equals(""))
				{
					firstNameField.setBorder(new LineBorder(Color.RED));
					firstNameError.setText("This field should not be empty.");
					valid = false;
				}
				if(clientEmail.equals(""))
				{
					emailField.setBorder(new LineBorder(Color.RED));
					emailError.setText("This field should not be empty.");
					valid = false;
				}
				if(clientUsername.equals(""))
				{
					usernameField.setBorder(new LineBorder(Color.RED));
					usernameError.setText("This field should not be empty.");
					valid = false;
				}
				if(clientPassword.equals(""))
				{
					passwordField.setBorder(new LineBorder(Color.RED));
					passwordError.setText("This field should not be empty.");
					valid = false;
				}
				
				if(!valid)
					return;
				
				queryStatus = QueryStatus.UNKNOWN_ERROR;
				loadingBar.load();
				queryStatus = gui.getDatabaseManager().insertNewClient(clientName, clientFirstName, clientEmail, clientUsername, clientPassword);


			}
	    });
	    
	}
}
