package info.reflectionsofmind.connexion.platform.core.common.message.stc;

import info.reflectionsofmind.connexion.util.convert.ICoder;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class STCMessageDecoder
{
	private final static List<ICoder<?>> CODERS = // 
	ImmutableList.<ICoder<?>> of( //
			STCMessage_ConnectionAccepted.CODER, //
			STCMessage_ParticipantConnected.CODER, //
			STCMessage_ParticipantStateChanged.CODER, // 
			STCMessage_ParticipantDisconnected.CODER, //
			STCMessage_Chat.CODER, //
			STCMessage_GameStarted.CODER, //
			STCMessage_Change.CODER);

	public static AbstractSTCMessage decode(String string)
	{
		for (ICoder<?> decoder : CODERS)
		{
			if (decoder.accepts(string))
			{
				return (AbstractSTCMessage) decoder.decode(string);
			}
		}

		throw new RuntimeException("Coder not found for [" + string + "] to decode it as a CtS event.");
	}
}
