package com.drawit.demo.dto;

// used in websocket communication for tracking coordinates of player's drawing
public class Coordinates {
    int x;
    int y;
    int prevX;
    int prevY;

    public Coordinates(int x, int y, int prevX, int prevY) {
        this.x = x;
        this.y = y;
        this.prevX = prevX;
        this.prevY = prevY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPrevX() {
        return prevX;
    }

    public void setPrevX(int prevX) {
        this.prevX = prevX;
    }

    public int getPrevY() {
        return prevY;
    }

    public void setPrevY(int prevY) {
        this.prevY = prevY;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                ", prevX=" + prevX +
                ", prevY=" + prevY +
                '}';
    }
}

