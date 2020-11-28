package net.fpalacios.cabinet.view.components;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import net.fpalacios.cabinet.graphics.animation.CodedAnimation;
import net.fpalacios.cabinet.view.states.PhotoSession;

/**
 * Es un componente que muestra una imagen escalada al size del componente
 */
public class PhotoDisplayer extends JComponent
{
	private static final long serialVersionUID = 3850221175976555346L;
	
	private static final int ANIMATION_TIME = 500; // Duracion de la animacion en milisegundos
	private static final int FPS            = 60;  // Actualizaciones por segundo
	
	/**
	 * Animacion de agrandarce y achicarse
	 */
	public CodedAnimation animation = new CodedAnimation(FPS, ANIMATION_TIME);

	/**
	 *  Imagen a mostrar
	 */
	public BufferedImage  image;

	private Rectangle normalPos;                  // Posicion normal
	private double    modX, modY, modW, modH;     // Cambio por frame a la posicion
	private double    nextX, nextY, nextW, nextH; // Siguiente posicion que tiene que ir
	
	public PhotoDisplayer(PhotoSession photoSession, BufferedImage image, int x, int y, int width, int height)
	{
		this.normalPos = new Rectangle(x, y, width, height);
		this.setBounds(normalPos);
		this.image = image;
		
		//Se pone arriba del todo y ocupando toda la ventana
		this.animation.addSetup(
			() ->
			{
				nextX = 0;
				nextY = 0;
				nextW = photoSession.getWidth();
				nextH = photoSession.getHeight();
				
				modX = (normalPos.x - nextX) / animation.getLoopCap();
				modY = (normalPos.y - nextY) / animation.getLoopCap();
				modW = (normalPos.width  - nextW) / animation.getLoopCap();
				modH = (normalPos.height - nextH) / animation.getLoopCap();
				photoSession.moveToFront(this);
			}
		);
		
		//Cambia el alpha
		this.animation.setLoop(
			() ->
			{
				nextX += modX;
				nextY += modY;
				nextW += modW;
				nextH += modH;
				
				if (nextX >= normalPos.x || nextY >= normalPos.y || nextW <= normalPos.width || nextH <= normalPos.height)
				{
					EventQueue.invokeLater( () -> this.setBounds(normalPos) );
				}
				else
				{
					EventQueue.invokeLater(
						() -> this.setBounds( (int) nextX, (int) nextY, (int) nextW, (int) nextH )
					);
				}
				
				this.repaint();
			}
		);
	}
	
	protected void paintComponent(Graphics g)
	{
		if (this.image != null)
		{
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.drawImage(this.image, 0, 0, this.getWidth(), this.getHeight(), null);
		}
	}
	
	public void doAnimation()
	{
		this.animation.start();
	}
}
