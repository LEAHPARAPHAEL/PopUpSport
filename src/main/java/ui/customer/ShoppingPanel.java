package ui.customer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
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

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import backend.ProductFilter;
import ui.GUI;
import ui.tools.*;

/**
 * The main shopping panel used by the customer to navigate the catalog and filter products.
 */
public class ShoppingPanel extends DescendantPanel
{
	private FilterPanel filterPanel;
	private SearchPanel searchPanel;
	private AlternatingCatalogPanel catalogPanel;
	private ProductFilter productFilter;
	
	public ShoppingPanel(GUI gui)
	{
		super(new GridBagLayout(), gui);
		/*
		 * A new product filter that contains all the criteria used to filter product from the database.
		 * By default, it is initialized so that all available products are displayed.
		 */
		productFilter = new ProductFilter();
        GridBagConstraints gbc = new GridBagConstraints();
        setOpaque(false);
        //A filter panel which can be used to filter products and that is displayed on the left side of the screen.
        filterPanel = new FilterPanel(gui);
        filterPanel.setPreferredSize(new Dimension(0,0));
        filterPanel.setBackground(GUI.BACKGROUND_CUSTOMER);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .3;
        gbc.weighty = 1;
        gbc.insets = new Insets(5,5,5,5);
        add(filterPanel, gbc);
        gui.getDatabaseManager().getProductsWithOrKeywords(productFilter);
        
        
        //A search panel which can be used by the customer to search for specific products and apply keywords and that is displayed on the top right corner of the screen.
        searchPanel = new SearchPanel();
        searchPanel.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .7;
        gbc.weighty = .1;
        gbc.insets = new Insets(5,5,5,5);
        add(searchPanel, gbc);


        //The alternating panel that either displays all the products or displays the properties of one product, displayed on the bottom right corner of the screen.
        catalogPanel = new AlternatingCatalogPanel(gui);
        catalogPanel.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .7;
        gbc.weighty = .8;
        gbc.insets = new Insets(5,5,5,5);
        add(catalogPanel, gbc);
        
        
	}
	
	/**
	 * Applies new keywords that are going to be used to filter products, either exclusively or inclusively.
	 * @param keywords The line of keywords that is going to be added. If several words are separated with commas or blank spaces, the line will be divided into as many singular keywords.
	 */
	public void applyNewKeywords(String keywords)
	{
		for (String keyword : keywords.split("[,; |]+"))
		{
			//Adds the keyword if it is not already there and if it is longer than 2 letters.
			if (!productFilter.hasKeyword(keyword) && keyword.length() > 2)
				productFilter.addOrKeyword(keyword);
		}
		//Recomputes the list of products matching the applied keywords after the addition of these ones.
		gui.getDatabaseManager().getProductsWithOrKeywords(productFilter);
		filterPanel.display();
	}
	
	/**
	 * Remove a single keyword from the list of keywords that have been applied.
	 * @param keyword The keyword to remove.
	 */
	public void removeKeyword(String keyword)
	{
		productFilter.removeKeyword(keyword);
		gui.getDatabaseManager().getProductsWithOrKeywords(productFilter);
		catalogPanel.propagateFilters(productFilter);
	}
	
	
	/**
	 * Passes the filter to the concerned panel, until it reaches the panel responsible for calling the database manager and displaying products.
	 */
	public void propagateFilters()
	{
		catalogPanel.propagateFilters(productFilter);
	}
	
	/**
	 * Gets the product filter containing the criteria used to filter products.
	 * @return The product filter object containing the criteria and keywords necessary to filter products in database select queries.
	 */
	public ProductFilter getProductFilter()
	{
		return this.productFilter;
	}
	
	
	/**
	 * The panel enabling the customer to search for specific products or filter them by applying keywords.
	 */
	private class SearchPanel extends JPanel
	{
		private JTextField searchBar;
		
		public SearchPanel()
		{
			super(new GridBagLayout());
			setBackground(GUI.BACKGROUND_CUSTOMER);
	        setBorder(new LineBorder(Color.LIGHT_GRAY));
	        
	        //Layout of the search panel
	        
	        NonOpaqueJLabel searchSection = new NonOpaqueJLabel("Find a product by name, brand, decription or keyword.");
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.6;
	        gbc.gridwidth = 2;
	        gbc.insets = new Insets(5,5,5,5);
	        add(searchSection, gbc);
	        

	        this.searchBar = new JTextField(20);
	        searchBar.setBorder(new LineBorder(Color.LIGHT_GRAY));
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 1;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 0.8;
	        gbc.weighty = 0.4;
	        gbc.insets = new Insets(5,5,5,5);
	        add(searchBar, gbc);
	        
	        RoundedButton searchButton = new RoundedButton("Apply");
	        searchButton.setBackground(GUI.BLUE);
	        gbc = new GridBagConstraints();
	        gbc.gridx = 1;
	        gbc.gridy = 1;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 0.2;
	        gbc.weighty = 0.4;
	        gbc.insets = new Insets(5,5,5,5);
	        add(searchButton, gbc);
	        
	        //Every time a letter is typed or removed, gets the filtered products with this line of keywords and display them.
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
	        
	        //Apply these keywords to the list of active keywords.
	        searchButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String keywords = searchBar.getText();
					if (keywords.length() > 2)
					{
						applyNewKeywords(keywords);
						searchBar.setText("");
					}
				}
	        	
	        });
		}
		
		/**
		 * Sets the keywords in the filter to be the line of keywords typed in the search bar.
		 */
		public void propagateKeywords()
		{
			productFilter.setKeywords(searchBar.getText());
			propagateFilters();
		}
	}
	
	
	
	/**
	 * The panel enabling the customer to select various filters to display the desired products.
	 */
	private class FilterPanel extends DescendantPanel
	{
		private JScrollPane scrollPane;
		private KeywordsPanel keywordsPanel;
		
	    public FilterPanel(GUI gui)
	    {
	    	super(new GridBagLayout(), gui);
	        setBorder(new LineBorder(Color.LIGHT_GRAY));
	        //Adds a filter label
	        NonOpaqueJLabel filters = new NonOpaqueJLabel("Filters");
	        filters.setPreferredSize(new Dimension(0,0));
	        filters.setFont(new Font("Serif", Font.BOLD, 20));
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.1;
	        add(filters, gbc);
	        
	        //Adds a slider panel to select the maximum price
	        NonOpaquePanel sliderPanel = new NonOpaquePanel(new GridBagLayout());
	        sliderPanel.setPreferredSize(new Dimension(0,0));
	        
	        NonOpaqueJLabel selectPrice = new NonOpaqueJLabel("Select a maximum price");
	        selectPrice.setPreferredSize(new Dimension(0,0));
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.4;
	        sliderPanel.add(selectPrice, gbc);
	        
	        JSlider priceSlider = new SliderGradient(0, 20000, 20000, GUI.BLUE, GUI.PRODUCT_CUSTOMER);
	        priceSlider.setMajorTickSpacing(20000);
	        priceSlider.setMinorTickSpacing(2000);
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 1;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.6;
	        sliderPanel.add(priceSlider, gbc);
	        
	        //Every time the maximum price is changed by the customer, gets the filtered product in real time.
	        priceSlider.addChangeListener(new ChangeListener() {
	            @Override
	            public void stateChanged(ChangeEvent e) 
	            {
					productFilter.setBudget(priceSlider.getValue());
					propagateFilters();
	            }
	        });

	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 1;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.15;
	        add(sliderPanel, gbc);
	        
	        
	        
	        NonOpaquePanel discountPanel = new NonOpaquePanel(new GridBagLayout());
	        sliderPanel.setPreferredSize(new Dimension(0,0));
	        
	        NonOpaqueJLabel selectDiscount = new NonOpaqueJLabel("Minimum discount percentage");
	        selectDiscount.setPreferredSize(new Dimension(0,0));
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.4;
	        discountPanel.add(selectDiscount, gbc);
	        
	        JSlider discountSlider =  new SliderGradient(0, 100, 0, GUI.BLUE, GUI.PRODUCT_CUSTOMER);
	        discountSlider.setMajorTickSpacing(50);
	        discountSlider.setMinorTickSpacing(10);
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 1;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.6;
	        discountPanel.add(discountSlider, gbc);
	        
	        //Every time the minimum discount is modified by the customer, get the filtered products in real time.
	        discountSlider.addChangeListener(new ChangeListener() {
	            @Override
	            public void stateChanged(ChangeEvent e) 
	            {
					productFilter.setDiscount(discountSlider.getValue());
					propagateFilters();
	            }
	        });

	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 2;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.15;
	        gbc.insets = new Insets(0,0,5,0);
	        add(discountPanel, gbc);
	        
	        
	        NonOpaquePanel andOrPanel = new NonOpaquePanel(new GridBagLayout());
	        NonOpaqueJLabel keywordsLabel = new NonOpaqueJLabel("Keywords");
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 0.5;
	        gbc.weighty = 1;
	        andOrPanel.add(keywordsLabel, gbc);
	        
	        NonOpaqueRadio or = new NonOpaqueRadio("Or");
			or.setPreferredSize(new Dimension(0,0));
			NonOpaqueRadio and = new NonOpaqueRadio("And");
			and.setPreferredSize(new Dimension(0,0));
			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroup.add(or);
			buttonGroup.add(and);
			or.setSelected(productFilter.isOr());
			
			gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.weightx = 0.25;
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			andOrPanel.add(or, gbc);
			
			gbc = new GridBagConstraints();
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.weightx = 0.25;
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			andOrPanel.add(and, gbc);
			
			
			NonOpaquePanel orderByPricePanel = new NonOpaquePanel(new GridBagLayout());
			orderByPricePanel.setPreferredSize(new Dimension(0,0));
			NonOpaqueRadio orderByPrice = new NonOpaqueRadio("Yes");
			orderByPrice.setPreferredSize(new Dimension(0,0));
			NonOpaqueRadio dontOrderByPrice = new NonOpaqueRadio("No");
			dontOrderByPrice.setPreferredSize(new Dimension(0,0));
			ButtonGroup buttonGroupCredit = new ButtonGroup();
			buttonGroupCredit.add(orderByPrice);
			buttonGroupCredit.add(dontOrderByPrice);
			if (productFilter.isOrderByPrice())
				orderByPrice.setSelected(true);
			else
				dontOrderByPrice.setSelected(true);
			
			orderByPrice.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					productFilter.setOrderByPrice(true);
					propagateFilters();
				}
			});
			
			dontOrderByPrice.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					productFilter.setOrderByPrice(false);
					propagateFilters();
				}
			});
			
			NonOpaqueJLabel orderByPriceLabel = new NonOpaqueJLabel("Order by price");
			orderByPriceLabel.setPreferredSize(new Dimension(0,0));
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.gridwidth = 2;
			gbc.fill = GridBagConstraints.BOTH;
			orderByPricePanel.add(orderByPriceLabel, gbc);
			
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weightx = 0.5;
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			orderByPricePanel.add(orderByPrice, gbc);
			
			gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.weightx = 0.5;
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			orderByPricePanel.add(dontOrderByPrice, gbc);
			
			
			gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 3;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.1;
	        gbc.insets = new Insets(0,0,15,0);
			add(orderByPricePanel, gbc);

	        andOrPanel.setPreferredSize(new Dimension(0,0));
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 5;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.1;
	        add(andOrPanel, gbc);
	        
	   
	        this.keywordsPanel = new KeywordsPanel();
	        this.scrollPane = new JScrollPane(keywordsPanel);
		    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setPreferredSize(new Dimension(0,0));
	        scrollPane.setOpaque(false);
	        scrollPane.getViewport().setOpaque(false);
	        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(GUI.BACKGROUND_CUSTOMER, GUI.PRODUCT_CUSTOMER));

			gbc = new GridBagConstraints();
			gbc.gridx = 0;
	        gbc.gridy = 6;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.3;
			add(scrollPane, gbc);
	        
			//The list of all possible colors that can be selected by the customer.
			ColorInfo[] colors = new ColorInfo[] {
					new ColorInfo(Color.red, "red"),
					new ColorInfo(Color.pink, "pink"),
					new ColorInfo(Color.orange, "orange"),
					new ColorInfo(Color.yellow, "yellow"),
					new ColorInfo(Color.green, "green"),
					new ColorInfo(Color.cyan, "cyan"), 
					new ColorInfo(Color.blue, "blue"),
					new ColorInfo(Color.magenta, "magenta"), 
					new ColorInfo(Color.lightGray, "lightGray"), 
					new ColorInfo(Color.gray, "gray"), 
					new ColorInfo(Color.black, "black"),
					new ColorInfo(Color.white, "white"),
			};
			
			NonOpaquePanel colorPanel = new NonOpaquePanel(new GridBagLayout());
			
			//For every color in the above list, creates a small square button representing this color that can be either selected or unselected.
			int count = 0;
			for (ColorInfo color : colors)
			{
				RoundedButton colorButton = new RoundedButton(10);
	            colorButton.setMargin(new Insets(0, 0, 0, 0));
	            colorButton.setFont(new Font("Serif", Font.BOLD, 24));
	            colorButton.setHorizontalTextPosition(SwingConstants.CENTER);
	            colorButton.setBackground(color.getColor());
	            
	            //If the color is dark enough, the selection symbol should be white instead of black.
	            if (color.getColor().getRed()<100 && color.getColor().getGreen()<100)
	            {
	            	colorButton.setForeground(Color.white);
	            }
				gbc = new GridBagConstraints();
				gbc.gridx = count/2;
		        gbc.gridy = count%2;
				colorPanel.add(colorButton, gbc);
				count++;
				colorButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) 
					{
						if (colorButton.getText().equals("\u2713"))
						{
							colorButton.setText("");
							productFilter.removeColor(color.getColorID());
						}
						else
						{
							colorButton.setText("\u2713");
							productFilter.addColor(color.getColorID(), color.getColorName());
						}
						propagateFilters();
					}
					
				});
			}

			colorPanel.setPreferredSize(new Dimension(0,0));
			gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 4;
	        gbc.weightx = 1;
	        gbc.weighty = 0.1;
	        gbc.fill = GridBagConstraints.BOTH;
	        add(colorPanel, gbc);
			
			
			//Resizes the color buttons to take a fixed proportion of space and remain squared shaped.
	        colorPanel.addComponentListener(new ComponentAdapter() 
	        {
	            @Override
	            public void componentResized(ComponentEvent e) 
	            { 
	            	int width = colorPanel.getWidth();
	            	int height = colorPanel.getHeight();
	            	int buttonSize = Math.min(2*width/colors.length, height/2);
	            	for (Component comp : colorPanel.getComponents())
	            	{
	            		comp.setPreferredSize(new Dimension(buttonSize, buttonSize));
	            		comp.revalidate();
	            	}
	            	colorPanel.revalidate();
	            	colorPanel.repaint();
	            	
	            }
	        });
	        
	        //Resizes the keywords in the keywords panel to take all the available horizontal space but a fixed proportion of vertical space.
	        scrollPane.getViewport().addComponentListener(new ComponentAdapter() 
	        {
	            @Override
	            public void componentResized(ComponentEvent e) 
	            { 
	            	int width = scrollPane.getWidth();
	            	int height = scrollPane.getHeight();
	            	for (Component comp : keywordsPanel.getComponents())
	            	{
	            		if (comp instanceof SingularKeywordPanel)
	            		{
	            			comp.setPreferredSize(new Dimension(width - 20, height/4));
	            			((SingularKeywordPanel)comp).displayKeyword();
	            			comp.revalidate();
	            		}
	            	}
	            	keywordsPanel.revalidate();
	            	keywordsPanel.repaint();
	            }
	        });
			
	        //These radio buttons control whether the products should match all the keywords (and) or only one of them (or) to be displayed.
	        
	        or.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					productFilter.setOr(true);
					gui.getDatabaseManager().getProductsWithOrKeywords(productFilter);
					propagateFilters();
				}
	        	
	        });
	        
	        and.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					productFilter.setOr(false);
					gui.getDatabaseManager().getProductsWithOrKeywords(productFilter);
					propagateFilters();
				}
	        	
	        });
	       
	        
	    }
	    
	    /**
	     * Display every keyword along with a button that can remove it from the list of applied keywords.
	     */
	    public void display()
	    {
	    	keywordsPanel.displayKeywords();
	        for (ComponentListener listener : scrollPane.getViewport().getComponentListeners())
	        {
	        	listener.componentResized(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
	        }
			
	    }
	    
	    private class KeywordsPanel extends JPanel
	    {
	    	public KeywordsPanel()
	    	{
	    		super(new GridBagLayout());
	    		setOpaque(false);
	    	}
	    	
	    	/**
	    	 * Display all the applied keywords.
	    	 */
	    	public void displayKeywords()
	    	{
				removeAll();
				//Max number of orders displayed.
			    GridBagConstraints gbc;
			    int count = 0;
				List<String> keywords = productFilter.getOrKeywords();
				for (String keyword : keywords)
			    {
			    	gbc = new GridBagConstraints(); 
			    	SingularKeywordPanel singularKeywordPanel = new SingularKeywordPanel(keyword);
			    	singularKeywordPanel.setPreferredSize(new Dimension(0,0));
			    	gbc.gridx = 0;
			    	gbc.gridy = count++;
			    	gbc.insets = new Insets(5,0,5,0);
			    	add(singularKeywordPanel, gbc);
			    }
	    	}
	    }
	    
	    private class SingularKeywordPanel extends NonOpaquePanel
	    {
	    	private String keyword;
	    	
	    	public SingularKeywordPanel(String keyword)
	    	{
	    		super(new GridBagLayout());
	    		this.keyword = keyword;
	    		setBorder(new LineBorder(Color.DARK_GRAY));
	    		displayKeyword();
	    	}
	    	
	    	/**
	    	 * Displays the keyword along with a button that can remove it from the list of applied keywords.
	    	 */
	    	public void displayKeyword()
	    	{
	    		removeAll();
	    		GridBagConstraints gbc = new GridBagConstraints();
	    		
	    		NonOpaqueTextArea label = new NonOpaqueTextArea(keyword);
	            label.setFont(new Font("Serif", Font.BOLD, 16));
	    		label.setEditable(false);
	            gbc.gridx = 0;
	            gbc.gridy = 0;
	            gbc.fill = GridBagConstraints.BOTH;
	            gbc.weightx = 0.8;
	            gbc.weighty = 1;
	            add(label, gbc);
	            
	            RoundedButton button = new RoundedButton("\u00D7", 10);
	            button.setBackground(GUI.RED);
	            button.setPreferredSize(new Dimension(0,0));
	            button.setMargin(new Insets(0, 0, 0, 0));
	            button.setFont(new Font("Serif", Font.BOLD, 24));
	            button.setHorizontalTextPosition(SwingConstants.CENTER);
	            gbc.gridx = 1;
	            gbc.gridy = 0;
	            gbc.fill = GridBagConstraints.BOTH;
	            gbc.weightx = 0.2;
	            gbc.weighty = 1;
	            add(button, gbc);
	    		
	            button.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) 
					{
						removeKeyword(keyword);
						display();
					}
	            	
	            });
	    		
	    	}
	    	
	    	
	    }
	    
	
	   
	    
	}
	
}
