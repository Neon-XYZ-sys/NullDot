package xyz.sys;

import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.sys.utils.fonts.FontUtils;

public class NullDot implements ModInitializer {
	public static final String MOD_ID = "template-mod";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        FontUtils.loadFont("gilroy-bold", "assets/null-dot/font/gilroy/Gilroy-Bold.ttf", 18f);
        LOGGER.info("initialization");
	}
}