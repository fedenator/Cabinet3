package net.fpalacios.cabinet.view.components;

import java.awt.Graphics;

import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import net.fpalacios.cabinet.filmStrip.FilmStrip;

import net.fpalacios.cabinet.flibs.fson.FSON;

import java.io.IOException;

public class FilmStripPreview extends JComponent
{
	private static final long serialVersionUID = 1L;

	private BufferedImage image;

	public FilmStrip filmStrip;
	public BufferedImage[] photos;

	public FilmStripPreview(
		FSON             filmStripFile,
		int              x,
		int              y,
		int              w,
		int              h,
		BufferedImage... photos
	) throws IOException
	{
		this.photos = photos;
		this.filmStrip = new FilmStrip(filmStripFile, photos);
		this.setBounds(x, y, w, h);
		this.image = filmStrip.createImage( getWidth(), getHeight() );
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(this.image, 0, 0, this);

	}
}
