package ui.customer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.json.JSONArray;
import org.json.JSONObject;

import ui.GUI;
import ui.tools.*;

/**
 * The panel used by the customer to finish their reservation, enter the delivery address and effectively order the content of their basket.
 */
public class PaymentAndShipmentPanel extends DescendantPanel
{
	private NonOpaqueJLabel recapOrder;
	private static final double LATITUDE = 48.8521345;
	private static final double LONGITUDE = 2.3090709;
	private static final double EARTH_RADIUS = 6371.0; 
	private RoundedButton cancel;
	private RoundedButton pay;
	private JSONArray jsonArray;
	private StringBuilder queryBuilder;
	private LoadingBar loadingBar;
	private NonOpaqueJLabel analyze;
	
	public PaymentAndShipmentPanel(GUI gui)
	{
		super(new GridBagLayout(), gui);
		GridBagConstraints gbc = new GridBagConstraints();
		setOpaque(false);
		//Sets the layout of this panel
		
		recapOrder = new NonOpaqueJLabel(String.format("<html>The total price of your order is %.2f <span style='color:red; font-weight:bold; font-size:12px;'>\u2359</span>.</html>", gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getBasketManager().getTotalPrice()), SwingUtilities.CENTER);
		recapOrder.setFont(new Font("Serif", Font.BOLD, 18));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0.15;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.BOTH;
		add(recapOrder, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.1;
		gbc.weighty = 0.85;
		gbc.gridheight = 8;
		gbc.fill = GridBagConstraints.BOTH;
		add(new NonOpaqueJLabel(), gbc);
		
		NonOpaqueJLabel address = new NonOpaqueJLabel("Shipment address");
		address.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 0.3;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(address, gbc);
		
		NonOpaqueJLabel postalCode = new NonOpaqueJLabel("Postal code");
		postalCode.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 0.3;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(postalCode, gbc);
		
		NonOpaqueJLabel city = new NonOpaqueJLabel("City");
		city.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.weightx = 0.3;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(city, gbc);
		
		JTextField adressField = new JTextField();
		adressField.setPreferredSize(new Dimension(0,0));
		adressField.setBorder(GUI.GRAY_BORDER);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(adressField, gbc);
		
		JTextField postalCodeField = new JTextField();
		postalCodeField.setPreferredSize(new Dimension(0,0));
		postalCodeField.setBorder(GUI.GRAY_BORDER);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(postalCodeField, gbc);
		
		JTextField cityField = new JTextField();
		cityField.setPreferredSize(new Dimension(0,0));
		cityField.setBorder(GUI.GRAY_BORDER);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(cityField, gbc);
		
		NonOpaqueJLabel country = new NonOpaqueJLabel("Country");
		country.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.weightx = 0.3;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(country, gbc);
		
		JTextField countryField = new JTextField();
		countryField.setPreferredSize(new Dimension(0,0));
		countryField.setBorder(GUI.GRAY_BORDER);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 4;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(countryField, gbc);
		
		this.cancel = new RoundedButton("Cancel order");
		cancel.setBackground(GUI.RED);
		cancel.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.weightx = 0.3;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(cancel, gbc);
		
		this.pay = new RoundedButton("Proceed with payment");
		pay.setBackground(GUI.GREEN);
		pay.setPreferredSize(new Dimension(0,0));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 5;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(pay, gbc);

		this.analyze = new NonOpaqueJLabel("Locating the delivery address...", SwingConstants.CENTER);
		analyze.setFont(new Font("Serif", Font.ITALIC, 16));
		analyze.setForeground(Color.DARK_GRAY);
        analyze.setVisible(false);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.weightx = 0.8;
		gbc.weighty = 0.1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(analyze, gbc);
		
		
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.weightx = 0.1;
		gbc.weighty = 0.85;
		gbc.gridheight = 8;
		gbc.fill = GridBagConstraints.BOTH;
		add(new NonOpaqueJLabel(), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 8;
		gbc.weightx = 0.8;
		gbc.weighty = 0.15;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		add(new NonOpaqueJLabel(), gbc);

	

		
		this.jsonArray = new JSONArray("[]");
		
		Runnable onTimeUp = () -> {
			MainCustomerPanel mainPanel = (MainCustomerPanel)SwingUtilities.getAncestorOfClass(MainCustomerPanel.class, pay);
			 //If no geographical point has been found.
            if (jsonArray.length() == 0) 
            {
            	mainPanel.showInfoPanelForTheNext(5, "Sorry ! We couldn't find this address.");
                analyze.setVisible(false);
            }
            //If too many geographical points have been found (>1).
            else if (jsonArray.length() > 1) 
            {
            	mainPanel.showInfoPanelForTheNext(5, "Multiple addresses found. Please be more specific.");
                analyze.setVisible(false);
            }
            else 
            {
            	//Get the latitude and longitude of the found geographical point.
            	JSONObject jsonObject = jsonArray.getJSONObject(0);
                String latitude = jsonObject.getString("lat");
                String longitude = jsonObject.getString("lon");
                
                //Computes the distance between this point and the warehouse using the Haversine formula.
                double distance = computeHaversineDistance(Double.parseDouble(latitude), Double.parseDouble(longitude));
                
                //Computes a very primitive estimation of the time needed for delivery based on this distance.
                double estimatedTime = computeEstimatedTimeRequiredForOrder(distance);
                
                //Creates a new order with all of these properties.
                gui.getDatabaseManager().insertOrder(queryBuilder.toString(), estimatedTime);
                
                //Refreshes all the panels and ends the reservation.
                gui.recursivelyRefreshAllPanels(gui);
                mainPanel.endReservation(true);
                mainPanel.updateCredit();
            	mainPanel.showInfoPanelAndSwitch(10, "Order completed !", PanelID.ORDER);
            }
		};
		
		this.loadingBar = new LoadingBar(onTimeUp);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.weightx = 0.8;
		gbc.weighty = 0.1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(loadingBar, gbc);
		

		//Cancels the reservation made by this customer and switches back to the catalog panel.
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (loadingBar.isVisible())
				{
					loadingBar.stop();
				}
				MainCustomerPanel mainCustomerPanel = (MainCustomerPanel)SwingUtilities.getAncestorOfClass(MainCustomerPanel.class, cancel);
				mainCustomerPanel.endReservation(false);
			}
		});
		

		
		pay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (loadingBar.isVisible())
				{
					return;
				}
				searchAddress(adressField.getText(), postalCodeField.getText(), cityField.getText(), countryField.getText());
			}
		});
	}
	
	/**
	 * Searchs on the open source map from Nominatim if the given address exists. If it exists, computes the geographical distance between this point and the location of the warehouse and estimates the time needed for the delivery.
	 * @param address The street name and number.
	 * @param postalCode The postal code.
	 * @param city The city.
	 * @param country The country.
	 */
    public void searchAddress(String address, String postalCode, String city, String country) {
        //pay.removeActionListener(payAction);
        //cancel.removeActionListener(cancelAction);
    	pay.setEnabled(false);
    	cancel.setEnabled(false);
    	
        //A parallel thread is started to not freeze the application if the search for the address takes too much time.
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception {
            	MainCustomerPanel mainPanel = (MainCustomerPanel)SwingUtilities.getAncestorOfClass(MainCustomerPanel.class, pay);
            	try 
                {
                	queryBuilder = new StringBuilder();
                    queryBuilder.append(address);
                    if (!postalCode.isEmpty()) {
                        queryBuilder.append(", ").append(postalCode);
                    }
                    if (!city.isEmpty()) {
                        queryBuilder.append(", ").append(city);
                    }
                    if (!country.isEmpty()) {
                        queryBuilder.append(", ").append(country);
                    }
          
                    String encodedAddress = java.net.URLEncoder.encode(queryBuilder.toString(), "UTF-8");
                  
                    //We take the first two results of the query to be able to tell the customer if we are not sure about which location is the one they meant.
                    String urlStr = "https://nominatim.openstreetmap.org/search?q=" + encodedAddress + "&format=json&limit=2";
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("User-Agent", "JavaOnlineStore/1.0");

                    //Write the result from the http request.
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) 
                    {
                        content.append(inputLine);
                    }
                    in.close();
                    conn.disconnect();

                    String jsonResponse = content.toString();
                    jsonArray = new JSONArray(jsonResponse);
                } 
                catch (IOException ex) 
                {
                	mainPanel.showInfoPanelForTheNext(5,"Sorry ! We couldn't find this address. Make sure you are connected to the Internet.");
                    ex.printStackTrace();
                }
                return null;
            }
            
            @Override
            protected void done()
            {
            	pay.setEnabled(true);
            	cancel.setEnabled(true);
            }
        };
        
        analyze.setVisible(true);
        loadingBar.load();
        worker.execute();  
    }
    
    /**
     * Computes the distance between the warehouse location and a given geographical point using the Haversine formula.
     * @param clientLatitude The latitude of the geographical point.
     * @param clientLongitude The longitude of the geographical point.
     * @return The distance between the two points, in kilometers.
     */
    public double computeHaversineDistance(double clientLatitude, double clientLongitude)
    {
        double latitudeRad = Math.toRadians(LATITUDE);
        double longitudeRad = Math.toRadians(LONGITUDE);
        double clientLatitudeRad = Math.toRadians(clientLatitude);
        double clientLongitudeRad = Math.toRadians(clientLongitude);
        
        //Difference in latitude and longitude
        double deltaLat = latitudeRad - clientLatitudeRad;
        double deltaLon = longitudeRad - clientLongitudeRad;
        
        //Haversine formula
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) + Math.cos(latitudeRad) * Math.cos(clientLatitudeRad) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS * c;
    }
    
    /**
     * Estimates the time needed for the delivery, depending on the distance separating the delivery point from the warehouse. Several modes of transport can be used if the distance is large enough, but additional transit time is added every time a new mode of transport is used.
     * @param distance The distance separating the delivery point from the warehouse.
     * @return The theoretical time needed for the delivery to be completed, in hours.
     */
    public double computeEstimatedTimeRequiredForOrder(double distance)
    {
    	double preparationTime = 24;
    	//Bicycle delivery : all of the delivery is done by bicycle.
    	if (distance < 5)
    	{
    		return distance / 15 + preparationTime;
    	}
    	//Car delivery : all of the delivery is done by car.
    	else if (distance < 300)
    	{
    		return distance / 80 + preparationTime + 12;
    	}
    	//Train delivery : splits the distance. The first 80% are done by train and the other 20% by car.
    	else if (distance < 1400)
    	{
    		return 0.8 * distance / 150 + computeEstimatedTimeRequiredForOrder(0.2*distance) + 24;
    	}
    	//Plane delivery : splits the distance. The first 90% are done by plane and the rest by train or car.
    	else
    	{
    		return 0.9 * distance / 800 + computeEstimatedTimeRequiredForOrder(0.2*distance) + 24;
    	}
    	
    	
    }
	
	
}
