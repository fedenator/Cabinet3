package net.fpalacios.cabinet;

import java.awt.*;
import javax.swing.*;

import java.awt.image.*;
import java.io.IOException;

import net.fpalacios.cabinet.config.Config;
import net.fpalacios.cabinet.config.Keybindings;
import net.fpalacios.cabinet.view.states.*;

public class Main
{
	private static JComponent currentPanel;
	
	public static JFrame window;

	// Configs
	public static Config      config;
	public static Keybindings keybindings;

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
		changePanel(new PhotoSession(config, keybindings));
	}

	public static void makeFilmStrip(BufferedImage photo1, BufferedImage photo2, BufferedImage photo3)
	{
		changePanel(new FilmStripMaker(config, keybindings, photo1, photo2, photo3));
	}

	private static void loadConfig()
	{
		try
		{
			config      = Assets.loadJson("config/Config.json"     , Config.class     );
			keybindings = Assets.loadJson("config/Keybindings.json", Keybindings.class);
		}
		catch(IOException e)
		{
			throw new RuntimeException("No se pudieron cargar los archivos de configuracion");
		}
	}

	public static void main(String[] args)
	{
		System.setProperty("sun.java2d.opengl", "true");
		
		loadConfig();

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
