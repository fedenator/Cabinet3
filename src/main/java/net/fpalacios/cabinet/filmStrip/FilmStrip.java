package net.fpalacios.cabinet.filmStrip;

import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;

import java.util.ArrayList;

import net.fpalacios.cabinet.flibs.fson.FSON;

import net.fpalacios.cabinet.flibs.util.FMath;
import net.fpalacios.cabinet.flibs.util.Loader;

import java.io.IOException;

/**
 * Representa una tira de fotos
 */
public class FilmStrip
{
	//El archivo donde esta el modelo de la tira
	private FSON model;

	//Lista de elemntos que conforman la tira
	private ArrayList<FilmStripElement> elements = new ArrayList<FilmStripElement>();

	//Tamaño en cm
	private double width, height;

	//Crea una tira de fotos con el modelo y las fotos dadas
	public FilmStrip(FSON model, BufferedImage[] photos) throws IOException
	{
		this.model = model;
		this.loadElements(photos);
	}

	private void loadElements(BufferedImage[] photos) throws IOException
	{
		this.width  = model.getDoubleValue("Width");
		this.height = model.getDoubleValue("Height");

		//Carga el fondo si hay
		if (this.model.hasKey("Background"))
		{
			BufferedImage img = Loader.loadBufferedImage( model.getStringValue("Background") );
			this.elements.add( new FilmStripElement(img, 0, 0, 100, 100) );
		}

		//Carga los elementos
		FSON components[] = this.model.getDirectSubElements();
		int photosUsed = 0; //Contador de fotos usadas
		for (FSON comp : components)
		{
			if( comp.getTag().equals("Image") )
			{
				double percentX = comp.getDoubleValue("x");
				double percentY = comp.getDoubleValue("y");
				double percentW = comp.getDoubleValue("width");
				double percentH = comp.getDoubleValue("height");

				BufferedImage img = null;
				//Si el elemento tiene el atributo src es que es una imagen predefinida
				if ( comp.hasDirectKey("src") )
				{
					img = Loader.loadBufferedImage( comp.getStringValue("src") );
				}
				//Si no hay que usar una foto que se saco el usuario
				else
				{
					img = photos[photosUsed];
					photosUsed++;
				}

				this.elements.add(new FilmStripElement(img, percentX, percentY, percentW, percentH));
			}
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
