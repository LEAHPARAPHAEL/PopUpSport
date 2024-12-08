package ui.tools;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import ui.GUI;

/**
 * Extension of a Swing JButton that can easily make the GUI switch to a different panel.
 */
public class SwitchingButton extends RoundedButton
{
	private JButton source;
	
	public SwitchingButton(String name, PanelID nextPanelID)
	{
		super(name);
		this.source = this;
		
		//Adds an action listener to enable the user to make the GUI switch panels just by clicking this button.
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		    	GUI gui = (GUI) SwingUtilities.getAncestorOfClass(GUI.class, source);
		    	if (gui != null)
		    		gui.switchPanel(nextPanelID);
				
			}
		});
		
	}
	

}
