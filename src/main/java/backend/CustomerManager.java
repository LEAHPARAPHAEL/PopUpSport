package backend;

import java.util.ArrayList;
import java.util.List;

/**
 * A customer manager that is responsible for all the operations involving customers in the application.
 */
public class CustomerManager 
{
	private List<CustomerProfile> customers;
	private CustomerProfile currentCustomer;
	

	/**
	 * A customer manager that is responsible for all the operations involving customers in the application.
	 */
	public CustomerManager()
	{
		this.customers = new ArrayList<>();
	}
	
	/**
	 * Gets the current customer that is logged in the application.
	 * @return An object customer profile containing all the information about the current customer.
	 */
	public CustomerProfile getCurrentCustomer() {
		return currentCustomer;
	}


	/**
	 * Sets the current customer profile to be the one passed as an argument.
	 * @param currentCustomer An object that contains all the information about the current customer.
	 */
	public void setCurrentCustomer(CustomerProfile currentCustomer) {
		this.currentCustomer = currentCustomer;
	}



	/**
	 * Returns a list of all customers in the database.
	 * @return An list of customer profiles containing information about each customer in the database.
	 */
	public List<CustomerProfile> getCustomers()
	{
		return customers;
	}
	
	/**
	 * Sets the list of customers to be the one given as parameter.
	 * @param customers The new list of customers.
	 */
	public void setCustomers(List<CustomerProfile> customers)
	{
		this.customers = customers;
	}
	
	/**
	 * Removes all the customers present in the list of all customers.
	 */
	public void resetCustomers()
	{
		this.customers = new ArrayList<>();
	}
	
	/**
	 * Adds a new customer to the list of all customers.
	 * @param customer A customer profile containing information about the new customer to add.
	 */
	public void addCustomer(CustomerProfile customer)
	{
		customers.add(customer);
	}
}
