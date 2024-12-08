package ui.tools;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLayeredPane;

public class InfoPanel extends JLayeredPane
{
	
	public InfoPanel() 
	{
		super();
		
		addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent e)
			{
				for (Component comp : getComponents())
				{
					comp.setBounds(0, 0, getWidth(), getHeight());
				}
				revalidate();
				repaint();
			}
		});
	}
	
	public void addComponent(Component comp)
	{
		int maxLayer = Integer.MIN_VALUE;
		for (Component component : getComponents())
		{
			maxLayer = Math.max(maxLayer, getLayer(component));
		}
		
		int newLayer = (maxLayer == Integer.MIN_VALUE) ? 0 : maxLayer + 1;
		
		add(comp, Integer.valueOf(newLayer));
		comp.setBounds(0, 0, getWidth(), getHeight());
		revalidate();
		repaint();
	}
	
	public void removeComponent(Component comp)
	{
		remove(comp);
		revalidate();
		repaint();
	}
	
	public boolean isEmpty()
	{
		return getComponents().length == 0;
	}
	
	
}
