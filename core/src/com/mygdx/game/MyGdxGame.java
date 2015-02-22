package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.model.LineSegment;
import com.mygdx.game.model.Point;

public class MyGdxGame extends ApplicationAdapter {
	private Stage stage;

	private SpriteBatch batch;
	private Texture img;
	private Sprite sprite;
	private int currImg = 1;
	
	private ShapeRenderer shapeRenderer;
	private List<LineSegment> lines = new ArrayList<LineSegment>();
	

	public void create () {
		stage = new Stage();
		shapeRenderer = new ShapeRenderer();
		Gdx.input.setInputProcessor(stage);
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		Table table = new Table();
		stage.addActor(table);
		table.setFillParent(true);
		table.bottom();

		TextButton prevFrameButton = new TextButton("Previous Frame", skin);
		
		prevFrameButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				currImg--;
				batch = new SpriteBatch();
				img = new Texture("1/" + "pic" + String.valueOf(currImg) + ".png");
				sprite = new Sprite(img);
				event.cancel();
				return false;
			}
		});
		
		TextButton nextFrameButton = new TextButton("Next Frame", skin);
			
		nextFrameButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("Click event");
				currImg++;
				batch = new SpriteBatch();
				img = new Texture("1/" + "pic" + String.valueOf(currImg) + ".png");
				sprite = new Sprite(img);
				event.cancel();
				return false;
			}
		});
		
		stage.addListener(new ClickListener(Buttons.LEFT) {
			private Point pointBuf;
			private boolean secondPoint = false;
			
			public void clicked(InputEvent event, float x, float y) {
				if (secondPoint) {
					Point p2 = new Point(x, y);
					lines.add(new LineSegment(pointBuf, p2));
					secondPoint = false;
				}
				else {
					pointBuf = new Point(x, y);
					secondPoint = true;
				}
			}
		});
		
		stage.addListener(new ClickListener(Buttons.RIGHT)
		{
		    @Override
		    public void clicked(InputEvent event, float x, float y)
		    {
		        // Iterate through list of line segments and do an intersection
		    	// test between the line and the click point. If <= 10 pixels, 
		    	// remove from list.
		    	
		    	for (LineSegment line : lines) {
		    		Point p1 = line.p1; 
		    		Point p2 = line.p2;
		    	
					if (Intersector.distanceSegmentPoint(p1.x, p1.y, p2.x, p2.y, x, y) <= 7) {
						lines.remove(line);
						break;
					}
				}
		    }
		});
		
		DragListener dl = new CustomDragListener();
		dl.setTapSquareSize((float) 0.5);
		stage.addListener(dl);
		
		table.add(prevFrameButton);
		table.add(nextFrameButton);
		
		batch = new SpriteBatch();
		img = new Texture("1/pic1.png");
		sprite = new Sprite(img);
	}

	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
		
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		
		for (LineSegment line : lines) {
			drawLine(line);
		}
		
		shapeRenderer.end();
	}

	private void drawLine(LineSegment line) {
		Point p1 = line.p1;
		Point p2 = line.p2;
		shapeRenderer.line(p1.x, p1.y, p2.x, p2.y);
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
		
	    @Override
	    public void dragStart(InputEvent event, float x, float y, int pointer) 
	    {
	    	for (int i = 0; i < lines.size(); i++) {
	    		LineSegment line = lines.get(i);
	    		Point p1 = line.p1; 
	    		Point p2 = line.p2;
	    		
	    		Vector2 vec1 = new Vector2(p1.x - x, p1.y - y);

	    		System.out.println(vec1.len());
	    		if (vec1.len() <= 20) {
	    			System.out.println("ASLKDHJASJKDAS");
	    			lineIndex = i;
	    			pointNum = 1;
	    		}
	    		else { 
	    			Vector2 vec2 = new Vector2(p2.x, p2.y).sub(x, y);
	    			
	    			if (vec2.len() <= 20) {
	    				System.out.println("ASLKDHJASJKDAS");
	    				lineIndex = i;
		    			pointNum = 2;
	    			}
	    			
	    			// Drag the whole line segment
	    			//if (intersector.)
	    		}
			}
	    }
	    
	    @Override
	    public void dragStop(InputEvent event, float x, float y, int pointer) {
	    	Point newP = new Point(x, y);
	    	LineSegment line = lines.get(lineIndex);
	    	Point oldP;
	    	if (pointNum == 1)
	    		oldP = line.p2;
	    	else 
	    		oldP = line.p1;
	    	
	    	LineSegment newLine = new LineSegment(oldP, newP);
	    	lines.set(lineIndex, newLine);
	    }
	}
}

