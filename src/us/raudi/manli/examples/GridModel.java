package us.raudi.manli.examples;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;

import us.raudi.manli.Amend;
import us.raudi.manli.Model;

public class GridModel extends Model{
	private Boolean[][] grid;
	
	public GridModel() {
		this(2,2);
	}
	
	public GridModel(int width, int height) {
		grid = new Boolean[width][height];
		
		
		grid[1][1] = true;
		grid[0][0] = false;
	}
	
	@Override
	public void register(Kryo k) {
		k.register(GridModel.class);
		k.register(Boolean[][].class);
		k.register(Boolean[].class);
	}

	@Override
	public boolean update(Amend data) {
		int x = data.getInt("x");
		int y = data.getInt("y");
		
		grid[x][y] = !getCell(x,y);
		
		return true;
	}
	
	public int getHeight() {
		return grid[0].length;
	}
	
	public int getWidth() {
		return grid.length;
	}
	
	public boolean getCell(int x, int y) {
		return grid[x][y] == null ? false : grid[x][y];
	}

	@Override
	public void addClient(Connection con) {
		// No client management needed for this application
	}

	@Override
	public void removeClient(Connection con) {
		// No client management needed for this application
	}

}
