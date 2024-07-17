package plancher.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ChatUtils {

	final static String ALPS = EnumChatFormatting.GREEN + "(" + EnumChatFormatting.DARK_BLUE + "Alps Client"
			+ EnumChatFormatting.GREEN + ")";

	public static void clientMessage(String str) {
		if (Minecraft.func_71410_x().field_71439_g != null)
			Minecraft.func_71410_x().field_71439_g.func_146105_b(new ChatComponentText(ALPS + " " + str));
	}

	public static void errorMessage(String str) {
		if (Minecraft.func_71410_x().field_71439_g != null)
			Minecraft.func_71410_x().field_71439_g
					.func_146105_b(new ChatComponentText(ALPS + EnumChatFormatting.RED + " " + str));
	}

	public static void debugMessage(String str) {
		if (Minecraft.func_71410_x().field_71439_g != null)
			Minecraft.func_71410_x().field_71439_g.func_146105_b(
					new ChatComponentText(EnumChatFormatting.RED + "(debug) " + EnumChatFormatting.RESET + str));
	}

	public static void serverMessage(String str) {
		if (str.length() == 0)
			return;
		if (Minecraft.func_71410_x().field_71439_g != null)
			Minecraft.func_71410_x().field_71439_g.func_71165_d(str);
	}

}
