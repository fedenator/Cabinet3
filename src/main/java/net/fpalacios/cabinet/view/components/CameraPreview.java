package net.fpalacios.cabinet.view.components;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import net.fpalacios.cabinet.camera.FCam;
import net.fpalacios.cabinet.view.states.PhotoSession;

/**
 * Componente que muestra lo que esta viendo la camara
 */
public class CameraPreview extends JComponent implements MouseListener
{
	private static final long serialVersionUID = 6777059407593823792L;
	
	private FCam          camera;       // Objeto que representa la camara
	private PhotoSession  photoSession;

	public  BufferedImage snapshot;     // Ultima imagen que se saco
	
	public CameraPreview(PhotoSession photoSession, FCam camera, int x, int y, int width, int height)
	{
		
		this.photoSession = photoSession;
		this.camera       = camera;
		this.snapshot     = camera.getSnapShot();
		
		EventQueue.invokeLater(
			() ->
			{
				this.setBounds(x, y, width, height);
			}
		);
		
		new Thread(
			() ->
			{
				while (true)
				{
					this.updateSnapshot();
					try
					{
						Thread.sleep(30);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		).start();
		
		this.addMouseListener(this);
	}

	private void updateSnapshot()
	{
		EventQueue.invokeLater(
			() ->
			{
				// Si el componente está inicializado
				if (this.getWidth() > 0 && this.getHeight() > 0)
				{
					BufferedImage newSnapshot = this.camera.getSnapShot();
					if (snapshot != null)
					{
						this.snapshot = newSnapshot;
						this.repaint();
					}
				}
			}
		);
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (this.snapshot != null)
		{
			g.drawImage(this.snapshot, 0, 0, this.getWidth(), this.getHeight(), null);
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
