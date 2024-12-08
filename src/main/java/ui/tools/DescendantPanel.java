package ui.tools;

import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ui.GUI;

/**
 * A panel that descends (hierarchy wise) from the GUI panel, which means it can make a direct call to the main GUI panel, which itself can call the database manager.
 */
public abstract class DescendantPanel extends JPanel
{
	protected GUI gui;
	
	/**
	 * A panel that descends (hierarchy wise) from the GUI panel, which means it can make a direct call to the main GUI panel, which itself can call the database manager.
	 * @param layoutManager The layout used to create the new panel.
	 * @param gui The reference to the main graphic user interface (GUI).
	 */
	public DescendantPanel(LayoutManager layoutManager, GUI gui)
	{
		super(layoutManager);
		this.gui = gui;
	}
	
	/**
	 * A panel that descends (hierarchy wise) from the GUI panel, which means it can make a direct call to the main GUI panel, which itself can call the database manager.
	 * @param gui The reference to the main graphic user interface (GUI).
	 */
	public DescendantPanel(GUI gui)
	{
		this.gui = gui;
	}
	
	/**
	 * Gets the reference to the main graphic user interface.
	 * @return The graphic user interface that can communicate with the database manager.
	 */
	public GUI getGUI()
	{
		return gui;
	}
}
