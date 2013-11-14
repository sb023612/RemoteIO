package com.dmillerw.remoteIO.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.oredict.OreDictionary;

import com.dmillerw.remoteIO.RemoteIO;
import com.dmillerw.remoteIO.core.CreativeTabRIO;
import com.dmillerw.remoteIO.lib.ModInfo;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemUpgrade extends Item {

	public Icon[] icons;
	
	public ItemUpgrade(int id) {
		super(id);
		
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabRIO.tab);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean idk) {
		for (String string : Upgrade.values()[stack.getItemDamage()].description) {
			list.add(string);
		}
	}
	
	@Override
	public Icon getIconFromDamage(int damage) {
		return this.icons[damage];
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void getSubItems(int id, CreativeTabs tab, List list) {
		for (Upgrade upgrade : Upgrade.values()) {
			if (upgrade.recipeComponents != null || upgrade == Upgrade.BLANK) {
				list.add(upgrade.toItemStack());
			}
		}
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.icons = new Icon[Upgrade.values().length];
		
		for (Upgrade upgrade : Upgrade.values()) {
			this.icons[upgrade.ordinal()] = register.registerIcon(ModInfo.RESOURCE_PREFIX + "upgrade/" + upgrade.texture);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item.upgrade." + Upgrade.values()[stack.getItemDamage()] + ".name";
	}	
	
	public static enum Upgrade {
		BLANK("blank",                        "Blank Upgrade",     null),
		ITEM("item",                          "Item",              new ItemStack[] {new ItemStack(Block.chest)},                                          "Allows for the basic transport of items"),
		FLUID("fluid",                        "Fluid",             new ItemStack[] {new ItemStack(Item.bucketEmpty)},                                     "Allows for the basic transport of fluids"),
		POWER_BC("powerBC",                   "Buildcraft Power",  new ItemStack[] {new ItemStack(Item.redstone)},                                        "Allows for the transfer of BC power (MJ)"),
		RANGE("range",                        "Range",             new ItemStack[] {new ItemStack(Item.glowstone)},                                       "Increases the range at which the IO block can connect", "Each upgrade increases the range by 8 blocks"),
		CROSS_DIMENSIONAL("crossDimensional", "Cross Dimensional", new ItemStack[] {new ItemStack(Block.obsidian),  new ItemStack(Block.enderChest)},                                                                 "Allows the IO block to connect across dimensions"),
		ISIDED_AWARE("iSidedAware",           "Side Awareness",    new ItemStack[] {new ItemStack(Block.hopperBlock)},                                    "Allows the IO block to determine side input/output"),
		REDSTONE("redstone",                  "Redstone",          new ItemStack[] {new ItemStack(Item.redstoneRepeater)},                                "Allows for the toggle of the remote connection via redstone"),
		CAMO("camo",                          "Adaptive Texture",  new ItemStack[] {new ItemStack(RemoteIO.instance.config.itemComponentID + 256, 1, 0)}, "Allows the IO block to take on the texture of any other block"),
		LOCK("lock",                          "Lock",              new ItemStack[] {new ItemStack(RemoteIO.instance.config.itemComponentID + 256, 1, 1)}, "Allows the IO block to be broken and replaced, while retaining all settings/links");
		
		public String texture;
		public String localizedName;
		
		public ItemStack[] recipeComponents;
		
		public String[] description;
		
		private Upgrade(String texture, String localizedName, ItemStack[] recipeComponents, String ... description) {
			this.texture = texture;
			this.localizedName = "Upgrade: " + localizedName;
			this.recipeComponents = recipeComponents;
			this.description = description;
		}
		
		public ItemStack toItemStack() {
			return new ItemStack(RemoteIO.instance.config.itemUpgrade, 1, this.ordinal());
		}
		
	}
	
}
