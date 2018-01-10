package us.raudi.manli.examples;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GridPanel extends JPanel{
	private static final long serialVersionUID = 3541593783695022305L;
	private GridModel model;
	
	public GridPanel(GridModel model) {
		this.model = model;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(model == null) return;

		Color clrBackground = new Color(255,255,255);
		Color clrBlocks = new Color(0,0,0);
		
		// fill background
		g.setColor(clrBackground);
		g.fillRect(-1, -1, getWidth()+2, getHeight()+2);
		g.setColor(clrBlocks);

		int blockWidth = getWidth() / model.getWidth();
		int blockHeight = getHeight() / model.getHeight();
		
		for(int i=0; i<model.getWidth(); i++) {
			int x = blockWidth * i;
			
			for(int j=0; j<model.getHeight(); j++) {
				int y = blockHeight * j;
				
				if(model.getCell(i,j))
					g.fillRect(x, y, blockWidth, blockHeight);
			}
			
		}
		
	}

	public int getBlockX(int x) {
		int blockWidth = getWidth() / model.getWidth();
		return x / blockWidth;
	}
	
	public int getBlockY(int y) {
		int blockHeight = getHeight() / model.getHeight();
		return y / blockHeight;
	}
}
