package ui.admin;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import backend.Product;
import backend.ProductFilter;
import ui.GUI;
import ui.tools.CustomScrollBarUI;
import ui.tools.DescendantPanel;
import ui.tools.PanelID;

/**
 * The panel used by the administrator to manage products. It can alternate between several panels to avoid generating them every single time, and instead stores them in memory for the administrator to be able to find them as they left them.
 */
public class AlternatingProductPanel extends DescendantPanel
{
	private ProductInfoPanelAdmin productInfoPanel;
	private ScrollableProductsPanel catalogPanel;
	private NewProductPanel newProductPanel;
	private CardLayout cardLayout;
	
	public AlternatingProductPanel(GUI gui)
	{
		super(gui);
		setOpaque(false);
		//A card layout allows the panel to store different subpanels and switch between them.
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		
		//The panel containing the scrollable panel which itself contains a panel displaying all the products.
		this.catalogPanel = new ScrollableProductsPanel();
		
		//The panel that can be accessed by clicking on a specific product and where the admin can modify information about the product.
		this.productInfoPanel = new ProductInfoPanelAdmin(gui);
		
		//The panel that can be used by the admin to add a new product to the database.
		this.newProductPanel = new NewProductPanel(gui);
		
		add(catalogPanel, "catalogPanel");
		add(productInfoPanel, "productInfoPanel");
		add(newProductPanel, "newProductPanel");
		cardLayout.show(this, "catalogPanel");
	}

	/**
	 * Given the identifier of a panel, switches to this panel.
	 * @param newPanelID The identifier of the panel to switch to.
	 */
	public void alternatePanels(PanelID newPanelID) {
		switch (newPanelID) {
		//Show the catalog panel as it was left by the admin, with the previous filters applied.
		case CATALOG:
			ProductManagementPanel parent = (ProductManagementPanel)SwingUtilities.getAncestorOfClass(ProductManagementPanel.class, this);
			catalogPanel.applyFilters(parent.getProductFilter());
			cardLayout.show(this, "catalogPanel");
			break;
		//Show the product info panel.
		case PRODUCT_INFO:
			cardLayout.show(this, "productInfoPanel");
			break;
		//Show the new product panel;
		case NEW:
			newProductPanel = new NewProductPanel(gui);
			cardLayout.show(this, "newProductPanel");
			break;
		default:
			break;
		}
		
	}

	/**
	 * Passes the current filter to the following panel, and so forth, until it reaches the panel responsible for calling the database manager and displaying the products.
	 * @param productFilter The filter containing the criteria the displayed products need to meet.
	 */
	public void propagateFilters(ProductFilter productFilter)
	{
		catalogPanel.applyFilters(productFilter);
	}
	
	/**
	 * Changes the current product info panel to a new one containing modifiable information about the new product.
	 * @param product The product whose information the admin wants to look at or modify.
	 */
	public void focusOnProduct(Product product)
	{
		productInfoPanel.setProduct(product);
	}
	
	private class ScrollableProductsPanel extends JPanel
	{
		private CatalogPanelAdmin catalogPanel;
		private int screenSize;
		private JScrollPane scrollPane;
		
		public ScrollableProductsPanel()
		{
	        super(new GridBagLayout());
	        setOpaque(false);
	        //The panel used to display the products.
	        this.catalogPanel = new CatalogPanelAdmin(gui);
	        
	        //The scrollable panel containing the preivous catalog panel.
	        this.scrollPane = new JScrollPane(catalogPanel);
	        scrollPane.setOpaque(false);
	        scrollPane.getViewport().setOpaque(false);
	        this.screenSize = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(GUI.BACKGROUND_ADMIN, GUI.PRODUCT_ADMIN));
		    GridBagConstraints gbc = new GridBagConstraints();
		    gbc.gridx = 0;
		    gbc.gridy = 0;
		    gbc.weightx = 1;
		    gbc.weighty = 1;
		    gbc.fill = GridBagConstraints.BOTH;
		    
		    add(scrollPane, gbc);
		    
		    //Resizes the products when the window is resized.
	        scrollPane.getViewport().addComponentListener(new ComponentAdapter() {
	            @Override
	            public void componentResized(ComponentEvent e) { 
	            	int width = scrollPane.getWidth();
	            	int height = scrollPane.getHeight();
	            	//The more stretched the window, the more products by row.
	            	int itemsPerRow = 4*width/screenSize + 2;
	            	for (Component comp : catalogPanel.getComponents())
	            	{
	            		if (comp instanceof JLabel)
	            			comp.setPreferredSize(new Dimension(width, height));
	            		else
	            		{
	            			comp.setPreferredSize(new Dimension(width/itemsPerRow - 15, (int)((width/itemsPerRow- 15)/0.6)));
	            			comp.revalidate();
	            		
	            		}

	            	}
	            	catalogPanel.recalculatePanels(itemsPerRow);
	            	catalogPanel.revalidate();
	            }
	        });
	        
		    
		}
		
		/**
		 * Passes the filter to the catalog panel which is responsible for calling the database manager and getting all the products to display.
		 * @param productFilter The filter used to select the products to display.
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
}


