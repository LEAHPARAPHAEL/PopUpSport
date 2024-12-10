package ui.tools;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * A JPanel that is modified to have a transparent background.
 */
public class NonOpaquePanel extends JPanel
{
	public NonOpaquePanel()
	{
		super();
		setOpaque(false);
		setPreferredSize(new Dimension(0,0));
	}
	public NonOpaquePanel(LayoutManager layoutManager)
	{
		super(layoutManager);
		setOpaque(false);
		setPreferredSize(new Dimension(0,0));
	}
}
