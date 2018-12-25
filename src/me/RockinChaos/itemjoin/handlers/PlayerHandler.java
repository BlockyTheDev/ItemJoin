package me.RockinChaos.itemjoin.handlers;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.nickedplayer.NickedPlayer;
import me.RockinChaos.itemjoin.ItemJoin;
import me.RockinChaos.itemjoin.utils.Hooks;
import me.RockinChaos.itemjoin.utils.Legacy;
import me.RockinChaos.itemjoin.utils.Utils;
import net.milkbowl.vault.economy.EconomyResponse;

public class PlayerHandler {
	
	private static final int PLAYER_CRAFT_INV_SIZE = 5;
	
    public static boolean isCraftingInv(InventoryView view) {
        return view.getTopInventory().getSize() == PLAYER_CRAFT_INV_SIZE;
    }
	
	public static boolean isCreativeMode(Player player) {
		final GameMode gamemode = player.getGameMode();
		final GameMode creative = GameMode.CREATIVE;
		if (gamemode == creative) {
			return true;
		}
		return false;
	}
	
	public static boolean isAdventureMode(Player player) {
		final GameMode gamemode = player.getGameMode();
		final GameMode adventure = GameMode.ADVENTURE;
		if (gamemode == adventure) {
			return true;
		}
		return false;
	}
	
	public static void setItemInHand(Player player, Material mat) {
		Legacy.setLegacyInHandItem(player, new ItemStack(mat));
	}
	
	public static void setHeldItemSlot(Player player) {
		if (ConfigHandler.getConfig("config.yml").getString("HeldItem-Slot") != null 
				&& Utils.isInt(ConfigHandler.getConfig("config.yml").getString("HeldItem-Slot")) 
				&& ConfigHandler.getConfig("config.yml").getInt("HeldItem-Slot") <= 8 && ConfigHandler.getConfig("config.yml").getInt("HeldItem-Slot") >= 0) {
			player.getInventory().setHeldItemSlot(ConfigHandler.getConfig("config.yml").getInt("HeldItem-Slot"));
		}
	}
	
	public static ItemStack getHandItem(Player player) {
		if (ServerHandler.hasCombatUpdate() && player.getInventory().getItemInMainHand().getType() != null && player.getInventory().getItemInMainHand().getType() != Material.AIR) {
			return player.getInventory().getItemInMainHand();
		} else if (ServerHandler.hasCombatUpdate() && player.getInventory().getItemInOffHand().getType() != null && player.getInventory().getItemInOffHand().getType() != Material.AIR) {
			return player.getInventory().getItemInOffHand();
		} else if (!ServerHandler.hasCombatUpdate()) {
			return Legacy.getLegacyInHandItem(player);
		}
		return null;
	}
	
	public static ItemStack getPerfectHandItem(Player player, String type) {
		if (ServerHandler.hasCombatUpdate() && type != null && type.equalsIgnoreCase("HAND")) {
			return player.getInventory().getItemInMainHand();
		} else if (ServerHandler.hasCombatUpdate() && type != null && type.equalsIgnoreCase("OFF_HAND")) {
			return player.getInventory().getItemInOffHand();
		} else if (!ServerHandler.hasCombatUpdate()) {
			return Legacy.getLegacyInHandItem(player);
		}
		return null;
	}
	
	public static ItemStack getMainHandItem(Player player) {
		if (ServerHandler.hasCombatUpdate()) {
			return player.getInventory().getItemInMainHand();
		} if (!ServerHandler.hasCombatUpdate()) {
			return Legacy.getLegacyInHandItem(player);
		}
		return null;
	}
	
<<<<<<< HEAD
	@SuppressWarnings("deprecation")
	public static ItemStack getMainHandItem(Player player) {
		if (ServerHandler.hasCombatUpdate()) {
			return player.getInventory().getItemInMainHand();
		} if (!ServerHandler.hasCombatUpdate()) {
			return player.getInventory().getItemInHand();
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
=======
>>>>>>> dev-version
	public static ItemStack getOffHandItem(Player player) {
		if (ServerHandler.hasCombatUpdate()) {
			return player.getInventory().getItemInOffHand();
		} if (!ServerHandler.hasCombatUpdate()) {
<<<<<<< HEAD
			return player.getInventory().getItemInHand();
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static void setInHandItem(Player player, ItemStack toSet) {
		player.getInventory().setItemInHand(toSet);
=======
			return Legacy.getLegacyInHandItem(player);
		}
		return null;
>>>>>>> dev-version
	}
	
	public static void setOffhandItem(Player player, ItemStack toSet) {
		if (ServerHandler.hasCombatUpdate()) {
			player.getInventory().setItemInOffHand(toSet);
		}
	}
	
	public static void updateExperienceLevels(final Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(ItemJoin.getInstance(), (Runnable)new Runnable() {
            public void run() {
            	player.setExp(player.getExp());
            	player.setLevel(player.getLevel());
            }
        }, 1L);
	}
	
	public static void updateInventory(final Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(ItemJoin.getInstance(), (Runnable)new Runnable() {
            public void run() {
            	Legacy.updateLegacyInventory(player);
            }
        }, 1L);
	}
	
	public static void delayUpdateInventory(final Player player, final long delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(ItemJoin.getInstance(), (Runnable)new Runnable() {
            public void run() {
            	Legacy.updateLegacyInventory(player);
            }
        }, delay);
	}
	
	public static boolean getNewSkullMethod() {
		try {
			if (Class.forName("org.bukkit.inventory.meta.SkullMeta").getMethod("getOwningPlayer") != null) { return true; }
		} catch (Exception e) { }
		return false;
	}
	
	public static String getSkullOwner(ItemStack item) {
		if (ServerHandler.hasSpecificUpdate("1_12") && item != null && item.hasItemMeta() && ItemHandler.isSkull(item.getType()) 
				&& ((SkullMeta) item.getItemMeta()).hasOwner() && getNewSkullMethod() != false) {
			String owner =  ((SkullMeta) item.getItemMeta()).getOwningPlayer().getName();
			if (owner != null) { return owner; }
		} else if (item != null && item.hasItemMeta() 
				&& ItemHandler.isSkull(item.getType())
				&& ((SkullMeta) item.getItemMeta()).hasOwner()) {
			String owner = Legacy.getLegacySkullOwner(((SkullMeta) item.getItemMeta()));
			if (owner != null) { return owner; }
		} 
		return "NULL";
	}

	public static Player getPlayerString(String playerName) {
		Player args = null;
		try { args = Bukkit.getPlayer(UUID.fromString(playerName)); } catch (Exception e) {}
		if (playerName != null && Hooks.hasBetterNick()) {
			NickedPlayer np = new NickedPlayer(Legacy.getLegacyPlayer(playerName));
			if (np.isNicked()) {
			return Legacy.getLegacyPlayer(np.getRealName());
			} else {
				return Legacy.getLegacyPlayer(playerName);
			}
		} else if (args == null) { return Legacy.getLegacyPlayer(playerName); }
		return args;
	}
	
	public static String getPlayerID(Player player) {
		if (player != null && player.getUniqueId() != null) {
			return player.getUniqueId().toString();
		} else if (player != null && Hooks.hasBetterNick()) {
			NickedPlayer np = new NickedPlayer(player);
			if (np.isNicked()) {
			return np.getRealName();
			} else {
				return player.getName();
			}
		} else if (player != null) {
			return player.getName();
		}
		return "";
	}
	
	public static String getOfflinePlayerID(OfflinePlayer player) {
		if (player != null && player.getUniqueId() != null) {
			return player.getUniqueId().toString();
		} else if (player != null && Hooks.hasBetterNick()) {
			NickedPlayer np = new NickedPlayer((BetterNick) player);
			if (np.isNicked()) {
			return np.getRealName();
			} else {
				return player.getName();
			}
		} else if (player != null) {
			return player.getName();
		}
		return "";
	}
	
	public static OfflinePlayer getOfflinePlayer(String playerName) {
		Collection<?> playersOnlineNew;
		OfflinePlayer[] playersOnlineOld;
		try {
			if (Bukkit.class.getMethod("getOfflinePlayers", new Class < ? > [0]).getReturnType() == Collection.class) {
				playersOnlineNew = ((Collection < ? > ) Bukkit.class.getMethod("getOfflinePlayers", new Class < ? > [0]).invoke(null, new Object[0]));
				for (Object objPlayer: playersOnlineNew) {
					Player player = ((Player)objPlayer);
					if (player.getName().equalsIgnoreCase(playerName)) {
						return player;
					}
				}
			} else {
				playersOnlineOld = ((OfflinePlayer[]) Bukkit.class.getMethod("getOfflinePlayers", new Class < ? > [0]).invoke(null, new Object[0]));
				for (OfflinePlayer player: playersOnlineOld) {
					if (player.getName().equalsIgnoreCase(playerName)) {
						return player;
					}
				}
			}
		} catch (Exception e) { ServerHandler.sendDebugTrace(e); } 
		return null;
	}
	
<<<<<<< HEAD
	@SuppressWarnings("deprecation")
	public static void setItemInHand(Player player, Material mat) {
		player.setItemInHand(new ItemStack(mat));
	}
	
	public static void setItemInOffHand(Player player, Material mat) {
		if (ServerHandler.hasCombatUpdate()) {
		player.getInventory().setItemInOffHand(new ItemStack(mat));
		}
	}
	
	@SuppressWarnings("deprecation")
=======
>>>>>>> dev-version
	public static double getBalance(Player player) {
		return Legacy.getLegacyBalance(player.getName());
	}
	
	public static EconomyResponse withdrawBalance(Player player, int cost) {
		return Legacy.withdrawLegacyBalance(player.getName(), cost);
	}
}