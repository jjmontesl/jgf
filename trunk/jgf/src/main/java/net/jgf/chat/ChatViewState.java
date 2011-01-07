package net.jgf.chat;

import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.system.Jgf;
import net.jgf.view.BaseViewState;

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
 * This class draws the main chat interface on the screen. The chat interface
 * consists of a rectangle showing the received chat messages and an input area
 * accepting the user input.
 * @author Schrijver
 * @version 1.0
 */
@Configurable
public class ChatViewState extends BaseViewState implements ChatObserver {

    private int            chatLines = 14;

    private InputHandler   input;

    private ChatLogicState logic;

    private Node           chatNode;

    private Text           inputText;

    private Text[]         bufferText;

    /**
     * Constructor.
     */
    public ChatViewState() {
    }

    /**
     * Initializes the chat view graphical objects.
     */
    public void initChatView() {

        DisplaySystem display = DisplaySystem.getDisplaySystem();

        chatNode = new Node();
        chatNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);

        BlendState as = createBlendState(display);
        TextureState font = createFont(display);

        float margin = 2;
        inputText = new Text("InputText", "");
        float height = (inputText.getHeight() * (chatLines + 1)) + 2;

        inputText.setTextureCombineMode(TextureCombineMode.Replace);
        Node inputNode = createInputNode(display, as, font, margin, height);

        // Create the console transparent background rectangle
        Quad chatQuad = createChatQuad(display, margin, height);

        chatNode.attachChild(chatQuad);
        chatNode.attachChild(inputNode);

        // Draw the console output
        bufferText = new Text[chatLines];
        for (int i = 0; i < chatLines; i++) {
            bufferText[i] = new Text("BufferText" + i, "");
            bufferText[i].setTextureCombineMode(TextureCombineMode.Replace);
            bufferText[i].setRandomColors();
            Node bufferNode = createBufferNode(display, as, font, margin, height, i);
            chatNode.attachChild(bufferNode);
        }

        chatNode.updateRenderState();

    }

    private Node createBufferNode(DisplaySystem display, BlendState as, TextureState font, float margin, float height,
            int i) {
        Node bufferNode = new Node("BufferNode" + i);
        bufferNode.attachChild(bufferText[i]);
        bufferNode.setRenderState(font);
        bufferNode.setRenderState(as);
        bufferNode.updateGeometricState(0.0f, true);
        bufferNode.setLocalTranslation(new Vector3f(margin, display.getHeight() - height - 1
                + (inputText.getHeight() * (i + 1)), 0));
        return bufferNode;
    }

    private Quad createChatQuad(DisplaySystem display, float margin, float height) {
        Quad chatQuad = new Quad("StreamConsoleWrapper", display.getWidth() - margin * 2, height);
        chatQuad.setLocalTranslation(new Vector3f(display.getWidth() / 2, display.getHeight() - (height / 2) - margin,
                0));
        chatQuad.setDefaultColor(new ColorRGBA(0.1f, 0.5f, 0.3f, 0.7f));
        BlendState chatAlphaState = createBlendState(display);
        // display.getRenderer().createBlendState();
        // chatAlphaState.setBlendEnabled(true);
        // chatAlphaState.setEnabled(true);
        chatQuad.setRenderState(chatAlphaState);
        chatQuad.setLightCombineMode(LightCombineMode.Off);
        chatQuad.updateRenderState();
        return chatQuad;
    }

    private Node createInputNode(DisplaySystem display, BlendState as, TextureState font, float margin, float height) {
        Node inputNode = new Node("InputNode");
        inputNode.attachChild(inputText);
        inputNode.setRenderState(font);
        inputNode.setRenderState(as);
        // inputNode.updateGeometricState(0.0f, true);
        inputNode.setLocalTranslation(new Vector3f(margin, display.getHeight() - height - 1, 0));
        return inputNode;
    }

    private TextureState createFont(DisplaySystem display) {
        TextureState font = display.getRenderer().createTextureState();
        font.setTexture(TextureManager.loadTexture(ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE,
                "console/defaultfont.tga"), Texture.MinificationFilter.NearestNeighborLinearMipMap,
                Texture.MagnificationFilter.NearestNeighbor));
        font.setEnabled(true);
        return font;
    }

    private BlendState createBlendState(DisplaySystem display) {
        BlendState as = display.getRenderer().createBlendState();
        as.setBlendEnabled(true);
        as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        as.setDestinationFunction(BlendState.DestinationFunction.One);
        as.setTestEnabled(true);
        as.setTestFunction(BlendState.TestFunction.GreaterThan);
        as.setEnabled(true);
        return as;
    }

    /**
     * Redraws the text on the buffer.
     */
    protected void updateBuffer() {

        List < String > lines = logic.getLastLines(chatLines, 0);

        int i = 0;
        for (; i < chatLines && i < lines.size(); i++) {
            bufferText[i].print(lines.get(lines.size() - 1 - i));
        }
        for (; i < chatLines; i++) {
            bufferText[i].print("");
        }
    }

    @Override
    public void lineAdded() {
        updateBuffer();
    }

    @Override
    public void readConfig(Config config, String configPath) {
        super.readConfig(config, configPath);
        Jgf.getDirectory().register(this, "logic", config.getString(configPath + "/logic/@ref"));
    }

    @Override
    public void doLoad() {
        super.doLoad();
        // TODO: Unload: remove observer
        logic.setChatObserver(this);
        input = new InputHandler();
        input.addAction(new KeyInputAction(), InputHandler.DEVICE_KEYBOARD, InputHandler.BUTTON_ALL,
                InputHandler.AXIS_ALL, false);

        initChatView();
        updateBuffer();
    }

    @Override
    public void doUnload() {
        super.doUnload();
        logic.setChatObserver(null);
        input = null;
    }

    @Override
    public void doUpdate(float tpf) {
        super.update(tpf);

        inputText.print("> " + logic.getCurrentChatMessage());
        chatNode.updateGeometricState(tpf, true);
    }

    @Override
    public void doInput(float tpf) {
        super.input(tpf);
        input.update(tpf);
    }

    @Override
    public void doRender(float tpf) {
        super.render(tpf);
        DisplaySystem.getDisplaySystem().getRenderer().draw(chatNode);
    }

    /**
     * @return the logic
     */
    public ChatLogicState getLogic() {
        return logic;
    }

    /**
     * @param logic the logic to set
     */
    public void setLogic(ChatLogicState logic) {
        this.logic = logic;
    }

    /**
     * <p>
     * Action to perform when a key is pressed on the keyboard. This view just
     * passes the input to the underlying console.
     * </p>
     */
    public class KeyInputAction extends InputAction {

        /**
         * Constructor.
         */
        public KeyInputAction() {
        }

        @Override
        public void performAction(InputActionEvent evt) {
            // System.out.println("Key pressed (index=" + evt.getTriggerIndex()
            // + ",time=" + evt.getTime() + ",press="
            // + evt.getTriggerPressed() + ")");
            if (!logic.setSpecialCharacterState(evt.getTriggerIndex(), evt.getTriggerPressed())) {
                logic.acceptChar(evt.getTriggerCharacter());
            }
        }

    }

}
