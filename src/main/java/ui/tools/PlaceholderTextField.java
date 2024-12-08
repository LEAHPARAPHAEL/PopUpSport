package ui.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * An extension of the java swing component JTextField, that can display a placeholder text in the field if it is empty and doesn't have the user's focus.
 */
public class PlaceholderTextField extends JTextField 
{
    private String placeholder;
    private Color placeholderColor;
    private Font placeholderFont;
    private boolean hasPlaceholder;

    /**
     * An extension of the java swing component JTextField, that can display a placeholder text in the field if it is empty and doesn't have the user's focus.
     * @param placeholder The string to display by default.
     */
    public PlaceholderTextField(String placeholder) 
    {
        this.placeholder = placeholder;
        this.placeholderColor = Color.GRAY; 
        setBorder(new LineBorder(Color.LIGHT_GRAY));
        //Any event triggers the repaint method, which is overridden to display the placeholder if needed.
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                repaint();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                repaint();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                repaint();
            }
        });
    }
    
    /**
     * Gets the text in the field, even if it is the placeholder string.
     * @return The text displayed in the field.
     */
    public String getTextAnyway()
    {
    	if (hasPlaceholder)
    		return placeholder;
    	else
    		return getText();
    }


    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);

        //If the field is empty and not the focus owner, it should display the placeholder.
        if (getText().isEmpty() && !isFocusOwner()) 
        {
        	hasPlaceholder = true;
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(placeholderFont);
            g2.setColor(placeholderColor);

            Insets insets = getInsets();
            int x = insets.left + 2;
            int y = getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 2;
            g2.drawString(placeholder, x, y);
        }
        else
        	hasPlaceholder = false;
    }
    

    public void setPlaceholder(String placeholder) 
    {
        this.placeholder = placeholder;
        repaint();
    }

    public void setPlaceholderColor(Color color) 
    {
        this.placeholderColor = color;
        repaint();
    }

    public void setPlaceholderFont(Font font) 
    {
        this.placeholderFont = font;
        repaint();
    }
}
