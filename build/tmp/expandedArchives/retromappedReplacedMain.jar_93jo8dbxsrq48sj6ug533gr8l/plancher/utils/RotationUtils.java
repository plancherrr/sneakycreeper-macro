package plancher.utils;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import plancher.features.Creeper;

public class RotationUtils {
	private static float pitchDifference;
	public static float yawDifference;
	private static int ticks = -1;
	private static int tickCounter = 0;
	private static Runnable callback = null;

	public static class Rotation {
		public float pitch;
		public float yaw;

		public Rotation(float pitch, float yaw) {
			this.pitch = pitch;
			this.yaw = yaw;
		}
	}

	private static double wrapAngleTo180(double angle) {
		return angle - Math.floor(angle / 360 + 0.5) * 360;
	}

	private static float wrapAngleTo180(float angle) {
		return (float) (angle - Math.floor(angle / 360 + 0.5) * 360);
	}

	public static Rotation vec3ToRotation(Vec3 vec) {
		Minecraft mc = Minecraft.func_71410_x();
		double diffX = vec.field_72450_a - mc.field_71439_g.field_70165_t;
		double diffY = vec.field_72448_b - mc.field_71439_g.field_70163_u - mc.field_71439_g.func_70047_e();
		double diffZ = vec.field_72449_c - mc.field_71439_g.field_70161_v;
		double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

		float pitch = (float) -Math.atan2(dist, diffY);
		float yaw = (float) Math.atan2(diffZ, diffX);
		pitch = (float) wrapAngleTo180((pitch * 180F / Math.PI + 90) * -1);
		yaw = (float) wrapAngleTo180((yaw * 180 / Math.PI) - 90);

		return new Rotation(pitch, yaw);
	}

	public static Rotation getRotationToBlock(BlockPos block) {
		double diffX = block.func_177958_n() - Minecraft.func_71410_x().field_71439_g.field_70165_t + 0.5;
		double diffY = block.func_177956_o() - Minecraft.func_71410_x().field_71439_g.field_70163_u + 0.5
				- Minecraft.func_71410_x().field_71439_g.func_70047_e();
		double diffZ = block.func_177952_p() - Minecraft.func_71410_x().field_71439_g.field_70161_v + 0.5;
		double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

		float pitch = (float) -Math.atan2(dist, diffY);
		float yaw = (float) Math.atan2(diffZ, diffX);
		pitch = (float) wrapAngleTo180((pitch * 180F / Math.PI + 90) * -1);
		yaw = (float) wrapAngleTo180((yaw * 180 / Math.PI) - 90);

		return new Rotation(pitch, yaw);
	}

	public static Rotation getRotationToBlock(BlockPos block, float offset) {
		double diffX = block.func_177958_n() - Minecraft.func_71410_x().field_71439_g.field_70165_t + 0.5;
		double diffY = block.func_177956_o() - Minecraft.func_71410_x().field_71439_g.field_70163_u + 0.5
				- Minecraft.func_71410_x().field_71439_g.func_70047_e();
		double diffZ = block.func_177952_p() - Minecraft.func_71410_x().field_71439_g.field_70161_v + 0.5;
		double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

		float pitch = (float) -Math.atan2(dist, diffY);
		float yaw = (float) Math.atan2(diffZ, diffX);
		pitch = (float) wrapAngleTo180((pitch * 180F / Math.PI + 90) * -1);
		yaw = (float) wrapAngleTo180((yaw * 180 / Math.PI) - 90);

		Random rdm = new Random();
		return new Rotation((17.5f + rdm.nextFloat() * (22.0F - 17.5F)), yaw);
	}

	public static void smoothLook(Rotation rotation, int ticks, Runnable callback) {

		if (ticks == 0) {
			look(rotation);
			if (callback != null)
				callback.run();
			return;
		}

		RotationUtils.callback = callback;

		pitchDifference = wrapAngleTo180(rotation.pitch - Minecraft.func_71410_x().field_71439_g.field_70125_A);
		yawDifference = wrapAngleTo180(rotation.yaw - Minecraft.func_71410_x().field_71439_g.field_70177_z);

		RotationUtils.ticks = ticks * 20;
		RotationUtils.tickCounter = 0;
	}

	public static void smartLook(Rotation rotation, int ticksPer180, Runnable callback) {
		float rotationDifference = Math.max(Math.abs(rotation.pitch - Minecraft.func_71410_x().field_71439_g.field_70125_A),
				Math.abs(rotation.yaw - Minecraft.func_71410_x().field_71439_g.field_70177_z));
		smoothLook(rotation, (int) (rotationDifference / 180 * ticksPer180), callback);
	}

	public static void look(Rotation rotation) {
		Minecraft.func_71410_x().field_71439_g.field_70125_A = rotation.pitch;
		Minecraft.func_71410_x().field_71439_g.field_70177_z = rotation.yaw;
	}

	@SubscribeEvent
	public void onTick(TickEvent event) {
		if (tickCounter < ticks) {
			Minecraft.func_71410_x().field_71439_g.field_70125_A += pitchDifference / ticks;
			Minecraft.func_71410_x().field_71439_g.field_70177_z += yawDifference / ticks;
			tickCounter++;
		} else if (callback != null) {
			callback.run();
			callback = null;
		}
	}
}
