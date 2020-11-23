package net.fpalacios.cabinet.view.components;

import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Optional;

import javax.swing.JComponent;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import net.fpalacios.cabinet.view.states.PhotoSession;

/**
 * Componente que muestra lo que esta viendo la camara
 */
public class CameraPreview extends JComponent implements MouseListener
{
	private static final long serialVersionUID = 6777059407593823792L;
	
	private Optional<WebcamPanel> cameraPnl = Optional.empty(); // Objeto que representa la camara
	private final PhotoSession  photoSession;

	private final BufferedImage defaulImage;

	public CameraPreview(
		PhotoSession     photoSession,
		Optional<Webcam> camera,
		BufferedImage    defaultImage,
		int              x,
		int              y,
		int              width,
		int              height
	)
	{
		this.photoSession = photoSession;
		this.defaulImage  = defaultImage;

		this.setLayout( new BorderLayout() );
		this.setBounds(x, y, width, height);

		if ( camera.isPresent() )
		{
			WebcamPanel cameraPnl = new WebcamPanel(camera.get(), true);
			this.cameraPnl = Optional.of(cameraPnl);
			this.add(cameraPnl, BorderLayout.CENTER);
		}

		this.addMouseListener(this);
	}

	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if ( !this.cameraPnl.isPresent() )
		{
			g.drawImage(this.defaulImage, 0, 0, this.getWidth(), this.getHeight(), null);
		}
	}
	
	/*----------------------------- Mouse Listener -----------------------------*/
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if ( !e.isConsumed() && e.getSource() == this)
		{
			this.photoSession.takePhotos();
			e.consume();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
}
