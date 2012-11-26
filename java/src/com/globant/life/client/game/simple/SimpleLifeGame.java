package com.globant.life.client.game.simple;

import java.util.ArrayList;

import com.globant.life.client.game.LifeGame;
import com.globant.life.client.game.Node;
import com.globant.life.client.game.NodeChange;

public class SimpleLifeGame implements LifeGame {

	private boolean[][] state;
	private int x;
	private int y;

	public SimpleLifeGame(int x, int y) {
		this.state = new boolean[x][y];
		this.x = x;
		this.y = y;
		for (int i = 0; i < this.x; i++) {
			for (int j = 0; j < this.y; j++) {
				this.state[i][j] = false;
			}
		}
	}

	@Override
	public Iterable<NodeChange> next() {

		final ArrayList<NodeChange> changes = new ArrayList<NodeChange>();

		for (int i = 0; i < this.x; i++) {
			for (int j = 0; j < this.y; j++) {
				boolean newState = calculateNextState(i, j);
				if (this.state[i][j] != newState) {
					changes.add(new SimpleNodeChange(new SimpleNode(i, j), newState));
				}
			}
		}

		for (NodeChange n : changes) {
			this.state[n.getNode().getX()][n.getNode().getY()] = !this.state[n.getNode().getX()][n.getNode().getY()];
		}

		return changes;
	}

	private boolean calculateNextState(int i, int j) {
		int n = countLiveNeighbours(i, j);
		boolean isAlive = this.state[i][j];
		if (isAlive) {
			if (n < 2) {
				return false;
			} else if (n == 2 || n == 3) {
				return true;
			} else {
				assert n > 3;
				return false;
			}
		} else {
			if (n == 3) {
				return true;
			} else {
				return false;
			}
		}
	}

	private int countLiveNeighbours(int i, int j) {

		int liveNeighbours = 0;
		liveNeighbours += nodeState(i - 1, j - 1);
		liveNeighbours += nodeState(i, j - 1);
		liveNeighbours += nodeState(i + 1, j - 1);

		liveNeighbours += nodeState(i - 1, j);
		liveNeighbours += nodeState(i + 1, j);

		liveNeighbours += nodeState(i - 1, j + 1);
		liveNeighbours += nodeState(i, j + 1);
		liveNeighbours += nodeState(i + 1, j + 1);

		return liveNeighbours;
	}

	private int nodeState(int i, int j) {

		try {
			return this.state[i][j] == true ? 1 : 0;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public NodeChange toogleNode(Node n) {
		this.state[n.getX()][n.getY()] = !this.state[n.getX()][n.getY()];
		NodeChange result = new SimpleNodeChange(n, this.state[n.getX()][n.getY()]);
		return result;
	}

}
