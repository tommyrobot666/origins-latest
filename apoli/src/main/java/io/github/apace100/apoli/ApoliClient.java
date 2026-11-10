package io.github.apace100.apoli;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.apace100.apoli.integration.PowerIntegrationClient;
import io.github.apace100.apoli.networking.ModPacketsS2C;
import io.github.apace100.apoli.networking.packet.c2s.UseActivePowerTypesC2SPacket;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.type.Active;
import io.github.apace100.apoli.power.type.PowerType;
//import io.github.apace100.apoli.registry.ApoliClassDataClient;
//import io.github.apace100.apoli.screen.GameHudRender;
//import io.github.apace100.apoli.screen.PowerHudRenderer;
import io.github.apace100.apoli.util.ApoliConfigClient;
import io.github.apace100.apoli.util.keybinding.KeyBindingUtil;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ApoliClient implements ClientModInitializer {

	public static KeyMapping.Category apoliCatagory = new KeyMapping.Category(Apoli.identifier("category"));
	public static KeyMapping showPowersOnUsabilityHint;

	public static final Map<String, Boolean> lastKeyMappingStates = new HashMap<>();
	public static boolean shouldReloadWorldRenderer = false;

	@Override
	public void onInitializeClient() {

		showPowersOnUsabilityHint = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.apoli.usability_hint.show_powers", InputConstants.Type.KEYBOARD, InputConstants.KEY_LALT, apoliCatagory));
		ModPacketsS2C.register();

//		ApoliClassDataClient.registerAll();
		PowerIntegrationClient.register();

//		GameHudRender.HUD_RENDERS.add(new PowerHudRenderer());

		AutoConfig.register(ApoliConfigClient.class, JanksonConfigSerializer::new);
		Apoli.config = AutoConfig.getConfigHolder(ApoliConfigClient.class).getConfig();

	}

	public static <P extends PowerType & Active> void performActivePowerTypes(List<P> activePowerTypes) {

		List<Identifier> powerTypeIds = activePowerTypes
			.stream()
			.peek(pt -> {if (pt.isActive()) {pt.onUse();}})
			.map(PowerType::getPower)
			.map(Power::getId)
			.toList();

		if (!powerTypeIds.isEmpty()) {
			ClientPlayNetworking.send(new UseActivePowerTypesC2SPacket(powerTypeIds));
		}

	}

}
