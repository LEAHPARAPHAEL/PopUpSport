package backend;

import java.sql.Timestamp;

import javax.swing.ImageIcon;

/**
 * An object containing all the informations about a customer's achievement.
 */
public class Achievement 
{
	private String description;
	private double reward;
	private ImageIcon picture;
	private Timestamp achievementDate;
	
	
	public Achievement(String description, double reward, ImageIcon picture, Timestamp achievementDate)
	{
		//Hola 
		this.description = description;
		this.reward = reward;
		this.picture = picture;
		this.achievementDate = achievementDate;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public double getReward() {
		return reward;
	}


	public void setReward(double reward) {
		this.reward = reward;
	}


	public ImageIcon getPicture() {
		return picture;
	}


	public void setPicture(ImageIcon picture) {
		this.picture = picture;
	}


	public Timestamp getAchievementDate() {
		return achievementDate;
	}


	public void setAchievementDate(Timestamp achievementDate) {
		this.achievementDate = achievementDate;
	}
	
	
}
