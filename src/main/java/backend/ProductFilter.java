package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An object containing several criteria used to filter products from the database.
 */
public class ProductFilter 
{
	private double budget;
	private String keywords;
	private boolean available;
	private int stock;
	private List<String> orKeywords;
	private boolean or;
	private boolean orderByPrice;
	private Map<Integer, String> colors; 
	private double discount;
	
	public ProductFilter()
	{
		this.budget = Double.MAX_VALUE;
		this.keywords = "";
		this.available = true;
		this.stock = 0;
		this.orKeywords = new ArrayList<>();
		this.or = true;
		this.orderByPrice = false;
		this.colors = new HashMap<>();
		this.discount = 0;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public void addColor(int colorID, String colorName)
	{
		colors.put(colorID, colorName);
	}
	
	/**
	 * Removes a color from the list of colors selected by the user to filter products.
	 * @param colorID The identifier of the color.
	 */
	public void removeColor(int colorID)
	{
		colors.remove(colorID);
	}
	
	/**
	 * Gets the identifiers and names of the colors chosen by the customer or the admin to filter products.
	 * @return A map containing as entries the identifiers of the colors and as values their names.
	 */
	public Map<Integer, String> getColors()
	{
		return this.colors;
	}
	
	public boolean isOrderByPrice() {
		return orderByPrice;
	}



	public void setOrderByPrice(boolean orderByPrice) {
		this.orderByPrice = orderByPrice;
	}



	public boolean isOr() {
		return or;
	}



	public void setOr(boolean or) {
		this.or = or;
	}



	public boolean hasKeyword(String keyword)
	{
		return orKeywords.contains(keyword);
	}
	
	public void removeKeyword(String keyword)
	{
		this.orKeywords.remove(keyword);
	}
	
	public void addOrKeyword(String keyword)
	{
		this.orKeywords.add(keyword);
	}
	
	/**
	 * Gets the list of all the keywords that have been applied to filter products.
	 * @return The list of all keywords applied to the bottom left corner of the screen.
	 */
	public List<String> getOrKeywords() {
		return orKeywords;
	}

	/**
	 * Sets the list of active keywords applied to filter products to be the one passed as an argument.
	 * @param orKeywords A list of keywords that will be used to filter products and displayed on the bottom left corner of the screen.
	 */
	public void setOrKeywords(List<String> orKeywords) {
		this.orKeywords = orKeywords;
	}



	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public double getBudget()
	{
		return this.budget;
	}
	
	public void setBudget(double budget)
	{
		this.budget = budget;
	}
	
	public boolean getAvailable()
	{
		return available;
	}
	
	public void setAvailable(boolean available)
	{
		this.available = available;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
	
}
