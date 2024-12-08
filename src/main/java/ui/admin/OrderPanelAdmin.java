package ui.admin;

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

import backend.CustomerProfile;
import backend.Order;
import ui.GUI;
import ui.tools.*;

/**
 * The panel used by the administrator to look at a specific customer's orders.
 */
public class OrderPanelAdmin extends DescendantPanel implements Refreshable
{
	private CustomerProfile customer;
	private JScrollPane scrollPane;
	private ScrollableOrderPanel scrollableOrderPanel;
	
	public OrderPanelAdmin(GUI gui, CustomerProfile customer)
	{
		super(new GridBagLayout(), gui);
		setOpaque(false);
		this.customer = customer;
		
		GridBagConstraints gbc = new GridBagConstraints();
		NonOpaqueJLabel myOrders = new NonOpaqueJLabel(customer.getFirstName() + " " + customer.getName()+"'s orders", SwingUtilities.CENTER);
		myOrders.setFont(new Font("Serif", Font.BOLD, 20));
		myOrders.setPreferredSize(new Dimension(0,0));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.8;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(myOrders, gbc);
		
		RoundedButton back = new RoundedButton("Back");
		back.setBackground(GUI.RED);
		back.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.2;
		gbc.weighty = 0.1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(back, gbc);
		
		//The panel that will contain all the individual panels for each order.
		this.scrollableOrderPanel = new ScrollableOrderPanel(gui);
		scrollPane = new JScrollPane(scrollableOrderPanel);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(0,0));
		scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(GUI.BACKGROUND_ADMIN, GUI.PRODUCT_ADMIN));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.9;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(scrollPane, gbc);
		
		scrollableOrderPanel.displayOrders();
		
		//Resize every individual order panel when the whole scrollable panel is resized.
        scrollPane.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) { 
            	int width = scrollPane.getWidth();
            	for (Component comp : scrollableOrderPanel.getComponents())
            	{
            		if (comp instanceof SingularOrderPanel)
            		{
            			//Resizes the order panel so that it takes a fixed amount of vertical space but all the available horizontal space.
            			comp.setPreferredSize(new Dimension(width - 30, width/3 - 20));
            			((SingularOrderPanel)comp).displayOrder();
            			comp.revalidate();
            		}
            	}
            	scrollableOrderPanel.revalidate();
            	scrollableOrderPanel.repaint();
            }
        });
        
		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				AlternatingCustomersPanel parent = (AlternatingCustomersPanel)SwingUtilities.getAncestorOfClass(AlternatingCustomersPanel.class, back);
				parent.alternatePanels(PanelID.CUSTOMER_INFO);
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
		 * Displays all the orders made by the customer.
		 */
		public void displayOrders()
		{
			removeAll();
			List<Order> orders = gui.getDatabaseManager().getCustomerOrders(customer.getUsername());
			//Max number of orders displayed.
			int count = 20;
		    GridBagConstraints gbc;
		    for (Order order : orders)
		    {
		    	if (count == 0)
		    		break;
		    	gbc = new GridBagConstraints();
		    	
		    	//Creates a new panel for each order made by the customer and adds them from the bottom, starting with the latest one, to get the most recent on top.
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
			setBackground(GUI.PRODUCT_ADMIN);
			displayOrder();
		}
		
		/**
		 * Display some information about a specific order made by the customer.
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
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			add(price, gbc);
			
			
			NonOpaqueTextArea address = new NonOpaqueTextArea("\n Delivery address : "+order.getAddress());
			address.setEditable(false);
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0.66;
			gbc.weighty = 0.33;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
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
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
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
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
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
			gbc.gridx = 2;
			gbc.gridy = 2;
			gbc.weightx = 0.165;
			gbc.weighty = 0.33;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			add(receipt, gbc);
			
			
			gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 2;
			gbc.weightx = 0.165;
			gbc.weighty = 0.33;
			gbc.fill = GridBagConstraints.BOTH;
			add(new NonOpaqueJLabel(), gbc);
			
			
			
			//Button that switches to a receipt panel associated with this order to review the details of the order.
			receipt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					AlternatingCustomersPanel parent = (AlternatingCustomersPanel)SwingUtilities.getAncestorOfClass(AlternatingCustomersPanel.class, receipt);
					parent.alternatePanels(new ReceiptPanelAdmin(gui, order, customer));
				}
			});
		
		}
	}

	//Refreshes the list of the customer's orders in case he made a new one every 30 seconds.
	@Override
	public void refresh() {
		int updatedOrders = gui.getDatabaseManager().updateCustomerOrders(customer.getUsername());
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
