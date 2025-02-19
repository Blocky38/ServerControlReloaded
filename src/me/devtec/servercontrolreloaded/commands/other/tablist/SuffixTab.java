package me.devtec.servercontrolreloaded.commands.other.tablist;

import org.bukkit.command.CommandSender;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.servercontrolreloaded.utils.TabList.FormatType;
import me.devtec.theapi.utils.StringUtils;

public class SuffixTab {

	public SuffixTab(CommandSender s, String[] args) {
		String type = args[1];
		if(!(type.equalsIgnoreCase("player")||type.equalsIgnoreCase("group"))) {
			Loader.advancedHelp(s, "Tablist", "Other", "Suffix");
			return;
		}
		String name = args[2];
		String ttype = args[3];
		if(!(ttype.equalsIgnoreCase("nametag")||ttype.equalsIgnoreCase("tablist")||ttype.equalsIgnoreCase("tab"))) {
			Loader.advancedHelp(s, "Tablist", "Other", "Suffix");
			return;
		}
		if(args[4].equalsIgnoreCase("set")) {
			if(args.length==5) {
				Loader.advancedHelp(s, "Tablist", "Other", "Suffix");
				return;
			}
			if(type.equalsIgnoreCase("player")) {
				String val = StringUtils.buildString(5, args);
				if(val.startsWith("\"") && val.endsWith("\""))val=val.substring(1, val.length()-1);
				TabList.setSuffix(name, ttype.equalsIgnoreCase("nametag"), FormatType.PER_PLAYER, val);
				Loader.sendMessages(s, "TabList.Set."+(ttype.equalsIgnoreCase("nametag")?"NameTag":"Tablist")+".Suffix.Player", 
						Placeholder.c().add("%value%", val).replace("%name%", name));
				return;
			}
			String val = StringUtils.buildString(5, args);
			if(val.startsWith("\"") && val.endsWith("\""))val=val.substring(1, val.length()-1);
			TabList.setSuffix(name, ttype.equalsIgnoreCase("nametag"), FormatType.GROUP, val);
			Loader.sendMessages(s, "TabList.Set."+(ttype.equalsIgnoreCase("nametag")?"NameTag":"Tablist")+".Suffix.Group", 
					Placeholder.c().add("%value%", val).replace("%name%", name));
			return;
		}
		if(args[4].equalsIgnoreCase("get")) {
			if(type.equalsIgnoreCase("player")) {
				Loader.sendMessages(s, "TabList.Get."+(ttype.equalsIgnoreCase("nametag")?"NameTag":"Tablist")+".Suffix.Player", 
						Placeholder.c().add("%value%", TabList.getSuffix(name, ttype.equalsIgnoreCase("nametag"), FormatType.PER_PLAYER)).replace("%name%", name));
				return;
			}
			Loader.sendMessages(s, "TabList.Get."+(ttype.equalsIgnoreCase("nametag")?"NameTag":"Tablist")+".Suffix.Group", 
					Placeholder.c().add("%value%", TabList.getSuffix(name, ttype.equalsIgnoreCase("nametag"), FormatType.GROUP)).replace("%name%", name));
			return;
		}
		Loader.advancedHelp(s, "Tablist", "Other", "Suffix");
	}
}
