package me.devtec.servercontrolreloaded.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.economy.EcoTop;
import me.devtec.servercontrolreloaded.commands.info.Staff;
import me.devtec.servercontrolreloaded.commands.time.PlayTime;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Item;
import me.devtec.servercontrolreloaded.utils.playtime.PlayTimeUtils;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.MemoryAPI;
import me.devtec.theapi.apis.TabListAPI;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.sortedmap.SortedMap.ComparableObject;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.reflections.Ref;
import me.devtec.theapi.utils.theapiutils.LoaderClass;

public class TabList {
	// GROUP, PRIORITE
	protected static final HashMap<String, String> sorting = new HashMap<>();

	public static void reload() {
		sorting.clear();
		List<String> sd = Loader.tab.getStringList("Options.Sorting-Groups");
		List<String> priorites = generate(sd.size());
		int current = 0;
		for(String a : sd)
			sorting.put(a, priorites.get(current++));
		
	}
	
	private static List<String> generate(int size) {
		List<String> a = new LinkedList<>();
		int length = (""+size).length()+1;
		for(int i = 0; i < size; ++i) {
			String s = "";
			int limit = length-(i+"").length();
			for(int d = 0; d < limit; ++d)
				s+="0";
			s+=i;
			a.add(s);
		}
		return a;
	}
	public enum FormatType {
		PER_PLAYER("PerPlayer"),
		PER_WORLD("PerWorld"),
		GROUP("Groups");
		private String ph;
		FormatType(String string) {
			ph=string;
		}
		
		public String getPath() {
			return ph;
		}
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 */
	public static String getPrefix(String path, boolean nametag, FormatType type) {
		return Loader.tab.getString(type.getPath()  +path+ "."+(nametag?"NameTag":"TabList")+".Prefix");
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 */
	public static String getSuffix(String path, boolean nametag, FormatType type) {
		return Loader.tab.getString(type.getPath() + path+"."+(nametag?"NameTag":"TabList")+".Suffix");
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 */
	public static String getNameFormat(String path, FormatType type) {
		return Loader.tab.getString(type.getPath() +path+ ".Format");
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 * @param value New prefix
	 */
	public static void setPrefix(String path, boolean nametag, FormatType type, String value) {
		Loader.tab.set(type.getPath()  +path+ "."+(nametag?"NameTag":"TabList")+".Prefix", value);
		Loader.tab.save();
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 * @param value New prefix
	 */
	public static void setSuffix(String path, boolean nametag, FormatType type, String value) {
		Loader.tab.set(type.getPath() + path+"."+(nametag?"NameTag":"TabList")+".Suffix", value);
		Loader.tab.save();
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 * @param value New header
	 */
	public static void setHeader(String path, FormatType type, List<String> value) {
		Loader.tab.set(path==null?"Header":type.getPath() + path+".Header", value);
		Loader.tab.save();
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 * @param value New header
	 */
	public static void setFooter(String path, FormatType type, List<String> value) {
		Loader.tab.set(path==null?"Footer":type.getPath() + path+".Footer", value);
		Loader.tab.save();
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 */
	public static List<String> getHeader(String path, FormatType type) {
		return Loader.tab.getStringList(path==null?"Header":type.getPath() + path+".Header");
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 */
	public static List<String> getFooter(String path, FormatType type) {
		return Loader.tab.getStringList(path==null?"Footer":type.getPath() + path+".Footer");
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 * @param value New prefix
	 */
	public static void setNameFormat(String path, FormatType type, String value) {
		Loader.tab.set(type.getPath() +path+ ".Format", value);
		Loader.tab.save();
	}

	public static String getPrefix(Player p, boolean nametag) {
		if(Loader.tab==null||p==null)return null;
		String end = "."+(nametag?"NameTag":"TabList")+".Prefix";
		
		if (Loader.tab.get("PerPlayer." + p.getName() + end)!=null)
			return replace(Loader.tab.getString("PerPlayer." + p.getName() + end), p, true);
		if (Loader.tab.get("PerWorld." + p.getWorld().getName() + end)!=null)
			return replace(Loader.tab.getString("PerWorld." + p.getWorld().getName() + end), p, true);
		String g = Staff.getGroup(p);
		if (Loader.tab.get("Groups." + g + end)!=null)
			return replace(Loader.tab.getString("Groups." + g + end), p, true);
		return null;
	}

	public static String getSuffix(Player p, boolean nametag) {
		if(Loader.tab==null||p==null)return null;
		String end = "."+(nametag?"NameTag":"TabList")+".Suffix";

		if (Loader.tab.get("PerPlayer." + p.getName() + end)!=null)
			return replace(Loader.tab.getString("PerPlayer." + p.getName() + end), p, true);
		if (Loader.tab.get("PerWorld." + p.getWorld().getName() + end)!=null)
			return replace(Loader.tab.getString("PerWorld." + p.getWorld().getName() + end), p, true);
		String g = Staff.getGroup(p);
		if (Loader.tab.get("Groups." + g + end)!=null)
			return replace(Loader.tab.getString("Groups." + g + end), p, true);
		return null;
	}

	public static String getNameFormat(Player p) {
		if(Loader.tab==null||p==null)return p.getName();
		String end = ".Format";

		if (Loader.tab.get("PerPlayer." + p.getName() + end)!=null)
			return replace(Loader.tab.getString("PerPlayer." + p.getName() + end), p, true);
		if (Loader.tab.get("PerWorld." + p.getWorld().getName() + end)!=null)
			return replace(Loader.tab.getString("PerWorld." + p.getWorld().getName() + end), p, true);
		String g = Staff.getGroup(p);
		if (Loader.tab.get("Groups." + g + end)!=null)
			return replace(Loader.tab.getString("Groups." + g + end), p, true);
		return "%tab_prefix% "+p.getName()+" %tab_suffix%";
	}
	static Pattern playtimetop = Pattern.compile("\\%playtimetop_([0-9]+)_(name|time|formatted_time|format_time|format)\\%", Pattern.CASE_INSENSITIVE)
			, playtime_player = Pattern.compile("\\%playtime_[_A-Za-z0-9]+\\%", Pattern.CASE_INSENSITIVE)
					, playtime_worldOrGm = Pattern.compile("\\%playtime_([_A-Za-z0-9]+)_(.+?)\\%", Pattern.CASE_INSENSITIVE)
					, playtime_world_gm = Pattern.compile("\\%playtime_([_A-Za-z0-9]+)_(.+?)_(SURVIVAL|CREATIVE|ADVENTURE|SPECTATOR)\\%", Pattern.CASE_INSENSITIVE)
					, playtime_worldOrGmMe = Pattern.compile("\\%playtime_(.+?)\\%", Pattern.CASE_INSENSITIVE)
					, playtime_world_gmMe = Pattern.compile("\\%playtime_(.+?)_(SURVIVAL|CREATIVE|ADVENTURE|SPECTATOR)\\%", Pattern.CASE_INSENSITIVE)
					, balancetop = Pattern.compile("\\%balancetop_([0-9]+)_(name|money|format_money|formatted_money)\\%", Pattern.CASE_INSENSITIVE);
	
	public static String replace(String header, Player p, boolean color) {
		if(header.contains("%playtimetop_")) {
			Matcher m = playtimetop.matcher(header);
			while(m.find()) {
				ComparableObject<String, Integer> player = PlayTimeUtils.getTop(StringUtils.getInt(m.group(1)));
				switch(m.group(2).toLowerCase()) {
				case "name":
					header=header.replace(m.group(), player.getKey());
					break;
				case "time":
					header=header.replace(m.group(), player.getValue()+"");
					break;
				case "format_time":
				case "formatted_time":
					header=header.replace(m.group(), StringUtils.timeToString(player.getValue()));
					break;
				case "format":
					header=header.replace(m.group(), Loader.trans.getString("PlayTime.PlayTop.Top").replace("%position%", StringUtils.getInt(m.group(1))+"")
							.replace("%player%", player.getKey()).replace("%playername%", PlayTime.player(p, player.getKey()))
							.replace("%playtime%", StringUtils.timeToString(player.getValue())));
					break;
				}
			}
		}
		if(p!=null) {
		if(header.contains("%money%"))
				header=header.replace("%money%", API.setMoneyFormat(EconomyAPI.getBalance(p.getName()), false));
		if(header.contains("%formatted_money%"))
			header=header.replace("%formatted_money%", API.setMoneyFormat(EconomyAPI.getBalance(p.getName()), true));
		if(header.contains("%format_money%"))
			header=header.replace("%format_money%", API.setMoneyFormat(EconomyAPI.getBalance(p.getName()), true));
		if(header.contains("%online%")) {
			int seen = 0;
			for(Player s : TheAPI.getPlayers())
				if(API.canSee(p,s.getName()))++seen;
			header=header.replace("%online%", seen + "");
		}
		/*
		 * Playtime Placeholders
		 * 
		 *  %scr_playtime%
		 *  %scr_playtime_<player>%
		 *  %scr_playtime_<player>_<world>%
		 *  %scr_playtime_<player_<world>_<GAMEMODE>%
		 *  %scr_playtime_<player>_<GAMEMODE>
		 *  
		 *  %scr_playtimetop_<position>_name%
		 *  %scr_playtimetop_<position>_time%
		 *  %scr_playtimetop_<position>_format_time%
		 *  %scr_playtimetop_<position>_formatted_time%
		 *  
		 */
		if(header.contains("%playtime%")) {
			header=header.replace("%playtime%", StringUtils.timeToString(PlayTimeUtils.playtime(p)));
		}
		if(header.contains("%raw_playtime%")) {
			header=header.replace("%raw_playtime%", PlayTimeUtils.playtime(p)+"");
		}
		if(header.contains("%playtime_")) {
			String player = null;
			GameMode mode = null;
			World world = null;
			//%playtime_<player>_<world>_<GAMEMODE>%
			Matcher m = playtime_world_gm.matcher(header);
			while(m.find()) {
				if(TheAPI.existsUser(m.group(1))) {
					player = m.group(1);
				}else continue;
				if(Bukkit.getWorld(m.group(2))!=null) {
					world = Bukkit.getWorld(m.group(2));
				}else continue;
				if(GameMode.valueOf(m.group(3).toUpperCase())!=null) {
					mode = GameMode.valueOf(m.group(3).toUpperCase());
				}else continue;
				header=header.replace(m.group(), StringUtils.timeToString(PlayTimeUtils.playtime(player, mode, world)));
			}
			//%playtime_<player>_<world/GAMEMODE>%
			m = playtime_worldOrGm.matcher(header);
			while(m.find()) {
				if(TheAPI.existsUser(m.group(1))) {
					player = m.group(1);
				}else continue;
				if(Bukkit.getWorld(m.group(2))!=null) {
					world = Bukkit.getWorld(m.group(2));
				}
				if(GameMode.valueOf(m.group(3).toUpperCase())!=null) {
					mode = GameMode.valueOf(m.group(3).toUpperCase());
				}
				if(world ==null && mode == null)continue;
				header=header.replace(m.group(), StringUtils.timeToString(PlayTimeUtils.playtime(player, mode, world)));
			}
			//%playtime_<world>_<GAMEMODE>%
			m = playtime_world_gmMe.matcher(header);
			while(m.find()) {
				if(TheAPI.existsUser(m.group(1))) {
					player = m.group(1);
				}else continue;
				if(Bukkit.getWorld(m.group(2))!=null) {
					world = Bukkit.getWorld(m.group(2));
				}else continue;
				if(GameMode.valueOf(m.group(3).toUpperCase())!=null) {
					mode = GameMode.valueOf(m.group(3).toUpperCase());
				}else continue;
				header=header.replace(m.group(), StringUtils.timeToString(PlayTimeUtils.playtime(p, mode, world)));
			}
			//%playtime_<world/GAMEMODE>%
			m = playtime_worldOrGmMe.matcher(header);
			while(m.find()) {
				if(TheAPI.existsUser(m.group(1))) {
					player = m.group(1);
				}else continue;
				if(Bukkit.getWorld(m.group(2))!=null) {
					world = Bukkit.getWorld(m.group(2));
				}
				if(GameMode.valueOf(m.group(3).toUpperCase())!=null) {
					mode = GameMode.valueOf(m.group(3).toUpperCase());
				}
				if(world ==null && mode == null)continue;
				header=header.replace(m.group(), StringUtils.timeToString(PlayTimeUtils.playtime(p, mode, world)));
			}
		}
		
		if(header.contains("%ping%"))
			header=header.replace("%ping%", Loader.getInstance.pingPlayer(p));
		if(header.contains("%world%"))
			header=header.replace("%world%", p.getWorld().getName());
		if(header.contains("%hp%"))
			header=header.replace("%hp%", StringUtils.fixedFormatDouble(((Damageable)p).getHealth()));
		if(header.contains("%health%"))
			header=header.replace("%health%", StringUtils.fixedFormatDouble(((Damageable)p).getHealth()));
		if(header.contains("%food%"))
			header=header.replace("%food%", p.getFoodLevel() + "");
		if(header.contains("%x%"))
			header=header.replace("%x%", StringUtils.fixedFormatDouble(p.getLocation().getX()));
		if(header.contains("%y%"))
			header=header.replace("%y%", StringUtils.fixedFormatDouble(p.getLocation().getY()));
		if(header.contains("%z%"))
			header=header.replace("%z%", StringUtils.fixedFormatDouble(p.getLocation().getZ()));
		if(header.contains("%vault_group%"))
			header=header.replace("%vault_group%", API.getGroup(p));
		if(header.contains("%vault_prefix%"))
			header=header.replace("%vault_prefix%", ""+TheAPI.colorize(Loader.getChatFormat(p, Item.PREFIX)));
		if(header.contains("%vault_suffix%"))
			header=header.replace("%vault_suffix%", ""+TheAPI.colorize(Loader.getChatFormat(p, Item.SUFFIX)));
		if(header.contains("%group%"))
			header=header.replace("%group%", Staff.getGroup(p));
		if(header.contains("%kills%"))
			header=header.replace("%kills%", "" + p.getStatistic(Statistic.PLAYER_KILLS));
		if(header.contains("%deaths%"))
			header=header.replace("%deaths%", "" + p.getStatistic(Statistic.DEATHS));
		if(header.contains("%deaths%"))
			header=header.replace("%deaths%", "" + p.getStatistic(Statistic.DEATHS));
		if(header.contains("%player%"))
			header=header.replace("%player%", p.getName());
		if(header.contains("%xp%"))
			header=header.replace("%xp%", p.getTotalExperience()+"");
		if(header.contains("%level%"))
			header=header.replace("%level%", p.getLevel()+"");
		if(header.contains("%exp%"))
			header=header.replace("%exp%", p.getTotalExperience()+"");
		if(header.contains("%playername%")) {
			String displayname = p.getName();
			if (p.getDisplayName() != null)
				displayname = p.getDisplayName();
			header=header.replace("%playername%", StringUtils.colorize(displayname));
		}
		if(header.contains("%customname%")) {
			String customname = p.getName();
		if (p.getCustomName() != null)
			customname = p.getCustomName();
		if(TheAPI.getUser(p).exists("DisplayName"))
			customname = TheAPI.getUser(p).getString("DisplayName");
			header=header.replace("%customname%", StringUtils.colorize(customname));
		}
		if(header.contains("%afk%"))
			header=header.replace("%afk%", Loader.getAFK(p));
		if(header.contains("%vanish%"))
			header=header.replace("%vanish%", Loader.getElse("Vanish", API.hasVanish(p)));
		if(header.contains("%fly%"))
			header=header.replace("%fly%", Loader.getElse("Fly", API.getSPlayer(p).hasFlyEnabled(true)||API.getSPlayer(p).hasTempFlyEnabled()));
		if(header.contains("%god%"))
			header=header.replace("%god%", Loader.getElse("God", API.getSPlayer(p).hasGodEnabled()));
		}else {
			if(header.contains("%online%"))
				header=header.replace("%online%", TheAPI.getOnlineCount()+"");
			if(header.contains("%playtime_")) {
				String player = null;
				GameMode mode = null;
				World world = null;
				//%playtime_<player>_<world>_<GAMEMODE>%
				Matcher m = playtime_world_gm.matcher(header);
				while(m.find()) {
					if(TheAPI.existsUser(m.group(1))) {
						player = m.group(1);
					}else continue;
					if(Bukkit.getWorld(m.group(2))!=null) {
						world = Bukkit.getWorld(m.group(2));
					}else continue;
					if(GameMode.valueOf(m.group(3).toUpperCase())!=null) {
						mode = GameMode.valueOf(m.group(3).toUpperCase());
					}else continue;
					header=header.replace(m.group(), StringUtils.timeToString(PlayTimeUtils.playtime(player, mode, world)));
				}
				//%playtime_<player>_<world/GAMEMODE>%
				m = playtime_worldOrGm.matcher(header);
				while(m.find()) {
					if(TheAPI.existsUser(m.group(1))) {
						player = m.group(1);
					}else continue;
					if(Bukkit.getWorld(m.group(2))!=null) {
						world = Bukkit.getWorld(m.group(2));
					}
					if(GameMode.valueOf(m.group(3).toUpperCase())!=null) {
						mode = GameMode.valueOf(m.group(3).toUpperCase());
					}
					if(world ==null && mode == null)continue;
					header=header.replace(m.group(), StringUtils.timeToString(PlayTimeUtils.playtime(player, mode, world)));
				}
			}
		}
		if(header.contains("%balancetop_")) {
			Matcher m = balancetop.matcher(header);
			while(m.find()) {
				int pos = StringUtils.getInt(m.group(1));
				String world = Eco.getEconomyGroupByWorld(Bukkit.getWorlds().get(0).getName());
				if(p!=null && p instanceof Player)
					world = Eco.getEconomyGroupByWorld((p).getWorld().getName());
				if(EcoTop.h==null || EcoTop.h.isEmpty())
					EcoTop.reload(p);
				
				int i = 0; 
				Entry<Double, String> user = null;
				Iterator<Entry<Double, String>> it = EcoTop.h.get(world).entrySet().iterator();
				while(it.hasNext()) {
					Entry<Double, String> next = it.next();
					if(++i==pos) {
						user=next;
						break;
					}
				}
				if(user==null)return null;
				switch(m.group(2).toLowerCase()) {
				case "format_money":
				case "formatted_money":
					return API.setMoneyFormat(user.getKey(), false);
				case "money":
					return user.getKey()+"";
				case "name":
					return user.getValue();
				}
			}
		}
		if(header.contains("%players_max%"))
			header=header.replace("%players_max%", TheAPI.getMaxPlayers() + "");
		if(header.contains("%online_max%"))
			header=header.replace("%online_max%", TheAPI.getMaxPlayers() + "");
		if(header.contains("%max_online%"))
			header=header.replace("%max_online%", TheAPI.getMaxPlayers() + "");
		if(header.contains("%max_players%"))
			header=header.replace("%max_players%", TheAPI.getMaxPlayers() + "");
		if(header.contains("%time%"))
			header=header.replace("%time%", new SimpleDateFormat(Loader.config.getString("Format.Time")).format(new Date()))
		;if(header.contains("%date%"))
			header=header.replace("%date%", new SimpleDateFormat(Loader.config.getString("Format.Date")).format(new Date()));
		if(header.contains("%tps%"))
			header=header.replace("%tps%", TheAPI.getServerTPS() + "")
		;if(header.contains("%ram_free%"))
			header=header.replace("%ram_free%", MemoryAPI.getFreeMemory(false) + "")
		;if(header.contains("%ram_free_percentage%"))
			header=header.replace("%ram_free_percentage%", MemoryAPI.getFreeMemory(true) + "%")
		;if(header.contains("%ram_usage%"))
			header=header.replace("%ram_usage%", MemoryAPI.getUsedMemory(false) + "")
		;if(header.contains("%ram_usage_percentage%"))
			header=header.replace("%ram_usage_percentage%", MemoryAPI.getUsedMemory(true) + "%")
		;if(header.contains("%ram_max%"))
			header=header.replace("%ram_max%", MemoryAPI.getMaxMemory() + "").replace("%ram_max_percentage%", "100%");
		header = PlaceholderAPI.setPlaceholders(p, header);
		if(color)
			header=TheAPI.colorize(header);
		return header;
	}
	
	public static final AnimationManager aset = new AnimationManager();
	
	public static void update() {
		aset.update();
	}
	
	public static String getTabListName(Player p) {
		String p2 = getPrefix(p, false), s2 = getSuffix(p, false);
		return getNameFormat(p).replace("%tab_prefix%", (p2!=null?replace(p2, p, true):"")).replace("%tab_suffix%", (s2!=null?replace(s2, p, true):""));
	}
	
	public static void setNameTag(Player p) {
		String p1 = getPrefix(p, true), s1 = getSuffix(p, true);
		if(setting.tab_nametag)
			NameTagChanger.setNameTag(p, p1!=null?aset.replaceWithoutColors(p,replace(p1, p, false)):"", s1!=null?aset.replaceWithoutColors(p,replace(s1, p, false)):"");
	}
	
	public static void setTabName(Player p) {
		if(setting.tab_name) //TODO
			TabListAPI.setTabListName(p, getTabListName(p));
	}
	
	public static final Object empty = LoaderClass.nmsProvider.packetPlayerListHeaderFooter("","");
	
	static int test;
	public static void removeTab() {
		for (Player p : TheAPI.getOnlinePlayers()) {
			NameTagChanger.remove(p);
			TabListAPI.setTabListName(p, null);
			Ref.sendPacket(p, empty);
		}
	}

	private static String get(String path, Player p) {
		if (setting.tab_header || setting.tab_footer)
			if (!Loader.tab.getStringList(path).isEmpty())
				return StringUtils.join(Loader.tab.getStringList(path), "\n");
		return "";
	}

	private static String getPath(Player p, String what) {
		if (what.equalsIgnoreCase("footer") && setting.tab_footer
				|| what.equalsIgnoreCase("header") && setting.tab_header) {
			if (Loader.tab.exists("PerPlayer." + p.getName() + "." + what))
				return get("PerPlayer." + p.getName() + "." + what, p);
			else if (Loader.tab.exists("PerWorld." + p.getWorld().getName() + "." + what))
				return get("PerWorld." + p.getWorld().getName() + "." + what, p);
			else if (Loader.tab.exists("PerGroup." + Staff.getGroup(p) + "." + what))
				return get("PerGroup." + Staff.getGroup(p) + "." + what, p);
			else {
				return get(what, p);
			}
		}
		return "";
	}
	
	public static void setFooterHeader(Player p) {
		TabListAPI.setHeaderFooter(p, aset.replaceWithoutColors(p,getPath(p, "Header")), aset.replaceWithoutColors(p,getPath(p, "Footer")));
	}

	public static List<String> replace(List<String> lore, Player a, boolean color) {
		lore.replaceAll(as -> replace(as,a,color));
		return lore;
	}
}
