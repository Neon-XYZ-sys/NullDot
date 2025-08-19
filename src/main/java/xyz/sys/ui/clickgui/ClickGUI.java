package xyz.sys.ui.clickgui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import xyz.sys.utils.color.ColorUtils;
import xyz.sys.utils.render.RenderUtils;

import static net.fabricmc.fabric.impl.client.indigo.Indigo.LOGGER;

public class ClickGUI extends Screen {
    public ClickGUI() {
        super(new TranslatableText("clickgui.title"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        RenderUtils.drawRoundedRect(matrices, 250, 250, 250, 250, 100, ColorUtils.ARGB2Int(255, 255, 255, 255));

        super.render(matrices, mouseX, mouseY, delta);
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}