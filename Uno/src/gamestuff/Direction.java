package gamestuff;

import java.awt.Point;

import managers.GameManager;

public enum Direction {
	south, west, north, east;

	public Point getPoint() {
		switch (this) {
		case south:
			return GameManager.calculatePoint(910, 880);
		case west:
			return GameManager.calculatePoint(50, 465);
		case north:
			return GameManager.calculatePoint(910, 50);
		case east:
			return GameManager.calculatePoint(1770, 465);
		default:
			return null;
		}
	}

	public boolean isPole() {
		return this == south || this == north;
	}
}