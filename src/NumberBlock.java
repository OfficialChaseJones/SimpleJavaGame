import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


public class NumberBlock 
{
	public int number;
	public boolean isSelected;
	public boolean isFinalBlock;
	public Color blockColor;
	int x;
	int y;
	int width;
	int height;
	int acceleration=0;
	
	
	NumberBlock(int maximumNumber)
	{
		if(Grid.HIGH_NUMBER_OCCURENCE_RATE>Math.random())
			number=(int)(Math.random()*maximumNumber)+1+maximumNumber;
		else
			number=(int)(Math.random()*maximumNumber)+1;
		isSelected = false;
		blockColor = Color.LIGHT_GRAY;
		isFinalBlock = false;
	}
	public void setPos(int _x,int _y,int _width,int _height)
	{
		x= _x;
		y=_y;
		width=_width;
		height=_height;
		
	}
	
	public void paint(Graphics g)//,int x,int y,int width,int height)
	{
		if(y<Grid.MIN_Y)
		{
			int calcHeight = Grid.MIN_Y-y;
			g.setColor(Color.BLACK);
			g.drawRect(x, Grid.MIN_Y, width-1, height-1-calcHeight);
			if(isSelected)
				g.setColor(Color.yellow);
			else
				g.setColor(blockColor);
			
			if(number==-1)
				g.setColor(Color.WHITE);//no square
			
			g.fillRect(x+1, Grid.MIN_Y+1, width-2, height-2-calcHeight);
			
			
		}
		else
		{
			g.setColor(Color.BLACK);
			g.drawRect(x, y, width-1, height-1);
			if(isSelected)
				g.setColor(Color.yellow);
			else
				g.setColor(blockColor);
			
			if(number==-1)
				g.setColor(Color.WHITE);//no square
			
			g.fillRect(x+1, y+1, width-2, height-2);
			
			if(number!=-1)
			{
				g.setColor(Color.BLACK);
				Font big;
				big= new Font("SansSerif", Font.BOLD, (int)(width*0.8));
				if(number>100)
					big= new Font("SansSerif", Font.BOLD, (int)(width*0.8)/2);
				g.setFont(big);
				if(number<10)
					g.drawString(""+number, (int)(x+0.3*width), (int)(y+height*0.8));
				else
					g.drawString(""+number, x+2, (int)(y+height*0.8));
			}
		}
		Math.max(Grid.MIN_Y,y);
	
	}
	
	
	
	public boolean isIn(int _x,int _y)
	{
		if(number==-1)
			return false;
		
		if(_x>x && _x<x+width
			&&_y>y && _y<y+height)
			return true;
		
		return false;
		
	}
	
}
