import java.awt.Color;
import java.awt.Graphics;


public class Grid 
{
	int gridWidth,gridHeight, maximumNumber; 
	NumberBlock[][] blocks;
	int x;
	int y;
	int width;
	int height;
	int blockWidth;
	int blockHeight;
	public static int MIN_Y;
	public static double HIGH_NUMBER_OCCURENCE_RATE = 0.06;  
	public boolean gameOver = false;//signals to the thread that it can stop.
	
	
	public void paint(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
		
		for(int i =0;i<gridWidth;i++)
		{
			for(int j =0;j<gridHeight;j++)
			{
				if(blocks[i][j]!=null)
					blocks[i][j].paint(g);//,x+i*blockWidth,y+j*blockHeight,blockWidth,blockHeight);
				//g.drawString(""+blocks[i][j].number, x+i*25, y+j*25);
			}
		}

		
	}
	
	/*
	 * If no blocks are selected, select
	 * else if new block is adjacent,select
	 * else deselect all other blocks, select current
	 * 
	 */
	public void onClick(int _x,int _y)
	{
		if(!blocksSelected())
			selectedBlock(_x,_y);
		else if(blockIsAdjacentSelectedBlock(_x,_y))
			selectedBlock(_x,_y);
		else
		{
			deselectAllBlocks();
			selectedBlock(_x,_y);
		}
	}
	
	private boolean blocksSelected()
	{
		for(int i =0;i<gridWidth;i++)
		{
			for(int j =0;j<gridHeight;j++)
			{
				if(blocks[i][j]!=null&&blocks[i][j].isSelected)
					return true;
			}
		}
		
		return false;
	}
	
	private boolean blockIsAdjacentSelectedBlock(int _x,int _y)
	{
		for(int i =0;i<gridWidth;i++)
		{
			for(int j =0;j<gridHeight;j++)
			{
				if(blocks[i][j]!=null&&blocks[i][j].isIn(_x,_y))
				{
					//check left
					if(i>0 && blocks[i-1][j]!=null && blocks[i-1][j].isSelected)
						return true;
					//check right
					if(i<gridWidth-1 && blocks[i+1][j]!=null &&blocks[i+1][j].isSelected)
						return true;
					//check up
					if(j>0 && blocks[i][j-1]!=null &&blocks[i][j-1].isSelected)
						return true;
					//check down
					if(j<gridHeight-1 && blocks[i][j+1]!=null &&blocks[i][j+1].isSelected)
						return true;
					
					//if(blocks[i][j].isSelected)
					//	return true;
					
					return false;

				}
			}
		}
		
		return false;
	}
	
	private void deselectAllBlocks()
	{
		for(int i =0;i<gridWidth;i++)
		{
			for(int j =0;j<gridHeight;j++)
			{
				if(blocks[i][j]!=null)
					blocks[i][j].isSelected=false;
			}
		}
	
	}
	
	private void selectedBlock(int _x,int _y)
	{
		for(int i =0;i<gridWidth;i++)
		{
			for(int j =0;j<gridHeight;j++)
			{
				if(blocks[i][j].isIn(_x,_y))
				{
					//toggle.if turning off,turn off all
					if(!blocks[i][j].isSelected)
						blocks[i][j].isSelected=true;
					else
					{
						deselectAllBlocks();
					}
					return;
				}
			}
		}
	
	}

	//for game animation
	//drop new blocks down to where they need to be
	public boolean increment()
	{
		boolean moved = false;
		
		for(int i =0;i<gridWidth;i++)
		{
			for(int j =0;j<gridHeight;j++)
			{
				//if block is above final height, increment and accelerate.
				if(blocks[i][j]!=null)
				{
					if(blocks[i][j].y<y+j*blockHeight)
					{
						blocks[i][j].acceleration++;
						
						blocks[i][j].y=blocks[i][j].y+blocks[i][j].acceleration;
						if(blocks[i][j].y>y+j*blockHeight)
							blocks[i][j].y=y+j*blockHeight;
					
						moved = true;
					}
					else 
						blocks[i][j].acceleration=0;
				}
			}
		}
		
		return moved;
	}
	
	public void setSize(int _x,int _y,int _w, int _h)
	{
		x=_x;
		y=_y;
		width=_w;
		height=_h;
		blockWidth = width/gridWidth;
		blockHeight = height/gridHeight;
		resetBlockPosition();
		MIN_Y = y;
	}
		
	public void resetBlockPosition()
	{
		for(int i =0;i<gridWidth;i++)
		{
			for(int j =0;j<gridHeight;j++)
			{
				//System.out.print("a");
				//System.out.print(blocks[i][j]);
				//System.out.print("b");
				blocks[i][j].setPos(x+i*blockWidth,y+j*blockHeight,blockWidth,blockHeight);
			}
		}
	}
	
	/*
	 * determine if matching set, then eliminate if necesary
	 * 
	 */
	public void eliminate()
	{
		//System.out.print("hey");
		int total=0;
		boolean isMatch =false;
		int allTheSame=0;
		int numberSelected=0;
		
		for(int i =0;i<gridWidth;i++)
		{
			for(int j =0;j<gridHeight;j++)
			{
				if(blocks[i][j].isSelected)
				{
					numberSelected=numberSelected+1;
					
					total = total+blocks[i][j].number;
					if(allTheSame==0)
						allTheSame =blocks[i][j].number;
					else if(allTheSame!=blocks[i][j].number)
						allTheSame = -1;
				}
			}
		}
		
		//System.out.print("total:"+total);
		
		//if the 1 of the numbers is the sum of the others, eliminate!
		for(int i =0;i<gridWidth;i++)
		{
			for(int j =0;j<gridHeight;j++)
			{
				if(blocks[i][j].isSelected)
				{
					if(total==blocks[i][j].number*2&&numberSelected>2)
					{
						blocks[i][j].number= blocks[i][j].number*2;//set new number
						blocks[i][j].isSelected = false;//deselect
						blocks[i][j].blockColor = Color.ORANGE;
						blocks[i][j].isFinalBlock = true;
						isMatch = true;
					}
				}
			}
		}

		//alternately, if all numbers are the same and number of selected>=3, eliminate
		//if(numberSelected>2&&allTheSame>0)
		//	isMatch = true;



		
		//eliminate squares
		if(isMatch)
		{
			for(int i =0;i<gridWidth;i++)
			{
				for(int j =0;j<gridHeight;j++)
				{
					if(blocks[i][j].isSelected)
						blocks[i][j]=null;
				}
			}
			
			shiftBlocks();
		}

	}

	public void shiftBlocks()
	{
		//if block is null, move from top down, add new one on top
		boolean nullFound = true;
		
		while(nullFound)
		{
			nullFound = false;
			
			for(int i =0;i<gridWidth;i++)
			{
				for(int j =gridHeight-1;j>=0;j--)
				{
					if(blocks[i][j]==null)
					{	
						if(j>0&&blocks[i][j-1]!=null)
							nullFound = true;//only do this if there is something to have fall;
						
						if(j>0)
						{
							blocks[i][j]=blocks[i][j-1];
							blocks[i][j-1]= null;
						}
						else
						{
							//remove for now.
							//System.out.println("new block");
							blocks[i][j]=new NumberBlock(maximumNumber);
							blocks[i][j].setPos(x+i*blockWidth,y+(j-1)*blockHeight,blockWidth,blockHeight);
							blocks[i][j].number=-1;//nonselectable block
						}
					}
				}
			}
		}
		//resetBlockPosition();
	}
	
	public void init(int _width,int _height,int _maximumNumber)
	{
		gridWidth = _width;
		gridHeight = _height;
		maximumNumber = _maximumNumber;
		
		blocks = new NumberBlock[gridWidth][];
		for(int i =0;i<gridWidth;i++)
		{
			blocks[i] = new NumberBlock[gridHeight];

			for(int j =0;j<gridHeight;j++)
			{
				blocks[i][j]= new NumberBlock(maximumNumber);
			}
		}
	}
	
	public int finishLevel(int minScore)
	{
		boolean completed =false;
		int score= 0;
		//The max number must be > 0
		for(int i =0;i<gridWidth;i++)
		{
			for(int j =0;j<gridHeight;j++)
			{
				if(blocks[i][j].isFinalBlock)
				{
					if(blocks[i][j].number>=minScore)
						completed = true;
					score +=blocks[i][j].number;
				}
				//else
				//	score -=blocks[i][j].number;
			}
		}
		
		if(!completed)
			return -1;
		else
			return score;
	}
	

}
