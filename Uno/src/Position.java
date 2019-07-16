
public enum Position {
	South, West, North, East;
	public static Position nextPosition(Position position, boolean isReversed) {
		if(isReversed) {
			if(position==South)
				return East;
			else if(position==West)
				return South;
			else if(position==North)
				return West;
			else
				return North;
		}else {
			if(position==South)
				return West;
			else if(position==West)
				return North;
			else if(position==North)
				return East;
			else
				return South;
		}
	}
	public static int indexOf(Position position) {
		for(int i = 0; i < values().length; i++) {
			if(values()[i]==position)
				return i;
		}
		return -1;
	}
}
