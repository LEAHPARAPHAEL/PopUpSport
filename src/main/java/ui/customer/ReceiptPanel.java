package ui.customer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import backend.Order;
import backend.Product;
import ui.GUI;
import ui.tools.*;

/**
 * The panel that enables the customer to review the receipt for one of their orders.
 */
public class ReceiptPanel extends DescendantPanel
{
	private Order order;
	private JScrollPane scrollPane;
	private byte[] receipt;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
	
	public ReceiptPanel(Order order, GUI gui)
	{
		super(new GridBagLayout(), gui);
		setOpaque(false);
		this.order = order;
		GridBagConstraints gbc = new GridBagConstraints();
		
		//Button to switch to the order panel associated with the order.
		RoundedButton backToOrders = new RoundedButton("Back");
		backToOrders.setBackground(GUI.RED);
		backToOrders.setPreferredSize(new Dimension(0,0));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.15;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		add(backToOrders, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.85;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		add(new NonOpaqueJLabel(), gbc);
		
		backToOrders.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainCustomerPanel mainCustomerPanel = (MainCustomerPanel)SwingUtilities.getAncestorOfClass(MainCustomerPanel.class, backToOrders);
				mainCustomerPanel.switchPanel(PanelID.ORDER);
			}
		});
		
		
		//Creates the receipt icon displayed in the scrollable panel by first creating a pdf file.
		ImageIcon labelIcon = null;
		this.receipt = null;
        try 
        {
            receipt = generateReceipt();

            labelIcon = transformToLabel(receipt);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        JLabel receiptLabel = new JLabel();

        //Scrollable panel containing the pdf of the receipt.
		this.scrollPane = new JScrollPane(receiptLabel);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(0,0));
		scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(GUI.BACKGROUND_CUSTOMER, GUI.PRODUCT_CUSTOMER));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.9;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		add(scrollPane, gbc);
		
		
		receiptLabel.setIcon(labelIcon);
        scrollPane.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) { 
                for (ComponentListener listener : receiptLabel.getComponentListeners())
                {
                	listener.componentResized(new ComponentEvent(receiptLabel, ComponentEvent.COMPONENT_RESIZED));
                }
            }
        });
        
        
        receiptLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) { 
            	ImageIcon icon = transformToLabel(receipt);
            	double ratio = (double)icon.getIconHeight()/(double)icon.getIconWidth();
	        	Image labelImage = icon.getImage();
	        	Image resizedImage = labelImage.getScaledInstance(scrollPane.getWidth(), (int)(scrollPane.getWidth()*ratio), Image.SCALE_SMOOTH);
	        	ImageIcon resizedIcon = new ImageIcon(resizedImage);
	        	receiptLabel.setIcon(resizedIcon);
            	revalidate();
            }
        });
        
        

	}
	
	/**
	 * Generates a receipt based on the order as an array of bytes.
	 * @return An array of bytes representing the formatted pdf.
	 * @throws IOException
	 */
	public byte[] generateReceipt() throws IOException
	{
		//Instantiates an output stream to write the bytes array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        
        //Creates a new pdf document.
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);
        Paragraph title = new Paragraph("Order Receipt").setTextAlignment(TextAlignment.CENTER).setFontSize(20).setMarginBottom(20);
        
        
        final String FONT_PATH = "fonts/EversonMono.ttf";

	    InputStream fontStream = getClass().getClassLoader().getResourceAsStream(FONT_PATH);
	
	    byte[] fontBytes;
	    try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) 
	    {
	        byte[] data = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = fontStream.read(data, 0, data.length)) != -1) 
	        {
	            buffer.write(data, 0, bytesRead);
	        }
	        buffer.flush();
	        fontBytes = buffer.toByteArray();
	    }
	
	    PdfFont font = PdfFontFactory.createFont(fontBytes, PdfEncodings.IDENTITY_H, true);
        
	    //Add our logo
	    final String LOGO_PATH = "Logo.png";

	    InputStream logoStream = getClass().getClassLoader().getResourceAsStream(LOGO_PATH);

	  // Convert InputStream to byte[]
	    byte[] logoBytes;
	    try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) 
	    {
	        byte[] data = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = logoStream.read(data, 0, data.length)) != -1) 
	        {
	            buffer.write(data, 0, bytesRead);
	        }
	        buffer.flush();
	        logoBytes = buffer.toByteArray();
	    }

	  // Create the PdfImageXObject from the byte array
	  PdfImageXObject xObject = new PdfImageXObject(ImageDataFactory.create(logoBytes));
        com.itextpdf.layout.element.Image logoTopRight = new com.itextpdf.layout.element.Image(xObject, 100).setHorizontalAlignment(HorizontalAlignment.RIGHT);
        com.itextpdf.layout.element.Image logoBottomLeft = new com.itextpdf.layout.element.Image(xObject, 100).setHorizontalAlignment(HorizontalAlignment.LEFT);
        logoBottomLeft.setFixedPosition(20, 20);
        document.add(logoTopRight);
        document.add(logoBottomLeft);
        
        //Adds all the information contained in the order, separated in several paragraphs.
        LocalDateTime formattedOrderTime = order.getOrderTime().toLocalDateTime();
        String formattedDate = formattedOrderTime.format(formatter);
        Paragraph orderReference = new Paragraph(String.format("Order #%d", order.getOrderID())).setBold();
        Paragraph orderAddress = new Paragraph("Delivery address : "+ order.getAddress());
        Paragraph orderTime = new Paragraph("Date : "+ formattedDate).setMarginBottom(20);
        Paragraph name = new Paragraph("Customer : " + gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getName() + " " + gui.getDatabaseManager().getCustomerManager().getCurrentCustomer().getFirstName()).setMarginBottom(40);
        Paragraph details = new Paragraph("Order details").setTextAlignment(TextAlignment.CENTER).setFontSize(16);
        document.add(title);
        document.add(orderReference);
        document.add(orderAddress);
        document.add(orderTime);
        document.add(name);
        document.add(details);

        //Table containing for each product its quantity, unit price when ordered and total price.
        Table table = new Table(UnitValue.createPercentArray(new float[]{50, 25, 25}));
        table.setWidth(UnitValue.createPercentValue(100)); 

        table.addHeaderCell(new Cell().add(new Paragraph("Product").setBold()).setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Quantity x Unit Price").setBold()).setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Price").setBold()).setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY));

        for (Entry<Integer, Integer> entry : order.getOrderContent().entrySet()) {
        	Product product = gui.getDatabaseManager().getProductManager().getProductById(entry.getKey());
            table.addCell(new Cell().add(new Paragraph(product.getName())));
            table.addCell(new Cell().add(new Paragraph(String.format("%d x %.2f", entry.getValue(), order.getProductPriceWhenOrdered(entry.getKey())))));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", entry.getValue()*order.getProductPriceWhenOrdered(entry.getKey())))));


        }
        double total = order.getPrice();
        Paragraph p = new Paragraph();
        Text totalValue = new Text(String.format("%.2f ", total)).setBold();
        Text conesCurr = new Text("\u2359").setBold().setFont(font).setFontColor(com.itextpdf.kernel.color.Color.RED);
        p.add(totalValue);
        p.add(conesCurr);
        
        table.addCell(new Cell(1, 2).add(new Paragraph("Total price").setBold()));
        table.addCell(new Cell().add(p));

        document.add(table);
        document.close();

        return byteArrayOutputStream.toByteArray();
	}
	
	
	/**
	 * Transforms the formatted pdf given as an array of bytes into an image icon.
	 * @param receipt The array of bytes representing the pdf file.
	 * @return The image icon obtained after converting the bytes array into an image.
	 */
	public ImageIcon transformToLabel(byte[] receipt)
	{
		PDDocument document = null;
		try 
		{
			//Renders the document as a buffered image, then converts it into an image icon.
			document = PDDocument.load(new ByteArrayInputStream(receipt));
		    PDFRenderer pdfRenderer = new PDFRenderer(document);
		    BufferedImage pageImage = pdfRenderer.renderImageWithDPI(0, 100); 
		    ImageIcon labelIcon = new ImageIcon(pageImage);
		    document.close();
		    return labelIcon;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		//Small subtleties to close properly the document, whether the creation was successful or not.
		finally 
		{
	 
			if (document != null) 
			{
				try 
				{
					document.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
				
		}

		return null;


	}
}
