package backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A product manager responsible for the operations involving the products in the application.
 */
public class ProductManager 
{
	/**
	 * A map containing as entries the identifiers of the products and as values the products themselves.
	 */
	private Map<Integer, Product> products;
	/**
	 * A list of integers corresponding to the identifiers of all the products matching the current applied keywords.
	 */
	private List<Integer> productsMatchingKeywords;
	
	public ProductManager(DatabaseManagerH2 databaseManager)
	{
		this.products = databaseManager.getProducts();
		setAllProductsMatchingKeywords();
	}
	
	/**
	 * Gets a product associated with the given identifier.
	 * @param productID The identifier used to find the product.
	 * @return The product associated with the given identifier.
	 */
	public Product getProductById(int productID)
	{
		return products.get(productID);
	}
    
	/**
	 * Gets the identifiers of all the products matching the current applied keywords.
	 * @return The list of integers corresponding to the identifiers of all the products matching the current applied keywords.
	 */
    public List<Integer> getProductsMatchingKeywords() 
    {
		return productsMatchingKeywords;
	}
    
    /**
     * Sets the products matching the current applied keywords to be all the products.
     */
    public void setAllProductsMatchingKeywords()
    {
    	this.productsMatchingKeywords = new ArrayList<>();
    	for (int productID: products.keySet())
    		productsMatchingKeywords.add(productID);
    	
    }

    /**
     * Sets the products matching the current applied keywords to be the ones passed as an argument.
     * @param productsMatchingKeywords The list of integers corresponding to the identifiers of all the products matching the current applied keywords.
     */
	public void setProductsMatchingKeywords(List<Integer> productsMatchingKeywords) {
		this.productsMatchingKeywords = productsMatchingKeywords;
	}

	/**
	 * Adds a product to the list of all products.
	 * @param productID The identifier of the new product.
	 * @param product The new product itself.
	 */
	public void addProduct(int productID, Product product)
    {
    	products.put(productID, product);
    }
    
	/**
	 * Removes the product associated with the given identifier.
	 * @param productID The identifier of the product to remove.
	 */
    public void removeProduct(int productID)
    {
    	products.remove(productID);
    }
    
	
}
