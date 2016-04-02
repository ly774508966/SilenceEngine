package com.shc.silenceengine.core.gameloops;

import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.IGameLoop;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.opengl.GLContext;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class VariableTimeSteppedLoop implements IGameLoop
{
    @Override
    public void performLoopFrame()
    {
        SilenceEngine.input.update();
        Game.INSTANCE.update(0);

        GLContext.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Game.INSTANCE.render(0);
    }
}
