
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class lifegame extends JFrame
{
	private final World world;
	
	public lifegame(int rows, int cols)
	{
		world = new World(rows, cols);
		new Thread(world).start();
		add(world);
	}
	
    public static void main(String[] args) 
    {
    	lifegame frame = new lifegame(40, 50);
    	
        JMenuBar menu = new JMenuBar();
        frame.setJMenuBar(menu);
        
        
        JMenu game = new JMenu("ÓÎÏ·");
        menu.add(game);
        JMenuItem start = game.add("¿ªÊ¼");
        start.addActionListener(frame.new StartActionListener());

        
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1007, 859);
        frame.setTitle("LifeGame");
        frame.setVisible(true);
        frame.setResizable(false);
    }	
    
    
    class StartActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		world.setCells();
    	}
    }
    
    
}
