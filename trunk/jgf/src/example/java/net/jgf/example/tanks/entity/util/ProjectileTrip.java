/**
 * 
 */
package net.jgf.example.tanks.entity.util;

import com.jme.math.LineSegment;
import com.jme.math.Vector3f;

public class ProjectileTrip {

	public final LineSegment segment = new LineSegment();

	public final Vector3f bounceDirection = new Vector3f();

	public final Vector3f hitPosition = new Vector3f();

}