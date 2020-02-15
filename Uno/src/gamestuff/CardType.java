package gamestuff;

public enum CardType {
	zero, one, two, three, four, five, six, seven, eight, nine, draw, reverse, skip, wild;

	public int index() {
		for (int i = 0; i < values().length; i++) {
			if (this == values()[i])
				return i;
		}
		return -1;
	}

	@Override
	public String toString() {
		switch (this) {
		case draw:
			return "draw";
		case reverse:
			return "reverse";
		case skip:
			return "skip";
		case wild:
			return "wild";
		default:
			return String.valueOf(index());
		}
	}
}