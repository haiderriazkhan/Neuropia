package com.mygdx.game.model;

import com.badlogic.gdx.math.Vector2;

public class LineSegment {
	public Vector2 p1, p2;

	public LineSegment(Vector2 p1, Vector2 p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public LineSegment(LineSegment copy) {
		p1 = new Vector2(copy.p1);
		p2 = new Vector2(copy.p2);
	}
	
	public void translate(Vector2 delta) {
		p1.add(delta);
		p2.add(delta);
	}
}
