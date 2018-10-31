

import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;


public class World extends JPanel implements Runnable
{
	private final int rows;
	private final int cols;
	private int[][] cells;
	private int[][] newcells;
	
	private int[] direct = {-1, 0, 1};

	private volatile boolean isChanging = false;
	
	public World(int rows, int cols)
	{
		this.rows = rows;
		this.cols = cols;
		this.cells = new int[rows][cols];
		emptyInit(cells);
	}
	
	//��ʼ��ϸ������
	public void emptyInit(int[][] cells) {
		for(int i = 0;i < rows;i++) {
			for(int j = 0;j < cols;j++) {
				cells[i][j] = CellState.DEAD.getValue();
			}
		}
	}
	
	//����ϸ�����У�����������
	public void randomInit(int[][] cells) {
		Random r = new Random();
		int value;
		for(int i = 0;i < rows;i++) {
			for(int j = 0;j < cols;j++) {
				value = r.nextInt(2);
				cells[i][j] = value;
			}
		}	
	}
	
	//�������У����������ھӴ����
	public int countLiveNeighbor(int [][]cells, int x , int y) {
		int count = 0;
		if(x < 0 || x >= rows || y < 0 || y >= cols)
			return -1;
		for(int i = 0;i < 3;i++) {
			for(int j = 0;j < 3;j++) {
				if((x + direct[i]) < 0 || (x + direct[i]) == rows 
						|| (y + direct[j]) < 0 || (y + direct[j]) == cols)
					continue;
				if(cells[x + direct[i]][y + direct[j]] == CellState.LIVE.getValue())
					count++;
			}
		}
		if(cells[x][y] == CellState.LIVE.getValue())
			count--;
		return count;
	}
	
	
	/*���һ��ϸ����Χ�� 3 ��ϸ��Ϊ�������ϸ��Ϊ��������ϸ����ԭ��Ϊ����תΪ����
	��ԭ��Ϊ���򱣳ֲ��䣻
	���һ��ϸ����Χ�� 2 ��ϸ��Ϊ�������ϸ��������״̬���ֲ��䣻
	����������£���ϸ��Ϊ��������ϸ����ԭ��Ϊ����תΪ������ԭ��Ϊ���򱣳ֲ��䡣*/
	//���ݸ������󣬲�����һ��ϸ������
	public int[][] generate(int[][] cells) {
		if(cells == null)
			return null;
		int[][] nextcells = new int[rows][cols];
		for(int i = 0;i < rows;i++) {
			for(int j = 0;j < cols;j++) {
				if(countLiveNeighbor(cells , i , j) == 2)
					nextcells[i][j] = cells[i][j];
				else if(countLiveNeighbor(cells , i , j) == 3)
					nextcells[i][j] = CellState.LIVE.getValue();
				else 
					nextcells[i][j] = CellState.DEAD.getValue();
				
			}
		}
		return nextcells;
	}
	
	@Override
	public void run(){
		while(true)
		{
			synchronized(this)
			{
				while(isChanging)
				{
					try 
					{
						this.wait();
					} catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
				}
				
				repaint();
				sleep(1);
				
				newcells = generate(cells);
				cells = newcells;
				
			}
		}
	}
	
	private void sleep(int i) {
		try
		{
			Thread.sleep(1000*i);
		}catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
	}

	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (int i = 0; i < rows; i++) 
        {
            for (int j = 0; j < cols; j++) 
            {
            	if(cells[i][j] == CellState.LIVE.getValue())
            	{
            		g.fillRect(j * 20, i * 20, 20, 20);
            	}
            	else
            	{
                    g.drawRect(j * 20, i * 20, 20, 20);            		
            	}
            }
        }
    }	
	
	
	static enum CellState {
		DEAD(0),LIVE(1);
		
		private int value;
		
		CellState(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
	}
	
	public void setCells() {
		isChanging = true;
		
		synchronized(this) {
			randomInit(cells);
			isChanging = false;
			this.notifyAll();
		}
		
	}
	
}	