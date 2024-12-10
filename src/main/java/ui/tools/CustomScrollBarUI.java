package ui.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * A custom UI to add to the default Swing scrollbar to make it look better.
 */
public class CustomScrollBarUI extends BasicScrollBarUI 
{
	public CustomScrollBarUI(Color trackColor, Color thumbColor)
	{
		super();
		this.thumbColor = thumbColor;
		this.trackColor = trackColor;
	}
	
	
    @Override
    protected void configureScrollBarColors() {
        this.thumbDarkShadowColor = Color.GRAY;
        this.thumbHighlightColor = Color.LIGHT_GRAY;
        this.thumbLightShadowColor = Color.WHITE;
        this.trackHighlightColor = Color.GRAY;
    }

    
    @Override
    protected JButton createDecreaseButton(int orientation) 
    {
        return createInvisibleButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation)
    {
        return createInvisibleButton();
    }

    private JButton createInvisibleButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(thumbColor);
        g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);

        g2.setColor(Color.BLACK);
        g2.drawRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(trackColor);
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);

        g2.setColor(Color.GRAY);
        g2.drawRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }
}
