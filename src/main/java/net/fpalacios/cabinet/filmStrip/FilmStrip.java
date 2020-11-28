package net.fpalacios.cabinet.filmStrip;

import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;

import java.util.ArrayList;


import net.fpalacios.cabinet.util.FMath;
import net.fpalacios.cabinet.Assets;

import java.io.IOException;

/**
 * Representa una tira de fotos
 */
public class FilmStrip
{
	//El archivo donde esta el modelo de la tira
	private FilmStripLayout layout;

	//Lista de elemntos que conforman la tira
	private ArrayList<FilmStripElement> elements = new ArrayList<FilmStripElement>();

	//Tamaño en cm
	private float width, height;

	//Crea una tira de fotos con el modelo y las fotos dadas
	public FilmStrip(FilmStripLayout layout, BufferedImage[] photos) throws IOException
	{ 
		this.layout = layout;
		this.loadElements(photos);
	}

	private void loadElements(BufferedImage[] photos) throws IOException
	{
		this.width  = layout.width;
		this.height = layout.height;

		BufferedImage background = Assets.loadBufferedImage("assets/film_strip/Background.jpg");
		this.elements.add( new FilmStripElement(background, 0, 0, 100, 100) );

		//Carga los elementos
		int photosUsed = 0; //Contador de fotos usadas
		for (FilmStripLayout.Rectangle rect : this.layout.photos)
		{
			float percentY = rect.y;
			float percentX = rect.x;
			float percentW = rect.w;
			float percentH = rect.h;

			BufferedImage img = photos[photosUsed];
			photosUsed += 1;

			this.elements.add(new FilmStripElement(img, percentX, percentY, percentW, percentH));
		}
	}

	/**
	 * Crea una imagen de la tira de fotos en el tamaño dado (en pixeles)
	 */
	public BufferedImage createImage(int width, int height)
	{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		for (FilmStripElement element : elements)
		{
			element.render(g, width, height);
		}

		g.dispose();
		return image;
	}

	/**
	 * Crea una imagen del tamaño cargado del modelo con los dpi dados
	 */
	public BufferedImage createPrintableImage(int dpi)
	{
		int inchesW = (int) FMath.fromCMtoPixels(width, dpi);
		int inchesH = (int) FMath.fromCMtoPixels(height, dpi);
		return this.createImage(inchesW, inchesH);
	}
}
