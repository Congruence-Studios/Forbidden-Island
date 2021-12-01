package com.congruence;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.congruence.start.StartMenu;
import com.congruence.start.StartScreen;
import com.congruence.state.*;
import com.congruence.ui.GameUI;
import com.congruence.ui.IslandTile;
import com.congruence.ui.PlayerHand;
import com.congruence.util.GameInitializeListener;
import com.congruence.util.GameStartListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ForbiddenIsland extends Game {

	private static final Logger logger = LoggerFactory.getLogger(ForbiddenIsland.class);

	private StartScreen startScreen;

	private StartMenu startMenu;

	public static Random random = new Random();

	public static AssetManager assetManager;

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
					null,
					2,
					2
			));
			playerOrder.put(1, TEMP_PLAYER_CARDS.peek());
			players.put(TEMP_PLAYER_CARDS.peek(), new Player(
					TEMP_PLAYER_CARDS.pop(),
					null,
					null,
					2,
					1
			));
			playerOrder.put(2, TEMP_PLAYER_CARDS.peek());
			players.put(TEMP_PLAYER_CARDS.peek(), new Player(
					TEMP_PLAYER_CARDS.pop(),
					null,
					null,
					1,
					2
			));
			playerOrder.put(3, TEMP_PLAYER_CARDS.peek());
			players.put(TEMP_PLAYER_CARDS.peek(), new Player(
					TEMP_PLAYER_CARDS.pop(),
					null,
					null,
					1,
					1
			));

			LinkedList<String> pickedIslandTiles = new LinkedList<>();
			int tilesLeft = 24;
			ArrayList<String> tempIslandTiles = (ArrayList<String>) Resources.IslandTiles.clone();
			while (tilesLeft > 0) {
				logger.info("tempIslandTiles.size(): " + tempIslandTiles.size() + " tilesLeft: " + tilesLeft);
				int rand = random.nextInt(tempIslandTiles.size());
				logger.info("rand: " + rand + " tile: " + tempIslandTiles.get(rand));
				pickedIslandTiles.add(tempIslandTiles.remove(rand));
				tilesLeft--;
			}
			LinkedList<String> floodedTileCards = new LinkedList<>();
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
						islandTiles[i][j] = pickedIslandTiles.pop();
						if (floodedTileCards.contains(i + "" + j)) {
							islandTileState[i][j] = GameState.FLOODED_ISLAND_TILE;
						}
					}
					else {
						islandTiles[i][j] = null;
						islandTileState[i][j] = -1;
					}
				}
			}


			assetManager = new AssetManager(new InternalFileHandleResolver());
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
			for (String folder : assetDirectories) {
				FileHandle handle = Gdx.files.internal("./desktop/assets/" + resolver.resolve(folder).path());
				logger.info("Path " + resolver.resolve(folder).path() + " List size " + handle.path());
				for (FileHandle asset : handle.list()) {
					FileHandle folderSub = resolver.resolve(asset.path());
					logger.info(asset.name());
					if (folderSub.isDirectory()) {
						for (FileHandle assetSub : folderSub.list()) {
							logger.info(assetSub.path());
							assetManager.load(assetSub.path(), Texture.class);
							logger.info(assetManager.getProgress() + "");
						}
					}
					else {
						logger.info(asset.path());
						assetManager.load(asset.path(), Texture.class);
						logger.info(assetManager.getProgress() + "");
					}
				}
			}

			Screen LoadingScreen = new Screen() {
				@Override
				public void show() {

				}

				@Override
				public void render(float delta) {
					assetManager.update();
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

			gameUI = new GameUI(new GameState(
					Difficulty,
					players,
					playerOrder,
					islandTiles,
					islandTileState,
					floodCardDeck,
					treasureCardDeck,
					WaterHeight
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

	@Override
	public void dispose() {
		assetManager.dispose();
	}

}
