package net.fpalacios.cabinet.filmStrip;

import java.util.List;

public class FilmStripLayout
{
	public static class Rectangle
	{
		public float x;
		public float y;
		public float w;
		public float h;
	}

	/**
	 * Ancho en cm de la tira
	 */
	public float width;

	/**
	 * Alto en cm de la tira
	 */
	public float height;

	/**
	 * Posicion donde estan las fotos en cm
	 */
	public List<Rectangle> photos;
}
