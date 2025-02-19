package me.devtec.servercontrolreloaded.commands.other;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.nms.nbt.NBTEdit;
import me.devtec.theapi.utils.theapiutils.LoaderClass;

public class Repair implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Repair", "Other")) {
			if(!CommandsManager.canUse("Other.Repair", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.Repair", s))));
				return true;
			}
		if(args.length<2) {
		if (s instanceof Player) {
				Player p = (Player) s;
				if (args.length == 0) {
					Material hand = p.getItemInHand().getType();
					if (hand != Material.AIR) {
						if(isTool(p.getItemInHand().getType().name())) {
							NBTEdit edit = new NBTEdit(p.getItemInHand());
							edit.remove("Damage");
							edit.remove("damage");
							p.setItemInHand(LoaderClass.nmsProvider.setNBT(p.getItemInHand(), edit));
						}
					Loader.sendMessages(s, "Repair.Hand.You");
					return true;
					}
					Loader.sendMessages(s, "Missing.HandEmpty");
					return true;
				}
				if (args[0].equalsIgnoreCase("all")) {
					ItemStack[] items = p.getInventory().getContents();
					int pos = 0;
					for (ItemStack t : items) {
						if (t != null && t.getType()!=Material.AIR)
							if(isTool(t.getType().name())) {
								NBTEdit edit = new NBTEdit(t);
								edit.remove("Damage");
								edit.remove("damage");
								t=LoaderClass.nmsProvider.setNBT(t, edit);
							}
						items[pos++]=t;
					}
					p.getInventory().setContents(items);
					Loader.sendMessages(s, "Repair.All.You");
					return true;
				}
				Material hand = p.getItemInHand().getType();
				if (hand != Material.AIR) {
					if(isTool(p.getItemInHand().getType().name())) {
						NBTEdit edit = new NBTEdit(p.getItemInHand());
						edit.remove("Damage");
						edit.remove("damage");
						p.setItemInHand(LoaderClass.nmsProvider.setNBT(p.getItemInHand(), edit));
					}
				Loader.sendMessages(s, "Repair.Hand.You");
				return true;
				}
				Loader.sendMessages(s, "Missing.HandEmpty");
				return true;
			}
		Loader.Help(s, "Repair", "Other");
		return true;
		}
		if (Loader.has(s, "Repair", "Other", "Other")) {
			Player p = TheAPI.getPlayer(args[2]);
			if(p==null) {
				Loader.notOnline(s, args[2]);
				return true;
			}
			if (args[0].equalsIgnoreCase("all")) {
				int pos = 0;
				ItemStack[] items = p.getInventory().getContents();
				for (ItemStack t : items) {
					if (t != null && t.getType()!=Material.AIR)
						if(isTool(t.getType().name())) {
							NBTEdit edit = new NBTEdit(t);
							edit.remove("Damage");
							edit.remove("damage");
							t=LoaderClass.nmsProvider.setNBT(t, edit);
						}
					items[pos++]=t;
				}
				Loader.sendMessages(s, "Repair.All.Other.Sender");
				Loader.sendMessages(p, "Repair.All.Other.Receiver", Placeholder.c().replace("%player%", s.getName())
						.replace("%playername%", s.getName()));
				return true;
			}
			Material hand = p.getItemInHand().getType();
			if (hand != Material.AIR) {
				if(isTool(p.getItemInHand().getType().name())) {
					NBTEdit edit = new NBTEdit(p.getItemInHand());
					edit.remove("Damage");
					edit.remove("damage");
					p.setItemInHand(LoaderClass.nmsProvider.setNBT(p.getItemInHand(), edit));
				}
				Loader.sendMessages(s, "Repair.Hand.Other.Sender");
				Loader.sendMessages(p, "Repair.Hand.Other.Receiver", Placeholder.c().replace("%player%", s.getName())
						.replace("%playername%", s.getName()));
			return true;
			}
			Loader.sendMessages(s, "Missing.TargetHandEmpty", Placeholder.c().replace("%player%", p.getName())
					.replace("%playername%", p.getDisplayName()));
			return true;
		}
		Loader.noPerms(s, "Repair", "Other", "Other");
		return true;
		}
		Loader.noPerms(s, "Repair", "Other");
		return true;
	}

	private boolean isTool(String name) {
		return name.endsWith("_PICKAXE")||name.endsWith("_AXE")||name.endsWith("_SPADE")||name.endsWith("_SHOVEL")
				||name.endsWith("_HOE")||name.endsWith("_HELMET")||name.endsWith("_BOOTS")||name.endsWith("_ON_A_STICK")||
		name.endsWith("_LEGGINGS")||name.endsWith("_CHESTPLATE")||name.endsWith("_SWORD")||name.equals("BOW")
		||name.equals("SHEARS")||name.equals("FLINT_AND_STEEL")||name.equals("TRIDENT")||name.equals("ELYTRA")
		||name.equals("CROSSBOW")||name.equals("SHIELD")||name.equals("FISHING_ROD");
	}

	final List<String> sd = Arrays.asList("Hand", "All");
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.has(s,"Repair","Other")) {
		if (args.length == 1)
			return StringUtils.copyPartialMatches(args[0], sd);
		if(args.length==2)
			return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
		}
		return Collections.emptyList();
	}

}
