package entity.cannon;
import entity.ProjectileMass;
import entity.mass.DamageUpMass;
import entity.mass.Mass;
import game.*;
import screen.GameScreen;

public class ShotgunMassCannon extends MassCannon
{
	public ShotgunMassCannon(double xCoord, double yCoord, int width, int height)
	{
		super(xCoord,yCoord,width,height);
	}
	
	public void fire()
	{
		for(int i=0; i<5; i++)
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
				
				((GameScreen)Game.game.getScreen()).getListOfEntities().add(new ProjectileMass(massXCoord, massYCoord, (int)mass.getRadius(), angle + Math.toRadians(Utility.negToPositive(20)), mass.getColor(),additionalDamage));
				
			}
		}
		fireTimer = System.currentTimeMillis();
		Sound.SHOOT.play();
	}
}