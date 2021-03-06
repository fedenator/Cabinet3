package net.fpalacios.cabinet.view.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

import net.fpalacios.cabinet.graphics.animation.AnimationChain;
import net.fpalacios.cabinet.graphics.animation.CodedAnimation;

/**
 * Un panel arriba de todos los componentes que hace un efecto de flash
 */
public class FlashingGlassPane extends JComponent
{
	private static final long serialVersionUID = 4578358888612473977L;

	// Duracion de la animacion en milisegundos
	private static final int ANIMATION_TIME = 200;

	// Valor de transparencia maxima del alpha
	private static final float MAX_ALPHA_VALUE = 1f;

	// Actualizaciones por segundo
	private static final int FPS = 60;

	// Valor del alpha(transparencia) 0=transparente ... 1=opaco
	private float alphaValue  = 0;

	// Animaciones
	private CodedAnimation increasingAnimation = new CodedAnimation(FPS, ANIMATION_TIME / 2);
	private CodedAnimation decreasingAnimation = new CodedAnimation(FPS, ANIMATION_TIME / 2);
	public  AnimationChain animation = new AnimationChain(increasingAnimation, decreasingAnimation);

	// Cuanto tiene que cambiar el alpha por frame
	private float alphaVariance =  MAX_ALPHA_VALUE / increasingAnimation.getLoopCap();

	public FlashingGlassPane()
	{
		this.setBackground(Color.WHITE);
		this.setOpaque(false);

		this.increasingAnimation.setLoop(
			() ->
			{
				float nextAlpha = alphaValue + alphaVariance;
				this.alphaValue = (nextAlpha >= MAX_ALPHA_VALUE)? MAX_ALPHA_VALUE : nextAlpha;
				this.repaint();
			}
		);

		this.decreasingAnimation.setLoop(
			() ->
			{
				float nextAlpha = alphaValue - alphaVariance;
				this.alphaValue = (nextAlpha <= 0)? 0 : nextAlpha;
				this.repaint();
			}
		);
	}

	/**
	 * Iniciar flash animation
	 */
	public void doFlashAnimation()
	{
		this.animation.start();
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		//Creamos un graphics para cambiarle el estado
		Graphics2D g2d = (Graphics2D) g.create();

		//Fijamos la transparecia segun la etapa que estemos
		g2d.setComposite( AlphaComposite.SrcOver.derive(alphaValue) );

		//Ponemos el mismo color que el fondo
		g2d.setColor( Color.WHITE );

		//Y pintamos el area que se nos pidio que pintemos
		Rectangle clip = g.getClipBounds();
		g2d.fillRect(clip.x, clip.y, clip.width, clip.height);
	}
}
