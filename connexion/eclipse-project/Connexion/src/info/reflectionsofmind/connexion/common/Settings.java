package info.reflectionsofmind.connexion.common;

import info.reflectionsofmind.connexion.transport.jabber.JabberAddress;

import org.jivesoftware.smack.util.StringUtils;

public class Settings
{
	private String clientName;
	private JabberAddress jabberAddress;

	public void load()
	{
		this.jabberAddress = new JabberAddress("connexion:connexion@binaryfreedom.info:5222/connexion-client");
		this.clientName = StringUtils.randomString(8);
	}

	public JabberAddress getJabberAddress()
	{
		return this.jabberAddress;
	}

	public void setJabberAddress(final JabberAddress jabberAddress)
	{
		this.jabberAddress = jabberAddress;
	}

	public String getClientName()
	{
		return this.clientName;
	}

	public void setClientName(final String playerName)
	{
		this.clientName = playerName;
	}
}
