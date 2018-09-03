package com.iinmorus.gtc.state;

import com.iinmorus.engine.Engine;
import static com.iinmorus.gtc.GTC.IDLE;
import static com.iinmorus.gtc.GTC.MULT;
import static com.iinmorus.gtc.GTC.SINGLE;
import com.iinmorus.gtc.bot.Bot;
import com.iinmorus.gtc.bot.FastBot;
import com.iinmorus.gtc.bot.PreciseBot;
import com.iinmorus.gtc.bot.SlowBot;
import com.iinmorus.gtc.entity.Cherry;
import com.iinmorus.gtc.entity.Snake;
import com.iinmorus.gtc.entity.Walls;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Multiplayer extends GTCState{
    
    //entities
    private Snake snake_P2;
    private Snake snake_P1;
    private Cherry cherry;
    private Walls walls;
    private Bot bot;
       
    //status
    private boolean isBot;
    private int score_P1, score_P2;
    private String winner;

    public Multiplayer(Engine engine) {
	super(engine);
    }

    @Override
    public void start(){
	isBot = true; //REMOVE: testing...
	
	stateTick = 0;
	time = 0;
	score_P1 = 0;
	score_P2 = 0;
	isOver = false;
	isPaused = false;
	
        snake_P1 = new Snake(0,0);
	snake_P2 = new Snake(width/scale-1, 0);
	snake_P2.setColor(new Color(112, 219, 112));
        cherry = new Cherry();
        walls = new Walls(Math.round(baseWallAmount*difficulty*0.60F));
	
	if(isBot){
	    switch(difficulty){
		case EASY:
		    bot = new SlowBot(snake_P2);
		    break;
		case MEDIUM:
		    bot = new PreciseBot(snake_P2);
		    break;
		case HARD:
		    bot = new FastBot(snake_P2);
		    break;
	    }
	    bot.changeGoal(cherry.getLocation());
	}
	
	engine.sounds.loop("match", 600, engine.sounds.getFrames("match") - 2000);
    }

    @Override
    public void update(){
	if(!isOver && !isPaused){
	    stateTick++;
	    if(stateTick%engine.settings.tickRate == 0) time++;
	    
	    walls.update(cherry.getLocation());
		
	    ArrayList<Point> blacklist = new ArrayList<>();
	    if(walls.isCollidable()) blacklist.addAll(walls.getWalls());
	    blacklist.addAll(snake_P1.getSnakePoints());
	    blacklist.addAll(snake_P2.getSnakePoints());
               
	    if(isBot) bot.control(blacklist);
	    
	    boolean isHitP1 = false;
	    if(!(isHitP1 = snake_P1.isCollision(blacklist)) && !snake_P2.isCollision(blacklist)){
                snake_P1.move();
	        snake_P2.move();
                    
	        boolean isCherryP1 = false;
	        if((isCherryP1 = snake_P1.getHead().equals(cherry.getLocation())) || snake_P2.getHead().equals(cherry.getLocation())){
		    engine.sounds.play("cherry");
		    if(isCherryP1){
		        score_P1 += baseScore*difficulty;
		        applyEffect(snake_P1);
		    }else{
		        score_P2 += baseScore*difficulty;
		        applyEffect(snake_P2);
		    }
		    cherry = new Cherry(blacklist);
		    if(isBot) bot.changeGoal(cherry.getLocation());
                }    
            }else{
		engine.sounds.stop("match");
		engine.sounds.play("hit");
		
		if(isHitP1) score_P1 -= baseScore*difficulty+((time+1)*0.2F);
		else score_P2 -= baseScore*difficulty+((time+1)*0.2F);
		
		if(score_P1 == score_P2) winner = "It's a Draw";
		else if(score_P1 > score_P2) winner = "Player 1";
		else winner = isBot ? "Bot":"Player 2"; 
		
		isOver = true;
	    }
        }
    }

    @Override
    public void draw(Graphics2D g){
	snake_P1.draw(g);
	snake_P2.draw(g);
	cherry.draw(g);
	walls.draw(g);
	
	g.setFont(scoreFont);
	g.setColor(snake_P1.getColor());
        g.drawString("Score: " + score_P1, 10, 20);
	g.setColor(snake_P2.getColor());
	g.drawString("Score: " + score_P2, 10, 40);
	g.setColor(overColor);
	g.drawString("Time: " + time, 10, height-10);
        
	g.setFont(warningFont);
        if(isOver){
            g.setColor(overColor);
            g.drawString(overMsg, width/2-g.getFontMetrics(warningFont).stringWidth(overMsg)/2, height/2);
	    g.setFont(msgFont);
	    g.drawString(msg, width/2-g.getFontMetrics(msgFont).stringWidth(msg)/2, height/2+40);
	    g.drawString(winner, width/2-g.getFontMetrics(msgFont).stringWidth(winner)/2, height/2+80);
        }else if(isPaused){
	    g.setColor(pauseColor);
	    g.drawString(pauseMsg, width/2-g.getFontMetrics(warningFont).stringWidth(pauseMsg)/2, height/2);
	}
    }

    @Override
    public void keyPressed(KeyEvent e){
	int key = e.getKeyCode();
	switch (key) {
	    case KeyEvent.VK_W:
		snake_P1.changeDirection(Snake.UP);
		break;
	    case KeyEvent.VK_S:
		snake_P1.changeDirection(Snake.DOWN);
		break;
	    case KeyEvent.VK_A:
		snake_P1.changeDirection(Snake.LEFT);
		break;
	    case KeyEvent.VK_D:
		snake_P1.changeDirection(Snake.RIGHT);
		break;
	    case KeyEvent.VK_SPACE:
		if(!isOver) setPaused(!isPaused);
		break;
	    case KeyEvent.VK_ESCAPE:
		start();
		setPaused(false);
		break;
	    case KeyEvent.VK_MINUS:
		engine.sounds.ajustMasterVolume(-5F);
		break;
	    case KeyEvent.VK_EQUALS:
		engine.sounds.ajustMasterVolume(5F);
		break;
	    case KeyEvent.VK_0:  //REMOVE: testing...
		engine.states.startState(IDLE);
		break;
	    case KeyEvent.VK_1:  //REMOVE: testing...
		engine.states.startState(SINGLE);
		break;
	}
	
	if(!isBot){
	    switch(key){
		case KeyEvent.VK_UP:
		    snake_P2.changeDirection(Snake.UP);
		    break;
		case KeyEvent.VK_DOWN:
		    snake_P2.changeDirection(Snake.DOWN);
		    break;
		case KeyEvent.VK_LEFT:
		    snake_P2.changeDirection(Snake.LEFT);
		    break;
		case KeyEvent.VK_RIGHT:
		    snake_P2.changeDirection(Snake.RIGHT);
		    break;
	    }
	}
    }
    
    private void applyEffect(Snake snake){
	switch(cherry.getEffect()){
	    case Cherry.GROW:
		snake.grow();
		break;
	    case Cherry.SHRINK:
		snake.shrink();
		break;
	}
    }

    @Override
    public void setPaused(boolean isPaused){
	this.isPaused = isPaused;
	if(isPaused) engine.sounds.stop("match");
	else engine.sounds.loop(
		"match", 
		engine.sounds.getPosition("match"), 
		600, 
		engine.sounds.getFrames("match") - 2000
	    );
    }

    public void setDifficulty(int difficulty){this.difficulty = difficulty;}
    public void vsBot(boolean isBot){this.isBot = isBot;}
    public int getScore_P1(){return score_P1;}
    public int getScore_P2(){return score_P2;}
    public int getTime(){return time;}  

    @Override
    public String getStateID() {
	return MULT;
    }
    
    @Override
    public void mousePressed(MouseEvent e){}
    
    @Override
    public void keyReleased(KeyEvent e){}

}
