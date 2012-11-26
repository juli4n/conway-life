package com.globant.life.client.game;

public interface LifeGame {

	NodeChange toggleNode(Node n);

	Iterable<NodeChange> next();

}
