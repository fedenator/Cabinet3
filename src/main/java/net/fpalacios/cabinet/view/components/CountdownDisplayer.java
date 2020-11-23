package net.fpalacios.cabinet.view.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import net.fpalacios.cabinet.flibs.graphics.Util;
import net.fpalacios.cabinet.flibs.graphics.animation.CodedAnimation;

public class CountdownDisplayer extends JComponent
{
	private static final long serialVersionUID = 1L;
	
	private BufferedImage textImage;
	private String        text = "";
	private long          time = 0;
	private long          duration;
	
	public CountdownDisplayer(long duration, int x, int y, int w, int h)
	{
		this.duration = duration;
		this.setBounds(x, y, w, h);
		this.setOpaque(false);
		this.setVisible(false);
	}
	
	private void changeText(String text)
	{
		if ( !this.text.equals(text) && !text.isEmpty() )
		{
			this.textImage = Util.createTextImagen(text, new Font("Serif", Font.BOLD, 300), Color.WHITE);
			this.text = text;
			this.repaint();
		}
	}
	
	public void restart()
	{
		this.text = "";
		this.time = 0;
		this.setVisible(true);
	}
	
	public void fhide()
	{
		this.setVisible(false);
	}
	
	public synchronized void addTime(long mils)
	{
		this.time += mils;
		this.changeText( "" + (int)( (this.duration - this.time) / CodedAnimation.SECOND + 1 ) );
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g.create();
		
		g2d.setColor(Color.BLACK);
	
		g2d.setComposite( AlphaComposite.SrcOver.derive(0.8f) );
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.fillOval( 0, 0, this.getWidth(), this.getHeight() );
		
		// Pinta el texto
		if ( !text.isEmpty() )
		{
			g2d.setComposite( AlphaComposite.SrcOver.derive(0.5f) );
			
			int offSetX = (int) (this.getWidth()*0.3);
			int offSetY = (int) (this.getHeight()*0.1);
			
			g2d.drawImage(
				this.textImage,
				offSetX,
				offSetY,
				this.getWidth()  - (offSetX * 2),
				this.getHeight() - (offSetY * 2),
				null
			);
		}
	}
}
