package ui.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import ui.GUI;

public class LoadingBar extends JLayeredPane
{
	private JProgressBar load;
	private RotatingCursor rotatingCursor;
    private double rotationSpeed = 6;
	private Runnable onTimeUp;
	private Timer timer;
	private int speed;
	private URL cursor;
	
	public LoadingBar(Runnable onTimeUp)
	{
		Random random = new Random();
		this.speed = 40 + random.nextInt(11);
		//this.speed = 5;
		this.rotationSpeed = speed / 6;
		this.cursor = getRandomCursorStream();
		setPreferredSize(new Dimension(0,0));
		
		this.load = new JProgressBar();
		load.setBackground(Color.LIGHT_GRAY);
		load.setForeground(GUI.BLUE);
        load.setMinimum(0);
        load.setMaximum(100); 
        load.setStringPainted(true);
        load.setBounds(0, 0, getWidth(), getHeight());
        
        this.rotatingCursor = new RotatingCursor(cursor);
        rotatingCursor.setBounds(0, 0, getHeight(), getHeight());
        
        add(load, Integer.valueOf(0));
        add(rotatingCursor, Integer.valueOf(1));
        
        this.onTimeUp = onTimeUp;
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) { 
            	load.setBounds(0, 0, getWidth(), getHeight());
            	rotatingCursor.setBounds((int) (load.getValue() / 100.0 * getWidth()) - getHeight() / 2, 0, getHeight(), getHeight());
               	rotatingCursor.resizeImage();
            }
        });
        

        
        setVisible(false);
	}
	
	public URL getRandomCursorStream() {
		 String[] cursors = new String[] {"cursors/football.png", "cursors/rugby.png", "cursors/basketball.png"};
		 Random random = new Random();
		 int rand = random.nextInt(3);
		 
		 return getClass().getClassLoader().getResource(cursors[rand]);
	}
	
	
	public void setVisible(boolean isVisible)
	{
		load.setVisible(isVisible);
		rotatingCursor.setVisible(isVisible);
	}
	
	public void setValue(int value)
	{
		load.setValue(value);
    	rotatingCursor.setBounds((int) (value / 100.0 * getWidth()) - getHeight() / 2, 0, getHeight(), getHeight());
		revalidate();
	}
	
	@Override
	public boolean isVisible()
	{
		return load.isVisible();
	}
	
	public void load()
	{
		setVisible(true);
        this.timer = new Timer(speed, new ActionListener() {
			int progress = 0;
        	@Override
			public void actionPerformed(ActionEvent e) {
				progress += 1;
				load.setValue(progress);
            	rotatingCursor.setBounds((int) (load.getValue() / 100.0 * getWidth()) - getHeight() / 2, 0, getHeight(), getHeight());
				rotatingCursor.rotate();
            	revalidate();
            	
            	if (progress >= 100)
            	{
            		((Timer) e.getSource()).stop();
            		onTimeUp.run();
            		reset();
            	}
			}
        });
		timer.start();
	}
	
	public void reset()
	{
		setVisible(false);
		load.setValue(0);
		rotatingCursor.setBounds(0, 0, getHeight(), getHeight());
	}
	
	public void stop()
	{
		timer.stop();
	}
	
	
	private class RotatingCursor extends JPanel 
	{
	    private BufferedImage image;
	    private double angle = 0; 
	    private URL cursor;
	    
	    public RotatingCursor(URL cursor) 
	    {
	    	this.cursor = cursor;
	        try 
	        {
	            this.image = ImageIO.read(cursor); 
	        } 
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	        }
	        setOpaque(false);
	        
	        addComponentListener(new ComponentAdapter() {
	        	@Override
	        	public void componentResized(ComponentEvent e) { 
	                
	        		if (getHeight() > 0 && getWidth() > 0)
	        		{
		        		Image temp = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
		                image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

		                Graphics2D g2d = image.createGraphics();
		                g2d.drawImage(temp, 0, 0, null);
		                g2d.dispose();
		                repaint();
	        		}
	        	}
	        });
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);

	        if (image != null) {
	            Graphics2D g2d = (Graphics2D) g;

	            int x = getWidth() / 2;
	            int y = getHeight() / 2;

	            AffineTransform transform = new AffineTransform();
	            transform.rotate(Math.toRadians(angle), x, y);

	            int imgX = x - image.getWidth() / 2;
	            int imgY = y - image.getHeight() / 2;
	            transform.translate(imgX, imgY);

	            g2d.drawImage(image, transform, null);
	        }
	    }
	    
	    public void rotate()
	    {
	    	this.angle += rotationSpeed;
	    	repaint();
	    }
	    
	    public void resizeImage()
	    {
	        try 
	        {
	            this.image = ImageIO.read(cursor); 
	        } 
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	        }
	        for (ComponentListener componentListener: getComponentListeners())
	        {
	        	componentListener.componentResized(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
	        }
	    }
	}
}
