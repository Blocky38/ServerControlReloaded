package Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;

@SuppressWarnings("deprecation")
public class AFkPlayerEvents implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onMove(PlayerMoveEvent e) { //Moving
		if (Math.abs(e.getFrom().getBlockX() - e.getTo().getBlockX()) > 0
				|| Math.abs(e.getFrom().getBlockZ() - e.getTo().getBlockZ()) > 0
				|| Math.abs(e.getFrom().getBlockY() - e.getTo().getBlockY()) > 0) {
			if (API.getSPlayer(e.getPlayer()).isAFK() && !API.getSPlayer(e.getPlayer()).hasVanish())
				Loader.sendBroadcasts(e.getPlayer(), "AFK.End");
			Loader.getInstance.save(API.getSPlayer(e.getPlayer()));
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerMessage(PlayerChatEvent e) { //Chatting in chat
		if (API.getSPlayer(e.getPlayer()).isAFK() && !API.getSPlayer(e.getPlayer()).hasVanish())
			Loader.sendBroadcasts(e.getPlayer(), "AFK.End");
		Loader.getInstance.save(API.getSPlayer(e.getPlayer()));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerMessage(PlayerCommandPreprocessEvent e) { //Sending commands
		if (!e.getMessage().toLowerCase().startsWith("/afk") && !e.getMessage().toLowerCase().startsWith("/away")) {
			if (API.getSPlayer(e.getPlayer()).isAFK() && !API.getSPlayer(e.getPlayer()).hasVanish())
				Loader.sendBroadcasts(e.getPlayer(), "AFK.End");
			Loader.getInstance.save(API.getSPlayer(e.getPlayer()));
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlaceBlock(BlockPlaceEvent e) { //Placing blocks
		if (API.getSPlayer(e.getPlayer()).isAFK() && !API.getSPlayer(e.getPlayer()).hasVanish())
			Loader.sendBroadcasts(e.getPlayer(), "AFK.End");
		Loader.getInstance.save(API.getSPlayer(e.getPlayer()));
		if (PunishmentAPI.getBanList(e.getPlayer().getName()).isJailed()
				|| PunishmentAPI.getBanList(e.getPlayer().getName()).isTempJailed())
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCaughtFish(PlayerFishEvent e) { //Fishing
		if (API.getSPlayer(e.getPlayer()).isAFK() && !API.getSPlayer(e.getPlayer()).hasVanish())
			Loader.sendBroadcasts(e.getPlayer(), "AFK.End");
		Loader.getInstance.save(API.getSPlayer(e.getPlayer()));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreakBlock(BlockBreakEvent e) { //Breaking blocks
		if (API.getSPlayer(e.getPlayer()).isAFK() && !API.getSPlayer(e.getPlayer()).hasVanish())
			Loader.sendBroadcasts(e.getPlayer(), "AFK.End");
		Loader.getInstance.save(API.getSPlayer(e.getPlayer()));
		if (PunishmentAPI.getBanList(e.getPlayer().getName()).isJailed()
				|| PunishmentAPI.getBanList(e.getPlayer().getName()).isTempJailed())
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClick(InventoryClickEvent e) { //Like invetory games
		if (API.getSPlayer((Player) e.getWhoClicked()).isAFK() && !API.getSPlayer((Player) e.getWhoClicked()).hasVanish())
			Loader.sendBroadcasts((Player)e.getWhoClicked(), "AFK.End");
		Loader.getInstance.save(API.getSPlayer((Player) e.getWhoClicked()));
		if (PunishmentAPI.getBanList(e.getWhoClicked().getName()).isJailed()
				|| PunishmentAPI.getBanList(e.getWhoClicked().getName()).isTempJailed())
			e.setCancelled(true);
	}
}
