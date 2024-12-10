package ui.tools;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JSlider;

/**
 * A customer JSlider in which the value of the slider is reflected by a color gradient along the slider.
 */
public class SliderGradient extends JSlider 
{
    private Color color1;
    private Color color2;
    private Color ticksColor = new Color(0, 0, 0);
    private int trackSize = 5;

    public SliderGradient(int start, int end, int current, Color color1, Color color2) 
    {
    	super(start, end, current);
        setUI(new SliderGradientUI(this));
        this.color1 = color1;
        this.color2 = color2;
        setPreferredSize(new Dimension(0,0));
        setPaintTicks(true);
        setPaintLabels(true);
        setOpaque(false);
    }
    
    public Color getTicksColor() {
        return ticksColor;
    }

    public void setTicksColor(Color ticksColor) {
        this.ticksColor = ticksColor;
    }

    public int getTrackSize() {
        return trackSize;
    }

    public void setTrackSize(int trackSize) {
        this.trackSize = trackSize;
    }

    public Color getColor1() {
        return color1;
    }

    public void setColor1(Color color1) {
        this.color1 = color1;
    }

    public Color getColor2() {
        return color2;
    }

    public void setColor2(Color color2) {
        this.color2 = color2;
    }

}
