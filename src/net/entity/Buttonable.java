package net.entity;

//Blocks that can be affected by a button press.
public interface Buttonable {
	void doPressEvent();
	void doReleaseEvent();
}
