package net.fpalacios.cabinet;

import java.awt.*;
import javax.swing.*;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.ipcam.IpCamDevice;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;

import java.awt.image.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

import net.fpalacios.cabinet.config.Config;
import net.fpalacios.cabinet.config.Keybindings;
import net.fpalacios.cabinet.config.WebcamConfig;
import net.fpalacios.cabinet.view.states.*;

public class Main
{
	private static JComponent currentPanel;
	private static Optional<Webcam> webcam;
	
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
		changePanel(new PhotoSession(config, keybindings, webcam));
	}

	public static void makeFilmStrip(BufferedImage photo1, BufferedImage photo2, BufferedImage photo3)
	{
		changePanel(new FilmStripMaker(config, keybindings, photo1, photo2, photo3));
	}

	private static Optional<Webcam> loadCamera()
	{
		if (!config.useCamera)
		{
			return Optional.empty();
		}

		WebcamConfig webcamConfig = config.webcamConfig;

		Webcam.setDriver(new IpCamDriver());
		try
		{
			IpCamDeviceRegistry.register(
				new IpCamDevice(
					webcamConfig.name,
					webcamConfig.url,
					webcamConfig.push? IpCamMode.PUSH : IpCamMode.PULL
				)
			);
		}
		catch (MalformedURLException e)
		{
			throw new RuntimeException(e);
		}

		Optional<Webcam> camera = Optional.ofNullable(Webcam.getDefault());
		camera.ifPresent(
			(c) ->
			{
				c.setViewSize(webcamConfig.resolution.getSize());
				c.open();
			}
		);

		return camera;
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
		webcam = loadCamera();

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
