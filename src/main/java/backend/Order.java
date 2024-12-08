package backend;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * An object containing all the information about an order.
 */
public class Order 
{
	private int orderID;
	private String address;
	private double price;
	private Timestamp orderTime;
	private Timestamp expectedDeliveryTime;
	private Timestamp realDeliveryTime;
	private String status;
	/**
	 * A map containing as entries the identifiers of the products and as values their quantities.
	 */
	private Map<Integer, Integer> orderContent;
	/**
	 * A map containing as entries the identifiers of the products and as values their price at the moment they were ordered.
	 */
	private Map<Integer, Double> pricesWhenOrdered;
	
	public Order(int orderID, String address, double price, Timestamp orderTime, Timestamp expectedDeliveryTime, Timestamp realDeliveryTime, String status)
	{
		this.orderID = orderID;
		this.address = address;
		this.price = price;
		this.orderTime = orderTime;
		this.expectedDeliveryTime = expectedDeliveryTime;
		this.realDeliveryTime = realDeliveryTime;
		this.status = status;
		this.orderContent = new HashMap<>();
		this.pricesWhenOrdered = new HashMap<>();
	}

	/**
	 * Gets the discounted prices for each product at the moment they were ordered.
	 * @return A map containing as entries the identifiers of the products and as values their price at the moment they were ordered.
	 */
	public Map<Integer, Double> getPricesWhenOrdered() {
		return pricesWhenOrdered;
	}

	/**
	 * Sets the prices of the products when they were ordered to be the ones given as a parameter.
	 * @param pricesWhenOrdered A map containing as entries the identifiers of the products and as values their price at the moment they were ordered.
	 */
	public void setPricesWhenOrdered(Map<Integer, Double> pricesWhenOrdered) {
		this.pricesWhenOrdered = pricesWhenOrdered;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Gets all the ordered products along with their quantities.
	 * @return A map containing as entries the identifiers of the products and as values their quantities.
	 */
	public Map<Integer, Integer> getOrderContent() {
		return orderContent;
	}
	
	/**
	 * Sets the ordered products and their quantities to be the ones given as a parameter.
	 * @param orderContent A map containing as entries the identifiers of the products and as values their quantities.
	 */
	public void setOrderContent(Map<Integer, Integer> orderContent) {
		this.orderContent = orderContent;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public Timestamp getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}

	public Timestamp getExpectedDeliveryTime() {
		return expectedDeliveryTime;
	}

	public void setExpectedDeliveryTime(Timestamp expectedDeliveryTime) {
		this.expectedDeliveryTime = expectedDeliveryTime;
	}

	public Timestamp getRealDeliveryTime() {
		return realDeliveryTime;
	}

	public void setRealDeliveryTime(Timestamp realDeliveryTime) {
		this.realDeliveryTime = realDeliveryTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Adds a product to an order with its ordered quantity and its price at the moment it was ordered.
	 * @param productID The identifier of the ordered product.
	 * @param quantity The quantity of this product to add.
	 * @param priceWhenOrdered The price of the product at the time it was ordered.
	 */
	public void addProduct(int productID, int quantity, double priceWhenOrdered)
	{
		orderContent.put(productID, quantity);
		pricesWhenOrdered.put(productID, priceWhenOrdered);
	}
	
	/**
	 * Gets the price of an ordered product at the time it was ordered.
	 * @param productID The identifier of the product.
	 * @return The price (in €) of the product when it was ordered.
	 */
	public double getProductPriceWhenOrdered(int productID)
	{
		return pricesWhenOrdered.get(productID);
	}
	
	
}
