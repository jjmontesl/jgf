package net.jgf.example.tanks.entity;

import java.util.LinkedList;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
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

    @Register (ref = "entity/root/players/player1")
    protected SpatialEntity targetEntity;

    protected TanksMap map;

    protected Path path;

    @Register (ref = "entity/root/players")
    protected EntityGroup players;

    @Register (ref = "entity/root/enemy")
    protected EntityGroup enemies;

    @Register (ref = "entity/root/bullets")
    protected EntityGroup bullets;

    protected float firePerSecond = 0.6f;

    protected float maxSpeed = 0.7f;

    protected float actionDistance = 19.0f;

    protected Vector3f originalPosition;

    protected Tile originalTile;

    protected float nextEval;
    
    protected Bresenham line = new Bresenham();

    public float getFirePerSecond() {
        return firePerSecond;
    }

    public void setFirePerSecond(float firePerSecond) {
        this.firePerSecond = firePerSecond;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.jgf.example.tanks.entity.Tank#load()
     */
    @Override
    public void doLoad() {
        super.doLoad();
        if (scene != null) {
            map = (TanksMap) scene.getProperties().get("map");
        }
    }

    @Override
    public void doActivate() {
        super.doActivate();
        originalPosition = spatial.getWorldTranslation().clone();
        originalTile = map.worldToTile(originalPosition);
    }

    @Override
    public void doUpdate(float tpf) {

        if (targetEntity != null) {

            if (targetEntity.getSpatial().getWorldTranslation().distanceSquared(
                    this.getSpatial().getWorldTranslation()) > (actionDistance * actionDistance)) {
                return;
            }

            // Evaluate target

            nextEval -= tpf;
            if (nextEval < 0) {
                nextEval = FastMath.nextRandomFloat() * 0.2f;

                evaluate();

                Tile to = findTarget();
                if (to != null) {
                    targetPos.set(map.tileToWorld(to));
                } else {
                    // If there is no target, go to last target

                    if (targetPos.x < 0.5f && targetPos.y < 0.5f && targetPos.z < 0.5f) {
                        targetPos.set(spatial.getWorldTranslation());
                    }

                    if (spatial.getWorldTranslation().distanceSquared(targetPos) < 4.0f) {
                        // Last target reached. Choose new target to patrol.
                        while (to == null) {
                            int tX = originalTile.col + FastMath.nextRandomInt(-5, 5);
                            int tY = originalTile.row + FastMath.nextRandomInt(-5, 5);
                            Tile cTile = map.tiles[tY][tX];
                            if (cTile.obstacle != false)
                                to = cTile;
                        }
                        targetPos.set(map.tileToWorld(to));
                    }

                }

            }

            direction.set(targetPos).subtractLocal(spatial.getLocalTranslation());
            direction.y = 0;

        } else {
            direction.zero();
        }

        if (direction.length() > 0.2f) {
            // Move
            direction.normalizeLocal().multLocal(maxSpeed);
        } else {
            // / Stand
            direction.zero();
        }

        if (targetEntity != null) {
            this.setTarget(targetEntity.getSpatial().getLocalTranslation());
            this.getTarget().y = 0.5f;
        }

        if (FastMath.rand.nextFloat() < (firePerSecond * tpf)) {
            fire();
        }

        super.doUpdate(tpf);

    }

    private Tile findTarget() {

        int bestValue = 1;
        LinkedList<Tile> candidates = new LinkedList<Tile>();

        for (int i = 0; i < map.height; i++) {
            for (int j = 0; j < map.width; j++) {
                Tile tile = map.tiles[i][j];

                int dist = ((tile.row - originalTile.row) * (tile.row - originalTile.row))
                        + ((tile.col - originalTile.col) * (tile.col - originalTile.col));

                if ((dist <= 36) && (!tile.obstacle) && (tile.dontGo <= 0)
                        && (Math.abs(bestValue - tile.value) >= 2)) {
                    // if (tile.value > bestValue) candidates.clear();
                    if (tile.value > bestValue)
                        bestValue = tile.value;
                    candidates.add(tile);
                }
            }
        }

        // logger.info("Sorting " + candidates.size() + " candidates");

        /*
         * String debug=""; for (int i = 0; i < map.getHeight(); i++) { for (int
         * j = 0; j < map.getWidth(); j++) { Tile tile = map.getTile(i, j); if
         * (tile.dontGo > 0) debug = debug + "- "; else debug = debug +
         * (tile.value > 9 ? "A" : tile.value) + " "; } debug = debug + "\n"; }
         */
        // logger.info ("\n" + debug);
        float dist = Float.MAX_VALUE;
        Tile selTile = null;
        for (Tile tile : candidates) {
            Vector3f posCandidate = map.tileToWorld(tile);
            Vector3f posTank = targetPos.clone();
            posTank.setY(0.5f);
            float tDist = posCandidate.subtractLocal(spatial.getWorldTranslation()).lengthSquared();
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
     * @param targetEntity
     *            the targetEntity to set
     */
    public void setTargetEntity(SpatialEntity targetEntity) {
        this.targetEntity = targetEntity;
    }

    /**
     * Fill the value and danger values of all tiles
     */
    public void evaluate() {

        // Initial settings
        for (int i = 0; i < map.height; i++) {
            for (int j = 0; j < map.width; j++) {
                Tile tile = map.tiles[i][j];
                tile.value = 9;
                tile.dontGo = 0;
                if (tile.obstacle) {
                    round(i, j, 1, -1, 0);
                    round(i, j, 1, -2, 1);
                }
            }
        }

        // Account for player
        for (Entity player : players.children()) {
            PlayerTank tank = (PlayerTank) player;
            Tile tile = map.worldToTile(tank.getSpatial().getWorldTranslation());
            round(tile.row, tile.col, 8, 2, 0);
            round(tile.row, tile.col, 7, 2, 0);
            round(tile.row, tile.col, 6, 2, 0);
            round(tile.row, tile.col, 5, 2, 0);
            round(tile.row, tile.col, 4, 2, 0);
            round(tile.row, tile.col, 3, 1, 0);
            round(tile.row, tile.col, 2, -4, 1);
            round(tile.row, tile.col, 1, -5, 1);
        }

        // Account for enemies
        for (Entity tank : enemies.children()) {
            Tank enemy = (Tank) tank;
            if (enemy != this) {
                Tile tile = map.worldToTile(enemy.getSpatial().getWorldTranslation());
                round(tile.row, tile.col, 3, -1, 0);
                round(tile.row, tile.col, 2, -2, 1);
                round(tile.row, tile.col, 1, -3, 1);
            }
        }

        // Obstacles
        for (int i = 0; i < map.height; i++) {
            for (int j = 0; j < map.width; j++) {
                if (map.tiles[i][j].obstacle) {
                    round(i, j, 1, -2, 0);
                    round(i, j, 0, 0, 1);
                }
            }
        }

        // Account for bullets
        for (Entity bulletX : bullets.children()) {
            Bullet bullet = (Bullet) bulletX;
            Tile tile = map.worldToTile(bullet.getSpatial().getWorldTranslation());
            Tile hit = map.worldToTile(bullet.getTrip().hitPosition);
            line.plot(tile.col, tile.row, hit.col, hit.row);

            int count = 0;
            while (line.next() && count < 5) {
                // logger.info("Painting " + line.getY() + "," + line.getX());
                count++;
                round(line.getY(), line.getX(), 2, -1, 0);
                round(line.getY(), line.getX(), 1, -99, 5);
            }
        }

    }

    private void round(int row, int col, int r, int value, int danger) {

        for (int i = row - r; i <= row + r; i++) {
            for (int j = col - r; j <= col + r; j++) {
                if ((i >= 0) && (i < map.height) && (j >= 0) && (j < map.width)) {
                    if (((i - row) * (i - row)) + ((j - col) * (j - col)) <= (r * r)) {
                        Tile tile = map.tiles[i][j];
                        tile.value += value;
                        tile.dontGo += danger;
                    }
                }
            }
        }

    }

}
