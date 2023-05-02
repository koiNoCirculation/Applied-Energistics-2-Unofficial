/*
 * This file is part of Applied Energistics 2. Copyright (c) 2013 - 2015, AlgorithmX2, All rights reserved. Applied
 * Energistics 2 is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version. Applied Energistics 2 is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General
 * Public License for more details. You should have received a copy of the GNU Lesser General Public License along with
 * Applied Energistics 2. If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.util;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.common.util.ForgeDirection;

import appeng.api.config.FuzzyMode;
import appeng.api.config.InsertionMode;
import appeng.integration.IntegrationRegistry;
import appeng.integration.IntegrationType;
import appeng.integration.abstraction.IBetterStorage;
import appeng.tile.AEBaseInvTile;
import appeng.tile.misc.TileInterface;
import appeng.util.inv.*;

public abstract class InventoryAdaptor implements Iterable<ItemSlot> {

    // returns an appropriate adaptor, or null
    public static InventoryAdaptor getAdaptor(final Object te, final ForgeDirection d) {
        if (te == null) {
            return null;
        }

        final IBetterStorage bs = (IBetterStorage) (IntegrationRegistry.INSTANCE.isEnabled(
                IntegrationType.BetterStorage) ? IntegrationRegistry.INSTANCE.getInstance(IntegrationType.BetterStorage)
                        : null);

        if (te instanceof EntityPlayer) {
            return new AdaptorIInventory(new AdaptorPlayerInventory(((EntityPlayer) te).inventory, false));
        } else if (te instanceof ArrayList) {
            @SuppressWarnings("unchecked")
            final ArrayList<ItemStack> list = (ArrayList<ItemStack>) te;

            return new AdaptorList(list);
        } else if (bs != null && bs.isStorageCrate(te)) {
            return bs.getAdaptor(te, d);
        } else if (te instanceof TileEntityChest) {
            return new AdaptorIInventory(Platform.GetChestInv(te));
        } else if (te instanceof ISidedInventory si) {
            final int[] slots = si.getAccessibleSlotsFromSide(d.ordinal());
            if (te instanceof TileInterface) {
                return new AdaptorDualityInterface(new WrapperMCISidedInventory(si, d), (TileInterface) te);
            }
            int stackLimit = 0;
            if (te instanceof AEBaseInvTile) {
                stackLimit = ((AEBaseInvTile) te).getInternalInventory().getInventoryStackLimit();
            }
            if (si.getSizeInventory() > 0 && slots != null && slots.length > 0) {
                if (stackLimit > 0) {
                    return new AdaptorIInventory(new WrapperMCISidedInventory(si, d), stackLimit);
                }
                return new AdaptorIInventory(new WrapperMCISidedInventory(si, d));
            }
        } else if (te instanceof IInventory i) {
            if (i.getSizeInventory() > 0) {
                return new AdaptorIInventory(i);
            }
        }

        return null;
    }

    // return what was extracted.
    public abstract ItemStack removeItems(int amount, ItemStack filter, IInventoryDestination destination);

    public abstract ItemStack simulateRemove(int amount, ItemStack filter, IInventoryDestination destination);

    // return what was extracted.
    public abstract ItemStack removeSimilarItems(int amount, ItemStack filter, FuzzyMode fuzzyMode,
            IInventoryDestination destination);

    public abstract ItemStack simulateSimilarRemove(int amount, ItemStack filter, FuzzyMode fuzzyMode,
            IInventoryDestination destination);

    // return what isn't used...
    public abstract ItemStack addItems(ItemStack toBeAdded);

    /**
     * @param insertionMode advice implementation on how ItemStacks should be inserted. Might not has an effect
     *                      whatsoever!
     */
    public ItemStack addItems(ItemStack toBeAdded, InsertionMode insertionMode) {
        return addItems(toBeAdded);
    }

    public abstract ItemStack simulateAdd(ItemStack toBeSimulated);

    /**
     * @param insertionMode advice implementation on how ItemStacks should be inserted. Might not has an effect
     *                      whatsoever!
     * @return The leftover itemstack, or null if everything could be inserted
     */
    public ItemStack simulateAdd(ItemStack toBeSimulated, InsertionMode insertionMode) {
        return simulateAdd(toBeSimulated);
    }

    public abstract boolean containsItems();
}
