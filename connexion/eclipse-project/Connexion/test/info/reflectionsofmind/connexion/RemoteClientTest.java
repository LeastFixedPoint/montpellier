package info.reflectionsofmind.connexion;

import java.util.Arrays;

import info.reflectionsofmind.connexion.common.Client;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerAcceptedEvent;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.remote.client.RemoteClient;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;

import org.mockito.Mockito;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

public class RemoteClientTest
{
	@Test
	public void testRemoteClientPreGame()
	{
		Client client = new Client("Name");
		
		ITransport transportMock = Mockito.mock(ITransport.class);
		INode nodeMock = Mockito.mock(INode.class);
		Mockito.when(nodeMock.getTransport()).thenReturn(transportMock);
		
		IServer serverMock = Mockito.mock(IServer.class);
		IRemoteClient otherClientMock = Mockito.mock(IRemoteClient.class);
		Mockito.when(serverMock.getClients()).thenReturn(Arrays.asList(otherClientMock));
		
		IRemoteClient remoteClient = new RemoteClient(client, nodeMock);
		
		remoteClient.sendConnected(serverMock, otherClientMock);
		remoteClient.sendStatusChanged(serverMock, otherClientMock);
		remoteClient.sendDisconnected(serverMock, otherClientMock);
		remoteClient.sendChatMessage(serverMock, otherClientMock, "Message");
	}

	@Test
	public void testRemoteClientInGame()
	{
		Client client = new Client("Name");
		
		ITransport transportMock = Mockito.mock(ITransport.class);
		INode nodeMock = Mockito.mock(INode.class);
		Mockito.when(nodeMock.getTransport()).thenReturn(transportMock);
		
		IServer serverMock = Mockito.mock(IServer.class);
		IRemoteClient otherClientMock = Mockito.mock(IRemoteClient.class);
		Mockito.when(serverMock.getClients()).thenReturn(Arrays.asList(otherClientMock));
		
		IRemoteClient remoteClient = new RemoteClient(client, nodeMock);
		
		remoteClient.sendConnected(serverMock, otherClientMock);
		remoteClient.sendStatusChanged(serverMock, otherClientMock);
		remoteClient.sendDisconnected(serverMock, otherClientMock);
		remoteClient.sendChatMessage(serverMock, otherClientMock, "Message");
	}
}
