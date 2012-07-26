import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public abstract class Player {
	//private List<Card> cardList;
	private Map<Territory, Integer> unitMap;
	protected boolean isHuman;
	private int playerID;
	private Color color;
	
	public Player(int playerID)
	{
		this.playerID = playerID;
		
		//cardList = new ArrayList<Card>();
		unitMap = new HashMap<Territory, Integer>();
	}
	public Player(int playerID, Color color)
	{
		this(playerID);
		this.color = color;
	}
	protected abstract void placeReinforcements(int number);
	public void attack(Territory from, Territory to) 
	{
		int unitsFrom = from.getUnitCount();
		int unitsTo = to.getUnitCount();
		
		int countDiceFrom = Math.min(unitsFrom-1, 3);
		int countDiceTo = Math.min(unitsTo, 2);
		
		List<Integer> diceFromList = new ArrayList<Integer>();
		List<Integer> diceToList = new ArrayList<Integer>();
		
		Random random = new Random();
		for(int i=0;i<countDiceFrom;++i)
		{
			//Roll attacking dice
			diceFromList.add(random.nextInt(6)+1);
		}
		for(int i=0;i<countDiceTo;++i)
		{
			//Roll defending dice
			diceToList.add(random.nextInt(6)+1);
		}
		
		Collections.sort(diceFromList);
		Collections.reverse(diceFromList);
		Collections.sort(diceToList);
		Collections.reverse(diceToList);
		
		for(int i=0;i<Math.min(countDiceFrom, countDiceTo);++i)
		{
			if(countDiceFrom > countDiceTo)
			{
				//attacker wins
				to.getOwner().addToTerritory(to, -1);
			}
			else
			{
				//defender wins
				addToTerritory(from, -1);
				from.getOwner().addToTerritory(from, -1);
			}
		}
	}
	
	public abstract void doAttackPhase();
	
	/**
	 * Gets a random territory in which the player has units.
	 * Use only if you want your AI to be awful.
	 * @return Random territory owned by player
	 */
	public Territory getRandomControlledTerritory()
	{
		Set<Territory> territories = unitMap.keySet();
		Random random = new Random();
		int territoryID = random.nextInt(territories.size());
		int i = 0;
		for(Territory terr : territories)
		{
			if(i == territoryID)
			{
				return terr;
			}
			++i;
		}
		throw new RuntimeException("wat");
	}
	/**
	 * Adds a number of units into a territory.  Units are friendly to the player
	 * currently controlling the specified territory.
	 * @param territory Territory to add units to
	 * @param number Number of units to add
	 */
	public void addToTerritory(Territory territory, int number)
	{
		unitMap.put(territory, unitMap.get(territory)+number);
		territory.setUnitNumber(number);
	}
	
	/**
	 * Gets whether the territory is owned by the player
	 * @param territory
	 * @return whether or not the territory is owned by the player
	 */
	public boolean isOwnerOf(Territory territory)
	{
		return unitMap.containsKey(territory);
	}
	/**
	 * Gets the number of units currently in the specified territory.
	 * Territory must be owned by the current player.
	 * @param territory
	 * @return int Number of units
	 */
	public int getUnitsInTerritory(Territory territory)
	{
		return unitMap.get(territory);
	}
	
	/**
	 * Gets the current color of the player
	 * @return Color
	 */
	public Color getColor()
	{
		if (color == null) return Color.WHITE;
		return color;
	}
}