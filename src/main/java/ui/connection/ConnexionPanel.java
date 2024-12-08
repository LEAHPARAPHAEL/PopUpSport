package ui.connection;
import javax.swing.*;
import javax.swing.border.LineBorder;

import ui.GUI;
import ui.tools.PanelID;
import ui.tools.RoundedPanel;
import ui.tools.SwitchingButton;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;

/**
 * The first panel seen by the user when entering the application and enabling them to log in or sign up. 
 */
public class ConnexionPanel extends JPanel
{
	public ConnexionPanel()
	{
		super(new GridBagLayout());
		//setBackground(GUI.BACKGROUND_CUSTOMER);
		//Sets the layout of this panel
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.weighty = 1;
        gbc.gridheight = 4;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JLabel(), gbc);
        
		gbc = new GridBagConstraints();
		gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.weighty = 1;
        gbc.gridheight = 4;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JLabel(), gbc);
        
        gbc = new GridBagConstraints();
		gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        gbc.weighty = 0.4;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JLabel(), gbc);
        
        gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        gbc.weighty = 0.4;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JLabel(), gbc);
        
        gbc = new GridBagConstraints();
		gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 0.2;
        gbc.weighty = 0.2;
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
        gbc.gridwidth = 3;
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
		
        

		SwitchingButton login = new SwitchingButton("Login", PanelID.LOGIN);
		login.setFocusPainted(false);
        login.setPreferredSize(new Dimension(0,0));
        login.setBackground(GUI.ORANGE);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(login, gbc);
        
        SwitchingButton signup = new SwitchingButton("Sign up", PanelID.SIGN_UP);
        signup.setFocusPainted(false);
        signup.setPreferredSize(new Dimension(0,0));
        signup.setBackground(GUI.BLUE);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(signup, gbc);
        
        
        
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();
        g2.setColor(GUI.BACKGROUND_ADMIN);
        g2.fillRect(0, 0, width / 2, height);
        g2.setColor(GUI.BACKGROUND_CUSTOMER);
        g2.fillRect(width / 2, 0, width / 2, height);
        g2.dispose();
    }
	
	
}
