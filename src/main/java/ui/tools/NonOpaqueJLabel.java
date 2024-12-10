package ui.tools;

import java.awt.Dimension;

import javax.swing.JLabel;

/**
 * A JLabel that was modified to have a transparent background.
 */
public class NonOpaqueJLabel extends JLabel
{
	public NonOpaqueJLabel()
	{
		super();
		setOpaque(false);
		setPreferredSize(new Dimension(0,0));
	}
	
	public NonOpaqueJLabel(String text)
	{
		super(text);
		setOpaque(false);
		setPreferredSize(new Dimension(0,0));
	}
	
	public NonOpaqueJLabel(String text, int display)
	{
		super(text, display);
		setOpaque(false);
		setPreferredSize(new Dimension(0,0));
	}
	
}
