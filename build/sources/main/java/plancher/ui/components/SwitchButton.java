package plancher.ui.components;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import plancher.utils.ChatUtils;

public class SwitchButton extends GuiButton {

	private static boolean hovered;
	int count = 0;
	int color = 0x04f5f900;
	private static boolean state;

	public SwitchButton(int buttonId, int x, int y, String buttonText, boolean b) {
		super(buttonId, x, y, buttonText);
		this.width = 60;
		this.height = 20;
		this.state = b;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		FontRenderer fontRenderer = mc.fontRendererObj;
		TextureManager textureManager = mc.getTextureManager();

		hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
				&& mouseY < this.yPosition + this.height;

		if (this.visible) {

			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);

			int textX = this.xPosition + this.width / 2 - 20;
			int textY = this.yPosition + (this.height - fontRenderer.FONT_HEIGHT) / 2;
			final int hoveredColor = 0xaaaaaaff; // ????? wth
			drawGradientRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height,
					0xFF2b2a2b, 0xFF777777);

			if (hovered) {
				int borderThickness = 1;
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.blendFunc(770, 771);
				drawRect(this.xPosition - borderThickness, this.yPosition - borderThickness,
						this.xPosition + this.width + borderThickness,
						this.yPosition - borderThickness + borderThickness, hoveredColor); // Haut
				drawRect(this.xPosition - borderThickness, this.yPosition - borderThickness + borderThickness,
						this.xPosition - borderThickness + borderThickness,
						this.yPosition + this.height + borderThickness, hoveredColor); // Gauche
				drawRect(this.xPosition - borderThickness, this.yPosition + this.height,
						this.xPosition + this.width + borderThickness, this.yPosition + this.height + borderThickness,
						hoveredColor); // Bas
				drawRect(this.xPosition + this.width, this.yPosition - borderThickness + borderThickness,
						this.xPosition + this.width + borderThickness, this.yPosition + this.height + borderThickness,
						hoveredColor); // Droite

			}
			if (this.state) {
				if (hovered)
					this.drawCenteredString(fontRenderer, this.displayString, textX + 20, textY, 0x70e32d);
				else
					this.drawCenteredString(fontRenderer, this.displayString, textX + 20, textY + 1, 0x70e32d);
			} else {

				if (hovered)
					this.drawCenteredString(fontRenderer, this.displayString, textX + 20, textY, 0xfa3d2f);
				else
					this.drawCenteredString(fontRenderer, this.displayString, textX + 20, textY + 1, 0xfa3d2f);
			}
		}
	}

	protected static int getRainbow(final int speed, final int offset) {
		float hue = (System.currentTimeMillis() + offset) % speed;
		float saturation = 0.9f;
		float brightness = 1f;

		int rgbColor = Color.HSBtoRGB(hue / speed, saturation, brightness);

		int redComponent = (rgbColor >> 16) & 0xFF;
		int greenComponent = (rgbColor >> 8) & 0xFF;
		int blueComponent = rgbColor & 0xFF;

		int hexColorValue = (redComponent << 16) | (greenComponent << 8) | blueComponent;

		return hexColorValue;
	}

	@Override
	public void playPressSound(SoundHandler soundHandlerIn) {
//		String s = "liquid.lavapop";
//
//		soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation(s), 2F));
	}

	public static boolean isHovered() {
		return hovered;
	}

	public static void setHovered(boolean hovered) {
		SwitchButton.hovered = hovered;
	}

}
