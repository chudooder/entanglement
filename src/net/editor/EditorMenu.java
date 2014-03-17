package net.editor;

import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Entity;
import chu.engine.KeyboardEvent;
import chu.engine.anim.Renderer;

/**
 * handles input in the editor
 * @author Shawn
 *
 */
public class EditorMenu extends Entity {

	private static final float FADE_TIME = 0.5f;
	private static final int ENTITY_LAYER = 1;
	private static final int FG_LAYER = 0;
	private static final int BG_LAYER = 2;
	private static final int WIRE_LAYER = 3;
	private static Texture layer_icon;
	private int selectedType = 0;
	private int arg = 0;
	private int selectedTile = 0;
	private int selectedWire = 0;
	private boolean visible;
	private float fadeTimer = 0;
	private String levelName;
	private boolean editingName;
	private int editLayer;
	public boolean renderEntities;
	public boolean renderBackground;
	public boolean renderForeground;
	public boolean renderWires;
	
	static {
		try {
			layer_icon = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/layer_icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public EditorMenu(float x, float y) {
		super(x, y);
		visible = true;
		sprite.addAnimation("MENU", layer_icon, 32, 32, 4, 0);
		editingName = false;
		levelName = "NewLevel";
		editLayer = 0;
		selectedTile = 0;
		selectedWire = 0;
		renderEntities = true;
		renderBackground = true;
		renderForeground = true;
		renderWires = true;
	}

	@Override
	public void beginStep() {
		List<KeyboardEvent> keys = Editor.getKeys();
		for (KeyboardEvent ke : keys) {
			if(ke.state) {					//Key pressed
				if(!editingName) {
					if(ke.key == Keyboard.KEY_Q && editLayer < 3) {
						editLayer++;
						sprite.setFrame(editLayer);
					} else if(ke.key == Keyboard.KEY_E && editLayer > 0) {
						editLayer--;
						sprite.setFrame(editLayer);
					}
					if (editLayer == BG_LAYER || editLayer == FG_LAYER) {	//Placing terrain/fg
						if (ke.key == Keyboard.KEY_W) {
							if(selectedTile >= 8) {
								selectedTile -= 8;
							}
							visible = true;
							fadeTimer = 0;
						} else if (ke.key == Keyboard.KEY_S) {
							if(selectedTile < 56) {
								selectedTile += 8;
							}
							visible = true;
							fadeTimer = 0;
						} else if (ke.key == Keyboard.KEY_A) {
							if(selectedTile > 0) {
								selectedTile--;
							}
							visible = true;
							fadeTimer = 0;
						} else if (ke.key == Keyboard.KEY_D) {
							if(selectedTile < 63) {
								selectedTile++;
							}
							visible = true;
							fadeTimer = 0;
						}
					} else if(editLayer == ENTITY_LAYER) {			//Placing Entities
						if (ke.key == Keyboard.KEY_W) {
							if (arg > 0)
								arg--;
							visible = true;
							fadeTimer = 0;
						} else if (ke.key == Keyboard.KEY_S) {
							if (arg < Element.getNumOfArgs(selectedType)-1)
								arg++;
							visible = true;
							fadeTimer = 0;
						} else if (ke.key == Keyboard.KEY_A) {
							if (selectedType > 0) {
								selectedType--;
								if(arg >= Element.getNumOfArgs(selectedType))
									arg = Element.getNumOfArgs(selectedType)-1;
							}
							visible = true;
							fadeTimer = 0;
						} else if (ke.key == Keyboard.KEY_D) {
							if (selectedType < Element.getNumOfTypes()-1) {
								selectedType++;
								if(arg >= Element.getNumOfArgs(selectedType))
									arg = Element.getNumOfArgs(selectedType)-1;
							}
							visible = true;
							fadeTimer = 0;
						}
					} else if(editLayer == WIRE_LAYER) {
						if (ke.key == Keyboard.KEY_A || ke.key == Keyboard.KEY_D) {
							selectedWire = 1-selectedWire;
						}
					}
					if (ke.key == Keyboard.KEY_Z) {
						renderWires = !renderWires;
					}
					if (ke.key == Keyboard.KEY_X) {
						renderBackground = !renderBackground;
					}
					if (ke.key == Keyboard.KEY_C) {
						renderEntities = !renderEntities;
					}
					if (ke.key == Keyboard.KEY_V) {
						renderForeground = !renderForeground;
					}
				} else {		//is editing name
					if(ke.key == Keyboard.KEY_BACK && levelName.length() > 0) {
						levelName = levelName.substring(0, levelName.length()-1);
					} else {
						char c = Keyboard.getEventCharacter();
						if(c >= ' ' && c <= '~' && "/\\:*\"<>".indexOf(c) == -1)
							levelName += c;
					}
				}
				if (ke.key == Keyboard.KEY_F2) {
					editingName = !editingName;
				} 
				if (ke.key == Keyboard.KEY_F1) { 
					((EditorStage)stage).getLevel().name = levelName;
					((EditorStage)stage).getLevel().serialize();
				}
				if (ke.key == Keyboard.KEY_F3) { 
					((EditorStage)stage).loadLevel(levelName);
				}
				if (ke.key == Keyboard.KEY_F5){ 
					((EditorStage)stage).loadLevel("");
				}
			}
		}
		
		/* Mouse input handling */
			if (Mouse.isButtonDown(0)) {
				int gridX = Mouse.getX() / 32;
				int gridY = (480 - Mouse.getY()) / 32;
				if(editLayer == FG_LAYER)
					((EditorStage) stage).getLevel().setForeground(gridX, gridY, selectedTile);
				if(editLayer == ENTITY_LAYER)
					((EditorStage) stage).getLevel().setElement(gridX, gridY, selectedType, arg);
				if(editLayer == BG_LAYER)
					((EditorStage) stage).getLevel().setBackground(gridX, gridY, selectedTile);
				if(editLayer == WIRE_LAYER)
					((EditorStage) stage).getLevel().setWire(gridX, gridY, 1+selectedWire);
			} else if (Mouse.isButtonDown(1)) {
				int gridX = Mouse.getX() / 32;
				int gridY = (480 - Mouse.getY()) / 32;
				if(editLayer == FG_LAYER)
					((EditorStage) stage).getLevel().setForeground(gridX, gridY, -1);
				if(editLayer == ENTITY_LAYER)
					((EditorStage) stage).getLevel().setElement(gridX, gridY, -1, 0);
				if(editLayer == BG_LAYER)
					((EditorStage) stage).getLevel().setBackground(gridX, gridY, -1);
				if(editLayer == WIRE_LAYER)
					((EditorStage) stage).getLevel().setWire(gridX, gridY, 0);
			}

	}

	@Override
	public void endStep() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("deprecation")
	@Override
	public void render() {
		//Draw level
		EditorLevel level = ((EditorStage)stage).getLevel();
		if(renderBackground)
			level.renderBackground();
		if(renderEntities)
			level.renderEntities();
		if(renderForeground)
			level.renderForeground();
		if(renderWires)
			level.renderWires();
		if (visible) {
			fadeTimer += Editor.getDeltaSeconds();
			if (fadeTimer > FADE_TIME) {
				visible = false;
				fadeTimer = 0;
			}
			if(editLayer == BG_LAYER || editLayer == FG_LAYER) {
				int centerX = 304;
				int centerY = 180;
				Renderer.drawRectangle(centerX - 32 * (selectedTile % 8),
						centerY - 32 * (selectedTile / 8), 
						centerX + 32 * (8 - selectedTile % 8), 
						centerY + 32 * (8 - selectedTile / 8), 0.02f, 
						new Color(255, 255, 255));
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						int renderX = centerX + 32 * (i - selectedTile%8);
						int renderY = centerY + 32 * (j - selectedTile/8);
						EditorLevel.tileset.render(renderX, renderY, i, j, 0.01f);
					}
				}
				Renderer.drawLine(centerX, centerY, centerX, centerY + 32, 2,
						0.0f, Color.red, Color.red);
				Renderer.drawLine(centerX, centerY + 32, centerX + 32,
						centerY + 32, 2, 0.0f, Color.red, Color.red);
				Renderer.drawLine(centerX + 32, centerY + 32, centerX + 32,
						centerY, 2, 0.0f, Color.red, Color.red);
				Renderer.drawLine(centerX + 32, centerY, centerX, centerY, 2,
						0.0f, Color.red, Color.red);
			} else if(editLayer == ENTITY_LAYER) {
				int centerX = 304;
				int centerY = 180;
				Renderer.drawRectangle(centerX - 32 * selectedType,
						centerY - 32 * arg, 
						centerX + 32 * (Element.getNumOfTypes() - selectedType), 
						centerY + 32 * (5 - arg), 0.02f, 
						new Color(255, 255, 255));
				for (int i = 0; i < Element.getNumOfTypes(); i++) {
					for (int j = 0; j < Element.getNumOfArgs(i); j++) {
						int renderX = centerX + 32 * (i - selectedType);
						int renderY = centerY + 32 * (j - arg);
						Element e = new Element(i,j);
						float tx = 32.0f/e.getTexture().getImageWidth();
						Renderer.render(e.getTexture(), 0, 0, tx, 1, renderX, renderY,
								renderX + 32, renderY + 32, 0.01f);
					}
				}
				Renderer.drawLine(centerX, centerY, centerX, centerY + 32, 2,
						0.0f, Color.red, Color.red);
				Renderer.drawLine(centerX, centerY + 32, centerX + 32,
						centerY + 32, 2, 0.0f, Color.red, Color.red);
				Renderer.drawLine(centerX + 32, centerY + 32, centerX + 32,
						centerY, 2, 0.0f, Color.red, Color.red);
				Renderer.drawLine(centerX + 32, centerY, centerX, centerY, 2,
						0.0f, Color.red, Color.red);
			}
		}
		if(editingName) {
			Editor.messageFont.drawString(0, 0, levelName, Color.red);
		} else {
			Editor.messageFont.drawString(0, 0, levelName, Color.white);
		}
		sprite.render(640-32, 0, 0.0f);
	}

	public void setLevelName(String name) {
		levelName = name;
	}

}
