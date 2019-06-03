package entity.cannon;
import entity.*;
import entity.mass.*;
import game.*;

import java.awt.*;
import screen.*;

public class MassCannon extends Cannon
{
	protected long fireTimer;
	private boolean canFire;
	private int numBullets;
	
	public MassCannon(double xCoord, double yCoord, int width, int height)
	{
		super(xCoord,yCoord,0,0);
		fireTimer = System.currentTimeMillis();
		canFire = true;
		numBullets = 1;
	}
	
	public void draw(Graphics g)
	{
		g.setColor(Color.BLUE);
		super.draw(g);
	}
	
	public boolean update(double delta)
	{
		angle += rotVel;
		rotVel *= Math.pow(.80,delta);
		xCoord = (((GameScreen)Game.game.getScreen()).getEquinox().getRadius() * Math.cos(angle) + (width/2*Math.sin(angle)));
		yCoord = (((GameScreen)Game.game.getScreen()).getEquinox().getRadius() * Math.sin(angle) - (width/2*Math.cos(angle)));
//		xCoord = ((GameScreen)Game.game.getScreen()).getEquinox().getRadius() * Math.cos(angle);
//		yCoord = ((GameScreen)Game.game.getScreen()).getEquinox().getRadius() * Math.sin(angle);
		
		width  = (int)(.15*((GameScreen)Game.game.getScreen()).getEquinox().getRadius());
		height = (int)(.32*((GameScreen)Game.game.getScreen()).getEquinox().getRadius());
		
		bounds.setBounds((int)xCoord, (int)yCoord, width, height);
		
		if(Game.game.input.left)
			moveCounterClock();
		if(Game.game.input.right)
			moveClock();
		
		if(!canFire)
		{
			if(System.currentTimeMillis() - fireTimer > 60*fireRate)
			{
				canFire = true;
				fireTimer = System.currentTimeMillis();
			}
		}
		if(Game.game.input.fire && canFire)
		{
			numBullets = 1;
			fire();
			canFire = false;
		}
		else if (Game.game.input.shotgun && canFire)
		{
			numBullets = 5;
			fire();
			canFire = false;
		}
		return true;
	}
	
	public void fire()
	{
		for(int i = 0; i < numBullets; i++)
		{
			if(((GameScreen)Game.game.getScreen()).getEquinox().getListOfMass().size() > 0)
			{
				Mass mass = ((GameScreen)Game.game.getScreen()).getEquinox().getListOfMass().get(0);
				//mass.moveToCannon();
				
				double massXCoord = ((GameScreen)Game.game.getScreen()).getEquinox().getRadius()*Math.cos(angle);
				double massYCoord = ((GameScreen)Game.game.getScreen()).getEquinox().getRadius()*Math.sin(angle);
				
				((GameScreen)Game.game.getScreen()).getEquinox().getListOfMass().remove(mass);
				int additionalDamage = 0;
				for(Mass m: ((GameScreen)Game.game.getScreen()).getEquinox().getListOfMass())
					if(m instanceof DamageUpMass)
						additionalDamage++;
				
				if(numBullets == 1)
					((GameScreen)Game.game.getScreen()).getListOfEntities().add(new ProjectileMass(massXCoord, massYCoord, (int)mass.getRadius(), angle, mass.getColor(),additionalDamage));
				else
					((GameScreen)Game.game.getScreen()).getListOfEntities().add(new ProjectileMass(massXCoord, massYCoord, (int)mass.getRadius(), angle + Math.toRadians(Utility.negToPositive(20)), mass.getColor(),additionalDamage));
			}
		}
		fireTimer = System.currentTimeMillis();
		Sound.SHOOT.play();
	}
}