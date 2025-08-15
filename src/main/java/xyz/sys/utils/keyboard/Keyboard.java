package xyz.sys.utils.keyboard;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class Keyboard {
    public static boolean isKeyPressed(int keyCode) {
        MinecraftClient client = MinecraftClient.getInstance();
        long handle = client.getWindow().getHandle();
        return GLFW.glfwGetKey(handle, keyCode) == GLFW.GLFW_PRESS;
    }
}
