package info.reflectionsofmind.connexion.local.client;

import info.reflectionsofmind.connexion.transport.jabber.JabberAddress;

import org.jivesoftware.smack.util.StringUtils;

public class Settings
{
	private String playerName;
	private JabberAddress jabberAddress;

	public void load()
	{
		this.jabberAddress = new JabberAddress("connexion:connexion@binaryfreedom.info:5222/connexion-client");
		this.playerName = StringUtils.randomString(8);
	}

	public JabberAddress getJabberAddress()
	{
		return this.jabberAddress;
	}

	public void setJabberAddress(final JabberAddress jabberAddress)
	{
		this.jabberAddress = jabberAddress;
	}

	public String getPlayerName()
	{
		return this.playerName;
	}

	public void setPlayerName(final String playerName)
	{
		this.playerName = playerName;
	}
}
