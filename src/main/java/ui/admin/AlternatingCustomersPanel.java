package ui.admin;

import java.awt.CardLayout;
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
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import backend.CustomerFilter;
import backend.CustomerProfile;
import ui.GUI;
import ui.tools.*;

/**
 * The panel used by the administrator to manage customers. It can alternate between several panels to avoid generating them every single time, and instead stores them in memory for the administrator to be able to find them as they left them.
 */
public class AlternatingCustomersPanel extends DescendantPanel implements Refreshable
{
	private JPanel otherPanel;
	private JScrollPane scrollPane;
	private ScrollableListOfCustomersPanel customersPanel;
	private CardLayout cardLayout;
	private CustomerFilter filter;
	private CustomerProfile currentCustomer;
	
	public AlternatingCustomersPanel(GUI gui)
	{
		super(gui);
		setOpaque(false);
		//A card layout allows the panel to store different subpanels and switch between them.
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		
		//The panel that will contain the list of all customers matching the selected criteria.
		this.customersPanel = new ScrollableListOfCustomersPanel();
		
		//The other panel that can be either a customer info panel, an order panel, or a receipt panel.
		this.otherPanel = new JPanel();
		
		//A scrollable panel that contains the list of all customers.
		this.scrollPane = new JScrollPane(customersPanel);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(0,0));
	    scrollPane.setBorder(GUI.GRAY_BORDER);
		scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(GUI.BACKGROUND_ADMIN, GUI.PRODUCT_ADMIN));
	    
		add(scrollPane, "customersPanel");
		add(otherPanel, "otherPanel");
		cardLayout.show(this, "customersPanel");
	    
		//A new filter that guarantees that all customers are selected by default.
		this.filter = new CustomerFilter();
		applyFilters(filter);
		
		//Is called when the window is resized to adjust the size of all the panels inside the scrollable panel.
        scrollPane.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) { 
            	int width = scrollPane.getWidth();
            	for (Component comp : customersPanel.getComponents())
            	{
            		//The individual panel should take all the horizontal space available but only a fixed proportion of the vertical space.
            		if (comp instanceof SingularCustomerPanel)
            		{
            			comp.setPreferredSize(new Dimension(width - 30, width/4 - 20));
            			((SingularCustomerPanel)comp).displayCustomer();
            			comp.revalidate();
            		}
            	}
            	customersPanel.revalidate();
            	customersPanel.repaint();
            }
        });
	}
	
	/**
	 * Given the identifier of a panel, switches to the corresponding panel, creating a new one in certain cases or getting its previous state.
	 * @param newPanelID The identifier of the new panel to switch to.
	 */
	public void alternatePanels(PanelID newPanelID) {
		switch (newPanelID) {
		//In this case, don't instantiate a new panel and show the catalog as it as left by the admin.
		case CUSTOMERS:
			applyFilters(filter);
			cardLayout.show(this, "customersPanel");
			break;
		//In this case, creates a new info panel with the specific information about the selected customer.
		case CUSTOMER_INFO:
			remove(otherPanel);
			otherPanel = new CustomerInfoPanel(gui, currentCustomer);
			add(otherPanel, "otherPanel");
			cardLayout.show(this, "otherPanel");
			break;
		//In this case, creates a new panel containing the orders of the selected customer.
		case ORDER:
			remove(otherPanel);
			otherPanel = new OrderPanelAdmin(gui, currentCustomer);
			add(otherPanel, "otherPanel");
			cardLayout.show(this, "otherPanel");
			break;
		default:
			break;
		}
	}

	/**
	 * Given a new panel, switches to this panel.
	 * @param newPanel A panel to switch to.
	 */
	public void alternatePanels(JPanel newPanel) {
		remove(otherPanel);
		otherPanel = newPanel;
		add(otherPanel, "otherPanel");
		cardLayout.show(this, "otherPanel");
	}
	
	public void setCurrentCustomer(CustomerProfile customer)
	{
		this.currentCustomer = customer;
	}
	
	/**
	 * Calls the database manager to select customers in the database matching the desired criteria.
	 * @param filter A filter containing criteria used to filter customers.
	 */
	public void applyFilters(CustomerFilter filter)
	{
		this.filter = filter;
		gui.getDatabaseManager().getCustomersWithFilters(filter);
		customersPanel.displayCustomers();
		for (ComponentListener listener : scrollPane.getViewport().getComponentListeners())
		{
			listener.componentResized(new ComponentEvent(scrollPane.getViewport(), ComponentEvent.COMPONENT_RESIZED));
		};
	}
	
	@Override
	public void refresh()
	{
		applyFilters(filter);
	}
	
	
	private class ScrollableListOfCustomersPanel extends JPanel
	{

		public ScrollableListOfCustomersPanel()
		{
	        super(new GridBagLayout());
	        setOpaque(false);
		}
		
		/**
		 * Displays the list of all customers matching the desired criteria.
		 */
		public void displayCustomers()
		{
			removeAll();
			//Gets from the customer manager the list of all customers matching the criteria.
			List<CustomerProfile> customers = gui.getDatabaseManager().getCustomerManager().getCustomers();
		    GridBagConstraints gbc;
		    int count = 0;
		    
		    //For every customer, creates a small panel with their information that can be clicked to access more details about the customer.
		    for (CustomerProfile customer : customers)
		    {
		    	gbc = new GridBagConstraints();
		    	SingularCustomerPanel singularCustomerPanel = new SingularCustomerPanel(customer);
		    	singularCustomerPanel.setPreferredSize(new Dimension(0,0));
		    	gbc.gridx = 0;
		    	gbc.gridy = count;
		    	gbc.insets = new Insets(5,5,5,5);
		    	add(singularCustomerPanel, gbc);
		    	count++;
		    }
		}
	}
	
	private class SingularCustomerPanel extends RoundedPanel
	{
		private CustomerProfile customer;
		
		public SingularCustomerPanel(CustomerProfile customer) 
		{
			super(new GridBagLayout());
			this.customer = customer;
			setBackground(GUI.PRODUCT_ADMIN);
		}
		
		public void displayCustomer()
		{
			GridBagConstraints gbc = new GridBagConstraints();
			NonOpaqueJLabel name = new NonOpaqueJLabel(customer.getFirstName()+" "+customer.getName());
			name.setPreferredSize(new Dimension(0,0));
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0.5;
			gbc.weighty = 0.5;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			add(name, gbc);
			
			NonOpaqueJLabel username = new NonOpaqueJLabel("Username : " +customer.getUsername());
			username.setPreferredSize(new Dimension(0,0));
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weightx = 0.5;
			gbc.weighty = 0.5;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			add(username, gbc);
			
			RoundedButton customerInfos = new RoundedButton("Details");
			customerInfos.setBackground(GUI.BLUE);
			customerInfos.setPreferredSize(new Dimension(0,0));
			gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.weightx = 0.5;
			gbc.weighty = 0.5;
			gbc.gridwidth = 2;
			gbc.insets = new Insets(5,5,5,5);
			gbc.fill = GridBagConstraints.BOTH;
			add(customerInfos, gbc);
			
			NonOpaqueJLabel credit = new NonOpaqueJLabel(String.format("<html>Credit : %.2f <span style='color:red; font-weight:bold; font-size:10px;'>\u2359</span></html>", customer.getCredit()));
			credit.setPreferredSize(new Dimension(0,0));
			gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.weightx = 0.3;
			gbc.weighty = 0.5;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			add(credit, gbc);
			
			NonOpaqueJLabel vip = new NonOpaqueJLabel(customer.isVip()?"Premium":"", SwingUtilities.CENTER);
			vip.setForeground(GUI.ORANGE);
			vip.setFont(new Font("Serif", Font.BOLD, 20));
			vip.setPreferredSize(new Dimension(0,0));
			gbc = new GridBagConstraints();
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.weightx = 0.2;
			gbc.weighty = 0.5;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			add(vip, gbc);
			
			//A button that can switch to an info panel about the customer.
			customerInfos.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setCurrentCustomer(customer);
					alternatePanels(PanelID.CUSTOMER_INFO);
				}
				
			});
		}
		
	}
}


