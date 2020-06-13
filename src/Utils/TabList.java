package Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI;

public class TabList {
	private static String group(Player p) {
		if (API.existVaultPlugin()) {
			if (Loader.perms != null && Loader.vault != null)
				if (Loader.perms.getPrimaryGroup(p) != null) {
					return Loader.perms.getPrimaryGroup(p);
				}
			return "default";
		}
		return "default";
	}

	public static String getGroup(Player p) {
		if (Loader.tab.getString("Groups." + group(p) + ".Priorite") != null) {
			return Loader.tab.getString("Groups." + group(p) + ".Priorite");
		}
		return "z";
	}

	public static String getPrefix(Player p) {
		if (Loader.tab.getString("Groups." + group(p) + ".Prefix") != null) {
			return replace(Loader.tab.getString("Groups." + group(p) + ".Prefix"), p);
		}
		return Loader.getInstance.getPrefix(p);
	}

	public static String getSuffix(Player p) {
		if (Loader.tab.getString("Groups." + group(p) + ".Suffix") != null) {
			return replace(Loader.tab.getString("Groups." + group(p) + ".Suffix"), p);
		}
		return Loader.getInstance.getSuffix(p);
	}

	public static String replace(String header, Player p) {
		header = TheAPI.getPlaceholderAPI().setPlaceholders(p, header);
		String customname = p.getName();
		String group = Loader.FormatgetGroup(p);
		if (Loader.vault != null)
			group = Loader.vault.getPrimaryGroup(p);
		if (p.getCustomName() != null)
			customname = p.getCustomName();
		String displayname = p.getName();
		if (p.getDisplayName() != null)
			displayname = p.getDisplayName();
		return header.replace("%money%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(p.getName()), true))
				.replace("%online%", TheAPI.getOnlinePlayers().size() + "")
				.replace("%max_players%", TheAPI.getMaxPlayers() + "")
				.replace("%ping%", Loader.getInstance.pingPlayer(p))
				.replace("%time%", new SimpleDateFormat(Loader.config.getString("Format.Time")).format(new Date()))
				.replace("%date%", new SimpleDateFormat(Loader.config.getString("Format.Date")).format(new Date()))
				.replace("%world%", p.getWorld().getName()).replace("%hp%", p.getHealth() + "")
				.replace("%health%", p.getHealth() + "").replace("%food%", p.getFoodLevel() + "")
				.replace("%x%", p.getLocation().getBlockX() + "").replace("%y%", p.getLocation().getBlockY() + "")
				.replace("%z%", p.getLocation().getBlockZ() + "").replace("%vault-group%", group)
				.replace("%vault-prefix%", Loader.getInstance.getPrefix(p))
				.replace("%prefix%", Loader.getInstance.getPrefix(p))
				.replace("%vault-suffix%", Loader.getInstance.getSuffix(p))
				.replace("%suffix%", Loader.getInstance.getSuffix(p)).replace("%group%", getGroup(p))
				.replace("%tps%", TheAPI.getServerTPS() + "").replace("%ping%", Loader.getInstance.pingPlayer(p) + "")
				.replace("%kills%", "" + p.getStatistic(Statistic.PLAYER_KILLS))
				.replace("%deaths%", "" + p.getStatistic(Statistic.DEATHS)).replace("%player%", p.getName())
				.replace("%playername%", displayname).replace("%customname%", customname)
				.replace("%ram_free%", TheAPI.getMemoryAPI().getFreeMemory(false) + "")
				.replace("%ram_free_percentage%", TheAPI.getMemoryAPI().getFreeMemory(true) + "%")
				.replace("%ram_usage%", TheAPI.getMemoryAPI().getUsedMemory(false) + "")
				.replace("%ram_usage_percentage%", TheAPI.getMemoryAPI().getUsedMemory(true) + "%")
				.replace("%ram_max%", TheAPI.getMemoryAPI().getMaxMemory() + "").replace("%ram_max_percentage%", "100%") // lol,
																															// why
																															// ?
																															// :D
				.replace("%afk%", Loader.getInstance.isAfk(p));
	}

	public static void setNameTag(Player p) {
		String prefix = TheAPI.colorize(TabList.replace(TabList.getPrefix(p), p));
		String suffix = TheAPI.colorize(TabList.replace(TabList.getSuffix(p), p));
		TheAPI.getTabListAPI().setTabListName(p, prefix + p.getName() + suffix);
		NameTagChanger.setNameTag(p, prefix, suffix);
	}

	static int test;

	public static void removeTab() {
		for (Player p : TheAPI.getOnlinePlayers()) {
			NameTagChanger.remove(p);
			p.setPlayerListName(p.getName());
			TheAPI.getTabListAPI().setHeaderFooter(p, "", "");
		}
	}

	private static String get(String path, Player p) {
		if (setting.tab_header || setting.tab_footer) {
			if (Loader.tab.getStringList(path) != null) {
				String a = TheAPI.getStringUtils().join(Loader.tab.getStringList(path), "\n");
				return replace(a, p);
			}
		}
		return "";
	}

	private static String getPath(Player p, String what) {
		if (what.equalsIgnoreCase("footer") && setting.tab_footer
				|| what.equalsIgnoreCase("header") && setting.tab_header) {
			if (Loader.tab.getString("PerPlayerTabList." + p.getName() + "." + what) != null)
				return get("PerPlayerTabList." + p.getName() + "." + what, p);
			else if (Loader.tab.getString("PerWorldTabList." + p.getWorld().getName() + "." + what) != null)
				return get("PerWorldTabList." + p.getWorld().getName() + "." + what, p);
			else {
				return get(what, p);
			}
		}
		return "";
	}

	public static void setFooterHeader(Player p) {
		TheAPI.getTabListAPI().setHeaderFooter(p, getPath(p, "Header"), getPath(p, "Footer"));
	}
}
