package info.reflectionsofmind.connexion.gui.join;

import info.reflectionsofmind.connexion.gui.common.TransportName;
import info.reflectionsofmind.connexion.transport.ITransport;

import java.util.List;

import javax.swing.JComboBox;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public final class TransportComboBox extends JComboBox
{
	public TransportComboBox(final JoinGameFrame joinGameFrame)
	{
		super(wrap(joinGameFrame.getClient().getTransports()).toArray());
	}
	
	public ITransport getSelectedTransport()
	{
		return ((TransportWrapper) getSelectedItem()).getTransport();
	}

	private static List<TransportWrapper> wrap(List<ITransport> transports)
	{
		return Lists.transform(transports, new Function<ITransport, TransportWrapper>()
		{
			@Override
			public TransportWrapper apply(ITransport transport)
			{
				return new TransportWrapper(transport, TransportName.getName(transport));
			}
		});
	}

	public static final class TransportWrapper
	{
		private final ITransport transport;
		private final String name;

		private TransportWrapper(final ITransport transport, final String name)
		{
			this.transport = transport;
			this.name = name;
		}

		public ITransport getTransport()
		{
			return this.transport;
		}

		@Override
		public String toString()
		{
			return this.name;
		}
	}
}
