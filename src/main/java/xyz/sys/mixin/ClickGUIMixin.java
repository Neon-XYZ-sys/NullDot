package xyz.sys.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.sys.utils.keyboard.Keyboard;
import xyz.sys.utils.color.ColorUtils;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import xyz.sys.utils.render.RenderUtils;

import java.security.Key;

@Mixin(InGameHud.class)
public class ClickGUIMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if(Keyboard.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
            RenderUtils.drawRect(matrices, 10, 10, 100, 100, ColorUtils.ARGB2Int(255,255,255,255));
        }
    }
}
