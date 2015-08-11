package mods.ocminecart.client.gui;

import java.util.Iterator;

import li.cil.oc.api.Driver;
import li.cil.oc.api.component.TextBuffer;
import li.cil.oc.api.driver.Item;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.client.renderer.TextBufferRenderCache;
import mods.ocminecart.OCMinecart;
import mods.ocminecart.Settings;
import mods.ocminecart.client.SlotIcons;
import mods.ocminecart.client.gui.widget.ImageButton;
import mods.ocminecart.client.renderer.gui.BufferRenderer;
import mods.ocminecart.common.container.ComputerCartContainer;
import mods.ocminecart.common.inventory.ComponetInventory;
import mods.ocminecart.common.minecart.ComputerCart;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class ComputerCartGui extends GuiContainer {
	
	private ResourceLocation textureNoScreen = new ResourceLocation( Settings.OC_ResLoc , "textures/gui/robot_noscreen.png");
	private ResourceLocation textureScreen = new ResourceLocation( Settings.OC_ResLoc , "textures/gui/robot.png");
	
	private ResourceLocation textureOnOffButton = new ResourceLocation( Settings.OC_ResLoc , "textures/gui/button_power.png");
	
	private ComputerCartContainer container;
	
	private BufferRenderer bufrender;
	
	private TextBuffer textbuffer;
	private boolean guiSizeChange = false;
	
	private int txtWidth, txtHeight;
	
	private double maxBufferWidth = 240.0;
	private double maxBufferHeight = 140.0;
	
	private double bufferscale = 0.0;

	private double bufferRenderWidth = Math.min(maxBufferWidth, TextBufferRenderCache.renderer().charRenderWidth() * 50);
	private double bufferRenderHeight = Math.min(maxBufferHeight, TextBufferRenderCache.renderer().charRenderHeight() * 16);
	
	private int bufferX = (int)(8 + (this.maxBufferWidth - this.bufferRenderWidth) /2);
	private int bufferY = (int)(8 + (this.maxBufferHeight - this.bufferRenderHeight) /2);
	

	public ComputerCartGui(InventoryPlayer inventory, ComputerCart entity) {
		super(new ComputerCartContainer(inventory,entity));
		this.container=(ComputerCartContainer) this.inventorySlots;
		
		for(int i=0;i<entity.compinv.getSizeInventory();i+=1){
			if(entity.compinv.getStackInSlot(i)!=null) System.out.println(entity.compinv.getStackInSlot(i).getDisplayName());
		}
		
		this.initComponents(this.container.getEntity().compinv.getComponents());
		ComponetInventory compinv = this.container.getEntity().compinv;
		for(int i=0;i<compinv.getSizeInventory();i+=1){
			if(compinv.getStackInSlot(i)!=null)
			OCMinecart.logger.log(Level.INFO, compinv.getStackInSlot(i).getDisplayName());
		}
		
		this.ySize= (container.getHasScreen()) ? ComputerCartContainer.YSIZE_SCR : ComputerCartContainer.YSIZE_NOSCR;
		this.xSize= ComputerCartContainer.XSIZE;
	}
	
	public void initGui(){
		super.initGui();
		int offset = (this.container.getHasScreen()) ? ComputerCartContainer.DELTA : 0;
		
		this.bufrender = new BufferRenderer(Minecraft.getMinecraft().renderEngine);
		this.guiSizeChange=true;
		
		this.txtHeight = (this.textbuffer!=null) ? this.textbuffer.getHeight() : 0;
		this.txtWidth = (this.textbuffer!=null) ? this.textbuffer.getWidth() : 0;
		
		this.bufrender.compileBackground((int)this.bufferRenderWidth, (int)this.bufferRenderHeight);
		
		this.buttonList.add(new ImageButton(0, this.guiLeft+5, 5+this.guiTop+offset, 18, 18, null, textureOnOffButton, true));
	}
	
	private void initComponents(Iterable<ManagedEnvironment> iterable){
		Iterator<ManagedEnvironment> list = iterable.iterator();
		while(list.hasNext()){
			ManagedEnvironment env = list.next();
			System.out.println("Component");
			if(env instanceof TextBuffer) this.textbuffer = (TextBuffer) env;
		}
	}

	@Override
	public boolean doesGuiPauseGame(){
		return false;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor3d(1F, 1F, 1F);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture((container.getHasScreen())? textureScreen : textureNoScreen );
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
	}
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int offset = (this.container.getHasScreen()) ? ComputerCartContainer.DELTA : 0;
		IIcon non = SlotIcons.fromTier(-1);
		this.mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
        this.drawTexturedModelRectFromIcon(170, 84+offset, non, 16, 16);
        
        if(this.container.getHasScreen() && this.textbuffer!=null){
        	this.drawBufferLayer();
        
        	double bw = this.txtWidth * TextBufferRenderCache.renderer().charRenderWidth();
        	double bh = this.txtHeight * TextBufferRenderCache.renderer().charRenderHeight();
        	double scaleX = Math.min(this.bufferRenderWidth / bw , 1);
        	double scaleY = Math.min(this.bufferRenderHeight / bh , 1);
        	this.bufferscale = Math.min(scaleX, scaleY);
        }
	}
	
	private void drawBufferLayer(){
	    GL11.glPushMatrix();
	    GL11.glTranslatef(bufferX, bufferY, 0);
	    Minecraft.getMinecraft().entityRenderer.disableLightmap(0);
	    RenderHelper.disableStandardItemLighting();
	    GL11.glPushMatrix();
	    GL11.glTranslatef(-3, -3, 0);
	    GL11.glColor4f(1, 1, 1, 1);
	    this.bufrender.drawBackground();
	    GL11.glPopMatrix();
	    GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    double scaleX = bufferRenderWidth / this.textbuffer.renderWidth();
	    double scaleY = bufferRenderHeight / this.textbuffer.renderHeight();
	    double scale = Math.min(scaleX, scaleY);
	      if (scaleX > scale) {
	        GL11.glTranslated(this.textbuffer.renderWidth() * (scaleX - scale) / 2, 0, 0);
	      }
	      else if (scaleY > scale) {
	        GL11.glTranslated(0, this.textbuffer.renderHeight() * (scaleY - scale) / 2, 0);
	      }
	      GL11.glScaled(scale, scale, scale);
	      GL11.glScaled(this.bufferscale, this.bufferscale, 1);
	      this.bufrender.drawText(this.textbuffer);
	    
	    GL11.glPopMatrix();
	}
}
