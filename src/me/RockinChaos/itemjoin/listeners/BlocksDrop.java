/*
 * ItemJoin
 * Copyright (C) CraftationGaming <https://www.craftationgaming.com/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.RockinChaos.itemjoin.listeners;

import java.util.Collection;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.RockinChaos.itemjoin.giveitems.utils.ItemMap;
import me.RockinChaos.itemjoin.giveitems.utils.ItemUtilities;
import me.RockinChaos.itemjoin.handlers.PlayerHandler;
import me.RockinChaos.itemjoin.handlers.ServerHandler;
import me.RockinChaos.itemjoin.utils.DependAPI;

public class BlocksDrop implements Listener {
	
   /**
    * Handles the Block Break custom item drops.
    * 
    * @param event - BlockBreakEvent
    */
	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent event) {
		final Block block = event.getBlock();
		final Material material = (block != null ? block.getType() : Material.AIR);
		final Player player = event.getPlayer();
		final Collection<ItemStack> drops = block.getDrops(PlayerHandler.getPlayer().getMainHandItem(player));
		ServerHandler.getServer().runAsyncThread(main -> {
			for (ItemMap itemMap: ItemUtilities.getUtilities().getItems()) {
				if (itemMap.blocksDrop() && block != null && material != Material.AIR && itemMap.getBlocksDrop().containsKey(material) 
				 && itemMap.inWorld(player.getWorld()) && itemMap.hasPermission(player) && this.willDrop(drops, material) && Math.random() <= itemMap.getBlocksDrop().get(material)) {
					for (String region : ((DependAPI.getDepends(false).getGuard().guardEnabled() && !itemMap.getEnabledRegions().isEmpty()) ? DependAPI.getDepends(false).getGuard().getRegionsAtLocation(player).split(", ") : new String[]{"FALSE"})) {
						if (!DependAPI.getDepends(false).getGuard().guardEnabled() || itemMap.getEnabledRegions().isEmpty() || itemMap.inRegion(region)) { 
							block.getWorld().dropItemNaturally(block.getLocation(), itemMap.getItem(player));
						}
					}
				}
			}
		});
	}
	
	private boolean willDrop(final Collection<ItemStack> drops, final Material material) {
		for (ItemStack item: drops) {
			if (item.getType().equals(material)) {
				return true;
			}
		}
		return false;
	}
}