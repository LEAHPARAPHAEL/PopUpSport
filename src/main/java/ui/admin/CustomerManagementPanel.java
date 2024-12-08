package ui.admin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import backend.CustomerFilter;
import ui.GUI;
import ui.tools.*;

/**
 * The panel used by the administrator to manage the customers using the application.
 */
public class CustomerManagementPanel extends DescendantPanel
{
	private FilterPanel filterPanel;
	private AdminSearchPanel searchPanel;
	private AlternatingCustomersPanel customersPanel;
	private CustomerFilter customerFilter;
	
	public CustomerManagementPanel(GUI gui)
	{
		super(new GridBagLayout(), gui);
		setOpaque(false);
		//The panel allowing the admin to select various filters for the customer.
		this.filterPanel = new FilterPanel();
		
		//The panel allowing the admin to type keywords to filter customers.
		this.searchPanel = new AdminSearchPanel();
		
		//The panel on the bottom right of the screen that can alternate between a catalog panel, or a detailed panel about a specific product.
		this.customersPanel = new AlternatingCustomersPanel(gui);
		
		//A new filter that guarantees all customers are displayed at first.
		this.customerFilter = new CustomerFilter();
		
        GridBagConstraints gbc = new GridBagConstraints();
        //Adds the filter panel to the left side of the main panel
        filterPanel.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .3;
        gbc.weighty = 1;
        gbc.insets = new Insets(5,5,5,5);
        add(filterPanel, gbc);
        
        //Adds the search panel to the right side of the main panel
        searchPanel.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .7;
        gbc.weighty = .1;
        gbc.insets = new Insets(5,5,5,5);
        add(searchPanel, gbc);

        
        //Adds the catalog panel to the main panel
        customersPanel.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .7;
        gbc.weighty = .9;
        gbc.insets = new Insets(5,5,5,5);
        add(customersPanel, gbc);
	}
	
	/**
	 * Passes the current filter to the next panel, until the last panel responsible for calling the database manager and displaying the customers.
	 */
	public void propagateFilters()
	{
		customersPanel.applyFilters(customerFilter);
	}
	

	/**
	 * Asks its alternating panel to switch panel with a panel corresponding to the given identifier.
	 * @param nextPanelID The identifier of the new panel the alternating panel needs to switch to.
	 */
	public void propagateCatalogAlternate(PanelID nextPanelID)
	{
		customersPanel.alternatePanels(nextPanelID);
	}
	
	/**
	 * The panel used by the administrator to select various filters narrowing down their search.
	 */
	private class FilterPanel extends JPanel
	{
		public FilterPanel()
		{
			super(new GridBagLayout());
	        setBorder(GUI.GRAY_BORDER);
	        setOpaque(false);
	        //Adds a filter label
	        NonOpaqueJLabel filters = new NonOpaqueJLabel("Filters");
	        filters.setFont(new Font("Serif", Font.BOLD, 20));
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.2;
	        gbc.gridwidth = 2;
	        gbc.insets = new Insets(5,5,5,5);
	        add(filters, gbc);
	        
	        //A panel allowing the admin to choose a minimum credit for the customer to be displayed.
	        NonOpaquePanel sliderPanel = new NonOpaquePanel(new GridBagLayout());
	        sliderPanel.setPreferredSize(new Dimension(0,0));
	        
	        NonOpaqueJLabel selectCredit = new NonOpaqueJLabel("Select a minimum credit");
	        selectCredit.setPreferredSize(new Dimension(0,0));
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.4;
	        gbc.gridwidth = 2;
	        gbc.insets = new Insets(5,5,5,5);
	        sliderPanel.add(selectCredit, gbc);
	        
	        JSlider creditSlider = new SliderGradient(0, 10000, 0, GUI.BLUE, GUI.PRODUCT_CUSTOMER);
	        creditSlider.setMajorTickSpacing(5000);
	        creditSlider.setMinorTickSpacing(1000);
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 1;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.6;
	        gbc.gridwidth = 2;
	        sliderPanel.add(creditSlider, gbc);
	        
	        creditSlider.addChangeListener(new ChangeListener() {
	            @Override
	            public void stateChanged(ChangeEvent e) 
	            {
	            	customerFilter.setCredit(creditSlider.getValue());
	            	propagateFilters();
	            }
	        });

	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 1;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.15;
	        gbc.gridwidth = 2;
	        add(sliderPanel, gbc);

	        //A group of radio buttons used to choose if the customers should be ordered by name.
			NonOpaqueRadio orderByName = new NonOpaqueRadio("Yes");
			orderByName.setPreferredSize(new Dimension(0,0));
			NonOpaqueRadio dontOrderByName = new NonOpaqueRadio("No");
			dontOrderByName.setPreferredSize(new Dimension(0,0));
			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroup.add(orderByName);
			buttonGroup.add(dontOrderByName);
			dontOrderByName.setSelected(true);
			
			orderByName.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					customerFilter.setOrderByName(true);
					propagateFilters();
				}
			});
			
			dontOrderByName.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					customerFilter.setOrderByName(false);
					propagateFilters();
				}
			});
			
			NonOpaqueJLabel orderByNameLabel = new NonOpaqueJLabel("Order by name");
			orderByNameLabel.setPreferredSize(new Dimension(0,0));
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.weightx = 1;
			gbc.weighty = 0.05;
			gbc.gridwidth = 2;
			gbc.insets = new Insets(5,5,5,5);
			gbc.fill = GridBagConstraints.BOTH;
			add(orderByNameLabel, gbc);
			
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.weightx = 0.5;
			gbc.weighty = 0.05;
			gbc.gridwidth = 1;
			gbc.insets = new Insets(5,5,5,5);
			gbc.fill = GridBagConstraints.BOTH;
			add(orderByName, gbc);
			
			gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 3;
			gbc.weightx = 0.5;
			gbc.weighty = 0.05;
			gbc.gridwidth = 1;
			gbc.insets = new Insets(5,5,5,5);
			gbc.fill = GridBagConstraints.BOTH;
			add(dontOrderByName, gbc);
	        
			/*
			 * A group of radio buttons used to choose if the customers should be ordered by credit.
			 * If so, credit takes precedence over name to order the customers.
			 */
			NonOpaqueRadio orderByCredit = new NonOpaqueRadio("Yes");
			orderByCredit.setPreferredSize(new Dimension(0,0));
			NonOpaqueRadio dontOrderByCredit = new NonOpaqueRadio("No");
			dontOrderByCredit.setPreferredSize(new Dimension(0,0));
			ButtonGroup buttonGroupCredit = new ButtonGroup();
			buttonGroupCredit.add(orderByCredit);
			buttonGroupCredit.add(dontOrderByCredit);
			dontOrderByCredit.setSelected(true);
			
			orderByCredit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					customerFilter.setOrderByCredit(true);
					propagateFilters();
				}
			});
			
			dontOrderByCredit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					customerFilter.setOrderByCredit(false);
					propagateFilters();
				}
			});
			
			NonOpaqueJLabel orderByCreditLabel = new NonOpaqueJLabel("Order by credit");
			orderByCreditLabel.setPreferredSize(new Dimension(0,0));
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 4;
			gbc.weightx = 1;
			gbc.weighty = 0.05;
			gbc.gridwidth = 2;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			add(orderByCreditLabel, gbc);
			
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 5;
			gbc.weightx = 0.5;
			gbc.weighty = 0.05;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			add(orderByCredit, gbc);
			
			gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 5;
			gbc.weightx = 0.5;
			gbc.weighty = 0.05;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			add(dontOrderByCredit, gbc);
			
			//A panel used to determine if the displayed customers should be vips or not.
			NonOpaquePanel vipPanel = new NonOpaquePanel(new GridBagLayout());
			vipPanel.setPreferredSize(new Dimension(0,0));
			
			NonOpaqueRadio all = new NonOpaqueRadio("All");
			all.setPreferredSize(new Dimension(0,0));
			NonOpaqueRadio vipY = new NonOpaqueRadio("Yes");
			vipY.setPreferredSize(new Dimension(0,0));
			NonOpaqueRadio vipN = new NonOpaqueRadio("No");
			vipN.setPreferredSize(new Dimension(0,0));
			ButtonGroup buttonGroupVIP = new ButtonGroup();
			buttonGroupVIP.add(vipY);
			buttonGroupVIP.add(vipN);
			buttonGroupVIP.add(all);
			all.setSelected(true);
			
			all.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					customerFilter.setVip(0);
					propagateFilters();
				}
			});
			
			vipY.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					customerFilter.setVip(1);
					propagateFilters();
				}
			});
			
			vipN.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					customerFilter.setVip(-1);
					propagateFilters();
				}
			});
			
			NonOpaqueJLabel vip = new NonOpaqueJLabel("Premium");
			vip.setPreferredSize(new Dimension(0,0));
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1;
			gbc.weighty = 0.5;
			gbc.gridwidth = 3;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			vipPanel.add(vip, gbc);
			
			gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.weightx = 0.33;
			gbc.weighty = 0.5;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			vipPanel.add(vipY, gbc);
			
			gbc = new GridBagConstraints();
			gbc.gridx = 2;
			gbc.gridy = 1;
			gbc.weightx = 0.33;
			gbc.weighty = 0.5;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			vipPanel.add(vipN, gbc);
			
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weightx = 0.33;
			gbc.weighty = 0.5;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			vipPanel.add(all, gbc);
			
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 6;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.1;
	        gbc.gridwidth = 2;
	        gbc.insets = new Insets(5,5,5,5);
	        add(vipPanel, gbc);
	        
	        		
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 7;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.35;
	        gbc.gridwidth = 2;
	        gbc.insets = new Insets(5,5,5,5);
	        add(new NonOpaqueJLabel(), gbc);
	        
	        
		}
	}
	
	/**
	 * The panel used by the administrator to search for a specific customer or apply keywords to narrow down their search.
	 */
	private class AdminSearchPanel extends JPanel
	{
		private JTextField searchBar;
		
		public AdminSearchPanel()
		{
			super(new GridBagLayout());
			setOpaque(false);
	        setBorder(GUI.GRAY_BORDER);
	        
	        NonOpaqueJLabel searchSection = new NonOpaqueJLabel("Find a customer by name, first name or username.");
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.6;
	        gbc.insets = new Insets(5,5,5,5);
	        add(searchSection, gbc);
	        

	        this.searchBar = new JTextField(20);
	        searchBar.setBorder(new LineBorder(Color.LIGHT_GRAY));
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 1;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.4;
	        gbc.insets = new Insets(5,5,5,5);
	        add(searchBar, gbc);
	        
	        //Every time a letter is typed or removed, a call is made to the database manager to select the customers matching these keywords.
	        searchBar.getDocument().addDocumentListener(new DocumentListener() {
	            @Override
	            public void insertUpdate(DocumentEvent e) {
	                propagateKeywords();
	            }

	            @Override
	            public void removeUpdate(DocumentEvent e) {
	            	propagateKeywords();
	            }

	            @Override
	            public void changedUpdate(DocumentEvent e) {
	            	propagateKeywords();
	            }});
	        
		}
		
		/**
		 * Sets the keywords of the current filter to be the ones typed in the search bar, and asks the parent panel to pass the modified filter to the panel responsible for displaying customers.
		 */
		public void propagateKeywords()
		{
			customerFilter.setKeywords(searchBar.getText());
			propagateFilters();
		}
	}
	
	
}
