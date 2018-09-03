package com.iinmorus.gtc.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import static com.iinmorus.gtc.ui.GameWindow.ENGINE;

public class Cherry extends Drawable{
    
    private Point location;

    public static final int GROW=1, SHRINK=2, SPRINT=3, IMUNE=4;
    private int effect;

    public Cherry() {
	this(null);
    }
    
    public Cherry(ArrayList<Point> pointsBlackList){
	color = new Color(222, 49, 99);
	Random random = new Random();
	location = new Point(random.nextInt(ENGINE.settings.width/SCALE), random.nextInt(ENGINE.settings.height/SCALE));
	if(pointsBlackList != null){
	    while(pointsBlackList.contains(location)){
		location = new Point(random.nextInt(ENGINE.settings.width/SCALE), random.nextInt(ENGINE.settings.height/SCALE));
	    }
	}
	effect = random.nextInt(5);
    }

    @Override
    public void draw(Graphics2D g){
	Color color = this.color.darker();
	for(int i=0; i< effect; i++){
	    color.darker();
	}
	g.setColor(color);
	g.fillRect(location.x*SCALE, location.y*SCALE, SCALE, SCALE);
    }

    public int getEffect(){
	return effect;
    }

    public Point getLocation(){
	return location;
    }
    
}