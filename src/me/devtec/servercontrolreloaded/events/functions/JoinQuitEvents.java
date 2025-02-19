
package me.devtec.servercontrolreloaded.events.functions;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import me.devtec.servercontrolreloaded.commands.message.Mail;
import me.devtec.servercontrolreloaded.commands.other.Vanish;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.API.TeleportLocation;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.ChatFormatter;
import me.devtec.servercontrolreloaded.utils.DisplayManager;
import me.devtec.servercontrolreloaded.utils.NameTagChanger;
import me.devtec.servercontrolreloaded.utils.SPlayer;
import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.servercontrolreloaded.utils.Tasks;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.servercontrolreloaded.utils.setting.DeathTp;
import me.devtec.servercontrolreloaded.utils.punishment.SPunishmentAPI;
import me.devtec.servercontrolreloaded.utils.skins.SkinData;
import me.devtec.servercontrolreloaded.utils.skins.SkinManager;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.TabListAPI;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.punishmentapi.Punishment;
import me.devtec.theapi.punishmentapi.Punishment.PunishmentType;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.datakeeper.User;
import me.devtec.theapi.utils.reflections.Ref;

public class JoinQuitEvents implements Listener {

	public static String replaceAll(String s, Player p) {
		String name = ChatFormatter.displayName(p);
		if(name==null)name=p.getDisplayName()==null?p.getName():p.getDisplayName();
		return PlaceholderAPI.setPlaceholders(p, s.replace("%player%", p.getName())
				.replace("%playername%", name)
				.replace("%customname%", p.getCustomName() != null ? p.getCustomName() : name)
				.replace("%prefix%", setting.prefix+"")
				.replace("%time%", setting.format_time.format(new Date()))
				.replace("%date%", setting.format_date.format(new Date()))
				.replace("%date-time%", setting.format_date_time.format(new Date())));
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		if(e.getResult()!=Result.ALLOWED)return;
		Player p = e.getPlayer();
		new Tasker() {
			public void run() {
				Tasks.regPlayer(p);
				User d = TheAPI.getUser(p);
				if(Loader.config.getBoolean("Options.Skins.onJoin")) {
					if(Loader.config.getBoolean("Options.Skins.Custom.setOwnToAll.set")) {
						String skin = Loader.config.getString("Options.Skins.Custom.setOwnToAll.value");
						SkinManager.generateSkin(skin.replace("%player%", p.getName()), data -> SkinManager.loadSkin(p, data), false);
					}else {
						String skin = d.getString("skin.value");
						if(skin==null) {
							skin=Loader.config.getString("Options.Skins.Custom.default"); //non null
							SkinManager.generateSkin(skin.replace("%player%", p.getName()), data -> SkinManager.loadSkin(p, data), false);
						}else {
							SkinData data = new SkinData();
							data.value=skin;
							data.skinName=d.getString("skin.name");
							data.signature=d.getString("skin.signature");
							SkinManager.loadSkin(p, data);
						}
					}
				}
			}
		}.runTask();
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerJoin(PlayerJoinEvent e) {
		e.setJoinMessage("");
		Player p = e.getPlayer();
		List<String> home = SPunishmentAPI.data.getStringList("tp-home");
		if(home.contains(p.getName().toLowerCase())) {
			home.remove(p.getName().toLowerCase());
			SPunishmentAPI.data.set("tp-home", home);
			SPunishmentAPI.data.save();
			if (setting.deathspawnbol) {
				if (setting.deathspawn == DeathTp.HOME)
					API.teleport(p, API.getTeleportLocation(p, TeleportLocation.HOME));
				else if (setting.deathspawn == DeathTp.BED)
					API.teleport(p, API.getTeleportLocation(p, TeleportLocation.BED));
				else if (setting.deathspawn == DeathTp.SPAWN) {
					API.teleport(p, API.getTeleportLocation(p, TeleportLocation.SPAWN));
					Loader.sendMessages(p, "Spawn.Teleport.You");
				}
			}else
				API.teleport(p, API.getTeleportLocation(p, TeleportLocation.SPAWN));
		}
		User d = TheAPI.getUser(p);
        List<Player> l = TheAPI.getOnlinePlayers();
        l.remove(p);
        String perm = d.getString("vanish.perm");
        boolean v = d.getBoolean("vanish");
        for(Player dd : l) {
        	if(v && !dd.hasPermission(perm))dd.hidePlayer(p);
        	User uu = TheAPI.getUser(dd);
        	if(uu.getBoolean("vanish") && !p.hasPermission(uu.getString("vanish.perm")+""))p.hidePlayer(dd);
        }
		new Tasker() {
			public void run() {
				me.devtec.servercontrolreloaded.utils.punishment.SPunishmentAPI.sendWarnings(p);
				if(TheAPI.isNewerThan(7))
		        if(API.hasVanish(p) || p.getGameMode()==GameMode.SPECTATOR)
		    		Vanish.moveInTab(p, API.hasVanish(p)?0:1, API.hasVanish(p));
				try {
			    	DisplayManager.initializePlayer(p);
				boolean fly = d.getBoolean("FlyOnQuit");
				if(fly)
				d.remove("FlyOnQuit");
				SPlayer s = API.getSPlayer(p); 
				s.setFlySpeed();
				s.setWalkSpeed();
				if (s.hasTempFlyEnabled())
					s.enableTempFly();
				else if ((s.hasFlyEnabled(false)||fly) && Loader.has(p, "Fly", "Other")) {
					p.setAllowFlight(true);
					p.setFlying(true);
				}
				if (s.hasGodEnabled()){
					s.setHP();
					s.setFood();
					s.setFire();
				}
				User d = TheAPI.getUser(p);
				Config f = Loader.config;
				if(API.hasVanish(p.getName())) {
					if(setting.vanish_action) {
						Vanish.task.put(p.getName(), new Tasker() {
							@Override
							public void run() {
								if(!p.isOnline() || !API.hasVanish(p.getName())) {
									cancel();
									return;
								}
								TheAPI.sendActionBar(p, Loader.getTranslation("Vanish.Active").toString());
							}
						}.runRepeating(0, 20));
					}
					Loader.sendMessages(p, "Vanish.Join");
				}
				if (!Mail.getMails(p.getName()).isEmpty())
					Loader.sendMessages(p,"Mail.Notification", Placeholder.c().add("%amount%", "" + d.getStringList("Mails").size()));
				if (setting.sound) {
					//SoundAPI.playSound(p, f.getString("Options.Sounds.Sound"));
					try {
						p.playSound(p.getLocation(), f.getString("Options.Sounds.Sound"), 1, 1);
					} catch (Exception e2) { }
				}
				
				d.set("JoinTime", System.currentTimeMillis() / 1000);
				if (!d.exist("FirstJoin"))
					d.set("FirstJoin", setting.format_date_time.format(new Date()));
				if (!p.hasPlayedBefore()) {
					if (!API.hasVanish(p.getName())) {
						Object o = Loader.events.get("onJoin.First.Text");
						if(o!=null) {
						if(o instanceof Collection) {
						for(Object fa : (Collection<?>)o) {
							if(fa!=null)
							TheAPI.bcMsg(replaceAll(fa+"",p));
						}}else
							if(!(""+o).isEmpty())
								TheAPI.bcMsg(replaceAll(""+o, p));
						}
					}
					Object o = Loader.events.get("onJoin.First.Messages");
					if(o!=null) {
						if(o instanceof Collection) {
						for(Object fa : (Collection<?>)o) {
							if(fa!=null)
							TheAPI.msg(replaceAll(fa+"",p),p);
						}}else
							if(!(""+o).isEmpty())
								TheAPI.msg(replaceAll(""+o, p),p);
					}
					Punishment banlist = TheAPI.getPunishmentAPI().getPunishments(p.getName()).stream().filter(a -> a.getType()==PunishmentType.JAIL).findFirst().orElse(null);
					if(banlist==null)
						banlist = TheAPI.getPunishmentAPI().getPunishmentsIP(TheAPI.getPunishmentAPI().getIp(e.getPlayer().getName())).stream().filter(a -> a.getType()==PunishmentType.MUTE).findFirst().orElse(null);
					Punishment result = banlist;
					new Tasker() {
						public void run() {
							if(result==null)
								API.teleportPlayer(p, TeleportLocation.SPAWN);
							else
								API.teleport(p, Position.fromString(result.getValue("position").toString()));
							Object o = Loader.events.get("onJoin.First.Commands");
							if(o!=null) {
							if(o instanceof Collection) {
								for(String fa : Loader.events.getStringList("onJoin.First.Commands")) {
									if(fa!=null)
									TheAPI.sudoConsole(TheAPI.colorize(replaceAll(fa,p)));
								}}else
									TheAPI.sudoConsole(TheAPI.colorize(replaceAll(""+o, p)));
							}
						}
					}.runTaskSync();
					o = Loader.events.get("onJoin.First.Broadcast");
					if(o!=null) {
						if(o instanceof Collection) {
						for(Object fa : (Collection<?>)o) {
							if(fa!=null)
							TheAPI.bcMsg(replaceAll(fa+"",p));
						}}else
							if(!(""+o).isEmpty())
								TheAPI.bcMsg(replaceAll(""+o, p));
					}
				} else {
					if (!API.hasVanish(p.getName())) {
						Object o = Loader.events.get("onJoin.Text");
						if(o!=null) {
							if(o instanceof Collection) {
							for(Object fa : (Collection<?>)o) {
								if(fa!=null)
								TheAPI.bcMsg(replaceAll(fa+"",p));
							}}else
								if(!(""+o).isEmpty())
									TheAPI.bcMsg(replaceAll(""+o, p));
						}}
					Object o = Loader.events.get("onJoin.Messages");
					if(o!=null) {
						if(o instanceof Collection) {
						for(Object fa : (Collection<?>)o) {
							if(fa!=null)
							TheAPI.msg(replaceAll(fa+"",p),p);
						}}else
							if(!(""+o).isEmpty())
								TheAPI.msg(replaceAll(""+o, p),p);
					}

					new Tasker() {
						public void run() {
						Object o = Loader.events.get("onJoin.Commands");
						if(o!=null) {
							if(o instanceof Collection) {
							for(Object fa : (Collection<?>)o) {
								if(fa!=null)
									TheAPI.sudoConsole(TheAPI.colorize(replaceAll(""+fa, p)));
							}}else
								if(!(""+o).isEmpty())
									TheAPI.sudoConsole(TheAPI.colorize(replaceAll(""+o, p)));
					}}
					}.runTaskSync();
					o = Loader.events.get("onJoin.Broadcast");
					if(o!=null) {
						if(o instanceof Collection) {
						for(Object fa : (Collection<?>)o) {
							if(fa!=null)
							TheAPI.bcMsg(replaceAll(fa+"",p));
						}}else
							if(!(""+o).isEmpty())
								TheAPI.bcMsg(replaceAll(""+o, p));
					}
				}
				if (!EconomyAPI.hasAccount(p)) {
					EconomyAPI.createAccount(p);
					if(Loader.config.getDouble("Options.Economy.Money")!=0)
						EconomyAPI.depositPlayer(p, Loader.config.getDouble("Options.Economy.Money"));
				}
				if(setting.tab) {
					if(setting.tab_footer || setting.tab_header)
						TabList.setFooterHeader(p);
					if(setting.tab_nametag) {
						TabList.setNameTag(p);
						NameTagChanger.updateVisibility(p);
					}
					TabList.update();
					
				}
				d.set("Joins", d.getInt("Joins")+1);
				}catch(Exception | NoSuchFieldError | NoSuchMethodError e) {}
				d.save();
			}}.runTask();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		Player p = e.getPlayer();
		DisplayManager.removeCache(p);
		NameTagChanger.remove(p);
		if(Loader.hasBungee)
			Ref.sendPacket(p,TabList.empty);
		TabListAPI.setTabListName(p, null);
		NameTagChanger.removeVisibility(p);
		p.setDisplayName(null);
		p.setCustomName(null);
		User d = TheAPI.getUser(p);
		boolean fly = p.isFlying() && p.getAllowFlight();
		p.setFlying(false);
		p.setAllowFlight(false);
		try {
			Vanish.task.remove(p.getName());
			if (!API.hasVanish(p)) {
					Object o = Loader.events.get("onQuit.Text");
					if(o!=null) {
						if(o instanceof Collection) {
							for(Object fa : (Collection<?>)o) {
								if(fa!=null)
								TheAPI.bcMsg(replaceAll(fa+"",p));
							}}else
								if(!(""+o).trim().isEmpty())
									TheAPI.bcMsg(replaceAll(""+o, p));
				}}
			Object o = Loader.events.get("onQuit.Messages");
			if(o!=null) {
				if(o instanceof Collection) {
				for(Object fa : (Collection<?>)o) {
					if(fa!=null)
					TheAPI.msg(replaceAll(fa+"",p),p);
				}}else
					if(!(""+o).trim().isEmpty())
						TheAPI.msg(replaceAll(""+o, p),p);
			}
			o = Loader.events.get("onQuit.Commands");
			if(o!=null) {
				if(o instanceof Collection) {
				for(Object fa : (Collection<?>)o) {
					if(fa!=null)
						TheAPI.sudoConsole(TheAPI.colorize(replaceAll(""+o, p)));
				}}else
					if(!(""+o).trim().isEmpty())
						TheAPI.sudoConsole(TheAPI.colorize(replaceAll(""+o, p)));
			}
			o = Loader.events.get("onQuit.Broadcast");
			if(o!=null) {
				if(o instanceof Collection) {
					for(Object fa : (Collection<?>)o) {
						if(fa!=null)
						TheAPI.bcMsg(replaceAll(fa+"",p));
					}}else
						if(!(""+o).trim().isEmpty())
							TheAPI.bcMsg(replaceAll(""+o, p));
			}
			d.set("LastLeave", setting.format_date_time.format(new Date()));
			d.set("LastLeaveTime", System.currentTimeMillis() / 1000);
			d.set("QuitPosition", new Position(p.getLocation()));
			if(fly)
				d.set("FlyOnQuit", true);
			else
				d.remove("FlyOnQuit");
		}catch(Exception | NoSuchFieldError | NoSuchMethodError err) {}
		API.removeSPlayer(p);
	}
}