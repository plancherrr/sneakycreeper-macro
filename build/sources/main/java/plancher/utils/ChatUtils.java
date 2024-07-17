package plancher.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ChatUtils {

	final static String ALPS = EnumChatFormatting.GREEN + "(" + EnumChatFormatting.DARK_BLUE + "Alps Client"
			+ EnumChatFormatting.GREEN + ")";

	public static void clientMessage(String str) {
		if (Minecraft.getMinecraft().thePlayer != null)
			Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(ALPS + " " + str));
	}

	public static void errorMessage(String str) {
		if (Minecraft.getMinecraft().thePlayer != null)
			Minecraft.getMinecraft().thePlayer
					.addChatComponentMessage(new ChatComponentText(ALPS + EnumChatFormatting.RED + " " + str));
	}

	public static void debugMessage(String str) {
		if (Minecraft.getMinecraft().thePlayer != null)
			Minecraft.getMinecraft().thePlayer.addChatComponentMessage(
					new ChatComponentText(EnumChatFormatting.RED + "(debug) " + EnumChatFormatting.RESET + str));
	}

	public static void serverMessage(String str) {
		if (str.length() == 0)
			return;
		if (Minecraft.getMinecraft().thePlayer != null)
			Minecraft.getMinecraft().thePlayer.sendChatMessage(str);
	}

}
