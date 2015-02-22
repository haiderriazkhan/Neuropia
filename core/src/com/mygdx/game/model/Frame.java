package com.mygdx.game.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Frame implements Iterable<LineSegment> {
	private List<LineSegment> lines = new ArrayList<LineSegment>();
	
	public Frame() {}
	
	public Frame(Frame frame) {
		for (LineSegment segment : frame) {
			lines.add(new LineSegment(segment));
		}
	}
	
	public void addSegment(LineSegment segment) {
		lines.add(segment);
	}
	
	public LineSegment getSegment(int index) {
		return lines.get(index);
	}
	
	public void setSegment(int index, LineSegment segment) {
		lines.set(index, segment);
	}
	
	public void removeSegment(LineSegment segment) {
		lines.remove(segment);
	}
	
	public int size() {
		return lines.size();
	}

	@Override
	public Iterator<LineSegment> iterator() {
		return lines.iterator();
	}
}
