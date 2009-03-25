package info.reflectionsofmind.connexion.platform.core.transport;

public interface IClientPacket
{
	IClientNode getFrom();
	String getContents();
}