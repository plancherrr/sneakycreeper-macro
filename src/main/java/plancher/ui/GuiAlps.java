package plancher.ui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.ai.EntityAILookAtVillager;
import plancher.features.AntiEscrowRefund;
import plancher.features.PestEsp;
import plancher.ui.components.CategoryButton;
import plancher.ui.components.SwitchButton;
import plancher.utils.ChatUtils;

public class GuiAlps extends GuiScreen {

	private static int colora = 111111;
	private static boolean guiLogin = false;
	int w = this.width;
	int h = this.height;
	private ScaledResolution sr;
	int screenWidth;
	int screenHeight;
	int bgSize = 50;
	private static int category = 0;
	{
		{
			{

				/*
				 * 1 garden | 2 misc | 3 gui
				 */

			}
		}
	}

	CategoryButton garden;
	CategoryButton misc;
	CategoryButton guiFeatures;

	SwitchButton pestEsp;
	SwitchButton antiEscrowRefund;

	@Override
	public void initGui() {
		sr = new ScaledResolution(mc);
		int screenW = sr.getScaledWidth();
		int rectX = (screenW - 500) / 2;
		{
			garden = new CategoryButton(1, rectX, 90, 86, 12, "Garden");
			guiFeatures = new CategoryButton(1, rectX, 90 + 12, 86, 12, "Gui");
			misc = new CategoryButton(1, rectX, 90 + 24, 86, 12, "Misc");
		}
		{
			pestEsp = new SwitchButton(1, rectX + 100, 100, "Pest ESP", PestEsp.pest);
			antiEscrowRefund = new SwitchButton(2, rectX + 100 + 50, 100, "Anti Escrow", AntiEscrowRefund.escrowRefund);

		}
		reInit();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		sr = new ScaledResolution(mc);

		screenWidth = sr.getScaledWidth();
		screenHeight = sr.getScaledHeight();

		int rectWidth = 500;
		int rectHeight = 300;

		int rectX = (screenWidth - rectWidth) / 2;
		int rectY = (screenHeight - rectHeight) / 2;
		int dividingLineX = rectX + rectWidth / 2 - 165;

		drawRect(rectX, rectY, rectX + rectWidth, rectY + rectHeight, 0xFF252626);
		drawRect(dividingLineX, rectY, dividingLineX + 1, rectY + rectHeight, 0xFF1f1f1e);

		mc.fontRendererObj.drawString("ALPS", rectX + (dividingLineX / 2) - 47, sr.getScaledHeight() / 8 + 12,
				colora / 5);

//		mc.fontRendererObj.drawString(getNameFromIndex(category), rectX + 250, sr.getScaledHeight() / 8 + 12,
//				colora / 5);
		drawTitle(getNameFromIndex(category), 1.7F);

		if (mc.gameSettings.guiScale != 3)
			mc.fontRendererObj.drawString("Use large gui scale please, don't be a dumb retard",
					sr.getScaledWidth() / 2 - 25, sr.getScaledHeight() / 2, colora / 5);

		colora++;
		super.drawScreen(mouseX, mouseY, partialTicks);

	}

	@Override
	public void actionPerformed(GuiButton b) {
		if (b == garden) {
			category = 1;
		} else if (b == misc) {
			category = 2;
		} else if (b == guiFeatures) {
			category = 3;
		} else if (b == pestEsp) {
			PestEsp.pest = !PestEsp.pest;
		} else if (antiEscrowRefund == b) {
			AntiEscrowRefund.escrowRefund = !AntiEscrowRefund.escrowRefund;
		}
		this.initGui();
		reInit();
	}

	private static String getNameFromIndex(int cate) {
		switch (cate) {
		case 0:
			return "Alps is welcoming you";
		case 1:
			return "Garden";
		case 2:
			return "Miscellaneous";
		case 3:
			return "Graphical interface";

		default:
			return "Other (weird, contact the dev pls)";
		}

	}

	private static void drawSizedString(String str, int x, int y, final float SIZE) {
		GL11.glPushMatrix();
		GL11.glScalef(SIZE, SIZE, SIZE);
		Minecraft.getMinecraft().fontRendererObj.drawString(str, x, y, 0xFFFFFF);
		GL11.glPopMatrix();
	}

	private void drawTitle(String text, float size) {
		int textWidth = mc.fontRendererObj.getStringWidth(text);
		int rectX = (width - 500) / 2;
		int rectY = (height - 300) / 2;

		int dividingLineX = rectX + 100;
		int textX = dividingLineX + (500 - dividingLineX) / 2 - textWidth / 2 - 125;
		int textY = rectY + 300 / 2 + 50;
		GL11.glPushMatrix();
		GL11.glScalef(size, size, size);
		mc.fontRendererObj.drawString(text, textX, 35, 0Xffffff);
		GL11.glPopMatrix();
// 1,500,000 
	}

	public void reInit() {
		h = this.height;
		w = this.width;
		buttonList.clear();

		buttonList.add(garden);
		buttonList.add(misc);
		buttonList.add(guiFeatures);
		if (category == 1) {
			buttonList.add(pestEsp);
		}

	}

	public static boolean isGui() {
		return guiLogin;
	}

	public static void setGui(boolean a) {
		guiLogin = a;
	}
}
