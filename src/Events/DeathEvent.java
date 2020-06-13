package Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import ServerControl.API;
import ServerControl.API.TeleportLocation;
import ServerControl.Loader;
import ServerControl.SPlayer;
import Utils.TabList;
import Utils.setting;
import Utils.setting.DeathTp;
import me.DevTec.TheAPI;
import me.DevTec.Other.User;

public class DeathEvent implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerDeath(PlayerAdvancementDoneEvent e) {
		TabList.setNameTag(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerDeath(PlayerDeathEvent e) {
		if (e.getEntity().getKiller() instanceof Player) {
			User s = TheAPI.getUser(e.getEntity().getKiller());
			s.setAndSave("Kills", s.getInt("Kills") + 1);
		}
		Player p = e.getEntity();
		API.setBack(p);
		if (p.hasPermission("ServerControl.KeepInv")) {
			e.setKeepInventory(true);
			e.getDrops().clear();
		}
		if (p.hasPermission("ServerControl.KeepExp")) {
			e.setKeepLevel(true);
			e.setDroppedExp(0);
		}
		User s = TheAPI.getUser(p);
		s.setAndSave("Deaths", s.getInt("Deaths") + 1);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void Respawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		try {
		if (TheAPI.getPunishmentAPI().getBanList(p.getName()).isJailed()
				|| TheAPI.getPunishmentAPI().getBanList(p.getName()).isTempJailed())
			e.setRespawnLocation((Location) Loader.config.get("Jails." + TheAPI.getUser(p).getString("Jail.Location")));
		else if (setting.deathspawnbol) {
			if (setting.deathspawn == DeathTp.HOME)
				e.setRespawnLocation(API.getTeleportLocation(p, TeleportLocation.HOME));
			else if (setting.deathspawn == DeathTp.BED)
				e.setRespawnLocation(API.getTeleportLocation(p, TeleportLocation.BED));
			else if (setting.deathspawn == DeathTp.SPAWN) {
				e.setRespawnLocation(API.getTeleportLocation(p, TeleportLocation.SPAWN));
				TheAPI.msg(Loader.s("Spawn.TeleportedToSpawn").replace("%world%", p.getWorld().getName())
						.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()), p);
			}
		}}catch(Exception eere) {}
		SPlayer a = new SPlayer(p);
		if (a.hasPermission("servercontrol.fly") && a.hasFlyEnabled())
			a.enableFly();
		if (a.hasPermission("servercontrol.god") && a.hasGodEnabled())
			a.enableGod();
	}
}
