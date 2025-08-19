package xyz.sys.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.sys.ui.clickgui.ClickGUI;
import xyz.sys.utils.keyboard.Keyboard;

@Mixin(MinecraftClient.class)
public class ClickGUIMixin {

    private boolean rightShiftPressed = false;

    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void onKeyInput(CallbackInfo ci) {
        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
            if (!rightShiftPressed) {
                MinecraftClient mc = MinecraftClient.getInstance();
                if (mc.currentScreen instanceof ClickGUI) {
                    mc.openScreen(null);
                } else {
                    mc.openScreen(new ClickGUI());
                }
            }
            rightShiftPressed = true;
        } else {
            rightShiftPressed = false;
        }
    }
}