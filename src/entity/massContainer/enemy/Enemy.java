package entity.massContainer.enemy;
import entity.mass.*;
import entity.massContainer.MassContainer;
import game.*;
import java.awt.*;
import screen.GameScreen;

public class Enemy extends MassContainer
{
	private double originalXVel;
	private double originalYVel;
	private int thirst;
	
	public Enemy(double xCoord, double yCoord, int numberOfMasses, double moveSpeed, int thirst)
	{
		super(xCoord,yCoord,0,moveSpeed);
		color = new Color(255,255,255);
		originalXVel = xVel;
		originalYVel = yVel;
		this.numberOfMasses = numberOfMasses;		
		bounds.setLocation((int)xCoord, (int)yCoord);
		genInitialMass(numberOfMasses);
		calcRadius(numberOfMasses);
		setMass();
		health = listOfMass.size()*5;
		this.thirst = thirst;
	}
	
	public void draw(Graphics g)
	{
		super.draw(g);
		g.setColor(color);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(4));
		
		g2d.drawOval((int)(xCoord-radius),(int)(yCoord-radius),width,height);
		g2d.setColor(new Color(g2d.getColor().getRed(),g2d.getColor().getBlue(),g2d.getColor().getGreen(),40));
		g2d.fillOval((int)(xCoord-radius),(int)(yCoord-radius),width,height);
	}
	
	public boolean update(double delta)
	{
		super.update(delta);
		xCoord -= xVel * .5;
		yCoord -= yVel * .5;
		
		if(offScreen())
			return false;
		if(removed) 
			return false;
		
		if (health <= 0)
		{
			for(int i = 0; i < numberOfMasses; i++)
			{
				if(listOfMass.size() > 0)
				{
					Mass mass = removeMass();
					((GameScreen)Game.game.getScreen()).getListOfEntities().add(mass);
					mass.setContainer(null);
				}
			}
			//for(int i = 0; i < numberOfMasses; i++)
			//	Game.game.getScreen().getListOfEntities().add(new Mass(xCoord,yCoord,Math.random()*360));
			return false;
		}
		
		bounds.setBounds((int)(xCoord-width/2), (int)(yCoord-height/2), width, width);
		if(listOfMass.size() == 0)
			return false;
		if(Utility.dist(0, 0, xCoord, yCoord) <= ((GameScreen)Game.game.getScreen()).getEquinox().getRadius() + width/2)
		{
			if(listOfMass.size() < thirst)
			{
				if(Game.game.getCurrentTicks() % 20 == 0)
				{
					Sound.MASS_TAKE.play();
					Mass mass = ((GameScreen)Game.game.getScreen()).getEquinox().getListOfMass().remove(0);
					listOfMass.add(mass);
					mass.setContainer(this);
				}
				xCoord = (((GameScreen)Game.game.getScreen()).getEquinox().getRadius() + radius)*Math.cos(angle); 
				yCoord = (((GameScreen)Game.game.getScreen()).getEquinox().getRadius() + radius)*Math.sin(angle);
			}
			else
			{
				xVel = -originalXVel;
				yVel = -originalYVel;
			}
		}
		else if(listOfMass.size() < thirst)
		{
			xVel = originalXVel;
			yVel = originalYVel;
		}
		
		massSpeed = radius/200;
		calcRadius(numberOfMasses);
		return true;
	}
	
	//calculates radius by adding up each mass's circumference.
	public void calcRadius(int mass)
	{
		double ratio = 1.5;
		super.calcRadius(mass,ratio);
	}
	
	public void setColor(Color c){color = c;}
}