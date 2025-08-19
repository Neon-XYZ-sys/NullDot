package xyz.sys.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;

public class RenderUtils {

    public static void drawRect(MatrixStack matrices, float x, float y, float width, float height, int color) {
        DrawableHelper.fill(matrices, (int) x, (int) y, (int) (x + width), (int) (y + height), color);
    }

    //TODO: maybe rewrite to optimization

    public static void drawRoundedRect(MatrixStack matrices, int x, int y, int width, int height, int radius, int color) {
        drawRect(matrices, x + radius, y, width - 2 * radius, height, color);

        drawRect(matrices, x+width-radius, y + radius, radius, height - 2 * radius, color);
        drawRect(matrices, x, y + radius, radius, height - 2 * radius, color);

        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        float a = (float)(color >> 24 & 255) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(7425);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        Matrix4f matrix = matrices.peek().getModel();

        buffer.begin(GL11.GL_TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, x + radius, y + radius, 0).color(r, g, b, a).next();
        for (int i = 180; i <= 270; i++) {
            double angle = Math.toRadians(i);
            buffer.vertex(matrix, (float)(x + radius + Math.sin(angle) * radius), (float)(y + radius + Math.cos(angle) * radius), 0).color(r, g, b, a).next();
        }
        tessellator.draw();

        buffer.begin(GL11.GL_TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, x + width - radius, y + radius, 0).color(r, g, b, a).next();
        for (int i = 90; i <= 180; i++) {
            double angle = Math.toRadians(i);
            buffer.vertex(matrix, (float)(x + width - radius + Math.sin(angle) * radius), (float)(y + radius + Math.cos(angle) * radius), 0).color(r, g, b, a).next();
        }
        tessellator.draw();

        buffer.begin(GL11.GL_TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, x + width - radius, y + height - radius, 0).color(r, g, b, a).next();
        for (int i = 0; i <= 90; i++) {
            double angle = Math.toRadians(i);
            buffer.vertex(matrix, (float)(x + width - radius + Math.sin(angle) * radius), (float)(y + height - radius + Math.cos(angle) * radius), 0).color(r, g, b, a).next();
        }
        tessellator.draw();

        buffer.begin(GL11.GL_TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, x + radius, y + height - radius, 0).color(r, g, b, a).next();
        for (int i = 270; i <= 360; i++) {
            double angle = Math.toRadians(i);
            buffer.vertex(matrix, (float)(x + radius + Math.sin(angle) * radius), (float)(y + height - radius + Math.cos(angle) * radius), 0).color(r, g, b, a).next();
        }
        tessellator.draw();

        RenderSystem.shadeModel(7424);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawRoundedOutline(MatrixStack matrices, int x, int y, int width, int height, int radius, float lineWidth, int color) {
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        float a = (float)(color >> 24 & 255) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.lineWidth(lineWidth);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        Matrix4f matrix = matrices.peek().getModel();

        buffer.begin(GL11.GL_LINE_STRIP, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix, x + radius, y, 0).color(r, g, b, a).next();
        buffer.vertex(matrix, x + width - radius, y, 0).color(r, g, b, a).next();

        for (int i = 90; i <= 180; i++) {
            double angle = Math.toRadians(i);
            buffer.vertex(matrix, (float)(x + width - radius + Math.sin(angle) * radius),
                    (float)(y + radius + Math.cos(angle) * radius), 0).color(r, g, b, a).next();
        }

        buffer.vertex(matrix, x + width, y + height - radius, 0).color(r, g, b, a).next();

        for (int i = 0; i <= 90; i++) {
            double angle = Math.toRadians(i);
            buffer.vertex(matrix, (float)(x + width - radius + Math.sin(angle) * radius),
                    (float)(y + height - radius + Math.cos(angle) * radius), 0).color(r, g, b, a).next();
        }

        buffer.vertex(matrix, x + radius, y + height, 0).color(r, g, b, a).next();

        for (int i = 270; i <= 360; i++) {
            double angle = Math.toRadians(i);
            buffer.vertex(matrix, (float)(x + radius + Math.sin(angle) * radius),
                    (float)(y + height - radius + Math.cos(angle) * radius), 0).color(r, g, b, a).next();
        }

        buffer.vertex(matrix, x, y + radius, 0).color(r, g, b, a).next();

        for (int i = 180; i <= 270; i++) {
            double angle = Math.toRadians(i);
            buffer.vertex(matrix, (float)(x + radius + Math.sin(angle) * radius),
                    (float)(y + radius + Math.cos(angle) * radius), 0).color(r, g, b, a).next();
        }

        buffer.vertex(matrix, x + radius, y, 0).color(r, g, b, a).next();

        tessellator.draw();

        RenderSystem.lineWidth(1.0f);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawRoundedShadow(MatrixStack matrices, int x, int y, int width, int height, int radius, int blur, int shadowColor) {
        float r = (float)(shadowColor >> 16 & 255) / 255.0F;
        float g = (float)(shadowColor >> 8 & 255) / 255.0F;
        float b = (float)(shadowColor & 255) / 255.0F;
        float baseAlpha = (float)(shadowColor >> 24 & 255) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();

        for (int i = 0; i < blur; i++) {
            float alpha = baseAlpha * (1.0f - (float)i / blur) * 0.5f;
            int offset = blur - i;

            drawRoundedShadowLayer(matrices, x - offset, y - offset,
                    width + offset * 2, height + offset * 2,
                    radius + offset, r, g, b, alpha);
        }

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    private static void drawRoundedShadowLayer(MatrixStack matrices, int x, int y, int width, int height, int radius, float r, float g, float b, float a) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        Matrix4f matrix = matrices.peek().getModel();

        buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, x + radius, y, 0).color(r, g, b, a).next();
        buffer.vertex(matrix, x + width - radius, y, 0).color(r, g, b, a).next();
        buffer.vertex(matrix, x + width - radius, y + height, 0).color(r, g, b, a).next();
        buffer.vertex(matrix, x + radius, y + height, 0).color(r, g, b, a).next();
        tessellator.draw();

        buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, x, y + radius, 0).color(r, g, b, a).next();
        buffer.vertex(matrix, x + radius, y + radius, 0).color(r, g, b, a).next();
        buffer.vertex(matrix, x + radius, y + height - radius, 0).color(r, g, b, a).next();
        buffer.vertex(matrix, x, y + height - radius, 0).color(r, g, b, a).next();
        tessellator.draw();

        buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, x + width - radius, y + radius, 0).color(r, g, b, a).next();
        buffer.vertex(matrix, x + width, y + radius, 0).color(r, g, b, a).next();
        buffer.vertex(matrix, x + width, y + height - radius, 0).color(r, g, b, a).next();
        buffer.vertex(matrix, x + width - radius, y + height - radius, 0).color(r, g, b, a).next();
        tessellator.draw();

        drawShadowCorner(matrix, buffer, x + radius, y + radius, radius, r, g, b, a, 180, 270);
        drawShadowCorner(matrix, buffer, x + width - radius, y + radius, radius, r, g, b, a, 90, 180);
        drawShadowCorner(matrix, buffer, x + width - radius, y + height - radius, radius, r, g, b, a, 0, 90);
        drawShadowCorner(matrix, buffer, x + radius, y + height - radius, radius, r, g, b, a, 270, 360);
    }

    private static void drawShadowCorner(Matrix4f matrix, BufferBuilder buffer, float centerX, float centerY, int radius, float r, float g, float b, float a, int startAngle, int endAngle) {
        buffer.begin(GL11.GL_TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix, centerX, centerY, 0).color(r, g, b, a).next();

        for (int i = startAngle; i <= endAngle; i++) {
            double angle = Math.toRadians(i);
            float edgeAlpha = a * 0.3f;
            buffer.vertex(matrix, (float)(centerX + Math.sin(angle) * radius),
                    (float)(centerY + Math.cos(angle) * radius), 0).color(r, g, b, edgeAlpha).next();
        }

        Tessellator.getInstance().draw();
    }

    public static void drawRoundedRectWithShadowAndOutline(MatrixStack matrices, int x, int y, int width, int height, int radius, int fillColor, int outlineColor, int shadowColor, int blur, float outlineWidth) {
        drawRoundedShadow(matrices, x, y, width, height, radius, blur, shadowColor);
        drawRoundedRect(matrices, x, y, width, height, radius, fillColor);

        drawRoundedOutline(matrices, x, y, width, height, radius, outlineWidth, outlineColor);
    }

    public static void drawRoundedLine(MatrixStack matrices, float x1, float y1, float x2, float y2, float width, int color) {
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        float a = (float)(color >> 24 & 255) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        Matrix4f matrix = matrices.peek().getModel();

        float dx = x2 - x1;
        float dy = y2 - y1;
        float length = (float)Math.sqrt(dx * dx + dy * dy);

        if (length == 0) return;

        dx /= length;
        dy /= length;

        float perpX = -dy * width / 2;
        float perpY = dx * width / 2;

        buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, x1 + perpX, y1 + perpY, 0).color(r, g, b, a).next();
        buffer.vertex(matrix, x2 + perpX, y2 + perpY, 0).color(r, g, b, a).next();
        buffer.vertex(matrix, x2 - perpX, y2 - perpY, 0).color(r, g, b, a).next();
        buffer.vertex(matrix, x1 - perpX, y1 - perpY, 0).color(r, g, b, a).next();
        tessellator.draw();

        drawCircle(matrices, x1, y1, width / 2, color);
        drawCircle(matrices, x2, y2, width / 2, color);

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawCircle(MatrixStack matrices, float x, float y, float radius, int color) {
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        float a = (float)(color >> 24 & 255) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        Matrix4f matrix = matrices.peek().getModel();

        buffer.begin(GL11.GL_TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, x, y, 0).color(r, g, b, a).next();

        for (int i = 0; i <= 360; i += 5) {
            double angle = Math.toRadians(i);
            buffer.vertex(matrix, (float)(x + Math.cos(angle) * radius),
                    (float)(y + Math.sin(angle) * radius), 0).color(r, g, b, a).next();
        }

        tessellator.draw();

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}