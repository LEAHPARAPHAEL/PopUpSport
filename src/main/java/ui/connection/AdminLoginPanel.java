package ui.connection;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import backend.DatabaseManagerH2.QueryStatus;
import ui.GUI;
import ui.tools.DescendantPanel;
import ui.tools.LoadingBar;
import ui.tools.NonOpaqueTextArea;
import ui.tools.PanelID;
import ui.tools.PlaceholderTextField;
import ui.tools.RoundedButton;
import ui.tools.SwitchingButton;

/**
 * The panel used by an administrator to log in.
 */
public class AdminLoginPanel extends DescendantPanel
{
	private QueryStatus queryStatus;
	
	public AdminLoginPanel(GUI gui)
	{
		super(new GridBagLayout(), gui);
		setBackground(GUI.BACKGROUND_ADMIN);
		//Sets the layout of the panel
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.weighty = 1;
        gbc.gridheight = 7;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JLabel(), gbc);
        
		gbc = new GridBagConstraints();
		gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.weighty = 1;
        gbc.gridheight = 7;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JLabel(), gbc);
        
        gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        gbc.weighty = 0.2;
        gbc.gridheight = 4;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JLabel(), gbc);
        
        gbc = new GridBagConstraints();
		gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        gbc.weighty = 0.1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JLabel(), gbc);
        
        gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 0.6;
        gbc.weighty = 0.15;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JLabel(), gbc);
        
        
        JLabel logoIcon = new JLabel();
        URL imageUrl = getClass().getClassLoader().getResource("Logo.png");
        ImageIcon logo = new ImageIcon(imageUrl);
        logoIcon.setPreferredSize(new Dimension(0,0));
        logoIcon.setHorizontalAlignment(SwingConstants.CENTER); 
        logoIcon.setVerticalAlignment(SwingConstants.CENTER);
        logoIcon.setIcon(logo);
        gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        gbc.weighty = 0.6;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        add(logoIcon, gbc);
		
        
		logoIcon.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            	int logoWidth = logoIcon.getWidth();
                int logoHeight = logoIcon.getHeight(); 
                int imageSize = Math.min(logoWidth, logoHeight);
                if (logoWidth > 0 && logoHeight > 0) {
            		Image logoImage = logo.getImage();
            		Image resizedImage = logoImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
            		ImageIcon resizedIcon = new ImageIcon(resizedImage);
            		logoIcon.setIcon(resizedIcon);
                    logoIcon.setHorizontalAlignment(SwingConstants.CENTER); 
                    logoIcon.setVerticalAlignment(SwingConstants.CENTER);
                }
	            logoIcon.revalidate(); 
            }
        });
		
		
		
		
		JLabel client = new JLabel("Login as an admin", SwingConstants.CENTER);
		client.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.2;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(client, gbc);
        
		PlaceholderTextField usernameField = new PlaceholderTextField("Username");
		usernameField.setBorder(new LineBorder(Color.LIGHT_GRAY));
		usernameField.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 0.2;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(usernameField, gbc);
        
        PlaceholderTextField passwordField = new PlaceholderTextField("Password");
        passwordField.setBorder(new LineBorder(Color.LIGHT_GRAY));
		passwordField.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 0.2;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(passwordField, gbc);
        
        SwitchingButton exit = new SwitchingButton("Back", PanelID.LOGIN);
		exit.setPreferredSize(new Dimension(0,0));
		exit.setBackground(GUI.RED);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.weightx = 0.1;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(exit, gbc);
        
        
		RoundedButton validate = new RoundedButton("Submit");
		validate.setMargin(new Insets(0, 0, 0, 0));
		validate.setPreferredSize(new Dimension(0,0));
		validate.setBackground(GUI.GREEN);
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.weightx = 0.1;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(validate, gbc);
        
        NonOpaqueTextArea error = new NonOpaqueTextArea();
        error.setEditable(false);
        error.setForeground(Color.RED);
        gbc = new GridBagConstraints();
		gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        gbc.weighty = 0.1;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(error, gbc);
        

        Runnable onTimeUp = () -> {
			switch (queryStatus)
			{
			case LOGIN_SUCCESSFUL:
				gui.switchPanel(PanelID.ADMIN);
		    	break;
			default:
				usernameField.setBorder(new LineBorder(Color.RED));
				passwordField.setBorder(new LineBorder(Color.RED));
				error.setText("This combination of username and password doesn't match any admin account.");
				break;
			}
        };
        LoadingBar loadingBar = new LoadingBar(onTimeUp);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.weightx = 0.6;
		gbc.weighty = 0.05;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(loadingBar, gbc);
        
		Border defaultBorder = usernameField.getBorder();
        
		
        exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (loadingBar.isVisible())
				{
					loadingBar.stop();
				}
				
			}
        });
		
	    validate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (loadingBar.isVisible())
				{
					return;
				}
				String adminUsername = usernameField.getText();
				String adminPassword = passwordField.getText();
				if (adminUsername.equals("") || adminPassword.equals(""))
					return;
				queryStatus = QueryStatus.UNKNOWN_ERROR;
				
		    	if (gui != null)
		    	{
		    		//Tries to log in the customer
					usernameField.setBorder(defaultBorder);
					passwordField.setBorder(defaultBorder);
		    		error.setText("");
		    		loadingBar.load();

					queryStatus = gui.getDatabaseManager().loginAdmin(adminUsername, adminPassword);
				}

			}
	    });
	    
		
		
		

	    
	    
	}
	
}
