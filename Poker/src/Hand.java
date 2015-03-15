import java.awt.Graphics;
import javax.swing.*;
import java.lang.Math;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Hand{

	private Card[] cards;
	private Card[] sorted;
	private Deck deck;
	private int size;
	private int bet = 0;
	private String scoreName = "";

	public int position;

	private boolean player;
	private JPanel j;
	private boolean vertical;
	private boolean up;
	private boolean fold;

	private Poker poker;
	private GUI gui;

	public int score;

	public Hand(){}
	public Hand(int position, Poker p){
		cards = new Card[5];
		sorted = new Card[5];
		deck = new Deck();
		size = 0;
		player = position==0 ? true: false;
		this.position = position;
		poker = p;
		fold = false;
		gui = poker.gameGui;
	}

	public void reset(){
		cards = new Card[5];
		size = 0;
	}

	public void paint(){
		j.removeAll();

	//if position is 2 or 3 pack the JLabels first
		if(position > 1)
			for(int i = 0; i < 5-size; i++)
			{
				j.add(new JLabel());
			}
		if(size > 0){
	//if posiition is 0 or 1, add facedown card 1st
			if(position < 2){
				cards[0].paint(j,vertical,(player || up));
		//paint cards
				for(int i=1;i<size;i++)
					cards[i].paint(j,vertical,true);
			}
	//if position is 0 or 1, add facedown card last
			else{
		//paint cards
				for(int i=size-1;i>0;i--){
					cards[i].paint(j,vertical,true);
				}
				cards[0].paint(j,vertical,(player || up));
			}
		}
	//if position is 0 or 1 pack the JLabels last
		if(position < 2)
			for(int i = 0; i < 5-size; i++)
			{
				j.add(new JLabel());
			}
		
		j.updateUI();
	}

	public void setDeck(Deck deck){
		this.deck = deck;
	}

	public boolean drawCard(int n){
		for(int i=0;i<n;i++){
			if(size < cards.length){
				cards[size] = deck.drawCard();
				size ++;
			}
			else{
				sort();
				return false;
			}
		}
		sort();
		paint();
		return true;
	}

	private void sort(){
		sorted = new Card[size];
		for(int i=0;i<size;i++)
			sorted[i] = cards[i];

		Arrays.sort(sorted);
	}

	// used only for sort()
	private int lowCard(){
		int index = 0;
		int low = 100; // should be higher than any card value
		for(int i=0;i<size;i++)
			if(cards[i] != null)
				if(cards[i].getValue() < low){
					low = cards[i].getValue();
					index = i;
				}
		return index;
	}

	public Card[] getCards(){
		return cards;
	}

	public void add(JPanel panel, boolean v)
	{
		j = panel;
		vertical = v;
	}

	public void flip()
	{
		up = !up;
	}

	public void addPoker(Poker p)
	{
		poker = p;
	}

	public void setScoreName(){
		if(score < 100)
			scoreName = "High Card";
		else if(score < 200)
			scoreName = "Pair";
		else if(score < 300)
			scoreName = "Two Pair";
		else if(score < 400)
			scoreName = "Three of a Kind";
		else if(score < 500)
			scoreName = "Straight";
		else if(score < 600)
			scoreName = "Flush";
		else if(score < 700)
			scoreName = "Full House";
		else if(score < 800)
			scoreName = "Four of a Kind";
		else if(score < 1000)
			scoreName = "Straight Flush";
	}

	/*--------------------------------------------------------------------------
	Section is for computing hand ranking
	--------------------------------------------------------------------------*/

	// simply meant to compare the rank of two hands
/**/	    public int getScore(){
/**/			if(!fold)
/**/			{
/**/				int score = highCard();
/**/
/**/				int[] temps = {pair(),twoPair(),three(),straight(),flush(),
/**/							fullHouse(),four(),straight() + flush()};
/**/
/**/				for(int i : temps)
/**/					if(i > score)
/**/						score = i;
/**/				//System.out.println(position+" "+score);
					this.score = score;
/**/				return score;
/**/			}
/**/			else
/**/			{
					this.score = -1;
/**/				return -1;
/**/			}
/**/		}
/**/
/**/	private int four(){
/**/		int score = 0;
/**/
/**/		int[] values = new int[5];
/**/		int[] matches = new int[5];
/**/		int index = 0;
/**/		for(int i=0;i<size && !Arrays.asList(matches).contains(3);i++){
/**/			if(!Arrays.asList(values).contains(cards[i].getValue())){
/**/				values[index] = cards[i].getValue();
/**/				matches[index] = 0;
/**/				index++;
/**/			}
/**/			else
/**/				matches[Arrays.asList(values).indexOf(cards[i].getValue())]++;
/**/		}
/**/
/**/		index = Arrays.asList(matches).indexOf(3);
/**/		if(index > -1){
				//scoreName = "4 of a kind";
/**/			score = 700 + values[index];
/**/		}
/**/
/**/		return score;
/**/	}
/**/
/**/	//done
/**/	private int fullHouse(){
/**/		int score = 0;
/**/
/**/		if(three() > 300 && twoPair() > 200){
				//scoreName = "Full House";
/**/			score = 600 + highCard();
			}
/**/
/**/		return score;
/**/	}
/**/
/**/	//done
/**/	private int flush(){
/**/		int score = 0;
/**/		boolean flush = true;
/**/
/**/		if(size > 0){
/**/			Card c = cards[0];
/**/			for(int i=1;i<size;i++)
/**/				if(c.getSuit() != cards[i].getSuit())
/**/					flush = false;
/**/	}
/**/
/**/		if(flush){
				//scoreName = "Flush";
/**/			score = 500 + highCard();
			}
/**/
/**/		return score;
/**/	}
/**/
/**/	//done
/**/	private int straight(){
/**/		int score = 0;
/**/		boolean straight = true; //start assuming true, try to prove it wrong
/**/
/**/		if(size > 0) //it should be, but just incase
/**/			for(int i=0;i<size;i++)
/**/				if(sorted[i].getValue() != sorted[0].getValue() + i)
/**/					straight = false;
/**/
/**/		if(straight){
				//scoreName = "Straight";
/**/			score = 400 + highCard();
			}
/**/
/**/		return score;
/**/	}
/**/
/**/	//done
/**/	private int three(){
/**/
		if(twoPair()>0){
			int highVal = twoPair() - 200;
			int count = 0;
			int notHigh = -1;
			for (int i = 0; i<size; i++){
				if(sorted[i].getValue() == highVal){
					count++;
					}
				}
			if(count==3){
				//scoreName = "3 of a kind";
				return 300 + highVal;
			}
			for(int i = 0; i<size; i++){
				if(sorted[i].getValue()!= highVal){
					if(notHigh == -1){
						notHigh = sorted[i].getValue();
						}
					else if (sorted[i].getValue()!= notHigh)
						return 0;
					}
				}
				//scoreName = "3 of a kind";
				return 300 + notHigh;
			}
		else if(pair()>0){
			int val = pair()-100;
			int count = 0;
			for(int i = 0; i<size; i++){
				if(sorted[i].getValue()==val)
					count++;
				}
				if(count>2)
					//scoreName = "3 of a kind";
				return count>2 ? 300 + val : 0;
			}
	return 0;

/**/}
/**/
/**/	//done
/**/	private int twoPair(){
			if(pair()>0){
				int firstPair = pair()-100;
				for(int i= 0; i<size; i++){
					if(sorted[i].getValue() != firstPair){
/**/					for (int j = i+1; j<size; j++){
/**/						if (sorted[i].getValue() == sorted[j].getValue()){
								//scoreName = "Two Pair";
/**/							return firstPair>sorted[j].getValue() ? 200 + firstPair : 200 + sorted[j].getValue();
/**/						}
						}
/**/				}
/**/			}
			}
/**/		return 0;
/**/	}
/**/	//done
/**/	private int pair(){

/**/		for(int i= 0; i<size; i++){
/**/			for (int j = i+1; j<size; j++){
/**/				if (sorted[i].getValue() == sorted[j].getValue()){
						//scoreName = "Pair";
/**/					return 100 + sorted[j].getValue();
/**/					}
/**/				}
/**/		}
/**/		return 0;
/**/
/**/	}
/**/
/**/	public int highCard(){
/**/		int value = 0;
/**/		for(int i=0;i<size;i++)
/**/			if(cards[i].getValue() > value){
					//scoreName = "High Card";
/**/				value = cards[i].getValue();
/**/			}
/**/		return value;
/**/	}
/**/
	/*--------------------------------------------------------------------------
	End hand ranking section
	--------------------------------------------------------------------------*/

	public int getBet(){
		return bet;
	}

	public String getScoreName()
	{
		return scoreName;
	}

	private void increaseBet(int n){
		bet += n;
		poker.highBet = bet;
		poker.pool += n;
		gui.setPot(poker.pool);
		gui.setBet(position,poker.highBet);
	}

	public void resetBet(){
		bet = 0;
		gui.setBet(position,0);
	}
	public void call(boolean f)
	{
		if(poker.firstBet){
			poker.lastRaised = this;
		}
		increaseBet(poker.money);
		poker.firstBet = false;
	
		poker.setMessage("Player goes all in.");
	}
	public void call()
	{
		if(poker.firstBet){
			poker.lastRaised = this;
		}
		increaseBet(poker.highBet - bet);
		poker.firstBet = false;
		if(position > 0)
			poker.setMessage("AI #"+position+ " called.");
		else
			poker.setMessage("Player called.");
	}

	private void raise(int n)
	{
		poker.highBet += n;
		poker.lastRaised = this;
		call();

		if(position > 0)
			poker.setMessage("AI #"+position+ " raised "+n);
		else
			poker.setMessage("Player raised: "+n);
	}

	public void bet()
		{
		double d = Math.random();
		boolean canfold = false;
		for(int i=0;i<4;i++)
			if(i!=position && !poker.players[i].getFold())
				canfold = true;
		if(d < .1 && canfold)
			{
				fold();
			}
		else if(d < .6)
			{
			raise(10);
			}
		else
			{
			call();
			}

		}
	public void fold()
		{
		setFold(true);
		gui.setBet(position,0);
		}
	public int getSize()
		{
		return size;
		}

	public void setFold(boolean s)
		{
		fold = s;
		}

	public boolean getFold()
		{
		return fold;
		}
	public String getName()
		{
			if(position == 0)
				{
				return "Player";
				}
			else
			{
			return "AI #" + position;
			}
		}
}