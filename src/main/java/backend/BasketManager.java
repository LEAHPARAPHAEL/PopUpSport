package backend;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A basket manager that is responsible for all the operations involving the customer's basket.
 */
public class BasketManager 
{
	private Map<Integer, Integer> basket;
	private ProductManager productManager;
	
	/**
	 * A basket manager that is responsible for all the operations involving the customer's basket.
	 * @param basket A map containing as entries the identifiers of the products present in the customer's basket and as values their quantities.
	 * @param productManager A product manager responsible for all the operations involving the products in the application.
	 */
	public BasketManager(Map<Integer, Integer> basket, ProductManager productManager)
	{
		this.basket = basket;
		this.productManager = productManager;
	}
	
	/**
	 * A basket manager that is responsible for all the operations involving the customer's basket.
	 * @param productManager A product manager responsible for all the operations involving the products in the application.
	 */
	public BasketManager(ProductManager productManager)
	{
		this.basket = new HashMap<>();
		this.productManager = productManager;
	}
	
	/**
	 * Tries to add a given quantity of a product to the customer's basket and returns the quantity that was effectively added.
	 * @param productID The identifier of the added product.
	 * @param amount The quantity the customer is trying to buy.
	 * @return The quantity of the product that was effectively added to the basket.
	 */
	public int addProducts(int productID, int amount)
	{
		//If the product is already in the basket in some quantity, check if the sum of the old and new quantities isn't more than the product actual stock.
		if (basket.containsKey(productID))
		{
			int attemptedPurchase = basket.get(productID)+amount;
			int maxStock = productManager.getProductById(productID).getStock();
			basket.put(productID, Math.min(attemptedPurchase, maxStock));
			return maxStock - attemptedPurchase;
		}

		//Otherwise, no issue with the stock since the customer cannot add more than there is remaining.
		else
			basket.put(productID, amount);
		return amount;
	}
	
	/**
	 * Updates the quantity of a given product in the basket. If the new stock is 0, the product is removed from the basket.
	 * @param productID The identifier of the product.
	 * @param newStock The new quantity for this product.
	 */
	public void updateStock(int productID, int newStock)
	{
		if (newStock == 0)
			basket.remove(productID);
		else
			basket.put(productID, newStock);
	}
	
	/**
	 * Gets the total price of the products in the basket, taking into account their potential discounts.
	 * @return The total price of the basket in €.
	 */
	public double getTotalPrice()
	{
		double totalPrice = 0;
		for (Entry<Integer, Integer> entry : basket.entrySet())
		{
			Product product = productManager.getProductById(entry.getKey());
			totalPrice += product.getPrice() * (1 - product.getDiscount()) * entry.getValue();
		}
		return totalPrice;
	}
	
	/**
	 * Returns the quantity of the product associated with the given identifier present in the basket.
	 * @param productID The identifier of the product.
	 * @return The quantity of this product present in the basket.
	 */
	public int getQuantityById(int productID)
	{
		return basket.get(productID);
	}
	
	/**
	 * Returns the size of the basket, which is the number of different products present in the basket.
	 * @return The number of different products in the basket.
	 */
	public int getBasketSize()
	{
		return basket.size();
	}
	
	/**
	 * Gets the content of the customer's basket.
	 * @return A map in which the entries are the identifiers for each product and the values their quantities in the basket.
	 */
	public Map<Integer, Integer> getBasket()
	{
		return basket;
	}
	
	/**
	 * Empties the basket by removing all its mappings.
	 */
	public void emptyBasket()
	{
		this.basket.clear();
	}
	
	/**
	 * Sets the content of the customer's basket to be the one given as parameter.
	 * @param basket A map in which the entries are the identifiers for each product and the values their quantities in the basket.
	 */
	public void setBasket(Map<Integer, Integer> basket)
	{
		this.basket.clear();
		for (Entry<Integer, Integer> entry : basket.entrySet())
		{
			int productStock = productManager.getProductById(entry.getKey()).getStock();
			if (productStock > 0 && productManager.getProductById(entry.getKey()).isAvailable())
				this.basket.put(entry.getKey(), Math.min(productStock, entry.getValue()));
		}
		
	}
}
