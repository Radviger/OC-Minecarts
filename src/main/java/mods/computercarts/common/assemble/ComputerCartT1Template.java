package mods.computercarts.common.assemble;

import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.common.Tier;
import mods.computercarts.common.assemble.util.General;
import mods.computercarts.common.items.ModItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;

public class ComputerCartT1Template {

    private static final int MAX_COMPLEXITY = 12;

    public static boolean select(ItemStack stack) {
        return stack.getItem().equals(ModItems.COMPUTER_CART_CASE) && stack.getItemDamage() == 0;
    }

    public static Object[] validate(IInventory inventory) {
        return General.validate(inventory, MAX_COMPLEXITY);
    }

    public static Object[] assemble(IInventory inventory) {
        return General.assemble(inventory, 0);
    }

    public static int[] getContainerTier() {
        return new int[]{1, 0, 0};
    }

    public static int[] getUpgradeTier() {
        return new int[]{0, 0, 0};
    }

    public static Iterable<Pair<String, Integer>> getComponentSlots() {
        ArrayList<Pair<String, Integer>> list = new ArrayList<Pair<String, Integer>>();
        list.add(Pair.of(Slot.Card, 0));
        list.add(Pair.of(Slot.None, -1));
        list.add(Pair.of(Slot.None, -1));

        list.add(Pair.of(Slot.CPU, 0));
        list.add(Pair.of(Slot.Memory, 0));
        list.add(Pair.of(Slot.Memory, 0));

        list.add(Pair.of("eeprom", Tier.Any()));
        list.add(Pair.of(Slot.HDD, 0));
        return list;
    }
}