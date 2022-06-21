package ru.nsu.fit.kolesnik.notrealroyale.model.worldmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WorldMap {
    private int width;
    private int height;

    Tile[][] tiles;

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
                    } else if (mapLine.charAt(x) == '.') {
                        tiles[y][x] = Tile.GRASS;
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
}
