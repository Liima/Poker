import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Arrays;


public class Poker implements ActionListener
	{
	public GUI gameGui;
	public int money = 500;
	public Hand[] players = new Hand[4];  //players[0] is the user
	public Deck deck;
	public Hand lastRaised;
	public int pool;
	public int highBet;
	boolean playerFolded;
	boolean firstBet;

	public Poker()
		{

		}

	public void addGUI(GUI g)
		{
		gameGui = g;
		}

	public void play()
		{
		gameGui.clearText();
		setMessage("Let's play Poker!\n");
		//new Deck
		deck = new Deck();
		deck.shuffle();
		//new Hands
		players[0]= new Hand(0, this);
		players[1]= new Hand(1, this);
		players[2]= new Hand(2, this);
		players[3]= new Hand(3, this);
		JPanel[] handPanels = gameGui.getHands();
		players[0].add(handPanels[0], true);
		players[1].add(handPanels[1], false);
		players[2].add(handPanels[2], true);
		players[3].add(handPanels[3], false);
		players[0].setDeck(deck);
		players[1].setDeck(deck);
		players[2].setDeck(deck);
		players[3].setDeck(deck);
		//reset bets
		playerFolded=false;
		pool = 0;
		highBet=5;
		lastRaised = players[0];
		//deal first cards
		deal();
		deal();
		//turn on buttons
		gameGui.showButtons(highBet - players[0].getBet());
		gameGui.setMoney(money);
		gameGui.highlight(0);
		}

	public int getMoney()
		{
		return money;
		}

//finish betting round for AI

	private void bet()
		{
		new Thinking();
		}

	private class Thinking implements ActionListener
		{
		int cell=1;
		final Timer t = new Timer(1000, this);
		private Thinking()
			{
			gameGui.highlight(1);
			t.start();
			}
		private void incrCell(){
			//do
			cell++;
			//while(players[cell%4].getFold()&& cell!=4);
		}
		public void actionPerformed(ActionEvent e)
			{
			if(cell == 4)								//get to player's turn
				{
				if(players[0].getFold())					//if player folded
					{
					t.stop();									//next round of betting
					bet();
					}
				else if(lastRaised == players[0])			//if player was last to raise
					{
					if(players[0].getSize() == 5)			//and player has 5 cards in hand
						{
						t.stop();									//end game
						reveal();
						}
					else										//and player has less than 5 cards in hand
						{
						t.stop();									//deal a card
						deal();
						}
					}
				else										//if player is still in, not last to raise
					{
					t.stop();									//show buttons and wait for input
					if(money <= 0 )
						{
						gameGui.getButtons()[3].setEnabled(true);
						}
					else
						{
							int k = highBet >= players[0].getBet()+money ? -1 : highBet;
							gameGui.showButtons(k);
						}
					}
				}
			else										//get to ai's turn
				{
				if(players[cell].getFold())					//if ai folded
					{
					incrCell();										//passes to next player
					gameGui.highlight(cell%4);
					}
				else if(lastRaised == players[cell])		//if ai was last to raise
					{
					if(players[cell].getSize() == 5)			//and ai has 5 cards in hand
						{
						t.stop();									// end the game
						reveal();
						}
					else										//and ai has less than 5 cards in hand
						{
						t.stop();									// deal a card
						deal();
						}
					}
				else										//if ai is still in,  not last to raise
					{
					players[cell].bet();						//ai bets, passes to next player
					incrCell();
					gameGui.highlight(cell%4);
					}
				}
			}
		}

//Deal

	private void deal()
		{
		for(int i =0; i<4;i++)
			{
			if(!players[i].getFold())
				{
				if(players[i].getSize()==5)
					{
					reveal();
					return;
					}
				players[i].resetBet();
				firstBet = true;
				highBet = 5;
				lastRaised = players[0];
				players[i].drawCard(1);
				}
			}
		gameGui.highlight(0);
		if(players[0].getFold() || money <=0)
			{
			gameGui.getButtons()[3].setEnabled(true);
			setMessage("Use the continue button to finish the round.");
			}
		else if(highBet - players[0].getBet() <=0)
			{
			gameGui.showButtons(-1);
			}
			else
			{
			gameGui.showButtons(highBet);
			}
		}


//Reveal
	public void reveal()
		{
		Hand winner = players[0];
		//flip up all cards
		//determine winner
		for(int i=1;i<4;i++)
			{
			if(players[i].getScore() > winner.getScore())
				winner = players[i];
			else if(players[i].getScore() == winner.getScore() &&
					players[i].highCard() > winner.highCard())
				winner = players[i];
			players[i].flip();
			players[i].paint();
			}

		//System.out.println("Winner: "+winner.position);

		if(winner == players[0])
			{
			money += pool;
			gameGui.setMoney(money);
			winner.setScoreName();
			setMessage("The player wins with a "+winner.getScoreName()+"!");
			}
		else
			{
			winner.setScoreName();
			setMessage("AI #" + winner.position + " wins with a "+winner.getScoreName()+"!");
			}

		for(int i=0;i<4;i++)
			{
			players[i].resetBet();
			}
		pool=0;
		gameGui.setPot(pool);
		if(money <= 0)
			setMessage("GAME OVER");
		else
			gameGui.showPlayAgain();
		}

	public void setMessage(String s)
		{
		gameGui.setMessage(s);
		}


	public void actionPerformed(ActionEvent e)
		{
		JComponent[] buttons= gameGui.getButtons();
		if(Arrays.asList(buttons).contains(e.getSource()))
			{
			if(!(e.getSource().equals((JComboBox)buttons[2]) && ((JComboBox)buttons[2]).getSelectedIndex() == 0))  //WHAT DOES THIS DO???
				{
				gameGui.hideButtons();
				}
			}

//Player Folds

		if(e.getSource().equals(buttons[0]))
			{
			players[0].fold();
			buttons[3].setEnabled(true);								//show continue button
			setMessage("Use the continue button to finish the round.");
			}

//Player Calls

		else if(e.getSource().equals(buttons[1]))
			{
			if(highBet >= players[0].getBet()+money)
				{
				money -=money;
				players[0].call(false);
				}
			else
				{
				money -= highBet - players[0].getBet();
				players[0].call();
				}

			gameGui.setMoney(money);
			bet();
			}

//Player Raises

		else if(e.getSource().equals(buttons[2]))
			{
			//get bet amount from drop down
			JComboBox temp = (JComboBox)buttons[2];
			if(temp.getSelectedIndex() > 0)
				{
				int t = 0;
				t = Integer.parseInt(temp.getSelectedItem().toString());
				highBet += t;
				money -= highBet-players[0].getBet();
				players[0].call();
				gameGui.setMoney(money);
				lastRaised = players[0];
				bet();
				}
			}

// Continue

		else if(e.getSource().equals(buttons[3]))
			{
			bet();
			buttons[3].setEnabled(false);
			}

//Player chooses "Play Again"
		else if(e.getSource().equals(buttons[4]))
			{
			play();
			buttons[3].setEnabled(false);//disable continue button
			}
		}
	}