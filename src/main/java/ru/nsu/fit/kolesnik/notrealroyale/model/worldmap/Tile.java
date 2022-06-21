package ru.nsu.fit.kolesnik.notrealroyale.model.worldmap;

public enum Tile {
    GRASS(false), WALL(true), ROCK(true);

    private final boolean collidable;

    Tile(boolean collidable) {
        this.collidable = collidable;
    }

    public boolean isCollidable() {
        return collidable;
    }
}
