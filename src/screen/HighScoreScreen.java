package screen;
import game.Game;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import uicomponent.Button;

public class HighScoreScreen extends MenuScreen
{
	public long ticksSurvived;
	private BufferedReader reader;
	private ArrayList<String> scoreList = new ArrayList<String>();
	private ArrayList<String> nameList = new ArrayList<String>();
	private String name;
	private static final String[] SWEAR_WORDS = {"fuck","shit","cunt","bitch","fag","nigger","tard","blaze","tit", 
        "porn","vag","pussy","cock","whore","slut","hole","dick","masturbate","pubes","suck","gay","sex", 
        "lesbian","balls","butt","prostitute","anal","cum","jizz","semen","naked","anus","bastard","beaner", 
        "boner","clit","hell","crap","shat","shart","dildo","douche","dumb","gooch","jerk","jew","muff", 
        "negro","piss","prick","queef","queer","skank","weed","dope","blow","meth","wank","nuts", 
        "taint","boob","turd","fart","fap","condom","bone","scrotum"}; 
	private boolean hacker;
	
	public HighScoreScreen(String name, long ticksSurvived, boolean hacker)
	{
		this.ticksSurvived = ticksSurvived;
		this.name = name;
		if(hasProfanity(name) || name.equals(""))
			this.name = "Equinox Player";
		if(hacker)
			this.name = "HACKER";
		
		listOfComponents.add(new Button(0,-300,Game.WIDTH,50,"HIGHSCORES",null,false));
		listOfComponents.add(new Button(Game.WIDTH/2-50,300,100,50,"TITLE","TitleScreen"));
		
		
		try
		{
			sendPost(""+ticksSurvived/60,this.name);
			
			for(int i = 0; i < 30; i++)
			{
				System.out.println("reading...");
				String info = reader.readLine();
				System.out.println(info);
				String[] splitInfo = info.split(":");
				scoreList.add(splitInfo[0]);
				nameList.add(splitInfo[1]);
			}
			reader.close();
		}
		catch(IOException e){e.printStackTrace();}
	}
	
	public void draw(Graphics g)
	{
		super.draw(g);
		g.setColor(Color.RED);
		g.setFont(new Font("Courier New",Font.BOLD,26));
		g.drawString("Online", -Game.WIDTH/2, -Game.HEIGHT/2 + 30);
		g.setColor(new Color(10,179,100));
		for(int i=0; i<scoreList.size(); i++)
		{
			String space = "";
			for(int j=nameList.get(i).length(); j<28; j++)
				space += " ";
			String space2 = "";
			for(int j=scoreList.get(i).length(); j<5; j++)
				space2 += " ";
			if(i<9)
				g.drawString(" " + (i+1) +": " + nameList.get(i) + space + scoreList.get(i) + space2 + " seconds",-420,-250+i*21);
			else
				g.drawString((i+1) + ": " + nameList.get(i) + space + scoreList.get(i) + space2 + " seconds",-420,-250+i*21);
		}
	}
	
	public boolean hasProfanity(String s)
	{
		for(String word: SWEAR_WORDS)
			if(s.toLowerCase().contains(word))
				return true;
		return false;
	}
	
	private void sendPost(String name, String score) throws IOException
	{
		URL url = new URL("http://playequinox.tk/highscores.py");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		
		DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
		
		writer.writeBytes(name + "\n" + score);
		writer.flush();
		writer.close();
		
		
		//this is just reading null
		reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		System.out.println();
	}
	
	public ArrayList<String> getScoreList(){return scoreList;}	
	public String getName(){return name;}
	public void setName(String n){name = n;}
	public void setTicksSurvived(long t){ticksSurvived = t;}
	public long getTicksSurvived(){return ticksSurvived;}
	public boolean getHacker(){return hacker;}
	public void setHacker(boolean b){hacker = b;}
}