package plancher.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MouseHelper;
import org.lwjgl.input.Mouse;

public class UngrabUtils {

	
	/*
	 * STOLEN FROM MIGHTY MINER AHAH
	 */
	public static boolean isUngrabbed = false;
	private static MouseHelper oldMouseHelper;
	private static boolean doesGameWantUngrabbed;

	public static void ungrabMouse() {
		Minecraft m = Minecraft.func_71410_x();
		if (isUngrabbed)
			return;
		m.field_71474_y.field_82881_y = false;
		if (oldMouseHelper == null)
			oldMouseHelper = m.field_71417_B;
		doesGameWantUngrabbed = !Mouse.isGrabbed();
		oldMouseHelper.func_74373_b();
		m.field_71415_G = true;
		m.field_71417_B = new MouseHelper() {
			@Override
			public void func_74374_c() {
			}

			@Override
			public void func_74372_a() {
				doesGameWantUngrabbed = false;
			}

			@Override
			public void func_74373_b() {
				doesGameWantUngrabbed = true;
			}
		};
		isUngrabbed = true;
	}

	/**
	 * This function performs all the steps required to regrab the mouse.
	 */
	public static void regrabMouse() {
		if (!isUngrabbed)
			return;
		Minecraft m = Minecraft.func_71410_x();
		m.field_71417_B = oldMouseHelper;
		if (!doesGameWantUngrabbed)
			m.field_71417_B.func_74372_a();
		oldMouseHelper = null;
		isUngrabbed = false;
	}
}