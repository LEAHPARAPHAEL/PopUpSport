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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import backend.Order;
import ui.GUI;
import ui.tools.*;
/**
 * The panel used by the customer to manage their orders.
 */
public class OrderPanel extends DescendantPanel implements Refreshable
{
	private JScrollPane scrollPane;
	private ScrollableOrderPanel scrollableOrderPanel;
	
	public OrderPanel(GUI gui)
	{
		super(new GridBagLayout(), gui);
		//Sets the layout of this panel
		setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		NonOpaqueJLabel myOrders = new NonOpaqueJLabel("My orders");
		myOrders.setFont(new Font("Serif", Font.BOLD, 20));
		myOrders.setPreferredSize(new Dimension(0,0));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.8;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		add(myOrders, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.1;
		gbc.weighty = 1;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		add(new NonOpaqueJLabel(), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0.1;
		gbc.weighty = 1;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		add(new NonOpaqueJLabel(), gbc);
		
		//The panel that will be placed inside the scrollpane and will contain all the individual orders made by the customer.
		this.scrollableOrderPanel = new ScrollableOrderPanel(gui);
		
		scrollPane = new JScrollPane(scrollableOrderPanel);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(0,0));
		scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(GUI.BACKGROUND_CUSTOMER, GUI.PRODUCT_CUSTOMER));
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 0.8;
		gbc.weighty = 0.9;
		gbc.fill = GridBagConstraints.BOTH;
		add(scrollPane, gbc);
		
		scrollableOrderPanel.displayOrders();
		
		//Resizes the individual order panels to take all the horizontal space available but only a fixed proportion of the available height.
        scrollPane.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) { 
            	int width = scrollPane.getWidth();
            	for (Component comp : scrollableOrderPanel.getComponents())
            	{
            		if (comp instanceof SingularOrderPanel)
            		{
            			comp.setPreferredSize(new Dimension(width - 30, width/3 - 20));
            			((SingularOrderPanel)comp).displayOrder();
            			comp.revalidate();
            		}
            	}
            	scrollableOrderPanel.revalidate();
            	scrollableOrderPanel.repaint();
            }
        });
        
	
	}
	
	private class ScrollableOrderPanel extends DescendantPanel
	{
		public ScrollableOrderPanel(GUI gui)
		{
			super(new GridBagLayout(), gui);
			setOpaque(false);
		}
		
		/**
		 * Organizes the layout of the panel to display all the customer's orders.
		 */
		public void displayOrders()
		{
			removeAll();
			
			//All the orders made by the customer, sorted by time of order.
			List<Order> orders = gui.getDatabaseManager().getCustomerOrders("Default");
		    
			//Max number of orders displayed.
			int count = 20;
		    
			GridBagConstraints gbc;
		    
		    //For every order made by the customer, add it from the bottom to have the most recent ones on top.
		    for (Order order : orders)
		    {
		    	if (count == 0)
		    		break;
		    	gbc = new GridBagConstraints();
		    	SingularOrderPanel singularOrderPanel = new SingularOrderPanel(order);
		    	singularOrderPanel.setPreferredSize(new Dimension(0,0));
		    	gbc.gridx = 0;
		    	gbc.gridy = count;
		    	gbc.insets = new Insets(5,5,5,5);
		    	add(singularOrderPanel, gbc);
		    	count--;
		    }
		    revalidate();
		    repaint();
		}
	}
	
	private class SingularOrderPanel extends RoundedPanel
	{
		private Order order;
		private NonOpaqueJLabel status;
		private NonOpaqueTextArea expectedDeliveryTime;
		private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
		
		public SingularOrderPanel(Order order)
		{
			super(new GridBagLayout());
			this.order = order;
			setBackground(GUI.PRODUCT_CUSTOMER);
			displayOrder();
		}
		
		/**
		 * Display the address the order needs to be delivered to, the time it was ordered, the expected time of delivery, and the status of the order.
		 */
		public void displayOrder()
		{
			removeAll();

			NonOpaqueJLabel price = new NonOpaqueJLabel(String.format("<html>Total price : %.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span>.</html>", order.getPrice()), SwingUtilities.CENTER);
			price.setPreferredSize(new Dimension(0,0));
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.weightx = 0.33;
			gbc.weighty = 0.33;
			gbc.gridwidth = 2;
			gbc.insets = new Insets(5,5,5,5);
			gbc.fill = GridBagConstraints.BOTH;
			add(price, gbc);
			
			
			NonOpaqueTextArea address = new NonOpaqueTextArea("\n Delivery address : "+order.getAddress());
			address.setEditable(false);
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0.66;
			gbc.weighty = 0.33;
			gbc.insets = new Insets(5,5,5,5);
			gbc.fill = GridBagConstraints.BOTH;
			add(address, gbc);
			
			
	        LocalDateTime formattedOrderTime = order.getOrderTime().toLocalDateTime();
	        String formattedDate = formattedOrderTime.format(formatter);
			NonOpaqueTextArea orderTime = new NonOpaqueTextArea("\n Order time : "+ formattedDate);
			orderTime.setEditable(false);
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weightx = 0.66;
			gbc.weighty = 0.33;
			gbc.insets = new Insets(5,5,5,5);
			gbc.fill = GridBagConstraints.BOTH;
			add(orderTime, gbc);
			
		
	        LocalDateTime formattedExpectedDeliveryTime = order.getExpectedDeliveryTime().toLocalDateTime();
	        formattedDate = formattedExpectedDeliveryTime.format(formatter);
			this.expectedDeliveryTime = new NonOpaqueTextArea("\n Expected delivery time : "+ formattedDate);
			expectedDeliveryTime.setEditable(false);
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.weightx = 0.66;
			gbc.weighty = 0.33;
			gbc.insets = new Insets(5,5,5,5);
			gbc.fill = GridBagConstraints.BOTH;
			add(expectedDeliveryTime, gbc);
			
			
			this.status = new NonOpaqueJLabel("",SwingUtilities.CENTER);
			status.setText(order.getStatus());
			if (order.getStatus().equals("Delivery in progress"))
				status.setForeground(Color.orange);
			else if (order.getStatus().equals("Delivery completed"))
			{
				status.setForeground(Color.blue);
		        formattedExpectedDeliveryTime = order.getRealDeliveryTime().toLocalDateTime();
		        formattedDate = formattedExpectedDeliveryTime.format(formatter);
		        expectedDeliveryTime.setText("\n Delivered the : "+ formattedDate);
			}
			else
				status.setForeground(Color.red);
			status.setPreferredSize(new Dimension(0,0));
			gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.weightx = 0.33;
			gbc.weighty = 0.33;
			gbc.gridwidth = 2;
			gbc.insets = new Insets(5,5,5,5);
			gbc.fill = GridBagConstraints.BOTH;
			add(status, gbc);
			
			RoundedButton receipt = new RoundedButton("Receipt");
			receipt.setBackground(GUI.BLUE);
			receipt.setPreferredSize(new Dimension(0,0));
			gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 2;
			gbc.weightx = 0.165;
			gbc.weighty = 0.33;
			gbc.insets = new Insets(5,5,5,5);
			gbc.fill = GridBagConstraints.BOTH;
			add(receipt, gbc);
			
			
			RoundedButton orderAgain = new RoundedButton("Order again");
			orderAgain.setBackground(GUI.ORANGE);
			orderAgain.setMargin(new Insets(0,0,0,0));
			orderAgain.setPreferredSize(new Dimension(0,0));
			gbc = new GridBagConstraints();
			gbc.gridx = 2;
			gbc.gridy = 2;
			gbc.weightx = 0.165;
			gbc.weighty = 0.33;
			gbc.insets = new Insets(5,5,5,5);
			gbc.fill = GridBagConstraints.BOTH;
			add(orderAgain, gbc);
			
			//Switches to the basket panel and loads the customer's basket with the order's content
			orderAgain.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					
					gui.getDatabaseManager().setBasket(order.getOrderContent());
					MainCustomerPanel mainCustomerPanel = (MainCustomerPanel)SwingUtilities.getAncestorOfClass(MainCustomerPanel.class, orderAgain);
					if (!gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().hasOngoingReservation())
					{
						mainCustomerPanel.switchPanel(PanelID.BASKET);
					}
				}
			});
			
			//Switches to a receipt panel displaying a receipt for this order
			receipt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					MainCustomerPanel mainCustomerPanel = (MainCustomerPanel)SwingUtilities.getAncestorOfClass(MainCustomerPanel.class, receipt);
					mainCustomerPanel.switchPanel(new ReceiptPanel(order, gui));
				}
			});
		
		}

	}

	@Override
	public void refresh() {
		int updatedOrders = gui.getDatabaseManager().updateCustomerOrders("Default");
		if (updatedOrders > 0)
		{
			scrollableOrderPanel.displayOrders();
			for (ComponentListener componentListener : scrollPane.getViewport().getComponentListeners())
			{
				componentListener.componentResized(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
			}
		}
		

	}
	
}


