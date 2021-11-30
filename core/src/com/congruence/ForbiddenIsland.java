package com.congruence;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.congruence.start.StartMenu;
import com.congruence.start.StartScreen;
import com.congruence.state.*;
import com.congruence.ui.GameUI;
import com.congruence.ui.IslandTile;
import com.congruence.util.GameInitializeListener;
import com.congruence.util.GameStartListener;

import java.util.*;

public class ForbiddenIsland extends Game {

	private StartScreen startScreen;

	private StartMenu startMenu;

	public static Random random = new Random();

	public static String[][] TEST_ISLAND_TILE_DATA = {
			{ null, null, "Misty-Marsh", "Misty-Marsh", null, null },
			{ null, null, "Misty-Marsh", "Misty-Marsh", null, null },
			{ "Misty-Marsh", "Misty-Marsh", "Misty-Marsh", "Misty-Marsh", "Misty-Marsh", "Misty-Marsh" },
			{ "Misty-Marsh", "Misty-Marsh", "Misty-Marsh", "Misty-Marsh", "Misty-Marsh", "Misty-Marsh" },
			{ null, null, "Misty-Marsh", "Misty-Marsh", null, null },
			{ null, null, "Misty-Marsh", "Misty-Marsh", null, null },
	};

	public static int[][] TEST_ISLAND_TILE_STATE = {
			{ -1, -1, 1, 1, -1, -1 },
			{ -1, -1, 1, 1, -1, -1 },
			{ 1, 1, 0, 1, 2, 1 },
			{ 1, 1, 1, 1, 0, 1 },
			{ -1, -1, 1, 1, -1, -1 },
			{ -1, -1, 1, 1, -1, -1 },
	};

	private GameInitializeListener gameInitializeListener = new GameInitializeListener() {
		@Override
		public void onInitialize() {
			HashMap<String, Player> players = new HashMap<>();
			HashMap<Integer, String> playerOrder = new HashMap<>();
			String[][] islandTiles = new String[6][6];
			int[][] islandTileState = new int[6][6];
			Stack<FloodCard> floodCardDeck = new Stack<>();
			Stack<TreasureCard> treasureCardDeck = new Stack<>();

			int numPlayers = Integer.parseInt(startMenu.getNumPlayers());
			String difficulty = startMenu.getDifficulty();
			int seed = Integer.parseInt(startMenu.getSeed());

			playerOrder.put(0, "Player 1");
			playerOrder.put(1, "Player 2");
			playerOrder.put(2, "Player 3");
			playerOrder.put(3, "Player 4");

			//placeholder test data for now
			players.put("Player 1", new Player(
					"Player 1",
					"idk lmao",
					new ArrayList<>(),
					2,
					2,
					Player.EXPLORER
			));
			players.put("Player 2", new Player(
					"Player 2",
					"idk lmao",
					new ArrayList<>(),
					3,
					2,
					Player.PILOT
			));
			players.put("Player 3", new Player(
					"Player 3",
					"idk lmao",
					new ArrayList<>(),
					2,
					3,
					Player.NAVIGATOR
			));
			players.put("Player 4", new Player(
					"Player 4",
					"idk lmao",
					new ArrayList<>(),
					3,
					3,
					Player.MESSENGER
			));

			LinkedList<String> pickedIslandTiles = new LinkedList<>();
			pickedIslandTiles.add("Fools Landing");
			pickedIslandTiles.add("Temple of the Moon");
			pickedIslandTiles.add("Temple of the Sun");
			pickedIslandTiles.add("Whispering Garden");
			pickedIslandTiles.add("Howling Garden");
			pickedIslandTiles.add("Cave of Embers");
			pickedIslandTiles.add("Cave of Shadows");
			pickedIslandTiles.add("Coral Palace");
			pickedIslandTiles.add("Tidal Palace");
			int tilesLeft = 15;
			while (tilesLeft > 0) {
				int rand = random.nextInt(Resources.IslandTiles.length);
				if (!pickedIslandTiles.contains(Resources.IslandTiles[rand])) {
					pickedIslandTiles.add(Resources.IslandTiles[rand]);
					tilesLeft--;
				}
			}
			Collections.shuffle(pickedIslandTiles);

			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 6; j++) {
					if (Resources.DefaultTileOrdering.contains(i + "" + j)) {
						islandTiles[i][j] = pickedIslandTiles.pop();
						islandTileState[i][j] = GameState.NORMAL_ISLAND_TILE;
					}
					else {
						islandTiles[i][j] = null;
						islandTileState[i][j] = -1;
					}
				}
			}

			gameUI = new GameUI(new GameState(
					GameState.NOVICE,
					players,
					playerOrder,
					islandTiles,
					islandTileState,
					floodCardDeck,
					treasureCardDeck
			));
			setScreen(gameUI);
		}
	};

	private GameUI gameUI;

	public ForbiddenIsland() {
		super();

		startScreen = new StartScreen();
		startScreen.addGameStartListeners(new GameStartListener() {
			@Override
			public void gameStarted() {
				startMenu = new StartMenu();
				startMenu.addGameStartListeners(gameInitializeListener);
				setScreen(startMenu);
			}
		});
	}

	@Override
	public void create() {
		setScreen(startScreen);
	}

}
