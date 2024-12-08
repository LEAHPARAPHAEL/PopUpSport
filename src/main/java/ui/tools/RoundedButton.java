package ui.tools;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    private int cornerRadius = 30;

    public RoundedButton(String text, int cornerRadius) {
        super(text);
        this.cornerRadius = cornerRadius;
        setOpaque(false);
        setFocusPainted(false); 
        setContentAreaFilled(false); 
        setBorderPainted(false);
    }
    
    public RoundedButton(int cornerRadius) {
        super();
        this.cornerRadius = cornerRadius;
        setOpaque(false);
        setFocusPainted(false); 
        setContentAreaFilled(false); 
        setBorderPainted(false);
    }
    
    public RoundedButton(String text) {
        super(text);
        setOpaque(false);
        setFocusPainted(false); 
        setContentAreaFilled(false); 
        setBorderPainted(false); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.setColor(getForeground());
        FontMetrics fm = g2.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(getText())) / 2;
        int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.DARK_GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2.dispose();
    }

}
