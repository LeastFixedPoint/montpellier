package info.reflectionsofmind.connexion.core.game;


public class Player
{
	private final String name;
	private int score = 0;
	private boolean disconnected = false;

	public Player(final String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

	public void addScore(final int bonus)
	{
		this.score += bonus;
	}

	public int getScore()
	{
		return this.score;
	}
	
	public boolean isDisconnected()
	{
		return this.disconnected;
	}
}
