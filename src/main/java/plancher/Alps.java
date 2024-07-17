package plancher;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommand;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import plancher.commands.LoginCommand;
import plancher.commands.MenuCommand;
import plancher.features.AntiEscrowRefund;
import plancher.features.Creeper;
import plancher.features.PestEsp;
import plancher.ui.GuiAlps;
import plancher.ui.LoginGui;
import plancher.utils.ChatUtils;
import plancher.utils.FontUtils;
import plancher.utils.RotationUtils;
import plancher.utils.ScoreboardUtils;

@Mod(modid = "alpsopts")
public class Alps {

	public static LoginGui loginGui = new LoginGui();
	public static GuiAlps alpsGui = new GuiAlps();

	KeyBinding mute = new KeyBinding("Mute the game", Keyboard.KEY_M, "Alps");

	boolean muted = false;
	float soundLevel = 50;
	private SoundCategory soundCategory = SoundCategory.MASTER;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		registerCommands(new LoginCommand());
		registerEvents(new RotationUtils(), new Creeper());
		registerKeybinds(mute, Creeper.creeperKey, Creeper.addToclip);
		changeLogo(); // todo: fix that

	}

	@SubscribeEvent
	public void onTick(ClientTickEvent e) {
		if (e.phase == Phase.END) {
			if (LoginGui.isGui()) {
				Minecraft.getMinecraft().displayGuiScreen(loginGui);
				loginGui.setGui(false);
			} else if (GuiAlps.isGui()) {
				Minecraft.getMinecraft().displayGuiScreen(alpsGui);
				GuiAlps.setGui(false);
			}
			Display.setTitle("Minecraft 1.8.9 | FPS : " + String.valueOf(Minecraft.getDebugFPS()));

		}
	}

	@SubscribeEvent
	public void onKey(KeyInputEvent e) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mute.isPressed()) {
			if (!muted) {
				soundLevel = mc.gameSettings.getSoundLevel(soundCategory);
			}
			muted = !muted;
			if (muted) {
				mc.gameSettings.setSoundLevel(soundCategory, 0);
			} else {
				mc.gameSettings.setSoundLevel(soundCategory, soundLevel);

			}

		} else if (Creeper.creeperKey.isPressed()) {

			Creeper.setCreeperMacro(!Creeper.isCreeperMacro());
			if (Creeper.isCreeperMacro())
				Creeper.start();
			else
				Creeper.stop();
		} else if (Creeper.addToclip.isPressed()) {
			EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
			String x = String.valueOf((int) p.posX);
			String y = String.valueOf((int) p.posY - 1);
			String z = String.valueOf((int) p.posZ);
			Creeper.copiedList.add(new BlockPos(p.posX, p.posY - 1, p.posZ));
			copyToClip("new BlockPos(" + x + ", " + y + ", " + z + "),");
			ChatUtils.clientMessage(
					"Copied " + EnumChatFormatting.WHITE + "new BlockPos(" + x + ", " + y + ", " + z + ")");
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

	private static void registerCommands(ICommand... commands) {
		for (ICommand o : commands) {
			ClientCommandHandler.instance.registerCommand(o);
		}
	}

	private static void registerKeybinds(KeyBinding... keys) {
		for (KeyBinding key : keys) {
			ClientRegistry.registerKeyBinding(key);
		}
	}

	private static void registerEvents(Object... classToReg) {
		for (Object o : classToReg) {
			MinecraftForge.EVENT_BUS.register(o);
		}
	}

	private void changeLogo() {
		InputStream inputStream16 = null;
		InputStream inputStream32 = null;
		try {
			inputStream16 = Minecraft.getMinecraft().mcDefaultResourcePack
					.getInputStreamAssets(new ResourceLocation("alpssmall.png"));
			inputStream32 = Minecraft.getMinecraft().mcDefaultResourcePack
					.getInputStreamAssets(new ResourceLocation("alpsbig.png"));

			if (inputStream16 == null || inputStream32 == null)
				return;

			System.out.println("Set icon to old one!");
			Display.setIcon(new ByteBuffer[] { readImageToBuffer(inputStream16), readImageToBuffer(inputStream32) });
		} catch (IOException ioexception) {
			IOUtils.closeQuietly(inputStream16);
			IOUtils.closeQuietly(inputStream32);
			ioexception.printStackTrace();
		}

	}

	private ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException {
		BufferedImage bufferedimage = ImageIO.read(imageStream);
		int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0,
				bufferedimage.getWidth());
		ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
		for (int i : aint) {
			bytebuffer.putInt(i << 8 | i >> 24 & 0xFF);
		}
		bytebuffer.flip();
		return bytebuffer;
	}

	@SubscribeEvent
	public void onOverlayRender(RenderGameOverlayEvent.Post event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.thePlayer == null)
			return;
		if (event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE)
			return;
		if (!muted)
			return;
		GL11.glPushMatrix();
		GL11.glScalef(1.5F, 1.5F, 1.5F);
		FontUtils.drawCenteredString("The game is currently muted", 300, 10, false);
		GL11.glPopMatrix();
	}

	private static void copyToClip(String s) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection stringSelection = new StringSelection(s);
		clipboard.setContents(stringSelection, null);
	}
}
