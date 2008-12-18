package info.reflectionsofmind.connexion;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import info.reflectionsofmind.connexion.common.Client;
import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.common.Client.State;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.remote.client.RemoteClient;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;

import java.util.Arrays;

import org.testng.annotations.Test;

public class RemoteClientTest
{
	@Test
	public void testRemoteClientPreGame() throws Exception
	{
		Client client = new Client("Name");
		
		ITransport transportMock = mock(ITransport.class);
		INode nodeMock = mock(INode.class);
		when(nodeMock.getTransport()).thenReturn(transportMock);
		
		IServer serverMock = mock(IServer.class);
		IRemoteClient otherClientMock = mock(IRemoteClient.class);
		when(serverMock.getClients()).thenReturn(Arrays.asList(otherClientMock));
		
		IRemoteClient remoteClient = new RemoteClient(client, nodeMock);
		
		remoteClient.sendConnected(serverMock, otherClientMock);
		verify(transportMock).send(eq(nodeMock), anyString());
		
		remoteClient.sendStateChanged(serverMock, otherClientMock, State.ACCEPTED);
		verify(transportMock).send(eq(nodeMock), anyString());

		remoteClient.sendDisconnected(serverMock, otherClientMock, DisconnectReason.SERVER_REQUEST);
		verify(transportMock).send(eq(nodeMock), anyString());

		remoteClient.sendChatMessage(serverMock, otherClientMock, "Message");
		verify(transportMock).send(eq(nodeMock), anyString());
}

	@Test
	public void testRemoteClientInGame() throws Exception
	{
		Client client = new Client("Name");
		
		ITransport transportMock = mock(ITransport.class);
		INode nodeMock = mock(INode.class);
		when(nodeMock.getTransport()).thenReturn(transportMock);
		
		IServer serverMock = mock(IServer.class);
		IRemoteClient otherClientMock = mock(IRemoteClient.class);
		when(serverMock.getClients()).thenReturn(Arrays.asList(otherClientMock));
		
		IRemoteClient remoteClient = new RemoteClient(client, nodeMock);
		
		remoteClient.sendGameStarted(serverMock);
		verify(transportMock).send(eq(nodeMock), anyString());

		remoteClient.sendLastTurn(serverMock);
		verify(transportMock).send(eq(nodeMock), anyString());
}
}
