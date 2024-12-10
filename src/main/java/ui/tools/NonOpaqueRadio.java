package ui.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import ui.GUI;

/**
 * A JButtonRadio that is modified to look nicer and have a transparent background.
 */
public class NonOpaqueRadio extends JRadioButton
{
	public NonOpaqueRadio()
	{
		super();
		setPreferredSize(new Dimension(0,0));
		setOpaque(false);
		setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setFocusPainted(false); 
        setContentAreaFilled(false);
	}
	public NonOpaqueRadio(String text)
	{
		super(text);
		setPreferredSize(new Dimension(0,0));
		setOpaque(false);
		setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setFocusPainted(false); 
        setContentAreaFilled(false);
	}
	
	@Override
	protected void paintComponent(Graphics g) {

	    Graphics2D g2 = (Graphics2D) g;
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	    int squareSize = 15; 
	    int padding = 5; 

	    FontMetrics fm = g2.getFontMetrics();
	    int textWidth = fm.stringWidth(getText());
	    int textHeight = fm.getAscent() - fm.getDescent();

	    int contentWidth = squareSize + padding + textWidth;
	    int contentHeight = Math.max(squareSize, textHeight);

	    int startX = (getWidth() - contentWidth) / 2;
	    int startY = (getHeight() - contentHeight) / 2;

	    if (isSelected()) {
	        g2.setColor(GUI.GREEN);
	    } else {
	        g2.setColor(Color.GRAY);
	    }
	    g2.fillRect(startX, startY + (contentHeight - squareSize) / 2, squareSize, squareSize);

	    g2.setColor(Color.BLACK);
	    g2.drawRect(startX, startY + (contentHeight - squareSize) / 2, squareSize, squareSize);

	    g2.setColor(getForeground());
	    int textX = startX + squareSize + padding;
	    int textY = startY + (contentHeight + fm.getAscent() - fm.getDescent()) / 2;
	    g2.drawString(getText(), textX, textY);
	}
	
}
