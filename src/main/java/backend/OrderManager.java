package backend;

import java.util.HashMap;
import java.util.Map;

/**
 * An order manager that is responsible for all the operations involving the orders in the application.
 */
public class OrderManager 
{
	/**
	 * A map containing as entries the identifiers of the orders and as values the orders themselves.
	 */
	private Map<Integer, Order> orders;
	
	/**
	 * A new order manager that is responsible for all the operations involving the orders in the application.
	 */
	public OrderManager()
	{
		orders = new HashMap<>();
	}

	/**
	 * Gets all orders with their identifiers.
	 * @return A map containing as entries the identifiers of the orders and as values the orders themselves.
	 */
	public Map<Integer, Order> getOrders() {
		return orders;
	}

	/**
	 * Sets all orders and their identifiers to be the ones given as a parameter.
	 * @param orders A map containing as entries the identifiers of the orders and as values the orders themselves.
	 */
	public void setOrders(Map<Integer, Order> orders) {
		this.orders = orders;
	}
	
	/**
	 * Gets a specific order identified by the integer passed as an argument.
	 * @param orderID The identifier of the order.
	 * @return The order associated with this identifier.
	 */
	public Order getOrderByID(int orderID)
	{
		return orders.get(orderID);
	}
	
	/**
	 * Deletes all orders and their identifiers from the map storing them.
	 */
	public void resetOrders()
	{
		this.orders.clear();
	}
	
}
