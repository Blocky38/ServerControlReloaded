package me.devtec.servercontrolreloaded.commands.bansystem;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class TempBanIP implements CommandExecutor, TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.has(s, "TempBanIP", "BanSystem")) {
			if(args.length==2) {
				try {
					if(args[1].substring(args[1].length()-1, args[1].length()).matches("[0-9]"))
						return Arrays.asList(args[1]+"s",args[1]+"m",args[1]+"h",args[1]+"d",args[1]+"w",args[1]+"mo");
				}catch(Exception e) {
					return Arrays.asList("15m","2h","2h30m","6h","12h","7d");
				}
			}
			return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
		}
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "TempBanIP", "BanSystem")) {
			if(!CommandsManager.canUse("BanSystem.TempBanIP", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("BanSystem.TempBanIP", s))));
				return true;
			}
			if (args.length == 0) {
				Loader.Help(s, "TempBanIP", "BanSystem");
				return true;
			}
			if (args.length == 1) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				TheAPI.getPunishmentAPI().banIP(args[0], StringUtils.getTimeFromString(Loader.config.getString("BanSystem.TempBanIP.Time")), Loader.config.getString("BanSystem.TempBanIP.Text").replace("%reason%",
								Loader.config.getString("BanSystem.TempBanIP.Reason")));
				Loader.sendMessages(s, "BanSystem.TempBanIP.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempBanIP.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(Loader.config.getString("BanSystem.TempBanIP.Time")))));
				Loader.sendBroadcasts(s, "BanSystem.TempBanIP.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempBanIP.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(Loader.config.getString("BanSystem.TempBanIP.Time")))));
				return true;
			}
			if (args.length == 2) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				TheAPI.getPunishmentAPI().banIP(args[0], StringUtils.getTimeFromString(args[1]), Loader.config.getString("BanSystem.TempBanIP.Text").replace("%reason%",
								Loader.config.getString("BanSystem.TempBanIP.Reason")));
				Loader.sendMessages(s, "BanSystem.TempBanIP.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempBanIP.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))));
				Loader.sendBroadcasts(s, "BanSystem.TempBanIP.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempBanIP.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))));
				return true;
			}
			if (TheAPI.getUser(args[0]).getBoolean("Immune")
					|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
				Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
				return true;
			}
			String msg = StringUtils.buildString(2, args);
			if(msg.endsWith("-s")||msg.endsWith("- s")) {
				msg = msg.endsWith("- s")?msg.substring(0, msg.length()-3):msg.substring(0, msg.length()-2);
				TheAPI.getPunishmentAPI().banIP(args[0], StringUtils.timeFromString(args[1]), Loader.config.getString("BanSystem.TempBanIP.Text").replace("%reason%",msg));
				Loader.sendMessages(s, "BanSystem.TempBanIP.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]).replace("%reason%", msg+" &f[Silent]").replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))));
				Loader.sendBroadcasts(s, "BanSystem.TempBanIP.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]).replace("%reason%", msg+" &f[Silent]").replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))), "servercontrol.silent");
				return true;
			}
			TheAPI.getPunishmentAPI().banIP(args[0], StringUtils.timeFromString(args[1]), Loader.config.getString("BanSystem.TempBanIP.Text").replace("%reason%",msg));
			Loader.sendMessages(s, "BanSystem.TempBanIP.Sender", Placeholder.c().replace("%operator%", s.getName())
					.replace("%ip%", args[0]).replace("%reason%", msg).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))));
			Loader.sendBroadcasts(s, "BanSystem.TempBanIP.Admins", Placeholder.c().replace("%operator%", s.getName())
					.replace("%ip%", args[0]).replace("%reason%", msg).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))));
			return true;
		}
		Loader.noPerms(s, "TempBanIP", "BanSystem");
		return true;
	}
}