package tp.pr5;

public enum Rotation {
	LEFT, RIGHT, UNKNOWN;
	
	public Rotation opposite() {
		if (this == LEFT)
			return RIGHT;
		if (this == RIGHT)
			return LEFT;
		return UNKNOWN;
	}
}

