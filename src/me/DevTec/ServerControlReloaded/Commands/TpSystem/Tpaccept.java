package me.DevTec.ServerControlReloaded.Commands.TpSystem;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;

public class Tpaccept implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "TpaAccept", "TpSystem")) {
			if (s instanceof Player) {
				RequestMap.accept((Player)s);
				return true;
		}}
		Loader.noPerms(s, "TpaAccept", "TpSystem");
		return true;
	}
}