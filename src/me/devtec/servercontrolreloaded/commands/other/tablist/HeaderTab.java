package me.devtec.servercontrolreloaded.commands.other.tablist;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.servercontrolreloaded.utils.TabList.FormatType;
import me.devtec.theapi.utils.StringUtils;

public class HeaderTab {

	public HeaderTab(CommandSender s, String[] args) {
		String type = args[1];
		if(!(type.equalsIgnoreCase("player")||type.equalsIgnoreCase("global")||type.equalsIgnoreCase("group"))) {
			Loader.advancedHelp(s, "Tablist", "Other", "Header");
			return;
		}
		if(type.equalsIgnoreCase("global")) {
			if(args[2].equalsIgnoreCase("add")) {
				List<String> lines = TabList.getHeader(null, FormatType.GROUP);
				String val = StringUtils.buildString(3, args);
				lines.add(val);
				TabList.setHeader(null, FormatType.GROUP, lines);
				Loader.sendMessages(s, "TabList.Add.Header.Global.Info", Placeholder.c().add("%value%", val));
				return;
			}
			if(args[2].equalsIgnoreCase("remove")) {
				List<String> lines = TabList.getHeader(null, FormatType.GROUP);
				int set = StringUtils.getInt(args[3]);
				if(lines.size() < set || set<0) {
					Loader.sendMessages(s, "TabList.Error", Placeholder.c().add("%size%", lines.size()+"").add("%value%", set+""));
					return;
				}
				String val = lines.remove(set);
				TabList.setHeader(null, FormatType.GROUP, lines);
				Loader.sendMessages(s, "TabList.Remove.Header.Global.Info", Placeholder.c().add("%position%", set+"").add("%value%", val+""));
				return;
			}
			if(args[2].equalsIgnoreCase("set")) {
				List<String> lines = TabList.getHeader(null, FormatType.GROUP);
				int set = StringUtils.getInt(args[3]);
				if(lines.size() < set || set<0) {
					Loader.sendMessages(s, "TabList.Error", Placeholder.c().add("%size%", lines.size()+"").add("%value%", set+""));
					return;
				}
				String val = StringUtils.buildString(4, args);
				lines.set(set, val);
				TabList.setHeader(null, FormatType.GROUP, lines);
				Loader.sendMessages(s, "TabList.Set.Header.Global.Info", Placeholder.c().add("%position%", set+"").add("%value%", val+""));
				return;
			}
			if(args[2].equalsIgnoreCase("list")) {
				Loader.sendMessages(s, "TabList.Get.Header.Global.Info");
				int id = 0;
				for(String ssd : Loader.tab.getStringList("Header"))
					Loader.sendMessages(s, "TabList.Get.Header.Global.List", Placeholder.c().replace("%line%", (id++)+"").replace("%value%",ssd));
				return;
			}
			Loader.advancedHelp(s, "Tablist", "Other", "Header");
			return;
		}
		if(args[3].equalsIgnoreCase("add")) {
			List<String> lines = TabList.getHeader(args[2], type.equalsIgnoreCase("group")?FormatType.PER_PLAYER:FormatType.GROUP);
			String val = StringUtils.buildString(4, args);
			lines.add(val);
			TabList.setHeader(args[2], type.equalsIgnoreCase("group")?FormatType.PER_PLAYER:FormatType.GROUP, lines);
			Loader.sendMessages(s, "TabList.Add.Header."+(type.equalsIgnoreCase("group")?"Group":"Player")+".Info", Placeholder.c().add("%value%", val));
			return;
		}
		if(args[3].equalsIgnoreCase("remove")) {
			List<String> lines = TabList.getHeader(args[2], type.equalsIgnoreCase("group")?FormatType.PER_PLAYER:FormatType.GROUP);
			int set = StringUtils.getInt(args[4]);
			if(lines.size() < set || set<0) {
				Loader.sendMessages(s, "TabList.Error", Placeholder.c().add("%size%", lines.size()+"").add("%value%", set+""));
				return;
			}
			String val = lines.remove(set);
			TabList.setHeader(null, type.equalsIgnoreCase("group")?FormatType.PER_PLAYER:FormatType.GROUP, lines);
			Loader.sendMessages(s, "TabList.Remove.Header."+(type.equalsIgnoreCase("group")?"Group":"Player")+".Info", Placeholder.c().add("%position%", set+"").add("%value%", val+""));
			return;
		}
		if(args[3].equalsIgnoreCase("set")) {
			List<String> lines = TabList.getHeader(args[2], type.equalsIgnoreCase("group")?FormatType.PER_PLAYER:FormatType.GROUP);
			int set = StringUtils.getInt(args[4]);
			if(lines.size() < set || set<0) {
				Loader.sendMessages(s, "TabList.Error", Placeholder.c().add("%size%", lines.size()+"").add("%value%", set+""));
				return;
			}
			String val = StringUtils.buildString(5, args);
			lines.set(set, val);
			TabList.setHeader(args[2], type.equalsIgnoreCase("group")?FormatType.PER_PLAYER:FormatType.GROUP, lines);
			Loader.sendMessages(s, "TabList.Set.Header."+(type.equalsIgnoreCase("group")?"Group":"Player")+".Info");
			return;
		}
		if(args[3].equalsIgnoreCase("list")) {
			Loader.sendMessages(s, "TabList.Get.Header."+(type.equalsIgnoreCase("group")?"Group":"Player")+".Info");
			int id = 0;
			for(String ssd : Loader.tab.getStringList((type.equalsIgnoreCase("group")?"PerGroup."+args[2]:"PerPlayer."+args[2])+".Header"))
				Loader.sendMessages(s, "TabList.Get.Header."+(type.equalsIgnoreCase("group")?"Group":"Player")+".List", Placeholder.c().replace("%line%", (id++)+"").replace("%value%",ssd));
			return;
		}
		Loader.advancedHelp(s, "Tablist", "Other", "Header");
	}
}
