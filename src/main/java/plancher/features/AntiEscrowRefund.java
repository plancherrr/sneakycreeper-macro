package plancher.features;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import plancher.utils.ChatUtils;

public class AntiEscrowRefund {

	public static String[] ranks = { "[VIP]", "[VIP+]", "[MVP]", "[MVP+]", "[MVP++]" };
	public static boolean escrowRefund = false;
	boolean shouldPressAh = false;
	int count = 0;

	@SubscribeEvent
	public void onChatReceived(ClientChatReceivedEvent event) {
		String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
		for (String str : ranks) {
			str = message;
			if (message.contains(str))
				return;
		}
		if (message.toLowerCase().contains("Escrow refunded")
				&& message.toLowerCase().contains("coins for BIN Auction Buy!")) {
			escrowRefund = true;
		}

	}

	@SubscribeEvent
	public void onTick(ClientTickEvent e) {
		if (e.phase != Phase.END)
			return;
		if (escrowRefund) {
			count++;
			if (count == 9) {
				escrowRefund = false;
				ChatUtils.serverMessage("/ah");
			} else if (count == 16) {
				count = 0;
				// Minecraft.getMinecraft().playerController.windowClick(0, count, count, count,
				// null);
			}
		}
	}
}
