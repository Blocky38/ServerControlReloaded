package me.devtec.servercontrolreloaded.commands.bansystem;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.scr.events.BanlistUnbanEvent;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.punishmentapi.Punishment;
import me.devtec.theapi.punishmentapi.Punishment.PunishmentType;
import me.devtec.theapi.utils.StringUtils;

public class UnBan implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		if(args.length==1 && Loader.has(s, "UnBan", "BanSystem")) {
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		}
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "UnBan", "BanSystem")) {
			if(!CommandsManager.canUse("BanSystem.UnBan", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("BanSystem.UnBan", s))));
				return true;
			}
			if (args.length == 0) {
				Loader.Help(s, "UnBan", "BanSystem");
				return true;
			}
			Punishment p = TheAPI.getPunishmentAPI().getPunishments(args[0]).stream().filter(a -> a.getType()==PunishmentType.BAN).findFirst().orElse(null);
			if (p!=null) {
				TheAPI.callEvent(new BanlistUnbanEvent(p));
				p.remove();
				Loader.sendMessages(s, "BanSystem.UnBan.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]));
				Loader.sendBroadcasts(s, "BanSystem.UnBan.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]));
				return true;
			}
			Loader.sendMessages(s, "BanSystem.Not.Banned", Placeholder.c().replace("%playername%", args[0]).replace("%player%", args[0]));
			return true;
		}
		Loader.noPerms(s, "UnBan", "BanSystem");
		return true;
	}
}
