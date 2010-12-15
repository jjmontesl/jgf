package de.lessvoid.nifty.jme.input;

import java.awt.Canvas;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lwjgl.opengl.Display;

import com.jme.input.KeyInput;
import com.jme.input.KeyInputListener;
import com.jme.input.MouseInput;
import com.jme.input.MouseInputListener;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.tools.resourceloader.ResourceLoader;

/**
 * Gets the mouse events from JME and dispatches them on request to a nifty class
 * This class has no dependency on lwjgl anymore
 * Jme version 2
 * @author larynx
 *
 */
public class JmeInputSystem implements /*Nifty*/InputSystem, /*JME2*/MouseInputListener, /*JME2*/KeyInputListener
{	
	/**
	 * <code>eventsMouse</code> is a list of mouse events, gets filled by JME events and gets polled/resetted by Nifty GUI class 
	 */
	private final List<MouseInputEvent> eventsMouse = new ArrayList<MouseInputEvent>();

	/**
	 * <code>eventsKeyboard</code> is a list of mouse events, gets filled by JME events and gets polled/resetted by Nifty GUI class 
	 */
	private final List<KeyboardInputEvent> eventsKeyboard = new ArrayList<KeyboardInputEvent>();

	/**
	 *  Remember Mouse button state
	 */
	private boolean bLastLeftMouseWasDown = false;

	/**
	 * List of notifies that should be called in case Nifty cannot process the message.
	 * Return type is ignored and all notifies get the event.
	 */
	private final Set<NiftyInputConsumer> inputConsumers=new HashSet<NiftyInputConsumer>();
	  	  
	/**
	 * Remember shift and control states
	 */
	private boolean shiftDown = false;
	private boolean controlDown = false;
	private Canvas canvas = null;

	/**
	 * <code>JmeInputSystem</code constructor adds itself as a JME input listener for mouse and keyboard 
	 */
	public JmeInputSystem()
	{
		// Add jme mouse listener		
		MouseInput.get().addListener(this);
				
		// Add jme keyboard listener
		KeyInput.get().addListener(this);
		
		initMouse();
	}
	
    @Override
    public void forwardEvents(final NiftyInputConsumer inputEventConsumer) {
      for (MouseInputEvent mouseEvent : eventsMouse) {
          boolean processed = inputEventConsumer.processMouseEvent(mouseEvent);
          if (!processed) {
              if(mouseEvent.isLeftButton()) {
                  // Clicked outside nifty panels
                  // Try to release focus
                  if(inputEventConsumer instanceof Nifty) {
                      Nifty nifty=(Nifty)inputEventConsumer;
                      Screen currentScreen = nifty.getCurrentScreen();
                      if (currentScreen!=null && !currentScreen.isNull()) {
                        currentScreen.getFocusHandler().resetFocusElements();
                      }
                  }
              }
              for(NiftyInputConsumer inputConsumer: inputConsumers) {
                  inputConsumer.processMouseEvent(mouseEvent);
              }
          }
      }
      eventsMouse.clear();

      for (KeyboardInputEvent keyEvent : eventsKeyboard) {
          boolean processed = inputEventConsumer.processKeyboardEvent(keyEvent);
          if (!processed) {
              for(NiftyInputConsumer inputConsumer: inputConsumers) {
                  inputConsumer.processKeyboardEvent(keyEvent);
              }
          }
      }
      eventsKeyboard.clear();
    } 


    /**
	 * JME keyboard input listener, gets called from JME after KeyInput.get().addListener(thisclass)
	 * @param character
	 * @param keyCode
	 * @param pressed
	 */
	@Override
	public void onKey( char character, int keyCode, boolean pressed ) 
	{             
		KeyboardInputEvent inputEvent = createKeyboardEvent(keyCode, character, pressed);
		eventsKeyboard.add(inputEvent); 
	}
  
	/**
	* JME MouseInputListener override onButton
	* gets called by JME after MouseInput.get().addListener(thisclass);
	*/
	@Override
	public void onButton(int button, boolean pressed, int x, int y)
	{
	  // Only the left mouse button is sent to nifty
	  if (button == 0)
	  {
	    if (canvas != null) {
	      y = canvas.getHeight() - y;
	    } else {
	      y = Display.getDisplayMode().getHeight() - y;
	    }
	    bLastLeftMouseWasDown = pressed; // remember left button state for dragging
	    MouseInputEvent inputEvent = new MouseInputEvent(x, y, bLastLeftMouseWasDown);
	    eventsMouse.add(inputEvent);
	  }
	}

	/**
	* JME MouseInputListener override onMove
	* gets called by JME after MouseInput.get().addListener(thisclass);
	*/
	@Override
	public void onMove(int xDelta, int yDelta, int newX, int newY)
	{
	  if (canvas != null) {
	    newY = canvas.getHeight() - newY;
	  } else {
	    newY = Display.getDisplayMode().getHeight() - newY;
	  }
	  MouseInputEvent inputEvent = new MouseInputEvent(newX, newY, bLastLeftMouseWasDown);
	  eventsMouse.add(inputEvent);
	}

	/**
	 * JME MouseInputListener override onWheel
	 * gets called by JME after MouseInput.get().addListener(thisclass);
	 */
	@Override
	public void onWheel(int wheelDelta, int x, int y) 
	{
	    // TODO Gaba> I suppose it would be nice to send the original event here, instead of a keyboard replacement.
	    // So the Set<NiftyInputConsumer>'s would be able to process it

		// Simulate cursor up/down keycodes		
		int iLinesToMove = Math.abs(wheelDelta) / 120;
		int iKeyCode = (wheelDelta < 0) ? KeyInput.KEY_DOWN : KeyInput.KEY_UP;
		for (int i = 0; i < iLinesToMove; i++)
		{
			KeyboardInputEvent inputEvent = createKeyboardEvent(iKeyCode, (char)0, true);
			eventsKeyboard.add(inputEvent);
		}
	}

	// The following code has been copied from lwjgl
	/**
	  * create KeyboardInputEvent.
	  * @param key key
	  * @param character character
	  * @param keyDown keyDown
	  * @return event
	  */
	private KeyboardInputEvent createKeyboardEvent(final int key, final char character, final boolean keyDown) 
	{
		if (isShiftDown(key, keyDown)) 
		{
			shiftDown = true;
		} 
		else if (isShiftUp(key, keyDown)) 
		{
			shiftDown = false;
		} 
		else if (isControlDown(key, keyDown)) 
		{
			controlDown = true;
		}
		else if (isControlUp(key, keyDown))
		{
			controlDown = false;
		}
		// because Nifty uses the same keyboard encoding as lwjgl does, we can directly forward
		// the keyboard event to Nifty without the need for conversion
		return new KeyboardInputEvent(key, character, keyDown, shiftDown, controlDown);
	}

	/**
	 * checks if the shift key is given.
	 * @param key key
	 * @return true when shift has been pressed and false otherwise
	 */
	private boolean isShiftKey(final int key) 
	{
		return key == KeyInput.KEY_LSHIFT || key == KeyInput.KEY_RSHIFT;
	}

	/**
	 * check if shift is down.
	 * @param key key to check
	 * @param keyDown keyDown
	 * @return true when left or right shift has been pressed
	 */
	private boolean isShiftDown(final int key, final boolean keyDown) 
	{
		return keyDown && isShiftKey(key);
	}

	/**
	 * check if shift is up.
	 * @param key key
	 * @param keyDown keyDown
	 * @return true when left or right shift has been released
	 */
	private boolean isShiftUp(final int key, final boolean keyDown) 
	{
		return !keyDown && isShiftKey(key);
	}
	
	/**
	 * check if the given key is the controlKey.
	 * @param key key
	 * @return true left or right control key pressed and false otherwise
	 */
	private boolean isControlKey(final int key) 
	{
		return key == KeyInput.KEY_RCONTROL || key == KeyInput.KEY_LCONTROL || key == KeyInput.KEY_LMETA || key == KeyInput.KEY_RMETA;
	}
	
	/**
	 * check if control key is down.
	 * @param key key
	 * @param keyDown keyDown
	 * @return controlDown
	 */
	private boolean isControlDown(final int key, final boolean keyDown) 
	{
		return keyDown && isControlKey(key);
	}
	
	/**
	 * check if control key is up.
	 * @param key key
	 * @param keyDown keyDown
	 * @return controlDown
	 */
	private boolean isControlUp(final int key, final boolean keyDown) 
	{
		return !keyDown && isControlKey(key);
	}

	public void setCanvas(Canvas canvas) {
	    this.canvas = canvas;
	}

    public void addInputNotify(NiftyInputConsumer inputConsumer) {
        this.inputConsumers.add(inputConsumer);
    }

    public void removeInputNotify(NiftyInputConsumer inputConsumer) {
        this.inputConsumers.remove(inputConsumer);
    }

    /* For software mouse
    public void update(float tpf) {
        updateMouse(tpf);
    }
    */

    /**
     * Right now I call this manually but it would be nice to be called by Nifty itself.
     */
    public void render() {
        renderMouse();
    }

    /* For software mouse
    private AbsoluteMouse cursor;
    private Node rootNodeForCursor;
    private InputHandler inputHandler;
    private TextureState cursorTextureState;
    */

    protected void initMouse() {
        MouseInput mouseInput = MouseInput.get();
        mouseInput.setCursorVisible(true);
        
        /* For software mouse, use this
        rootNodeForCursor = new Node("mouseRoot");
        final BlendState alphaState = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        alphaState.setBlendEnabled(true);
        alphaState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        alphaState.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        alphaState.setEnabled(true);
        rootNodeForCursor.setRenderState(alphaState);

        cursor = new AbsoluteMouse("mouse", DisplaySystem.getDisplaySystem().getWidth(), DisplaySystem
                .getDisplaySystem().getHeight());
        cursorTextureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        Texture cursorTexture = TextureManager.loadTexture(ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, "cursors/curs_default.png"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        // flip?
        cursorTextureState.setTexture(cursorTexture, 0);
        cursorTextureState.setEnabled(true);
        cursor.setRenderState(cursorTextureState);
        cursor.setRenderQueueMode(Renderer.QUEUE_ORTHO);
        inputHandler = new InputHandler();
        cursor.registerWithInputHandler(inputHandler);
        rootNodeForCursor.attachChild(cursor);

        rootNodeForCursor.updateRenderState();
         */
    }
    
    public void setMouse(String name) {
        MouseInput mouseInput = MouseInput.get();
        mouseInput.setHardwareCursor(ResourceLoader.getResource(name));
        
        /* For software mouse, use this
        Texture cursorTexture = TextureManager.loadTexture(ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, name), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        // flip?
        cursorTextureState.setTexture(cursorTexture, 0);
         */
    }
    
    public void setMouse(String name, int hotspotX, int hotspotY) {
        // TODO transform hotspotX and hotspotY
        MouseInput mouseInput = MouseInput.get();
        mouseInput.setHardwareCursor(ResourceLoader.getResource(name), hotspotX, hotspotY);

        /* For software mouse, use this
        setMouse(name);
        cursor.setHotSpotOffset(new Vector3f(hotspotX, hotspotY, 0f));
         */
    }
    
    protected void updateMouse(float tpf) {
        /* For software mouse, use this
        inputHandler.update(tpf);
        rootNodeForCursor.updateGeometricState(tpf, true);
         */
    }

    protected void renderMouse() {
        MouseInput.get().update();

        /* For software mouse, use this
        DisplaySystem.getDisplaySystem().getRenderer().draw(rootNodeForCursor);
         */
    }
}