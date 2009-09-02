package info.reflecitonsofmind.connexion.platform.gui.event.stc;

import info.reflectionsofmind.connexion.util.convert.DecodingException;
import info.reflectionsofmind.connexion.util.convert.EncodingException;
import info.reflectionsofmind.connexion.util.convert.ICoder;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerToClientMessageCoder implements ICoder<IServerToClientMessage>
{
	@Override
	public boolean accepts(final String string)
	{
		return false;
	}
	
	@Override
	public IServerToClientMessage decode(final String string) throws DecodingException
	{
		try
		{
			final JSONObject json = new JSONObject(string);
			
			final String type = json.optString("type", null);
			
			if (type == null) //
				throw new DecodingException("Type of message not specified. Message contents: [" + string + "].");
			
			if (KickNotice.class.getSimpleName().equals(type)) //
				return new KickNotice();
			
			if (ParticipationAccepted.class.getSimpleName().equals(type)) //
				return new ParticipationAccepted();
			
			throw new DecodingException("Cannot decode message of type [" + json.getString("type")
					+ "]. Message contents: [" + string + "].");
		}
		catch (final JSONException e)
		{
			throw new DecodingException(e);
		}
	}
	
	@Override
	public String encode(final IServerToClientMessage message) throws EncodingException
	{
		try
		{
			final JSONObject json = new JSONObject().append("type", message.getClass().getSimpleName());
			
			if (message instanceof KickNotice) //
				return json.toString();
			
			if (message instanceof ParticipationAccepted) //
				return json.toString();
			
			throw new EncodingException("Unrecognized class [" + message.getClass() + "] of object [" + message + "]");
		}
		catch (final JSONException e)
		{
			throw new EncodingException(e);
		}
	}
}
