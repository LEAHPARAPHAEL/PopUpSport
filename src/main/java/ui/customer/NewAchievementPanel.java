package ui.customer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.TransferHandler;
import javax.swing.border.LineBorder;

import ui.GUI;
import ui.admin.AlternatingProductPanel;
import ui.tools.*;

public class NewAchievementPanel extends DescendantPanel
{
	
	private File imageFile;
	private ImageIcon icon;
	private JLabel achievementIcon;
	
	public NewAchievementPanel(GUI gui) 
	{
		super(new GridBagLayout(), gui);
		setOpaque(false);
		
		RoundedButton back = new RoundedButton("Back");
		back.setBackground(GUI.RED);
		GridBagConstraints gbc = new GridBagConstraints();
		back.setPreferredSize(new Dimension(0,0));
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
		gbc.weightx = 0.3;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		add(new NonOpaqueJLabel(), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		add(new NonOpaqueJLabel(), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.weightx = 1;
		gbc.weighty = 0.3;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		add(new NonOpaqueJLabel(), gbc);
	
		NonOpaqueJLabel analyze = new NonOpaqueJLabel("Analyzing your amazing performance...", SwingConstants.CENTER);
        analyze.setFont(new Font("Serif", Font.ITALIC, 16));
		analyze.setPreferredSize(new Dimension(0,0));
		analyze.setForeground(Color.DARK_GRAY);
        analyze.setVisible(false);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 1;
		gbc.weighty = 0.05;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(analyze, gbc);
		
		
		
		this.achievementIcon = new JLabel("Drop an image", SwingUtilities.CENTER);
		achievementIcon.setPreferredSize(new Dimension(0,0));
		achievementIcon.setBorder(new LineBorder(Color.LIGHT_GRAY));
		achievementIcon.setBackground(GUI.PRODUCT_CUSTOMER);
		//Adds a drag and drop property to this zone.
		achievementIcon.setTransferHandler(new ImageFileTransferHandler());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridheight = 3;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(achievementIcon, gbc);
		
		
		NonOpaqueJLabel descriptionInfo = new NonOpaqueJLabel("Tell us about your achievement");
		descriptionInfo.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.05;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(descriptionInfo, gbc);
		
		NonOpaqueTextArea description = new NonOpaqueTextArea();
		description.setEditable(true);
		description.setBorder(new LineBorder(Color.LIGHT_GRAY));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.weightx = 0.5;
		gbc.weighty = 0.35;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(description, gbc);

		RoundedButton submit = new RoundedButton("Submit");
		submit.setBackground(GUI.GREEN);
		submit.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(submit, gbc);
		
		
		Runnable onTimeUp = () -> {
			String achievementDescription = description.getText();
			achievementDescription = achievementDescription.length() > 500 ? achievementDescription.substring(0, 500) : achievementDescription;
			double reward = gui.getDatabaseManager().insertAchievement(description.getText(), imageFile, icon);
			
			MainCustomerPanel parent = (MainCustomerPanel)SwingUtilities.getAncestorOfClass(MainCustomerPanel.class, back);
			if (reward == 0)
			{
				parent.showInfoPanelForTheNext(10, "Sadly, this achievement was not registered... Try to provide more details about your performance.");
			}
			else
			{
				String message = String.format("<html>Congratulations ! Your account has been credited with %.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span>.</html>", reward);
				boolean premium = gui.getDatabaseManager().checkIfCustomerBecomesPremium();
				if (premium)
					message += " You are now a premium member !";
				parent.updateCredit();
				parent.showInfoPanelAndSwitch(10, message, PanelID.SHOPPING);
			}
			analyze.setVisible(false);
		};

		LoadingBar loadingBar = new LoadingBar(onTimeUp);
        loadingBar.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.weightx = 1;
		gbc.weighty = 0.05;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(loadingBar, gbc);
		
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (loadingBar.isVisible())
				{
					return;
				}
				analyze.setVisible(true);
				loadingBar.load();
			}
			
		});
		
		
		back.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (loadingBar.isVisible())
				{
					loadingBar.stop();
				}
				MainCustomerPanel parent = (MainCustomerPanel)SwingUtilities.getAncestorOfClass(MainCustomerPanel.class, back);
				parent.switchPanel(PanelID.ACHIEVEMENTS);
			}
		});
		
	    for (ComponentListener listener : getComponentListeners()) {
	        removeComponentListener(listener);
	    }
		
	    
		addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            	int panelWidth = getWidth();
                int panelHeight = getHeight(); 
                int imageSize = Math.min(panelWidth, panelHeight)/2;
                if (panelWidth > 0 && panelHeight > 0) {
            		if (icon != null)
            		{
                    	Image productImage = icon.getImage();
                		Image resizedImage = productImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
                		ImageIcon resizedIcon = new ImageIcon(resizedImage);
                		achievementIcon.setIcon(resizedIcon);
                        achievementIcon.setHorizontalAlignment(SwingConstants.CENTER); 
                        achievementIcon.setVerticalAlignment(SwingConstants.CENTER);
            		}

                }
  
	            revalidate(); 
            }
        });
		
		manuallyResize();
		
	}
	
	
	
	
	
	
	/**
	 * Calls manually the resize component listener to reorganize displayed panels upon resizing.
	 */
	public void manuallyResize()
	{
		for (ComponentListener componentListener : getComponentListeners())
		{
			componentListener.componentResized(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
		}
	}
	
	
	private class ImageFileTransferHandler extends TransferHandler {

	    @Override
	    public boolean canImport(TransferHandler.TransferSupport support) 
	    {
	        if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) 
	        {
	            return false;
	        }
	        return true;
	    }

	    @Override
	    public boolean importData(TransferHandler.TransferSupport support) 
	    {
	        if (!canImport(support)) 
	        {
	            return false;
	        }

	        try 
	        {
	            Transferable transferable = support.getTransferable();
	            List<File> droppedFiles = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

	            if (droppedFiles.size() > 0) 
	            {
	                File file = droppedFiles.get(0); 

	                // Check if the file is a PNG image
	                if (file.getName().toLowerCase().endsWith(".png")) 
	                {
	                    // Read the image and set it as an ImageIcon on the NonOpaqueJLabel
	                    BufferedImage img = ImageIO.read(file);
	                    if (img != null) 
	                    {
	                        imageFile = file;
	                    	icon = new ImageIcon(img);
	                        achievementIcon.setIcon(icon);
	                        achievementIcon.setText(null); 
	                        manuallyResize();
	                        return true;
	                    }
	                } 
	                else 
	                {
	                    JOptionPane.showMessageDialog(null, "Please drop a valid PNG file.");
	                }
	            }
	        } 
	        catch (Exception e) 
	        {
	            e.printStackTrace();
	        }

	        return false;
	    }
	}
	
}


