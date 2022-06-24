package ru.nsu.fit.kolesnik.notrealroyale.model.worldmap;

import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.RevolverBooster;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Chest;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.HealingSalve;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WorldMap {
    private int width;
    private int height;

    private Tile[][] tiles;
    private final List<Chest> chests;
    private final List<RevolverBooster> revolverBoosters;
    private final List<HealingSalve> healingSalves;

    public WorldMap() {
        chests = new ArrayList<>();
        revolverBoosters = new ArrayList<>();
        healingSalves = new ArrayList<>();
    }

    public void loadMap(String mapName) {
        try {
            InputStream mapInputStream = getClass().getClassLoader().getResourceAsStream(mapName);
            if (mapInputStream == null) {
                throw new RuntimeException("Can not open map file!");
            }
            InputStreamReader mapInputStreamReader = new InputStreamReader(mapInputStream);
            BufferedReader bufferedReader = new BufferedReader(mapInputStreamReader);
            String mapSizesLine = bufferedReader.readLine();
            if (mapSizesLine == null) {
                throw new RuntimeException("Invalid map file format!");
            }
            String[] mapSizes = mapSizesLine.split(" ");
            width = Integer.parseInt(mapSizes[0]);
            if (width <= 0) {
                throw new RuntimeException("Invalid map file format!");
            }
            height = Integer.parseInt(mapSizes[1]);
            if (height <= 0) {
                throw new RuntimeException("Invalid map file format!");
            }
            tiles = new Tile[height][width];
            String mapLine = bufferedReader.readLine();
            int y = 0;
            while (mapLine != null && y < height) {
                if (mapLine.length() != width) {
                    throw new RuntimeException("Invalid map file format!");
                }
                for (int x = 0; x < mapLine.length(); x++) {
                    if (mapLine.charAt(x) == '#') {
                        tiles[y][x] = Tile.WALL;
                    } else if (mapLine.charAt(x) == '&') {
                        tiles[y][x] = Tile.ROCK;
                    } else if (mapLine.charAt(x) == '!') {
                        tiles[y][x] = Tile.CACTUS;
                    } else if (mapLine.charAt(x) == '.') {
                        tiles[y][x] = Tile.SAND;
                        if (Math.random() < 0.005) {
                            Chest chest = new Chest(x, y);
                            chests.add(chest);
                        }
                    } else {
                        throw new RuntimeException("Invalid map file format!");
                    }
                }
                y++;
                mapLine = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile getTile(int tileX, int tileY) {
        return tiles[tileY][tileX];
    }

    public List<Chest> getChests() {
        return chests;
    }

    public List<RevolverBooster> getBoosters() {
        return revolverBoosters;
    }

    public List<HealingSalve> getHealingSalves() {
        return healingSalves;
    }
}
