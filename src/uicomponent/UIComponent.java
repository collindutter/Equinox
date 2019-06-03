package uicomponent;
import game.*;
import java.awt.*;

public abstract class UIComponent extends Rectangle
{
	private static final long serialVersionUID = 1L;
	protected int xCoord, yCoord;
	protected int width, height;
	protected Color backgroundColor, edgeColor, textColor;
	protected boolean clickable;
	
	public UIComponent(int xCoord, int yCoord, int width, int height, boolean clickable)
	{
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.height = height;
		this.width = width;
		setBounds(xCoord-width/2,yCoord-height/2,width,height);
		backgroundColor = Color.LIGHT_GRAY;
		edgeColor = Color.LIGHT_GRAY;
		textColor = Color.LIGHT_GRAY;
		this.clickable = clickable;
	}
	
	public void update()
	{
		int mouseX = Input.mouseX;
		int mouseY = Input.mouseY;
		if(clickable && contains(mouseX,mouseY))//mouse over
		{
			backgroundColor = new Color(0x666666);
			edgeColor = new Color(10,179,100);
			textColor = Color.BLACK;
		}
		else //normal state
		{
			edgeColor = Color.DARK_GRAY;
			backgroundColor = Color.DARK_GRAY;
			textColor = Color.BLACK;
		}
	}
	
	public abstract void draw(Graphics g);
	
	
	public void click()
	{ 
		Sound.CLICK.play();
	}
	
	public boolean getClickable(){return clickable;}
	public double getXCoord(){return xCoord;}
	public double getYCoord(){return yCoord;}
}