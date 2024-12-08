package ui.customer;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import backend.ProductFilter;
import ui.GUI;
import ui.tools.*;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * The panel that contains the JScrollPane displaying the catalog of all products.
 */
public class ScrollableCatalogPanel extends NonOpaquePanel
{
	private CatalogPanelCustomer catalogPanel;
	private int screenSize;
	private int itemsPerRow;
	private JScrollPane scrollPane;
	
	public ScrollableCatalogPanel(CatalogPanelCustomer catalogPanel)
	{
        super(new GridBagLayout());
        //The scrollable panel containing a catalog panel that contains itself all the products to display.
        this.scrollPane = new JScrollPane(catalogPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        //The catalog panel containing the individual product panels.
        this.catalogPanel = catalogPanel;
        this.screenSize = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(GUI.BACKGROUND_CUSTOMER, GUI.PRODUCT_CUSTOMER));
        
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.weightx = 1;
	    gbc.weighty = 1;
	    gbc.fill = GridBagConstraints.BOTH;
	    
	    add(scrollPane, gbc);
	    
	    //Resizes the individual product panels when their container is resized.
        scrollPane.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) { 
            	int width = scrollPane.getWidth();
            	int height = scrollPane.getHeight();
            	int itemsPerRow = 4*width/screenSize + 2;
            	for (Component comp : catalogPanel.getComponents())
            	{
            		if (comp instanceof JLabel)
            			comp.setPreferredSize(new Dimension(width, height));
            		else
            		{
            			comp.setPreferredSize(new Dimension(width/itemsPerRow-15, (int)((width/itemsPerRow-15)/0.6)));
            			comp.revalidate();
            		
            		}

            	}
            	catalogPanel.recalculatePanels(itemsPerRow);
            	catalogPanel.revalidate();
            }
        });
        
	    
	}
	
	/**
	 * Passes the given filter to the catalog panel which is responsible for calling the database manager and displaying the filtered products.
	 * @param productFilter The filter containing the criteria used to filter products.
	 */
	public void applyFilters(ProductFilter productFilter)
	{
		catalogPanel.applyFilters(productFilter);
		for (ComponentListener listener : scrollPane.getViewport().getComponentListeners())
		{
			listener.componentResized(new ComponentEvent(scrollPane.getViewport(), ComponentEvent.COMPONENT_RESIZED));
		}
	}
	
	
}
