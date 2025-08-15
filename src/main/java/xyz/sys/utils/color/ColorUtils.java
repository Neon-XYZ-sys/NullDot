package xyz.sys.utils.color;

public class ColorUtils {
    public static int ARGB2Int(int alpha, int red, int green, int blue){
        return ((alpha & 0xFF) << 24) |
                ((red & 0xFF) << 16) |
                ((green & 0xFF) << 8)  |
                (blue & 0xFF);
    }
}
