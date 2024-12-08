package ui.admin;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import backend.Achievement;
import backend.CustomerProfile;
import ui.GUI;
import ui.tools.*;

public class AchievementPanelAdmin extends DescendantPanel
{
	private List<Achievement> achievements;
	private JScrollPane scrollPane;
	private CustomerProfile customer;

	public AchievementPanelAdmin(GUI gui, CustomerProfile customer) 
	{
		super(new GridBagLayout(), gui);
		setOpaque(false);
		this.customer = customer;
		this.achievements = gui.getDatabaseManager().getCustomerAchievements(customer.getUsername());
		
		
		GridBagConstraints gbc = new GridBagConstraints();
		NonOpaqueJLabel fame = new NonOpaqueJLabel(String.format("%s's achievements", customer.getUsername()), SwingConstants.CENTER);
		fame.setFont(new Font("Serif", Font.BOLD, 20));
		fame.setPreferredSize(new Dimension(0,0));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.8;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(fame, gbc);
		
		RoundedButton back = new RoundedButton("Back");
		back.setPreferredSize(new Dimension(0,0));
		back.setBackground(GUI.RED);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.2;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(back, gbc);
		
		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				AlternatingCustomersPanel parent = (AlternatingCustomersPanel)SwingUtilities.getAncestorOfClass(AlternatingCustomersPanel.class, back);
				parent.alternatePanels(PanelID.CUSTOMER_INFO);
			}
			
		});
		
		ScrollableAchievementPanel scrollableOrderPanel = new ScrollableAchievementPanel();
		
		scrollPane = new JScrollPane(scrollableOrderPanel);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(0,0));
		scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(GUI.BACKGROUND_ADMIN, GUI.PRODUCT_ADMIN));
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.9;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		add(scrollPane, gbc);
		
		scrollableOrderPanel.displayAchievements();
		
		//Resizes the individual order panels to take all the horizontal space available but only a fixed proportion of the available height.
        scrollPane.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) { 
            	int width = scrollPane.getWidth();
            	for (Component comp : scrollableOrderPanel.getComponents())
            	{
            		if (comp instanceof SingularAchievementPanel)
            		{
            			comp.setPreferredSize(new Dimension(width - 30, width/3 - 20));
            			((SingularAchievementPanel)comp).displayAchievement();
            			comp.revalidate();
            		}
            	}
            	scrollableOrderPanel.revalidate();
            	scrollableOrderPanel.repaint();
            }
        });
        
       

	}
	
	private class ScrollableAchievementPanel extends JPanel
	{
		public ScrollableAchievementPanel()
		{
			super(new GridBagLayout());
			setOpaque(false);
		}
		
		/**
		 * Organizes the layout of the panel to display all the customer's orders.
		 */
		public void displayAchievements()
		{
			removeAll();
			
			int count = 20;
		    
			GridBagConstraints gbc;
		    
		    //For every order made by the customer, add it from the bottom to have the most recent ones on top.
		    for (Achievement achievement : achievements)
		    {
		    	if (count == 0)
		    		break;
		    	gbc = new GridBagConstraints();
		    	SingularAchievementPanel singularAchievementPanel = new SingularAchievementPanel(achievement);
		    	singularAchievementPanel.setPreferredSize(new Dimension(0,0));
		    	gbc.gridx = 0;
		    	gbc.gridy = count;
		    	gbc.insets = new Insets(5,5,5,5);
		    	add(singularAchievementPanel, gbc);
		    	count--;
		    }
		}
	}
	
	private class SingularAchievementPanel extends RoundedPanel
	{
		private Achievement achievement;
		private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
		
		public SingularAchievementPanel(Achievement achievement)
		{
			super(new GridBagLayout());
			this.achievement = achievement;
			setBackground(GUI.PRODUCT_ADMIN);
			displayAchievement();
			
		}
		
		/**
		 * Display the address the order needs to be delivered to, the time it was ordered, the expected time of delivery, and the status of the order.
		 */
		public void displayAchievement()
		{
			removeAll();
			GridBagConstraints gbc;
			
			
			NonOpaqueJLabel achievementIcon = new NonOpaqueJLabel();
			achievementIcon.setPreferredSize(new Dimension(0,0));
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0.5;
			gbc.weighty = 0.8;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			add(achievementIcon, gbc);
			
			NonOpaqueJLabel reward = new NonOpaqueJLabel(String.format("<html>Reward : %.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span></html>", achievement.getReward()));
			reward.setPreferredSize(new Dimension(0,0));
			gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.gridheight = 4;
			gbc.weightx = 0.5;
			gbc.weighty = 0.2;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			add(reward, gbc);
			

	        LocalDateTime formattedOrderTime = achievement.getAchievementDate().toLocalDateTime();
	        String formattedDate = formattedOrderTime.format(formatter);
			NonOpaqueTextArea achievementDate = new NonOpaqueTextArea("\n Completed the : "+ formattedDate);
			achievementDate.setEditable(false);
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weightx = 0.5;
			gbc.weighty = 0.2;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			add(achievementDate, gbc);
			
			NonOpaqueTextArea description = new NonOpaqueTextArea(achievement.getDescription());
			description.setEditable(false);
			gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.weightx = 0.5;
			gbc.weighty = 0.8;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			add(description, gbc);
		
			addComponentListener(new ComponentAdapter() {
	            @Override
	            public void componentResized(ComponentEvent e) {
	            	int panelWidth = getWidth();
	                int panelHeight = getHeight(); 
	            	
	                int imageSize = (int)Math.min(0.5*panelWidth, 0.8*panelHeight);
	                if (panelWidth > 0 && panelHeight > 0) {
	            		Image image = achievement.getPicture().getImage();
	            		Image resizedImage = image.getScaledInstance(imageSize - 15, imageSize - 15, Image.SCALE_SMOOTH);
	            		ImageIcon resizedIcon = new ImageIcon(resizedImage);
	            		achievementIcon.setIcon(resizedIcon);
	                    achievementIcon.setHorizontalAlignment(SwingConstants.CENTER); 
	                    achievementIcon.setVerticalAlignment(SwingConstants.CENTER);
	                }
		            revalidate(); 
	            }
	        });
		}


	}


	
	

}