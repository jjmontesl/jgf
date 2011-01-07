/*
 * JGF - Java Game Framework
 * $Id$
 *
 * Copyright (c) 2008, JGF - Java Game Framework
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     * Neither the name of the 'JGF - Java Game Framework' nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY <copyright holder> ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <copyright holder> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.jgf.jme.view;

import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.console.Console;
import net.jgf.console.ConsoleObserver;
import net.jgf.console.StreamConsoleWrapper;
import net.jgf.system.Jgf;
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
 * <p>The ConsoleView state is a simple interface to the console system.</p>
 * <p>This is simply a graphic and keyboard interface to the
 * console system. The actual console implementation is provided by 
 * the {@link Console} service.</p>
 * <p>This ViewState draws the console while it is active, and processes
 * key input from user, sending it to the referenced console system.</p>
 * <p>The console is a transparent rectangle drawn horizontally on top
 * of the screen. A prompt is shown, allowing user to enter commands that
 * are sent to the referenced console system.</p>
 * <p><b>Note:</b> See the <tt>console</tt> example application for a configuration file
 * that shows a complete working console system.</p>
 * <p><b>Note:</b> This ConsoleView requires a {@link StreamConsoleWrapper}
 * and not a {@link Console} to work. When defining your console service, you 
 * can use StreamConsoleWrapper to wrap a Console.</p>
 * 
 * @see Console
 * 
 * @author jjmontes
 */
@Configurable
public class ConsoleView extends BaseViewState implements ConsoleObserver {

	@SuppressWarnings("unused")
	private final static Logger logger = Logger.getLogger(ConsoleView.class);

	/**
	 * Number of console lines to show.
	 */
	protected int consoleLines = 14;

	/**
	 * The underlying StreamConsoleWrapper references the actual console
	 * service. 
	 */
	protected StreamConsoleWrapper console;

	/**
	 * JME root node that contains the user interface.
	 */
	protected Node consoleNode;

	/**
	 * InputText text node. This is the text containing current user input line to the console.
	 */
	private Text inputText;

	/**
	 * BufferText text lines. Contains the text objects that show the console output.
	 */
	protected Text[] bufferText;

	/**
	 * JME InputHandler for console input.
	 */
	protected InputHandler input;


	/**
	 * <p>Action to perform when a key is pressed on the keyboard. This
	 * view just passes the input to the underlying console.</p>
	 */
	public class KeyInputAction extends InputAction {

		/* (non-Javadoc)
		 * @see com.jme.input.action.InputActionInterface#performAction(com.jme.input.action.InputActionEvent)
		 */
		public void performAction(InputActionEvent evt) {

			//logger.trace("Key pressed (index=" + evt.getTriggerIndex() + ",time=" + evt.getTime() + ",press=" + evt.getTriggerPressed() + ")");
			console.acceptChar(evt.getTriggerCharacter());

		}

	}


	/* (non-Javadoc)
	 * @see net.jgf.core.state.State#load()
	 */
	@Override
	public void doLoad() {
		super.doLoad();
		// TODO: Unload: remove observer
		console.addConsoleObserver(this);
		input = new InputHandler();
		input.addAction(new KeyInputAction(), InputHandler.DEVICE_KEYBOARD, InputHandler.BUTTON_ALL, InputHandler.AXIS_ALL, false);

		initConsole();
		updateBuffer();
	}

	/**
	 * Initializes the console graphical objects.
	 */
	public void initConsole() {
		
		DisplaySystem display = DisplaySystem.getDisplaySystem();

		consoleNode = new Node();
		consoleNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);

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

		// Create the console transparent background rectangle
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

	    // Draw the console output
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


	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#update(float)
	 */
	@Override
	public void doUpdate(float tpf) {
		super.doUpdate(tpf);

		inputText.print("> " + console.getCommand());
		consoleNode.updateGeometricState(tpf, true);
	}
	
	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#input(float)
	 */
	@Override
	public void doInput(float tpf) {
		super.doInput(tpf);
		input.update(tpf);
	}

	/**
	 * Draws the console.
	 */
	@Override
	public void doRender(float tpf) {
		super.doRender (tpf);
		DisplaySystem.getDisplaySystem().getRenderer().draw(consoleNode);
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		Jgf.getDirectory().register(this, "console", config.getString(configPath + "/console/@ref"));

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

	
	/* (non-Javadoc)
	 * @see net.jgf.console.ConsoleObserver#lineAdded(net.jgf.console.Console, java.lang.String)
	 */
	@Override
	public void lineAdded(Console console, String line) {
		updateBuffer();
	}




}
