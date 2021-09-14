package me.devtec.servercontrolreloaded.commands.tpsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;

public class Tp implements CommandExecutor, TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if (Loader.has(s, "Tp","TpSystem")) {
		if(args.length==1) {
			List<String> c = new ArrayList<>();
			if(Loader.has(s, "Tp","TpSystem","Location")) {
			if(!(s instanceof ConsoleCommandSender)) {
				Location d = s instanceof BlockCommandSender ? ((BlockCommandSender)s).getBlock().getLocation() : ((Player)s).getLocation();
				c.add(StringUtils.fixedFormatDouble(d.getX())+" "+StringUtils.fixedFormatDouble(d.getY())+" "+StringUtils.fixedFormatDouble(d.getZ()));
				c.add(StringUtils.fixedFormatDouble(d.getX())+" "+StringUtils.fixedFormatDouble(d.getY()));
				c.add(StringUtils.fixedFormatDouble(d.getX()));
				c.add("~ ~ ~");
				c.add("~ ~");
				c.add("~");
			}else {
				c.add("X Y Z");
				c.add("X Y");
				c.add("X");
			}}
			if(Loader.has(s, "Tp","TpSystem"))
			c.addAll(API.getPlayerNames(s));
			return StringUtils.copyPartialMatches(args[0], c);
		}
		if(args.length==2) {
			List<String> c = new ArrayList<>();
			if(!(s instanceof ConsoleCommandSender)) {
				Location d = s instanceof BlockCommandSender ? ((BlockCommandSender)s).getBlock().getLocation() : ((Player)s).getLocation();
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					if(Loader.has(s, "Tp","TpSystem","Location")||Loader.has(s, "Tp","TpSystem","LocationOther")) {
					c.add(StringUtils.fixedFormatDouble(d.getX())+" "+StringUtils.fixedFormatDouble(d.getY())+" "+StringUtils.fixedFormatDouble(d.getZ()));
					c.add(StringUtils.fixedFormatDouble(d.getX())+" "+StringUtils.fixedFormatDouble(d.getY()));
					c.add(StringUtils.fixedFormatDouble(d.getX()));
					c.add("~ ~ ~");
					c.add("~ ~");
					c.add("~");
					}
					if(Loader.has(s, "Tp","TpSystem","Other"))
					c.addAll(API.getPlayerNames(s));
				}else {
					if(Loader.has(s, "Tp","TpSystem","Location")||Loader.has(s, "Tp","TpSystem","LocationOther")) {
					c.add(StringUtils.fixedFormatDouble(d.getY())+" "+StringUtils.fixedFormatDouble(d.getZ()));
					c.add(StringUtils.fixedFormatDouble(d.getY()));
					c.add("~ ~");
					c.add("~");
					}
				}
			}else {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.addAll(API.getPlayerNames(s));
					c.add("X Y Z");
					c.add("X Y");
					c.add("X");
				}else {
					c.add("Y Z");
					c.add("Y");
				}
			}
			return StringUtils.copyPartialMatches(args[1], c);
		}
		if(args.length==3 && (Loader.has(s, "Tp","TpSystem","Location")||Loader.has(s, "Tp","TpSystem","LocationOther"))) {
			List<String> c = new ArrayList<>();
			if(!(s instanceof ConsoleCommandSender)) {
				Location d = s instanceof BlockCommandSender ? ((BlockCommandSender)s).getBlock().getLocation() : ((Player)s).getLocation();
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.add(StringUtils.fixedFormatDouble(d.getY())+" "+StringUtils.fixedFormatDouble(d.getZ()));
					c.add(StringUtils.fixedFormatDouble(d.getY()));
					c.add("~ ~");
					c.add("~");
				}else {
					c.add(StringUtils.fixedFormatDouble(d.getZ()));
					c.add("~");
				}
			}else {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.add("Y Z");
					c.add("Y");
				}else {
					c.add("Z");
				}
			}
			return StringUtils.copyPartialMatches(args[2], c);
		}
		if(args.length==4 && (Loader.has(s, "Tp","TpSystem","Location")||Loader.has(s, "Tp","TpSystem","LocationOther"))) {
			List<String> c = new ArrayList<>();
			if(!(s instanceof ConsoleCommandSender)) {
				Location d = s instanceof BlockCommandSender ? ((BlockCommandSender)s).getBlock().getLocation() : ((Player)s).getLocation();
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.add(StringUtils.fixedFormatDouble(d.getZ()));
					c.add("~");
				}else {
					c.add(StringUtils.fixedFormatDouble(d.getYaw())+" "+StringUtils.fixedFormatDouble(d.getPitch()));
					c.add(StringUtils.fixedFormatDouble(d.getYaw()));
					c.add("~ ~");
					c.add("~");
				}
			}else {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.add("Z");
				}else {
					c.add("YAW PITCH");
					c.add("YAW");
				}
			}
			return StringUtils.copyPartialMatches(args[3], c);
		}
		if(args.length==5 && (Loader.has(s, "Tp","TpSystem","Location")||Loader.has(s, "Tp","TpSystem","LocationOther"))) {
			List<String> c = new ArrayList<>();
			if(!(s instanceof ConsoleCommandSender)) {
				Location d = s instanceof BlockCommandSender ? ((BlockCommandSender)s).getBlock().getLocation() : ((Player)s).getLocation();
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.add(StringUtils.fixedFormatDouble(d.getYaw())+" "+StringUtils.fixedFormatDouble(d.getPitch()));
					c.add(StringUtils.fixedFormatDouble(d.getYaw()));
					c.add("~ ~");
					c.add("~");
				}else {
					c.add(StringUtils.fixedFormatDouble(d.getPitch()));
					c.add("~");
				}
			}else {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.add("YAW PITCH");
					c.add("YAW");
				}else {
					c.add("PITCH");
				}
			}
			return StringUtils.copyPartialMatches(args[4], c);
		}
		if(args.length==6 && Loader.has(s, "Tp","TpSystem","LocationOther")) {
			List<String> c = new ArrayList<>();
			if(!(s instanceof ConsoleCommandSender)) {
				Location d = s instanceof BlockCommandSender ? ((BlockCommandSender)s).getBlock().getLocation() : ((Player)s).getLocation();
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.add(StringUtils.fixedFormatDouble(d.getPitch()));
					c.add("~");
				}
			}else {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.add("PITCH");
				}
			}
			return StringUtils.copyPartialMatches(args[5], c);
		}
		}
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Tp","TpSystem")) {
			if(!CommandsManager.canUse("TpSystem.Tp", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("TpSystem.Tp", s))));
				return true;
			}
			if (args.length == 0) {
				Loader.Help(s, "Tp", "TpSystem");
				return true;
			}
			if (args.length == 1) {
				if (s instanceof Player) {
					Player target = TheAPI.getPlayer(args[0]);
					if (target == null || !API.getPlayers(s).contains(target)) {
						if(!API.getPlayers(s).contains(target) && target!=null) {
							Loader.Help(s, "Tp", "TpSystem");
							return true;
						}
						if(Loader.has(s, "Tp","TpSystem", "ToOfflinePlayer")) {
							User u = TheAPI.getUser(args[0]);
							if(u==null || !u.exist("LastLeavePosition")) {
								Loader.Help(s, "Tp", "TpSystem");
								return true;
							}
							if(Loader.has(s, "TpToggle", "TpSystem", "Bypass") || !Loader.has(s, "TpToggle", "TpSystem", "Bypass") && !RequestMap.isBlocking(s.getName(), args[0])) {
								Location loc = StringUtils.getLocationFromString(u.getString("LastLeavePosition"));
								API.setBack(((Player) s));
								if (setting.tp_safe)
									API.safeTeleport((Player) s,((Player) s).isFlying(),new Position(loc));
								else
									API.teleport((Player)s, loc);
								Loader.sendMessages(s, "TpSystem.Tp.Player.YouToPlayer", Placeholder.c().replace("%player%", u.getName() ));
								return true;
							}		
							Loader.sendMessages(s, "TpSystem.Block.IsBlocked.Teleport", Placeholder.c().replace("%player%", target.getName()).replace("%playername%", target.getDisplayName()));
							return true;
						}
						Loader.Help(s, "Tp", "TpSystem");
						return true;
					} else {
						if (Loader.has(s, "TpToggle", "TpSystem", "Bypass") || !Loader.has(s, "TpToggle", "TpSystem", "Bypass") && !RequestMap.isBlocking(s.getName(), target.getName())) {
							Loader.sendMessages(s, "TpSystem.Tp.Player.YouToPlayer", Placeholder.c().replace("%player%", target.getName()));
							API.setBack(((Player) s));
							if (setting.tp_safe)
								API.safeTeleport((Player) s,((Player) s).isFlying(),new Position(target.getLocation()));
							else
								API.teleport((Player)s, target.getLocation());
							return true;
						}
						Loader.sendMessages(s, "TpSystem.Block.IsBlocked.Teleport", Placeholder.c().replace("%player%", target.getName()).replace("%playername%", target.getDisplayName()));
						return true;
					}
				}
				Loader.Help(s, "Tp", "TpSystem");
				return true;
			}
			if (args.length == 2) {
				if (Loader.has(s, "Tp","TpSystem","Other")) {
					Player p0 = TheAPI.getPlayer(args[0]);
					if(p0==null || !API.getPlayers(s).contains(p0)) {
						Loader.notOnline(s,args[0]);
						return true;
					}
					Player p1 = TheAPI.getPlayer(args[1]);
					if (p1 == null || !API.getPlayers(s).contains(p1)) {
						if (s instanceof Player) {
							Loader.Help(s, "Tp", "TpSystem");
							return true;
						}
						Loader.Help(s, "Tp", "TpSystem");
						return true;
					} else {
						String player = p0.getName();
						String playername = p0.getDisplayName();
						String player1 = p1.getName();
						String playername1 = p1.getDisplayName();
						Loader.sendMessages(s, "TpSystem.Tp.Player.PlayerToNextPlayer", Placeholder.c()
								.replace("%player%", player)
								.replace("%playername%", playername)
								.replace("%next-player%", player1)
								.replace("%next-playername%", playername1));
						API.setBack(p0);
						if (setting.tp_safe)
							API.safeTeleport(p0,p0.isFlying(),new Position(p1.getLocation()));
						else
							API.teleport(p0, p1.getLocation());
						return true;
					}
				}
				Loader.noPerms(s, "Tp", "TpSystem","Other");
				return true;
			}
			if (args.length == 3) {
				if (Loader.has(s, "Tp","TpSystem","Location")) {
					if (s instanceof Player) {
						Player p=(Player)s;
						double x=StringUtils.calculate(args[0].replace("~", p.getLocation().getX()+""))
						, y=StringUtils.calculate(args[1].replace("~", p.getLocation().getY()+"")),
						z=StringUtils.calculate(args[2].replace("~", p.getLocation().getZ()+""));
					Loader.sendMessages(p, "TpSystem.Tp.Location.YouToLocation", Placeholder.c()
							.replace("%x%", StringUtils.fixedFormatDouble(x))
							.replace("%y%", StringUtils.fixedFormatDouble(y))
							.replace("%z%", StringUtils.fixedFormatDouble(z))
							.replace("%yaw%", "0").replace("%pitch%", "0"));
					API.setBack(p);
					if (setting.tp_safe)
						API.safeTeleport(p,p.isFlying(),new Position(p.getWorld(), x, y, z, p.getLocation().getYaw(),p.getLocation().getPitch()));
					else
						API.teleport(p, new Location(p.getWorld(), x, y, z, p.getLocation().getYaw(),p.getLocation().getPitch()));
					return true;
					}
					Loader.Help(s, "Tp", "TpSystem");
					return true;
				}
				Loader.noPerms(s, "Tp", "TpSystem","Location");
				return true;
			}
			if (args.length == 4) {
			/*	if(Loader.has(s, "Tp", "TpSystem","Location") && s instanceof Player) {
					Player p = (Player)s;
					double x=StringUtils.calculate(args[0].replace("~", p.getLocation().getX()+""))
					, y=StringUtils.calculate(args[1].replace("~", p.getLocation().getY()+"")), 
					z=StringUtils.calculate(args[2].replace("~", p.getLocation().getZ()+""));
					float yaw = (float) StringUtils.calculate(args[3].replace("~", p.getLocation().getYaw()+""));
					Loader.sendMessages(s, "TpSystem.Tp.Location.YouToLocation", Placeholder.c().add("%player%", s.getName())
							.replace("%x%", StringUtils.fixedFormatDouble(x))
							.replace("%y%", StringUtils.fixedFormatDouble(y))
							.replace("%z%", StringUtils.fixedFormatDouble(z))
							.replace("%yaw%", StringUtils.fixedFormatDouble(yaw))
							.replace("%pitch%", "0"));
					
					API.setBack((Player) s);
					if(setting.tp_safe)						
						API.safeTeleport((Player)s,((Player)s).isFlying(), new Position(p.getWorld(), x, y, z, yaw, ((Player)s).getLocation().getPitch())); 
					else
						API.teleport(p, new Location(p.getWorld(), x, y, z, yaw, 0));
					return true;
				}
				if (Loader.has(s, "Tp","TpSystem","LocationOther")) {
					Player p = TheAPI.getPlayer(args[0]);
					if (p != null && API.getPlayers(s).contains(p)) {
						double x=StringUtils.calculate(args[1].replace("~", p.getLocation().getX()+""))
								, y=StringUtils.calculate(args[2].replace("~", p.getLocation().getY()+"")), 
								z=StringUtils.calculate(args[3].replace("~", p.getLocation().getZ()+""));
							Loader.sendMessages(s, "TpSystem.Tp.Location.PlayerToLocation", Placeholder.c().add("%player%", p.getName()).replace("%playername%", p.getDisplayName())
									.replace("%x%", StringUtils.fixedFormatDouble(x))
									.replace("%y%", StringUtils.fixedFormatDouble(y))
									.replace("%z%", StringUtils.fixedFormatDouble(z))
									.add("%yaw%", "0")
									.add("%pitch%", "0"));
							Loader.sendMessages(p, "TpSystem.Tp.Location.YouToLocation", Placeholder.c()
									.replace("%x%", StringUtils.fixedFormatDouble(x))
									.replace("%y%", StringUtils.fixedFormatDouble(y))
									.replace("%z%", StringUtils.fixedFormatDouble(z))
									.add("%yaw%", "0")
									.add("%pitch%", "0"));
							API.setBack(p);
							if (setting.tp_safe)
								API.safeTeleport(p,p.isFlying(),new Position(p.getWorld(), x, y, z, p.getLocation().getYaw(),p.getLocation().getPitch()));
							else
							API.teleport(p, new Location(p.getWorld(), x, y, z, 0, 0));
							return true;
					}
					Loader.Help(s, "Tp", "TpSystem");
					return true;
				}				
				Loader.noPerms(s, "Tp", "TpSystem","LocationOther");
				return true;				
			*/

				Player target = TheAPI.getPlayer(args[0]);
				if (target != null && API.getPlayers(s).contains(target)) {
					if (Loader.has(s, "Tp","TpSystem","LocationOther")) {
						Player pp = TheAPI.getPlayer(args[0]);
						if (pp != null && API.getPlayers(s).contains(target)) {
							double x=StringUtils.calculate(args[1].replace("~", target.getLocation().getX()+""))
									, y=StringUtils.calculate(args[2].replace("~", target.getLocation().getY()+"")), 
									z=StringUtils.calculate(args[3].replace("~", target.getLocation().getZ()+""));
								Loader.sendMessages(s, "TpSystem.Tp.Location.PlayerToLocation", Placeholder.c().add("%player%", target.getName()).replace("%playername%", target.getDisplayName())
										.replace("%x%", StringUtils.fixedFormatDouble(x))
										.replace("%y%", StringUtils.fixedFormatDouble(y))
										.replace("%z%", StringUtils.fixedFormatDouble(z))
										.add("%yaw%", "0")
										.add("%pitch%", "0"));
								Loader.sendMessages(target, "TpSystem.Tp.Location.YouToLocation", Placeholder.c()
										.replace("%x%", StringUtils.fixedFormatDouble(x))
										.replace("%y%", StringUtils.fixedFormatDouble(y))
										.replace("%z%", StringUtils.fixedFormatDouble(z))
										.add("%yaw%", "0")
										.add("%pitch%", "0"));
								API.setBack(target);
								if (setting.tp_safe)
									API.safeTeleport(target,target.isFlying(),new Position(target.getWorld(), x, y, z, target.getLocation().getYaw(),target.getLocation().getPitch()));
								else
								API.teleport(target, new Location(target.getWorld(), x, y, z, 0, 0));
								return true;
						}
						Loader.Help(s, "Tp", "TpSystem");
						return true;
					}				
					Loader.noPerms(s, "Tp", "TpSystem","LocationOther");
					return true;
				}
				if(Loader.has(s, "Tp", "TpSystem","Location") && s instanceof Player) {
					Player p = (Player)s;
					double x=StringUtils.calculate(args[0].replace("~", p.getLocation().getX()+""))
					, y=StringUtils.calculate(args[1].replace("~", p.getLocation().getY()+"")), 
					z=StringUtils.calculate(args[2].replace("~", p.getLocation().getZ()+""));
					float yaw = (float) StringUtils.calculate(args[3].replace("~", p.getLocation().getYaw()+""));
					Loader.sendMessages(s, "TpSystem.Tp.Location.YouToLocation", Placeholder.c().add("%player%", s.getName())
							.replace("%x%", StringUtils.fixedFormatDouble(x))
							.replace("%y%", StringUtils.fixedFormatDouble(y))
							.replace("%z%", StringUtils.fixedFormatDouble(z))
							.replace("%yaw%", StringUtils.fixedFormatDouble(yaw))
							.replace("%pitch%", "0"));
					
					API.setBack((Player) s);
					if(setting.tp_safe)						
						API.safeTeleport((Player)s,((Player)s).isFlying(), new Position(p.getWorld(), x, y, z, yaw, ((Player)s).getLocation().getPitch())); 
					else
						API.teleport(p, new Location(p.getWorld(), x, y, z, yaw, 0));
					return true;
				}
				Loader.noPerms(s, "Tp", "TpSystem","Location");
				return true;
			}
			if (args.length == 5) {
				if(Loader.has(s, "Tp", "TpSystem","Location") && s instanceof Player) {
					Player p = (Player)s;
					double x=StringUtils.calculate(args[0].replace("~", p.getLocation().getX()+""))
					, y=StringUtils.calculate(args[1].replace("~", p.getLocation().getZ()+"")), 
					z=StringUtils.calculate(args[2].replace("~", p.getLocation().getY()+""));
					float yaw = (float) StringUtils.calculate(args[3].replace("~", p.getLocation().getYaw()+"")),
							pitch = (float) StringUtils.calculate(args[4].replace("~", p.getLocation().getPitch()+""));
					Loader.sendMessages(s, "TpSystem.Tp.Location.YouToLocation", Placeholder.c().add("%player%", s.getName())
							.replace("%x%", StringUtils.fixedFormatDouble(x))
							.replace("%y%", StringUtils.fixedFormatDouble(y))
							.replace("%z%", StringUtils.fixedFormatDouble(z))
							.replace("%yaw%", StringUtils.fixedFormatDouble(yaw))
							.replace("%pitch%", StringUtils.fixedFormatDouble(pitch)));
					
					API.setBack((Player) s);
					if(setting.tp_safe)						
						API.safeTeleport((Player)s,((Player)s).isFlying(), new Position(p.getWorld(), x, y, z, yaw, pitch)); 
					else
						API.teleport(p, new Location(p.getWorld(), x, y, z, yaw, pitch));
					return true;
				}
				if (Loader.has(s, "Tp","TpSystem","LocationOther")) {
					Player p = TheAPI.getPlayer(args[0]);
					if (p != null && API.getPlayers(s).contains(p)) {
						double x=StringUtils.calculate(args[1].replace("~", p.getLocation().getX()+""))
							, y=StringUtils.calculate(args[2].replace("~", p.getLocation().getY()+"")), 
							z=StringUtils.calculate(args[3].replace("~", p.getLocation().getZ()+""));
							float yaw = (float) StringUtils.calculate(args[4].replace("~", p.getLocation().getYaw()+""));
							Loader.sendMessages(s, "TpSystem.Tp.Location.PlayerToLocation", Placeholder.c().add("%player%", p.getName()).replace("%playername%", p.getDisplayName())
									.replace("%x%", StringUtils.fixedFormatDouble(x))
									.replace("%y%", StringUtils.fixedFormatDouble(y))
									.replace("%z%", StringUtils.fixedFormatDouble(z))
									.replace("%yaw%", StringUtils.fixedFormatDouble(yaw))
									.replace("%pitch%", "0"));
							Loader.sendMessages(p, "TpSystem.Tp.Location.YouToLocation", Placeholder.c()
									.replace("%x%", StringUtils.fixedFormatDouble(x))
									.replace("%y%", StringUtils.fixedFormatDouble(y))
									.replace("%z%", StringUtils.fixedFormatDouble(z))
									.replace("%yaw%", StringUtils.fixedFormatDouble(yaw))
									.replace("%pitch%", "0"));
							API.setBack(p);
							if (setting.tp_safe)
								API.safeTeleport(p,p.isFlying(),new Position(p.getWorld(), x, y, z, yaw, p.getLocation().getPitch()));
							else
								API.teleport(p, new Location(p.getWorld(), x, y, z, yaw, 0));
							return true;
					}
					Loader.Help(s, "Tp", "TpSystem");
					return true;
				}				
				Loader.noPerms(s, "Tp", "TpSystem","LocationOther");
				return true;				
			}
			
			if (Loader.has(s, "Tp","TpSystem","LocationOther")) {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					double x=StringUtils.calculate(args[1].replace("~", p.getLocation().getX()+""))
					, y=StringUtils.calculate(args[2].replace("~", p.getLocation().getY()+"")), 
					z=StringUtils.calculate(args[3].replace("~", p.getLocation().getZ()+""));
					float yaw = (float) StringUtils.calculate(args[4].replace("~", p.getLocation().getYaw()+"")),
							pitch = (float) StringUtils.calculate(args[5].replace("~", p.getLocation().getPitch()+""));
					Loader.sendMessages(s, "TpSystem.Tp.Location.PlayerToLocation", Placeholder.c().add("%player%", p.getName()).replace("%playername%", p.getDisplayName())
							.replace("%x%", StringUtils.fixedFormatDouble(x))
							.replace("%y%", StringUtils.fixedFormatDouble(y))
							.replace("%z%", StringUtils.fixedFormatDouble(z))
							.replace("%yaw%", StringUtils.fixedFormatDouble(yaw))
							.replace("%pitch%", StringUtils.fixedFormatDouble(pitch)));
					Loader.sendMessages(p, "TpSystem.Tp.Location.YouToLocation", Placeholder.c()
							.replace("%x%", StringUtils.fixedFormatDouble(x))
							.replace("%y%", StringUtils.fixedFormatDouble(y))
							.replace("%z%", StringUtils.fixedFormatDouble(z))
							.replace("%yaw%", StringUtils.fixedFormatDouble(yaw))
							.replace("%pitch%", StringUtils.fixedFormatDouble(pitch)));
					API.setBack(p);
					if (setting.tp_safe)
						API.safeTeleport(p,p.isFlying(),new Position(p.getWorld(), x, y, z, yaw, pitch));
					else 
						API.teleport(p, new Location(p.getWorld(), x, y, z, yaw, pitch));
					return true;
				}
				Loader.Help(s, "Tp", "TpSystem");
				return true;
			}
			Loader.noPerms(s, "Tp", "TpSystem","LocationOther");
			return true;
		}
		Loader.noPerms(s, "Tp", "TpSystem");
		return true;
	}
}
