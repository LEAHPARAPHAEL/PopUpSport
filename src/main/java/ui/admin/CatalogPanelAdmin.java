package ui.admin;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import backend.Product;
import backend.ProductFilter;
import ui.GUI;
import ui.tools.DescendantPanel;

/**
 * The panel used by the administrator to display the products present in the database.
 */
public class CatalogPanelAdmin extends DescendantPanel
{
	private List<Product> products;
	private int itemsPerRow;

	public CatalogPanelAdmin(GUI gui) 
	{
		super(new GridBagLayout(), gui);
		this.products = new ArrayList<>();
		this.itemsPerRow = 3;
		applyFilters(new ProductFilter());
		setOpaque(false);
	}

	/**
	 * Show the products matching the current filter chosen by the admin.
	 */
	public void updateProducts()
	{
		removeAll();
	    GridBagConstraints gbc = new GridBagConstraints();
	    int size = products.size();
	    
	    //If no products match the filter, signal it to the admin.
	    if (size == 0)
	    {
	    	JLabel noProducts = new JLabel("No product was found matching these criteria.", SwingUtilities.CENTER);
	    	noProducts.setPreferredSize(new Dimension(0,0));
	    	gbc.gridx = 0;
	    	gbc.gridy = 0;
	    	add(noProducts, gbc);
	    }
	    
	    else
	    {
	    	//Otherwise, every product is displayed in its own individual panel.
	    	for (int i = 0; i < size; i++)
	    	{
	    		ProductPanelAdmin productPanel = new ProductPanelAdmin(products.get(i));
	    		productPanel.setPreferredSize(new Dimension(0,0));
	    		gbc = new GridBagConstraints();
	            gbc.gridx = i % itemsPerRow; 
	            gbc.gridy = i / itemsPerRow;
				gbc.insets = new Insets(5,5,5,5);
	            add(productPanel, gbc);
	    	}
	    }
	    
	}
	
	/**
	 * Calls the database manager to get the products matching the given filter and display them.
	 * @param productFilter The filter containing the criteria products need to meet.
	 */
	public void applyFilters(ProductFilter productFilter)
	{
		this.products = gui.getDatabaseManager().getProductsWithFilters(productFilter);
		updateProducts();
		revalidate();
		repaint();
	}
	
	/**
	 * Sets the number of items per row and displays the products accordingly.
	 * @param itemsPerRow The number of products to display on each row.
	 */
	public void recalculatePanels(int itemsPerRow)
	{
		this.itemsPerRow = itemsPerRow;
		Component[] components = getComponents(); 
		removeAll();
		GridBagConstraints gbc;
		for (int i = 0; i < components.length; i++)
		{
			gbc = new GridBagConstraints();
			gbc.gridx = i % itemsPerRow;
			gbc.gridy = i / itemsPerRow;
			gbc.insets = new Insets(5,5,5,5);
			add(components[i], gbc);
		}

	}
	
	

}
