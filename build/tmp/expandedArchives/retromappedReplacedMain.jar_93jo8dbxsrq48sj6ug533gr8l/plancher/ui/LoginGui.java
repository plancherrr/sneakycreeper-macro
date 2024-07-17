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
	int w = this.field_146294_l;
	int h = this.field_146295_m;
	private ScaledResolution sr;

	private static GuiTextField name = new GuiTextField(1, null, 1, 1, 1, 1);
	private static GuiTextField token = new GuiTextField(1, null, 1, 1, 1, 1);
	private static GuiTextField uuid = new GuiTextField(1, null, 1, 1, 1, 1);

	private static GuiButton process = new GuiButton(1, 1, 1, "");
	private static GuiButton quickLog = new GuiButton(1, 1, 1, "");

	@Override
	public void func_73866_w_() {
		Keyboard.enableRepeatEvents(true);
		sr = new ScaledResolution(field_146297_k);

		// TEXT FIELDS
		{
			name = new GuiTextField(1, field_146297_k.field_71466_p, sr.func_78326_a() / 2 - 100,
					sr.func_78328_b() / 2 - 100, 200, 20);
			token = new GuiTextField(1, field_146297_k.field_71466_p, sr.func_78326_a() / 2 - 100,
					sr.func_78328_b() / 2 - 50, 200, 20);
			uuid = new GuiTextField(1, field_146297_k.field_71466_p, sr.func_78326_a() / 2 - 100, sr.func_78328_b() / 2, 200,
					20);
			token.func_146203_f(2000);
		}
		// BUTTONS
		{
			process = new GuiButton(1, sr.func_78326_a() / 2 - 50, sr.func_78328_b() / 2 + 100, 100, 20, "Log in");

			quickLog = new GuiButton(1, sr.func_78326_a() / 2 - 50, sr.func_78328_b() / 2 + 140, 100, 20,
					"quick log");
		}
		clearFields();
		step = 1;
		reInit();
	}

	@Override
	public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
		func_146276_q_();
		field_146297_k.field_71466_p.func_78276_b("Username",
				sr.func_78326_a() / 2 - field_146297_k.field_71466_p.func_78256_a("Username") / 2,
				sr.func_78328_b() / 2 - 120, Color.cyan.getRGB());

		field_146297_k.field_71466_p.func_78276_b("Token", sr.func_78326_a() / 2 - field_146297_k.field_71466_p.func_78256_a("Token") / 2,
				sr.func_78328_b() / 2 - 70, Color.cyan.getRGB());

		field_146297_k.field_71466_p.func_78276_b("UUID", sr.func_78326_a() / 2 - field_146297_k.field_71466_p.func_78256_a("UUID") / 2,
				sr.func_78328_b() / 2 - 20, Color.cyan.getRGB());
		super.func_73863_a(mouseX, mouseY, partialTicks);

		name.func_146194_f();
		token.func_146194_f();
		uuid.func_146194_f();

		switch (step) {
		case 1:
			name.func_146195_b(true);
			token.func_146195_b(false);
			uuid.func_146195_b(false);

			break;
		case 2:
			name.func_146195_b(false);
			token.func_146195_b(true);
			uuid.func_146195_b(false);

			break;
		case 3:
			name.func_146195_b(false);
			token.func_146195_b(false);
			uuid.func_146195_b(true);

			break;
		case 4:
			step = 0;
			name.func_146195_b(true);
			token.func_146195_b(false);
			uuid.func_146195_b(false);
			if (checkFields()) {
				setSession(name.func_146179_b(), uuid.func_146179_b(), token.func_146179_b(), "Mojang");
				ChatUtils.clientMessage("You are now logged as " + field_146297_k.func_110432_I().func_111285_a());
				ChatUtils.errorMessage("You must disconnect to finalize the login phase");
				field_146297_k.field_71439_g.func_71053_j();
			} else {
				ChatUtils.errorMessage("invalid parameters");
				field_146297_k.field_71439_g.func_71053_j();
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void func_146284_a(GuiButton b) {
		if (b == process) {
			step = 0;
			name.func_146195_b(true);
			token.func_146195_b(false);
			uuid.func_146195_b(false);
			if (checkFields()) {
				setSession(name.func_146179_b(), uuid.func_146179_b(), token.func_146179_b(), "Mojang");
				ChatUtils.clientMessage("You are now logged as " + field_146297_k.func_110432_I().func_111285_a());
				ChatUtils.errorMessage("You must disconnect to finalize the login phase");
				field_146297_k.field_71439_g.func_71053_j();
			} else {
				ChatUtils.errorMessage("invalid parameters");
				field_146297_k.field_71439_g.func_71053_j();
			}
		} else if (b == quickLog) {

		}
	}

	public void reInit() {
		h = this.field_146295_m;
		w = this.field_146294_l;
		field_146292_n.clear();
		field_146292_n.add(process);
		field_146292_n.add(quickLog);

	}

	public static boolean isGui() {
		return guiAlps;
	}

	public static void setGui(boolean a) {
		guiAlps = a;
	}

	@Override
	public boolean func_73868_f() {
		return false;
	}

	@Override
	protected void func_73869_a(char typedChar, int keyCode) throws IOException {
		name.func_146201_a(typedChar, keyCode);
		token.func_146201_a(typedChar, keyCode);
		uuid.func_146201_a(typedChar, keyCode);

		if (Keyboard.KEY_ESCAPE == keyCode) {
			field_146297_k.field_71439_g.func_71053_j();
			ChatUtils.debugMessage("aze");
		} else if (Keyboard.KEY_RETURN == keyCode || Keyboard.KEY_TAB == keyCode) {
			step += 1;
		}

		else
			super.func_73869_a(typedChar, keyCode);
	}

	private static void clearFields() {
		name.func_146180_a("");
		token.func_146180_a("");
		uuid.func_146180_a("");

	}

	private static boolean checkFields() {
		if (name.func_146179_b().equals("") || token.func_146179_b().equals("") || uuid.func_146179_b().equals(""))
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
			Minecraft minecraft = Minecraft.func_71410_x();
			Session session = new Session(username, uuid, token, type);
			sessionField.set(Minecraft.func_71410_x(), session);
		} catch (Exception e) {
		}
	}
}
