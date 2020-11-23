package net.fpalacios.cabinet.camera;

import java.awt.image.BufferedImage;

import java.net.URISyntaxException;

import com.github.sarxos.webcam.Webcam;

import net.fpalacios.cabinet.flibs.util.Loader;

/**
 * Capturador de camara que usa OpenCV y trabaja con concurrencia
 * Captura todo el tiempo la camara y va dejando la ultima captura procesada guardada para devolver
 */
public class FCamOpenCV implements FCam
{
	private final Webcam camera;

	//Ultima captura procesada
	private BufferedImage image;

	public FCamOpenCV(int camera) throws URISyntaxException
	{
		this.camera = Webcam.getWebcams().get(camera);

		//Toma una captura cada 30 milisegundos
		new Thread(
			() ->
			{
				this.camera.open();

				while (true)
				{
					this.image = Loader.convertToCompatibleImage(this.takeSnapShot());

					try
					{
						Thread.sleep(30);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		).start();
	}

	/**
	 * Devuelva la ultima captura de la camaraprocesada
	 */
	@Override
	public BufferedImage getSnapShot()
	{
		return this.image;
	}

	private BufferedImage takeSnapShot()
	{
		return camera.getImage();
	}
}
