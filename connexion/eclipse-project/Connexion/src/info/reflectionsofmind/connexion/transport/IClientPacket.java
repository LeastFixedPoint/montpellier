package info.reflectionsofmind.connexion.transport;

public interface IClientPacket
{
	IClientNode getFrom();
	String getContents();
}