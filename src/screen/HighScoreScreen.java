package screen;
import game.Game;
import java.awt.*;
import java.io.*;
import java.util.*;
import uicomponent.Button;

public class HighScoreScreen extends MenuScreen
{
	public long ticksSurvived;

	private BufferedReader reader;

	private ArrayList<String> scoreList = new ArrayList<String>();

	private ArrayList<String> nameList = new ArrayList<String>();

	private String name;

	private static final String[] SWEAR_WORDS = {"test", "example"};

	private boolean hacker;

	public HighScoreScreen(String name, long ticksSurvived, boolean hacker)
	{
		this.ticksSurvived = ticksSurvived;
		this.name = name;
		if(hasProfanity(name) || name.equals(""))
			this.name = "Equinox Player";
		if(hacker)
			this.name = "HACKER";

		listOfComponents.add(new Button(0, -300, Game.WIDTH, 50, "HIGHSCORES", null, false));
		listOfComponents.add(new Button(Game.WIDTH / 2 - 50, 300, 100, 50, "TITLE", "TitleScreen"));

		updateHighScoresFile();
	}

	private void updateHighScoresFile()
	{
		boolean added = false;
		try
		{
			String userHome = System.getProperty("user.home");
			File highScoresFile = new File(userHome + "/equinox_highscores.txt");
			if(highScoresFile.exists())
			{
				System.out.println("Getting high scores from computer.");
				reader = new BufferedReader(new FileReader(userHome + "/equinox_highscores.txt"));
			}
			else
			{
				System.out.println("Getting high scores from jar.");
				reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/equinox_highscores.txt")));
			}
			for(int i = 0; i < 30; i++)
			{
				String info = reader.readLine();
				String[] splitInfo = info.split(":");
				String oldName = splitInfo[0];
				int oldScore = Integer.parseInt(splitInfo[1]);
				long newScore = ticksSurvived / 60;
				if(!added && newScore > oldScore)
				{
					scoreList.add(""+newScore);
					nameList.add(name);
					added = true;
					if(++i >= 30)
						break;
				}
				scoreList.add(""+oldScore);
				nameList.add(oldName);
			}
			reader.close();

			BufferedWriter writer = new BufferedWriter(new FileWriter(userHome + "/equinox_highscores.txt"));
			for(int i=0; i<30; i++)
				writer.write(nameList.get(i) + ":" + scoreList.get(i) + "\n");
			writer.close();

		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void draw(Graphics g)
	{
		super.draw(g);
		g.setColor(Color.RED);
		g.setFont(new Font("Courier New", Font.BOLD, 26));
		g.drawString("Online", -Game.WIDTH / 2, -Game.HEIGHT / 2 + 30);
		g.setColor(new Color(10, 179, 100));
		for(int i = 0; i < scoreList.size(); i++)
		{
			String space = "";
			for(int j = nameList.get(i).length(); j < 28; j++)
				space += " ";
			String space2 = "";
			for(int j = scoreList.get(i).length(); j < 5; j++)
				space2 += " ";
			if(i < 9)
				g.drawString(" " + (i + 1) + ": " + nameList.get(i) + space + scoreList.get(i)
							+ space2 + " seconds", -420, -250 + i * 21);
			else
				g.drawString((i + 1) + ": " + nameList.get(i) + space + scoreList.get(i) + space2
							+ " seconds", -420, -250 + i * 21);
		}
	}

	public boolean hasProfanity(String s)
	{
		for(String word: SWEAR_WORDS)
			if(s.toLowerCase().contains(word))
				return true;
		return false;
	}

	public ArrayList<String> getScoreList(){return scoreList;}
	public String getName(){return name;}
	public void setName(String n){name = n;}
	public void setTicksSurvived(long t){ticksSurvived = t;}
	public long getTicksSurvived(){return ticksSurvived;}
	public boolean getHacker(){return hacker;}
	public void setHacker(boolean b){hacker = b;}
}
