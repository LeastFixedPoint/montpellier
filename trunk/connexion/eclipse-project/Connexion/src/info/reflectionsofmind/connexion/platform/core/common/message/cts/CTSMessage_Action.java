package info.reflectionsofmind.connexion.platform.core.common.message.cts;

import info.reflectionsofmind.connexion.platform.core.transport.IClientNode;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class CTSMessage_Action extends AbstractCTSMessage
{
	public final static String PREFIX = AbstractCTSMessage.EVENT_PREFIX + ":turn";

	private final String encodedAction;

	public CTSMessage_Action(final String encodedAction)
	{
		this.encodedAction = encodedAction;
	}

	@Override
	public void dispatch(IClientNode from, ICTSMessageTarget target)
	{
		target.onAction(from, this);
	}

	public String getEncodedAction()
	{
		return this.encodedAction;
	}

	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public final static ICoder<CTSMessage_Action> CODER = new AbstractCoder<CTSMessage_Action>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public CTSMessage_Action decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			return new CTSMessage_Action(tokens[0]);
		}

		@Override
		public String encode(final CTSMessage_Action message)
		{
			return join(PREFIX, message.getEncodedAction());
		}
	};
}
