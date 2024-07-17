package plancher.ui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class CategoryButton extends GuiButton {

	public CategoryButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
	}

	@Override
	public void func_146112_a(Minecraft mc, int mouseX, int mouseY) {
		if (this.field_146125_m) {
			FontRenderer fontrenderer = mc.field_71466_p;
			this.field_146123_n = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f
					&& mouseY < this.field_146129_i + this.field_146121_g;
			int i = this.func_146114_a(this.field_146123_n);
			this.func_146119_b(mc, mouseX, mouseY);
			int j = 0xFFFFFF;
			if (!field_146123_n)
				j = 0xFFFFFF;
			else
				j = 0xaba595;
			this.func_73732_a(fontrenderer, this.field_146126_j, this.field_146128_h + this.field_146120_f / 2,
					this.field_146129_i + (this.field_146121_g - 8) / 2, j);
		}
	}

	@Override
	public void func_146113_a(SoundHandler soundHandlerIn) {
		// no sound when pressed
	}

}
