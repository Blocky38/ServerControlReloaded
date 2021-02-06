package me.DevTec.ServerControlReloaded.Events;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.Utils.SPlayer;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.reflections.Ref;

public class WorldChange implements Listener {

	Map<String, Integer> sleepTask = new HashMap<>();
	Map<String, List<Player>> perWorldSleep = new HashMap<>();
	Constructor<?> c = Ref.constructor(Ref.nms("PacketPlayOutUpdateTime"), long.class, long.class, boolean.class);
	Method setTime = Ref.method(Ref.nms("WorldServer"), "setDayTime", long.class);
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onSleep(PlayerBedEnterEvent e) {
		if (setting.singeplayersleep && canEvent(e)) {
			World w = e.getBed().getWorld();
			Object f = Ref.world(w);
			boolean time = Boolean.parseBoolean(w.getGameRuleValue("DO_DAYLIGHT_CYCLE"));
			List<Player> s = perWorldSleep.getOrDefault(w.getName(), new ArrayList<>());
			s.add(e.getPlayer());
			perWorldSleep.put(w.getName(), s);
			if(!sleepTask.containsKey(w.getName())) {
				sleepTask.put(w.getName(), new Tasker() {
					long start = w.getTime();
					boolean doNight = false;
					
					public void run() {
						for(Player s : perWorldSleep.get(w.getName())) {
							int old = (int) Ref.get(Ref.player(s), "sleepTicks");
							if(old >= 98)
								Ref.set(Ref.player(s), "sleepTicks", 98);
						}
						
						if(start >= 24000) {
							start=0;
							doNight=true;
							Object data = Ref.get(Ref.world(w),"worldData");
							Ref.set(data, "raining", false);
							Ref.set(data, "thundering", false);
							w.setWeatherDuration(0);
						}
						if(doNight && start >= 500) {
							Object data = Ref.get(Ref.world(w),"worldData");
							Ref.set(data, "raining", false);
							Ref.set(data, "thundering", false);
							w.setWeatherDuration(0);
							cancel();
						}
						Ref.invoke(f,setTime, (long)Ref.invoke(f,"getDayTime")+ (start - (long)Ref.invoke(f,"getDayTime")));
						for (Player p : w.getPlayers())
							Ref.sendPacket(p, Ref.newInstance(c, w.getTime(), p.getPlayerTime(), time));
						start+=50;
					}
				}.runRepeating(0, 1));
			}
		}
	}
	
	private boolean canEvent(PlayerBedEnterEvent e) {
		try {
			return e.getBedEnterResult()==BedEnterResult.OK;
		}catch(Exception | NoSuchFieldError | NoSuchMethodError er) {
			return true;
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onSleep(PlayerBedLeaveEvent e) {
		if (setting.singeplayersleep) { //remove cache and stop task
			if(perWorldSleep.containsKey(e.getBed().getWorld().getName())) {
			perWorldSleep.get(e.getBed().getWorld().getName()).remove(e.getPlayer());
			if(perWorldSleep.get(e.getBed().getWorld().getName()).isEmpty()) {
				perWorldSleep.remove(e.getBed().getWorld().getName());
			}
			if(sleepTask.containsKey(e.getBed().getWorld().getName())) {
				Scheduler.cancelTask(sleepTask.get(e.getBed().getWorld().getName()));
				sleepTask.remove(e.getBed().getWorld().getName());
			}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void OnPlayerWorldChangeEvent(PlayerChangedWorldEvent e) {
		SPlayer a = API.getSPlayer(e.getPlayer());
		a.createEconomyAccount();
		if (a.hasFlyEnabled())
			a.enableFly();
		if (a.hasTempFlyEnabled())
			a.enableTempFly();
		if (a.hasGodEnabled())
			a.enableGod();
		a.setGamamode();

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChangeGamamode(PlayerGameModeChangeEvent e) {
		SPlayer a = API.getSPlayer(e.getPlayer());
		if (a.hasFlyEnabled())
			a.enableFly();
		if (a.hasTempFlyEnabled())
			a.enableTempFly();
		if (a.hasGodEnabled())
			a.enableGod();
	}
}