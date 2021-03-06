
package net.jgf.example.mudvolley1.logic;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.entity.EntityManager;
import net.jgf.example.mudvolley1.MudSettings;
import net.jgf.example.mudvolley1.entity.BallEntity;
import net.jgf.example.mudvolley1.entity.PlayerEntity;
import net.jgf.logic.BaseLogicState;
import net.jgf.scene.SceneManager;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;


/**
 *
 */
@Configurable
public class InGameLogic extends BaseLogicState {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(InGameLogic.class);

	@Register (ref = "scene/manager")
	private SceneManager sceneManager;

	@Register (ref = "entity")
	private EntityManager entityManager;

	@Register (ref = "entity/root/player1")
	private PlayerEntity player1;

	@Register (ref = "entity/root/player2")
	private PlayerEntity player2;

	@Register (ref = "entity/root/ball")
	private BallEntity ball;

	@Override
	public void doUpdate(float tpf) {

		super.doUpdate(tpf);

		sceneManager.update(tpf);
		entityManager.update(tpf);

		Vector3f ballPos = ball.getSpatial().getLocalTranslation();

		// Collisions ball-floor
		if (ballPos.y < MudSettings.BALL_RADIUS) {
			if (ball.getSpeed().y < 0) ball.getSpeed().y *= -1;
			ball.bounce();
		}

		// Collision ball-borders
		if ((ball.getSpatial().getLocalTranslation().x > (MudSettings.FIELD_WIDTH - MudSettings.BALL_RADIUS ))) {
			if (ball.getSpeed().x > 0) ball.getSpeed().x *= -1;
		}
		if ((ball.getSpatial().getLocalTranslation().x < (-MudSettings.FIELD_WIDTH + MudSettings.BALL_RADIUS ))) {
			if (ball.getSpeed().x < 0) ball.getSpeed().x *= -1;
		}

		// Collision ball-net
		if (FastMath.abs(ballPos.x) < MudSettings.FIELD_NET_HALFWIDTH + MudSettings.BALL_RADIUS) {
			if (ballPos.y < MudSettings.FIELD_NET_HEIGHT) {
				ball.getSpeed().x = -ball.getSpeed().x;
			} else if (ballPos.y < MudSettings.FIELD_NET_HEIGHT + (MudSettings.BALL_RADIUS)) {
				ball.getSpeed().y = FastMath.abs(ball.getSpeed().y);
			}
			ball.bounce();
		}

		// Collision player-ball
		for (int i = 0; i < 2; i++) {

			PlayerEntity pe = (i == 0 ? player1 : player2);

			float px = pe.getSpatial().getLocalTranslation().x;
			float py = pe.getSpatial().getLocalTranslation().y;
			float bx = ball.getSpatial().getLocalTranslation().x;
			float by = ball.getSpatial().getLocalTranslation().y;
			float pr = MudSettings.PLAYER_RADIUS;
			float br = MudSettings.BALL_RADIUS;

			if (FastMath.sqr(px-bx) + FastMath.sqr(py - by) < FastMath.sqr(pr + br)  ) {
				// Calculate angle and bounce ball
				float angle = FastMath.atan((by - py) / (bx - px));
				ball.getSpeed().x = MudSettings.BALL_BOUNCE * FastMath.cos(angle) * (bx>px?1f:-1f);
				ball.getSpeed().y = MudSettings.BALL_BOUNCE * FastMath.sin(angle) * (bx>px?1f:-1f);
				ball.bounce();
			}

		}

	}





}
