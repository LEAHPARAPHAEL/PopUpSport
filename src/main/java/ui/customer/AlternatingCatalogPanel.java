package ui.customer;
import java.awt.CardLayout;

import backend.Product;
import backend.ProductFilter;
import ui.GUI;
import ui.tools.DescendantPanel;
import ui.tools.PanelID;

/**
 * The panel used by the customer to look at the different products. It can alternate between several panels to not have to generate them every single time and instead keeps them in memory for the customer to find them the way they left them.
 */
public class AlternatingCatalogPanel extends DescendantPanel
{
	private ProductInfoPanelCustomer productInfoPanel;
	private ScrollableCatalogPanel catalogPanel;
	private CardLayout cardLayout;
	
	public AlternatingCatalogPanel(GUI gui)
	{
		super(gui);
		setOpaque(false);
		//A card layout allows the panel to store different subpanels and switch between them.
		cardLayout = new CardLayout();
		setLayout(cardLayout);

		//The panel containing the scrollable panel which itself contains a panel displaying all the products.
		this.catalogPanel = new ScrollableCatalogPanel(new CatalogPanelCustomer(gui));
		
		//The panel containing information about a specific product, accessed by clicking on the picture of a product.
		this.productInfoPanel = new ProductInfoPanelCustomer(gui);

		add(catalogPanel, "catalogPanel");
		add(productInfoPanel, "productInfoPanel");
		cardLayout.show(this, "catalogPanel");
	}

	/**
	 * Switches to the panel referenced by the given identifier.
	 * @param newPanelID The identifier of the panel to switch to.
	 */
	public void alternatePanels(PanelID newPanelID) {
		switch (newPanelID) {
		case CATALOG:
			cardLayout.show(this, "catalogPanel");
			break;
		case PRODUCT_INFO:
			cardLayout.show(this, "productInfoPanel");
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * Passes the given filter to the next panel, until it reaches the panel responsible for calling the database manager and displaying filtered products.
	 * @param productFilter The filter object containing the criteria the products need to meet.
	 */
	public void propagateFilters(ProductFilter productFilter)
	{
		catalogPanel.applyFilters(productFilter);
	}
	
	/**
	 * Sets the product info panel to display information about the given product.
	 * @param product The product to display. 
	 */
	public void focusOnProduct(Product product)
	{
		productInfoPanel.setProduct(product);
	}
}
