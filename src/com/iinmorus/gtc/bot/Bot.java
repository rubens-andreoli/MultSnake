package com.iinmorus.gtc.bot;


import com.iinmorus.gtc.entity.Snake;
import java.awt.Point;
import java.util.ArrayList;

public abstract class Bot {
    
    protected Point goal;
    protected final Snake snake;

    public Bot(Snake snake){
	this.snake = snake;
    }

    public void changeGoal(Point goal){
	this.goal = goal;
    }

    public abstract void control(ArrayList<Point> avoidPoints);
 
}