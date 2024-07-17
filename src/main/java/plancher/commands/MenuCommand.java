package plancher.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import plancher.ui.GuiAlps;

public class MenuCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "alps";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/" + getCommandName() + " (retard)";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		GuiAlps.setGui(true);
	}

	@Override
	public List<String> getCommandAliases() {
		List aliases = new ArrayList<String>();
		aliases.add("alp");
		aliases.add("al");
		aliases.add("a");
		aliases.add("alpes");
		aliases.add("alpas");
		aliases.add("alpa");
		aliases.add("fucklgbt");
		aliases.add("nigga");
		aliases.add("nigger");
		aliases.add("niggers");
		aliases.add("niga");
		aliases.add("nig");
		aliases.add("ngga");
		aliases.add("niger");
		aliases.add("nigerian");
		aliases.add("allah");
		aliases.add("salarab");
		aliases.add("salenegre");
		aliases.add("negre");
		aliases.add("negro");
		aliases.add("whore");



		return aliases;
	}

	public int getRequiredPermissionLevel() {
		return 0;
	}

}
