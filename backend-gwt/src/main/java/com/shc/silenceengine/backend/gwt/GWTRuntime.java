package com.shc.silenceengine.backend.gwt;

import com.google.gwt.animation.client.AnimationScheduler;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;

/**
 * The GwtRuntime class creates the GWT configuration, creates the display. It starts the native event loop and emits
 * the events required for SilenceEngine to start the game loop.
 *
 * @author Sri Harsha Chilakapati
 */
public final class GwtRuntime
{
    private GwtRuntime()
    {
    }

    public static void start()
    {
        SilenceEngine.log = new GwtLogDevice();
        SilenceEngine.display = new GwtDisplayDevice();
        SilenceEngine.input = new GwtInputDevice();
        SilenceEngine.io = new GwtIODevice();
        SilenceEngine.graphics = new GwtGraphicsDevice();

        Game.INSTANCE.init();

        AnimationScheduler.get().requestAnimationFrame(GwtRuntime::frameLoop);
    }

    private static void frameLoop(double timestamp)
    {
        SilenceEngine.gameLoop.performLoopFrame();

        // Request another frame
        AnimationScheduler.get().requestAnimationFrame(GwtRuntime::frameLoop);
    }
}
