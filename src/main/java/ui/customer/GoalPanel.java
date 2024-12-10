package ui.customer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ui.GUI;
import ui.tools.CustomScrollBarUI;
import ui.tools.NonOpaqueJLabel;
import ui.tools.NonOpaqueTextArea;

/**
 * A panel that contains a small description of our company and our goal.
 */
public class GoalPanel extends JPanel
{
	public GoalPanel()
	{
		super(new GridBagLayout());
		setOpaque(false);
		
		NonOpaqueJLabel logoIcon = new NonOpaqueJLabel();
        URL imageUrl = getClass().getClassLoader().getResource("Logo.png");
        ImageIcon logo = new ImageIcon(imageUrl);
        logoIcon.setHorizontalAlignment(SwingConstants.CENTER); 
        logoIcon.setVerticalAlignment(SwingConstants.CENTER);
        logoIcon.setIcon(logo);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
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
		
		
		
        JTextPane textPane = new JTextPane();
        textPane.setOpaque(false);
        StyledDocument doc = textPane.getStyledDocument();

        Style boldStyle = textPane.addStyle("Bold", null);
        StyleConstants.setBold(boldStyle, true);
        
        Style redStyle = textPane.addStyle("Red", null);
        StyleConstants.setForeground(redStyle, Color.RED);

        try 
        {
            doc.insertString(doc.getLength(), "What is Pop Up Sport ? \n\n", boldStyle);

            doc.insertString(doc.getLength(), "Pop Up Sport is a governmental initiative, endorsed by the French Ministry of Health.\n", null);

            doc.insertString(doc.getLength(), "In response to growing concerns about sedentary lifestyles and the increasing rates of health issues related to physical inactivity (cardiovascular diseases, obesity, diabetes…), the government sought a way to encourage citizens to ", null);
            doc.insertString(doc.getLength(), "be more active. \n\n", boldStyle);
            
            doc.insertString(doc.getLength(), "Pop Up Sport is THE platform that will motivate you to engage in physical activity and achieve your fitness goals, by rewarding you accordingly (see functioning below).\n\n", null);
            doc.insertString(doc.getLength(), "On this app, you can track, upload and share your sports performances, such as running, swimming, cycling, or walking to name but a few. At the moment, the exhaustive list is made up of all Olympic disciplines, but will soon be extended.\n\n\n", null);
            doc.insertString(doc.getLength(), "How do I get rewarded ?\n\n", boldStyle);
            doc.insertString(doc.getLength(), "The reward system is very intuitive : ", null);
            doc.insertString(doc.getLength(), "Work out. Share. Earn.\n\n", boldStyle);
            doc.insertString(doc.getLength(), "It’s a cinch !\n\n", null);
            doc.insertString(doc.getLength(), "When you upload your sporting performances, you earn cones (", null);
            doc.insertString(doc.getLength(), "\u2359", redStyle);
            doc.insertString(doc.getLength(), "). Cones are Pop Up Sport’s currency. The amount of cones you receive after each upload depends on several factors : intensity, duration, type of sport.\n\n", null);
            doc.insertString(doc.getLength(), "Basically, the more you exercise, the more they accumulate as you progress towards your goals. Their cute design should be reminiscent of your hard training !\n\n\n", null);
            doc.insertString(doc.getLength(),"Premium Status\n\n" , boldStyle);
            doc.insertString(doc.getLength(),"Users may earn Premium status by the time they upload 5 performances, unlocking the ability to earn twice as many cones on future activities. BUT this perk is not granted forever : if you do not share anything with us after becoming a Premium user, the status vanishes within 10 days ! Our best piece of advice : ", null);
            doc.insertString(doc.getLength(), "be consistent !", boldStyle);
            
        } catch (BadLocationException e) 
        {
            e.printStackTrace();
        }
		
		
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setPreferredSize(new Dimension(0,0));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(GUI.BACKGROUND_CUSTOMER, GUI.PRODUCT_CUSTOMER));
		

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(scrollPane, gbc);
		
	}
}
