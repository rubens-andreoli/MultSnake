package com.iinmorus.gtc.ui;

import com.iinmorus.engine2d.Engine;
import com.iinmorus.engine2d.Settings;
import com.iinmorus.engine2d.State;
import com.iinmorus.gtc.state.GameState;
import com.iinmorus.gtc.state.Idle;
import com.iinmorus.gtc.state.Multiplayer;
import com.iinmorus.gtc.state.Singleplayer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

public class GameWindow extends JFrame{
    
    public static final Engine GAME;
    static{
	Settings s = new Settings.Builder().create();
	ArrayList<State> states = new ArrayList<>();
	states.add(new Idle());
	states.add(new Singleplayer());
	states.add(new Multiplayer());
	GAME = new Engine(s);
	GAME.registerStates(states, GameState.IDLE);
    }

    private Thread gameThread;
    
    public GameWindow(){
	//SwingUtilities.invokeLater(GAME);
	gameThread = new Thread(GAME);
	gameThread.setName("GAME");
	this.init();
    }
    
    private void init(){
	this.setTitle("GTC!");
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setResizable(false);
	this.setContentPane(GAME);
	if(GAME.settings.fullscreen){
	    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    this.setUndecorated(true);
	}
	JMenuBar menu = new JMenuBar();
	    JMenu game = new JMenu("Game");
		JMenu start = new JMenu("Start");
		    JMenuItem single = new JMenuItem("Single Player");
		    JMenuItem bot = new JMenuItem("Player vs Bot");
		    JMenuItem multi = new JMenuItem("Player vs Player");
		    start.add(single);
		    start.addSeparator();
		    start.add(bot);
		    start.add(multi);
		JMenuItem pause = new JMenuItem("Pause");
		JMenuItem load = new JMenuItem("Load");
		load.setEnabled(false);
		JMenuItem save = new JMenuItem("Save");
		save.setEnabled(false);
		game.add(start);
		game.add(pause);
		game.addSeparator();
		game.add(load);
		game.add(save);
	menu.add(game);
	
	this.setJMenuBar(menu);
	this.pack();
	
	gameThread.start();
	try {
	    gameThread.join();
	} catch (InterruptedException ex) {
	    Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
	}
	
	/////EXEMPLOS:
//	GAME.states.getState(GameState.SINGLE, Singleplayer.class).getScore();		    //Pega score da partida singleplayer
//	GAME.states.startState(GameState.SINGLE);					    //Começa partida
//	GAME.states.getState(GameState.SINGLE).setPaused(true);				    //Pausa partida
//	GAME.states.getState(GameState.MULT, Multiplayer.class).vsBot(true);		    //Liga bot na partida multiplayer
//	GAME.states.getState(GameState.SINGLE).setDifficulty(GameState.HARD);		    //Muda dificuldade da partida
//	GAME.states.getState(GameState.MULT, Multiplayer.class).getScore_P1();		    //Pega score do player 1 da partida multiplayer	    
//	GAME.states.loadState(/*loaded state from file*/);				    //Carrega partida salva
        
	/////COMEÇA UMA PARTIDA EASY SINGLEPLAYER QUE COMEÇA PAUSADA:
//	State s = GAME.states.getState(GameState.SINGLE);
//	s.setDifficulty(GameState.EASY);
//	GAME.states.startState(GameState.SINGLE);
//	s.setPaused(true);

	/////COMEÇA UMA PARTIDA MEDIUM SINGLEPLAYER
//	GAME.states.getState(GameState.SINGLE).setDifficulty(GameState.MEDIUM);
//	GAME.states.startState(GameState.SINGLE);

	/////COMEÇA UMA PARTIDA HARD MULTIPLAYER VS BOT
//	Multiplayer m = GAME.states.getState(GameState.MULT, Multiplayer.class);
//	m.setDifficulty(GameState.HARD);
//	m.vsBot(true);
//	GAME.states.startState(GameState.MULT);

	this.setVisible(true);
    
    }

    
}
