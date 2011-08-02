package com.globant.life.client.game.simple;

import com.globant.life.client.game.Node;
import com.globant.life.client.game.NodeChange;

public class SimpleNodeChange implements NodeChange {

	private Node node;

	private boolean newState;

	public SimpleNodeChange(Node node, boolean newState) {
		super();
		this.node = node;
		this.newState = newState;
	}

	@Override
	public boolean getNewState() {
		return this.newState;
	}

	@Override
	public Node getNode() {
		return this.node;
	}

}
