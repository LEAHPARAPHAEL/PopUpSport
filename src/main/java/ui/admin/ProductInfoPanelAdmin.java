package ui.admin;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.LineBorder;

import backend.Product;
import ui.GUI;
import ui.tools.*;

/**
 * The panel used by the administrator to modify the properties of a product.
 */
public class ProductInfoPanelAdmin extends DescendantPanel implements Refreshable
{
	private Product product;
	private ImageIcon icon;
	private File imageFile;
	private NonOpaqueJLabel productIcon;
	
	private double price;
	private int stock;
	private double discount;
	private boolean available;
	private boolean updateSuccessful;
	private boolean isUpdating;
	
	public ProductInfoPanelAdmin(GUI gui) 
	{
		super(new GridBagLayout(),gui);
		setOpaque(false);
	}

	public void setProduct(Product product)
	{
		this.product = product;
		this.icon = product.getIcon();
		removeAll();
		displayProductInfos();
	}
	
	/**
	 * Display the editable properties of the products.
	 */
	public void displayProductInfos()
	{
		RoundedButton back = new RoundedButton("Back to catalog");
		back.setBackground(GUI.RED);
		GridBagConstraints gbc = new GridBagConstraints();
		back.setPreferredSize(new Dimension(0,0));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(back, gbc);
		
		
		NonOpaqueJLabel colorInfo = new NonOpaqueJLabel("Choose one or several colors", SwingUtilities.CENTER);
		colorInfo.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(colorInfo, gbc);
		
		//The label containing the picture representing the product, in which a file can be dropped to change this image.
		this.productIcon = new NonOpaqueJLabel();
		productIcon.setPreferredSize(new Dimension(0,0));
		productIcon.setForeground(Color.red);
		productIcon.setHorizontalTextPosition(SwingConstants.CENTER);
		productIcon.setVerticalTextPosition(SwingConstants.CENTER);
		productIcon.setFont(new Font("Serif", Font.BOLD, 24));
		productIcon.setTransferHandler(new ImageFileTransferHandler());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridheight = 6;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(productIcon, gbc);
		
		
		PlaceholderTextField stockField = new PlaceholderTextField(String.format("%d",product.getStock()));
		stockField.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 3;
		gbc.weightx = 0.25;
		gbc.weighty = 0.075;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(stockField, gbc);
		
		
		NonOpaqueTextArea name = new NonOpaqueTextArea();
		name.setEditable(true);
		name.setBorder(GUI.GRAY_BORDER);
		name.setText(product.getName());
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.weightx = 0.35;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(name, gbc);
		
		NonOpaqueJLabel nameLabel = new NonOpaqueJLabel("Name", SwingUtilities.CENTER);
		nameLabel.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.weightx = 0.15;
		gbc.weighty = 0.1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(nameLabel, gbc);
		
		NonOpaqueTextArea brand = new NonOpaqueTextArea();
		brand.setEditable(true);
		brand.setText(product.getBrand());
		brand.setPreferredSize(new Dimension(0,0));
		brand.setBorder(GUI.GRAY_BORDER);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 8;
		gbc.weightx = 0.35;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(brand, gbc);
		
		NonOpaqueJLabel brandLabel = new NonOpaqueJLabel("Brand", SwingUtilities.CENTER);
		brandLabel.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.weightx = 0.15;
		gbc.weighty = 0.1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(brandLabel, gbc);
		
		NonOpaqueTextArea description = new NonOpaqueTextArea();
		description.setEditable(true);
		description.setText(product.getDescription());
		description.setBorder(GUI.GRAY_BORDER);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 9;
		gbc.weightx = 0.35;
		gbc.weighty = 0.2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(description, gbc);
		
		NonOpaqueJLabel descriptionLabel = new NonOpaqueJLabel("Description", SwingUtilities.CENTER);
		descriptionLabel.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 9;
		gbc.weightx = 0.15;
		gbc.weighty = 0.1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(descriptionLabel, gbc);
		
		
		NonOpaqueJLabel stockLabel = new NonOpaqueJLabel("Current stock", SwingUtilities.CENTER);
		stockLabel.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.weightx = 0.25;
		gbc.weighty = 0.075;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(stockLabel, gbc);
		
		
		NonOpaqueJLabel priceLabel = new NonOpaqueJLabel("<html>Unit price (<span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span>)</html>", SwingUtilities.CENTER);
		priceLabel.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.weightx = 0.25;
		gbc.weighty = 0.075;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(priceLabel, gbc);
		
		PlaceholderTextField priceField = new PlaceholderTextField(String.format("%.2f", product.getPrice()));
		priceField.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 2;
		gbc.weightx = 0.25;
		gbc.weighty = 0.075;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(priceField, gbc);
		
		NonOpaqueJLabel discountLabel = new NonOpaqueJLabel("Discount", SwingUtilities.CENTER);
		discountLabel.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 5;
		gbc.weightx = 0.25;
		gbc.weighty = 0.075;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(discountLabel, gbc);
		
		PlaceholderTextField discountField = new PlaceholderTextField(String.format("%d", (int)(product.getDiscount()*100)));
		discountField.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 5;
		gbc.weightx = 0.25;
		gbc.weighty = 0.075;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(discountField, gbc);
		
		
		RoundedButton removeProduct = new RoundedButton("Remove product");
		removeProduct.setBackground(GUI.RED);
		removeProduct.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 6;
		gbc.weightx = 0.25;
		gbc.weighty = 0.075;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(removeProduct, gbc);
		
		RoundedButton applyChanges = new RoundedButton("Apply changes");
		applyChanges.setBackground(GUI.GREEN);
		applyChanges.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 6;
		gbc.weightx = 0.25;
		gbc.weighty = 0.075;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(applyChanges, gbc);
		
		NonOpaqueTextArea infos = new NonOpaqueTextArea();
		infos.setEditable(false);
		infos.setForeground(Color.red);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 8;
		gbc.weightx = 0.5;
		gbc.weighty = 0.3;
		gbc.gridheight = 2;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(infos, gbc);
		
		
		Runnable onTimeUp = () -> {
			MainAdminPanel mainPanel = (MainAdminPanel)SwingUtilities.getAncestorOfClass(MainAdminPanel.class, infos);
			if (isUpdating)
			{
				if (updateSuccessful)
				{
					product.setName(name.getText());
					product.setBrand(brand.getText());
					product.setDescription(description.getText());
					product.setPrice(price);
					product.setStock(stock);
					product.setAvailable(available);
					product.setDiscount(discount);
					if (imageFile != null)
					{
						product.setIcon(icon);
					}
					
					mainPanel.showInfoPanelForTheNext(10, "The properties of the product have been successfully modified.");
					infos.setText("");
				}
				//Otherwise, the properties couldn't be modified because the product is reserved by a customer.
				else
				{
					mainPanel.showInfoPanelForTheNext(10, "The properties of this product cannot be modified because it has been reserved by a customer.");
				}
			}
			
			
			//Remove the product
			else 
			{
				//The product has been successfully removed from the database.
				if (updateSuccessful)
				{
					mainPanel.showInfoPanelForTheNext(10,"The product has been removed from the database.");
				}
				//The product couldn't be removed from the database because it has been reserved or ordered by a customer.
				else
				{
					mainPanel.showInfoPanelForTheNext(10,"The product couldn't be removed and has been set to unavailable.");
				}
				
				//Refreshes the list of products with the selected filters after this modification.
				ProductManagementPanel productManagementPanel = (ProductManagementPanel)SwingUtilities.getAncestorOfClass(ProductManagementPanel.class, infos);
				productManagementPanel.propagateFilters();
				productManagementPanel.propagateCatalogAlternate(PanelID.CATALOG);
			}
		};
		
		LoadingBar loadingBar = new LoadingBar(onTimeUp);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 7;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(loadingBar, gbc);
		
		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (loadingBar.isVisible())
				{
					loadingBar.stop();
				}
				AlternatingProductPanel cardPanel = (AlternatingProductPanel)SwingUtilities.getAncestorOfClass(AlternatingProductPanel.class, back);
				cardPanel.alternatePanels(PanelID.CATALOG);
			}
			
		});
		
		
		//A group of radio buttons that enables the admin to set the product as available or unavailable
		NonOpaqueRadio availableRadio = new NonOpaqueRadio("Available");
		availableRadio.setPreferredSize(new Dimension(0,0));
		NonOpaqueRadio notAvailableRadio = new NonOpaqueRadio("Not available");
		notAvailableRadio.setPreferredSize(new Dimension(0,0));
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(availableRadio);
		buttonGroup.add(notAvailableRadio);
		
		if (product.isAvailable())
			availableRadio.setSelected(true);
		else
			notAvailableRadio.setSelected(true);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 4;
		gbc.weightx = 0.25;
		gbc.weighty = 0.075;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		add(availableRadio, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 4;
		gbc.weightx = 0.25;
		gbc.weighty = 0.075;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		add(notAvailableRadio, gbc);
		
		//A list of all colors available for the product to be described as.
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
		
		//The panel containing all the color buttons.
		NonOpaquePanel colorPanel = new NonOpaquePanel(new GridBagLayout());
		
		//A map containing as entries the identifiers of the selected colors and as values their names.
		Map<Integer, String> activeColors = new HashMap<>();
		
		//Splits the single string representing the current colors of the products into an array of strings.
		String[] productColorsArray = product.getColors().split("[,; |]+");
		
		//Converts this array of colors into a list
		List<String> productColorsList = new ArrayList<>();
		
		for (String color : productColorsArray)
		{
			productColorsList.add(color);
		}
		
		int count = 0;
		for (ColorInfo color : colors)
		{
			RoundedButton colorButton = new RoundedButton(10);
            colorButton.setMargin(new Insets(0, 0, 0, 0));
            colorButton.setFont(new Font("Serif", Font.BOLD, 24));
            colorButton.setHorizontalTextPosition(SwingConstants.CENTER);
            colorButton.setBackground(color.getColor());
            
            //If the color is dark enough, the selection symbol should be white to be visible.
            if (color.getColor().getRed()<100 && color.getColor().getGreen()<100)
            {
            	colorButton.setForeground(Color.white);
            }
			gbc = new GridBagConstraints();
			gbc.gridx = count/2;
	        gbc.gridy = count%2;
			colorPanel.add(colorButton, gbc);
			
			//If the color belongs to one of the current colors of the products, put it as selected.
			if (productColorsList.contains(color.getColorName()))
			{
				colorButton.setText("\u2713");
				activeColors.put(color.getColorID(), color.getColorName());
			}
			count++;
			
			//Enables the color button to be selected and unselected.
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
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.125;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(colorPanel, gbc);
		
		
		
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
        
		
		
		
		
		
		
		
		
		
        //Removes the product from the database or sets it as unavailable.
		removeProduct.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (loadingBar.isVisible())
				{
					return;
				}
				isUpdating = false;
				loadingBar.load();
				updateSuccessful = gui.getDatabaseManager().deleteProduct(product.getId());
			}
		});
		
		//Modifies the properties of the product.
		applyChanges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (loadingBar.isVisible())
				{
					return;
				}
				
				MainAdminPanel mainPanel = (MainAdminPanel)SwingUtilities.getAncestorOfClass(MainAdminPanel.class, infos);
				
				//Check the value of every field to see if it is non empty or in the right format.
				if (name.getText().equals(""))
				{
					mainPanel.showInfoPanelForTheNext(5, "The field corresponding to the name of the product cannot be empty.");
					return;
				}
				if (brand.getText().equals(""))
				{
					mainPanel.showInfoPanelForTheNext(5,"The field corresponding to the brand of the product cannot be empty." );
					return;
				}
				if (description.getText().equals(""))
				{
					mainPanel.showInfoPanelForTheNext(5, "The field corresponding to the description of the product cannot be empty.");
					return;
				}
				try
				{
					price = Double.parseDouble(priceField.getTextAnyway().replace(",", "."));
				}
				catch (NumberFormatException nfe)
				{
					mainPanel.showInfoPanelForTheNext(5, "Please enter a valid price for the new product.");
					return;
				}
				try
				{
					stock = Integer.parseInt(stockField.getTextAnyway());
				}
				catch (NumberFormatException nfe)
				{
					mainPanel.showInfoPanelForTheNext(5, "Please enter a valid initial stock for the new product.");
					return;
				}
				try
				{
					discount = Double.parseDouble(discountField.getTextAnyway().replace(",", "."));
					if (discount < 0 || discount >= 100)
					{
						mainPanel.showInfoPanelForTheNext(5, "Please enter a valid discount.");
	
						return;
					}
					//Percentage converted into 0 to 1 value
					if (discount >= 1)
						discount = discount / 100;
				}
				catch (NumberFormatException nfe)
				{
					mainPanel.showInfoPanelForTheNext(5, "Please enter a valid discount.");
					return;
				}
				
				//Creates a single String to represent all the selected colors for the product.
				StringBuilder productColors = new StringBuilder();
				for (String color : activeColors.values())
				{
					productColors.append(color + " ");
				}
				product.setColors(productColors.toString());
				available = availableRadio.isSelected();

				//If the properties of the product have been successfully modified.
				isUpdating = true;
				loadingBar.load();
				updateSuccessful = gui.getDatabaseManager().updateOneProduct(product.getId(), name.getText(),description.getText(),brand.getText(), price, stock, available, productColors.toString(), imageFile, discount);

			
				

			}
		});
		
	    for (ComponentListener listener : getComponentListeners()) {
	        removeComponentListener(listener);
	    }
		
	    //Resizes the image of the product when its container is resized.
		addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            	int panelWidth = getWidth();
                int panelHeight = getHeight();
                int imageSize = Math.min(panelWidth, panelHeight)/2;
                if (imageSize > 0) 
                {
            		Image productImage = icon.getImage();
            		Image resizedImage = productImage.getScaledInstance(imageSize - 10, imageSize - 10, Image.SCALE_SMOOTH);
            		ImageIcon resizedIcon = new ImageIcon(resizedImage);
            		productIcon.setIcon(resizedIcon);
                    productIcon.setHorizontalAlignment(SwingConstants.CENTER); 
                    productIcon.setVerticalAlignment(SwingConstants.CENTER);
                }
  
	            revalidate(); 
            }
        });
		
		manuallyResize();
        
	}

	/**
	 * Manually calls the resize component listener to reorganize displayed panels.
	 */
	public void manuallyResize()
	{
		for (ComponentListener componentListener : getComponentListeners())
		{
			componentListener.componentResized(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
		}
	}
	
	
	@Override
	public void refresh() 
	{
		
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
                            productIcon.setIcon(icon);
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
