package com.congruence.state;

import java.util.*;

import static java.util.List.*;

public class Resources {

    public static final String[] PLAYER_NAMES = {
            "Engineer",
            "Diver",
            "Navigator",
            "Explorer",
            "Messenger",
            "Pilot"
    };

    public static final LinkedList<String> IslandTiles = new LinkedList<>(Arrays.asList(
            "Breakers Bridge",
            "Bronze Gate",
            "Cave of Embers",
            "Cave of Shadows",
            "Cliffs of Abandon",
            "Copper Gate",
            "Coral Palace",
            "Crimson Forest",
            "Dunes of Deception",
            "Fools Landing",
            "Gold Gate",
            "Howling Garden",
            "Iron Gate",
            "Lost Lagoon",
            "Misty Marsh",
            "Observatory",
            "Phantom Rock",
            "Silver Gate",
            "Temple of the Moon",
            "Temple of the Sun",
            "Tidal Palace",
            "Twilight Hollow",
            "Watchtower",
            "Whispering Garden"
    ));

    public static final TreeSet<String> DefaultTileOrdering = new TreeSet<>();

    public static final String[] ArtifactNames = {
            "The Crystal of Fire",
            "The Earth Stone",
            "The Ocean's Chalice",
            "The Statue of the Wind"
    };

    public static final TreeMap<String, String> DefaultArtifactMapPlacement = new TreeMap<>();

    static {
        DefaultTileOrdering.add("02");
        DefaultTileOrdering.add("03");
        DefaultTileOrdering.add("11");
        DefaultTileOrdering.add("12");
        DefaultTileOrdering.add("13");
        DefaultTileOrdering.add("14");
        DefaultTileOrdering.add("20");
        DefaultTileOrdering.add("21");
        DefaultTileOrdering.add("22");
        DefaultTileOrdering.add("23");
        DefaultTileOrdering.add("24");
        DefaultTileOrdering.add("25");
        DefaultTileOrdering.add("30");
        DefaultTileOrdering.add("31");
        DefaultTileOrdering.add("32");
        DefaultTileOrdering.add("33");
        DefaultTileOrdering.add("34");
        DefaultTileOrdering.add("35");
        DefaultTileOrdering.add("41");
        DefaultTileOrdering.add("42");
        DefaultTileOrdering.add("43");
        DefaultTileOrdering.add("44");
        DefaultTileOrdering.add("52");
        DefaultTileOrdering.add("53");

        DefaultArtifactMapPlacement.put("05", "The Ocean's Chalice");
        DefaultArtifactMapPlacement.put("00", "The Crystal of Fire");
        DefaultArtifactMapPlacement.put("55", "The Statue of the Wind");
        DefaultArtifactMapPlacement.put("50", "The Earth Stone");
    }
}
