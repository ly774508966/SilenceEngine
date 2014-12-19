package com.shc.silenceengine.graphics;

import com.shc.silenceengine.core.SilenceException;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;

/**
 * @author Sri Harsha Chilakapati
 */
public class TrueTypeFont
{
    public static final int STYLE_NORMAL = Font.PLAIN;
    public static final int STYLE_BOLD   = Font.BOLD;
    public static final int STYLE_ITALIC = Font.ITALIC;

    private static final int STANDARD_CHARACTERS = 256;

    private FontChar[] chars = new FontChar[STANDARD_CHARACTERS];

    private boolean antiAlias = true;

    private Texture     fontTexture;
    private Font        awtFont;
    private FontMetrics fontMetrics;

    public TrueTypeFont(String name, int style, int size)
    {
        this(new Font(name, style, size));
    }

    public TrueTypeFont(InputStream is)
    {
        this(is, true);
    }

    public TrueTypeFont(InputStream is, boolean antiAlias)
    {
        this (is, STYLE_NORMAL, 18, antiAlias);
    }

    public TrueTypeFont(InputStream is, int style, int size, boolean antiAlias)
    {
        try
        {
            this.awtFont = Font.createFont(Font.TRUETYPE_FONT, is);
            this.antiAlias = antiAlias;

            awtFont = awtFont.deriveFont(style, (float) size);

            createSet();
        }
        catch (Exception e)
        {
            throw new SilenceException(e.getMessage());
        }
    }

    public TrueTypeFont(Font fnt)
    {
        this(fnt, true);
    }

    public TrueTypeFont(Font fnt, boolean antiAlias)
    {
        this.awtFont   = fnt;
        this.antiAlias = antiAlias;

        createSet();
    }

    private void createSet()
    {
        // A temporary BufferedImage to get access to FontMetrics
        BufferedImage tmp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = tmp.createGraphics();

        g2d.setFont(awtFont);

        if (antiAlias)
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        fontMetrics = g2d.getFontMetrics();

        int textureWidth = 0;
        int textureHeight = 0;

        int positionX = 0;
        int positionY = 0;

        // Find texture size and FontChar info
        for (int i=0; i<STANDARD_CHARACTERS; i++)
        {
            char ch = (char) i;
            chars[i] = new FontChar();

            // Max texture Width is 512px
            if (positionX + fontMetrics.charWidth(ch) + 26 > 512)
            {
                textureHeight += fontMetrics.getHeight() + 16;
                positionX = 0;
                positionY = textureHeight;
            }

            textureWidth = Math.max(textureWidth, positionX);

            chars[i].advance = fontMetrics.stringWidth("_" + ch) - fontMetrics.charWidth('_');

            chars[i].x = positionX;
            chars[i].y = positionY;
            chars[i].w = chars[i].advance + 16;
            chars[i].h = fontMetrics.getHeight();

            positionX += chars[i].w + 10;
        }

        g2d.dispose();

        // Create a final Texture image
        BufferedImage texImage = new BufferedImage(textureWidth + 26, textureHeight, BufferedImage.TYPE_INT_ARGB);
        g2d = texImage.createGraphics();

        g2d.setFont(awtFont);
        g2d.setColor(java.awt.Color.BLACK);

        if (antiAlias)
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i=0; i<STANDARD_CHARACTERS; i++)
        {
            g2d.drawString(String.valueOf((char) i), chars[i].x, chars[i].y + fontMetrics.getAscent());
        }

        g2d.dispose();

        fontTexture = Texture.fromBufferedImage(texImage);
    }

    public void drawString(Batcher b, String text, float x, float y)
    {
        drawString(b, text, x, y, Color.WHITE);
    }

    public void drawString(Batcher b, String text, float x, float y, Color col)
    {
        Texture current = Texture.CURRENT;

        b.begin(Primitive.TRIANGLES);
        {
            fontTexture.bind();

            float startX = x;

            for (char ch : text.toCharArray())
            {
                FontChar c = chars[(int) ch];

                if (ch == '\n')
                {
                    y += fontMetrics.getHeight();
                    x = startX;

                    continue;
                }

                float minU = c.x / fontTexture.getWidth();
                float maxU = (c.x + c.w) / fontTexture.getWidth();
                float minV = c.y / fontTexture.getHeight();
                float maxV = (c.y + c.h) / fontTexture.getHeight();

                b.vertex(x, y);
                b.color(col);
                b.texCoord(minU, minV);

                b.vertex(x + chars[ch].w, y);
                b.color(col);
                b.texCoord(maxU, minV);

                b.vertex(x, y + chars[ch].h);
                b.color(col);
                b.texCoord(minU, maxV);

                b.vertex(x + chars[ch].w, y);
                b.color(col);
                b.texCoord(maxU, minV);

                b.vertex(x, y + chars[ch].h);
                b.color(col);
                b.texCoord(minU, maxV);

                b.vertex(x + chars[ch].w, y + chars[ch].h);
                b.color(col);
                b.texCoord(maxU, maxV);

                x += c.advance;
            }
        }
        b.end();

        current.bind();
    }

    public int getWidth(String str)
    {
        int width = 0;

        for (char ch : str.toCharArray())
            width += chars[(int) ch].advance;

        return width;
    }

    public TrueTypeFont derive(float size)
    {
        return new TrueTypeFont(awtFont.deriveFont(size));
    }

    public void dispose()
    {
        fontTexture.dispose();
    }

    public int getHeight()
    {
        return fontMetrics.getHeight();
    }

    private static class FontChar
    {
        public int x;
        public int y;
        public int w;
        public int h;
        public int advance;
    }
}