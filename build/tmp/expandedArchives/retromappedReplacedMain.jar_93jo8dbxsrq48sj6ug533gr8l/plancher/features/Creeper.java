package plancher.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import plancher.utils.ChatUtils;
import plancher.utils.DrawUtils;
import plancher.utils.FontUtils;
import plancher.utils.RotationUtils;
import plancher.utils.RotationUtils.Rotation;
import plancher.utils.ScoreboardUtils;
import plancher.utils.UngrabUtils;
import scala.collection.parallel.ParIterableLike.Count;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

public class Creeper {

	private static State state;
	private static boolean creeperMacro = false;
	public static KeyBinding creeperKey = new KeyBinding("Nyanza Macro (experimental)", 0, "Alps");
	public static KeyBinding addToclip = new KeyBinding("bp clip", 0, "Alps");
	public static BlockPos target = null;
	static int hours, minutes, seconds;
	int sprintCounter = 0;
	int rotationCounter = 0;
	int jumpingCounter = 0;
	int timerCounter = 0;
	static int stuckTimer = 0;
	static int stuckFailsafe = 0;
	static int index = 0;
	private static int killedMobs = 0;
	public static List<BlockPos> copiedList = new ArrayList<BlockPos>();
	public static BlockPos[] jumpingPos = { new BlockPos(-1, 152, -38), new BlockPos(-1, 152, -37),
			new BlockPos(-1, 152, -36), new BlockPos(2, 153, -38), new BlockPos(2, 153, -37), new BlockPos(2, 153, -36),
			new BlockPos(14, 154, -33), new BlockPos(14, 154, -32), new BlockPos(14, 154, -31),
			new BlockPos(15, 154, -31), new BlockPos(15, 154, -32), new BlockPos(15, 154, -33),
			new BlockPos(22, 155, -33), new BlockPos(21, 155, -32), new BlockPos(22, 155, -32),
			new BlockPos(22, 155, -34), new BlockPos(27, 156, -33), new BlockPos(27, 156, -32),
			new BlockPos(27, 156, -31), new BlockPos(-6, 151, -5), new BlockPos(-7, 151, -5), new BlockPos(-7, 151, -4),
			new BlockPos(-6, 151, -4), new BlockPos(-12, 152, 1), new BlockPos(5, 146, 12), new BlockPos(6, 146, 12),
			new BlockPos(6, 146, 11), new BlockPos(5, 146, 11), new BlockPos(6, 146, 10), new BlockPos(7, 146, 11),
			new BlockPos(7, 146, 10), new BlockPos(8, 146, 10), new BlockPos(7, 146, 9), new BlockPos(8, 146, 9),
			new BlockPos(8, 146, 8), };
	public static BlockPos[] bpRoute = { new BlockPos(3, 151, 30), new BlockPos(-6, 151, 28),
			new BlockPos(-13, 152, 27), new BlockPos(-23, 153, 23), new BlockPos(-26, 153, 7),
			new BlockPos(-30, 152, -1), new BlockPos(-28, 153, -10), new BlockPos(-25, 153, -16),
			new BlockPos(-21, 154, -21), new BlockPos(-17, 154, -25), new BlockPos(-13, 153, -29),
			new BlockPos(-7, 152, -33), new BlockPos(-2, 152, -37), new BlockPos(2, 153, -37),
			new BlockPos(8, 154, -35), new BlockPos(13, 154, -34), new BlockPos(20, 155, -34),
			new BlockPos(26, 156, -32), new BlockPos(32, 157, -31), new BlockPos(38, 156, -25),
			new BlockPos(37, 155, -20), new BlockPos(35, 152, -13), new BlockPos(27, 150, -8),
			new BlockPos(17, 150, -9), new BlockPos(9, 151, -11), new BlockPos(0, 150, -12), new BlockPos(-12, 152, -1),
			new BlockPos(-15, 153, 4), new BlockPos(-8, 153, 5), new BlockPos(2, 147, 10), new BlockPos(9, 147, 12),
			new BlockPos(15, 147, 16), new BlockPos(23, 146, 15), new BlockPos(27, 146, 9), new BlockPos(33, 146, 7),
			new BlockPos(38, 149, 8), new BlockPos(38, 149, 21), new BlockPos(27, 150, 29), new BlockPos(20, 151, 30),
			new BlockPos(12, 151, 30) };

	private enum State {
		MACROING, STUCK;
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent e) {
		if (Minecraft.func_71410_x().field_71439_g == null)
			return;

		if (target == null)
			return;
		if (playerLoc() == null)
			return;
		if (isCreeperMacro()) {

			if (state == State.MACROING) {

				Minecraft mc = Minecraft.func_71410_x();
				EntityPlayerSP player = mc.field_71439_g;
				if (target != null) {
					if (e.phase == Phase.START) {
						timerCounter++;
						stuckFailsafe++;
					}
					sprintCounter++;
					rotationCounter++;
					jumpingCounter++;
					KeyBinding.func_74510_a(mc.field_71474_y.field_74351_w.func_151463_i(), true);
					RotationUtils.smoothLook(RotationUtils.getRotationToBlock(target, 1), 5, null);

					if (stuckFailsafe >= 200) {
						ChatUtils.clientMessage("starting stuck failsafe");
						state = State.STUCK;
					}
					if (jumpingCounter >= 3) {
						KeyBinding.func_74510_a(Minecraft.func_71410_x().field_71474_y.field_74314_A.func_151463_i(),
								false);
					}

					switch (sprintCounter) {
					case 20:
						sprintCounter = 0;
						break;
					case 1:
						KeyBinding.func_74510_a(mc.field_71474_y.field_151444_V.func_151463_i(), true);
						break;

					default:
						break;
					}

				}
				for (BlockPos jumpingBps : jumpingPos) {
					if (doBpMatch(jumpingBps)) {
						jump();
					}
				}
				if (doBpMatch(target)) {
					KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), false);
					stuckFailsafe = 0;
					rotationCounter = 0;
					if (!(index >= bpRoute.length - 1)) {
						index += 1;
						target = bpRoute[index];
					} else if (index == bpRoute.length - 1) {
						index = 0;
						target = bpRoute[index];
					}
				}
				if (Minecraft.func_71410_x().field_71462_r != null)
					stop();
			} else if (state == State.STUCK) {

				for (BlockPos bps : bpRoute) {
					if (doBpMatch(bps)) {
						state = State.MACROING;
						target = bpRoute[1];
						index = 1;
						stuckFailsafe = 0;
						stuckTimer = 0;
					}
				}
				if (e.phase == Phase.START) {

					stuckTimer++;
					RotationUtils.smoothLook(RotationUtils.getRotationToBlock(new BlockPos(3, 152, 30), 2), 6, null);
					KeyBinding.func_74510_a(Minecraft.func_71410_x().field_71474_y.field_74311_E.func_151463_i(), false);

					switch (stuckTimer) {
					case 10:
						ChatUtils.serverMessage("/warp deep");
						break;
					case 100:
						KeyBinding.func_74510_a(Minecraft.func_71410_x().field_71474_y.field_74351_w.func_151463_i(),
								true);
					default:
						break;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onChatReceived(ClientChatReceivedEvent event) {
		String message = StringUtils.func_76338_a(event.message.func_150260_c());
		if (message.contains("Your Bat Swarm hit 1 enemy for"))
			killedMobs += 1;
		if (message.contains("Your Bat Swarm hit 2 enemies for"))
			killedMobs += 2;

	}

	@SubscribeEvent
	public void onDrawBlock(DrawBlockHighlightEvent e) {
		Minecraft mc = Minecraft.func_71410_x();
		EntityPlayerSP player = mc.field_71439_g;
		if (player == null)
			return;
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

		double d0 = e.player.field_70169_q + (e.player.field_70165_t - e.player.field_70169_q) * (double) e.partialTicks;
		double d1 = e.player.field_70167_r + (e.player.field_70163_u - e.player.field_70167_r) * (double) e.partialTicks;
		double d2 = e.player.field_70166_s + (e.player.field_70161_v - e.player.field_70166_s) * (double) e.partialTicks;

		Vec3 pos = new Vec3(d0, d1, d2);

		GL11.glTranslated(-pos.field_72450_a, -pos.field_72448_b, -pos.field_72449_c);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		if (true) {
			GL11.glDisable(GL11.GL_DEPTH_TEST); // Draw the line on top of the geometry
		}

		/*
		 * -------------------------------------------------------------------
		 */
		for (int i = 0; i < bpRoute.length - 1; i++) {
			BlockPos bp = new BlockPos(bpRoute[i].func_177958_n() + 0.5d, bpRoute[i].func_177956_o() + 1d, bpRoute[i].func_177952_p() + 0.5d);
			BlockPos nextBp = new BlockPos(bpRoute[i + 1].func_177958_n() + 0.5d, bpRoute[i + 1].func_177956_o() + 1d,
					bpRoute[i + 1].func_177952_p() + 0.5d);
			DrawUtils.drawLineWithGL(bp, nextBp, false, 226 / 255f, 247 / 255f, 2 / 255f);
		}
		DrawUtils.drawLineWithGL(new BlockPos(bpRoute[0].func_177958_n(), bpRoute[0].func_177956_o() + 1d, bpRoute[0].func_177952_p()),
				new BlockPos(bpRoute[bpRoute.length - 1].func_177958_n(), bpRoute[bpRoute.length - 1].func_177956_o() + 1,
						bpRoute[bpRoute.length - 1].func_177952_p()),
				false, 226 / 255f, 247 / 255f, 2 / 255f);

		/*
		 * -------------------------------------------------------------------
		 */
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}

	@SubscribeEvent
	public void onRender(RenderWorldLastEvent e) {
		Minecraft mc = Minecraft.func_71410_x();
		EntityPlayerSP player = mc.field_71439_g;
		if (player == null)
			return;
		for (Entity creeper : mc.field_71441_e.field_72996_f) {
			if (creeper instanceof EntityCreeper) {
				DrawUtils.entityESPBox(creeper, e.partialTicks, Color.cyan);
				DrawUtils.drawNametag("MACROER");
			}
		}
		if (isCreeperMacro()) {

			for (BlockPos bp : bpRoute) {

				if (bp != target)
					DrawUtils.highlightBlock(bp, Color.green, 0.2f, e.partialTicks);
				else
					DrawUtils.highlightBlock(bp, Color.red, 0.7f, e.partialTicks);
				DrawUtils.renderTextInWorld(mc, String.valueOf(ArrayUtils.indexOf(bpRoute, bp) + 1), bp.func_177958_n() + 0.5f,
						bp.func_177956_o() + 2.2f, bp.func_177952_p() + 0.5f, e.partialTicks, 0.1f);
			}
		} else {

			for (BlockPos bp : bpRoute) {
				if (bp != target)
					DrawUtils.highlightBlock(bp, Color.green, 0.11f, e.partialTicks);
				else
					DrawUtils.highlightBlock(bp, Color.red, 0.11f, e.partialTicks);
				DrawUtils.renderTextInWorld(mc, String.valueOf(ArrayUtils.indexOf(bpRoute, bp) + 1), bp.func_177958_n() + 0.5f,
						bp.func_177956_o() + 2.2f, bp.func_177952_p() + 0.5f, e.partialTicks, 0.1f);
			}

		}
		if (copiedList.size() > 0) {
			for (BlockPos copiedBp : copiedList) {
				DrawUtils.highlightBlock(copiedBp, Color.black, 0.5f, e.partialTicks);
			}
		}

//		DrawUtils.highlightBlock(playerLoc(), Color.RED, 0.4f, e.partialTicks);
//		for(BlockPos bps : jumpingPos) {
//			DrawUtils.highlightBlock(bps, Color.magenta, 0.5f, e.partialTicks);
//		}
	}

	@SubscribeEvent
	public void onOverlayRender(RenderGameOverlayEvent.Post event) {
		Minecraft mc = Minecraft.func_71410_x();
		if (mc.field_71439_g == null)
			return;
		if (event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE)
			return;
		if (isCreeperMacro()) {
			if (minutes > 0) {
				FontUtils.drawCenteredString(
						EnumChatFormatting.GREEN + String.valueOf(killedMobs) + " creepers killed"
								+ EnumChatFormatting.WHITE + " (" + String.valueOf(killedMobs / minutes) + "/min)",
						400, 110);
			} else {
				FontUtils.drawCenteredString(EnumChatFormatting.GREEN + String.valueOf(killedMobs) + " creepers killed",
						400, 110);
			}
			FontUtils.drawCenteredString(EnumChatFormatting.GREEN + "Nyanza probability : " + EnumChatFormatting.WHITE
					+ String.valueOf(killedMobs) + "/2,000,000" + EnumChatFormatting.GREEN + " \u2248 "
					+ EnumChatFormatting.WHITE + String.valueOf(killedMobs / 2000000) + "%", 400, 100);
			FontUtils.drawCenteredString(EnumChatFormatting.GREEN + "Macro time : " + EnumChatFormatting.WHITE
					+ String.valueOf(formatSeconds(timerCounter / 20)), 400, 130);
			FontUtils.drawCenteredString(EnumChatFormatting.GREEN + "Stuck timer : " + EnumChatFormatting.WHITE
					+ String.valueOf(stuckFailsafe * 50) + "/10,000ms", 400, 120);
		}
	}

	public static void start() {
		state = State.MACROING;
		stuckFailsafe = 0;
		stuckTimer = 0;
		target = closestBp(bpRoute);
		index = ArrayUtils.indexOf(bpRoute, target);
		ChatUtils.clientMessage("Nyanza Dye macro " + EnumChatFormatting.DARK_GREEN + "ON");
		UngrabUtils.ungrabMouse();
		copiedList.clear();
	}

	public static void stop() {
		ChatUtils.clientMessage("Nyanza Dye macro " + EnumChatFormatting.DARK_RED + "OFF");
		setCreeperMacro(false);
		KeyBinding.func_74510_a(Minecraft.func_71410_x().field_71474_y.field_74351_w.func_151463_i(), false);
		KeyBinding.func_74510_a(Minecraft.func_71410_x().field_71474_y.field_74311_E.func_151463_i(), false);
		KeyBinding.func_74510_a(Minecraft.func_71410_x().field_71474_y.field_74314_A.func_151463_i(), false);
		UngrabUtils.regrabMouse();
		;

	}

	private static void jump() {
		if (Minecraft.func_71410_x().field_71439_g == null)
			return;
		KeyBinding.func_74510_a(Minecraft.func_71410_x().field_71474_y.field_74314_A.func_151463_i(), true);

	}

	private static BlockPos closestBp(BlockPos[] route) {
		Minecraft mc = Minecraft.func_71410_x();
		double closestDistanceSquared = Double.MAX_VALUE;
		BlockPos closestBp = null;
		for (BlockPos bp : route) {
			if (mc.field_71439_g.func_174818_b(bp) < closestDistanceSquared) {
				closestBp = bp;
				closestDistanceSquared = mc.field_71439_g.func_174818_b(bp);
			}
		}
		return closestBp;
	}

	public static BlockPos playerLoc() {
		EntityPlayerSP p = Minecraft.func_71410_x().field_71439_g;
		return new BlockPos(p.field_70165_t, p.field_70163_u - 0.4d, p.field_70161_v);
	}

	public static boolean doBpMatch(BlockPos b2) {
		Minecraft mc = Minecraft.func_71410_x();

		if (mc.field_71439_g.func_174818_b(b2) < 5d)// 2.25d
			return true;
		return false;
	}

	public static boolean isCreeperMacro() {
		return creeperMacro;
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		if (isCreeperMacro())
			Creeper.stop();
	}

	public static void setCreeperMacro(boolean creeperMacro) {
		Creeper.creeperMacro = creeperMacro;
	}

	public static String formatSeconds(int totalSeconds) {
		hours = totalSeconds / 3600;
		minutes = (totalSeconds % 3600) / 60;
		seconds = totalSeconds % 60;

		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
}
