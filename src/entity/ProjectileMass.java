package entity;
import java.awt.*;
import screen.GameScreen;
import entity.massContainer.*;
import game.*;

public class ProjectileMass extends Entity
{
	private double damage;
	private double xVel, yVel;
	private Color color;
	
	public ProjectileMass(double xCoord, double yCoord, int radius, double angle, Color color, int additionalDamage)
	{
		super(xCoord,yCoord,radius*2,radius*2);
		xVel = 20 * Math.cos(angle);
		yVel = 20 * Math.sin(angle);	
		this.color = color;
		damage = radius * 4;
		damage += additionalDamage*20000;
	}
	
	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillOval((int)xCoord-width/2, (int)yCoord-width/2, (int)width, (int)height);
		g.setColor(Color.WHITE);
		//g.drawLine((int)(xCoord), (int)(yCoord), (int)(xCoord), (int)(yCoord));
	}
	
	public boolean update(double delta)
	{
		xCoord += xVel;
		yCoord += yVel;
		bounds.setBounds((int)xCoord-width/2, (int)yCoord-height/2, width, height);
		
		if(offScreen())
			return false;
	
		for(Entity ent : ((GameScreen)Game.game.getScreen()).getListOfEntities())
		{
			if(ent != this && ent instanceof MassContainer) 
			{
				MassContainer mc = (MassContainer)ent;
				if(Utility.dist(xCoord,yCoord,mc.getXCoord(),mc.getYCoord()) <= mc.getRadius() + width/2)
				{
					Game.game.getScreen().shakeScreen();
					Sound.ENTITY_HIT.play();
					mc.setHealth(mc.getHealth() - damage);
					return false;
				}
			}
		}
		return true;
	}
}