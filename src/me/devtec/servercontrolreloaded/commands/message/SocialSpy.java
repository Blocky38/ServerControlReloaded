package me.devtec.servercontrolreloaded.commands.message;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class SocialSpy implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.has(s, "SocialSpy", "Message")) {
			if(!CommandsManager.canUse("Message.SocialSpy", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Message.SocialSpy", s))));
				return true;
			}
			if(TheAPI.getUser((Player)s).getBoolean("socialspy"))
				Loader.sendMessages(s, "SocialSpy.Off");
			else
				Loader.sendMessages(s, "SocialSpy.On");
			TheAPI.getUser((Player)s).setSave("socialspy", !TheAPI.getUser((Player)s).getBoolean("socialspy"));
			return true;
		}
		Loader.noPerms(s, "SocialSpy", "Message");
		return true;
	}
	
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] arg3) {
		return Collections.emptyList();
	}
}
