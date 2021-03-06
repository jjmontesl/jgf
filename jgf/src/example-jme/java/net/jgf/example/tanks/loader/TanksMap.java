package net.jgf.example.tanks.loader;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;


public class TanksMap {

	public int width;

	public int height;

	public String rawData;

	public static class Tile {

		public int col;

		public int row;

		public int raise;

		public String text;
		
		public String tag;
		
		public int group;

		public boolean obstacle;

		public int value;

		public int dontGo;

	}

	public Tile[][] tiles;

	/**
	 * @param tiles the tiles to set
	 */
	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	public TanksMap(int height, int width) {
		super();
		this.width = width;
		this.height = height;
		tiles = new Tile[height][width];
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	public Tile worldToTile(Vector3f worldTranslation) {
		int row = (int)FastMath.abs(worldTranslation.z);
		int col = (int) FastMath.abs(worldTranslation.x);
		if (row < 0) row = 0;
		if (row > height - 1) row = height - 1;
		if (col < 0) col = 0;
		if (col > width - 1) col = width - 1;
		return tiles[row][col];
	}

	public Vector3f tileToWorld(Tile tile) {
		return new Vector3f(0.5f + tile.col, 0.5f, 0.5f + tile.row);
	}



}