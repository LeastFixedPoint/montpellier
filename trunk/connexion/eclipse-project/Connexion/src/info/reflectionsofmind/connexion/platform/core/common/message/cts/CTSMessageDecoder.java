package info.reflectionsofmind.connexion.platform.core.common.message.cts;

import info.reflectionsofmind.connexion.util.convert.ICoder;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class CTSMessageDecoder
{
	private final static List<ICoder<?>> CODERS = // 
	ImmutableList.<ICoder<?>> of( //
			CTSMessage_ConnectionRequest.CODER, // 
			CTSMessage_DisconnectNotice.CODER, //
			CTSMessage_Chat.CODER, //
			CTSMessage_Action.CODER);

	public static AbstractCTSMessage decode(String string)
	{
		for (ICoder<?> coder : CODERS)
		{
			if (coder.accepts(string))
			{
				return (AbstractCTSMessage) coder.decode(string);
			}
		}
		
		throw new RuntimeException("Cannot decode string [" + string + "] as a client-to-server event.");
	}
}
