package backend;

import javax.swing.ImageIcon;

/**
 * An object containing all the properties of a product.
 */
public class Product 
{
	private int id;
	private String name;
	private String description;
	private double price;
	private int stock;
	private String brand;
	private ImageIcon icon;
	private boolean available;
	private String colors;
	private double discount;
	
	public Product(int id, String name, String description, String brand, double price, int stock, ImageIcon icon, boolean available, String colors, double discount)
	{
		this.id = id;
		this.name = name;
		this.description = description;
		this.brand = brand;
		this.price = price;
		this.stock = stock;
		this.icon = icon;
		this.available = available;
		this.colors = colors;
		this.discount = discount;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getColors() {
		return colors;
	}

	public void setColors(String colors) {
		this.colors = colors;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public ImageIcon getIcon()
	{
		return icon;
	}
	
	public void setIcon(ImageIcon icon)
	{
		this.icon = icon;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public boolean isAvailable()
	{
		return available;
	}
	
	public void setAvailable(boolean available)
	{
		this.available = available;
	}
	
	
	
	
}
