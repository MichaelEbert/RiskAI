public class RiskAI {
	static RiskAI riskAI;
	GameData currentGame;
	static TerritoryData aaa= new TerritoryData();
	public static void main(String args[])
	{
		riskAI = new RiskAI();
	}
	public RiskAI()
	{
		currentGame = new GameData(4); //4 player game
	}
}