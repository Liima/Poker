import javax.swing.*;

public class PokerMain
{
	public static void main(String[] args)
	{
		JFrame poker = new JFrame("Poker");
		poker.setSize(800, 700);
		poker.add(new GUI());
		poker.setFocusable(true);
		poker.setResizable(true);
		poker.setVisible(true);
	}
}