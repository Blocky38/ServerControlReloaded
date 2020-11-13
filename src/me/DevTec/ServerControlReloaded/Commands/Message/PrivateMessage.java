package me.DevTec.ServerControlReloaded.Commands.Message;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.Utils.Colors;
import me.DevTec.TheAPI.TheAPI;

public class PrivateMessage implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "PrivateMessage", "Message")) {
			if (args.length == 0 || args.length == 1) {
				Loader.Help(s, "PrivateMessage", "Message");
			}
			if (args.length >= 2) {

				String msg = Colors.colorize(TheAPI.buildString(args).replaceFirst(args[0] + " ", ""), false, s);
				String from = "";
				String to = "";
				if (args[0].equalsIgnoreCase("CONSOLE")) {
					if (s instanceof Player == false) {
						TheAPI.getUser(s.getName()).setAndSave("Server.Reply", "CONSOLE");
					} else {
						TheAPI.getUser(s.getName()).setAndSave("Server.Reply", s.getName());
						TheAPI.getUser(s.getName()).setAndSave("Players." + s.getName() + ".Reply", "CONSOLE");
					}
					from = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageFrom")
							.replace("%from%", s.getName()).replace("%to%", "CONSOLE"));
					to = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageTo")
							.replace("%from%", s.getName()).replace("%to%", "CONSOLE"));
					to = to.replace("%message%", msg);
					from = from.replace("%message%", msg);
					s.sendMessage(to);
					Bukkit.getConsoleSender().sendMessage(from);
					return true;
				} else {
					Player p = TheAPI.getPlayer(args[0]);
					if (p != null) {
						from = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageFrom")
								.replace("%from%", s.getName()).replace("%to%", p.getName()));
						to = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageTo")
								.replace("%from%", s.getName()).replace("%to%", p.getName()));
						to = to.replace("%message%", msg);
						from = from.replace("%message%", msg);
						if (s instanceof Player == false) {
							TheAPI.getUser(s.getName()).setAndSave("Reply", "CONSOLE");
							TheAPI.getUser("CONSOLE").setAndSave("Server", p.getName());
						} else {
							TheAPI.getUser(s.getName()).setAndSave("Reply", p.getName());
							TheAPI.getUser(p).setAndSave("Reply", s.getName());
						}
						s.sendMessage(to);
						p.sendMessage(from);
						return true;
					}
					Loader.notOnline(s, args[0]);
					return true;
				}				
			}
			return true;
		}
		Loader.noPerms(s, "PrivateMessage", "Message");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}
