package de.lessvoid.nifty.jme.render.helper;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Quad;
import com.jme.util.geom.BufferUtils;

public class VertexColoredQuad extends Quad {
  private static final long serialVersionUID = 1L;
  private ColorRGBA topLeft;
  private ColorRGBA topRight;
  private ColorRGBA bottomRight;
  private ColorRGBA bottomLeft;
  
  public VertexColoredQuad(String name, float width, float height, final ColorRGBA topLeft, final ColorRGBA topRight, final ColorRGBA bottomRight, final ColorRGBA bottomLeft) {
    super();
    this.topLeft = topLeft;
    this.topRight = topRight;
    this.bottomRight = bottomRight;
    this.bottomLeft = bottomLeft;
    updateGeometry(width, height);
  }

  public void updateGeometry(float width, float height) {
    super.updateGeometry(width, height);
    setColorBuffer(BufferUtils.createFloatBuffer(4 * getVertexCount()));
    getColorBuffer().put(new float[] { 
        topLeft.r,     topLeft.g,     topLeft.b,     topLeft.a,
        bottomLeft.r,  bottomLeft.g,  bottomLeft.b,  bottomLeft.a,
        bottomRight.r, bottomRight.g, bottomRight.b, bottomRight.a,
        topRight.r,    topRight.g,    topRight.b,    topRight.a
    });
  }
}
