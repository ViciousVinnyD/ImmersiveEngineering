/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.immersiveengineering.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockIESlabs extends ItemBlockIEBase
{
	public ItemBlockIESlabs(Block b)
	{
		super(b);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag tooltipFlag)
	{
		super.addInformation(stack, world, tooltip, tooltipFlag);
	}

	@Override
	public ActionResultType onItemUse(PlayerEntity player, World world, BlockPos pos, Hand hand, Direction side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		BlockState iblockstate = world.getBlockState(pos);
		Block localBlock = iblockstate.getBlock();
		BlockPos posThere = pos;
		BlockPos posOffset = pos.offset(side);

		if(localBlock==Blocks.SNOW_LAYER&&localBlock.isReplaceable(world, pos))
			side = Direction.UP;
		else if(!localBlock.isReplaceable(world, pos))
			pos = pos.offset(side);

		IESlabTileEntity stackSlab = null;
		if(side.getAxis().isVertical()&&this.block.equals(world.getBlockState(posThere).getBlock())&&world.getBlockState(posThere).getBlock().getMetaFromState(world.getBlockState(posThere))==stack.getItemDamage())
		{
			TileEntity te = world.getTileEntity(posThere);
			if(te instanceof IESlabTileEntity&&((IESlabTileEntity)te).slabType+side.ordinal()==1)
				stackSlab = ((IESlabTileEntity)te);
		}
		else if(this.block.equals(world.getBlockState(posOffset).getBlock())&&world.getBlockState(posOffset).getBlock().getMetaFromState(world.getBlockState(posOffset))==stack.getItemDamage())
		{
			TileEntity te = world.getTileEntity(posOffset);
			if(te instanceof IESlabTileEntity)
			{
				int type = ((IESlabTileEntity)te).slabType;
				if((type==0&&(side==Direction.DOWN||hitY >= .5))||(type==1&&(side==Direction.UP||hitY <= .5)))
					stackSlab = ((IESlabTileEntity)te);
			}
		}
		else
			return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
		if(stackSlab!=null)
		{
			stackSlab.slabType = 2;
			stackSlab.markContainingBlockForUpdate(null);
			world.playSound(stackSlab.getPos().getX()+.5, stackSlab.getPos().getY()+.5, stackSlab.getPos().getZ()+.5, this.block.getSoundType().getPlaceSound(), SoundCategory.BLOCKS, (this.block.getSoundType().getVolume()+1.0F)/2.0F, this.block.getSoundType().getPitch()*0.8F, false);
			stack.shrink(1);
			return ActionResultType.SUCCESS;
		}
		else
			return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, Direction side, PlayerEntity player, ItemStack stack)
	{
		return true;
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, PlayerEntity player, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, BlockState newState)
	{
		boolean ret = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
		if(ret)
		{
			TileEntity tileEntity = world.getTileEntity(pos);
			if(tileEntity instanceof IESlabTileEntity)
				((IESlabTileEntity)tileEntity).slabType = (side==Direction.DOWN||(side!=Direction.UP&&hitY >= .5))?1: 0;
		}
		return ret;
	}
}