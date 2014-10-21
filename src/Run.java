import javax.swing.JFrame;
import javax.swing.JPanel;


public class Run
{
	
	public static void main(String[] args) throws InterruptedException
	{
		JPanel pane;
		MazeGen maze = new MazeGen(100, 100, 2);
		JFrame frame = new JFrame("maze");
		frame.setContentPane(pane = maze.pane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1050);
        frame.setVisible(true);
        /*Thread genThread = new Thread(maze.gen);
        genThread.start();*/
        
        
	}
}
