import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class MazeGen 
{
	int rows;
	int cols;
	Cell[][] maze;
	MazePanel pane;
	boolean solution = false;
	TreeNode root;
	ArrayList<Integer> fin;
	JButton solveButt;
	JButton genButt;
	JTextField tWidth, tHeight, tSleep;
	int spot = 1;
	int sleepTime;
	
	public MazeGen(int numRows, int numCols, int sleep) throws InterruptedException
	{
		rows = numRows;
		cols = numCols;
		sleepTime = sleep;
		maze = new Cell[numRows][numCols];
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < cols; j++)
			{
				maze[i][j] = new Cell(i, j);
			}
		}
		maze[0][0].setVisited(true);
		pane = new MazePanel();
	}
	Runnable gen = new Runnable(){

		@Override
		public void run() 
		{
			try 
			{
				solution = false;
				fin = new ArrayList<Integer>();
				//fin = new ArrayList<Integer>();
				maze = new Cell[rows][cols];
				for(int i = 0; i < rows; i++)
				{
					for(int j = 0; j < cols; j++)
					{
						maze[i][j] = new Cell(i, j);
					}
				}
				maze[0][0].setVisited(true);
				root = new TreeNode(new Integer(1)); //reinitializing needed resources for rerun.
				generate(0,0, root);
				solution = true;
				genButt.setEnabled(true);
				solveButt.setEnabled(true);
				//solve(root);
				//pane.repaint();
				
				//solution = true;
				/*do
				{
					pane.repaint();
					spot++;
					Thread.sleep(sleepTime);
				}while(spot < fin.size());*/
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			catch(StackOverflowError t)
			{
				pane.stackOverflow();
			}
		}
		
	};
	public void setRows(int r)
	{
		rows = r;
	}
	public void setCols(int c)
	{
		cols = c;
	}
	public void setSleepTime(int s)
	{
		sleepTime = s;
	}
	
	Runnable runSolve = new Runnable(){

		@Override
		public void run() 
		{
			try 
			{
				solve(root);
				pane.repaint();
				genButt.setEnabled(true);
				solveButt.setEnabled(true);
				
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		
	};
	public boolean solve(TreeNode curNode) throws InterruptedException
	{
		List<TreeNode> children = curNode.getChildren();
		ArrayList list = new ArrayList<Integer>();
		for(TreeNode n : children)
			list.add((Integer)n.getReference());
		fin.add((Integer)curNode.getReference());
		pane.repaint();
		Thread.sleep(sleepTime);
		
		if(list.contains(new Integer(cols*rows)))
		{
			fin.add(cols*rows);
			//fin.add((Integer)curNode.getReference());
			return true;
		}
		else
		{
			for(TreeNode n : children)
			{
				if(solve(n))
				{
					//fin.add((Integer)curNode.getReference());
					return true;
				}
			}
			fin.remove((Integer)curNode.getReference());
			pane.repaint();
			Thread.sleep(sleepTime);
			return false;
		}
		
	}
	public void generate(int row, int col, TreeNode curNode) throws InterruptedException, StackOverflowError
	{
		int num;
		char dir;
		String sub = "nsew";
		TreeNode n;
		for(int i = 3; i >= 0; i--)
		{
			num = (int)Math.floor(Math.random()* new Double(i+1));
			dir = sub.charAt(num);
			switch(dir)
			{
				case 'w':
					if(col > 0 && !maze[row][col-1].isVisited())
					{
						maze[row][col].setWestWall(false);
						maze[row][col-1].setEastWall(false);
						maze[row][col-1].setVisited(true);
						pane.repaint();
						Thread.sleep(sleepTime);
						//System.out.println("From:: "  + row + ", " + col + " to " + row + ", " + (col-1));
						curNode.addChildNode(n = new TreeNode(new Integer((Integer)curNode.getReference() - 1)));
						generate(row, col-1, n);
						
					}
					break;
				case 'n':
					if(row > 0 && !maze[row-1][col].isVisited())
					{
						maze[row][col].setNorthWall(false);
						maze[row-1][col].setSouthWall(false);
						maze[row-1][col].setVisited(true);
						pane.repaint();
						Thread.sleep(sleepTime);
						//System.out.println("From:: "  + row + ", " + col + " to " + (row-1) + ", " + col);
						curNode.addChildNode(n = new TreeNode(new Integer((Integer)curNode.getReference() - cols)));
						generate(row-1, col, n);
						
					}
					break;
				case 'e':
					if(col < cols-1 && !maze[row][col+1].isVisited())
					{
						maze[row][col].setEastWall(false);
						maze[row][col+1].setWestWall(false);
						maze[row][col+1].setVisited(true);
						pane.repaint();
						Thread.sleep(sleepTime);
						//System.out.println("From:: "  + row + ", " + col + " to " + row + ", " + (col+1));
						curNode.addChildNode(n = new TreeNode(new Integer((Integer)curNode.getReference() + 1)));
						generate(row, col+1, n);
						
					}
					break;
				case 's':
					if(row < rows-1 && !maze[row+1][col].isVisited())
					{
						maze[row][col].setSouthWall(false);
						maze[row+1][col].setNorthWall(false);
						maze[row+1][col].setVisited(true);
						pane.repaint();
						Thread.sleep(sleepTime);
						//System.out.println("From:: "  + row + ", " + col + " to " + (row+1) + ", " + col);
						curNode.addChildNode(n = new TreeNode(new Integer((Integer)curNode.getReference() + cols)));
						generate(row+1, col, n);
						
					}
				break;
			}
			sub = sub.substring(0, num) + sub.substring(num+1);
		}
		return;
	}
	class Cell
	{
		private boolean northWall, southWall, eastWall, westWall;
		private boolean visited = false;
		private int row, col;
		
		public Cell(int rowNum, int colNum)
		{
			row = rowNum;
			col = colNum;
			northWall = true;
			southWall = true;
			eastWall = true;
			westWall = true;
		}
		public int getRow(){return row;}
		public int getCol(){return col;}
		public boolean hasNorthWall(){return northWall;}
		public boolean hasSouthWall(){return southWall;}
		public boolean hasEastWall(){return eastWall;}
		public boolean hasWestWall(){return westWall;}
		public void setNorthWall(boolean val){northWall = val;}
		public void setSouthWall(boolean val){southWall = val;}
		public void setEastWall(boolean val){eastWall = val;}
		public void setWestWall(boolean val){westWall = val;}
		public boolean isVisited(){return visited;}
		public void setVisited(boolean val){visited = val;}
	}
	class MazePanel extends JPanel
	{
		int height = 800;
		int width = 1850;
		public MazePanel()
		{
			super(new BorderLayout());
			this.setBackground(Color.WHITE);
			JPanel buttonLayout = new JPanel(new GridLayout(1, 2));
			JPanel inputPanel = new JPanel(new GridLayout(3,2));
			JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 20, 20));
			tWidth = new JTextField(5);
			tWidth.setText("" + 100);
			tHeight = new JTextField(5);
			tHeight.setText("" + 100);
			tSleep = new JTextField(5);
			tSleep.setText("" + 2);
			
			inputPanel.add(new JLabel("Cell Width (More than 100 might overflow)"));
			inputPanel.add(tWidth);
			inputPanel.add(new JLabel("Cell Height (More than 100 might overflow)"));
			inputPanel.add(tHeight);
			inputPanel.add(new JLabel("Sleep Time (in milliseconds)"));
			inputPanel.add(tSleep);
			solveButt = new JButton("Solve");
			genButt = new JButton("Generate");
			
			solveButt.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					try
					{
						if(solution)
						{
	
							setSleepTime(Integer.parseInt(tSleep.getText()));
							Thread solveThread = new Thread(runSolve);
							solveThread.start();
							genButt.setEnabled(false);
							solveButt.setEnabled(false);
						}
						else
							JOptionPane.showMessageDialog(MazePanel.this, "Maze is not done generating");
					}
					catch(NumberFormatException e)
					{
						JOptionPane.showMessageDialog(MazePanel.this, "Enter valid integers in the input boxes");
					}
				}
				
			});
			genButt.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					try
					{
						setRows(Integer.parseInt(tHeight.getText()));
						setCols(Integer.parseInt(tWidth.getText()));
						setSleepTime(Integer.parseInt(tSleep.getText()));
						Thread genThread = new Thread(gen);
						genThread.start();
						genButt.setEnabled(false);
						solveButt.setEnabled(false);
					}
					catch(NumberFormatException e)
					{
						JOptionPane.showMessageDialog(MazePanel.this, "Enter valid integers in the input boxes");
					}
				}
				
			});
			
			buttonLayout.add(inputPanel);
			
			buttonPanel.add(solveButt);
			buttonPanel.add(genButt);
			buttonPanel.setBorder(new EmptyBorder(20,20,20,20));
			buttonLayout.add(buttonPanel);
			
			buttonLayout.setBorder(new EmptyBorder(20,20,20,20));
			
			this.add(buttonLayout, BorderLayout.SOUTH);
		}
		public void stackOverflow()
		{

			JOptionPane.showMessageDialog(MazePanel.this, "Stack Overflow\nRecursion is too deep\nReduce rows/columns");
		}
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			int cellHeight = height/rows;
			int cellWidth = width/cols;
			g.setColor(Color.BLACK);
			g.drawLine(0, 0, width, 0);
			g.drawLine(0, 0, 0, height);
			
			for(int i = 0; i < rows; i++)
			{
				for(int j = 0; j < cols; j++)
				{
					if(maze[i][j].hasSouthWall())
					{
						g.drawLine((width/cols) * j, (height/rows) * (i+1), (width/cols) *(j+1), height/rows * (i+1));
					}
					if(maze[i][j].hasEastWall())
					{
						g.drawLine(width/cols *(j+1), height/rows * i, width/cols * (j+1), height/rows * (i+1));
					}
				}
			}
			if(solution && fin.size() > 1)
			{
				Graphics2D g2 = (Graphics2D)g;
				g2.setColor(Color.RED);
				g2.setStroke(new BasicStroke(3));
				Integer numAt, numTo;
				for(int i = 0; i < fin.size()-1; i++)
				{
					numAt = fin.get(i) - 1;
					numTo= fin.get(i+1) - 1;
					g.drawLine((numAt%cols)*cellWidth + cellWidth/2, (numAt/cols)*cellHeight + cellHeight/2, (numTo%cols)*cellWidth + cellWidth/2, (numTo/cols)*cellHeight + cellHeight/2);
				}
			}
		}
	}
}
