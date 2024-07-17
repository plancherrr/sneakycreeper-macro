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
		this.field_146120_f = 60;
		this.field_146121_g = 20;
		this.state = b;
	}

	@Override
	public void func_146112_a(Minecraft mc, int mouseX, int mouseY) {
		FontRenderer fontRenderer = mc.field_71466_p;
		TextureManager textureManager = mc.func_110434_K();

		hovered = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f
				&& mouseY < this.field_146129_i + this.field_146121_g;

		if (this.field_146125_m) {

			GlStateManager.func_179147_l();
			GlStateManager.func_179120_a(770, 771, 1, 0);
			GlStateManager.func_179112_b(770, 771);

			int textX = this.field_146128_h + this.field_146120_f / 2 - 20;
			int textY = this.field_146129_i + (this.field_146121_g - fontRenderer.field_78288_b) / 2;
			final int hoveredColor = 0xaaaaaaff; // ????? wth
			func_73733_a(this.field_146128_h, this.field_146129_i, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g,
					0xFF2b2a2b, 0xFF777777);

			if (hovered) {
				int borderThickness = 1;
				GlStateManager.func_179147_l();
				GlStateManager.func_179120_a(770, 771, 1, 0);
				GlStateManager.func_179112_b(770, 771);
				func_73734_a(this.field_146128_h - borderThickness, this.field_146129_i - borderThickness,
						this.field_146128_h + this.field_146120_f + borderThickness,
						this.field_146129_i - borderThickness + borderThickness, hoveredColor); // Haut
				func_73734_a(this.field_146128_h - borderThickness, this.field_146129_i - borderThickness + borderThickness,
						this.field_146128_h - borderThickness + borderThickness,
						this.field_146129_i + this.field_146121_g + borderThickness, hoveredColor); // Gauche
				func_73734_a(this.field_146128_h - borderThickness, this.field_146129_i + this.field_146121_g,
						this.field_146128_h + this.field_146120_f + borderThickness, this.field_146129_i + this.field_146121_g + borderThickness,
						hoveredColor); // Bas
				func_73734_a(this.field_146128_h + this.field_146120_f, this.field_146129_i - borderThickness + borderThickness,
						this.field_146128_h + this.field_146120_f + borderThickness, this.field_146129_i + this.field_146121_g + borderThickness,
						hoveredColor); // Droite

			}
			if (this.state) {
				if (hovered)
					this.func_73732_a(fontRenderer, this.field_146126_j, textX + 20, textY, 0x70e32d);
				else
					this.func_73732_a(fontRenderer, this.field_146126_j, textX + 20, textY + 1, 0x70e32d);
			} else {

				if (hovered)
					this.func_73732_a(fontRenderer, this.field_146126_j, textX + 20, textY, 0xfa3d2f);
				else
					this.func_73732_a(fontRenderer, this.field_146126_j, textX + 20, textY + 1, 0xfa3d2f);
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
	public void func_146113_a(SoundHandler soundHandlerIn) {
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
