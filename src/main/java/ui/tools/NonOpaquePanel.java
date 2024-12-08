package ui.tools;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;

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
