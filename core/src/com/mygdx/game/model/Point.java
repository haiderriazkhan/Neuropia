package com.mygdx.game.model;

public class Point {
	public float x, y;

	public Point(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public void add(float x, float y) {
		this.x += x;
		this.y += y;
	}
	
	public void sub(float x, float y) {
		this.x -= x;
		this.y -= y;
	}
}
