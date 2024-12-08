package ui.tools;

import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.JTextArea;

public class NonOpaqueTextArea extends JTextArea
{
	public NonOpaqueTextArea()
	{
		super();
		setPreferredSize(new Dimension(0,0));
		setLineWrap(true);
		setWrapStyleWord(true);
		setOpaque(false);
	}
	
	public NonOpaqueTextArea(String text)
	{
		super(text);
		setPreferredSize(new Dimension(0,0));
		setLineWrap(true);
		setWrapStyleWord(true);
		setOpaque(false);
	}
}
