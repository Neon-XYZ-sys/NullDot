package xyz.sys.utils.fonts;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontUtils {

    private static final Map<String, Font> LOADED_FONTS = new HashMap<>();
    private static final Map<String, CachedText> TEXT_CACHE = new HashMap<>();

    private static final int PADDING = 4;
    private static final int OVERSAMPLE = 2; // для чіткості

    private static class CachedText {
        final Identifier textureId;
        final int width;
        final int height;
        final int ascent; // щоб baseline був правильним

        CachedText(Identifier textureId, int width, int height, int ascent) {
            this.textureId = textureId;
            this.width = width;
            this.height = height;
            this.ascent = ascent;
        }
    }

    public static Font loadFont(String name, String absolutePath, float size) {
        try (InputStream is = FontUtils.class.getResourceAsStream(absolutePath)) {
            if (is == null) {
                throw new IllegalArgumentException("Font resource not found: " + absolutePath);
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
            LOADED_FONTS.put(name, font);
            return font;
        } catch (Exception e) {
            e.printStackTrace();
            Font fallback = new Font("Arial", Font.PLAIN, (int) size);
            LOADED_FONTS.put(name, fallback);
            return fallback;
        }
    }

    public static void drawString(MatrixStack matrices, String fontName, String text, int x, int y, int argbColor, float scale) {
        if (text == null || text.isEmpty()) return;

        CachedText cached = getOrBake(fontName, text, argbColor);
        if (cached == null) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        mc.getTextureManager().bindTexture(cached.textureId);

        matrices.push();
        matrices.scale(scale / OVERSAMPLE, scale / OVERSAMPLE, 1.0f);

        // Враховуємо ascent для коректного позиціонування верхньої частини тексту
        int adjustedY = (int) (y * OVERSAMPLE - cached.ascent * scale);

        DrawableHelper.drawTexture(
                matrices,
                (int) (x * OVERSAMPLE),
                adjustedY,
                0, 0,
                cached.width,
                cached.height,
                cached.width,
                cached.height
        );

        matrices.pop();
    }

    public static void drawCenteredString(MatrixStack matrices, String fontName, String text, int centerX, int y, int argbColor, float scale) {
        if (text == null || text.isEmpty()) return;

        CachedText cached = getOrBake(fontName, text, argbColor);
        if (cached == null) return;

        int halfWidth = (int) (cached.width * scale / (2.0f * OVERSAMPLE));

        MinecraftClient mc = MinecraftClient.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        mc.getTextureManager().bindTexture(cached.textureId);

        matrices.push();
        matrices.scale(scale / OVERSAMPLE, scale / OVERSAMPLE, 1.0f);

        // Враховуємо ascent для коректного позиціонування верхньої частини тексту
        int adjustedY = (int) (y * OVERSAMPLE - cached.ascent * scale);

        DrawableHelper.drawTexture(
                matrices,
                (centerX - halfWidth) * OVERSAMPLE,
                adjustedY,
                0, 0,
                cached.width,
                cached.height,
                cached.width,
                cached.height
        );

        matrices.pop();
    }

    public static void clearCache() {
        MinecraftClient mc = MinecraftClient.getInstance();
        for (CachedText cached : TEXT_CACHE.values()) {
            mc.getTextureManager().destroyTexture(cached.textureId);
        }
        TEXT_CACHE.clear();
    }

    private static CachedText getOrBake(String fontName, String text, int argbColor) {
        Font font = LOADED_FONTS.get(fontName);
        if (font == null) {
            font = new Font("Arial", Font.PLAIN, 18);
            LOADED_FONTS.put(fontName, font);
        }

        String key = fontName + "|" + argbColor + "|" + text;
        CachedText cached = TEXT_CACHE.get(key);
        if (cached != null) return cached;

        // Обчислюємо bounds
        BufferedImage measImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gMeas = measImg.createGraphics();
        applyHints(gMeas);
        gMeas.setFont(font);
        FontRenderContext frc = gMeas.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(text, frc);
        int ascent = gMeas.getFontMetrics().getAscent();
        gMeas.dispose();

        int textW = (int) Math.ceil(bounds.getWidth());
        int textH = (int) Math.ceil(bounds.getHeight());

        // Перевірка на нульові розміри
        if (textW <= 0 || textH <= 0) {
            return null;
        }

        int imgW = (textW + PADDING * 2) * OVERSAMPLE;
        int imgH = (textH + PADDING * 2) * OVERSAMPLE;

        BufferedImage img = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        applyHints(g);
        g.setFont(font.deriveFont(font.getSize2D() * OVERSAMPLE));
        g.setColor(new Color(argbColor, true));
        g.drawString(text, PADDING * OVERSAMPLE, (PADDING + ascent) * OVERSAMPLE);
        g.dispose();

        NativeImageBackedTexture dyn = new NativeImageBackedTexture(
                xyz.sys.utils.image.ImageUtils.convertToNative(img)
        );
        Identifier id = MinecraftClient.getInstance().getTextureManager()
                .registerDynamicTexture("font_" + key.hashCode(), dyn);

        CachedText baked = new CachedText(id, imgW, imgH, ascent);
        TEXT_CACHE.put(key, baked);
        return baked;
    }

    private static void applyHints(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }
}