package ui.customer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Map.Entry;

import javax.swing.JButton;
import ui.tools.*;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import ui.GUI;

/**
 * The panel used by the customer to manage their basket.
 */
public class BasketPanel extends DescendantPanel implements Refreshable
{
	private BasketScrollablePanel basketScrollablePanel;
	private JScrollPane scrollPane;
	private NonOpaqueJLabel totalPrice;
	
	public BasketPanel(GUI gui)
	{
		super(new GridBagLayout(), gui);
		setOpaque(false);
		//The panel that will be inside the scrollpane to display all the products present in the customer's basket.
		this.basketScrollablePanel = new BasketScrollablePanel();
		
		//The scrollable panel containing the panel containing itself the products present in the customer's basket.
		this.scrollPane = new JScrollPane(basketScrollablePanel);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(0,0));
		scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(GUI.BACKGROUND_CUSTOMER, GUI.PRODUCT_CUSTOMER));
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.weightx = 0.6;
	    gbc.weighty = 1;
	    gbc.gridheight = 5;
	    gbc.fill = GridBagConstraints.BOTH;
	    add(scrollPane, gbc);
	    
	    basketScrollablePanel.displayBasketItems();
	    
	    //Right side of the panel : total price of the basket and button to pay.
	    NonOpaqueJLabel myBasket = new NonOpaqueJLabel("My basket", SwingConstants.CENTER);
	    myBasket.setFont(new Font("Serif", Font.BOLD, 25));
	    gbc = new GridBagConstraints();
	    gbc.gridx = 1;
	    gbc.gridy = 0;
	    gbc.weightx = 0.4;
	    gbc.weighty = 0.2;
	    gbc.gridwidth = 3;
	    gbc.fill = GridBagConstraints.BOTH;
	    add(myBasket, gbc);
	    
	    gbc = new GridBagConstraints();
	    this.totalPrice = new NonOpaqueJLabel(String.format("<html>Total price : %.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span>.</html>",gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getBasketManager().getTotalPrice()), SwingUtilities.CENTER);
	    totalPrice.setPreferredSize(new Dimension(0,0));
	    gbc.gridx = 1;
	    gbc.gridy = 2;
	    gbc.weightx = 0.4;
	    gbc.weighty = 0.2;
	    gbc.gridwidth = 3;
	    gbc.fill = GridBagConstraints.BOTH;
	    add(totalPrice, gbc);
	    
	    gbc = new GridBagConstraints();
	    NonOpaqueJLabel credit = new NonOpaqueJLabel(String.format("<html>Your credit : %.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span>.</html>",gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getCredit()), SwingUtilities.CENTER);
	    credit.setPreferredSize(new Dimension(0,0));
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    gbc.weightx = 0.4;
	    gbc.weighty = 0.1;
	    gbc.gridwidth = 3;
	    gbc.fill = GridBagConstraints.BOTH;
	    add(credit, gbc);
	    
	    gbc = new GridBagConstraints();
	    gbc.gridx = 1;
	    gbc.gridy = 3;
	    gbc.weightx = 0.1;
	    gbc.weighty = 0.1;
	    gbc.fill = GridBagConstraints.BOTH;
	    add(new NonOpaqueJLabel(), gbc);
	    
	    RoundedButton next = new RoundedButton("Next");
	    next.setBackground(GUI.BLUE);
	    next.setPreferredSize(new Dimension(0,0));
	    gbc = new GridBagConstraints();
	    gbc.gridx = 2;
	    gbc.gridy = 3;
	    gbc.weightx = 0.2;
	    gbc.weighty = 0.1;
	    gbc.fill = GridBagConstraints.BOTH;
	    add(next, gbc);

	    gbc = new GridBagConstraints();
	    gbc.gridx = 3;
	    gbc.gridy = 3;
	    gbc.weightx = 0.1;
	    gbc.weighty = 0.1;
	    gbc.fill = GridBagConstraints.BOTH;
	    add(new NonOpaqueJLabel(), gbc);
	    
	    gbc = new GridBagConstraints();
	    NonOpaqueTextArea errorArea = new NonOpaqueTextArea();
	    errorArea.setEditable(false);
	    errorArea.setForeground(Color.RED);
	    gbc.gridx = 1;
	    gbc.gridy = 4;
	    gbc.weightx = 0.4;
	    gbc.weighty = 0.4;
	    gbc.gridwidth = 3;
	    gbc.fill = GridBagConstraints.BOTH;
	    add(errorArea, gbc);
	    
	    //Tries to switch to the payment and shipment part of the order if the customer has enough credit and all the products in the basket are available in the desired quantities.
	    next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainCustomerPanel mainCustomerPanel = (MainCustomerPanel)SwingUtilities.getAncestorOfClass(MainCustomerPanel.class, next);
				//Not enough credit to buy the content of this basket
			    if (gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getCredit() < gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getBasketManager().getTotalPrice())
				{
					mainCustomerPanel.showInfoPanelForTheNext(10, "Insufficient credit. You can get more cones by publishing achievements in My account > Achievements");
					return;
				}
				
				//If there is at least one product in the basket.
				if (gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getBasketManager().getBasketSize()>0)
				{
					//An array of two integers indicating how many products are not available anymore and how many have been reserved by other customers.
					int[] successFlags = gui.getDatabaseManager().checkIfBasketAvailable();
					
					//No problem with the quantities, so we proceed with the reservation.
					if (successFlags[0] == 0 && successFlags[1] == 0)
					{
						
						mainCustomerPanel.beginReservation();
					}
						
					else
					{
						//No products have been set to unavailable, but some have been reserved
						if (successFlags[0] == 0)
						{
							errorArea.setText(String.format("%d products are not available anymore for this quantity. They have either been bought or reserved for buying by other customers.", successFlags[1]));
						}
						//No products have been reserved but some are not available anymore.
						else if (successFlags[1] == 0)
						{
							errorArea.setText(String.format("%d products are not available anymore for purchasing", successFlags[0]));
						}
						//Some products are not available anymore and some have been reserved.
						else
							errorArea.setText(String.format("%d products have been reserved or bought by other customers. %d products are not available anymore for purchasing.", successFlags[0], successFlags[1]));
						
						basketScrollablePanel.propagateStockConstraints();
						totalPrice.setText(String.format("<html>Total price : %.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span>.</html>",gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getBasketManager().getTotalPrice()));
						
					}
					
				}
			}
	    	
	    });
	    
	    
	    //Resizes the individual basket item panels to take all the horizontal space but a fixed proportion of the vertical one.
        scrollPane.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) { 
            	int width = scrollPane.getWidth();
            	for (Component comp : basketScrollablePanel.getComponents())
            	{
            		if (comp instanceof BasketItemPanel)
            		{
            			comp.setPreferredSize(new Dimension(width-20, width/2-20));
            			((BasketItemPanel)comp).displayProduct();
            			comp.revalidate();
            		}
            	}
            	basketScrollablePanel.revalidate();
            	basketScrollablePanel.repaint();
            }
        });

	}
	
	private class BasketScrollablePanel extends JPanel
	{
		
		public BasketScrollablePanel()
		{
			super(new GridBagLayout());
			setOpaque(false);
		}
		
		/**
		 * Organizes the layout to display all the products in the customer's basket.
		 */
		public void displayBasketItems()
		{
				removeAll();
				GridBagConstraints scrollableGbc;
			    int count = 0;
			    
			    //For every product, an individual panel is created containing the information about the product.
			    for (Entry<Integer, Integer> entry :gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getBasketManager().getBasket().entrySet())
			    {
			    	scrollableGbc = new GridBagConstraints();
			    	BasketItemPanel itemPanel = new BasketItemPanel(entry.getKey(), entry.getValue(), gui);
			    	itemPanel.setPreferredSize(new Dimension(0,0));
			    	scrollableGbc.gridx = 0;
			    	scrollableGbc.gridy = count;
			    	scrollableGbc.insets = new Insets(5,5,5,5);
			    	add(itemPanel, scrollableGbc);
			    	count++;
			    }
				
		        for (ComponentListener listener : scrollPane.getViewport().getComponentListeners())
		        {
		        	listener.componentResized(new ComponentEvent(scrollPane.getViewport(), ComponentEvent.COMPONENT_RESIZED));
		        }
				
			
		}
		/**
		 * For every product in the customer's basket, check if the desired quantity is still available or if the stock for this product has decreased.
		 */
		public void propagateStockConstraints()
		{
			for (Component comp : getComponents())
			{
				if (comp instanceof BasketItemPanel)
				{
					((BasketItemPanel)comp).onCheckedIfProductAvailable();

				}

			}
		}
	}
	
	/**
	 * Updates the total price of the basket.
	 */
	public void notifyChanges()
	{
		totalPrice.setText(String.format("<html>Total price : %.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span>.</html>",gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getBasketManager().getTotalPrice()));
	}
	
	/**
	 * When a product is removed, reorganizes the displayed products and updates the total price of the basket.
	 */
	public void notifyRemoval()
	{
		basketScrollablePanel.displayBasketItems();
		notifyChanges();
	}

	@Override
	public void refresh() {
		notifyChanges();
	}

}
