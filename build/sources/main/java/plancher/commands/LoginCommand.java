package plancher.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import plancher.ui.LoginGui;
import plancher.utils.ChatUtils;

import java.lang.reflect.Field;

public class LoginCommand extends CommandBase {
	@Override
	public String getCommandName() {
		return "login";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/login";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		LoginGui.setGui(true);
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

}
