package entity.mass;
import java.awt.*;
import entity.*;
import entity.massContainer.*;
import entity.massContainer.enemy.*;
import game.*;

public class Mass extends Entity
{
	protected double xVel, yVel;
	protected int radius;
	public Color color;
	protected double moveSpeed = 1;
	protected MassContainer container;
	
	public Mass(double xCoord, double yCoord, MassContainer container)
	{
		super(xCoord, yCoord,0);
		//moveSpeed = container.getMassSpeed();
		if(container!=null)
		{
			xVel = moveSpeed * Math.cos(Math.random() * Math.toRadians(360)) + container.getXVel();
			yVel = moveSpeed * Math.sin(Math.random() * Math.toRadians(360)) + container.getYVel();
		}
		
		color = new Color((int) (Math.random() * 0xFFFFFF));
		
		//diameter = (Math.random() * 10) + 5;
		radius = (int)(((Math.random()*10) + 5) / 2);
		this.container = container;
	}
	
	public Mass(double xCoord, double yCoord,double angle)
	{
		this(xCoord,yCoord,null);
		//moveSpeed = container.getMassSpeed();
		xVel = moveSpeed * Math.cos(Math.toRadians(angle));
		yVel = moveSpeed * Math.sin(Math.toRadians(angle));
	}
	
	public Mass(MassContainer container)
	{
		this(container.getXCoord(), container.getYCoord(), container);
	}
	
	public void draw(Graphics g) 
	{
		if(container != null)
		{
			double colorModifier = Math.random();
			color = new Color((int)(colorModifier*container.getColor().getRed()), (int)(colorModifier*container.getColor().getGreen()),(int)(colorModifier*container.getColor().getBlue()));
			g.setColor(color);
		}
		else
			g.setColor(color);
		g.fillOval((int)(xCoord-radius), (int)(yCoord-radius), (int)radius*2, (int)radius*2);
		//g.drawLine((int)xCoord,(int)yCoord,(int)container.getXCoord(),(int)container.getYCoord());
	}
	
	public boolean update(double delta)
	{
		if(offScreen())
			return false;
		xCoord += xVel;
		yCoord += yVel;
		
		if(container!=null)
		{
			if(Utility.dist(container.getXCoord(), container.getYCoord(),xCoord, yCoord) + radius >= container.getRadius())
			{
				//translate to origin
				double CtempX = container.getXCoord();
				double CtempY = container.getYCoord();
				xCoord-=container.getXCoord();
				yCoord-=container.getYCoord();
				//calculate angle relative to container center
				double angle = Math.atan2(yCoord, xCoord);
				//re-translate
				xCoord+=container.getXCoord();
				yCoord+=container.getYCoord();
				container.setXCoord(CtempX);
				container.setYCoord(CtempY);
				
				double tempx = xVel;
				xVel = -yVel;
				yVel = tempx;
				xCoord -= 5 * Math.cos(angle);
				yCoord -= 5 * Math.sin(angle);
			}
		}
		
		else if(container == null)
		{
			for(Entity entity : Game.game.getScreen().getListOfEntities())
			{
				if(entity instanceof MassContainer && !(entity instanceof PowerUp) && !(entity instanceof LeakyEnemy))
				{
					MassContainer massCont = (MassContainer)entity;
					if(Utility.dist(massCont.getXCoord(), massCont.getYCoord(),xCoord, yCoord) + radius <= massCont.getRadius())
					{
						massCont.getListOfMass().add(this);
					
						(Math.random() > .5 ? Sound.MASS_ABSORB : Sound.POWERUP_ABSORB).play();
						container = massCont;
						return false;
					}
				}
			}
		}
		return true;
	} 

	
	public int getRadius(){return radius;}
	public double getXVel(){return xVel;}
	public void setXVel(double xVel){this.xVel = xVel;}
	public double getYVel(){return yVel;}
	public void setYVel(double yVel){this.yVel = yVel;}
	public void setContainer(MassContainer c){container = c;}

	public Color getColor(){return color;}
}