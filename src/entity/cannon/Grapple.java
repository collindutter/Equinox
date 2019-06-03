package entity.cannon;
import game.*;
import entity.Entity;
import entity.mass.Mass;
import entity.massContainer.*;
import java.awt.*;
import screen.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public class Grapple
{
	private double baseX, baseY,
					tipX, tipY;
	private double angle;
	private List<Point2D.Double> grapplePoints = new ArrayList<Point2D.Double>();
	private double time1 = .265*((GameScreen) Game.game.getScreen()).getEquinox().getRadius();
	private double time2 = time1/2;
	private double time3 = .67*((GameScreen) Game.game.getScreen()).getEquinox().getRadius();
	private double grappleSpeed = 6;
	private boolean reversed;
	private MassContainer victim;
	private int transferSpeed = 30;
	
	public Grapple(double angle, int additionalGrappleSpeed, int additionalTransferSpeed)
	{
		this.grappleSpeed += additionalGrappleSpeed*4;
		this.transferSpeed -= additionalTransferSpeed*10;
		if(transferSpeed<5)
			transferSpeed = 5;
		this.angle = angle;
		baseX = ((GameScreen) Game.game.getScreen()).getEquinox().getRadius() * Math.cos(angle);
		baseY = ((GameScreen) Game.game.getScreen()).getEquinox().getRadius() * Math.sin(angle);
		tipX = baseX;
		tipY = baseY;
	}

	public void update(double delta)
	{
		if(!reversed)
			extend();
		else
			retract();
		
		for(Entity entity: ((GameScreen) Game.game.getScreen()).getListOfEntities())
		{
			if(entity instanceof MassContainer && !(entity instanceof Equinox))
			{
				MassContainer mc = ((MassContainer)entity);
				if(Utility.dist(tipX, tipY, mc.getXCoord(), mc.getYCoord()) < mc.getRadius()+8)
				{
					reversed = true;
					mc.setXCoord(tipX + mc.getRadius()*Math.cos(angle));
					mc.setYCoord(tipY + mc.getRadius()*Math.sin(angle));
					victim = mc;
					break;
				}
			}
		}
		
		if(victim != null)
		{
			if(grapplePoints.size() == 0)
			{
				if(victim.getListOfMass().size() > 0 && victim.getHealth() > 0)
				{
					GrappleCannon grappleCannon = ((GameScreen) Game.game.getScreen()).getGrappleCannon();
					Equinox eq = ((GameScreen) Game.game.getScreen()).getEquinox();
					if(Game.game.getCurrentTicks() % transferSpeed == 0)
					{
						(Math.random() > .5 ? Sound.MASS_ABSORB : Sound.POWERUP_ABSORB).play();
						Mass mass = victim.removeMass();
						mass.setXCoord(eq.getRadius()*Math.cos(grappleCannon.getAngle())-4);
						mass.setYCoord(eq.getRadius()*Math.sin(grappleCannon.getAngle())-4);
						if(eq.getListOfMass().size() >= 20)
							eq.getListOfMass().add(20,mass);
						else
							eq.getListOfMass().add(mass);
						mass.setContainer(eq);
					}
					victim.setXCoord((eq.getRadius()+(grappleCannon.getHeight())+victim.getRadius())*Math.cos(grappleCannon.getAngle()));
					victim.setYCoord((eq.getRadius()+(grappleCannon.getHeight())+victim.getRadius())*Math.sin(grappleCannon.getAngle()));
				}
				else
					victim = null;
			}
		}
	}
	
	private void retract()
	{
		if(!grapplePoints.isEmpty())
		{
			int size = grapplePoints.size();
			for(int i=size-1; i>=size-(grappleSpeed*6); i--)
			{
				tipX = grapplePoints.get(i).x;
				tipY = grapplePoints.get(i).y;
				grapplePoints.remove(i);
			}
		}
	}
	
	private void extend()
	{
		//inner braid
		for(int i=0;i<grappleSpeed;i++)
		{
			double xCurve1 = 5*(time1*Math.cos(angle) - Math.sin(time1)*Math.sin(angle));
			double yCurve1 = 5*(Math.sin(time1)*Math.cos(angle) + time1*Math.sin(angle));
			grapplePoints.add(new Point2D.Double(xCurve1,yCurve1));
			
			double angle2 = Math.toRadians(90) - angle;
			double yCurve2 = 5*(time1*Math.cos(angle2) - Math.sin(time1)*Math.sin(angle2));
			double xCurve2 = 5*(Math.sin(time1)*Math.cos(angle2) + time1*Math.sin(angle2));
			grapplePoints.add(new Point2D.Double(xCurve2,yCurve2));
			
			time1+=.16;
		}
		//outer braid
		for(int i=0;i<grappleSpeed;i++)
		{
			double xCurve1 = 10*(time2*Math.cos(angle) - Math.sin(time2)*Math.sin(angle));
			double yCurve1 = 10*(Math.sin(time2)*Math.cos(angle) + time2*Math.sin(angle));
			grapplePoints.add(new Point2D.Double(xCurve1,yCurve1));
			
			double angle2 = Math.toRadians(90) - angle;
			double yCurve2 = 10*(time2*Math.cos(angle2) - Math.sin(time2)*Math.sin(angle2));
			double xCurve2 = 10*(Math.sin(time2)*Math.cos(angle2) + time2*Math.sin(angle2));
			grapplePoints.add(new Point2D.Double(xCurve2,yCurve2));
			
			time2+=.08;
		}
		//lines
		for(int i=0;i<grappleSpeed;i++)
		{
			double xLine1 = 2*((time3-1)*Math.cos(angle)-((-3.1/time3)+3.1)*Math.sin(angle));
			double yLine1 = 2*((time3-1)*Math.sin(angle)+((-3.1/time3)+3.1)*Math.cos(angle));
			grapplePoints.add(new Point2D.Double(xLine1,yLine1));
			
			double angle3 = Math.toRadians(90) - angle;
			double yLine2 = 2*((time3-1)*Math.cos(angle3)-((-3.1/time3)+3.1)*Math.sin(angle3));
			double xLine2 = 2*((time3-1)*Math.sin(angle3)+((-3.1/time3)+3.1)*Math.cos(angle3));
			grapplePoints.add(new Point2D.Double(xLine2,yLine2));
			
			tipX = xLine1;
			tipY = yLine1;
			
			time3+=.4;
		}
	}
	
	public void draw(Graphics g)
	{
 		g.setColor(new Color(0xFFFF));
 		
 		for(Point2D.Double point: grapplePoints)
		{
 			g.fillOval((int)point.x,(int)point.y,1,1);
		}
	}
	
	public MassContainer getVictim(){return victim;}
	public void toggle(boolean r){reversed = r;}	
	public double getTipX(){return tipX;}
	public double getTipY(){return tipY;}
	public double getBaseX(){return baseX;}
	public double getBaseY(){return baseY;}
	public List<Point2D.Double> getGrapplePoints(){return grapplePoints;}
}