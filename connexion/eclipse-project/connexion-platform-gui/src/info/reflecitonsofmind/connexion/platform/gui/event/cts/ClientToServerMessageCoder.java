package info.reflecitonsofmind.connexion.platform.gui.event.cts;

import info.reflectionsofmind.connexion.util.convert.DecodingException;
import info.reflectionsofmind.connexion.util.convert.EncodingException;
import info.reflectionsofmind.connexion.util.convert.ICoder;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientToServerMessageCoder implements ICoder<IClientToServerMessage>
{
	@Override
	public boolean accepts(final String string)
	{
		return false;
	}
	
	@Override
	public IClientToServerMessage decode(final String string) throws DecodingException
	{
		try
		{
			final JSONObject json = new JSONObject(string);
			
			final String type = json.optString("type", null);
			
			if (type == null) //
				throw new DecodingException("Type of message not specified. Message contents: [" + string + "].");
			
			if (DisconnectNotice.class.getSimpleName().equals(type)) //
				return new DisconnectNotice();
			
			if (ParticipationRequest.class.getSimpleName().equals(type)) //
				return new ParticipationRequest(json.getString("name"));
			
			throw new DecodingException("Cannot decode message of type [" + json.getString("type")
					+ "]. Message contents: [" + string + "].");
		}
		catch (final JSONException e)
		{
			throw new DecodingException(e);
		}
	}
	
	@Override
	public String encode(final IClientToServerMessage message) throws EncodingException
	{
		try
		{
			final JSONObject json = new JSONObject().append("type", message.getClass().getSimpleName());
			
			if (message instanceof ParticipationRequest) //
				return json.append("name", ((ParticipationRequest)message).getName()).toString();
			
			if (message instanceof DisconnectNotice) //
				return json.toString();
			
			throw new EncodingException("Unrecognized class [" + message.getClass() + "] of object [" + message + "]");
		}
		catch (final JSONException e)
		{
			throw new EncodingException(e);
		}
	}
}
