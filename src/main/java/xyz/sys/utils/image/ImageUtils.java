package xyz.sys.utils.image;

import net.minecraft.client.texture.NativeImage;

import java.awt.image.BufferedImage;

public class ImageUtils {
    public static NativeImage convertToNative(BufferedImage img) {
        NativeImage nativeImage = new NativeImage(NativeImage.Format.ABGR, img.getWidth(), img.getHeight(), true);
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int argb = img.getRGB(x, y);

                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                int rgba = (a << 24) | (b << 16) | (g << 8) | r;
                nativeImage.setPixelColor(x, y, rgba);
            }
        }
        return nativeImage;
    }
}
