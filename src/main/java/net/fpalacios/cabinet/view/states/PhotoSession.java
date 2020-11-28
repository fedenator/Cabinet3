package net.fpalacios.cabinet.view.states;

import java.io.IOException;
import java.util.Optional;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.KeyStroke;

import com.github.sarxos.webcam.Webcam;

import net.fpalacios.cabinet.Main;
import net.fpalacios.cabinet.config.Config;
import net.fpalacios.cabinet.config.Keybindings;
import net.fpalacios.cabinet.Assets;

import net.fpalacios.cabinet.graphics.animation.AnimationChain;
import net.fpalacios.cabinet.graphics.animation.CodedAnimation;
import net.fpalacios.cabinet.graphics.animation.ParallelAnimation;
import net.fpalacios.cabinet.graphics.animation.ScriptAnimation;

import net.fpalacios.cabinet.util.AwtActionsUtils;
import net.fpalacios.cabinet.view.components.CameraPreview;
import net.fpalacios.cabinet.view.components.CountdownDisplayer;
import net.fpalacios.cabinet.view.components.FlashingGlassPane;
import net.fpalacios.cabinet.view.components.PhotoDisplayer;

/**
 * Estado donde se sacan las fotos
 */
public class PhotoSession extends JLayeredPane
{
	private static final long serialVersionUID = -3298912502204354233L;

	// Imagen en caso de que no se puedan sacar fotos con la camara
	private final BufferedImage defaultImage;

	private Optional<Webcam> camera = Optional.empty();

	// Displayeres para las fotos que se sacan
	private CameraPreview cameraPreview;

	private PhotoDisplayer photoDisplayer1;
	private PhotoDisplayer photoDisplayer2;
	private PhotoDisplayer photoDisplayer3;

	private CountdownDisplayer countdownDisplayer; // Muestra la cuenta regresiva para las fotos

	private FlashingGlassPane fglassPane;          // Panel que cubre la ventana que hace la animacion de flasheo

	private AnimationChain animation;              // Animaciones de cuando sacas las fotos
	private CodedAnimation countdown;              // Animacion de cuenta regresiva

	private BufferedImage backgroundImage;

	private Config config;

	public PhotoSession(Config config, Keybindings keybindings, Optional<Webcam> camera)
	{
		this.config = config;
		this.camera = camera;

		int animationDelayMs = (int) (this.config.delay * 1000);
		
		try
		{
			defaultImage         = Assets.loadBufferedImage("assets/Default.jpg");
			this.backgroundImage = Assets.loadBufferedImage("assets/Background.jpg");
		}
		catch(IOException e)
		{
			throw new RuntimeException("Error al cargar assets", e);
		}
		
		/*------------------------------ Crea y agrega el GUI --------------------------------*/
		this.fglassPane = new FlashingGlassPane();
		Main.setGlassPane(this.fglassPane);

		this.setLayout(null);

		AwtActionsUtils.addActionToKeyStroke(this, "takePhotos", keybindings.takePhotos, () -> this.takePhotos() );

		this.getInputMap().put(KeyStroke.getKeyStroke(keybindings.takePhotos), "takePhotos");
		this.getActionMap().put( "takePhotos", AwtActionsUtils.basic( () -> this.takePhotos() ) );

		this.photoDisplayer1 = new PhotoDisplayer(this, this.defaultImage, 10, 10, 442, 242);
		this.photoDisplayer2 = new PhotoDisplayer(this, this.defaultImage, 10, 262, 442, 242);
		this.photoDisplayer3 = new PhotoDisplayer(this, this.defaultImage, 10, 516, 442, 242);
		this.cameraPreview = new CameraPreview(this, camera, this.defaultImage, 462, 10, 894, 748);

		this.countdownDisplayer = new CountdownDisplayer(
			animationDelayMs,
			(int) (Main.window.getWidth()  / 2 - 200 / 2),
			(int) (Main.window.getHeight() / 2 - 200 / 2),
			200,
			200
		);

		this.add(photoDisplayer1, photoDisplayer2, photoDisplayer3, cameraPreview);
		this.add(countdownDisplayer, POPUP_LAYER);

		this.countdown = new CodedAnimation(60, animationDelayMs);

		this.countdown.addSetup( () -> countdownDisplayer.restart() );

		this.countdown.setLoop( () -> countdownDisplayer.addTime( this.countdown.getDelay() ) );

		// Crea la animacion de sacar fotos
		this.animation = new AnimationChain(
			this.countdown,
			new ParallelAnimation(
				new ScriptAnimation(() -> this.photoDisplayer1.image = this.takeWebcamSnapshot()),
				this.photoDisplayer1.animation,
				this.fglassPane.animation
			),
			this.countdown,
			new ParallelAnimation(
				new ScriptAnimation(() -> this.photoDisplayer2.image = this.takeWebcamSnapshot()),
				this.photoDisplayer2.animation,
				this.fglassPane.animation
			),
			this.countdown,
			new ParallelAnimation(
					new ScriptAnimation(() -> this.photoDisplayer3.image = this.takeWebcamSnapshot()),
					this.photoDisplayer3.animation,
					this.fglassPane.animation
			),
			new ScriptAnimation(
				() ->
				{
					Main.makeFilmStrip(
						this.photoDisplayer1.image,
						this.photoDisplayer2.image,
						this.photoDisplayer3.image
					);
				} 
			)
		);
	}

	private BufferedImage takeWebcamSnapshot()
	{
		if (this.camera.isPresent())
		{
			return this.camera.get().getImage();
		}
		else
		{
			return this.defaultImage;
		}
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(this.backgroundImage, 0, 0, null);
	}

	// Un atajo para añadir las cosas mas simple
	private void add(JComponent... components)
	{
		for (JComponent c : components)
		{
			this.add(c, DEFAULT_LAYER);
		}
	}

	public void takePhotos()
	{
		this.animation.start();
	}
}
