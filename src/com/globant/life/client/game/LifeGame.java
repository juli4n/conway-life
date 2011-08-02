package com.globant.life.client.game;

public interface LifeGame {

	NodeChange toogleNode(Node n);

	Iterable<NodeChange> next();

}
