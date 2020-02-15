package managers;

public enum Resolution {
	_1920x1080, _1600x900, _1366x768, _1280x720, _640x360;

	public int indexOf() {
		for (int i = 0; i < 5; i++)
			if (this == values()[i])
				return i;
		return -1;
	}

	public int width() {
		switch (this) {
		case _1920x1080:
			return 1920;
		case _1600x900:
			return 1600;
		case _1366x768:
			return 1366;
		case _1280x720:
			return 1280;
		case _640x360:
			return 640;
		default:
			return -1;
		}
	}

	public int height() {
		switch (this) {
		case _1920x1080:
			return 1080;
		case _1600x900:
			return 900;
		case _1366x768:
			return 768;
		case _1280x720:
			return 720;
		case _640x360:
			return 360;
		default:
			return -1;
		}
	}
}