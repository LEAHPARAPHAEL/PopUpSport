package ui.admin;

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
import javax.swing.JButton;

import ui.GUI;
import ui.tools.NonOpaqueJLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import backend.Product;
import ui.tools.NonOpaqueTextArea;
import ui.tools.PanelID;
import ui.tools.RoundedPanel;

/**
 * A panel containing concise information about a product and allows the user to obtain detailed information if it is clicked.
 */
public class ProductPanelAdmin extends RoundedPanel
{
	private Product product;
	
	public ProductPanelAdmin(Product product)
	{
		super(new GridBagLayout());
		this.product = product;
		setBackground(GUI.PRODUCT_ADMIN);
		GridBagConstraints gbc = new GridBagConstraints();
		
		//The button that contains the picture of the product and can be clicked to access its editable properties.
		JButton productButton = new JButton();
		productButton.setMargin(new Insets(0,0,0,0));
		productButton.setFont(new Font("Serif", Font.BOLD, 20));
		productButton.setForeground(GUI.RED);
		productButton.setHorizontalTextPosition(SwingConstants.CENTER);
		productButton.setVerticalTextPosition(SwingConstants.CENTER);
		productButton.setContentAreaFilled(false);
		productButton.setPreferredSize(new Dimension(0,0));
		productButton.setOpaque(false);
        productButton.setBorderPainted(false); ;
		productButton.setPreferredSize(new Dimension(0,0));
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
				AlternatingProductPanel cardPanel = (AlternatingProductPanel)SwingUtilities.getAncestorOfClass(AlternatingProductPanel.class, productButton);
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
		gbc.insets = new Insets(5,5,5,5);
		gbc.anchor = GridBagConstraints.SOUTH;
		

		add(productName, gbc);


		gbc = new GridBagConstraints();
		NonOpaqueJLabel productBrand = new NonOpaqueJLabel(product.getBrand());
		productBrand.setForeground(Color.DARK_GRAY);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		gbc.anchor = GridBagConstraints.SOUTH;

		add(productBrand, gbc);
		
		
		NonOpaqueJLabel productPrice = new NonOpaqueJLabel(String.format("<html>%.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span></html>", product.getPrice()));
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
		add(new NonOpaqueJLabel(), gbc);
		
		//If there is a discount applied to this product, display it on the picture and modify the way the price is displayed.
		if (product.getDiscount() > 0)
		{
			productButton.setText(String.format("-%d%%", (int)(product.getDiscount()*100)));
			productPrice.setText(String.format("<html><s>%.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span></s>	&nbsp&nbsp<span style='color:red;'>%.2f <span style='font-weight:bold; font-size:10px;'>\u2359</span></span></html>", product.getPrice(), product.getPrice()*(1 - product.getDiscount())));
		}
		else
		{
			productButton.setText("");
			productPrice.setText(String.format("<html>%.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span></html>", product.getPrice()));
		}
		
		//Resizes manually the image to fit in its container when resized.
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
}
