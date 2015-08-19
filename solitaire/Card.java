package HW_3;
/**
 * Manan Singh
 * @author Manan
 */
public class Card {
	
	public static String[] values = {" ","A","2","3","4","5","6","7","8","9","10","J","Q","K"};
	public static char[] suits = {' ', '\u2666', '\u2663','\u2665', '\u2660'};
	private int suit;
	private int value;
	private boolean isFaceUp;
	
	/**
	 * Creates a Card object
	 */
	public Card(){
		
	}
	
	/**
	 * Creates a Card object
	 * @param value The number of the card
	 * @param suit The suit of the card
	 */
	public Card(int value, int suit){
		if((value > 0 && value < 14) && (suit > 0 && suit < 5)){
			this.setValue(value);
			this.setSuit(suit);
			setFaceUp(false);
		}
	}
	
	/**
	 * Creates a Card object
	 * @param value The number of the card
	 * @param suit The suit of the card
	 * @param faceUp If the card is face up or not
	 */
	public Card(int value, int suit, boolean faceUp){
		this(value, suit);
		this.isFaceUp = faceUp;
	}
	
	/**
	 * If the card is red or not
	 * @return If this card is red
	 */
	public boolean isRed(){
		if(suit % 2 != 0){
			return true;
		}
		return false;
	}
	
	/**
	 * Returns a string representation of this card
	 */
	public String toString(){
		return "[" + values[value] + suits[suit] + "]";
	}
	
	/**
	 * Gets the suit of this card
	 * @return The suit of this card
	 */
	public int getSuit() {
		return suit;
	}
	
	/**
	 * Sets the suit of the card
	 * @param suit The new suit of this card
	 */
	public void setSuit(int suit) {
		if(suit > 0 && suit < 5){
			this.suit = suit;
		}
	}
	
	/**
	 * Gets the number of the card
	 * @return The numerical value of this card
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Sets the value of the card
	 * @param value The new numerical value of this card
	 */
	public void setValue(int value) {
		if(value > 0 && value < 14){
			this.value = value;
		}
	}
	
	/**
	 * Returns if the card is face up or not
	 * @return True if the card is face up, false if not
	 */
	public boolean isFaceUp() {
		return isFaceUp;
	}

	/**
	 * Sets if the card is face up or not
	 * @param isFaceUp If this card is face up or not
	 */
	public void setFaceUp(boolean isFaceUp) {
		this.isFaceUp = isFaceUp;
	}
	
	
}
