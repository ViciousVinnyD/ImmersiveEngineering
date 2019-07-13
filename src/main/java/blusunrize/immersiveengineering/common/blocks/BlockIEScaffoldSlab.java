/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.immersiveengineering.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockIEScaffoldSlab<E extends Enum<E> & IEBaseBlock.IBlockEnum> extends BlockIESlab<E>
{
	public BlockIEScaffoldSlab(String name, Material material, PropertyEnum property)
	{
		super(name, material, property);
		setBlockLayer(BlockRenderLayer.CUTOUT_MIPPED);
	}

	@Override
	public void addCollisionBoxToList(BlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof IESlabTileEntity)
		{
			int type = ((IESlabTileEntity)te).slabType;
			if(type==0)
				addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(.0625f, 0, .0625f, .9375f, .5, .9375f));
			else if(type==1)
				addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(.0625f, .5, .0625f, .9375f, 1, .9375f));
			else
				addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(.0625f, 0, .0625f, .9375f, 1, .9375f));
		}
		else
			addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(.0625f, 0, .0625f, .9375f, .5, .9375f));
	}

	@Override
	public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof IESlabTileEntity)
		{
			int type = ((IESlabTileEntity)te).slabType;
			if(type==0)
				return new AxisAlignedBB(0, 0, 0, 1, .5f, 1);
			else if(type==1)
				return new AxisAlignedBB(0, .5f, 0, 1, 1, 1);
			else
				return FULL_BLOCK_AABB;
		}
		else
			return new AxisAlignedBB(0, 0, 0, 1, .5f, 1);
	}

	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, BlockState state, Entity entityIn)
	{
		super.onEntityCollision(worldIn, pos, state, entityIn);
		if(entityIn instanceof LivingEntity&&!((LivingEntity)entityIn).isOnLadder()&&isLadder(state, worldIn, pos, (LivingEntity)entityIn))
		{
			float f5 = 0.15F;
			if(entityIn.motionX < -f5)
				entityIn.motionX = -f5;
			if(entityIn.motionX > f5)
				entityIn.motionX = f5;
			if(entityIn.motionZ < -f5)
				entityIn.motionZ = -f5;
			if(entityIn.motionZ > f5)
				entityIn.motionZ = f5;

			entityIn.fallDistance = 0.0F;
			if(entityIn.motionY < -0.15D)
				entityIn.motionY = -0.15D;

			if(entityIn.motionY < 0&&entityIn instanceof PlayerEntity&&entityIn.isSneaking())
			{
				entityIn.motionY = 0;
				return;
			}
			if(entityIn.collidedHorizontally)
				entityIn.motionY = .2;
		}
	}

	@Override
	public boolean isLadder(BlockState state, IBlockAccess world, BlockPos pos, LivingEntity entity)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof IESlabTileEntity&&((IESlabTileEntity)te).slabType==0)
			return entity.posY-pos.getY() < .5;
		return true;
	}

	@Override
	public boolean allowHammerHarvest(BlockState state)
	{
		return true;
	}
}