package com.congruence.desktop;


import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.congruence.ForbiddenIsland;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Forbidden Island");
		config.setWindowedMode(1280, 720);
		config.setResizable(true);
		config.setMaximized(true);
		config.disableAudio(true);

		config.setWindowIcon("Icon-Windows.png");
		new Lwjgl3Application(new ForbiddenIsland(), config);
	}
}
