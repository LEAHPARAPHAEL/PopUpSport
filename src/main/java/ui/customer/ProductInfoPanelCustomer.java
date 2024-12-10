package ui.customer;
import java.awt.Color;
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
import java.awt.event.ComponentListener;

import javax.swing.ImageIcon;
import ui.tools.*;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import backend.Product;
import ui.GUI;
import ui.tools.DescendantPanel;
import ui.tools.PanelID;
import ui.tools.Refreshable;

/**
 * The panel used by the customer to look at the detailed properties of a product.
 */
public class ProductInfoPanelCustomer extends DescendantPanel implements Refreshable
{
	private Product product;
	private int quantity;
	private double unitPrice;
	private NonOpaqueJLabel stock;
	private NonOpaqueJLabel amount;
	private NonOpaqueJLabel price;
	private NonOpaqueJLabel productIcon;
	
	public ProductInfoPanelCustomer(GUI gui)
	{
		super(new GridBagLayout(), gui);
		setOpaque(false);
	}
	
	/**
	 * Sets the current product to be the one passed as an argument.
	 * @param product The product whose properties we want to display.
	 */
	public void setProduct(Product product)
	{
		this.quantity = 1;
		this.product = product;
		this.unitPrice = product.getPrice();
		removeAll();
		displayProductInfos();
	}
	
	/**
	 * Organizes the layout of the panel to display all the properties of the product.
	 */
	public void displayProductInfos()
	{
		//Sets the layout of this panel
		
		RoundedButton back = new RoundedButton("Back to catalog");
		back.setBackground(GUI.RED);
		GridBagConstraints gbc = new GridBagConstraints();
		back.setPreferredSize(new Dimension(0,0));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(back, gbc);
		
		//Button that switches back to the catalog panel.
		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AlternatingCatalogPanel cardPanel = (AlternatingCatalogPanel)SwingUtilities.getAncestorOfClass(AlternatingCatalogPanel.class, back);
				cardPanel.alternatePanels(PanelID.CATALOG);
			}
			
		});
		
		NonOpaqueJLabel blank1 = new NonOpaqueJLabel();
		blank1.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		add(blank1, gbc);
		
		//Label in which the picture of the product is displayed.
		this.productIcon = new NonOpaqueJLabel();
		productIcon.setForeground(Color.red);
		productIcon.setHorizontalTextPosition(SwingConstants.CENTER);
		productIcon.setVerticalTextPosition(SwingConstants.CENTER);
		productIcon.setFont(new Font("Serif", Font.BOLD, 24));
		productIcon.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridheight = 4;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(productIcon, gbc);
		
		NonOpaqueJLabel name = new NonOpaqueJLabel(product.getName());
		name.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(name, gbc);
		
		NonOpaqueJLabel brand = new NonOpaqueJLabel(product.getBrand());
		brand.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(brand, gbc);
		
		NonOpaqueTextArea description = new NonOpaqueTextArea();
		description.setBackground(brand.getBackground());
		description.setPreferredSize(new Dimension(0,0));
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.setEditable(false);
		description.setText("Description :\n"+ product.getDescription());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.weightx = 0.5;
		gbc.weighty = 0.2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(description, gbc);
		
		this.stock = new NonOpaqueJLabel(product.getStock()==0 ? "Product out of stock." : String.format("In stock : %d.", product.getStock()), SwingUtilities.CENTER);
		stock.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(stock, gbc);
		
		RoundedButton minus = new RoundedButton("-");
		minus.setBackground(GUI.ORANGE);
		minus.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 0.1;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(minus, gbc);
		
		this.amount = new NonOpaqueJLabel("1", SwingUtilities.CENTER);
		amount.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.weightx = 0.3;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(amount, gbc);
		
		RoundedButton plus = new RoundedButton("+");
		plus.setBackground(GUI.ORANGE);
		plus.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 2;
		gbc.weightx = 0.1;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(plus, gbc);
		
		
		this.price = new NonOpaqueJLabel(String.format("<html>Total price : %.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span>.</html>", unitPrice), SwingUtilities.CENTER);
		price.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.weightx = 0.5;
		gbc.weighty = 0.2;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(price, gbc);
		
		RoundedButton addToBasket = new RoundedButton("Add to basket");
		addToBasket.setBackground(GUI.BLUE);
		addToBasket.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(addToBasket, gbc);
		
		NonOpaqueTextArea added = new NonOpaqueTextArea();
		added.setEditable(false);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.weightx = 0.5;
		gbc.weighty = 0.4;
		gbc.gridheight = 3;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(added, gbc);
		
		updateInfos();
		
		//Add one product to the selected amount
		plus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (quantity < product.getStock())
				{
					quantity++;
					updateInfos();
				}
			}
		});
		
		//Removes one product from the selected amount
		minus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (quantity > 1)
				{
					quantity--;
					updateInfos();
				}
			}
		});
		
		//Add the selected amount to the customer's basket if the customer doesn't have an ongoing reservation.
		addToBasket.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().hasOngoingReservation())
				{
					int basketFillSucceeded = gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getBasketManager().addProducts(product.getId(), quantity);
					added.setText(basketFillSucceeded >= 0 ? String.format("%d product(s) have been added to your basket.", quantity) : String.format("%d product(s) have been added. %d product(s) could not be added", quantity+basketFillSucceeded, -basketFillSucceeded));
				}
				else
					added.setText("You have an ongoing transaction to complete. To perform any modification on your basket, please cancel the current transaction in your basket menu.");

			}
		});
		
		
	    for (ComponentListener listener : getComponentListeners()) {
	        removeComponentListener(listener);
	    }
		
	    //Resizes the picture of the product when its container is resized.
		addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            	int panelWidth = getWidth();
                int panelHeight = getHeight(); 
                int imageSize = Math.min(panelWidth, panelHeight)/2;
                if (panelWidth > 0 && panelHeight > 0) {
            		Image productImage = product.getIcon().getImage();
            		Image resizedImage = productImage.getScaledInstance(imageSize - 20, imageSize - 20, Image.SCALE_SMOOTH);
            		ImageIcon resizedIcon = new ImageIcon(resizedImage);
            		productIcon.setIcon(resizedIcon);
                    productIcon.setHorizontalAlignment(SwingConstants.CENTER); 
                    productIcon.setVerticalAlignment(SwingConstants.CENTER);
                }
                
                
                
	            revalidate(); 
            }
        });
		
		for (ComponentListener componentListener : getComponentListeners())
		{
			componentListener.componentResized(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
		}
        
	}
	
	/**
	 * Display the up to date properties of the product.
	 */
	public void updateInfos()
	{
		amount.setText(String.format("%d", quantity));
		if (product.getDiscount() > 0)
		{
			productIcon.setText(String.format("-%d%%", (int)(product.getDiscount()*100)));
			price.setText(String.format("<html>Total price :&nbsp<s>%.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span></s> &nbsp<span style='color:red;'>%.2f <span style='font-weight:bold; font-size:10px;'>\u2359</span></span></html>", unitPrice*quantity, quantity*unitPrice*(1 - product.getDiscount())));
		}
		else
		{
			productIcon.setText("");
			price.setText(String.format("<html>Total price : %.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span>.</html>", quantity * unitPrice));
		}
	}

	@Override
	public void refresh() {
		if (product != null)
		{
			stock.setText(product.getStock()==0 ? "Product out of stock." : String.format("In stock : %d.", product.getStock()));
			quantity = Math.min(quantity, product.getStock());
			updateInfos();
		}
	}
	
	
}
