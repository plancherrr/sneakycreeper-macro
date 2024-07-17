package plancher.features;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import plancher.utils.ChatUtils;
import plancher.utils.DrawUtils;
import plancher.utils.ScoreboardUtils;

public class PestEsp {

	public static boolean pest = false;

	@SubscribeEvent
	public void onRender(RenderWorldLastEvent e) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = mc.thePlayer;
		if (mc.thePlayer == null)
			return;
		if (ScoreboardUtils.scoreboardContains("The Garden") && pest)
			findPests(e.partialTicks);

	}

	private static void findPests(float p) {
		Minecraft mc = Minecraft.getMinecraft();
		for (Entity ent : mc.theWorld.loadedEntityList) {
			if (ent != mc.thePlayer) {
				if (ent instanceof EntitySilverfish) {
					DrawUtils.entityESPBox(ent, p, Color.white);
					DrawUtils.renderTracer(ent, Color.blue, p);
				}
			}
		}
	}

	private static boolean isPest(String name) {

		String[] pests = { "beetle", "cricket", "earthworm", "fly", "locust", "mite", "mosquito", "moth", "rat",
				"slug" };
		for (String str : pests) {
			if (name.contains(str)) {
				return true;
			}
		}

		return false;
	}
}
