package me.devtec.servercontrolreloaded.commands.tpsystem;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;

public class Tpall implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] arg3) {
		return Collections.emptyList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "TpAll", "TpSystem")) {
			if(!CommandsManager.canUse("TpSystem.TpAll", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("TpSystem.TpAll", s))));
				return true;
			}
			if (s instanceof Player) {
				ArrayList<String> list = new ArrayList<>();
				for (Player p : TheAPI.getOnlinePlayers()) {
					if (p == s)
						continue;
					User d = TheAPI.getUser(p);
					if (!d.getBoolean("TpBlock." + s.getName()) && !d.getBoolean("TpBlock-Global")
							|| d.getBoolean("TpBlock." + s.getName()) && !d.getBoolean("TpBlock-Global")
									&& s.hasPermission("ServerControl.Tpall.Blocked")
							|| d.getBoolean("TpBlock." + s.getName()) && d.getBoolean("TpBlock-Global")
									&& s.hasPermission("ServerControl.Tpall.Blocked")
							|| !d.getBoolean("TpBlock." + s.getName()) && d.getBoolean("TpBlock-Global")
									&& s.hasPermission("ServerControl.Tpall.Blocked")) {
						list.add(p.getName());
						API.teleport(p, ((Player) s).getLocation());
					}
				}
				Loader.sendMessages(s, "TpSystem.TpAll", Placeholder.c().replace("%list%",
						list.isEmpty()?"none":StringUtils.join(list, ", ")).replace("%amount%",
								list.size()+""));
				return true;
			}
			return true;
		}
		Loader.noPerms(s, "TpAll", "TpSystem");
		return true;
	}
}