import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI extends JPanel
{
	private JButton playAgain;
	private JButton call;
	private JButton fold;
	private JButton cont;
	private JTextArea dialog;
	private JScrollPane scrollPane;
	private JComboBox betInput;
	private JPanel textTray;
	private JPanel handPanel;
	private JPanel ai1Hand;
	private JPanel ai2Hand;
	private JPanel ai3Hand;
	private JPanel playerHand;
	private JPanel v1Panel;
	private JPanel v2Panel;
	private JPanel playPanel;
	private JPanel centerGrid;
	private JPanel potPanel;
	private JPanel buttonPanel;
	private JPanel rightTray;
	private JLabel[] bets = new JLabel[4];
	private JLabel potLabel;
	private JPanel[] emptyPanel = new JPanel[4];

	private JPanel cashPanel = new JPanel();
	private JLabel cashLabel = new JLabel();

	private Poker poker;

	public GUI()
	{
		for(int i = 0; i < 4; i++)
		{
			emptyPanel[i] = new JPanel();
			emptyPanel[i].setPreferredSize(new Dimension(200, 0));
			emptyPanel[i].setBackground(new Color(20, 154, 60));
		}

		//set Look and Feel
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{}

		BorderLayout gameLayout = new BorderLayout();
		this.setLayout(gameLayout);
		this.setFocusable(true);
		this.setBackground(new Color(20, 154, 60));

		handPanel = new JPanel();
		handPanel.setLayout(new BorderLayout());
		handPanel.setBackground(new Color(20, 154, 60));

		GridLayout hHandLayout = new GridLayout(5, 1);
		GridLayout vHandLayout = new GridLayout(1, 5);


		ai1Hand = new JPanel();
		ai1Hand.setLayout(hHandLayout);
		ai1Hand.setBackground(new Color(20, 154, 60));
		handPanel.add(ai1Hand, BorderLayout.WEST);


		v1Panel = new JPanel();
		v1Panel.setLayout(new BorderLayout());
		v1Panel.setBackground(new Color(20, 154, 60));
		ai2Hand = new JPanel();
		ai2Hand.setLayout(vHandLayout);
		ai2Hand.setBackground(new Color(20, 154, 60));
		v1Panel.add(emptyPanel[0], BorderLayout.WEST);
		v1Panel.add(ai2Hand, BorderLayout.CENTER);
		v1Panel.add(emptyPanel[1], BorderLayout.EAST);
		handPanel.add(v1Panel, BorderLayout.NORTH);


		ai3Hand = new JPanel();
		ai3Hand.setLayout(hHandLayout);
		ai3Hand.setBackground(new Color(20, 154, 60));
		handPanel.add(ai3Hand, BorderLayout.EAST);


		v2Panel = new JPanel();
		v2Panel.setLayout(new BorderLayout());
		v2Panel.setBackground(new Color(20, 154, 60));
		playerHand = new JPanel();
		playerHand.setLayout(vHandLayout);
		playerHand.setBackground(new Color(20, 154, 60));
		v2Panel.add(emptyPanel[2], BorderLayout.WEST);
		v2Panel.add(playerHand, BorderLayout.CENTER);
		v2Panel.add(emptyPanel[3], BorderLayout.EAST);
		handPanel.add(v2Panel, BorderLayout.SOUTH);


		//center
		playPanel = new JPanel();
		playPanel.setBackground(new Color(20,154,60));
		playPanel.setLayout(new BorderLayout());
		for(int g = 0; g < bets.length; g++)
		{
			bets[g] = new JLabel("Bet: 0");
			bets[g].setFont(bets[g].getFont().deriveFont(18.0f));
		}

		bets[0].setHorizontalAlignment(SwingConstants.CENTER);
		playPanel.add(bets[0], BorderLayout.SOUTH);
		playPanel.add(bets[1], BorderLayout.WEST);
		bets[2].setHorizontalAlignment(SwingConstants.CENTER);
		playPanel.add(bets[2], BorderLayout.NORTH);
		playPanel.add(bets[3], BorderLayout.EAST);


		centerGrid = new JPanel();
		centerGrid.setLayout(new GridLayout(3, 1));
		centerGrid.setBackground(new Color(20, 154, 60));

		//spacer
		centerGrid.add(new JLabel());

		//actual center
		potPanel = new JPanel();
		potPanel.setLayout(new BorderLayout());
		potPanel.setBackground(new Color(20, 154, 60));

		potLabel = new JLabel("Pot: 0");
		potLabel.setFont(potLabel.getFont().deriveFont(32.0f));
		potLabel.setHorizontalAlignment(SwingConstants.CENTER);
		potPanel.add(potLabel, BorderLayout.NORTH);

		potPanel.add(new JLabel(), BorderLayout.CENTER);

		buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(20, 154, 60));
		buttonPanel.setLayout(new GridLayout(1, 3));

		//initialize buttons
		playAgain = new JButton("Play Again");
		//playAgain.addActionListener(poker);

		fold = new JButton("Fold");
		//fold.addActionListener(poker);

		call = new JButton("Call");
		//call.addActionListener(poker);

		cont = new JButton("Continue");
		cont.setEnabled(false);

		betInput = new JComboBox();
		//betInput.addActionListener(poker);


		potPanel.add(buttonPanel, BorderLayout.SOUTH);
		centerGrid.add(potPanel);

		//spacer
		centerGrid.add(new JLabel());



		playPanel.add(centerGrid, BorderLayout.CENTER);

		handPanel.add(playPanel, BorderLayout.CENTER);

		this.add(handPanel,BorderLayout.CENTER);

		BorderLayout textTrayLayout = new BorderLayout();
		textTray = new JPanel();
		textTray.setLayout(textTrayLayout);

		dialog = new JTextArea();
		dialog.setEditable(false);
		dialog.setRows(3);
		dialog.append("Let's play Poker!\n");
		scrollPane = new JScrollPane(dialog);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setWheelScrollingEnabled(true);
		this.updateUI();
		textTray.add(scrollPane, BorderLayout.CENTER);

		rightTray = new JPanel();
		rightTray.add(cashPanel);
		rightTray.add(cont);

		textTray.add(rightTray, BorderLayout.EAST);

		this.add(textTray, BorderLayout.SOUTH);


		poker = new Poker();
		playAgain.addActionListener(poker);
		fold.addActionListener(poker);
		call.addActionListener(poker);
		cont.addActionListener(poker);
		betInput.addActionListener(poker);
		poker.addGUI(this);
		poker.play();

	} //end constructor

	public JPanel[] getHands(){
		JPanel[] panels = {playerHand, ai1Hand, ai2Hand, ai3Hand};
		return panels;
	}

	public JComponent[] getButtons(){
		// 0 	fold
		// 1 	call
		// 2 	betInput
		// 3	continue
		// 4	playAgain
		JComponent[] buttons = {fold,call,betInput,cont,playAgain};
		return buttons;
	}

	public void hideButtons(){
		buttonPanel.removeAll();
	}

	public void clearText(){
		dialog.replaceRange("", 0, dialog.getText().length());
	}

	public void showButtons(int value){
		//System.out.println("Last Raised: " + poker.lastRaised.position);
		buttonPanel.removeAll();

		betInput.removeAllItems();
		betInput.addItem("Raise");
		for(int i = 5; i <= poker.getMoney() - (poker.highBet-poker.players[0].getBet()); i+=5)
		{
			betInput.addItem(i);
		}
		buttonPanel.add(betInput);

		if(value != -1)
			call.setText("Call ("+(value - poker.players[0].getBet())+")");
		else
			call.setText("All in");

		buttonPanel.add(call);
		buttonPanel.add(fold);

		this.updateUI();
	}


	public void setBet(int hand,int value)
		{

		if(!poker.players[hand].getFold())
			bets[hand].setText(poker.players[hand].getName() + "'s Bet: "+value);
		else
			bets[hand].setText(poker.players[hand].getName() + " (Folded)");
		}

	public void setPot(int value){
		potLabel.setText("Pot : " + value);
	}

	public void setMessage(String msg){
		dialog.append("\n"+msg);
		dialog.setCaretPosition(dialog.getText().length());
	}

	public void showPlayAgain(){
		buttonPanel.removeAll();
		buttonPanel.add(new JLabel());
		buttonPanel.add(playAgain);
		buttonPanel.add(new JLabel());
		this.updateUI();
	}

	public void setMoney(int money){
		cashLabel.setText("Player cash: "+money);
		cashLabel.setFont(cashLabel.getFont().deriveFont(16.0f));
		cashPanel.add(cashLabel);
	}

	public void highlight(int hand){
		Color green = new Color(20, 154, 60);
		Color dark = new Color(10, 77, 30);

		playerHand.setBackground(green);
		ai1Hand.setBackground(green);
		ai2Hand.setBackground(green);
		ai3Hand.setBackground(green);

		switch(hand){
			case 0:
				playerHand.setBackground(dark);
				break;
			case 1:
				ai1Hand.setBackground(dark);
				break;
			case 2:
				ai2Hand.setBackground(dark);
				break;
			case 3:
				ai3Hand.setBackground(dark);
				break;
		}
	}
}