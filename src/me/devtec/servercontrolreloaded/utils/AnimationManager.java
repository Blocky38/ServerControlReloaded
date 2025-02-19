package me.devtec.servercontrolreloaded.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.utils.Animation;
import me.devtec.theapi.utils.StringUtils;

public class AnimationManager {
	final Map<String, Animation> a = new HashMap<>();
	
	public void reload() {
		a.clear();
		for(String anim : Loader.anim.getKeys()) {
			Animation s = new Animation(Loader.anim.getStringList(anim+".lines")
					, (long) StringUtils.calculate(Loader.anim.getString(anim+".speed")));
			a.put(anim, s);
		}
	}
	
	public String requestAnimation(String name) {
		Animation get = a.get(name);
		if(get==null)return null;
		return get.get();
	}
	
	public String replace(Player player, String where) {
		if(where==null)return null;
		for(Entry<String, Animation> e : a.entrySet()) {
			where=where.replace("%animation-"+e.getKey()+"%", e.getValue().get());
		}
		return PlaceholderAPI.setPlaceholders(player, TabList.replace(where, player, true));
	}

	public String replaceWithoutColors(Player player, String string) {
		if(string==null)return null;
		for(Entry<String, Animation> e : a.entrySet()) {
			string=string.replace("%animation-"+e.getKey()+"%", e.getValue().get());
		}
		return PlaceholderAPI.setPlaceholders(player, TabList.replace(string, player, false));
	}
	
	public void update() {
		for(Animation e : a.values())
			e.next();
	}
}
