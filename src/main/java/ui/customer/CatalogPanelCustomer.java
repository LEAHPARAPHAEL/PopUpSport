package ui.customer;
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
 * A panel contained inside of a JScrollPane and that displays the products matching the customer's filters.
 */
public class CatalogPanelCustomer extends DescendantPanel
{
	private List<Product> products;
	private int itemsPerRow;
	
	public CatalogPanelCustomer(GUI gui)
	{
		super(new GridBagLayout(),gui);
		setOpaque(false);
		this.products = new ArrayList<>();
		this.itemsPerRow = 3;
		//A new filter guarantees that all products are selected
		applyFilters(new ProductFilter());
	}

	/**
	 * Organizes the layout to display all the products matching the filter.
	 */
	public void updateProducts()
	{
		removeAll();
	    GridBagConstraints gbc = new GridBagConstraints();
	    int size = products.size();
	    
	    //If no products match the filter, inform the customer.
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
	    	//Otherwise, create an individual panel for each product matching the filters.
	    	for (int i = 0; i < size; i++)
	    	{
	    		ProductPanel productPanel = new ProductPanel(products.get(i));
	    		productPanel.setPreferredSize(new Dimension(0,0));
	    		gbc = new GridBagConstraints();
	            gbc.gridx = i % itemsPerRow; 
	            gbc.gridy = i / itemsPerRow;
	            gbc.anchor = GridBagConstraints.NORTHWEST;
	            gbc.insets = new Insets(5,5,5,5);
	            add(productPanel, gbc);
	    	}
	    }
	    
	    
	}
	
	/**
	 * Calls the database manager to get the products matching the given filter and displays them.
	 * @param productFilter A filter containing the criteria to select products from the database.
	 */
	public void applyFilters(ProductFilter productFilter)
	{
		this.products = gui.getDatabaseManager().getProductsWithFilters(productFilter);
		removeAll();
		updateProducts();
		revalidate();
		repaint();
	}
	
	/**
	 * Sets the number of items per row to be the one passed as an argument and reorganizes the layout accordingly.
	 * @param itemsPerRow The number of products that appear on each row of the catalog.
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
