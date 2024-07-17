package plancher.features;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class RouteWalker {

	BlockPos[] route = { new BlockPos(2, 2, 2), new BlockPos(30, 50, 30) };
	
	BlockPos goalPos = null;
	int routeIndex = 0;
	
	boolean routeWalker = false;

	public void onTick(ClientTickEvent e) {
		if (routeWalker) {
			Minecraft mc = Minecraft.func_71410_x();
			if (mc.field_71439_g != null) {

			}
		}
	}
}
