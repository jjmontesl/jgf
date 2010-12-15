package de.lessvoid.nifty.jme.render;

import java.net.URL;
import java.nio.FloatBuffer;
import java.util.logging.Logger;

import org.lwjgl.opengl.GL11;

import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Spatial;
import com.jme.scene.TexCoords;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.BufferUtils;
import com.jme.util.resource.ResourceLocatorTool;
import com.jmex.angelfont.Rectangle;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

/**
 * <code>JmeRenderDevice</code> is a Nifty RenderDevice implementation for JME2
 * @author void
 * @author larynx
 * @version 1.1
 * 
 * History
 * 1.0 initial version
 * 1.1 added scaled font rendering
 */
public class JmeRenderDevice implements RenderDevice 
{
  private BlendState blend;
  private BlendState multiply;
  private BlendState frameCurrentBlendState;
  
  // Added by larynx 2010.04.08
  // backport from jme3 code
  private Renderer r;
  
  // Added by larynx, 2010.04.11
  // Use always the same quad for render drawing
  private final /*VertexColoredQuad*/ Quad quadDraw = new Quad("GUI:NIFTY:QUAD");
  private final Quad quadDrawTexture = new Quad("GUI:NIFTY:TEXQUAD");
  private final Quad quadDrawSubTexture = new Quad("GUI:NIFTY:SUBTEXQUAD");

  // color object
  private final ColorRGBA colorRGBA = new ColorRGBA();
  
  // text rendering coordinates
  private float xTextPos = 0.0f;
  private float yTextPos = 0.0f;
  
  // Used for subimage rendering in renderImage
  private final TexCoords texCoords = new TexCoords(null, 2);
  private final FloatBuffer texBuffer = BufferUtils.createVector2Buffer(4);
    
  // Remember ortho state
  private boolean wasInOrthoMode = false;
  
  // Used for clipping / scissor box
  private boolean isClippingEnabled = false;
  private int currentClippingX0 = 0;
  private int currentClippingY0 = 0;
  private int currentClippingX1 = 0;
  private int currentClippingY1 = 0;
  
  // Changed by larynx 2010.04.08
  // Add common logging and serial
  private static final Logger logger = Logger.getLogger(JmeRenderDevice.class.getName());
  private static final long serialVersionUID = 1L;
  
  
  public JmeRenderDevice() 
  {
    this.r = DisplaySystem.getDisplaySystem().getRenderer();

    blend = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
    blend.setBlendEnabled(true);
    blend.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
    blend.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
    blend.setTestEnabled(false);
    blend.setEnabled(true);

    multiply = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
    multiply.setBlendEnabled(true);
    multiply.setSourceFunction(BlendState.SourceFunction.DestinationColor);
    multiply.setDestinationFunction(BlendState.DestinationFunction.Zero);
    multiply.setTestEnabled(false);
    multiply.setEnabled(true);
    
    frameCurrentBlendState = blend;
    
        
    quadDraw.setLightState(DisplaySystem.getDisplaySystem().getRenderer().createLightState());
	quadDraw.getLightState().setEnabled(false);
	quadDraw.setRenderState(blend);
	quadDraw.setCullHint(CullHint.Never);
	quadDraw.setRenderQueueMode(Renderer.QUEUE_ORTHO);
	quadDraw.setLightCombineMode(Spatial.LightCombineMode.Off);
	quadDraw.updateRenderState();
	
	quadDrawTexture.setLightState(DisplaySystem.getDisplaySystem().getRenderer().createLightState());
    quadDrawTexture.getLightState().setEnabled(false);
	quadDrawTexture.setRenderState(blend);
	quadDrawTexture.setRenderQueueMode(Renderer.QUEUE_ORTHO);    
    quadDrawTexture.setLightCombineMode(Spatial.LightCombineMode.Off);    
	quadDrawTexture.setCullHint(CullHint.Never);
	quadDrawTexture.updateRenderState();

	quadDrawSubTexture.setLightState(DisplaySystem.getDisplaySystem().getRenderer().createLightState());
	quadDrawSubTexture.getLightState().setEnabled(false);
	quadDrawSubTexture.setRenderState(blend);
	quadDrawSubTexture.setRenderQueueMode(Renderer.QUEUE_ORTHO);    
	quadDrawSubTexture.setLightCombineMode(Spatial.LightCombineMode.Off);    
	quadDrawSubTexture.setCullHint(CullHint.Never);
	quadDrawSubTexture.updateRenderState();
  }

  public int getWidth() 
  {
    return DisplaySystem.getDisplaySystem().getWidth();
  }

  public int getHeight() 
  {
    return DisplaySystem.getDisplaySystem().getHeight();
  }

  public void beginFrame() 
  {
    frameCurrentBlendState = blend;
    
    wasInOrthoMode = r.isInOrthoMode(); 
	if (!wasInOrthoMode)
	{
		r.setOrtho();
	}
		
    GL11.glDisable(GL11.GL_SCISSOR_TEST);
    isClippingEnabled = false;
    currentClippingX0 = 0;
    currentClippingY0 = 0;
    currentClippingX1 = 0;
    currentClippingY1 = 0;
    
    //logger.info("BeginFrame");
  }

  public void endFrame() 
  {
	disableClip();
	
	if (!wasInOrthoMode)
	{
		r.unsetOrtho();
	}
	  
	//logger.info("EndFrame");	  
  }

  public void clear() 
  {
   // hudNode.detachAllChildren();
  }

  public RenderImage createImage(final String filename, final boolean filterLinear) 
  {
	//logger.info("createImage " + filename);
    String jmeResourceName = filename;
    if (!filename.startsWith("/")) 
    {
      jmeResourceName = "/" + filename;
    }
    URL u = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, jmeResourceName);
    return new JmeRenderImage(u, filterLinear);
  }

  public RenderFont createFont(final String filename) 
  {
	//logger.info("createFont " + filename);
    return new JmeRenderFont(filename, this);
  }

  public void renderQuad(final int x, final int y, final int width, final int height, final Color color) 
  {
	  
	  //logger.info("renderQuad" + " x:" + x + " y:" + y + " width:" + width + " height:" + height + " color:" + color);
	  	 
	  colorRGBA.set(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());	  	  
	  quadDraw.setSolidColor(colorRGBA);
	  
	  quadDraw.getWorldTranslation().set(x + width/2, getHeight() - (y + height/2), 0.0f);
	  
	  quadDraw.resize(width, height);
	  
	  quadDraw.setRenderState(frameCurrentBlendState);
	  quadDraw.updateRenderState();
	  
	  r.draw(quadDraw);	  
  }

  public void renderQuad(final int x, final int y, final int width, final int height, final Color topLeft, final Color topRight, final Color bottomRight, final Color bottomLeft) 
  {
	  logger.info("renderQuad not implemented");
  }

  public void renderImage(final RenderImage renderImage, final int x, final int y, final int width, final int height, final Color color, final float scale) 
  {
	  //logger.info("renderImage" + " x:" + x + " y:" + y + " width:" + width + " height:" + height + " color:" + color + " scale:" + scale + " name:" + ((JmeRenderImage) renderImage).getTextureState().getTexture().getImageLocation());
	 	 	  
	  colorRGBA.set(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());	  	  
	  quadDrawTexture.setSolidColor(colorRGBA);
	  
	  quadDrawTexture.getWorldTranslation().set(x + (width/2 + 0.5f), getHeight() - (y + (height/2 + 0.5f)) , 0.0f);
	  quadDrawTexture.resize(width, height);
	  
      JmeRenderImage internalImage = (JmeRenderImage) renderImage;

      float textureWidth = (float)internalImage.getTextureWidth();
      float textureHeight = (float)internalImage.getTextureHeight();
      float imageWidth = (float)internalImage.getWidth();
      float imageHeight = (float)internalImage.getHeight();

      float u1 = imageWidth / textureWidth;
      float v1 = imageHeight / textureHeight;
	  
      texBuffer.rewind();
      texBuffer.put(0).put(0);
      texBuffer.put(0).put(v1);
      texBuffer.put(u1)  .put(v1);
      texBuffer.put(u1)  .put(0);            
      //texBuffer.flip();
           
      // assign texture coordinates to the quad
      texCoords.coords = texBuffer;
      quadDrawSubTexture.setTextureCoords(texCoords); 
      
      quadDrawTexture.setRenderState(((JmeRenderImage)renderImage).getTextureState());
	  quadDrawTexture.setRenderState(frameCurrentBlendState);
	  quadDrawTexture.updateRenderState();
	  r.draw(quadDrawTexture);	 	 	  
  }

  public void renderImage(
      final RenderImage renderImage,
      final int x,
      final int y,
      final int w,
      final int h,
      final int srcX,
      final int srcY,
      final int srcW,
      final int srcH,
      final Color color,
      final float scale,
      final int centerX,
      final int centerY) 
  {
	  // TODO: check what to do with zero arguments
	  //if ((w == 0) || (h == 0) || (srcW == 0) || (srcH == 0))
	  //  return;
	  
	  // logger.info("renderSubImage" + " x:" + x + " y:" + y + " w:" + w + " h:" + h + " srcX:" + srcX + " srcY:" + srcY + " srcW:" + srcW + " srcH:" + srcH + " color:" + color + " scale:" + scale + " name:" + ((JmeRenderImage) renderImage).getTextureState().getTexture().getImageLocation() + " centerX:" + centerX + " centerY:" + centerY); 
	  
      JmeRenderImage internalImage = (JmeRenderImage) renderImage;
	  
      float textureWidth = (float)internalImage.getTextureWidth();
      float textureHeight = (float)internalImage.getTextureHeight();

      float u0 = srcX / textureWidth;
      float v0 = srcY / textureHeight;
      float u1 = (srcX + srcW) / textureWidth;
      float v1 = (srcY + srcH) / textureHeight;

      //logger.info(startX + ", " + startY + ", " + endX + ", " + endY);
	  // correct texture application:        
      texBuffer.rewind();
      texBuffer.put(u0).put(v0);
      texBuffer.put(u0).put(v1);
      texBuffer.put(u1)  .put(v1);
      texBuffer.put(u1)  .put(v0);            
      //texBuffer.flip();
           
      // assign texture coordinates to the quad
      texCoords.coords = texBuffer;
      quadDrawSubTexture.setTextureCoords(texCoords); 
      
      // apply the texture state to the quad
      quadDrawSubTexture.setRenderState(((JmeRenderImage)renderImage).getTextureState());

	  	  	  
	  colorRGBA.set(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());	  	  
	  quadDrawSubTexture.setSolidColor(colorRGBA);

	  
      //float x0 = centerX + (x - centerX) * scale;
      //float y0 = centerY - (y - centerY) * scale;
	        
//	  quadDrawSubTexture.getWorldTranslation().set(x + w/2, getHeight() - (y + h/2), 0.0f);
    quadDrawSubTexture.getWorldTranslation().set(x + (w/2 + 0.5f), getHeight() - (y + (h/2 + 0.5f)), 0.0f);
	  		
	  //quadDrawSubTexture.setLocalScale(scale);
	  
      quadDrawSubTexture.resize(w * scale, h * scale);
      quadDrawSubTexture.setRenderState(frameCurrentBlendState);
	  quadDrawSubTexture.updateRenderState();	  	 
	  r.draw(quadDrawSubTexture);	  	
  }
  
  /**
   * <code>renderFont</code> draws a text. Scaling is done with the scale zero point in the middle of the text string (x and y).
   * @param font the JmeRenderFont to use
   * @param text the text as a string
   * @param x the coordinate of the left border
   * @param y the coordinate of the top border
   * @param Color
   * @param fontsize 1.0f is normal (100%), 1.25f is 25% bigger
   */
  public void renderFont(final RenderFont font, final String text, final int x, final int y, final Color color, final float fontSize) 
  {
	  //logger.info("renderFont " + text + " x:" + x + " y:" + y + " size:" + fontSize + " color:" + color); 
	  
	  JmeRenderFont jmeFont = (JmeRenderFont)font;
	  	        
      // recalculate font coordiantes according to font size scale
      xTextPos = x + 0.5f * jmeFont.getStringWidth(text, 1.0f)  * (1f - fontSize);
	  yTextPos = y + 0.5f * jmeFont.getSize() * (1f - fontSize);
	  	  
	  jmeFont.txt.setBox(new Rectangle(xTextPos, getHeight() - yTextPos, getWidth() - xTextPos, yTextPos));	  	  	  
      jmeFont.txt.setSize(fontSize * jmeFont.getSize());
	        
	  colorRGBA.set(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());	  	  	  
	  jmeFont.txt.setDefaultColor(colorRGBA);
    
	  jmeFont.txt.setText(text);
	  jmeFont.txt.update();
      
      r.draw(jmeFont.txt);
  }

	/**
	 * Enable clipping to the given region.
	 * @param x0 x0
	 * @param y0 y0
	 * @param x1 x1
	 * @param y1 y1
	*/
	public void enableClip(int x0, int y0, int x1, int y1)
	{
		//logger.info("enableClip x0:" + x0 + " y0:" + y0 + " x1:" + x1 + " y1:" + y1);
		
		if (isClippingEnabled && currentClippingX0 == x0 && currentClippingY0 == y0 && currentClippingX1 == x1 && currentClippingY1 == y1) 
		{
			return;
		}
		isClippingEnabled = true;
		currentClippingX0 = x0;
		currentClippingY0 = y0;
		currentClippingX1 = x1;
		currentClippingY1 = y1;
		GL11.glScissor(x0, getHeight() - y1, x1 - x0, y1 - y0);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);	  
	}

	/**
	 * Disable clipping
	 */  
	public void disableClip() 
	{
		if (!isClippingEnabled) 
		{
			return;
		}
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		isClippingEnabled = false;
		currentClippingX0 = 0;
		currentClippingY0 = 0;
		currentClippingX1 = 0;
		currentClippingY1 = 0;	  
	}

	public void setBlendMode(final BlendMode renderMode) 
	{
		if (BlendMode.BLEND.equals(renderMode)) 
		{
			frameCurrentBlendState = blend;
		} 
		else if (BlendMode.MULIPLY.equals(renderMode)) 
		{
			frameCurrentBlendState = multiply;
		}
	}
  
}
