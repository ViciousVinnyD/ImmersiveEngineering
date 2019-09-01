/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.immersiveengineering.client.gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.metal.SqueezerTileEntity;
import blusunrize.immersiveengineering.common.gui.SqueezerContainer;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

public class SqueezerScreen extends IEContainerScreen
{
	SqueezerTileEntity tile;

	public SqueezerScreen(PlayerInventory inventoryPlayer, SqueezerTileEntity tile)
	{
		super(new SqueezerContainer(inventoryPlayer, tile), inventoryPlayer);
		this.tile = tile;
	}

	@Override
	public void render(int mx, int my, float partial)
	{
		super.render(mx, my, partial);
		List<ITextComponent> tooltip = new ArrayList<>();
		ClientUtils.handleGuiTank(tile.tanks[0], guiLeft+112, guiTop+21, 16, 47, 177, 31, 20, 51, mx, my, "immersiveengineering:textures/gui/cokeOven.png", tooltip);
		if(mx > guiLeft+158&&mx < guiLeft+165&&my > guiTop+22&&my < guiTop+68)
			tooltip.add(new StringTextComponent(tile.getEnergyStored(null)+"/"+tile.getMaxEnergyStored(null)+" IF"));
		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, font, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GlStateManager.color3f(1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture("immersiveengineering:textures/gui/squeezer.png");
		this.blit(guiLeft, guiTop, 0, 0, xSize, ySize);

		int stored = (int)(46*(tile.getEnergyStored(null)/(float)tile.getMaxEnergyStored(null)));
		fillGradient(guiLeft+158, guiTop+22+(46-stored), guiLeft+165, guiTop+68, 0xffb51500, 0xff600b00);
		ClientUtils.handleGuiTank(tile.tanks[0], guiLeft+112, guiTop+21, 16, 47, 177, 31, 20, 51, mx, my, "immersiveengineering:textures/gui/squeezer.png", null);

	}
}