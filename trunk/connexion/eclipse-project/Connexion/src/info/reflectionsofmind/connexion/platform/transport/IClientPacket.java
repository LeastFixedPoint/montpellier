package info.reflectionsofmind.connexion.platform.transport;

public interface IClientPacket
{
	IClientNode getFrom();
	String getContents();
}