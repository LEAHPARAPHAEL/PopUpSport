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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import backend.Product;
import ui.GUI;
import ui.tools.*;

/**
 * A panel containing information about a product present in the customer's basket.
 */
public class BasketItemPanel extends RoundedPanel implements Refreshable
{
	private Product product;
	private int quantity;
	private int inStock;
	private int productID;
	private double unitPrice;
	private double discount;
	private NonOpaqueTextArea availability;
	private NonOpaqueJLabel price;
	private NonOpaqueJLabel amount;
	private NonOpaqueJLabel stock;
	private NonOpaqueJLabel productIcon;
	private GUI gui;
	
	public BasketItemPanel(int productID, int quantity, GUI gui)
	{
		super(new GridBagLayout());
		this.gui = gui;
		this.productID = productID;
		
		//Gets the actual object containing all the properties of the product from its identifier.
		this.product = gui.getDatabaseManager().getProductManager().getProductById(productID);
		this.quantity = quantity;
		this.inStock = product.getStock();
		this.unitPrice = product.getPrice();
		this.discount = product.getDiscount();
		setBackground(GUI.PRODUCT_CUSTOMER);
		
		displayProduct();
		
	}
	
	/**
	 * Organizes the layout to display all the properties of the product.
	 */
	public void displayProduct()
	{
		//Gets the quantity of the product present in the customer's basket.
		this.quantity = gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getBasketManager().getQuantityById(product.getId());
		removeAll();
		
		//The label contining the picture of the product.
		this.productIcon = new NonOpaqueJLabel();
		productIcon.setPreferredSize(new Dimension(0,0));
		productIcon.setForeground(Color.red);
		productIcon.setHorizontalTextPosition(SwingConstants.CENTER);
		productIcon.setVerticalTextPosition(SwingConstants.CENTER);
		productIcon.setFont(new Font("Serif", Font.BOLD, 24));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 4;
		gbc.weightx = 0.5;
		gbc.weighty = 0.8;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(productIcon, gbc);
		
		NonOpaqueTextArea productName = new NonOpaqueTextArea(product.getName());
		productName.setEditable(false);
		productName.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.insets = new Insets(0,5,0,5);
		gbc.fill = GridBagConstraints.BOTH;
		add(productName, gbc);
		
		NonOpaqueJLabel productBrand = new NonOpaqueJLabel(product.getBrand());
		productBrand.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.insets = new Insets(0,5,0,5);
		gbc.fill = GridBagConstraints.BOTH;
		add(productBrand, gbc);
		
		
		this.stock = new NonOpaqueJLabel(inStock==0 ? "Product out of stock." : String.format("In stock : %d.", product.getStock()), SwingUtilities.CENTER);
		stock.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.2;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		add(stock, gbc);
		
		RoundedButton minus = new RoundedButton("-");
		minus.setBackground(GUI.ORANGE);
		minus.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 0.1;
		gbc.weighty = 0.2;
		gbc.fill = GridBagConstraints.BOTH;
		add(minus, gbc);
		
		this.amount = new NonOpaqueJLabel(String.format("%d", quantity), SwingUtilities.CENTER);
		amount.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.weightx = 0.3;
		gbc.weighty = 0.2;
		gbc.fill = GridBagConstraints.BOTH;
		add(amount, gbc);
		
		RoundedButton plus = new RoundedButton("+");
		plus.setBackground(GUI.ORANGE);
		plus.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.weightx = 0.1;
		gbc.weighty = 0.2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0,0,0,5);
		add(plus, gbc);
		
		this.price = new NonOpaqueJLabel(String.format("<html>Price : %.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span></html>", quantity*unitPrice), SwingUtilities.CENTER);
		price.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 0.5;
		gbc.weighty = 0.2;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		add(price, gbc);
		
		RoundedButton removeProduct = new RoundedButton("Remove product");
		removeProduct.setBackground(GUI.RED);
		removeProduct.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.weightx = 0.5;
		gbc.weighty = 0.2;
		gbc.gridwidth = 3;
		gbc.insets = new Insets(0,0,0,5);
		gbc.fill = GridBagConstraints.BOTH;
		add(removeProduct, gbc);
		
		this.availability = new NonOpaqueTextArea();
		availability.setEditable(false);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.weightx = 0.5;
		gbc.weighty = 0.2;
		gbc.gridwidth = 3;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		add(availability, gbc);
		
		updateInfos();
		
		//Adds one more instance of this product to the basket if possible.
		plus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (quantity < inStock)
				{
					quantity++;
					updateInfos();
					gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getBasketManager().updateStock(productID, quantity);
					BasketPanel basketPanel = (BasketPanel)SwingUtilities.getAncestorOfClass(BasketPanel.class, plus);
					basketPanel.notifyChanges();
				}
			}
		});
		
		//Removes an instance of this product from the basket if more than 1. Otherwise, the remove button should be used to remove the product completely.
		minus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (quantity > 1)
				{
					quantity--;
					updateInfos();
					gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getBasketManager().updateStock(productID, quantity);
					BasketPanel basketPanel = (BasketPanel)SwingUtilities.getAncestorOfClass(BasketPanel.class, minus);
					basketPanel.notifyChanges();
				}
			}
		});
		
		//Completely removes the product from the basket.
		removeProduct.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BasketPanel basketPanel = (BasketPanel)SwingUtilities.getAncestorOfClass(BasketPanel.class, removeProduct);
				gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getBasketManager().updateStock(productID, 0);
				basketPanel.notifyRemoval();
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
            	
                int imageSize = (int)Math.min(0.5*panelWidth, 0.8*panelHeight);
                if (panelWidth > 0 && panelHeight > 0) {
            		Image productImage = product.getIcon().getImage();
            		Image resizedImage = productImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
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
	 * Display the up to date properties of the product, including its current stock, price and discount.
	 */
	public void updateInfos()
	{
		amount.setText(String.format("%d", quantity));
		if (product.getDiscount() > 0)
		{
			productIcon.setText(String.format("-%d%%", (int)(product.getDiscount()*100)));
			price.setText(String.format("<html>Price :&nbsp<s>%.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span></s>&nbsp<span style='color:red;'>%.2f <span style='font-weight:bold; font-size:10px;'>\u2359</span></span></html>", unitPrice*quantity, quantity*unitPrice*(1 - discount)));
		}
		else
		{
			productIcon.setText("");
			price.setText(String.format("<html>Price : %.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span>.</html>", quantity * unitPrice));
		}
			
	}
	
	/**
	 * Informs the customer if the available amount of the product is less than the desired amount.
	 */
	public void onCheckedIfProductAvailable()
	{
		int availableProducts = gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getBasketManager().getQuantityById(productID);
		if (availableProducts < quantity)
		{
			quantity = availableProducts;
			availability.setText(String.format("Only %d product(s) are available at the moment.", availableProducts));
			
		}
		else
		{
			availability.setText("");
		}
		updateInfos();

	
	}
	
	public int getProductID()
	{
		return productID;
	}

	@Override
	public void refresh() {
		quantity = Math.min(quantity, product.getStock());
		inStock = product.getStock();
		stock.setText(inStock==0 ? "Product out of stock." : String.format("In stock : %d.", inStock));
		updateInfos();
	}
	
	
}
