package com.globant.life.client.game.simple;

import com.globant.life.client.game.Node;

public class SimpleNode implements Node {

	private int x;
	private int y;

	public SimpleNode(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}
}
