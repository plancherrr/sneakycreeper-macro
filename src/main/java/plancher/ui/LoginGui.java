package plancher.ui;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Session;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import plancher.utils.ChatUtils;

public class LoginGui extends GuiScreen {

	private static int step = 1;
	private static boolean guiAlps = false;
	int w = this.width;
	int h = this.height;
	private ScaledResolution sr;

	private static GuiTextField name = new GuiTextField(1, null, 1, 1, 1, 1);
	private static GuiTextField token = new GuiTextField(1, null, 1, 1, 1, 1);
	private static GuiTextField uuid = new GuiTextField(1, null, 1, 1, 1, 1);

	private static GuiButton process = new GuiButton(1, 1, 1, "");
	private static GuiButton quickLog = new GuiButton(1, 1, 1, "");

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		sr = new ScaledResolution(mc);

		// TEXT FIELDS
		{
			name = new GuiTextField(1, mc.fontRendererObj, sr.getScaledWidth() / 2 - 100,
					sr.getScaledHeight() / 2 - 100, 200, 20);
			token = new GuiTextField(1, mc.fontRendererObj, sr.getScaledWidth() / 2 - 100,
					sr.getScaledHeight() / 2 - 50, 200, 20);
			uuid = new GuiTextField(1, mc.fontRendererObj, sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() / 2, 200,
					20);
			token.setMaxStringLength(2000);
		}
		// BUTTONS
		{
			process = new GuiButton(1, sr.getScaledWidth() / 2 - 50, sr.getScaledHeight() / 2 + 100, 100, 20, "Log in");

			quickLog = new GuiButton(1, sr.getScaledWidth() / 2 - 50, sr.getScaledHeight() / 2 + 140, 100, 20,
					"quick log");
		}
		clearFields();
		step = 1;
		reInit();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		mc.fontRendererObj.drawString("Username",
				sr.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth("Username") / 2,
				sr.getScaledHeight() / 2 - 120, Color.cyan.getRGB());

		mc.fontRendererObj.drawString("Token", sr.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth("Token") / 2,
				sr.getScaledHeight() / 2 - 70, Color.cyan.getRGB());

		mc.fontRendererObj.drawString("UUID", sr.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth("UUID") / 2,
				sr.getScaledHeight() / 2 - 20, Color.cyan.getRGB());
		super.drawScreen(mouseX, mouseY, partialTicks);

		name.drawTextBox();
		token.drawTextBox();
		uuid.drawTextBox();

		switch (step) {
		case 1:
			name.setFocused(true);
			token.setFocused(false);
			uuid.setFocused(false);

			break;
		case 2:
			name.setFocused(false);
			token.setFocused(true);
			uuid.setFocused(false);

			break;
		case 3:
			name.setFocused(false);
			token.setFocused(false);
			uuid.setFocused(true);

			break;
		case 4:
			step = 0;
			name.setFocused(true);
			token.setFocused(false);
			uuid.setFocused(false);
			if (checkFields()) {
				setSession(name.getText(), uuid.getText(), token.getText(), "Mojang");
				ChatUtils.clientMessage("You are now logged as " + mc.getSession().getUsername());
				ChatUtils.errorMessage("You must disconnect to finalize the login phase");
				mc.thePlayer.closeScreen();
			} else {
				ChatUtils.errorMessage("invalid parameters");
				mc.thePlayer.closeScreen();
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void actionPerformed(GuiButton b) {
		if (b == process) {
			step = 0;
			name.setFocused(true);
			token.setFocused(false);
			uuid.setFocused(false);
			if (checkFields()) {
				setSession(name.getText(), uuid.getText(), token.getText(), "Mojang");
				ChatUtils.clientMessage("You are now logged as " + mc.getSession().getUsername());
				ChatUtils.errorMessage("You must disconnect to finalize the login phase");
				mc.thePlayer.closeScreen();
			} else {
				ChatUtils.errorMessage("invalid parameters");
				mc.thePlayer.closeScreen();
			}
		} else if (b == quickLog) {

		}
	}

	public void reInit() {
		h = this.height;
		w = this.width;
		buttonList.clear();
		buttonList.add(process);
		buttonList.add(quickLog);

	}

	public static boolean isGui() {
		return guiAlps;
	}

	public static void setGui(boolean a) {
		guiAlps = a;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		name.textboxKeyTyped(typedChar, keyCode);
		token.textboxKeyTyped(typedChar, keyCode);
		uuid.textboxKeyTyped(typedChar, keyCode);

		if (Keyboard.KEY_ESCAPE == keyCode) {
			mc.thePlayer.closeScreen();
			ChatUtils.debugMessage("aze");
		} else if (Keyboard.KEY_RETURN == keyCode || Keyboard.KEY_TAB == keyCode) {
			step += 1;
		}

		else
			super.keyTyped(typedChar, keyCode);
	}

	private static void clearFields() {
		name.setText("");
		token.setText("");
		uuid.setText("");

	}

	private static boolean checkFields() {
		if (name.getText().equals("") || token.getText().equals("") || uuid.getText().equals(""))
			return false;
		return true;
	}

	public static void setSession(String usernameIn, String playerIDIn, String tokenIn, String sessionTypeIn) {
		Field sessionField = null;
		try {
			sessionField = ReflectionHelper.findField(Minecraft.class, "field_71449_j");
		} catch (Exception e) {
			try {
				sessionField = ReflectionHelper.findField(Minecraft.class, "session");
			} catch (Exception ex) {
			}
		} finally {
			if (sessionField == null) {
				System.out.print("Error can't init the Session Field.");
				return;
			}
		}
		try {
			sessionField.setAccessible(true);
			String username = usernameIn;
			String uuid = playerIDIn;
			String token = tokenIn;
			String type = sessionTypeIn;
			Minecraft minecraft = Minecraft.getMinecraft();
			Session session = new Session(username, uuid, token, type);
			sessionField.set(Minecraft.getMinecraft(), session);
		} catch (Exception e) {
		}
	}
}
