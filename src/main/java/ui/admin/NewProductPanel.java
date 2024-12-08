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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.LineBorder;

import backend.Product;
import ui.GUI;
import ui.tools.*;

/**
 * The panel used by the administrator to create a new product and add it to the database.
 */
public class NewProductPanel extends DescendantPanel
{
	private ImageIcon icon;
	private File imageFile;
	private JLabel productIcon;
	private int productID;
	private double price;
	private int stock;
	private double discount;
	private StringBuilder productColors;
	
	public NewProductPanel(GUI gui)
	{
		super(new GridBagLayout(), gui);
		setOpaque(false);
		//A button that switches back to the catalog panel.
		RoundedButton back = new RoundedButton("Back to catalog");
		back.setBackground(GUI.RED);
		GridBagConstraints gbc = new GridBagConstraints();
		back.setPreferredSize(new Dimension(0,0));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 2;
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
		
		//The place where the image of the product is displayed and where a file can be dropped to change it.
		this.productIcon = new JLabel("Drop an image", SwingUtilities.CENTER);
		productIcon.setPreferredSize(new Dimension(0,0));
		productIcon.setBorder(new LineBorder(Color.LIGHT_GRAY));
		productIcon.setBackground(GUI.PRODUCT_ADMIN);
		
		//Adds a drag and drop property to this zone.
		productIcon.setTransferHandler(new ImageFileTransferHandler());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridheight = 5;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(productIcon, gbc);
		
		JTextField stockField = new JTextField();
		stockField.setBorder(new LineBorder(Color.LIGHT_GRAY));
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
		name.setBackground(stockField.getBackground());
		name.setLineWrap(true);
		name.setWrapStyleWord(true);
		name.setEditable(true);
		name.setPreferredSize(new Dimension(0,0));
		name.setBorder(new LineBorder(Color.LIGHT_GRAY));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.weightx = 0.35;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(name, gbc);
		
		NonOpaqueJLabel nameLabel = new NonOpaqueJLabel("Name", SwingUtilities.CENTER);
		nameLabel.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.weightx = 0.15;
		gbc.weighty = 0.1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(nameLabel, gbc);
		
		NonOpaqueTextArea brand = new NonOpaqueTextArea();
		brand.setEditable(true);
		brand.setBorder(new LineBorder(Color.LIGHT_GRAY));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.weightx = 0.35;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(brand, gbc);
		
		NonOpaqueJLabel brandLabel = new NonOpaqueJLabel("Brand", SwingUtilities.CENTER);
		brandLabel.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.weightx = 0.15;
		gbc.weighty = 0.1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(brandLabel, gbc);
		
		NonOpaqueTextArea description = new NonOpaqueTextArea();
		description.setEditable(true);
		description.setBorder(new LineBorder(Color.LIGHT_GRAY));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 8;
		gbc.weightx = 0.35;
		gbc.weighty = 0.2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(description, gbc);
		
		NonOpaqueJLabel descriptionLabel = new NonOpaqueJLabel("Description", SwingUtilities.CENTER);
		descriptionLabel.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.weightx = 0.15;
		gbc.weighty = 0.2;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(descriptionLabel, gbc);
		
		
		NonOpaqueJLabel stockLabel = new NonOpaqueJLabel("Initial stock", SwingUtilities.CENTER);
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
		
		JTextField priceField = new JTextField();
		priceField.setBorder(GUI.GRAY_BORDER);
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
		gbc.gridy = 4;
		gbc.weightx = 0.25;
		gbc.weighty = 0.075;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(discountLabel, gbc);
		
		PlaceholderTextField discountField = new PlaceholderTextField("0");
		discountField.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 4;
		gbc.weightx = 0.25;
		gbc.weighty = 0.075;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(discountField, gbc);
		
		//Removes the image representing the product.
		RoundedButton clearIcon = new RoundedButton("Remove image");
		clearIcon.setBackground(GUI.RED);
		clearIcon.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 5;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(clearIcon, gbc);
		
		RoundedButton createProduct = new RoundedButton("Create product");
		createProduct.setBackground(GUI.GREEN);
		createProduct.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 6;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(createProduct, gbc);
		
		
		Runnable onTimeUp = () -> {
			MainAdminPanel mainPanel = (MainAdminPanel)SwingUtilities.getAncestorOfClass(MainAdminPanel.class, createProduct);
			if (productID >= 0)
			{
				//Selects all products matching the current filter with the addition of the new product.
				ProductManagementPanel productManagementPanel = (ProductManagementPanel)SwingUtilities.getAncestorOfClass(ProductManagementPanel.class, createProduct);
				gui.getDatabaseManager().getProductsWithOrKeywords(productManagementPanel.getProductFilter());
				productManagementPanel.propagateFilters();
				mainPanel.showInfoPanelAndSwitch(10, "The new product has successfully been inserted into the database !", PanelID.PRODUCT_MANAGEMENT);
			}
			else
			{
				mainPanel.showInfoPanelForTheNext(10, "A problem has occurred when trying to insert the new product. Check that the image you have dropped has a low resolution.");
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
		
		NonOpaqueTextArea infos = new NonOpaqueTextArea();
		infos.setEditable(false);
		infos.setForeground(Color.red);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 8;
		gbc.weightx = 0.5;
		gbc.weighty = 0.2;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(infos, gbc);
		
		back.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (loadingBar.isVisible())
				{
					loadingBar.stop();
				}
				AlternatingProductPanel cardPanel = (AlternatingProductPanel)SwingUtilities.getAncestorOfClass(AlternatingProductPanel.class, back);
				cardPanel.alternatePanels(PanelID.CATALOG);
			}
		});
		
		
		//A list of all color available to select for the new product.
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
		
		//A map containing as entries the identifiers of the chosen colors and as values their names. 
		Map<Integer, String> activeColors = new HashMap<>();
		
		NonOpaquePanel colorPanel = new NonOpaquePanel(new GridBagLayout());
		
		int count = 0;
		//For every color, a small square button is created that can be selected or unselected.
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
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.175;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
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
		
		
		
		
		clearIcon.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				icon = null;
				productIcon.setIcon(icon);
				productIcon.setText("Drop an image");
				imageFile = null;
			}
			
		});
		
		createProduct.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (loadingBar.isVisible())
				{
					return;
				}
				MainAdminPanel mainPanel = (MainAdminPanel)SwingUtilities.getAncestorOfClass(MainAdminPanel.class, infos);
				
				//Checks all the fields to see if the values are authorized.
				if (name.getText().equals(""))
				{
					mainPanel.showInfoPanelForTheNext(5, "The field corresponding to the name of the product cannot be empty.");
					return;
				}
				if (brand.getText().equals(""))
				{
					mainPanel.showInfoPanelForTheNext(5, "The field corresponding to the brand of the product cannot be empty.");
					return;
				}
				if (description.getText().equals(""))
				{
					mainPanel.showInfoPanelForTheNext(5, "The field corresponding to the description of the product cannot be empty.");
					return;
				}
				if (icon == null)
				{
					mainPanel.showInfoPanelForTheNext(5, "The product must have an image representing it.");
					return;
				}
				try
				{
					price = Double.parseDouble(priceField.getText().replace(",", "."));
				}
				catch (NumberFormatException nfe)
				{
					mainPanel.showInfoPanelForTheNext(5, "Please enter a valid price for the new product.");
					return;
				}
				try
				{
					stock = Integer.parseInt(stockField.getText());
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
						infos.setText("Please enter a valid discount.");
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
				
				//Constructs a single string containing the names of every selected color.
				productColors = new StringBuilder();
				for (String color : activeColors.values())
				{
					productColors.append(color + " ");
				}

				//Gets the generated auto-increment primary key for the new product.
				loadingBar.load();
				productID = gui.getDatabaseManager().addToProductTable(name.getText(), description.getText(), brand.getText(), price, stock,  imageFile, productColors.toString(), discount);
				gui.getDatabaseManager().getProductManager().addProduct(productID, new Product(productID, name.getText(), description.getText(), brand.getText(), price, stock, icon, true, productColors.toString(), discount));
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
                		Image resizedImage = productImage.getScaledInstance(imageSize - 10, imageSize - 10, Image.SCALE_SMOOTH);
                		ImageIcon resizedIcon = new ImageIcon(resizedImage);
                		productIcon.setIcon(resizedIcon);
                        productIcon.setHorizontalAlignment(SwingConstants.CENTER); 
                        productIcon.setVerticalAlignment(SwingConstants.CENTER);
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
                            productIcon.setIcon(icon);
                            productIcon.setText(null); 
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
