package backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * An object containing all the personal information about a customer.
 */
public class CustomerProfile 
{
	private String name;
	private String firstName;
	private String email;
	private String username;
	private String password;
	private double credit;
	private BasketManager basketManager;
	private boolean hasOngoingReservation;
	private boolean hasReservationToComplete;
	private long reservationTimeLeft;
	private List<Achievement> achievements;
	private boolean vip;
	
	public CustomerProfile(String name, String firstName, String email, String username, String password, double credit, boolean vip, BasketManager basketManager)
	{
		this.name = name;
		this.firstName = firstName;
		this.email = email;
		this.username = username;
		this.password = password;
		this.credit = credit;
		this.basketManager = basketManager;
		this.reservationTimeLeft = 0;
		this.hasOngoingReservation = false;
		this.hasReservationToComplete = false;
		this.vip = vip;
		this.achievements = new ArrayList<>();
	}
	
	
	public CustomerProfile(String name, String firstName, String email, String username, double credit, boolean vip)
	{
		this.name = name;
		this.firstName = firstName;
		this.email = email;
		this.username = username;
		this.credit = credit;
		this.vip = vip;
		this.achievements = new ArrayList<>();
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public void addAchievement(Achievement achievement)
	{
		achievements.add(achievement);
		Collections.sort(achievements, Comparator.comparing(Achievement::getAchievementDate));
	}
	
	public List<Achievement> getAchievements() {
		return achievements;
	}


	public void setAchievements(List<Achievement> achievements) {
		this.achievements = achievements;
	}


	public boolean isVip()
	{
		return vip;
	}
	
	public void setVip(boolean vip)
	{
		this.vip = vip;
	}
	

	/**
	 * Checks if the customer has a reservation they have not completed the last time they logged out.
	 * @return True if the customer has a reservation to complete, false otherwise.
	 */
	public boolean hasReservationToComplete() {
		return hasReservationToComplete;
	}
	
	public void setHasReservationToComplete(boolean hasReservationToComplete) {
		this.hasReservationToComplete = hasReservationToComplete;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	/**
	 * Adds a certain amount of credit to the customer's current credit.
	 * @param addedCredit The amount of credit to add (in €).
	 */
	public void addCredit(double addedCredit)
	{
		this.credit += addedCredit;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the basket manager that is responsible for all operations involving the customer's basket.
	 * @return The basket manager that contains the customer's basket and controls all the operations involving it.
	 */
	public BasketManager getBasketManager()
	{
		return this.basketManager;
	}
	
	/**
	 * Sets the basket manager to be the one given as a parameter. 
	 * @param basketManager The new basket manager that contains the customer's basket and is responsible for all operations involving it.
	 */
	public void setBasketManager(BasketManager basketManager)
	{
		this.basketManager = basketManager;
	}

	public boolean hasOngoingReservation() {
		return hasOngoingReservation;
	}
	
	public void setHasOngoingReservation(boolean hasOngoingReservation) {
		this.hasOngoingReservation = hasOngoingReservation;
	}


	public long getReservationTimeLeft() {
		return reservationTimeLeft;
	}
	

	public void setReservationTimeLeft(long reservationTimeLeft) {
		this.reservationTimeLeft = reservationTimeLeft;

	}
	
	
	
	
	
}
