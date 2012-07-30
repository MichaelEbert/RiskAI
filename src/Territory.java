import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Territory {
	private List<Territory> linkedTerritories;
	public String name;
	private Continent continent; //Only for data retrieval
	private List<String> linkedTerritoryNames;
	private Player owner;
	private TerritoryGraphics graphic;
	private static BufferedWriter territoryDataWriter;
	
	public Territory(int x, int y)
	{
		linkedTerritories = new ArrayList<Territory>();
		try {			
			territoryDataWriter = new BufferedWriter(new PrintWriter(new FileWriter("TerritoryWriteData.txt")));
		} catch (IOException e) {
			throw new RuntimeException("WTF ECLIPSE");
		}
		//graphic = new TerritoryNode(this);
		//graphic.setCoords(100, 100);
		graphic = new TerritoryGraphics(x,y,this);
	}
	public Territory(String name, Continent continent, List<String> ajacentTerritoryNames, int x, int y)
	{
		this(x,y);
		linkedTerritoryNames= new LinkedList<String>(ajacentTerritoryNames);
		this.name=name;
		this.continent=continent;
	}
	public List<String> getAjacentTerritoryNameList()
	{
		return new LinkedList<String>(linkedTerritoryNames);
	}
	public List<Territory> getAjacentTerritoryList()
	{
		return new LinkedList<Territory>(linkedTerritories);
	}
	public void link(Territory territory)
	{
		linkedTerritories.add(territory);
	}
	public int getNumberOfAjacentTerritories()
	{
		return linkedTerritories.size();
	}
	public Territory getRandomLinkedTerritory()
	{
		Random random = new Random();
		return linkedTerritories.get(random.nextInt(linkedTerritories.size()));
	}
	
	public Territory getRandomLinkedUnownedTerritory(Player player)
	{
		List<Territory> territoryList = new ArrayList<Territory>();
		for(int i=0;i<linkedTerritories.size();i++)
		{
			territoryList.add(linkedTerritories.get(i));
		}
		Collections.shuffle(territoryList);
		for(Territory territory : territoryList)
		{
			if(territory.getOwner() != player)
			{
				return territory;
			}
		}
		return null; //no attackable countries
	}
	
	/**
	 * Returns the number of troops on a territory minus the number of enemy
	 * troops on surrounding territories
	 * @return int
	 */
	public int getRelativeStrength()
	{
		int enemyTroops = 0;
		for(Territory territory : getAdjacentEnemyTerritories())
		{
			enemyTroops += territory.getUnitCount();
		}
		return getUnitCount() - enemyTroops;
	}
	
	/**
	 * Returns a set of adjacent enemy territories
	 * @return Set<Territory>
	 */

	public Set<Territory> getAdjacentEnemyTerritories()
	{
		Set<Territory> adjacentTerritories = new HashSet<Territory>();
		for (Territory t : getAjacentTerritoryList())
		{
			if(t.getOwner()!=getOwner())
				adjacentTerritories.add(t);
		}
		return adjacentTerritories;
	}
	
	public Territory getRandomLinkedOwnedTerritory(Player player)
	{
		List<Territory> territoryList = new ArrayList<Territory>();
		for(int i=0;i<linkedTerritories.size();i++)
		{
			territoryList.add(linkedTerritories.get(i));
		}
		Collections.shuffle(territoryList);
		for(Territory territory : territoryList)
		{
			if(territory.getOwner() == player)
			{
				return territory;
			}
		}
		return null; //no neighboring friendly countries
	}
	
	public int getUnitCount()
	{
		return owner!=null ? owner.getUnitsInTerritory(this) : 0;
	}
	
	public Player getOwner()
	{
		return owner;
	}

	public void setOwner(Player owner)
	{
		this.owner=owner;
	}
	
	public String toString()
	{
		/*String namePart = name + ", ";
		String linkedPart = "linked to ";
		for (String str : linkedTerritoryNames)
		{
			linkedPart = linkedPart + str + " and ";
		}
		String continentPart = "on continent " + continent.name;
		String ownerPart = ", with " + units + " armies on it";
		return namePart + linkedPart + continentPart + ownerPart;*/
		
		
		return name;
	}
	
	public String semicolonForm()
	{
		String line = name + ";";
		line += continent.name + ";";
		line+=linkedTerritoryNames.size() + ";";
		for (String str : linkedTerritoryNames)
		{
			line += str + ",";
		}
		line += ";";
		line += String.valueOf(getXCoord()) + ";";
		line += String.valueOf(getYCoord()) + ";";
		return line;
	}
	
	public void writeToFile()
	{
		try {
			territoryDataWriter.write(semicolonForm() + System.getProperty("line.separator"));
			territoryDataWriter.flush();
		} catch (IOException e) {
			throw new RuntimeException("WTF JAVA");
		}
	}
	public int getXCoord()
	{
		return graphic.xCoord;
	}
	public int getYCoord()
	{
		return graphic.yCoord;
	}
	public void setCoordinates(int x, int y)
	{
		graphic.setCoords(x, y);
	}
	public TerritoryGraphics getTerritoryGraphic()
	{
		return this.graphic;
	}

}
