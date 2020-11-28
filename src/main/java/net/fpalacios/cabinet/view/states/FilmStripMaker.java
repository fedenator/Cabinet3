package net.fpalacios.cabinet.view.states;

import java.io.IOException;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import java.awt.image.BufferedImage;

import java.time.LocalDateTime;

import javax.print.attribute.standard.MediaSize;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import net.fpalacios.cabinet.Main;
import net.fpalacios.cabinet.config.Config;
import net.fpalacios.cabinet.config.Keybindings;
import net.fpalacios.cabinet.filmStrip.FilmStripLayout;
import net.fpalacios.cabinet.Assets;

import net.fpalacios.cabinet.graphics.Scaller;

import net.fpalacios.cabinet.printer.FPrinter;

import net.fpalacios.cabinet.util.AwtActionsUtils;
import net.fpalacios.cabinet.util.ErrorHandler;

import net.fpalacios.cabinet.view.components.FilmStripPreview;
import net.fpalacios.cabinet.view.components.ImageDisplayer;
/**
 * Form para imprimir las tiras
 */
public class FilmStripMaker extends JPanel
{
	private static final long serialVersionUID = 1L;

	private ImageDisplayer imageDisplayer1;
	private ImageDisplayer imageDisplayer2;
	private ImageDisplayer imageDisplayer3;

	private JButton btnPrint;
	private JButton btnCancel;

	private FilmStripPreview filmStripPreview;

	private BufferedImage backgroundImage;

	private Config config;

	public FilmStripMaker(Config config, Keybindings keybindings, BufferedImage... photos)
	{
		this.config = config;

		FilmStripLayout filmStripLayout;
		BufferedImage   buttonImage;
		try
		{
			filmStripLayout      = Assets.loadJson("config/FilmStripLayout.json", FilmStripLayout.class);
			buttonImage          = Assets.loadBufferedImage("assets/Button.png");
			this.backgroundImage = Assets.loadBufferedImage("assets/Background.jpg");
		}
		catch(IOException e)
		{
			throw new RuntimeException("Error al cargar el layout de la tira fotografica", e);
		}

		/*----------------- Create and show GUI -----------------*/
		this.setLayout(null);

		AwtActionsUtils.addActionToKeyStroke(this, "cancel", keybindings.cancel, () -> cancel() );
		AwtActionsUtils.addActionToKeyStroke(this, "print" , keybindings.print , () -> print()  );

		this.imageDisplayer1 = new ImageDisplayer(photos[0], 10, 10, 442, 242 );
		this.imageDisplayer2 = new ImageDisplayer(photos[1], 10, 262, 442, 242);
		this.imageDisplayer3 = new ImageDisplayer(photos[2], 10, 516, 442, 242);

		this.btnPrint  = new JButton   ("Imprimir");
		this.btnPrint.addActionListener( a -> print() );
		this.btnPrint.setBounds        (1206, 458, 150, 150);
		this.setUpButton               (this.btnPrint, buttonImage);

		this.btnCancel = new JButton    ("Cancelar");
		this.btnCancel.addActionListener( a -> cancel() );
		this.btnCancel.setBounds        (1206, 608, 150, 150);
		this.setUpButton                (this.btnCancel, buttonImage);

		try
		{
			this.filmStripPreview = new FilmStripPreview(filmStripLayout, 654, 10, 499, 748, photos);
		}
		catch (IOException e)
		{
			ErrorHandler.fatal("Error creating FilmStripPreview.", e);
		}

		this.add(
			this.imageDisplayer1,
			this.imageDisplayer2,
			this.imageDisplayer3,
			this.filmStripPreview,
			this.btnPrint,
			this.btnCancel
		);
	}

	private void setUpButton(JButton jbutton, BufferedImage buttonImage)
	{
		jbutton.setIcon( new ImageIcon( Scaller.simpleBilinear( buttonImage, jbutton.getWidth(), jbutton.getHeight() ) ) );

		jbutton.setContentAreaFilled      (false);
		jbutton.setFocusPainted           (false);
		jbutton.setBorderPainted          (false);
		jbutton.setOpaque                 (false);
		jbutton.setMargin                 ( new Insets(0, 11, 0, 0) );
		jbutton.setFont                   ( new Font("Arial", Font.PLAIN, 30) );
		jbutton.setForeground             (Color.WHITE);
		jbutton.setHorizontalTextPosition (JButton.CENTER);
		jbutton.setVerticalTextPosition   (JButton.CENTER);
	}

	//Guarda las fotos en el disco duro
	private void saveImages()
	{
		for (int i = 0; i < filmStripPreview.photos.length; i++)
		{
			//Genera un nombre que no tenga conflicto
			LocalDateTime date = LocalDateTime.now();
			String name = "" + date.getDayOfMonth() + "-" +
				date.getMonth() + "-" + date.getYear() + "--" +
				date.getHour() + "-" + date.getMinute() + "-" + date.getSecond() + "-" + i + ".png";

			try
			{
				Assets.saveImage(filmStripPreview.photos[i], "photos/" + name, "png");
			}
			catch (IOException e)
			{
				ErrorHandler.soft("Error saveing images.", e);
			}
		}
	}

	private void print()
	{
		if (this.config.print)
		{
			for (int i=0; i<this.config.copies; i++)
			{
				FPrinter.print(
					this.filmStripPreview.filmStrip.createPrintableImage(FPrinter.DPI_STANDAR_PRINTER), 10, 15, MediaSize.ISO.A6
				);
			}
		}
		
		this.saveImages();
		Main.restart();
	}


	private void cancel()
	{
		Main.restart();
	}

	private void add(JComponent... comps)
	{
		for (JComponent comp : comps) this.add(comp);
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(this.backgroundImage, 0, 0, this);
	}
}
