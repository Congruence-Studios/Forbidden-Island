package com.congruence.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.congruence.ForbiddenIsland;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Forbidden Island";
		config.width = 1280;
		config.height = 720;
		config.resizable = false;

		config.addIcon("Icon-Windows.png", Files.FileType.Internal);
		new LwjglApplication(new ForbiddenIsland(), config);
	}
}
