package ui.tools;

import java.awt.Color;

/**
 * An object containing information about a specific color.
 */
public class ColorInfo 
{
	private Color color;
	private String colorName;
	private static int id;
	private int colorID;
	
	public ColorInfo(Color color, String colorName)
	{
		this.color = color;
		this.colorName = colorName;
		this.colorID = id++;
	}

	public int getColorID() {
		return colorID;
	}

	public void setColorID(int colorID) {
		this.colorID = colorID;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	
	
}
