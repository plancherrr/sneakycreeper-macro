package plancher.utils;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;

public class DrawUtils {
	private static Minecraft mc = Minecraft.func_71410_x();
	private static final int[] DISPLAY_LISTS_2D = new int[4];
	private static final Map<Integer, Boolean> glCapMap = new HashMap<Integer, Boolean>();

	public static int getChroma(float speed, int offset) {
		return Color.HSBtoRGB(((System.currentTimeMillis() - offset * 10L) % (long) speed) / speed, 0.88F, 0.88F);
	}

	static {
		for (int i = 0; i < DISPLAY_LISTS_2D.length; i++) {
			DISPLAY_LISTS_2D[i] = glGenLists(1);
		}

		glNewList(DISPLAY_LISTS_2D[0], GL_COMPILE);

		quickDrawRect(-7F, 2F, -4F, 3F);
		quickDrawRect(4F, 2F, 7F, 3F);
		quickDrawRect(-7F, 0.5F, -6F, 3F);
		quickDrawRect(6F, 0.5F, 7F, 3F);

		glEndList();

		glNewList(DISPLAY_LISTS_2D[1], GL_COMPILE);

		quickDrawRect(-7F, 3F, -4F, 3.3F);
		quickDrawRect(4F, 3F, 7F, 3.3F);
		quickDrawRect(-7.3F, 0.5F, -7F, 3.3F);
		quickDrawRect(7F, 0.5F, 7.3F, 3.3F);

		glEndList();

		glNewList(DISPLAY_LISTS_2D[2], GL_COMPILE);

		quickDrawRect(4F, -20F, 7F, -19F);
		quickDrawRect(-7F, -20F, -4F, -19F);
		quickDrawRect(6F, -20F, 7F, -17.5F);
		quickDrawRect(-7F, -20F, -6F, -17.5F);

		glEndList();

		glNewList(DISPLAY_LISTS_2D[3], GL_COMPILE);

		quickDrawRect(7F, -20F, 7.3F, -17.5F);
		quickDrawRect(-7.3F, -20F, -7F, -17.5F);
		quickDrawRect(4F, -20.3F, 7.3F, -20F);
		quickDrawRect(-7.3F, -20.3F, -4F, -20F);

		glEndList();
	}

	public static void drawLineWithGL(BlockPos blockA, BlockPos blockB, boolean whiteLine, float red, float green,
			float blue) {

		double width = 3d;

		if (whiteLine) {
			GL11.glColor4f(255, 255, 255, 0F);
			GL11.glLineWidth(3);
		} else {
			GL11.glLineWidth((float) width);
			GL11.glColor4f(red, green, blue, 0F);
		}

		GL11.glBegin(GL11.GL_LINE_STRIP);

		GL11.glVertex3d(blockA.func_177958_n(), blockA.func_177956_o(), blockA.func_177952_p());
		GL11.glVertex3d(blockB.func_177958_n(), blockB.func_177956_o(), blockB.func_177952_p());

		GL11.glEnd();
	}

	public static void renderTextInWorld(Minecraft mc, String text, double x, double y, double z, float partialTicks,
			float scale) {
		Vec3 playerPos = mc.field_71439_g.func_174824_e(partialTicks);
		double distance = playerPos.func_72438_d(new Vec3(x, y, z));
		float maxDistance = 200.0f;
		float minDistance = 8.0f;

		// Calculate opacity
		float opacity = 1.0f;

		// If the distance is greater than maxDistance, do not render the text
		if (distance >= maxDistance - 1) {
			opacity = 0.001f;
			return;
		}

		if (distance > minDistance) {
			opacity = 1.001f - (float) (distance - minDistance) / (maxDistance - minDistance);
		}

		// Ensure opacity is within the correct bounds
		opacity = Math.max(0.0f, Math.min(1.0f, opacity));

		GlStateManager.func_179094_E();
		GlStateManager.func_179137_b(x - playerPos.field_72450_a, y - playerPos.field_72448_b, z - playerPos.field_72449_c);
		GlStateManager.func_179114_b(-mc.func_175598_ae().field_78735_i, 0.0F, 1.0F, 0.0F);
		GlStateManager.func_179114_b(mc.func_175598_ae().field_78732_j, 1.0F, 0.0F, 0.0F);
		GlStateManager.func_179152_a(-scale, -scale, scale);

		GlStateManager.func_179140_f();
		if (true) {
			GL11.glDisable(GL11.GL_DEPTH_TEST); // Draw the text on top of the geometry
		}
		GlStateManager.func_179132_a(false);
		GlStateManager.func_179147_l();
		GlStateManager.func_179120_a(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

		// Calculate the color with alpha transparency
		int alpha = (int) (opacity * 255.0f);
		int color = (alpha << 24) | 0xFFFFFF; // Add the opacity to the color code

		// Render the text
		int glyphWidth = mc.field_71466_p.func_78256_a(text);
		int glyphHeight = mc.field_71466_p.field_78288_b;

		// Define margin
		int margin = 4;

		// Draw the background rectangle with margin + text

		mc.field_71466_p.func_175065_a(text, -glyphWidth / 2, -glyphHeight / 2, color, false);

		GlStateManager.func_179126_j();
		GlStateManager.func_179132_a(true);
		GlStateManager.func_179145_e();
		GlStateManager.func_179084_k();
		GlStateManager.func_179121_F();
	}

	public static void drawPixelBox(final Vec3 vec, final Color color, final double size, float partialTicks) {
		final RenderManager renderManager = mc.func_175598_ae();

		final double x = vec.field_72450_a - renderManager.field_78730_l;
		final double y = vec.field_72448_b - renderManager.field_78731_m;
		final double z = vec.field_72449_c - renderManager.field_78728_n;

		AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + size, y + size, z + size);

		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		enableGlCap(GL_BLEND);
		disableGlCap(GL_TEXTURE_2D, GL_DEPTH_TEST);
		glDepthMask(false);

		glColor(color.getRed(), color.getGreen(), color.getBlue(), 35);
		// drawFilledBox(axisAlignedBB);

		glLineWidth(3F);
		enableGlCap(GL_LINE_SMOOTH);
		glColor(color);

		drawSelectionBoundingBox(axisAlignedBB);

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		glDepthMask(true);
		resetCaps();
	}

	public static void disableGlCap(final int... caps) {
		for (final int cap : caps)
			setGlCap(cap, false);
	}

	public static void glColor(final Color color) {
		glColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public static void glColor(final int red, final int green, final int blue, final int alpha) {
		GL11.glColor4f(red / 255F, green / 255F, blue / 255F, alpha / 255F);
	}

	public static void resetCaps() {
		glCapMap.forEach(DrawUtils::setGlState);
	}

	public static void enableGlCap(final int cap) {
		setGlCap(cap, true);
	}

	public static void disableGlCap(final int cap) {
		setGlCap(cap, true);
	}

	public static void setGlCap(final int cap, final boolean state) {
		glCapMap.put(cap, glGetBoolean(cap));
		setGlState(cap, state);
	}

	public static void setGlState(final int cap, final boolean state) {
		if (state)
			glEnable(cap);
		else
			glDisable(cap);
	}

	public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
		Tessellator tessellator = Tessellator.func_178181_a();
		WorldRenderer worldrenderer = tessellator.func_178180_c();

		worldrenderer.func_181668_a(GL_LINE_STRIP, DefaultVertexFormats.field_181705_e);

		// Lower Rectangle
		worldrenderer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();

		// Upper Rectangle
		worldrenderer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();

		// Upper Rectangle
		worldrenderer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();

		worldrenderer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();

		worldrenderer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();

		tessellator.func_78381_a();
	}

	public static void quickDrawRect(final float x, final float y, final float x2, final float y2) {
		glBegin(GL_QUADS);

		glVertex2d(x2, y);
		glVertex2d(x, y);
		glVertex2d(x, y2);
		glVertex2d(x2, y2);

		glEnd();
	}

	public static void highlightBlock(BlockPos pos, Color color, float partialTicks) {
		Entity viewer = Minecraft.func_71410_x().func_175606_aa();
		double x = pos.func_177958_n() - (viewer.field_70142_S + (viewer.field_70165_t - viewer.field_70142_S) * partialTicks);
		double y = pos.func_177956_o() - (viewer.field_70137_T + (viewer.field_70163_u - viewer.field_70137_T) * partialTicks);
		double z = pos.func_177952_p() - (viewer.field_70136_U + (viewer.field_70161_v - viewer.field_70136_U) * partialTicks);

		GlStateManager.func_179097_i();
		GlStateManager.func_179140_f();
		drawFilledBoundingBox(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1), color, 1f);
		GlStateManager.func_179145_e();
		GlStateManager.func_179126_j();
	}

	public static void highlightBlock(BlockPos pos, Color color, float opacity, float partialTicks) {
		Entity viewer = Minecraft.func_71410_x().func_175606_aa();
		double x = pos.func_177958_n() - (viewer.field_70142_S + (viewer.field_70165_t - viewer.field_70142_S) * partialTicks);
		double y = pos.func_177956_o() - (viewer.field_70137_T + (viewer.field_70163_u - viewer.field_70137_T) * partialTicks);
		double z = pos.func_177952_p() - (viewer.field_70136_U + (viewer.field_70161_v - viewer.field_70136_U) * partialTicks);
		GlStateManager.func_179097_i();
		GlStateManager.func_179140_f();
		drawFilledBoundingBox(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1), color, opacity);
		GlStateManager.func_179126_j();
		GlStateManager.func_179145_e();
	}

	public static void drawBlockBox(final BlockPos blockPos, final Color color, final int width, float partialTicks) {
		if (width == 0)
			return;
		final RenderManager renderManager = mc.func_175598_ae();

		final double x = blockPos.func_177958_n() - renderManager.field_78730_l;
		final double y = blockPos.func_177956_o() - renderManager.field_78731_m;
		final double z = blockPos.func_177952_p() - renderManager.field_78728_n;

		AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
		final Block block = mc.field_71441_e.func_180495_p(blockPos).func_177230_c();

		if (block != null) {
			final EntityPlayerSP player = mc.field_71439_g;

			final double posX = player.field_70142_S + (player.field_70165_t - player.field_70142_S) * partialTicks;
			final double posY = player.field_70137_T + (player.field_70163_u - player.field_70137_T) * partialTicks;
			final double posZ = player.field_70136_U + (player.field_70161_v - player.field_70136_U) * partialTicks;

			block.func_180654_a(mc.field_71441_e, blockPos);

			axisAlignedBB = block.func_180646_a(mc.field_71441_e, blockPos)
					.func_72314_b(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D)
					.func_72317_d(-posX, -posY, -posZ);
		}

		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		enableGlCap(GL_BLEND);
		disableGlCap(GL_TEXTURE_2D, GL_DEPTH_TEST);
		glDepthMask(false);

		glColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() != 255 ? color.getAlpha() : 26);
		// drawFilledBox(axisAlignedBB);

		glLineWidth((float) width);
		enableGlCap(GL_LINE_SMOOTH);
		glColor(color);

		drawSelectionBoundingBox(axisAlignedBB);

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		glDepthMask(true);
		resetCaps();
	}

	public void drawOutlinedRect(int x, int y, int width, int height, Color fill, Color outline) {
		GlStateManager.func_179094_E();
		GlStateManager.func_179094_E();
		GlStateManager.func_179147_l();
		GlStateManager.func_179141_d();
		GlStateManager.func_179090_x();

		int xEnd = x + width;
		int yEnd = y + height;

		Gui.func_73734_a(x, y, xEnd, yEnd, fill.getRGB());

		GlStateManager.func_179121_F();
		GlStateManager.func_179147_l();
		GlStateManager.func_179141_d();
		GlStateManager.func_179090_x();

		GL11.glLineWidth(2f);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

		float[] outlineColor = translateToFloat(outline);

		GlStateManager.func_179131_c(outlineColor[0], outlineColor[1], outlineColor[2], 1f);

		GL11.glBegin(GL11.GL_LINE_LOOP);

		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, yEnd);
		GL11.glVertex2d(xEnd, yEnd);
		GL11.glVertex2d(xEnd, y);

		GL11.glEnd();

		GL11.glDisable(GL11.GL_LINE_SMOOTH);

		GlStateManager.func_179098_w();
		GlStateManager.func_179118_c();
		GlStateManager.func_179084_k();
		GlStateManager.func_179117_G();
		GlStateManager.func_179121_F();
	}

	public static void renderTracer(double posX, double posY, double posZ, double height, Color color,
			float partialTicks) {
		Entity render = mc.func_175606_aa();
		Tessellator tessellator = Tessellator.func_178181_a();
		WorldRenderer worldRenderer = tessellator.func_178180_c();

		final double realX = render.field_70142_S + (render.field_70165_t - render.field_70142_S) * partialTicks;
		final double realY = render.field_70137_T + (render.field_70163_u - render.field_70137_T) * partialTicks;
		final double realZ = render.field_70136_U + (render.field_70161_v - render.field_70136_U) * partialTicks;

		GlStateManager.func_179094_E();
		GlStateManager.func_179137_b(-realX, -realY, -realZ);
		GlStateManager.func_179090_x();
		GlStateManager.func_179140_f();
		GL11.glDisable(3553);
		GlStateManager.func_179147_l();
		GlStateManager.func_179118_c();
		GL11.glLineWidth(2f);
		GlStateManager.func_179097_i();
		GlStateManager.func_179132_a(false);
		GlStateManager.func_179120_a(770, 771, 1, 0);
		GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
		worldRenderer.func_181668_a(3, DefaultVertexFormats.field_181706_f);

		worldRenderer.func_181662_b(realX, realY + render.func_70047_e(), realZ)
				.func_181669_b(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).func_181675_d();
		worldRenderer.func_181662_b(posX, posY, posZ).func_181669_b(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
				.func_181675_d();

		Tessellator.func_178181_a().func_78381_a();

		GlStateManager.func_179137_b(realX, realY, realZ);
		GlStateManager.func_179084_k();
		GlStateManager.func_179141_d();
		GlStateManager.func_179098_w();
		GlStateManager.func_179126_j();
		GlStateManager.func_179132_a(true);
		GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.func_179121_F();
	}

	public static void renderEspBox(BlockPos blockPos, float partialTicks, int color) {
		renderEspBox(blockPos, partialTicks, color, 0.5f);
	}

	public static void renderEspBox(BlockPos blockPos, float partialTicks, int color, float opacity) {
		if (blockPos != null) {
			IBlockState blockState = mc.field_71441_e.func_180495_p(blockPos);

			if (blockState != null) {
				Block block = blockState.func_177230_c();
				block.func_180654_a(mc.field_71441_e, blockPos);
				double d0 = mc.field_71439_g.field_70142_S
						+ (mc.field_71439_g.field_70165_t - mc.field_71439_g.field_70142_S) * (double) partialTicks;
				double d1 = mc.field_71439_g.field_70137_T
						+ (mc.field_71439_g.field_70163_u - mc.field_71439_g.field_70137_T) * (double) partialTicks;
				double d2 = mc.field_71439_g.field_70136_U
						+ (mc.field_71439_g.field_70161_v - mc.field_71439_g.field_70136_U) * (double) partialTicks;
				drawFilledBoundingBox(block.func_180646_a(mc.field_71441_e, blockPos).func_72314_b(0.002D, 0.002D, 0.002D)
						.func_72317_d(-d0, -d1, -d2), color, opacity);
			}
		}
	}

	public static void drawFilledBoundingBox(AxisAlignedBB aabb, int color, float opacity) {
		GlStateManager.func_179147_l();
		GlStateManager.func_179097_i();
		GlStateManager.func_179140_f();
		GlStateManager.func_179120_a(770, 771, 1, 0);
		GlStateManager.func_179090_x();
		Tessellator tessellator = Tessellator.func_178181_a();
		WorldRenderer worldrenderer = tessellator.func_178180_c();

		float a = (color >> 24 & 0xFF) / 255.0F;
		float r = (color >> 16 & 0xFF) / 255.0F;
		float g = (color >> 8 & 0xFF) / 255.0F;
		float b = (color & 0xFF) / 255.0F;

		GlStateManager.func_179131_c(r, g, b, a * opacity);
		worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
		tessellator.func_78381_a();
		worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
		tessellator.func_78381_a();
		GlStateManager.func_179131_c(r, g, b, a * opacity);
		worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
		tessellator.func_78381_a();
		worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
		tessellator.func_78381_a();
		GlStateManager.func_179131_c(r, g, b, a * opacity);
		worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
		tessellator.func_78381_a();
		worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
		tessellator.func_78381_a();
		GlStateManager.func_179131_c(r, g, b, a);
		RenderGlobal.func_181561_a(aabb);
		GlStateManager.func_179098_w();
		GlStateManager.func_179126_j();
		GlStateManager.func_179084_k();
		GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public static void drawFilledBoundingBox(AxisAlignedBB aabb, Color c, float alphaMultiplier) {
		GlStateManager.func_179147_l();
		GlStateManager.func_179140_f();
		GlStateManager.func_179120_a(770, 771, 1, 0);
		GlStateManager.func_179090_x();
		Tessellator tessellator = Tessellator.func_178181_a();
		WorldRenderer worldrenderer = tessellator.func_178180_c();
		GlStateManager.func_179131_c(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f,
				c.getAlpha() / 255f * alphaMultiplier);
		worldrenderer.func_181668_a(GL11.GL_QUADS, DefaultVertexFormats.field_181705_e);
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
		tessellator.func_78381_a();
		worldrenderer.func_181668_a(GL11.GL_QUADS, DefaultVertexFormats.field_181705_e);
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
		tessellator.func_78381_a();
		GlStateManager.func_179131_c(c.getRed() / 255f * 0.8f, c.getGreen() / 255f * 0.8f, c.getBlue() / 255f * 0.8f,
				c.getAlpha() / 255f * alphaMultiplier);
		worldrenderer.func_181668_a(GL11.GL_QUADS, DefaultVertexFormats.field_181705_e);
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
		tessellator.func_78381_a();
		worldrenderer.func_181668_a(GL11.GL_QUADS, DefaultVertexFormats.field_181705_e);
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
		tessellator.func_78381_a();
		GlStateManager.func_179131_c(c.getRed() / 255f * 0.9f, c.getGreen() / 255f * 0.9f, c.getBlue() / 255f * 0.9f,
				c.getAlpha() / 255f * alphaMultiplier);
		worldrenderer.func_181668_a(GL11.GL_QUADS, DefaultVertexFormats.field_181705_e);
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
		tessellator.func_78381_a();
		worldrenderer.func_181668_a(GL11.GL_QUADS, DefaultVertexFormats.field_181705_e);
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
		worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
		tessellator.func_78381_a();
		GlStateManager.func_179098_w();
		GlStateManager.func_179084_k();
	}

	public static void enableGL2D() {
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glDepthMask(true);
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glHint(3155, 4354);
	}

	public static void disableGL2D() {
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glHint(3155, 4352);
	}

	public static void drawTargetESP(Entity target, Color color, float partialTicks) {
		double z;
		double x;
		int i;
		GL11.glPushMatrix();
		float location = (float) ((Math.sin((double) System.currentTimeMillis() * 0.005) + 1.0) * 0.5);
		GlStateManager.func_179137_b(
				target.field_70142_S + (target.field_70165_t - target.field_70142_S) * (double) partialTicks
						- mc.func_175598_ae().field_78730_l,
				target.field_70137_T + (target.field_70163_u - target.field_70137_T) * (double) partialTicks
						- mc.func_175598_ae().field_78731_m + (double) (target.field_70131_O * location),
				target.field_70136_U + (target.field_70161_v - target.field_70136_U) * (double) partialTicks
						- mc.func_175598_ae().field_78728_n);
		enableGL2D();
		GL11.glShadeModel(7425);
		GL11.glDisable(2884);
		GL11.glLineWidth(3.0f);
		GL11.glBegin(3);
		double cos = Math.cos((double) System.currentTimeMillis() * 0.005);
		for (i = 0; i <= 120; ++i) {
			GL11.glColor4f((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f,
					(float) color.getBlue() / 255.0f, 1.0f);
			x = Math.cos((double) i * Math.PI / 60.0) * (double) target.field_70130_N;
			z = Math.sin((double) i * Math.PI / 60.0) * (double) target.field_70130_N;
			GL11.glVertex3d(x, (double) 0.15f * cos, z);
		}
		GL11.glEnd();
		GL11.glBegin(5);
		for (i = 0; i <= 120; ++i) {
			GL11.glColor4f((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f,
					(float) color.getBlue() / 255.0f, 0.5f);
			x = Math.cos((double) i * Math.PI / 60.0) * (double) target.field_70130_N;
			z = Math.sin((double) i * Math.PI / 60.0) * (double) target.field_70130_N;
			GL11.glVertex3d(x, (double) 0.15f * cos, z);
			GL11.glColor4f((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f,
					(float) color.getBlue() / 255.0f, 0.2f);
			GL11.glVertex3d(x, (double) -0.15f * cos, z);
		}
		GL11.glEnd();
		GL11.glShadeModel(7424);
		GL11.glEnable(2884);
		disableGL2D();
		GL11.glPopMatrix();
	}

	public static float[] translateToFloat(Color color) {
		return new float[] { color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f,
				color.getAlpha() / 255f };
	}

	public static void entityESPBox(Entity entity, float partialTicks, Color color) {
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(3042);
		GL11.glLineWidth(1.5f);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		setColor(color);
		RenderGlobal.func_181561_a(new AxisAlignedBB(
				entity.func_174813_aQ().field_72340_a - entity.field_70165_t
						+ (entity.field_70169_q + (entity.field_70165_t - entity.field_70169_q) * (double) partialTicks
								- Minecraft.func_71410_x().func_175598_ae().field_78730_l),
				entity.func_174813_aQ().field_72338_b - entity.field_70163_u
						+ (entity.field_70167_r + (entity.field_70163_u - entity.field_70167_r) * (double) partialTicks
								- Minecraft.func_71410_x().func_175598_ae().field_78731_m),
				entity.func_174813_aQ().field_72339_c - entity.field_70161_v
						+ (entity.field_70166_s + (entity.field_70161_v - entity.field_70166_s) * (double) partialTicks
								- Minecraft.func_71410_x().func_175598_ae().field_78728_n),
				entity.func_174813_aQ().field_72336_d - entity.field_70165_t
						+ (entity.field_70169_q + (entity.field_70165_t - entity.field_70169_q) * (double) partialTicks
								- Minecraft.func_71410_x().func_175598_ae().field_78730_l),
				entity.func_174813_aQ().field_72337_e - entity.field_70163_u
						+ (entity.field_70167_r + (entity.field_70163_u - entity.field_70167_r) * (double) partialTicks
								- Minecraft.func_71410_x().func_175598_ae().field_78731_m),
				entity.func_174813_aQ().field_72334_f - entity.field_70161_v
						+ (entity.field_70166_s + (entity.field_70161_v - entity.field_70166_s) * (double) partialTicks
								- Minecraft.func_71410_x().func_175598_ae().field_78728_n)));
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
		GL11.glDisable(3042);
	}

	public static void setColor(Color c) {
		GL11.glColor4f((float) c.getRed() / 255.0f, (float) c.getGreen() / 255.0f, (float) c.getBlue() / 255.0f,
				(float) c.getAlpha() / 255.0f);
	}

	public static void enableChams() {
		GL11.glEnable(32823);
		GlStateManager.func_179088_q();
		GlStateManager.func_179136_a(1.0f, -1000000.0f);
	}

	public static void disableChams() {
		GL11.glDisable(32823);
		GlStateManager.func_179136_a(1.0f, 1000000.0f);
		GlStateManager.func_179113_r();
	}

	public static void miniBlockBox(BlockPos block, Color color) {
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(3042);
		GL11.glLineWidth(2.0f);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		setColor(color);
		Minecraft.func_71410_x().func_175598_ae();
		RenderGlobal.func_181561_a(
				new AxisAlignedBB(block.func_177958_n() - 0.05 - Minecraft.func_71410_x().func_175598_ae().field_78730_l,
						block.func_177956_o() - 0.05 - Minecraft.func_71410_x().func_175598_ae().field_78731_m,
						block.func_177952_p() - 0.05 - Minecraft.func_71410_x().func_175598_ae().field_78728_n,
						block.func_177958_n() + 0.05 - Minecraft.func_71410_x().func_175598_ae().field_78730_l,
						block.func_177956_o() + 0.05 - Minecraft.func_71410_x().func_175598_ae().field_78731_m,
						block.func_177952_p() + 0.05 - Minecraft.func_71410_x().func_175598_ae().field_78728_n));
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
		GL11.glDisable(3042);
	}

	public static void drawNametag(String str) {
		FontRenderer fontrenderer = mc.field_71466_p;
		float f1 = 0.0266666688f;
		GlStateManager.func_179094_E();
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GlStateManager.func_179114_b(-Minecraft.func_71410_x().func_175598_ae().field_78735_i, 0.0F, 1.0F, 0.0F);
		GlStateManager.func_179114_b(Minecraft.func_71410_x().func_175598_ae().field_78732_j, 1.0F, 0.0F, 0.0F);
		GlStateManager.func_179152_a(-f1, -f1, f1);
		GlStateManager.func_179140_f();
		GlStateManager.func_179132_a(false);
		GlStateManager.func_179097_i();
		GlStateManager.func_179147_l();
		GlStateManager.func_179120_a(770, 771, 1, 0);
		Tessellator tessellator = Tessellator.func_178181_a();
		WorldRenderer bufferBuilder = tessellator.func_178180_c();
		int i = 0;

		int j = fontrenderer.func_78256_a(str) / 2;
		GlStateManager.func_179090_x();
		bufferBuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
		bufferBuilder.func_181662_b(-j - 1, -1 + i, 0.0D).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F).func_181675_d();
		bufferBuilder.func_181662_b(-j - 1, 8 + i, 0.0D).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F).func_181675_d();
		bufferBuilder.func_181662_b(j + 1, 8 + i, 0.0D).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F).func_181675_d();
		bufferBuilder.func_181662_b(j + 1, -1 + i, 0.0D).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F).func_181675_d();
		tessellator.func_78381_a();
		GlStateManager.func_179098_w();
		fontrenderer.func_78276_b(str, -fontrenderer.func_78256_a(str) / 2, i, 553648127);
		GlStateManager.func_179132_a(true);

		fontrenderer.func_78276_b(str, -fontrenderer.func_78256_a(str) / 2, i, -1);

		GlStateManager.func_179126_j();
		GlStateManager.func_179147_l();
		GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.func_179121_F();
	}

	public static void renderTracer(Entity entity, Color color, float partialTicks) {
		renderTracer(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, entity.field_70131_O, color, partialTicks);
	}

}
