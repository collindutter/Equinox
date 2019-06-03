package entity.cannon;
import java.awt.*;
import screen.GameScreen;
import game.*;
import entity.mass.*;

public class GrappleCannon extends Cannon
{
	double mouseX;
	double mouseY;
	double mouseAngle;
	private Grapple grapple;
	private Color color;
	
	public GrappleCannon(double xCoord,double yCoord,int width,int height)
	{
		super(xCoord,yCoord,width,height);
		color = new Color(0xFFFF);
	}
	
	public void draw(Graphics g)
	{
		g.setColor(color);
		super.draw(g);
		if(grapple!=null)
			grapple.draw(g);
	}
	
	public boolean update(double delta)
	{
		mouseX = Input.mouseX;
		mouseY = Input.mouseY;
//		Point mousePoint = MouseInfo.getPointerInfo().getLocation();
//		mouseX = mousePoint.x - Game.game.getLocationOnScreen().x - Game.WIDTH/2;
//		mouseY = mousePoint.y - Game.game.getLocationOnScreen().y - Game.HEIGHT/2;
		mouseAngle = Math.atan2(mouseY, mouseX);
		
		if(grapple == null)
			angle = mouseAngle;
		
		xCoord = ((GameScreen)Game.game.getScreen()).getEquinox().getRadius() * Math.cos(angle) + (width/2)*Math.sin(angle);
		yCoord = ((GameScreen)Game.game.getScreen()).getEquinox().getRadius() * Math.sin(angle) - (width/2)*Math.cos(angle);
		
		width  = (int)(.15*((GameScreen)Game.game.getScreen()).getEquinox().getRadius());
		height = (int)(.32*((GameScreen)Game.game.getScreen()).getEquinox().getRadius());
		
		bounds.setBounds((int)xCoord, (int)yCoord, width, height);
		
		if(Game.game.input.grappleLeft)
			moveCounterClock();
		if(Game.game.input.grappleRight)
			moveClock();
		
		if(grapple != null)
		{
			grapple.update(delta);
			if(grapple.getTipX()>Game.WIDTH/2+30||grapple.getTipX()<-Game.WIDTH/2-30||grapple.getTipY()>Game.HEIGHT/2+30||grapple.getTipY()<-Game.HEIGHT/2-30)
			{
				grapple.toggle(true);
				return true;
			}
			if(grapple.getGrapplePoints().size() == 0 && grapple.getVictim() == null)
				grapple = null;
		}
		
		return true;
	}
	
	public void moveCounterClock()
	{
		rotVel-=.05;
	}
	
	public void moveClock()
	{
		rotVel+=.05;
	}
	
	public void fire()
	{
		if(Math.abs((((GameScreen) Game.game.getScreen()).getEquinox().getRadius() + height) * Math.cos(angle)) < Game.WIDTH/2 && Math.abs((((GameScreen) Game.game.getScreen()).getEquinox().getRadius() + height) * Math.sin(angle)) < Game.HEIGHT/2)
		{
			int additionalGrappleSpeed = 0;
			for(Mass m: ((GameScreen)Game.game.getScreen()).getEquinox().getListOfMass())
				if(m instanceof GrappleSpeedUpMass)
					additionalGrappleSpeed++;
			int additionalTransferSpeed = 0;
			for(Mass m: ((GameScreen)Game.game.getScreen()).getEquinox().getListOfMass())
				if(m instanceof TransferSpeedUpMass)
					additionalTransferSpeed++;
			if(grapple == null)
			{
				grapple = new Grapple(angle,additionalGrappleSpeed,additionalTransferSpeed);
				Sound.GRAPPLE.play();
			}
		}
		else
			grapple = null;
	}
	
	public Grapple getGrapple(){return grapple;}
}