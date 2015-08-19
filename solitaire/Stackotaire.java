package HW_3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;
/**
 * Manan Singh 
 * @author Manan
 */
public class Stackotaire {
	
	private static Stack<Card> deck;
	private static ArrayList<Card> shallowDeck;
	
	public static void main(String[] args) {
		//For the main game, it exists to facilitate restarting the game
		boolean running = true;
		//Will continuously run so that the board will keep being displayed 
		boolean keepPlaying = true;
		while(running){
			//In case someone restarted the game
			keepPlaying = true;
			CardStack[] foundations = {new CardStack('f'), new CardStack('f'), new CardStack('f'),
					new CardStack('f')};
			CardStack[] tableau = {new CardStack('t'), new CardStack('t'), new CardStack('t'),
					new CardStack('t'), new CardStack('t'), new CardStack('t'), new CardStack('t')};
			CardStack stock = new CardStack('s');
			CardStack waste = new CardStack('w');
			initialize();
			//Distribute the cards to the Tableau
			for(int i = 0; i < tableau.length; i++){
				for(int j = 0; j <= i; j++){
					Card temp = deck.pop();
					if(i == j){
						temp.setFaceUp(true);
					}
					tableau[i].push(temp);
				}
			}
			//Distribute the rest of the cards to the stock
			while(!deck.isEmpty()){
				stock.push(deck.pop());
			}
			while(keepPlaying){
				Scanner reader = new Scanner(System.in);
				System.out.println();
				displayBoard(stock, waste, tableau, foundations);
				System.out.println("Enter a command: ");
				String command = reader.next().toUpperCase();
				switch (command) {
					case "QUIT" : {
						System.out.println("Are you sure you want to quit? (Y/N)");
						String response = reader.next().toUpperCase();
						while(true){
							if(!((response.equals("Y") || (response.equals("N"))))){
								System.out.println("Are you sure you want to quit? (Y/N)");
								response = reader.next().toUpperCase();
								continue;
							}
							break;
						}
						if(response.equals("Y")){
							running = false;
							keepPlaying = false;
							System.out.println("Sorry, you lose!");
						}
					}
					break;
					case "RESTART" : {
						System.out.println("Do you want to restart the game? (Y/N)");
						String response = reader.next().toUpperCase();
						while(true){
							if(!((response.equals("Y") || (response.equals("N"))))){
								System.out.println("Do you want to restart the game? (Y/N)");
								response = reader.next().toUpperCase();
								continue;
							}
							break;
						}
						if(response.equals("Y")){
							keepPlaying = false;
							System.out.println("Sorry, you lose! Starting new game.");
						}
					}
					break;
					case "DRAW" : {
						waste.push(stock.pop());
						if(stock.isEmpty()){
							while(!waste.isEmpty()){
								stock.push(waste.pop());
							}
							waste.push(stock.pop());
						}
					}
					break;
					case "MOVE" : {
						String moveFrom = reader.next();
						String moveTo = reader.next();
						if(moveFrom.length() > 2 || moveTo.length() > 2){
							System.out.println("Invalid arguments");
							continue;
						}
						try {
							move(foundations, tableau, waste, moveFrom, moveTo);
						} catch (InvalidMoveException e) {
							System.out.println("That move isn't available");
						}
					}
					break;
					case "MOVEN" : {
						String moveFrom = reader.next();
						String moveTo = reader.next();
						int num = reader.nextInt();
						if(moveFrom.length() > 2 || moveTo.length() > 2){
							System.out.println("Invalid arguments");
							continue;
						}
						try {
							moveN(tableau, num, moveFrom, moveTo);
						} catch (InvalidMoveException e) {
							System.out.println("That move is not available");
						}
					}
					break;
					default: {
						System.out.println("That's not a given command!");
					}
				}
				//Check if the board is winning
				if(checkWinningBoard()){
					System.out.println("Good job! you won!");
					running = false;
					keepPlaying = false;
				}else{
					continue;
				}
			}
		}
	}
	
	/**
	 * Generates the cards and distributes them to the deck
	 */
	private static void initialize(){
		deck = new Stack<Card>();
		shallowDeck = new ArrayList<Card>();
		//Generate 52 cards and shuffle the order
		for(int i = 1; i <= Card.values.length - 1; i++){
			for(int j = 1; j <= Card.suits.length - 1; j++){
				Card c = new Card(i, j, false);
				deck.push(c);
				shallowDeck.add(c);
			}
		}
		Collections.shuffle(deck);
	}
	
	/**
	 * Renders an image of the current position in the game board
	 * @param stock The stock in the game
	 * @param waste The waste in the game
	 * @param tableau The tableau in the game
	 * @param foundations The foundations in the game
	 */
	private static void displayBoard(CardStack stock, CardStack waste, CardStack[] tableau, 
			CardStack[] foundations){
		for(int i = 0; i < foundations.length; i++){
			if(foundations[i].isEmpty()){
				System.out.print("[F" + (i + 1) + "]");
			}else{
				foundations[i].printStack();
			}
		}
		System.out.print("\tW1 ");
		if(waste.isEmpty()){
			System.out.print("[ ]");
		}else{
			waste.printStack();
		}
		System.out.print("     ");
		stock.printStack();
		System.out.print(" " + stock.size());
		System.out.println("\n");
		for(int i = tableau.length - 1; i >= 0; i--){
			System.out.print("T" + (i + 1) + " ");
			tableau[i].printStack();
		}
	}
	
	/**
	 * Moves a card from one CardStack to another
	 * @param foundations The foundations in the game
	 * @param tableau The tableau in the game
	 * @param waste The waste in the game
	 * @param moveFrom The CardStack from where the card will be taken from
	 * @param moveTo The CardStack to where the card will be moved to
	 * @throws InvalidMoveException Occurs if a move cannot be made
	 */
	private static void move(CardStack[] foundations, CardStack[] tableau, CardStack waste,
			String moveFrom, String moveTo) throws InvalidMoveException{
		CardStack toMoveFrom = null;
		CardStack toMoveTo = null;
		//Get the location from where you have to move from
		if(moveFrom.toLowerCase().charAt(0) == 't' && 
			     (Character.getNumericValue(moveFrom.charAt(1)) > 0 && 
			     Character.getNumericValue(moveFrom.charAt(1)) < 8)){
			//Must subtract one so that you get the correct position in the array
			toMoveFrom = tableau[Character.getNumericValue(moveFrom.charAt(1)) - 1];
		}else if(moveFrom.toLowerCase().charAt(0) == 'f' && 
				(Character.getNumericValue(moveFrom.charAt(1)) > 0 && 
				Character.getNumericValue(moveFrom.charAt(1)) < 8)){
			toMoveFrom = foundations[Character.getNumericValue(moveFrom.charAt(1)) - 1];
		}else if(moveFrom.toLowerCase().charAt(0) == 'w' && 
				(Character.getNumericValue(moveFrom.charAt(1)) == 1)){
			toMoveFrom = waste;
		}else{
			throw new InvalidMoveException("Not a valid move");
		}
		//Get the location from where you have to move to
		if(moveTo.toLowerCase().charAt(0) == 't' && 
			     (Character.getNumericValue(moveTo.charAt(1)) > 0 && 
			     Character.getNumericValue(moveTo.charAt(1)) < 8)){
			toMoveTo = tableau[Character.getNumericValue(moveTo.charAt(1)) - 1];
		}else if(moveTo.toLowerCase().charAt(0) == 'f' && 
				(Character.getNumericValue(moveTo.charAt(1)) > 0 && 
				Character.getNumericValue(moveTo.charAt(1)) < 8)){
			toMoveTo = foundations[Character.getNumericValue(moveTo.charAt(1)) - 1];
		}else{
			throw new InvalidMoveException("Not a valid move");
		}
		Card c2 = null;
		try{
			c2 = toMoveFrom.peek();
		}catch(EmptyStackException e){
			c2 = null;
		}
		Card c1 = null;
		try{
			c1 = toMoveTo.peek();
		}catch(EmptyStackException e){
			//In this case, it should be a king being transferred to an empty stack
			c1 = null;
		}
		//If the stack you're moving the card to is a foundation
		if(toMoveTo.getType() == 'f'){
			if(toMoveFrom.getType() == 'f'){
				throw new InvalidMoveException();
			}else if(toMoveTo.isEmpty()){
				if(c2.getValue() == 1){
					Card c = toMoveFrom.pop();
					c.setFaceUp(true);
					toMoveTo.push(c);
					if(!toMoveFrom.isEmpty()){
						toMoveFrom.peek().setFaceUp(true);
					}
				}else{
					throw new InvalidMoveException();
				}
			}else{
				if(c2.getValue() - 1 == toMoveTo.peek().getValue()){
					if(c2.getSuit() == toMoveTo.peek().getSuit()){
						Card c = toMoveFrom.pop();
						c.setFaceUp(true);
						toMoveTo.push(c);
						if(!toMoveFrom.isEmpty()){
							toMoveFrom.peek().setFaceUp(true);
						}
					}else{
						throw new InvalidMoveException();
					}
				}else{
					throw new InvalidMoveException();
				}
			}
		}else{
			if(compareCards(c1, c2)){
				Card c = toMoveFrom.pop();
				c.setFaceUp(true);
				toMoveTo.push(c);
				if(!toMoveFrom.isEmpty()){
					toMoveFrom.peek().setFaceUp(true);
				}
			}else{
				throw new InvalidMoveException();
			}
		}
		
	}
	
	/**
	 * Moves a group of cards from one CardStack to another
	 * @param tableau The tableau of the game
	 * @param n The number of cards in the group
	 * @param moveFrom The CardStack from where the cards are taken from
	 * @param moveTo The CardStack to where the cards are placed
	 * @throws InvalidMoveException Occurs when a move cannot be made
	 */
	private static void moveN(CardStack[] tableau, int n, String moveFrom,
			String moveTo) throws InvalidMoveException {
		CardStack toMoveFrom = null;
		CardStack toMoveTo = null;
		//Get the location of the tableau you have to move from
		if(moveFrom.toLowerCase().charAt(0) == 't' && 
			     (Character.getNumericValue(moveFrom.charAt(1)) > 0 && 
			     Character.getNumericValue(moveFrom.charAt(1)) < 8)){
			//Must subtract one so that you get the correct position in the array
			toMoveFrom = tableau[Character.getNumericValue(moveFrom.charAt(1)) - 1];
		}else{
			throw new InvalidMoveException("Not a valid move");
		}
		//Get the location of the tableau you have to move to
		if(moveTo.toLowerCase().charAt(0) == 't' && 
			     (Character.getNumericValue(moveTo.charAt(1)) > 0 && 
			     Character.getNumericValue(moveTo.charAt(1)) < 8)){
			toMoveTo = tableau[Character.getNumericValue(moveTo.charAt(1)) - 1];
		}else{
			throw new InvalidMoveException("Not a valid move");
		}
		if(n > toMoveFrom.size()){
			throw new InvalidMoveException("Not a valid move");
		}
		//Stores the cards that needs to be transferred
		CardStack holder = new CardStack('t');
		for(int i = 0; i < n; i++){
			holder.push(toMoveFrom.pop());
		}
		Card c = null;
		try{
			c = toMoveTo.peek();
		}catch(EmptyStackException e){
			c = null;
		}
		if(!holder.peek().isFaceUp() || !compareCards(c, holder.peek())){
			while(!holder.isEmpty()){
				toMoveFrom.push(holder.pop());
			}
			throw new InvalidMoveException();
		}else{
			while(!holder.isEmpty()){
				toMoveTo.push(holder.pop());
				if(!toMoveFrom.isEmpty()){
					toMoveFrom.peek().setFaceUp(true);
				}
			}
		}
		
	}
	
	/**
	 * Shows if a card can be logically placed beneath another card according to the rules of solitaire
	 * @param c1 The card that is already placed
	 * @param c2 The card that will be placed under the other
	 * @return True if c2 can be placed under c1, false if not
	 */
	private static boolean compareCards(Card c1, Card c2){
		if(c2.getValue() == 13 && c1 == null){
			return true;
		}else if(c1 == null && (c2 == null || c2 != null)){
			return false;
		}else if(c1.getValue() == c2.getValue() + 1){
			if(c2.isRed() && !c1.isRed() || !c2.isRed() && c1.isRed()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the game has been won 
	 * @return True if all the cards are flipped up, false if not
	 */
	private static boolean checkWinningBoard(){
		for(Card x : shallowDeck){
			if(!x.isFaceUp()){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * An Exception class for handling moves that cannot be made
	 * @author Manan Singh
	 */
	static class InvalidMoveException extends Exception {
		
		public InvalidMoveException(){
			super();
		}
		
		public InvalidMoveException(String message){
			super(message);
		}
	}

}
