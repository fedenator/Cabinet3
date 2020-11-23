package net.fpalacios.cabinet.camera;

import java.awt.image.BufferedImage;

import java.io.IOException;

import net.fpalacios.cabinet.flibs.util.Loader;

/**
 * Capturador de camara que devuelve una imagen en vez de usar la camara
 */
public class FCamDebug implements FCam {
	private BufferedImage image;

	public FCamDebug(String pathToImage) throws IOException {
		image = Loader.loadBufferedImage(pathToImage);
	}

	/**
	 * Devuelve una imagen fija en vez de capturar la camara
	 */
	public BufferedImage getSnapShot() {
		return image;
	}


}
