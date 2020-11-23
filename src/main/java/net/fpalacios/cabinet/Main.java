package net.fpalacios.cabinet;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

import net.fpalacios.cabinet.view.states.*;

public class Main
{
	// Ventana principal de la aplicacion
	private static JFrame     window;
	private static JComponent currentPanel;

	public static void changePanel(JComponent panel)
	{
		if (currentPanel != null)
		{
			window.getContentPane().remove(currentPanel);
		}

		window.getContentPane().add(panel, BorderLayout.CENTER);
		currentPanel = panel;
		window.getContentPane().revalidate();
		window.repaint();
	}

	public static void setFullScreen()
	{
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setUndecorated(true);
		window.setVisible(true);
	}

	public static void setGlassPane(JComponent comp)
	{
		window.setGlassPane(comp);
		comp.setVisible(true);
	}

	public static void restart()
	{
		changePanel(new PhotoSession());
	}

	public static void makeFilmStrip(BufferedImage photo1, BufferedImage photo2, BufferedImage photo3)
	{
		changePanel(new FilmStripMaker(photo1, photo2, photo3));
	}

	/*--------------------------------- Getters y Setters --------------------------------------*/
	public static int getWidth()
	{
		return window.getWidth();
	}

	public static int getHeight()
	{
		return window.getHeight();
	}

	public static void main(String[] args)
	{
		System.setProperty("sun.java2d.opengl", "true");

		EventQueue.invokeLater(
			() ->
			{
				window = new JFrame("Cabinet 3.0");
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setFullScreen();
				window.setVisible(true);
				window.setLayout(new BorderLayout());
				restart();
			}
		);
	}
}
