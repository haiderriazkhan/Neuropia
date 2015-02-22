package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.model.Frame;
import com.mygdx.game.model.LineSegment;

public class MyGdxGame extends ApplicationAdapter {
	private Stage stage;

	private Image img;
	private Stack imgStack;
	private int currImg = 1, currIndex = 0;
	
	private ShapeRenderer shapeRenderer;
	private Frame frame = new Frame();
	private List<Frame> frames = new ArrayList<Frame>();
	
	private Table table;

	public void create () {
		stage = new Stage();
		shapeRenderer = new ShapeRenderer();
		Gdx.input.setInputProcessor(stage);
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		table = new Table();
		imgStack = new Stack();
		table.add(imgStack);
		stage.addActor(table);
		table.setFillParent(true);
		table.center();
		
		Table table2 = new Table();
		stage.addActor(table2);
		table2.setFillParent(true);
		table2.bottom();
		
		TextButton nextFrameButton = new TextButton(">>", skin);	
		
		nextFrameButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				frame.removeDisconnectedLineSegments();
				if (currIndex >= frames.size())
					frames.add(frame);
				else
					frames.set(currIndex, frame);
				
				currIndex++;
				if (currIndex >= frames.size()) {
					frame = new Frame(frame);
				}
				else {
					frame = frames.get(currIndex);
				}
				currImg++;
				img = new Image(new Texture("1/" + "pic" + String.valueOf(currImg) + ".png"));
				imgStack.add(img);
				event.cancel();
				return false;
			}
		});
		
		stage.addListener(new ClickListener(Buttons.LEFT) {
			private Vector2 pointBuf;
			private boolean connected = false;
			private boolean secondPoint = false;
			
			public void clicked(InputEvent event, float x, float y) {
				if (secondPoint) {
					Vector2 p2 = new Vector2(x, y);
					frame.addSegment(new LineSegment(pointBuf, p2, connected));
					secondPoint = false;
				}
				else {
					pointBuf = new Vector2(x, y);
					
					if (frame.size() > 0) {
						// Case 1: near endpoint
						for (LineSegment line : frame) {
							Vector2 p1 = new Vector2(line.p1).sub(pointBuf);
							Vector2 p2 = new Vector2(line.p2).sub(pointBuf);
							
							if (p1.len() < 10) {
								pointBuf = line.p1;
								connected = line.connected;
								secondPoint = true;
								return;
							}
							else if (p2.len() < 10) {
								pointBuf = line.p2;
								connected = line.connected;
								secondPoint = true;
								return;
							}	
						}
						
						// Case 2: intersects line
						for (LineSegment line : frame) {
							if (Intersector.distanceSegmentPoint(line.p1, line.p2, pointBuf) < 10) {
								Vector2 nearest = new Vector2();
								Intersector.nearestSegmentPoint(line.p1, line.p2, pointBuf, nearest);
								pointBuf = nearest;
								connected = line.connected;
								secondPoint = true;
								return;
							}
						}
						
						connected = false;
					}
					else {
						connected = true;
					}
					
					secondPoint = true;
				}
			}
		});
		
		stage.addListener(new ClickListener(Buttons.RIGHT)
		{
		    @Override
		    public void clicked(InputEvent event, float x, float y)
		    {
		        for (LineSegment line : frame) {
		    		Vector2 p1 = line.p1; 
		    		Vector2 p2 = line.p2;
		    		Vector2 mouse = new Vector2(x, y);
		    	
					if (Intersector.distanceSegmentPoint(p1, p2, mouse) <= 7) {
						frame.removeSegment(line);
						break;
					}
				}
		    }
		});

		table2.add(nextFrameButton)
			.padBottom(15)
			.width(150);
		table2.setColor(Color.BLACK);
		
		img = new Image(new Texture("1/" + "pic" + String.valueOf(currImg) + ".png"));
		imgStack.add(img);
		table.padBottom(70);
	}

	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		Gdx.gl.glLineWidth(20);
		shapeRenderer.begin(ShapeType.Filled);
		
		for (LineSegment line : frame) {
			drawLine(line);
		}
		
		shapeRenderer.end();
	}

	private void drawLine(LineSegment line) {
		if (line.connected)
			shapeRenderer.setColor(Color.GREEN);
		else
			shapeRenderer.setColor(Color.RED);
		Vector2 p1 = line.p1;
		Vector2 p2 = line.p2;
		shapeRenderer.rectLine(p1, p2, 3);
	}

	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	public void dispose () {
		stage.dispose();
	}
}

