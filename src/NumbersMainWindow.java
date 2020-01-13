import java.awt.Button;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class NumbersMainWindow  extends Frame  implements WindowListener, ActionListener, KeyListener, MouseListener
{
	Grid gameGrid;
	GameTicker gameTickerThread;
	MenuItem miStart;;
	MenuItem miInstructions;
	MenuItem miExit;
	MenuItem miHighScores;
	Button finishButton;
	Label scoreLabel;
	Label levelLabel;
	Label minBlockSizeLabel;
	int score;
	int level;
	
	NumbersMainWindow()
	{
		this.setVisible(true);
		this.setResizable(false);
		this.setSize(280,600);
		this.setLocation(100, 300);
		//this.setBackground(Color.BLUE);
		this.addWindowListener(this);	
		this.addMouseListener(this);
        
		finishButton = new Button("Complete Level");
		
		finishButton.setLabel("Complete Level");
		this.add(finishButton);
		finishButton.addActionListener(this);
		
		finishButton.setVisible(false);
		
		scoreLabel = new Label("Score");
		scoreLabel.setText("Score:0");
		this.add(scoreLabel);
		scoreLabel.setVisible(false);	
		
		levelLabel = new Label("Level");
		levelLabel.setText("Level:1");
		this.add(levelLabel);
		levelLabel.setVisible(false);	
		
		minBlockSizeLabel = new Label("MinBlockSize");
		minBlockSizeLabel.setText("Minimum block size for this level:");
		this.add(minBlockSizeLabel);
		minBlockSizeLabel.setVisible(false);	
			
		//create menu
		MenuBar mb = new MenuBar();
		Menu gameMenu =new Menu();
		gameMenu.setName("Game");
		gameMenu.setLabel("Game");
		gameMenu.setEnabled(true);

		Menu highScoresMenu =new Menu();
		highScoresMenu.setName("High Scores");
		highScoresMenu.setLabel("High Scores");
		highScoresMenu.setEnabled(true);


		miStart= new MenuItem();
		miStart.setLabel("Start");
		gameMenu.add(miStart);
		miStart.addActionListener(this);

		miInstructions= new MenuItem();
		miInstructions.setLabel("Instructions");
		gameMenu.add(miInstructions);
		miInstructions.addActionListener(this);

		miExit= new MenuItem();
		miExit.setLabel("Exit");
		gameMenu.add(miExit);
		miExit.addActionListener(this);
		
		miHighScores= new MenuItem();
		miHighScores.setLabel("High Scores");
		highScoresMenu.add(miHighScores);
		miHighScores.addActionListener(this);
		
		mb.add(gameMenu);
		mb.add(highScoresMenu);
		this.setMenuBar(mb);
	}
	
	//init game by launching this window.
	public static void main(String[] args)
	{
		new NumbersMainWindow();
		
	}
	
	public void update(Graphics g)
	{
		//Needs to be overridden to avoid flashing
		if(gameGrid!=null)
			gameGrid.paint(g);
	}
	public void paint(Graphics g)
	{
		if(gameGrid!=null)
			gameGrid.paint(g);
	}
	
	public void windowOpened(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowClosing(WindowEvent e){System.exit(0); }
	public void windowActivated(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
	
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseClicked(MouseEvent e)
	{
		if(gameGrid!=null)
		{
			if(e.getButton()==e.BUTTON1)
				gameGrid.onClick(e.getX(), e.getY());
			else//if it's not a left click, assume right click
				gameGrid.eliminate();
			
			if(finishLevel()!=-1)
				finishButton.setVisible(true);
			
			repaint();
			
			if(gameTickerThread==null)
			{
				gameTickerThread = new GameTicker(this,gameGrid);
				gameTickerThread.start();
			}
		}
		
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==miExit)
			System.exit(0);
		
		if(e.getSource()==miStart)
		{
			if(gameGrid!=null)
				gameGrid.gameOver= true;
			gameGrid = new Grid();
			gameGrid.init(4,10,10);
			gameGrid.setSize(60, 120, 160, 400);
			gameTickerThread = new GameTicker(this,gameGrid);
			gameTickerThread.start();
			//this.repaint();
			
			finishButton.setLocation(80, 540);
			finishButton.setVisible(false);
			finishButton.setSize(120,30);
			
			scoreLabel.setLocation(120,50);
			scoreLabel.setSize(180,15);
			scoreLabel.setVisible(false);
			scoreLabel.setFont( new Font("SansSerif", Font.BOLD, 12));
			
			levelLabel.setLocation(120,70);
			levelLabel.setSize(260,15);
			levelLabel.setVisible(true);
			levelLabel.setFont( new Font("SansSerif", Font.BOLD, 12));
			
			minBlockSizeLabel.setLocation(40,90);
			minBlockSizeLabel.setSize(260,15);
			minBlockSizeLabel.setVisible(true);
			minBlockSizeLabel.setFont( new Font("SansSerif", Font.BOLD, 12));
				
			score = 0;
			level = 1;
			
			scoreLabel.setText("Score:0");
			levelLabel.setText("Level:1");
			minBlockSizeLabel.setText("Minimum Block Size To Advance:"+getMinBlockSize(level));
			this.repaint();

		}

		if(e.getSource()==miInstructions)
		{
			//TO DO
		}

		if(e.getSource()==miHighScores)
		{
			//TO DO
		}

		if(e.getSource()==finishButton)
		{
			int newScore =  finishLevel();
			if(newScore!=-1)
			{
				score +=newScore;
				level = level+1;
				
				//re-init game:
				gameGrid.gameOver= true;
				gameGrid = new Grid();
				gameGrid.init(4,10,10);
				gameGrid.setSize(60, 120, 160, 400);
				gameTickerThread = new GameTicker(this,gameGrid);
				gameTickerThread.start();
				
				finishButton.setVisible(false);
			}
				
			scoreLabel.setText("Score:"+score);
			levelLabel.setText("Level:"+level);
			minBlockSizeLabel.setText("Minimum Block Size To Advance:"+getMinBlockSize(level));

			repaint();
			
		}
		
		
		/*MenuItem miStart;;
		MenuItem miInstructions;
		MenuItem miExit;
		MenuItem miHighScores;*/
	}
	
	private int finishLevel()
	{
		if(gameGrid == null)
			return -1;
		return gameGrid.finishLevel(getMinBlockSize(level));
	}
	
	private static int getMinBlockSize(int level)
	{
		switch(level)
		{
		case 1:return 30;
		case 2:return 60;
		case 3:return 80;
		case 4:return 100;
		case 5:return 120;
		case 6:return 150;
		case 7:return 160;
		case 8:return 170;
		case 9:return 180;
		case 10:return 190;
		default:return -1;
		}
		
	}
	
	public void keyPressed(KeyEvent e){}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
}
