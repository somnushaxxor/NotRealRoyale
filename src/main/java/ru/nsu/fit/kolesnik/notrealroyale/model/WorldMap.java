package ru.nsu.fit.kolesnik.notrealroyale.model;

import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Chest;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Wall;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WorldMap {
    private int width;
    private int height;

    private final List<Wall> walls;
    private final List<Chest> chests;

    public WorldMap() {
        walls = new ArrayList<>();
        chests = new ArrayList<>();
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
            String mapLine = bufferedReader.readLine();
            int y = 0;
            while (mapLine != null && y < height) {
                if (mapLine.length() != width) {
                    throw new RuntimeException("Invalid map file format!");
                }
                for (int x = 0; x < mapLine.length(); x++) {
                    if (mapLine.charAt(x) == '#') {
                        Wall wall = new Wall(x, y);
                        walls.add(wall);
                    } else if (mapLine.charAt(x) == '$') {
                        Chest chest = new Chest(x, y);
                        chests.add(chest);
                    } else if (mapLine.charAt(x) != '.') {
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

    public List<Wall> getWalls() {
        return walls;
    }

    public List<Chest> getChests() {
        return chests;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
