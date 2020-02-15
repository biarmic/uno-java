package gamestuff;

public enum CardColor {
	blue, red, yellow, green, wild;

	public int index() {
		for (int i = 0; i < values().length; i++) {
			if (this == values()[i])
				return i;
		}
		return -1;
	}
}