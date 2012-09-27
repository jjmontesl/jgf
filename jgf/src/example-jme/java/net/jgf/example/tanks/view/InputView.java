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

package net.jgf.example.tanks.view;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.example.tanks.entity.PlayerTank;
import net.jgf.jme.settings.KeySetting;
import net.jgf.jme.view.CursorRenderView;
import net.jgf.settings.SettingHandler;
import net.jgf.system.Jgf;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.input.InputHandler;
import com.jme.input.Mouse;
import com.jme.input.MouseInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Plane;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameState;

/**
 * <p>This InputView handles player input for JGF Tanks example game.</p> 
 */
@Configurable
public class InputView extends BaseViewState {
    
    /**
     * Class logger
     */
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(InputView.class);

    /**
     * A reference to the player whose input is being handled.
     */
    @Register (ref="entity/root/links/self")
    private net.jgf.example.tanks.entity.Player self;

    protected InputHandler inputHandler;

    private SettingHandler<Integer> keyLeft = new SettingHandler<Integer>(KeySetting.class, "#{settings/input/key/left}");
    private SettingHandler<Integer> keyRight = new SettingHandler<Integer>(KeySetting.class, "#{settings/input/key/right}");
    private SettingHandler<Integer> keyUp = new SettingHandler<Integer>(KeySetting.class, "#{settings/input/key/up}");
    private SettingHandler<Integer> keyDown = new SettingHandler<Integer>(KeySetting.class, "#{settings/input/key/down}");

    
    public InputView() {
        super();
    }

    /**
     * Key action
     */
    public class KeyInputAction extends InputAction {

        public void performAction(InputActionEvent evt) {

            // logger.info("Key pressed (index=" + evt.getTriggerIndex() +
            // ",time=" + evt.getTime() + ",press=" + evt.getTriggerPressed() +
            // ")");

            net.jgf.example.tanks.entity.Tank tank = self.getTank();
            
            if ((tank == null) || (! (tank instanceof PlayerTank))) {
                return;
            }

            PlayerTank playerTank = (PlayerTank) tank;
            
            if (evt.getTriggerIndex() == keyLeft.getValue()) {
                playerTank.setWalkLeft(evt.getTriggerPressed());
            } else if (evt.getTriggerIndex() == keyRight.getValue()) {
                playerTank.setWalkRight(evt.getTriggerPressed());
            } else if (evt.getTriggerIndex() == keyUp.getValue()) {
                playerTank.setWalkUp(evt.getTriggerPressed());
            } else if (evt.getTriggerIndex() == keyDown.getValue()) {
                playerTank.setWalkDown(evt.getTriggerPressed());
            }

        }

    }

    public class TankMouseInputAction extends InputAction {

        private Mouse mouse;

        private Vector2f screenPosition = new Vector2f();
        private Vector3f clickStartPos = new Vector3f();

        public TankMouseInputAction(Mouse mouse) {
            super();
            this.mouse = mouse;
        }

        public void performAction(InputActionEvent evt) {

            if ((self == null) || (self.getTank() == null)) return;

            // logger.info("Key pressed (index=" + evt.getTriggerIndex() +
            // ",time=" + evt.getTime() + ",press=" + evt.getTriggerPressed() +
            // ")");
            mouse.getLocalTranslation();

            screenPosition.x = MouseInput.get().getXAbsolute();
            screenPosition.y = MouseInput.get().getYAbsolute();

            DisplaySystem.getDisplaySystem().getWorldCoordinates(screenPosition, 1f, clickStartPos);

            Ray ray = new Ray(DisplaySystem.getDisplaySystem().getRenderer().getCamera()
                    .getLocation(), clickStartPos.subtractLocal(DisplaySystem.getDisplaySystem()
                            .getRenderer().getCamera().getLocation()));
            ray.getDirection().normalizeLocal();

            Plane plane = new Plane();
            plane.setPlanePoints(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(10.0f, 0.0f, 0.0f),
                    new Vector3f(0.0f, 0.0f, 10.0f));
            ray.intersectsWherePlane(plane, self.getTank().getTarget());

            if (evt.getTriggerPressed()) {
                self.getTank().setFiring(true);
            }
            
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see net.jgf.core.state.State#load()
     */
    @Override
    public void doLoad() {

        super.doLoad();

        inputHandler = new InputHandler();
        inputHandler.addAction(new KeyInputAction(), InputHandler.DEVICE_KEYBOARD,
                InputHandler.BUTTON_ALL, InputHandler.AXIS_ALL, false);

    }

    @Override
    public void doActivate() {
        super.doActivate();
        CursorRenderView cursorView = Jgf.getDirectory().getObjectAs("view/root/ingame/cursor",
                CursorRenderView.class);
        cursorView.getMouse().registerWithInputHandler(inputHandler);
        inputHandler.addAction(new TankMouseInputAction(cursorView.getMouse()),
                InputHandler.DEVICE_MOUSE, InputHandler.BUTTON_ALL, InputHandler.AXIS_ALL, false);
    }

    /**
     * Client update per frame.
     * 
     * @see GameState#update(float)
     */
    @Override
    public void doInput(float tpf) {

        inputHandler.update(tpf);

    }

    @Override
    public void readConfig(Config config, String configPath) {
        super.readConfig(config, configPath);
    }

    
}
