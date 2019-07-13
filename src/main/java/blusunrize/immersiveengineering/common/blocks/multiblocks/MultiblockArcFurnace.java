/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.immersiveengineering.common.blocks.multiblocks;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsIE;
import blusunrize.immersiveengineering.common.blocks.EnumMetals;
import blusunrize.immersiveengineering.common.blocks.metal.ArcFurnaceTileEntity;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalMultiblock;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class MultiblockArcFurnace implements IMultiblock
{
	public static MultiblockArcFurnace instance = new MultiblockArcFurnace();
	static ItemStack[][][] structure = new ItemStack[5][5][5];

	static
	{
		for(int h = 0; h < 5; h++)
			for(int l = 0; l < 5; l++)
				for(int w = 0; w < 5; w++)
				{
					if(h==0)
					{
						if(l==0&&w==2)
							structure[h][l][w] = new ItemStack(Items.CAULDRON);
						else if(l==2&&(w==0||w==4))
							structure[h][l][w] = new ItemStack(IEContent.blockStorage, 1, BlockTypes_MetalsIE.STEEL.getMeta());
						else if(l==0&&w==0)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
						else if(l==4&&w==2)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
						else if(l==4||(l > 2&&(w==0||w==4)))
							structure[h][l][w] = new ItemStack(IEContent.blockSheetmetal, 1, EnumMetals.STEEL.getMeta());
						else
							structure[h][l][w] = new ItemStack(IEContent.blockSheetmetalSlabs, 1, EnumMetals.STEEL.getMeta());
					}
					else if(h==1)
					{
						if(l==2&&(w==0||w==4))
							structure[h][l][w] = new ItemStack(IEContent.blockStorage, 1, BlockTypes_MetalsIE.STEEL.getMeta());
						else if(l==0&&w==0)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta());
						else if(l==4&&w > 0&&w < 4)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
						else if((w==0||w==4)&&l > 2)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
						else if(l >= 2&&w > 0&&w < 4)
							structure[h][l][w] = new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.BLASTBRICK_REINFORCED.getMeta());
					}
					else if(h==2)
					{
						if(l==2&&(w==0||w==4))
							structure[h][l][w] = new ItemStack(IEContent.blockStorage, 1, BlockTypes_MetalsIE.STEEL.getMeta());
						else if(l==4&&w > 0&&w < 4)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
						else if(l==4)
							structure[h][l][w] = new ItemStack(IEContent.blockSheetmetal, 1, EnumMetals.STEEL.getMeta());
						else if(w > 0&&w < 4)
							structure[h][l][w] = new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.BLASTBRICK_REINFORCED.getMeta());
					}
					else if(h==3)
					{
						if(l==4&&w==2)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
						else if(l==4&&(w==1||w==3))
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
						else if(l > 0&&w > 0&&w < 4)
							structure[h][l][w] = new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.BLASTBRICK_REINFORCED.getMeta());
					}
					else if(h==4)
					{
						if(l > 1&&w==2)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
						else if(l==4&&(w==1||w==3))
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
					}

					if(structure[h][l][w]==null)
					{
						structure[h][l][w] = ItemStack.EMPTY;
					}
				}
	}

	@Override
	public ItemStack[][][] getStructureManual()
	{
		return structure;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean overwriteBlockRender(ItemStack stack, int iterator)
	{
		return false;
	}

	@Override
	public BlockState getBlockstateFromStack(int index, ItemStack stack)
	{
		if(!stack.isEmpty())
		{
			if(stack.getItem()==Items.CAULDRON)
				return Blocks.CAULDRON.getDefaultState();
			else if(stack.getItem() instanceof BlockItem)
				return ((BlockItem)stack.getItem()).getBlock().getStateFromMeta(stack.getItemDamage());
		}
		return null;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRenderFormedStructure()
	{
		return true;
	}

	//@OnlyIn(Dist.CLIENT)
	static ItemStack renderStack = ItemStack.EMPTY;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderFormedStructure()
	{
		if(renderStack.isEmpty())
			renderStack = new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.ARC_FURNACE.getMeta());

		GlStateManager.translate(2.5, 2.25, 2.25);
		GlStateManager.rotate(-45, 0, 1, 0);
		GlStateManager.rotate(-20, 1, 0, 0);
		GlStateManager.scale(6.5, 6.5, 6.5);

		GlStateManager.disableCull();
		ClientUtils.mc().getRenderItem().renderItem(renderStack, ItemCameraTransforms.TransformType.GUI);
		GlStateManager.enableCull();
	}

	@Override
	public float getManualScale()
	{
		return 12;
	}

	@Override
	public String getUniqueName()
	{
		return "IE:ArcFurnace";
	}

	@Override
	public boolean isBlockTrigger(BlockState state)
	{
		return state.getBlock()==Blocks.CAULDRON;
	}

	@Override
	public boolean createStructure(World world, BlockPos pos, Direction side, PlayerEntity player)
	{
		if(side==Direction.UP||side==Direction.DOWN)
			side = Direction.fromAngle(player.rotationYaw);
		BlockPos startPos = pos;
		side = side.getOpposite();

		if(Utils.isOreBlockAt(world, startPos.add(0, -1, 0), "scaffoldingSteel")
				&&Utils.isBlockAt(world, startPos.offset(side, 2).add(0, -1, 0), IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta()))
		{
			startPos = startPos.offset(side, 2);
			side = side.getOpposite();
		}

		boolean mirrored = false;
		boolean b = structureCheck(world, startPos, side, mirrored);
		if(!b)
		{
			mirrored = true;
			b = structureCheck(world, startPos, side, mirrored);
		}

		if(b)
		{
			BlockState state = IEContent.blockMetalMultiblock.getStateFromMeta(BlockTypes_MetalMultiblock.ARC_FURNACE.getMeta());
			state = state.with(IEProperties.FACING_HORIZONTAL, side);
			for(int l = 0; l < 5; l++)
				for(int w = -2; w <= 2; w++)
					for(int h = 0; h < 5; h++)
						if(!structure[h][l][w+2].isEmpty())
						{
							int ww = mirrored?-w: w;
							BlockPos pos2 = startPos.offset(side, l).offset(side.rotateY(), ww).add(0, h, 0);

							world.setBlockState(pos2, state);
							TileEntity curr = world.getTileEntity(pos2);
							if(curr instanceof ArcFurnaceTileEntity)
							{
								ArcFurnaceTileEntity tile = (ArcFurnaceTileEntity)curr;
								tile.formed = true;
								tile.pos = h*25+l*5+(w+2);
								tile.offset = new int[]{(side==Direction.WEST?-l+2: side==Direction.EAST?l-2: side==Direction.NORTH?ww: -ww), h-1, (side==Direction.NORTH?-l+2: side==Direction.SOUTH?l-2: side==Direction.EAST?ww: -ww)};
								tile.mirrored = mirrored;
								tile.markDirty();
								world.addBlockEvent(pos2, IEContent.blockMetalMultiblock, 255, 0);
							}
						}
		}
		return b;
	}

	boolean structureCheck(World world, BlockPos startPos, Direction dir, boolean mirror)
	{
		for(int l = 0; l < 5; l++)
			for(int w = -2; w <= 2; w++)
				for(int h = 0; h < 5; h++)
					if(!structure[h][l][w+2].isEmpty())
					{
						int ww = mirror?-w: w;
						BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);

						if(world.isAirBlock(pos))
							return false;
						if(OreDictionary.itemMatches(structure[h][l][w+2], new ItemStack(Items.CAULDRON), true))
						{
							if(!Utils.isBlockAt(world, pos, Blocks.CAULDRON, OreDictionary.WILDCARD_VALUE))
								return false;
						}
						else if(OreDictionary.itemMatches(structure[h][l][w+2], new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta()), true))
						{
							if(!Utils.isOreBlockAt(world, pos, "scaffoldingSteel"))
								return false;
						}
						else if(OreDictionary.itemMatches(structure[h][l][w+2], new ItemStack(IEContent.blockSheetmetal, 1, EnumMetals.STEEL.getMeta()), true))
						{
							if(!Utils.isOreBlockAt(world, pos, "blockSheetmetalSteel"))
								return false;
						}
						else if(OreDictionary.itemMatches(structure[h][l][w+2], new ItemStack(IEContent.blockSheetmetalSlabs, 1, EnumMetals.STEEL.getMeta()), true))
						{
							if(!Utils.isOreBlockAt(world, pos, "slabSheetmetalSteel"))
								return false;
						}
						else if(OreDictionary.itemMatches(structure[h][l][w+2], new ItemStack(IEContent.blockStorage, 1, BlockTypes_MetalsIE.STEEL.getMeta()), true))
						{
							if(!Utils.isOreBlockAt(world, pos, "blockSteel"))
								return false;
						}
						else
						{
							Block b = Block.getBlockFromItem(structure[h][l][w+2].getItem());
							if(b!=null)
								if(!Utils.isBlockAt(world, pos, b, structure[h][l][w+2].getItemDamage()))
									return false;
						}
					}
		return true;
	}

	static final IngredientStack[] materials = new IngredientStack[]{
			new IngredientStack(new ItemStack(Items.CAULDRON)),
			new IngredientStack("slabSheetmetalSteel", 14),
			new IngredientStack("blockSheetmetalSteel", 8),
			new IngredientStack("blockSteel", 6),
			new IngredientStack("scaffoldingSteel", 5),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 10, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 5, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockStoneDecoration, 27, BlockTypes_StoneDecoration.BLASTBRICK_REINFORCED.getMeta()))};

	@Override
	public IngredientStack[] getTotalMaterials()
	{
		return materials;
	}
}