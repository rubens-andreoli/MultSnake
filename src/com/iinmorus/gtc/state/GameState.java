package com.iinmorus.gtc.state;

import com.iinmorus.engine2d.State;
import static com.iinmorus.gtc.ui.GameWindow.GAME;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.Serializable;

public abstract class GameState implements State, Serializable{
    public static final int EASY=1, MEDIUM=2, HARD=3;
    public static final String IDLE="idle", SINGLE="single", MULT="mult";
    
    //ui
    protected Color backgroung = Color.BLACK;
    protected Color overColor = Color.RED;
    protected Color pauseColor = Color.YELLOW;
    protected Color titleColor = Color.RED;
    protected String overMsg = "GAME OVER";
    protected String pauseMsg = "PAUSED";
    protected String title = "GET THAT CHERRY!";
    protected Font scoreFont;
    protected Font warningFont;
    protected Font titleFont;
    
    //configs
    protected int baseWallAmount = 30;
    protected int baseScore = 5;
    protected int difficulty = EASY;
    
    //status
    protected long stateTick;
    protected int time;
    protected boolean isPaused, isOver;

    @Override
    public void loadResources() {
	//Font
	try {
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/manaspc.ttf")));
	    titleFont = new Font("Manaspace", Font.PLAIN, 60);
	    scoreFont = new Font("Manaspace", Font.PLAIN, 20);
	    warningFont = new Font("Manaspace", Font.BOLD, 100);
	} catch (Exception e) {
	    titleFont = new Font(Font.MONOSPACED, Font.BOLD, 60);
	    scoreFont = new Font(Font.MONOSPACED, Font.PLAIN, 20);
	    warningFont = new Font(Font.MONOSPACED, Font.BOLD, 100);
	}
	
	//Music
	GAME.sounds.loadMP3("/sounds/idle.mp3", "idle");
	GAME.sounds.loadMP3("/sounds/match.mp3", "match");
	
	//SFX
	GAME.sounds.loadMP3("/sounds/hit.mp3", "hit");
	GAME.sounds.loadMP3("/sounds/cherry.mp3", "cherry");
	
    }
    
    
    
}
