package us.raudi.manli.examples;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JFrame;

import us.raudi.manli.Amend;
import us.raudi.manli.ManliClient;
import us.raudi.manli.ManliServer;

public class GridFrame extends JFrame implements Runnable {
	private static final long serialVersionUID = 4259203701890332012L;

	private final ManliClient client;
	private GridPanel panel;
	
	public GridFrame(ManliClient c) {
		this.client = c;
		this.panel = new GridPanel((GridModel) client.getModel());
		
		setSize(800, 800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		getContentPane().add(panel);
		
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int x = panel.getBlockX(e.getX());
				int y = panel.getBlockY(e.getY());

				Amend a = new Amend();
				a.putInt("x", x);
				a.putInt("y", y);
				
				client.sendAmend(a);
			}

			@Override
			public void mousePressed(MouseEvent e) { }

			@Override
			public void mouseReleased(MouseEvent e) { }

			@Override
			public void mouseEntered(MouseEvent e) { }

			@Override
			public void mouseExited(MouseEvent e) { }
			
		});
	}
	

	@Override
	public void run() {
		while(true){
			this.repaint();
		}
	}

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, InterruptedException {
		
		ManliServer server = new ManliServer(new GridModel(8,8));
		server.start();
		
		Thread.sleep(2000);
		
		
		for(int i=0; i<3; i++) {
			ManliClient client = new ManliClient("localhost", server.getPort());
			client.connect(GridModel.class);
			
	
			Thread.sleep(2000);
			
			new Thread(new GridFrame(client)).start();
		}
	}
}
