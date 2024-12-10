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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import ui.tools.*;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import backend.ProductFilter;
import ui.GUI;

/**
 * The panel used by the administrator that contains the main functionalities needed to manage the products of the database.
 */
public class ProductManagementPanel extends DescendantPanel
{
	private AdminFilterPanel filterPanel;
	private AdminSearchPanel searchPanel;
	private AlternatingProductPanel catalogPanel;
	private ProductFilter productFilter;
	
	public ProductManagementPanel(GUI gui)
	{
		super(new GridBagLayout(), gui);
		setOpaque(false);
		//A filter that contains the criteria the product needs to match, initialized so that all products are selected.
		productFilter = new ProductFilter();
		gui.getDatabaseManager().getProductsWithOrKeywords(productFilter);
		
		//A panel that enables the admin to select the products according to several filters.
		this.filterPanel = new AdminFilterPanel();
		
		//A panel that enables the admin to type keywords to select products.
		this.searchPanel = new AdminSearchPanel();
		
		//The main panel on the bottom right corner of the screen, displaying a catalog view of the products.
		this.catalogPanel = new AlternatingProductPanel(gui);
		
		
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
        catalogPanel.setPreferredSize(new Dimension(0,0));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .7;
        gbc.weighty = .8;
        gbc.insets = new Insets(5,5,5,5);
        add(catalogPanel, gbc);
	}
	
	/**
	 * Passes the filter to the concerned panel, until it reaches the panel responsible for calling the database manager and displaying products.
	 */
	public void propagateFilters()
	{
		catalogPanel.propagateFilters(productFilter);
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
	 * Asks the alternating catalog panel to switch to the panel corresponding to the given identifier.
	 * @param nextPanelID The identifier of the next panel to switch to.
	 */
	public void propagateCatalogAlternate(PanelID nextPanelID)
	{
		catalogPanel.alternatePanels(nextPanelID);
	}
	
	public ProductFilter getProductFilter()
	{
		return this.productFilter;
	}
	
	/**
	 * The panel enabling the administrator to find products matching certain criteria.
	 */
	private class AdminFilterPanel extends NonOpaquePanel
	{
		private JScrollPane scrollPane;
		private KeywordsPanel keywordsPanel;
		
		public AdminFilterPanel()
		{
			super(new GridBagLayout());
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
	        gbc.weighty = 0.05;
	        gbc.gridwidth = 2;
	        gbc.insets = new Insets(5,5,5,5);
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
	        gbc.insets = new Insets(5,5,5,5);
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
	        gbc.gridwidth = 2;
	        gbc.insets = new Insets(5,5,5,5);
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
	        gbc.insets = new Insets(5,5,5,5);
	        discountPanel.add(selectDiscount, gbc);
	        
	        JSlider discountSlider = new SliderGradient(0, 100, 0, GUI.BLUE, GUI.PRODUCT_CUSTOMER);
	        discountSlider.setOpaque(false);
	        discountSlider.setMajorTickSpacing(50);
	        discountSlider.setMinorTickSpacing(10);
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 1;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.6;
	        discountPanel.add(discountSlider, gbc);
	        
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
	        gbc.gridwidth = 2;
	        gbc.insets = new Insets(5,5,5,5);
	        add(discountPanel, gbc);
	        
	        
	        //A group of radio buttons to select if the displayed products are available or unavailable
			NonOpaqueRadio availableRadio = new NonOpaqueRadio("Yes");
			availableRadio.setPreferredSize(new Dimension(0,0));
			NonOpaqueRadio notAvailableRadio = new NonOpaqueRadio("No");
			notAvailableRadio.setPreferredSize(new Dimension(0,0));
			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroup.add(availableRadio);
			buttonGroup.add(notAvailableRadio);
			availableRadio.setSelected(true);
			
			availableRadio.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					productFilter.setAvailable(true);
					propagateFilters();

				}
			});
			
			notAvailableRadio.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					productFilter.setAvailable(false);
					propagateFilters();
				}
			});
			
			NonOpaqueJLabel availability = new NonOpaqueJLabel("Product available");
			availability.setPreferredSize(new Dimension(0,0));
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.weightx = 1;
			gbc.weighty = 0.05;
			gbc.gridwidth = 2;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			add(availability, gbc);
			
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 4;
			gbc.weightx = 0.5;
			gbc.weighty = 0.05;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			add(availableRadio, gbc);
			
			gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 4;
			gbc.weightx = 0.5;
			gbc.weighty = 0.05;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			add(notAvailableRadio, gbc);
	       
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
			gbc.weighty = 0.05;
			gbc.gridwidth = 2;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			orderByPricePanel.add(orderByPriceLabel, gbc);
			
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weightx = 0.5;
			gbc.weighty = 0.05;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			orderByPricePanel.add(orderByPrice, gbc);
			
			gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.weightx = 0.5;
			gbc.weighty = 0.05;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			orderByPricePanel.add(dontOrderByPrice, gbc);
			
			
			gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 5;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.1;
	        gbc.gridwidth = 2;
	        gbc.insets = new Insets(5,5,5,5);
			add(orderByPricePanel, gbc);

	        RoundedButton addNewProduct = new RoundedButton("Add new product");
	        addNewProduct.setBackground(GUI.BLUE);
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 9;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.05;
	        gbc.gridwidth = 2;
	        gbc.insets = new Insets(5,5,5,5);
	        add(addNewProduct, gbc);

	        NonOpaquePanel andOrPanel = new NonOpaquePanel(new GridBagLayout());
	        NonOpaqueJLabel keywordsLabel = new NonOpaqueJLabel("Keywords");
	        keywordsLabel.setPreferredSize(new Dimension(0,0));
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 0.5;
	        gbc.weighty = 1;
	        gbc.insets = new Insets(5,5,5,5);
	        andOrPanel.add(keywordsLabel, gbc);
	        
	        NonOpaqueRadio or = new NonOpaqueRadio("Or");
			or.setPreferredSize(new Dimension(0,0));
			NonOpaqueRadio and = new NonOpaqueRadio("And");
			and.setPreferredSize(new Dimension(0,0));
			ButtonGroup orButtonGroup = new ButtonGroup();
			orButtonGroup.add(or);
			orButtonGroup.add(and);
			or.setSelected(productFilter.isOr());
			
			gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.weightx = 0.25;
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			andOrPanel.add(or, gbc);
			
			gbc = new GridBagConstraints();
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.weightx = 0.25;
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5,5,5,5);
			andOrPanel.add(and, gbc);

	        andOrPanel.setPreferredSize(new Dimension(0,0));
	        gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 7;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.05;
	        gbc.gridwidth = 2;
	        gbc.insets = new Insets(5,5,5,5);
	        add(andOrPanel, gbc);
	        
	        //The keyword panel containing all the keywords that have been applied from the search bar.
	        this.keywordsPanel = new KeywordsPanel();
	        
	        //A scrollable panel that contains every individual keyword in its own panel.
	        this.scrollPane = new JScrollPane(keywordsPanel);
	        scrollPane.setOpaque(false);
	        scrollPane.getViewport().setOpaque(false);
		    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setPreferredSize(new Dimension(0,0));
			scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(GUI.BACKGROUND_ADMIN, GUI.PRODUCT_ADMIN));
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
	        gbc.gridy = 8;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.25;
	        gbc.gridwidth = 2;
	        gbc.insets = new Insets(5,5,5,5);
			add(scrollPane, gbc);
	        
			//The list of all colors that can be selected to filter products.
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
			
			int count = 0;
			//For every color, a small square button is created that can be selected or unselected.
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
	        gbc.gridy = 6;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.weightx = 1;
	        gbc.weighty = 0.1;
	        gbc.gridwidth = 2;
	        gbc.insets = new Insets(5,5,5,5);
	        add(colorPanel, gbc);
			
			
			
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
	        
			
			
			
			//Resizes all the small keywords panels when their container is resized.
	        scrollPane.getViewport().addComponentListener(new ComponentAdapter() {
	            @Override
	            public void componentResized(ComponentEvent e) { 
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

	        
	        addNewProduct.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					propagateCatalogAlternate(PanelID.NEW);;
				}
	        	
	        });
		}
		
		/**
		 * Refreshes the keywords panel and reorganizes the individual keyword panels inside.
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
	    	 * Organizes the keyword panels to display.
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
		
		private class SingularKeywordPanel extends JPanel
	    {
	    	private String keyword;
	    	
	    	public SingularKeywordPanel(String keyword)
	    	{
	    		super(new GridBagLayout());
	    		this.keyword = keyword;
	    		setBorder(new LineBorder(Color.DARK_GRAY));
	    		setOpaque(false);
	    		displayKeyword();
	    	}
	    	
	    	/**
	    	 * Display the keyword along with the button to remove it.
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
	            
	            RoundedButton button = new RoundedButton("\u00D7");
	            button.setPreferredSize(new Dimension(0,0));
	            button.setBackground(GUI.RED);
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
						ProductManagementPanel productManagementPanel = (ProductManagementPanel)SwingUtilities.getAncestorOfClass(ProductManagementPanel.class, button);
						if (productManagementPanel != null)
						{
							productManagementPanel.removeKeyword(keyword);
							display();
						}
					}
	            	
	            });
	    		
	    	}
	    	
	    	
	    }
	}
	
	/**
	 * The panel used by the administrator to search a specific product or apply some new keywords to narrow down their search.
	 */
	private class AdminSearchPanel extends JPanel
	{
		private JTextField searchBar;
		
		public AdminSearchPanel()
		{
			super(new GridBagLayout());
	        setBorder(GUI.GRAY_BORDER);
	        setOpaque(false);
	        
	        NonOpaqueJLabel searchSection = new NonOpaqueJLabel("Find a product by name, brand, description or keyword.");
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
	        
	        //Every time a letter is typed or deleted, the filter changes and the selected products are refreshed.
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
		 * Sets the current keywords of the filter to be the content of the search bar and applies this filter.
		 */
		public void propagateKeywords()
		{
			productFilter.setKeywords(searchBar.getText());
			propagateFilters();
			
		}
	}
	
	
}
