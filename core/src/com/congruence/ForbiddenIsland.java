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
		public void onInitialize(int NumberOfPlayers, int Difficulty) {
			HashMap<String, Player> players = new HashMap<>();
			HashMap<Integer, String> playerOrder = new HashMap<>();
			String[][] islandTiles = new String[6][6];
			int[][] islandTileState = new int[6][6];
			Stack<FloodCard> floodCardDeck = new Stack<>();
			Stack<TreasureCard> treasureCardDeck = new Stack<>();

			Stack<String> TEMP_PLAYER_CARDS = new Stack<>();
			List<String> TEMP_PLAYER_CARDS_LIST = Arrays.asList(Resources.PLAYER_NAMES);
			Collections.shuffle(TEMP_PLAYER_CARDS_LIST);
			TEMP_PLAYER_CARDS.addAll(TEMP_PLAYER_CARDS_LIST);

			playerOrder.put(0, TEMP_PLAYER_CARDS.peek());
			players.put(TEMP_PLAYER_CARDS.peek(), new Player(
					TEMP_PLAYER_CARDS.pop(),
					null,
					null,
					0,
					0
			));
			playerOrder.put(1, TEMP_PLAYER_CARDS.peek());
			players.put(TEMP_PLAYER_CARDS.peek(), new Player(
					TEMP_PLAYER_CARDS.pop(),
					null,
					null,
					0,
					0
			));
			playerOrder.put(2, TEMP_PLAYER_CARDS.peek());
			players.put(TEMP_PLAYER_CARDS.peek(), new Player(
					TEMP_PLAYER_CARDS.pop(),
					null,
					null,
					0,
					0
			));
			playerOrder.put(3, TEMP_PLAYER_CARDS.peek());
			players.put(TEMP_PLAYER_CARDS.peek(), new Player(
					TEMP_PLAYER_CARDS.pop(),
					null,
					null,
					0,
					0
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
						islandTileState[i][j] = GameState.FLOODED_ISLAND_TILE;
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
