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
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			FontRenderer fontrenderer = mc.fontRendererObj;
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
					&& mouseY < this.yPosition + this.height;
			int i = this.getHoverState(this.hovered);
			this.mouseDragged(mc, mouseX, mouseY);
			int j = 0xFFFFFF;
			if (!hovered)
				j = 0xFFFFFF;
			else
				j = 0xaba595;
			this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2,
					this.yPosition + (this.height - 8) / 2, j);
		}
	}

	@Override
	public void playPressSound(SoundHandler soundHandlerIn) {
		// no sound when pressed
	}

}
