package com.congruence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.congruence.start.StartMenu;
import com.congruence.start.StartScreen;
import com.congruence.state.*;
import com.congruence.ui.GameUI;
import com.congruence.util.GameInitializeListener;
import com.congruence.util.GameStartListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ForbiddenIsland extends Game {

	private static final Logger logger = LoggerFactory.getLogger(ForbiddenIsland.class);

	private StartScreen startScreen;

	private StartMenu startMenu;

	public static Random random = new Random();

	public static AssetManager assetManager;

	public static LinkedList<String> islandTilesUsed;

	private GameInitializeListener gameInitializeListener = new GameInitializeListener() {
		@Override
		public void onInitialize(int NumberOfPlayers, int Difficulty) {
			random = new Random();
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

			int WaterHeight = 1;

			playerOrder.put(0, TEMP_PLAYER_CARDS.peek());
			players.put(TEMP_PLAYER_CARDS.peek(), new Player(
					TEMP_PLAYER_CARDS.pop(),
					null,
					new ArrayList<>(),
					1,
					1
			));
			playerOrder.put(1, TEMP_PLAYER_CARDS.peek());
			players.put(TEMP_PLAYER_CARDS.peek(), new Player(
					TEMP_PLAYER_CARDS.pop(),
					null,
					new ArrayList<>(),
					1,
					4
			));
			if (NumberOfPlayers > 2) {
				playerOrder.put(2, TEMP_PLAYER_CARDS.peek());
				players.put(TEMP_PLAYER_CARDS.peek(), new Player(
						TEMP_PLAYER_CARDS.pop(),
						null,
						new ArrayList<>(),
						4,
						4
				));
			}
			if (NumberOfPlayers > 3) {
				playerOrder.put(3, TEMP_PLAYER_CARDS.peek());
				players.put(TEMP_PLAYER_CARDS.peek(), new Player(
						TEMP_PLAYER_CARDS.pop(),
						null,
						new ArrayList<>(),
						4,
						1
				));
			}
			logger.info(NumberOfPlayers + "");

			for (int i = 0; i < 5; i++) {
				treasureCardDeck.add(new TreasureCard("Ocean's Chalice", TreasureCard.TREASURE_CARD));
				treasureCardDeck.add(new TreasureCard("Crystal of Fire", TreasureCard.TREASURE_CARD));
				treasureCardDeck.add(new TreasureCard("Statue of the Wind", TreasureCard.TREASURE_CARD));
				treasureCardDeck.add(new TreasureCard("Earth Stone", TreasureCard.TREASURE_CARD));
			}

			treasureCardDeck.add(new TreasureCard("Helicopter", TreasureCard.HELICOPTER_CARD));
			treasureCardDeck.add(new TreasureCard("Helicopter", TreasureCard.HELICOPTER_CARD));
			treasureCardDeck.add(new TreasureCard("Helicopter", TreasureCard.HELICOPTER_CARD));

			treasureCardDeck.add(new TreasureCard("Sandbag", TreasureCard.SANDBAG_CARD));
			treasureCardDeck.add(new TreasureCard("Sandbag", TreasureCard.SANDBAG_CARD));

			Collections.shuffle(treasureCardDeck);

			for (Player e: players.values()) {
				e.getCardsAtHand().add(treasureCardDeck.pop());
				e.getCardsAtHand().add(treasureCardDeck.pop());
			}

			treasureCardDeck.add(new TreasureCard("Waters Rise", TreasureCard.WATERS_RISE_CARD));
			treasureCardDeck.add(new TreasureCard("Waters Rise", TreasureCard.WATERS_RISE_CARD));
			treasureCardDeck.add(new TreasureCard("Waters Rise", TreasureCard.WATERS_RISE_CARD));

			Collections.shuffle(treasureCardDeck);


			LinkedList<String> pickedIslandTiles = new LinkedList<>();

			int tilesLeft = 24;
			ArrayList<String> tempIslandTiles = new ArrayList<>(Resources.IslandTiles);
			while (tilesLeft > 0) {
				logger.info("tempIslandTiles.size(): " + tempIslandTiles.size() + " tilesLeft: " + tilesLeft);
				int rand = random.nextInt(tempIslandTiles.size());
				logger.info("rand: " + rand + " tile: " + tempIslandTiles.get(rand));
				pickedIslandTiles.add(tempIslandTiles.remove(rand));
				tilesLeft--;
			}
			ForbiddenIsland.islandTilesUsed = new LinkedList<>();
			islandTilesUsed.addAll(pickedIslandTiles);

			LinkedList<String> floodedTileCards = new LinkedList<>();
			ArrayList<FloodCard> floodCards = new ArrayList<>();
			List<String> tileIslandCardTemp = new ArrayList<>(Resources.DefaultTileOrdering);
			Iterator<String> setIterator = tileIslandCardTemp.iterator();
			Collections.shuffle(tileIslandCardTemp);
			for (int i = 0; i < 6; i++) {
				floodedTileCards.add( setIterator.next() );
			}
			Collections.shuffle(pickedIslandTiles);
			logger.info(pickedIslandTiles.toString());
			Collections.shuffle(floodedTileCards);
			logger.info(floodedTileCards.toString());
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 6; j++) {
					if (Resources.DefaultTileOrdering.contains(i + "" + j)) {
						islandTiles[i][j] = pickedIslandTiles.peek();
						if (floodedTileCards.contains(i + "" + j)) {
							islandTileState[i][j] = GameState.FLOODED_ISLAND_TILE;
							floodCards.add(new FloodCard(pickedIslandTiles.peek(),null, FloodCard.ISLAND_CARD));
							logger.info(pickedIslandTiles.peek());
						}
						pickedIslandTiles.pop();
					}
					else {
						islandTiles[i][j] = null;
						islandTileState[i][j] = -1;
					}
				}
			}

			for (String e : ForbiddenIsland.islandTilesUsed) {
				floodCardDeck.add(new FloodCard(e, null, FloodCard.ISLAND_CARD));
				floodCardDeck.add(new FloodCard(e, null, FloodCard.ISLAND_CARD));
			}

			Collections.shuffle(floodCardDeck);


			Screen LoadingScreen = new Screen() {
				@Override
				public void show() {

				}

				@Override
				public void render(float delta) {
					Gdx.gl.glClearColor(18 / 255f, 18 / 255f, 18 / 255f, 1);
					Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
					assetManager.update();
					if (assetManager.update())
						setScreen(gameUI);
					logger.info(assetManager.getProgress()+"");
				}

				@Override
				public void resize(int width, int height) {

				}

				@Override
				public void pause() {

				}

				@Override
				public void resume() {

				}

				@Override
				public void hide() {

				}

				@Override
				public void dispose() {

				}
			};
			setScreen(LoadingScreen);
			assetManager.finishLoading();

			if (Difficulty == GameState.NORMAL) {
				WaterHeight = 2;
			}
			else if (Difficulty == GameState.ELITE) {
				WaterHeight = 3;
			}
			else if (Difficulty == GameState.LEGENDARY) {
				WaterHeight = 4;
			}

			gameUI = new GameUI(new GameState(
					Difficulty,
					players,
					playerOrder,
					islandTiles,
					islandTileState,
					floodCardDeck,
					treasureCardDeck,
					WaterHeight,
					floodCards
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
		loadAsserts();
	}

	@Override
	public void dispose() {
		assetManager.dispose();
	}

	private void loadAsserts() {
		assetManager = new AssetManager(new InternalFileHandleResolver());
		TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter();
		textureParameter.genMipMaps = true;
		Array<String> assetDirectories = new Array<>();
		FileHandleResolver resolver = new InternalFileHandleResolver();
		assetDirectories.add("artifacts");
		assetDirectories.add("ability-icon");
		assetDirectories.add("artifacts");
		assetDirectories.add("custom-ui");
		assetDirectories.add("flood-deck");
		assetDirectories.add("treasure-deck");
		assetDirectories.add("island-tiles");
		assetDirectories.add("water-meter");
		assetDirectories.add("pawn");
		for (String folder : assetDirectories) {
			FileHandle handle =  resolver.resolve(folder);
			//logger.info("Path " + resolver.resolve(folder).path() + " List size " + handle.list().length);
			for (FileHandle asset : handle.list()) {
				FileHandle folderSub = resolver.resolve(asset.path());
				if (folderSub.isDirectory()) {
					for (FileHandle assetSub : folderSub.list()) {
						//logger.info(assetSub.path());
						assetManager.load(assetSub.path(), Texture.class, textureParameter);
						//logger.info(assetManager.getProgress() + "");
					}
				}
				else {
					//logger.info(asset.path());
					logger.info("Loading asset: " + asset.path());
					assetManager.load(asset.path(), Texture.class, textureParameter);
					//logger.info(assetManager.getProgress() + "");
				}
			}
		}
	}
}
