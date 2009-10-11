
package net.jgf.example.tanks.entity;

import java.util.LinkedList;

import net.jgf.config.Configurable;
import net.jgf.entity.Entity;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.ai.Bresenham;
import net.jgf.example.tanks.loader.TanksMap;
import net.jgf.example.tanks.loader.TanksMap.Tile;
import net.jgf.jme.entity.SpatialEntity;
import net.jgf.system.Jgf;

import org.apache.commons.jxpath.ri.compiler.Path;
import org.apache.log4j.Logger;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 */
@Configurable
public class AITank extends Tank {

	private static final Logger logger = Logger.getLogger(AITank.class);

	protected Vector3f targetPos = new Vector3f();

	protected SpatialEntity targetEntity = null;

	protected TanksMap map;

	protected Path path;

	protected EntityGroup players;

	protected EntityGroup enemies;

	protected EntityGroup bullets;

	protected Tile finalTargetPos;


	/* (non-Javadoc)
	 * @see net.jgf.example.tanks.entity.Tank#load()
	 */
	@Override
	public void load() {
		super.load();
		Jgf.getDirectory().register(this, "targetEntity", "entity/root/players/player1");
		players = Jgf.getDirectory().getObjectAs("entity/root/players", EntityGroup.class);
		enemies = Jgf.getDirectory().getObjectAs("entity/root/enemy", EntityGroup.class);
		bullets = Jgf.getDirectory().getObjectAs("entity/root/bullets", EntityGroup.class);
		map = (TanksMap)scene.getProperties().get("map");
	}

	@Override
	public void update(float tpf) {

		if (targetEntity != null) {

			boolean calculate = false;
			calculate = true;

			if (calculate) {

				// Evaluate target
				evaluate();
				Tile to = findTarget();
				targetPos.set(map.tileToWorld(to));

			}

			direction.set(targetPos).subtractLocal(spatial.getLocalTranslation());
			direction.y = 0;

		} else {
			direction.zero();
		}

		if (direction.length() > 0.2f) {
			// Move
			direction.normalizeLocal().multLocal(0.5f);
		} else {
			/// Stand
			direction.zero();
		}

		this.setTarget(targetEntity.getSpatial().getLocalTranslation());
		this.getTarget().y = 0.5f;

		if (FastMath.rand.nextFloat() < 0.01) {
			//fire();
		}

		super.update(tpf);

	}

	private Tile findTarget() {

		int bestValue = -9999999;
		LinkedList<Tile> candidates = new LinkedList<Tile>();
		for (int i = 0; i < map.getHeight(); i++) {
			for (int j = 0; j < map.getWidth(); j++) {
				Tile tile = map.getTile(i, j);
				if ((!tile.obstacle) && (tile.dontGo <= 0) && (tile.value >= bestValue)) {
					if (tile.value > bestValue) candidates.clear();
					bestValue = tile.value;
					candidates.add(tile);
				}
			}
		}

		//logger.info("Sorting " + candidates.size() + " candidates");


		/*
		String debug="";
		for (int i = 0; i < map.getHeight(); i++) {
			for (int j = 0; j < map.getWidth(); j++) {
				Tile tile = map.getTile(i, j);
				if (tile.dontGo > 0) debug = debug + "- ";
					else debug = debug + (tile.value > 9 ? "A" : tile.value) + " ";
			}
			debug = debug + "\n";
		}
		*/
		//logger.info ("\n" + debug);

		float dist = Float.MAX_VALUE;
		Tile selTile = null;
		for (Tile tile : candidates) {
			Vector3f posCandidate = map.tileToWorld(tile);
			Vector3f posTank = targetPos.clone();
			posTank.setY(0.5f);
			float tDist = posCandidate.subtractLocal(posTank).lengthSquared();
			if (tDist < dist) {
				dist = tDist;
				selTile = tile;
			}
		}

		return (selTile);
	}

	/**
	 * @return the targetEntity
	 */
	public SpatialEntity getTargetEntity() {
		return targetEntity;
	}

	/**
	 * @param targetEntity the targetEntity to set
	 */
	public void setTargetEntity(SpatialEntity targetEntity) {
		this.targetEntity = targetEntity;
	}

	/**
	 * Fill the value and danger values of all tiles
	 */
	public void evaluate() {

		// Initial settings
		for (int i = 0; i < map.getHeight(); i++) {
			for (int j = 0; j < map.getWidth(); j++) {
				map.getTile(i, j).value = 9;
				map.getTile(i, j).dontGo = 0;
				if (map.getTile(i,j).obstacle) {
					round(i, j, 1,  -1, 0);
					round(i, j, 0,  0, 1);
				}
			}
		}

		// Account for player
		for (Entity player : players.children()) {
			PlayerTank tank = (PlayerTank) player;
			Tile tile = map.worldToTile(tank.getSpatial().getWorldTranslation());
			round(tile.row, tile.col, 8,  0, 0);
			round(tile.row, tile.col, 7,  0, 0);
			round(tile.row, tile.col, 6,  0, 0);
			round(tile.row, tile.col, 5,  0, 0);
			round(tile.row, tile.col, 4,  0, 0);
			round(tile.row, tile.col, 3,  1, 0);
			round(tile.row, tile.col, 2,  -1, 0);
			round(tile.row, tile.col, 1,  0, 1);
			round(tile.row, tile.col, 0,  0, 1);
		}

		// Account for enemies
		for (Entity tank : enemies.children()) {
			Tank enemy = (Tank) tank;
			if (enemy != this) {
				Tile tile = map.worldToTile(enemy.getSpatial().getWorldTranslation());
				round(tile.row, tile.col, 3,  -1, 0);
				round(tile.row, tile.col, 2,  0, 0);
				round(tile.row, tile.col, 1,  -1, 0);
				round(tile.row, tile.col, 0,  0, 1);
			}
		}

		// Account for bullets
		for (Entity bulletX : bullets.children()) {
			Bullet bullet = (Bullet) bulletX;
			Tile tile = map.worldToTile(bullet.getSpatial().getWorldTranslation());
			Tile hit = map.worldToTile(bullet.getTrip().hitPosition);
			Bresenham line = new Bresenham();
			line.plot(tile.col, tile.row, hit.col, hit.row);

			while (line.next()) {
				//logger.info("Painting " + line.getY() + "," + line.getX());
				round (line.getY(), line.getX(), 2,  -2, 0);
				round (line.getY(), line.getX(), 1, -2, 1);
				round (line.getY(), line.getX(), 0, 0, 1);
			}
		}


	}

	private void round(int row, int col, int r, int value, int danger) {
		for (int i = row - r; i <= row + r; i ++) {
			for (int j = col - r; j <= col + r; j++) {
				if ((i >= 0) && (i < map.getHeight()) && (j >= 0) && (j < map.getWidth())) {
					map.getTile(i, j).value += value;
					map.getTile(i, j).dontGo += danger;
				}
			}
		}
	}

}
