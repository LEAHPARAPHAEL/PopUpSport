package ui.admin;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import backend.CustomerProfile;
import backend.Order;
import backend.Product;
import ui.GUI;
import ui.tools.*;

/**
 * The panel used by the administrator to look at the receipt from a specific customer's order.
 */
public class ReceiptPanelAdmin extends DescendantPanel
{
	private Order order;
	private CustomerProfile customer;
	private JScrollPane scrollPane;
	private byte[] receipt;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
	
	
	public ReceiptPanelAdmin(GUI gui, Order order, CustomerProfile customer)
	{
		super(new GridBagLayout(), gui);
		this.order = order;
		this.customer = customer;
		GridBagConstraints gbc = new GridBagConstraints();
		setOpaque(false);
		//Button to switch to the order panel associated with the order.
		RoundedButton backToOrders = new RoundedButton("Back");
		backToOrders.setBackground(GUI.RED);
		backToOrders.setPreferredSize(new Dimension(0,0));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.15;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		add(backToOrders, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.85;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		add(new JLabel(), gbc);
		
		backToOrders.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AlternatingCustomersPanel parent = (AlternatingCustomersPanel)SwingUtilities.getAncestorOfClass(AlternatingCustomersPanel.class, backToOrders);
				parent.alternatePanels(PanelID.ORDER);
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
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(0,0));
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
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
        
        //Adds all the information contained in the order, separated in several paragraphs.
        LocalDateTime formattedOrderTime = order.getOrderTime().toLocalDateTime();
        String formattedDate = formattedOrderTime.format(formatter);
        Paragraph orderReference = new Paragraph(String.format("Order #%d", order.getOrderID()));
        Paragraph orderAddress = new Paragraph("Delivery address : "+order.getAddress());
        Paragraph orderTime = new Paragraph("Date : "+formattedDate).setMarginBottom(20);
        Paragraph name = new Paragraph("Customer : " + customer.getName() + " " + customer.getFirstName()).setMarginBottom(40);
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
        table.addHeaderCell(new Cell().add(new Paragraph("Quantity x unit price").setBold()).setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Price").setBold()).setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY));

        for (Entry<Integer, Integer> entry : order.getOrderContent().entrySet()) {
        	Product product = gui.getDatabaseManager().getProductManager().getProductById(entry.getKey());
            table.addCell(new Cell().add(new Paragraph(product.getName())));
            table.addCell(new Cell().add(new Paragraph(String.format("%d x %.2f", entry.getValue(), order.getProductPriceWhenOrdered(entry.getKey())))));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f \u2359", entry.getValue()*order.getProductPriceWhenOrdered(entry.getKey())))));


        }
        double total = order.getPrice();
        table.addCell(new Cell(1, 2).add(new Paragraph("Total price").setBold()));
        table.addCell(new Cell().add(new Paragraph(String.format("%.2f \u2359", total)).setBold()));

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
