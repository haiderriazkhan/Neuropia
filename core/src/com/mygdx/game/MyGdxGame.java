package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.model.Frame;
import com.mygdx.game.model.LineSegment;

public class MyGdxGame extends ApplicationAdapter {
	private Stage stage;

	private SpriteBatch batch;
	private Image img;
	private int currImg = 1, currIndex = 0;
	
	private ShapeRenderer shapeRenderer;
	private Frame frame = new Frame();
	private List<Frame> frames = new ArrayList<Frame>();

	public void create () {
		stage = new Stage();
		shapeRenderer = new ShapeRenderer();
		Gdx.input.setInputProcessor(stage);
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		Table table = new Table();
		stage.addActor(table);
		table.setFillParent(true);
		table.bottom();

		TextButton prevFrameButton = new TextButton("<<", skin);
		
		prevFrameButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (currIndex >= frames.size())
					frames.add(frame);
				else
					frames.set(currIndex, frame);
				currIndex--;
				frame = frames.get(currIndex);
				
				currImg--;
				batch = new SpriteBatch();
				img = new Image(new Texture("1/" + "pic" + String.valueOf(currImg) + ".png"));
				stage.addActor(img);
				img.toBack();
				event.cancel();
				return false;
			}
		});
		
		TextButton nextFrameButton = new TextButton(">>", skin);
			
		nextFrameButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
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
				batch = new SpriteBatch();
				img = new Image(new Texture("1/" + "pic" + String.valueOf(currImg) + ".png"));
				stage.addActor(img);
				img.toBack();
				event.cancel();
				return false;
			}
		});
		
		stage.addListener(new ClickListener(Buttons.LEFT) {
			private Vector2 pointBuf;
			private boolean secondPoint = false;
			
			public void clicked(InputEvent event, float x, float y) {
				if (secondPoint) {
					Vector2 p2 = new Vector2(x, y);
					frame.addSegment(new LineSegment(pointBuf, p2));
					secondPoint = false;
				}
				else {
					pointBuf = new Vector2(x, y);
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
		
		DragListener dl = new CustomDragListener();
		dl.setTapSquareSize((float) 1.0);
		stage.addListener(dl);
		
		table.add(prevFrameButton);
		table.add(nextFrameButton);
		
		batch = new SpriteBatch();
		img = new Image(new Texture("1/" + "pic" + String.valueOf(currImg) + ".png"));
		stage.addActor(img);
		img.toBack();
	}

	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		Gdx.gl.glLineWidth(20);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.GREEN);
		
		for (LineSegment line : frame) {
			drawLine(line);
		}
		
		shapeRenderer.end();
	}

	private void drawLine(LineSegment line) {
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
	
	class CustomDragListener extends DragListener
	{
		private int lineIndex;
		private int pointNum;	// 1 or 2
		private boolean translateLine = false;
		private Vector2 translatePoint;
		
	    @Override
	    public void dragStart(InputEvent event, float x, float y, int pointer) 
	    {
	    	for (int i = 0; i < frame.size(); i++) {
	    		LineSegment line = frame.getSegment(i);
	    		Vector2 p1 = line.p1; 
	    		Vector2 p2 = line.p2;
	    		Vector2 mouse = new Vector2(x, y);
	    		
	    		Vector2 vec1 = new Vector2(p1).sub(mouse);

	    		if (vec1.len() <= 10) {
	    			lineIndex = i;
	    			pointNum = 1;
	    			return;
	    		}
	    		else { 
	    			Vector2 vec2 = new Vector2(p2).sub(mouse);
	    			
	    			if (vec2.len() <= 10) {
	    				lineIndex = i;
		    			pointNum = 2;
		    			return;
	    			}
	    			
	    			// Drag the whole line segment
	    			if (Intersector.distanceSegmentPoint(p1, p2, mouse) <= 10) {
	    				lineIndex = i;
	    				translateLine = true;
	    				translatePoint = mouse;
	    				return;
	    			}
	    		}
			}
	    	
	    	lineIndex = -1;
	    }
	    
	    @Override
	    public void drag(InputEvent event, float x, float y, int pointer) {
	    	dragUpdate(event, x, y, pointer);
	    }
	    
	    @Override
	    public void dragStop(InputEvent event, float x, float y, int pointer) {
	    	dragUpdate(event, x, y, pointer);
	    	translateLine = false;
	    }
	    
	    private void dragUpdate(InputEvent event, float x, float y, int pointer) {
	    	if (lineIndex == -1) return;
	    	else if (translateLine) {
	    		LineSegment line = frame.getSegment(lineIndex);
	    		Vector2 delta = new Vector2(x, y).sub(translatePoint);
	    		line.translate(delta);
	    		translatePoint = new Vector2(x, y);
	    		frame.setSegment(lineIndex, line);
	    		
	    	}
	    	else {
	    		Vector2 newP = new Vector2(x, y);
		    	LineSegment line = frame.getSegment(lineIndex);
		    	Vector2 oldP;
		    	LineSegment newLine;
		    	if (pointNum == 1) {
		    		oldP = line.p2;
		    		newLine = new LineSegment(newP, oldP);
		    	}	
		    	else {
		    		oldP = line.p1;
		    		newLine = new LineSegment(oldP, newP);
		    	}
		    		
		    	frame.setSegment(lineIndex, newLine);
	    	}
	    }
	}
}

