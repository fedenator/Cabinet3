package net.fpalacios.cabinet.filmStrip;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.fpalacios.cabinet.flibs.util.FMath;

/**
 * Reprecenta un elemento de una tira de fotos
 */
public class FilmStripElement
{
	//Tamaño que tiene que ocupar el elemento en la tira (en porcentage)
	public double percentX;
	public double percentY;
	public double percentW;
	public double percentH;

	//Imagen que reprecenta al elemento
	private BufferedImage image;

	//Crea un elemento del tamaño dado (tamaño relativo)
	public FilmStripElement(
		BufferedImage image,
		double percentX,
		double percentY,
		double percentW,
		double percentH
	)
	{
		this.image = image;

		this.percentX = percentX;
		this.percentY = percentY;
		this.percentW = percentW;
		this.percentH = percentH;
	}

	//Pinta la imagen en el graphis dado segun el tamaño de la hoja (en pixeles)
	public void render(Graphics g, int maxWidth, int maxHeight)
	{
		//Calcula el tamaño y la pocicion en pixeles en la hoja
		int x = (int) FMath.getNumberFromPercent(maxWidth, this.percentX);
		int y = (int) FMath.getNumberFromPercent(maxHeight, this.percentY);
		int width  = (int) FMath.getNumberFromPercent(maxWidth, this.percentW);
		int height = (int) FMath.getNumberFromPercent(maxHeight, this.percentH);

		//Pinta la imagen
		g.drawImage(image, x, y, width, height, null);
	}
}
