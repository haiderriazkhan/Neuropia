package com.mygdx.game.model;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class TreeNode {
	private double LENGTH;
	private Vector2 rootPoint;
	private ArrayList<TreeNode> CHILDREN;
	private double initialFrame;
	private ArrayList<LineSegment> treeSegments;
	public TreeNode(){
		LENGTH=0;
		CHILDREN=new ArrayList<TreeNode>();
		initialFrame=0;
		setRootPoint(new Vector2(0,0));
		setTreeSegments(new ArrayList<LineSegment>());
		
	}
	public double getLength(){
		return this.LENGTH;
	}
	public void setLength(double length){
		this.LENGTH=length;
	}
	
	public void setParent(int length){
		this.LENGTH=length;
	}
	public ArrayList<TreeNode> getChildren() {
		return CHILDREN;
	}

	public void setChildren(ArrayList<TreeNode> cHILDREN) {
		CHILDREN = cHILDREN;
	}

	public double getInitialFrame() {
		return initialFrame;
	}

	public void setInitialFrame(double initialFrame) {
		this.initialFrame = initialFrame;
	}
	public ArrayList<LineSegment> getTreeSegments() {
		return treeSegments;
	}
	public void setTreeSegments(ArrayList<LineSegment> treeSegments) {
		this.treeSegments = treeSegments;
	}
	public Vector2 getRootPoint() {
		return rootPoint;
	}
	public void setRootPoint(Vector2 rootPoint) {
		this.rootPoint = rootPoint;
	}
	
}
