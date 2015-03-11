import java.lang.Math;

public class Deck{

	private Card[] cards = new Card[52];
	private int numCards = 52;

	public Deck(){
		for(int i=0;i<52;i++){
			cards[i] = new Card(i);
		}
	}

	public void shuffle(){
		numCards = 52;
		Card c;
		int j;
		for(int i=0;i<52;i++){
			j = (int)(Math.random()*52);
			c = cards[i];
			cards[i] = cards[j];
			cards[j] = c;
		}
	}

	public Card drawCard(){
		numCards--;
		return cards[numCards];
	}
}