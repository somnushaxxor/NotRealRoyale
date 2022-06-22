package ru.nsu.fit.kolesnik.notrealroyale.model.worldmap;

public enum Tile {
    SAND(false), WALL(true), ROCK(true), CACTUS(true);

    private final boolean collidable;

    Tile(boolean collidable) {
        this.collidable = collidable;
    }

    public boolean isCollidable() {
        return collidable;
    }
}
