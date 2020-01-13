import java.awt.Frame;


public class GameTicker extends Thread{

	Frame frame;
	Grid gameGrid;
	
	public GameTicker(Frame _frame,Grid _gameGrid)
	{
		frame =_frame;
		gameGrid = _gameGrid;
	}
	
	public void run()
	{
		while(true)
		{
			try
			{
				//System.out.println("thread");
				this.sleep(100);
				boolean changed=gameGrid.increment();
				if(changed)
					frame.repaint();
				
				if(gameGrid.gameOver)
					return;
			}
			catch(Exception e)
			{
				e.printStackTrace();	
			}
			
		}
	}
}
	
