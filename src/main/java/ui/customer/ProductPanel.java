package ui.customer;
import javax.swing.*;
import javax.swing.border.LineBorder;

import backend.Product;
import ui.GUI;
import ui.tools.NonOpaqueJLabel;
import ui.tools.NonOpaqueTextArea;
import ui.tools.PanelID;
import ui.tools.Refreshable;
import ui.tools.RoundedPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * A panel containing concise information about a product, that enables the customer to look at more detailed information if the picture of the product is pressed.
 */
public class ProductPanel extends RoundedPanel implements Refreshable
{
	private Product product;
	private JButton productButton;
	private NonOpaqueJLabel productPrice;
	
	public ProductPanel(Product product)
	{
		super(new GridBagLayout());
		setBackground(GUI.PRODUCT_CUSTOMER);
		this.product = product;
		displayProduct();

	}
	
	/**
	 * Display some basic properties of the product, as long with a picture of it that can be clicked to see additional information about the product.
	 */
	public void displayProduct()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		
		//A button in which the picture of the product is displayed and that can switch to a more detailed panel focusing on this product.
		this.productButton = new JButton();
		productButton.setMargin(new Insets(0,0,0,0));
		productButton.setForeground(GUI.RED);
		productButton.setFont(new Font("Serif", Font.BOLD, 20));
		productButton.setHorizontalTextPosition(SwingConstants.CENTER);
		productButton.setVerticalTextPosition(SwingConstants.CENTER);
		productButton.setContentAreaFilled(false);
		productButton.setPreferredSize(new Dimension(0,0));
		productButton.setOpaque(false);
        productButton.setBorderPainted(false); ;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0.6;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(productButton, gbc);
		
		productButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AlternatingCatalogPanel cardPanel = (AlternatingCatalogPanel)SwingUtilities.getAncestorOfClass(AlternatingCatalogPanel.class, productButton);
				cardPanel.focusOnProduct(product);
				cardPanel.alternatePanels(PanelID.PRODUCT_INFO);
			}
		});
		
		gbc = new GridBagConstraints();
		NonOpaqueTextArea productName = new NonOpaqueTextArea(product.getName());
		productName.setEditable(false);
		productName.setForeground(Color.DARK_GRAY);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.15;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.insets = new Insets(5,5,5,5);
		add(productName, gbc);


		gbc = new GridBagConstraints();
		NonOpaqueJLabel productBrand = new NonOpaqueJLabel(product.getBrand());
		productBrand.setForeground(Color.DARK_GRAY);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.insets = new Insets(5,5,5,5);
		add(productBrand, gbc);
		
		
		this.productPrice = new NonOpaqueJLabel(String.format("<html>%.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span></html>", product.getPrice()));
		productPrice.setPreferredSize(new Dimension(0,0));
		productPrice.setForeground(Color.DARK_GRAY);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 1;
		gbc.weighty = 0.05;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.insets = new Insets(5,5,5,5);
		add(productPrice, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 1;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.SOUTH;
		add(new JLabel(), gbc);
		
		updateInfos();
		
		//Resizes the picture of the product when its container is resized.
		addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                if (panelWidth > 0 && panelHeight > 0) {
            		Image productImage = product.getIcon().getImage();
            		Image resizedImage = productImage.getScaledInstance(panelWidth - 30, panelWidth - 30, Image.SCALE_SMOOTH);
            		ImageIcon resizedIcon = new ImageIcon(resizedImage);

                    productButton.setIcon(resizedIcon);
                }
               
	            revalidate(); 
            }
        });
		
        for (ComponentListener listener : getComponentListeners())
        {
        	listener.componentResized(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
        }
		
	}
	
	
	/**
	 * Display up to date information about the product.
	 */
	public void updateInfos()
	{
		if (product.getDiscount() > 0)
		{
			productButton.setText(String.format("-%d%%", (int)(product.getDiscount()*100)));
			productPrice.setText(String.format("<html><s>%.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span></s>	&nbsp&nbsp<span style='color:red;'>%.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span></span></html>", product.getPrice(), product.getPrice()*(1 - product.getDiscount())));
		}
		else
		{
			productButton.setText("");
			productPrice.setText(String.format("<html>%.2f<span style='color:red; font-weight:bold; font-size:10px;'> \u2359</span> </html>", product.getPrice()));
		}
	}

	@Override
	public void refresh() {
		updateInfos();
	}
	


}
