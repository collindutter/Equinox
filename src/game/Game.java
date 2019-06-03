package game;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.*;
import javax.swing.*;
import screen.*;
import uicomponent.*;

public class Game extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;
	public  static final int WIDTH = 1000,
							HEIGHT = WIDTH * 3/4;
	public  static double scale = 1;
	private static long ticks = 0, playTicks = 0;
	//public  static String folderPath = System.getProperty("user.home") + "/Equinox Data";
	private static Screen currentScreen;
	public  static Map<String,Screen> screens = new HashMap<String,Screen>();
	public  static Map<String,Boolean> settings = new HashMap<String,Boolean>();
	public  static final Random rand = new Random();
	private JFrame frame = new JFrame("Equinox-ALPHA BUILD");
	public  boolean running;
	public  Thread thread;
	public  Input input;
	private int fps;
	public static Game game;
	private int fpsToBeDrawn;
	
	public static void main(String[] args)
	{
		try{Thread.sleep(100);}catch(InterruptedException e){e.printStackTrace();}
		game = new Game();
		game.frame.add(game);
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setResizable(false);
		game.frame.pack();
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		//game.createAndSetCursor();
		game.changeCursor(Cursor.CROSSHAIR_CURSOR);
		game.startGame();
	}
	
	public Game()
	{
		frame.addWindowListener(new WindowAdapter() 
		{
			  public void windowClosing(WindowEvent e)
			  {
				  stopGame();
			  }
		});
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setFocusable(true);
		setIgnoreRepaint(true);
		requestFocus();
		input = new Input();
		addKeyListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		Sound.init();
		
		loadSettings();
		settings.put("antialiasing", true);
		settings.put("sound", true);
		settings.put("fps", false);
		
		screens.put("LevelScreen", new LevelScreen());
		screens.put("TutorialScreen", new TutorialScreen(0));
		screens.put("EnemyProfileScreen", new EnemyProfileScreen(0));
		screens.put("GameScreen", new GameScreen());
		screens.put("OptionScreen", new OptionScreen());
		screens.put("SurvivalGame", new SurvivalGame());
		screens.put("CreditsScreen", new CreditsScreen());
		screens.put("GraphicsScreen", new GraphicsScreen());
		screens.put("SoundScreen", new SoundScreen());
		screens.put("TitleScreen", new TitleScreen());
		screens.put("HighScoreScreen", new HighScoreScreen("",0,false));

	}
	
	public void run()
	{
		long lastTime = System.nanoTime();
		double delta = 0.0;
		double nsPerTick = 1000000000.0 / 60.0;
		long startTimer = System.currentTimeMillis();
//		int frames = 0;
		
		while(running)
		{
			if(currentScreen == null)
				setScreen(screens.get("TitleScreen"));
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			while(delta >= 1)
			{
				update(delta);
				ticks++;
				if(currentScreen instanceof GameScreen)
					playTicks++;
				delta--;
			}
			draw();
			fps++;
			
			try
			{
				Thread.sleep(3);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			if(System.currentTimeMillis() - startTimer > 1000)
			{
				fpsToBeDrawn = fps;
				startTimer += 1000;
				fps = 0;
			}
		}
	}
	
	public void draw()
	{
		BufferStrategy bs = getBufferStrategy();
		if(bs == null)
		{
			createBufferStrategy(2);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		Graphics2D g2d = (Graphics2D)g;
		if(settings.get("antialiasing"))
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.fillRect(0,0,WIDTH,HEIGHT);
		g.translate(WIDTH/2, HEIGHT/2);
		currentScreen.draw(g);
		if(settings.get("fps"))
		{
			g.setFont(new Font("Courier New",Font.BOLD,20));
			g.setColor(Color.RED);
			g.drawString(String.valueOf(fpsToBeDrawn),Game.WIDTH/2-40, -Game.HEIGHT/2+25);
		}
		g.dispose();
		bs.show();
	}
	
	public void update(double delta)
	{
		input.updateKeys();
		if(!isFocusOwner())
		{
			input.releaseAllKeys();
			if(currentScreen instanceof GameScreen)
				setScreen(screens.get("OptionScreen"));
		}
		if(input.pause && !(currentScreen instanceof DeathScreen))
			setScreen(screens.get("OptionScreen"));
		currentScreen.update(delta);
		
		for(UIComponent ui: currentScreen.getListOfComponents())
		{
			if(!ui.getClickable())
				break;
			else if(ui.getBounds().contains(new Point(Input.mouseX, Input.mouseY)))
			{
				if(Game.game.frame.getCursor().getType() != Cursor.HAND_CURSOR)
					Game.game.changeCursor(Cursor.HAND_CURSOR);
				return;
			}
		}
		if(frame.getCursor().getType() != Cursor.CROSSHAIR_CURSOR)
			changeCursor(Cursor.CROSSHAIR_CURSOR);
	}
	
	
	
	private void loadSettings()
	{
		
	}
	
	public void changeCursor(int type)
	{
		try{frame.setCursor(new Cursor(type));}
		catch(Exception e){e.printStackTrace();}
	}
	
	public int getCursorType()
	{
		return frame.getCursor().getType();
	}
	
	public void stopGame()
	{
		running = false;
		try
		{
			thread.join();
		}
		catch(InterruptedException e){e.printStackTrace();}
	}
	
	public void startGame()
	{
		running = true;
		thread = new Thread(this,"Game");
		thread.start();
	}
	
	public static void setScreen(Screen screen)
	{
		currentScreen = screen;
		//if(game.getListeners(0))
		//game.addKeyListener(screen);
	}
	
	public Screen getScreen(){return currentScreen;}
	public long getCurrentTicks(){return ticks;}
	public static long getPlayTicks(){return playTicks;}
	public static void setPlayTicks(long ticks){playTicks = ticks;}
}