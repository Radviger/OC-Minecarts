package mods.ocminecart.common.driver;

import mods.ocminecart.OCMinecart;
import mods.ocminecart.common.minecart.ComputerCart;
import mods.ocminecart.common.minecart.IComputerCart;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import li.cil.oc.api.Items;
import li.cil.oc.api.driver.EnvironmentAware;
import li.cil.oc.api.driver.EnvironmentHost;
import li.cil.oc.api.driver.Item;
import li.cil.oc.api.driver.item.HostAware;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.server.component.UpgradeInventoryController;

public class DriverInventoryController implements Item, HostAware, EnvironmentAware {

	@Override
	public boolean worksWith(ItemStack stack, Class<? extends EnvironmentHost> host) {
		if(host.equals(ComputerCart.class)) return this.worksWith(stack);
		return false;
	}

	@Override
	public boolean worksWith(ItemStack stack) {
		if(stack!= null && Items.get("inventoryControllerUpgrade").createItemStack(1).isItemEqual(stack)){
			return true;
		}
		return false;
	}

	@Override
	public String slot(ItemStack stack) {
		return Slot.Upgrade;
	}

	@Override
	public int tier(ItemStack stack) {
		return 1;
	}

	@Override
	public NBTTagCompound dataTag(ItemStack stack) {
		return CustomDriver.dataTag(stack);
	}

	@Override
	public Class<? extends Environment> providedEnvironment(ItemStack stack) {
		if(stack!= null && Items.get("inventoryControllerUpgrade").createItemStack(1).isItemEqual(stack))
			return UpgradeInventoryController.Drone.class;
		return null;
	}

	@Override
	public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost host) {
		//I can use the Component for the Drone because it just require a Agent
		if(host instanceof ComputerCart) 
			return new UpgradeInventoryController.Drone((ComputerCart)host);
		return null;
	}

}
