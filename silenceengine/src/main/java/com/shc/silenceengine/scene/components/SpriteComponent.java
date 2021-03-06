/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.shc.silenceengine.scene.components;

import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.graphics.SpriteBatch;

/**
 * @author Sri Harsha Chilakapati
 */
public class SpriteComponent extends Component2D
{
    public final Color tint = Color.BLACK.copy();

    public Sprite sprite;
    public float opacity = 1;
    public int   layer   = 0;

    private TransformComponent2D transformComponent;
    private SpriteBatch          spriteBatch;

    public SpriteComponent(Sprite sprite, SpriteBatch spriteBatch)
    {
        this.sprite = sprite;
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void init()
    {
        transformComponent = entity.transformComponent;
    }

    @Override
    public void update(float deltaTime)
    {
        sprite.update(deltaTime);
    }

    @Override
    public void render(float deltaTime)
    {
        spriteBatch.render(sprite, transformComponent.transform, tint, opacity, layer);
    }
}
