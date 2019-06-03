package screen;
import game.Game;
import java.awt.*;
import uicomponent.Button;

public class CreditsScreen extends MenuScreen
{
	public CreditsScreen()
	{
		listOfComponents.add(new Button(0,-300,Game.WIDTH,50,"CREDITS",null,false));
		listOfComponents.add(new Button(Game.WIDTH/2-50,300,100,50,"BACK","OptionScreen"));
	}
	
	public void draw(Graphics g)
	{
		super.draw(g);
		//g.setFont(new Font("Courier New",Font.PLAIN,12));
		g.setColor(Color.WHITE);
		g.drawString("Equinox© was made by Collin Dutter and Kyle Kovacs",-250,-80);
		g.drawString("for an APCS final project in 2013.", -250, -60);
	}
}