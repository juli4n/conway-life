package com.globant.life.client;

import com.globant.life.client.game.LifeGame;
import com.globant.life.client.game.NodeChange;
import com.globant.life.client.game.simple.SimpleLifeGame;
import com.globant.life.client.game.simple.SimpleNode;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;

public class LifeGameMain implements EntryPoint {
	
	private Canvas c;
	private LifeGame game = new SimpleLifeGame(50, 50);
	private int width = 500;
	private int height = 500;
	private int space = 10;
	private boolean running = false;
	private Timer timer;
	
	public void onModuleLoad() {

		c = Canvas.createIfSupported();

		final PushButton startButton = new PushButton(new Image("img/start_icon.png"));

		c.setSize(this.width + "px", this.height + "px");
		c.getCanvasElement().setWidth(this.width);
		c.getCanvasElement().setHeight(this.height);

		RootPanel.get("gridDiv").add(c);
		RootPanel.get("buttonsDiv").add(startButton);
		
		drawGrid();

		c.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int x = event.getRelativeX(c.getCanvasElement()) / LifeGameMain.this.space;
				int y =  event.getRelativeY(c.getCanvasElement()) / LifeGameMain.this.space;
				fillNode(x,y);
				NodeChange nc = LifeGameMain.this.game.toggleNode(new SimpleNode(x, y));
				toggleState(nc);
			}
		});
		
		this.timer = new Timer() {
			@Override
			public void run() {
				doNext();
				if (running) {
					timer.schedule(50);		
				}
			}
		};
		
		startButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (running) {
					startButton.getUpFace().setImage(new Image("img/start_icon.png"));					
					timer.cancel();
					running = false;
				} else {
					startButton.getUpFace().setImage(new Image("img/stop_icon.png"));					
					running = true;
					timer.schedule(50);
				}
			}
		});
		
	}
	
	private void doNext() {
		for (NodeChange n : LifeGameMain.this.game.next()) {
			toggleState(n);
		}
	}

	private void toggleState(NodeChange n) {
		if (n.getNewState()) {
			fillNode(n.getNode().getX(), n.getNode().getY());						
		} else {
			clearNode(n.getNode().getX(), n.getNode().getY());
		}
	}
	
	private void fillNode(int x, int y) {
		c.getContext2d().setLineWidth(1);
		c.getContext2d().fillRect(x * space, y * space, space, space);
	}

	private void clearNode(int x, int y) {
		c.getContext2d().setLineWidth(1);
		c.getContext2d().clearRect(x * space +1, y * space+1, space -2, space-2);
	}

	private void drawGrid() {
		c.getContext2d().setLineWidth(1);
		for (int i = 0; i <= this.height / space; i++) {
			c.getContext2d().moveTo(0, i * space);
			c.getContext2d().lineTo(this.width, i * space);
			c.getContext2d().stroke();
		}
		for (int i = 0; i <= this.width / space; i++) {
			c.getContext2d().moveTo(i * space, 0);
			c.getContext2d().lineTo(i * space, this.height);
			c.getContext2d().stroke();
		}
	}
}
