package xyz.sys.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.sys.utils.color.ColorUtils;
import xyz.sys.utils.fonts.FontUtils;
import xyz.sys.utils.render.RenderUtils;

import java.awt.*;

@Mixin(InGameHud.class)
public abstract class HudMixin {

    @Inject(method = "render", at = @At(value = "TAIL"))
    private void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        FontUtils.drawString(matrices, "gilroy-bold", "HUD check", 10, 12, ColorUtils.ARGB2Int(50, 0, 255, 255), 1);
        RenderUtils.drawRoundedRect(matrices, 10, 10, 100, 20, 5, ColorUtils.ARGB2Int(100, 255, 255, 255));
    }
}
