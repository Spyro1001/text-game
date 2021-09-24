package com.example.game.util;

import com.example.game.world.GameWorld;
import com.example.game.model.Direction;
import com.example.game.model.Exit;
import com.example.game.model.Location;
import com.example.game.model.character.Bear;
import com.example.game.model.character.Troll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameInitializerFileReader {

    private static final Logger logger = LoggerFactory.getLogger(GameInitializer.class.getSimpleName());

    private GameInitializerFileReader() {

    }

    public static GameWorld initializeGame() {

        GameWorld game = new GameWorld();

        // load location data
        Stream<String> directionStream = null;
        try {
            Path path = Paths.get(GameInitializerFileReader.class.getClassLoader()
                    .getResource("locations.psv")
                    .toURI());
            directionStream = Files.lines(path);

        } catch (URISyntaxException | IOException e) {
            logger.error("There was an error reading the locations.", e);
        }

        Map<String, Location> locations = directionStream.map(line -> {

            StringTokenizer tokenizer = new StringTokenizer(line, "|");

            String[] locationArr = new String[3];
            int index = 0;
            while (tokenizer.hasMoreTokens()) {
                locationArr[index++] = tokenizer.nextToken();
            }

            return new Location(locationArr[0], locationArr[1], locationArr[2]);

        }).collect(Collectors.toMap(Location::getId, l -> l));

        // load exit data
        Stream<String> exitStream = null;
        try {
            Path path = Paths.get(GameInitializerFileReader.class.getClassLoader()
                    .getResource("exits.psv")
                    .toURI());
            exitStream = Files.lines(path);

        } catch (URISyntaxException | IOException e) {
            logger.error("There was an error reading the exits.", e);
        }

        Map<String, Exit> exits = exitStream.map(line -> {

            StringTokenizer tokenizer = new StringTokenizer(line, "|");

            String[] exitArr = new String[3];
            int index = 0;
            while (tokenizer.hasMoreTokens()) {
                exitArr[index++] = tokenizer.nextToken();
            }

            return new Exit(exitArr[0],
                    Direction.valueOf(exitArr[1].toUpperCase()),
                    locations.get(exitArr[2]));

        }).collect(Collectors.toMap(Exit::getId, exit -> exit));

        // load location-exit data
        Stream<String> locationExitStream = null;
        try {
            Path path = Paths.get(GameInitializerFileReader.class.getClassLoader()
                    .getResource("location-exits.psv")
                    .toURI());
            locationExitStream = Files.lines(path);

        } catch (URISyntaxException | IOException e) {
            logger.error("There was an error reading the exits.", e);
        }

        // map exits to locations
        locationExitStream.forEach(line -> {

            StringTokenizer tokenizer = new StringTokenizer(line, "|");

            String[] locationExitsArr = new String[2];
            int index = 0;
            while (tokenizer.hasMoreTokens()) {
                locationExitsArr[index++] = tokenizer.nextToken();
            }

            locations.get(locationExitsArr[0]).addExit(exits.get(locationExitsArr[1]));

        });

        // create npc characters
        Bear bear = new Bear();
        Troll troll = new Troll();

        // add npc characters
        locations.get("6").addCharacter(bear);
        locations.get("8").addCharacter(troll);

        // set initial location
        game.setCurrentLocation(locations.get("2"));

        logger.info("Game initialized.");
        return game;
    }

    public static void main(String[] args) {
        GameInitializerFileReader.initializeGame();
    }
}
