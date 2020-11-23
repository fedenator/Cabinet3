package net.fpalacios.cabinet.camera;

import java.awt.image.BufferedImage;

import java.net.URISyntaxException;

import com.github.sarxos.webcam.Webcam;

import net.fpalacios.cabinet.flibs.util.Loader;

/**
 * Capturador de camara que usa OpenCV y trabaja con concurrencia
 * Captura todo el tiempo la camara y va dejando la ultima captura procesada guardada para devolver
 */
public class FCamOpenCV implements Runnable, FCam {

	/*--------------------------- Propiedades --------------------------------------*/
	private final Webcam camera;

	//Hilo de procesamiento en donde se va caputrando la camara
	private Thread thread = new Thread(this);

	//Ultima captura procesada
	private volatile BufferedImage image;

	/*-------------------------------- Constructores ---------------------------------*/
	/**
	 * Crea una capturador para la camara dada,
	 */
	public FCamOpenCV(int cammera) throws URISyntaxException {
		// //Obtiene la direccion del jar
		// File jarDir = new File(FCamOpenCV.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
		// String path = jarDir.getAbsolutePath();

		// //Carga la libreria de OpenCV
		// path += "/" + pathLib;
		// System.out.println(path);
		// System.load(path);

		// //Abre la camara
		// camera = new VideoCapture(cammera);

		this.camera = Webcam.getDefault();

		thread.start();
	}

	/*--------------------------------------- Funciones -----------------------------*/
	/**
	 * Devuelva la ultima captura de la camaraprocesada
	 */
	public BufferedImage getSnapShot()
	{
		return this.image;
	}

	private BufferedImage takeSnapShot()
	{
		return camera.getImage();
	}

    //Toma una captura cada 30 milisegundos
	public void run() {
		this.camera.open();

		while (true) {
			BufferedImage buffer = Loader.convertToCompatibleImage(this.takeSnapShot());
			image = buffer;
			try {
				Thread.sleep(30);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
