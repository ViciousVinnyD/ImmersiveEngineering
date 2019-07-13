/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.immersiveengineering.client.gui;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonCheckbox;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import blusunrize.immersiveengineering.client.gui.elements.GuiReactiveList;
import blusunrize.immersiveengineering.common.blocks.metal.TurretChemTileEntity;
import blusunrize.immersiveengineering.common.blocks.metal.TurretGunTileEntity;
import blusunrize.immersiveengineering.common.blocks.metal.TurretTileEntity;
import blusunrize.immersiveengineering.common.gui.ContainerTurret;
import blusunrize.immersiveengineering.common.network.MessageTileSync;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class GuiTurret extends GuiIEContainerBase
{
	public TurretTileEntity tile;
	private TextFieldWidget nameField;

	public GuiTurret(PlayerInventory inventoryPlayer, TurretTileEntity tile)
	{
		super(new ContainerTurret(inventoryPlayer, tile));
		this.tile = tile;
		this.ySize = 190;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		mc.keyboardListener.enableRepeatEvents(true);
		this.nameField = new TextFieldWidget(0, this.fontRenderer, guiLeft+11, guiTop+88, 58, 12);
		this.nameField.setTextColor(-1);
		this.nameField.setDisabledTextColour(-1);
		this.nameField.setEnableBackgroundDrawing(false);
		this.nameField.setMaxStringLength(30);

		this.buttons.clear();
		this.buttons.add(new GuiReactiveList(this, 0, guiLeft+10, guiTop+10, 60, 72, tile.targetList.toArray(new String[0]))
		{
			@Override
			public void onClick(double mouseX, double mouseY)
			{
				super.onClick(mouseX, mouseY);
				CompoundNBT tag = new CompoundNBT();
				int listOffset = -1;
				int rem = selectedOption;
				tile.targetList.remove(rem);
				tag.putInt("remove", rem);
				listOffset = getOffset()-1;
				handleButtonClick(tag, listOffset);
			}
		}.setPadding(0, 0, 2, 2));
		this.buttons.add(new GuiButtonIE(1, guiLeft+74, guiTop+84, 24, 16, I18n.format(Lib.GUI_CONFIG+"turret.add"), "immersiveengineering:textures/gui/turret.png", 176, 65)
		{
			@Override
			public void onClick(double mouseX, double mouseY)
			{
				super.onClick(mouseX, mouseY);
				CompoundNBT tag = new CompoundNBT();
				int listOffset = -1;
				String name = nameField.getText();
				if(!tile.targetList.contains(name))
				{
					listOffset = ((GuiReactiveList)buttons.get(0)).getMaxOffset();
					tag.putString("add", name);
					tile.targetList.add(name);
				}
				nameField.setText("");
				handleButtonClick(tag, listOffset);
			}
		});
		this.buttons.add(new GuiButtonCheckbox(2, guiLeft+74, guiTop+10, I18n.format(Lib.GUI_CONFIG+"turret.blacklist"), !tile.whitelist)
		{
			@Override
			public void onClick(double mouseX, double mouseY)
			{
				super.onClick(mouseX, mouseY);
				CompoundNBT tag = new CompoundNBT();
				int listOffset = -1;
				tile.whitelist = !state;
				tag.putBoolean("whitelist", tile.whitelist);
				handleButtonClick(tag, listOffset);
			}
		});
		this.buttons.add(new GuiButtonCheckbox(3, guiLeft+74, guiTop+26, I18n.format(Lib.GUI_CONFIG+"turret.animals"), tile.attackAnimals)
		{
			@Override
			public void onClick(double mouseX, double mouseY)
			{
				super.onClick(mouseX, mouseY);
				CompoundNBT tag = new CompoundNBT();
				int listOffset = -1;
				tile.attackAnimals = state;
				tag.putBoolean("attackAnimals", tile.attackAnimals);
				handleButtonClick(tag, listOffset);
			}
		});
		this.buttons.add(new GuiButtonCheckbox(4, guiLeft+74, guiTop+42, I18n.format(Lib.GUI_CONFIG+"turret.players"), tile.attackPlayers)
		{
			@Override
			public void onClick(double mouseX, double mouseY)
			{
				super.onClick(mouseX, mouseY);
				CompoundNBT tag = new CompoundNBT();
				int listOffset = -1;
				tile.attackPlayers = state;
				tag.putBoolean("attackPlayers", tile.attackPlayers);
				handleButtonClick(tag, listOffset);
			}
		});
		this.buttons.add(new GuiButtonCheckbox(5, guiLeft+74, guiTop+58, I18n.format(Lib.GUI_CONFIG+"turret.neutrals"), tile.attackNeutrals)
		{
			@Override
			public void onClick(double mouseX, double mouseY)
			{
				super.onClick(mouseX, mouseY);
				CompoundNBT tag = new CompoundNBT();
				int listOffset = -1;
				tile.attackNeutrals = state;
				tag.putBoolean("attackNeutrals", tile.attackNeutrals);
				handleButtonClick(tag, listOffset);
			}
		});

		if(tile instanceof TurretChemTileEntity)
			this.buttons.add(new GuiButtonState(6, guiLeft+135, guiTop+68, 14, 14, null, ((TurretChemTileEntity)tile).ignite, "immersiveengineering:textures/gui/turret.png", 176, 51, 0)
			{
				@Override
				public void onClick(double mouseX, double mouseY)
				{
					super.onClick(mouseX, mouseY);
					CompoundNBT tag = new CompoundNBT();
					int listOffset = -1;
					((TurretChemTileEntity)tile).ignite = state;
					tag.putBoolean("ignite", ((TurretChemTileEntity)tile).ignite);
					handleButtonClick(tag, listOffset);
				}
			});
		else if(tile instanceof TurretGunTileEntity)
			this.buttons.add(new GuiButtonState(6, guiLeft+134, guiTop+31, 16, 16, null, ((TurretGunTileEntity)tile).expelCasings, "immersiveengineering:textures/gui/turret.png", 176, 81, 0)
			{
				@Override
				public void onClick(double mouseX, double mouseY)
				{
					super.onClick(mouseX, mouseY);
					CompoundNBT tag = new CompoundNBT();
					int listOffset = -1;
					((TurretGunTileEntity)tile).expelCasings = state;
					tag.putBoolean("expelCasings", ((TurretGunTileEntity)tile).expelCasings);
					handleButtonClick(tag, listOffset);
				}
			});

	}

	private void handleButtonClick(CompoundNBT nbt, int listOffset)
	{
		if(!nbt.isEmpty())
		{
			ImmersiveEngineering.packetHandler.sendToServer(new MessageTileSync(tile, nbt));
			this.initGui();
			if(listOffset >= 0)
				((GuiReactiveList)this.buttons.get(0)).setOffset(listOffset);
		}
	}
	@Override
	public void render(int mx, int my, float partial)
	{
		super.render(mx, my, partial);
		this.nameField.drawTextField(mx, my, partial);

		ArrayList<ITextComponent> tooltip = new ArrayList<>();
		if(mx >= guiLeft+158&&mx < guiLeft+165&&my >= guiTop+16&&my < guiTop+62)
			tooltip.add(new StringTextComponent(tile.getEnergyStored(null)+"/"+tile.getMaxEnergyStored(null)+" IF"));

		if(tile instanceof TurretChemTileEntity)
		{
			ClientUtils.handleGuiTank(((TurretChemTileEntity)tile).tank, guiLeft+134, guiTop+16, 16, 47, 196, 0, 20, 51, mx, my, "immersiveengineering:textures/gui/turret.png", tooltip);
			if(mx >= guiLeft+135&&mx < guiLeft+149&&my >= guiTop+68&&my < guiTop+82)
				tooltip.add(new TranslationTextComponent(Lib.GUI_CONFIG+"turret.ignite_fluid"));
		}
		else if(tile instanceof TurretGunTileEntity)
		{
			if(mx >= guiLeft+134&&mx < guiLeft+150&&my >= guiTop+31&&my < guiTop+47)
				tooltip.add(new TranslationTextComponent(Lib.GUI_CONFIG+"turret.expel_casings_"+(((TurretGunTileEntity)tile).expelCasings?"on": "off")));
		}
		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, -1, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GlStateManager.color3f(1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture("immersiveengineering:textures/gui/turret.png");
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		int stored = (int)(46*(tile.getEnergyStored(null)/(float)tile.getMaxEnergyStored(null)));
		ClientUtils.drawGradientRect(guiLeft+158, guiTop+16+(46-stored), guiLeft+165, guiTop+62, 0xffb51500, 0xff600b00);

		if(tile instanceof TurretChemTileEntity)
		{
			this.drawTexturedModalRect(guiLeft+132, guiTop+14, 176, 0, 20, 51);
			ClientUtils.handleGuiTank(((TurretChemTileEntity)tile).tank, guiLeft+134, guiTop+16, 16, 47, 196, 0, 20, 51, mx, my, "immersiveengineering:textures/gui/turret.png", null);
		}
		else if(tile instanceof TurretGunTileEntity)
		{
			ClientUtils.drawDarkSlot(guiLeft+134, guiTop+13, 16, 16);
			ClientUtils.drawDarkSlot(guiLeft+134, guiTop+49, 16, 16);
		}
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		mc.keyboardListener.enableRepeatEvents(false);
	}

	@Override
	public boolean keyPressed(int key, int scancode, int p_keyPressed_3_)
	{
		if(super.keyPressed(key, scancode, p_keyPressed_3_))
			return true;
		else if(this.nameField.isFocused()&&key==GLFW.GLFW_KEY_ENTER)
		{
			String name = this.nameField.getText();
			if(!tile.targetList.contains(name))
			{
				CompoundNBT tag = new CompoundNBT();
				tag.putString("add", name);
				tile.targetList.add(name);
				ImmersiveEngineering.packetHandler.sendToServer(new MessageTileSync(tile, tag));

				this.initGui();
				((GuiReactiveList)this.buttons.get(0)).setOffset(((GuiReactiveList)this.buttons.get(0)).getMaxOffset());
			}
			return true;
		}
		else if(!this.nameField.keyPressed(key, scancode, p_keyPressed_3_))
			return true;
		else
			return false;
	}
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		return this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
	}
}