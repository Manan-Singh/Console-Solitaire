package HW_3;

import java.util.Collections;
import java.util.Stack;
/**
 * Manan Singh
 * @author Manan
 */
public class CardStack extends Stack<Card> {
	
	private char type;
	
	/**
	 * Creates a new CardStack object 
	 * @param type The type of CardStack the object is. It can be a stock, waste, tableau, or a foundation
	 */
	public CardStack(char type){
		this.type = type;
	}
	
	/**
	 * Prints out the cards this CardStack in a type specific format
	 */
	public void printStack(){
		switch (type) {
		case 's' : {
			System.out.print("[XX]");
		}
		break;
		case 'w' : {
			System.out.print(this.peek().toString());
		}
		break;
		case 't' : {
			Stack<Card> temp = new Stack<Card>();
			Card currentCard = null;
			String fullStack = "";
			while(!this.isEmpty()){
				temp.push(this.pop());
			}
			while(!temp.isEmpty()){
				currentCard = temp.pop();
				fullStack += currentCard.isFaceUp() ? currentCard.toString() : "[XX]";
				this.push(currentCard);
			}
			System.out.println(fullStack);
		}
		break;
		case 'f' : {
			System.out.print(this.peek().toString());
		}
		break;
		}
	}
	
	/**
	 * Removes the top card from the stack
	 * @return The card that was just popped off the stack
	 */
	public Card pop(){
		return super.pop();
	}
	
	/**
	 * Adds a new card to the top of the stack
	 * @return The card that was just pushed onto the stack
	 */
	public Card push(Card c){
		return super.push(c);
	}
	
	/**
	 * Indicates if the stack is empty or not
	 * @return True if the stack is empty, false if not
	 */
	public boolean isEmpty(){
		return super.isEmpty();
	}
	
	/**
	 * Returns the number of elements in the stack
	 * @return The number of elements in the stack
	 */
	public int size(){
		return super.size();
	}
	
	
	
	/**
	 * Gets what type of CardStack
	 * @return The type of CardStack this is
	 */
	public char getType(){
		return type;
	}
}
