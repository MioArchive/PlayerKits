package net.javamio.playerkits.util.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public record AbstractItem(ItemStack stack, Consumer<InventoryClickEvent> handler) {

}
