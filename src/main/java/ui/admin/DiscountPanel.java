package ui.admin;

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
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import ui.GUI;
import ui.tools.*;

/**
 * The panel used by the administrator to start a discount on a collection of products.
 */
public class DiscountPanel extends DescendantPanel
{
	public DiscountPanel(GUI gui)
	{
		super(new GridBagLayout(), gui);
		setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.7;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 3;
		add(new NonOpaqueJLabel(), gbc);
		
		//Returns to the product management panel.
		RoundedButton back = new RoundedButton("Back to products");
		back.setBackground(GUI.RED);
		back.setPreferredSize(new Dimension(0,0));
		back.setMargin(new Insets(0,0,0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(back, gbc);
	
		
		
		NonOpaqueJLabel discountInfo = new NonOpaqueJLabel("Start a discount on selected products", SwingConstants.CENTER);
		discountInfo.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 4;
		gbc.insets = new Insets(5,5,5,5);
		add(discountInfo, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.3;
		gbc.weighty = 0.65;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridheight = 6;
		add(new NonOpaqueJLabel(), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 9;
		gbc.weightx = 1;
		gbc.weighty = 0.05;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 4;
		add(new NonOpaqueJLabel(), gbc);
	
		
		//An array containing all the colors available for the admin to filter the products.
		ColorInfo[] colors = new ColorInfo[] {
				new ColorInfo(Color.red, "red"),
				new ColorInfo(Color.pink, "pink"),
				new ColorInfo(Color.orange, "orange"),
				new ColorInfo(Color.yellow, "yellow"),
				new ColorInfo(Color.green, "green"),
				new ColorInfo(Color.cyan, "cyan"), 
				new ColorInfo(Color.blue, "blue"),
				new ColorInfo(Color.magenta, "magenta"), 
				new ColorInfo(Color.lightGray, "lightGray"), 
				new ColorInfo(Color.gray, "gray"), 
				new ColorInfo(Color.black, "black"),
				new ColorInfo(Color.white, "white"),
		};
		
		//A map containing as entries the identifiers of the colors and as values their names.
		Map<Integer, String> activeColors = new HashMap<>();
		
		NonOpaquePanel colorPanel = new NonOpaquePanel(new GridBagLayout());
		
		int count = 0;
		
		//For each color, a small square button is created that can either be selected or unselected.
		for (ColorInfo color : colors)
		{
			RoundedButton colorButton = new RoundedButton(10);
            colorButton.setMargin(new Insets(0, 0, 0, 0));
            colorButton.setFont(new Font("Serif", Font.BOLD, 24));
            colorButton.setHorizontalTextPosition(SwingConstants.CENTER);
            if (color.getColor().getRed()<100 && color.getColor().getGreen()<100)
            {
            	colorButton.setForeground(Color.white);
            }
            colorButton.setBackground(color.getColor());
			gbc = new GridBagConstraints();
			gbc.gridx = count/2;
	        gbc.gridy = count%2;
			colorPanel.add(colorButton, gbc);
			count++;
			colorButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) 
				{
					if (colorButton.getText().equals("\u2713"))
					{
						colorButton.setText("");
						activeColors.remove(color.getColorID());
					}
					else
					{
						colorButton.setText("\u2713");
						activeColors.put(color.getColorID(), color.getColorName());
					}
				}
				
			});
		}

		
		colorPanel.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.4;
        gbc.weighty = 0.15;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(colorPanel, gbc);
        
		gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        gbc.weighty = 0.15;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(new NonOpaqueJLabel(), gbc);
        
		//When the panel is resized, every color button is resized accordingly.
        colorPanel.addComponentListener(new ComponentAdapter() 
        {
            @Override
            public void componentResized(ComponentEvent e) 
            { 
            	int width = colorPanel.getWidth();
            	int height = colorPanel.getHeight();
            	int buttonSize = Math.min(2*width/colors.length, height/2);
            	for (Component comp : colorPanel.getComponents())
            	{
            		comp.setPreferredSize(new Dimension(buttonSize, buttonSize));
            		comp.revalidate();
            	}
            	colorPanel.revalidate();
            	colorPanel.repaint();
            	
            }
        });
		
		NonOpaqueJLabel priceMin = new NonOpaqueJLabel("Minimum price");
		priceMin.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.weightx = 0.2;
		gbc.weighty = 0.1;
		gbc.insets = new Insets(5,5,5,5);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(priceMin, gbc);
		
		PlaceholderTextField priceMinField = new PlaceholderTextField("0");
		priceMinField.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.weightx = 0.2;
		gbc.weighty = 0.1;
		gbc.insets = new Insets(5,5,5,5);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(priceMinField, gbc);
		
        NonOpaqueTextArea priceMinError = new NonOpaqueTextArea();
        priceMinError.setLineWrap(true);
        priceMinError.setWrapStyleWord(true);
        priceMinError.setEditable(false);
        priceMinError.setBackground(priceMin.getBackground());
        priceMinError.setForeground(Color.RED);
        priceMinError.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        gbc.weighty = 0.1;
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        add(priceMinError, gbc);
		
		NonOpaqueJLabel priceMax = new NonOpaqueJLabel("Maximum price");
		priceMax.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.weightx = 0.2;
		gbc.weighty = 0.1;
		gbc.insets = new Insets(5,5,5,5);
		gbc.fill = GridBagConstraints.BOTH;
		add(priceMax, gbc);
		
		PlaceholderTextField priceMaxField = new PlaceholderTextField("20000");
		priceMaxField.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 4;
		gbc.weightx = 0.2;
		gbc.weighty = 0.1;
		gbc.insets = new Insets(5,5,5,5);
		gbc.fill = GridBagConstraints.BOTH;
		add(priceMaxField, gbc);
		
        NonOpaqueTextArea priceMaxError = new NonOpaqueTextArea();
        priceMaxError.setLineWrap(true);
        priceMaxError.setWrapStyleWord(true);
        priceMaxError.setEditable(false);
        priceMaxError.setBackground(priceMin.getBackground());
        priceMaxError.setForeground(Color.RED);
        priceMaxError.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        gbc.weighty = 0.1;
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        add(priceMaxError, gbc);
		
		NonOpaqueJLabel brand = new NonOpaqueJLabel("Targeted brands");
		brand.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.weightx = 0.2;
		gbc.weighty = 0.1;
		gbc.insets = new Insets(5,5,5,5);
		gbc.fill = GridBagConstraints.BOTH;
		add(brand, gbc);
		
		JTextField brandField = new JTextField("");
		brandField.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 5;
		gbc.weightx = 0.2;
		gbc.weighty = 0.1;
		gbc.insets = new Insets(5,5,5,5);
		gbc.fill = GridBagConstraints.BOTH;
		add(brandField, gbc);

        NonOpaqueTextArea brandError = new NonOpaqueTextArea();
        brandError.setLineWrap(true);
        brandError.setWrapStyleWord(true);
        brandError.setEditable(false);
        brandError.setBackground(priceMin.getBackground());
        brandError.setForeground(Color.RED);
        brandError.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        gbc.weighty = 0.1;
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        add(brandError, gbc);
		
		NonOpaqueJLabel discount = new NonOpaqueJLabel("Discount amount");
		discount.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.weightx = 0.2;
		gbc.weighty = 0.1;
		gbc.insets = new Insets(5,5,5,5);
		gbc.fill = GridBagConstraints.BOTH;
		add(discount, gbc);
		
		PlaceholderTextField discountField = new PlaceholderTextField("0");
		discountField.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 6;
		gbc.weightx = 0.2;
		gbc.weighty = 0.1;
		gbc.insets = new Insets(5,5,5,5);
		gbc.fill = GridBagConstraints.BOTH;
		add(discountField, gbc);
		
        NonOpaqueTextArea discountError = new NonOpaqueTextArea();
        discountError.setEditable(false);
        discountError.setForeground(Color.RED);
        gbc = new GridBagConstraints();
		gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        gbc.weighty = 0.1;
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        add(discountError, gbc);
		
		RoundedButton startDiscount = new RoundedButton("Launch discount");
		startDiscount.setBackground(GUI.GREEN);
		startDiscount.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.weightx = 0.4;
		gbc.weighty = 0.1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(5,5,5,5);
		gbc.fill = GridBagConstraints.BOTH;
		add(startDiscount, gbc);
		
		Runnable onTimeUp = () -> {
			MainAdminPanel mainPanel = (MainAdminPanel)SwingUtilities.getAncestorOfClass(MainAdminPanel.class, startDiscount);
			mainPanel.showInfoPanelForTheNext(10, "The discount has been applied to the selected products !");
		};
		
		LoadingBar loadingBar = new LoadingBar(onTimeUp);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.weightx = 1;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 4;
		gbc.insets = new Insets(5,5,5,5);
		add(loadingBar, gbc);
		
		back.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (loadingBar.isVisible())
				{
					loadingBar.stop();
				}
				MainAdminPanel mainPanel = (MainAdminPanel)SwingUtilities.getAncestorOfClass(MainAdminPanel.class, back);
				mainPanel.switchPanel(PanelID.PRODUCT_MANAGEMENT);
			}
			
		});
		
		
		
		Border defaultBorder = discountField.getBorder();
		
		startDiscount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (loadingBar.isVisible())
				{
					return;
				}
				//Resets the borders of the error boxes.
				priceMinField.setBorder(defaultBorder);
				priceMaxField.setBorder(defaultBorder);
				brandField.setBorder(defaultBorder);
				discountField.setBorder(defaultBorder);
				priceMinError.setText("");
				priceMaxError.setText("");
				discountError.setText("");
				brandError.setText("");
				
				double priceMinValue = 0;
				double priceMaxValue = 0;
				double discountValue = 0;
				String[] targetedBrands = brandField.getText().split("[,; |]+");
				
				
				boolean isValid = true;
				//Tries to convert the content of certain fields into numerical values and display errors if it fails.
				try
				{
					priceMinValue = Double.parseDouble(priceMinField.getTextAnyway().replace(",", "."));
				}
				catch (NumberFormatException nfe)
				{
					priceMinError.setBorder(new LineBorder(Color.red));
					priceMinError.setText("Please enter a valid minimum price for the new product.");
					isValid = false;
				}
				try
				{
					priceMaxValue = Double.parseDouble(priceMaxField.getTextAnyway().replace(",", "."));
				}
				catch (NumberFormatException nfe)
				{
					priceMaxError.setBorder(new LineBorder(Color.red));
					priceMaxError.setText("Please enter a valid maximum price for the new product.");
					isValid = false;
				}
				try
				{
					discountValue = Double.parseDouble(discountField.getTextAnyway().replace(",", "."));
					if (discountValue < 0 || discountValue >= 100)
					{
						discountError.setBorder(new LineBorder(Color.red));
						discountError.setText("Please enter a valid discount.");
						isValid = false;
					}
					//Percentage converted into 0 to 1 value
					if (discountValue >= 1)
						discountValue = discountValue / 100;
				}
				catch (NumberFormatException nfe)
				{
					discountError.setBorder(new LineBorder(Color.red));
					discountError.setText("Please enter a valid discount for the new product.");
					isValid = false;
				}
				
				if (!isValid)
				{
					return;
				}
				
				//Starts the discount on the selected products.
				loadingBar.load();
				gui.getDatabaseManager().launchGlobalDiscount(activeColors, priceMinValue, priceMaxValue, discountValue, targetedBrands);
			}
			
		});
		
		
		
			
	}
}
