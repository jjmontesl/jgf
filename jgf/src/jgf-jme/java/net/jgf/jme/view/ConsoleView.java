
package net.jgf.jme.view;

import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.console.Console;
import net.jgf.console.ConsoleObserver;
import net.jgf.console.StreamConsoleWrapper;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;

/**
 * This represents the view console game state. It processes
 * input and draws the console.
 */
@Configurable
public class ConsoleView extends BaseViewState implements ConsoleObserver {

	@SuppressWarnings("unused")
	private final static Logger logger = Logger.getLogger(ConsoleView.class);

	protected int consoleLines = 14;

	protected StreamConsoleWrapper console;

	protected String consoleRef;

	/**
	 * The root node which contains the user interface.
	 */
	protected Node consoleNode;

	/**
	 * InputText text node
	 */
	private Text inputText;

	/**
	 * BufferText text lines
	 */
	protected Text[] bufferText;

	protected InputHandler input;


	/**
	 * Key action
	 */
	public class KeyInputAction extends InputAction {

		public void performAction(InputActionEvent evt) {

			//logger.info("Key pressed (index=" + evt.getTriggerIndex() + ",time=" + evt.getTime() + ",press=" + evt.getTriggerPressed() + ")");

			console.acceptChar(evt.getTriggerCharacter());

		}

	}

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#load()
	 */
	@Override
	public void load() {
		super.load();
		// TODO: Unload: remove observer
		console.addConsoleObserver(this);
		input = new InputHandler();
		input.addAction(new KeyInputAction(), InputHandler.DEVICE_KEYBOARD, InputHandler.BUTTON_ALL, InputHandler.AXIS_ALL, false);

		initConsole();
		updateBuffer();
	}

	/**
	 * Initializes the console
	 */
	public void initConsole() {
		consoleNode = new Node();
		consoleNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);

		DisplaySystem display = DisplaySystem.getDisplaySystem();

		BlendState as = display.getRenderer().createBlendState();
		as.setBlendEnabled(true);
		as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		as.setDestinationFunction(BlendState.DestinationFunction.One);
		as.setTestEnabled(true);
		as.setTestFunction(BlendState.TestFunction.GreaterThan);
		as.setEnabled(true);
		TextureState font = display.getRenderer().createTextureState();
		font.setTexture(TextureManager.loadTexture(ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, "console/defaultfont.tga"),
				Texture.MinificationFilter.NearestNeighborLinearMipMap,
        Texture.MagnificationFilter.NearestNeighbor));
		font.setEnabled(true);

		float margin = 2;

		inputText = new Text("InputText", "");
		float height = (inputText.getHeight() * (consoleLines + 1)) + 2;

		inputText.setTextureCombineMode(TextureCombineMode.Replace);
		Node inputNode = new Node("InputNode");
		inputNode.attachChild(inputText);
		inputNode.setRenderState(font);
		inputNode.setRenderState(as);
		//inputNode.updateGeometricState(0.0f, true);
		inputNode.setLocalTranslation(new Vector3f(margin, display.getHeight() - (height) - 1, 0));

		// Create the console
		Quad consoleQuad = new Quad("StreamConsoleWrapper", display.getWidth() - margin * 2, height);
			consoleQuad.setLocalTranslation(new Vector3f(display.getWidth() / 2, display.getHeight() - (height / 2) - margin, 0));
			consoleQuad.setDefaultColor(new ColorRGBA(0.1f, 0.5f, 0.3f, 0.7f));
		BlendState consoleAlphaState = display.getRenderer().createBlendState();
		consoleAlphaState.setBlendEnabled(true);
		consoleAlphaState.setEnabled(true);
		consoleQuad.setRenderState(consoleAlphaState);
		consoleQuad.setLightCombineMode(LightCombineMode.Off);
		consoleQuad.updateRenderState();

    consoleNode.attachChild(consoleQuad);
    consoleNode.attachChild(inputNode);



		bufferText = new Text[consoleLines];
		for (int i = 0; i < consoleLines; i++) {
			bufferText[i] = new Text("BufferText" + i, "");
			bufferText[i].setTextureCombineMode(TextureCombineMode.Replace);
			bufferText[i].setRandomColors();
			Node bufferNode = new Node("BufferNode" + i);
			bufferNode.attachChild(bufferText[i]);
			bufferNode.setRenderState(font);
			bufferNode.setRenderState(as);
			bufferNode.updateGeometricState(0.0f, true);
			bufferNode.setLocalTranslation(new Vector3f(margin, display.getHeight() - (height) - 1 + (inputText.getHeight() * ( i + 1)), 0));
			consoleNode.attachChild(bufferNode);
		}

    consoleNode.updateRenderState();

	}

	/**
	 * Redraws the text on the buffer
	 */
	protected void updateBuffer() {

		List<String> lines = console.getLastLines(consoleLines, 0);

		int i = 0;
		for (; ((i < consoleLines) && (i <lines.size())); i++) {
			bufferText[i].print(lines.get(lines.size() - 1 - i));
		}
		for (; i < consoleLines; i++) {
			bufferText[i].print ("");
		}
	}

	/**
	 * Processes input for the console.
	 */
	@Override
	public void update(float tpf) {
		super.update(tpf);

		input.update(tpf);

		inputText.print("> " + console.getCommand());
		consoleNode.updateGeometricState(tpf, true);
	}

	/**
	 * Draws the console.
	 */
	@Override
	public void render(float tpf) {
		super.render (tpf);
		DisplaySystem.getDisplaySystem().getRenderer().draw(consoleNode);
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		net.jgf.system.Jgf.getDirectory().register(this, "console", config.getString(configPath + "/console/@ref"));

	}

	/**
	 * @return the console
	 */
	public StreamConsoleWrapper getConsole() {
		return console;
	}

	/**
	 * @param console the console to set
	 */
	public void setConsole(StreamConsoleWrapper console) {
		this.console = console;
	}

	@Override
	public void lineAdded(Console console, String line) {
		updateBuffer();
	}




}
