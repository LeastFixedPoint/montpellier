package info.reflectionsofmind.connexion.fortress.core.client;

import info.reflectionsofmind.connexion.fortress.core.common.StartInfo;
import info.reflectionsofmind.connexion.fortress.core.common.action.ActionEncoder;
import info.reflectionsofmind.connexion.fortress.core.common.change.AbstractChange;
import info.reflectionsofmind.connexion.fortress.core.common.change.ChangeDecoder;
import info.reflectionsofmind.connexion.platform.core.client.IClientCoder;
import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;
import info.reflectionsofmind.connexion.platform.core.common.game.IGameConfig;
import info.reflectionsofmind.connexion.platform.core.common.game.IStartInfo;
import info.reflectionsofmind.connexion.util.convert.IDecoder;
import info.reflectionsofmind.connexion.util.convert.IEncoder;

public final class ClientCoder implements IClientCoder
{
	private final IDecoder<GameConfig> gameConfigDecoder;
	private final IDecoder<AbstractChange> changeDecoder;
	private final IDecoder<StartInfo> startInfoDecoder;
	private final IEncoder<IAction> actionEncoder; 

	public ClientCoder(final ClientGame game)
	{
		this.gameConfigDecoder = new GameConfig.Decoder();
		this.changeDecoder = new ChangeDecoder(game);
		this.startInfoDecoder = new StartInfo.Decoder(game);
		this.actionEncoder = new ActionEncoder(game);
	}

	@Override
	public IChange decodeChange(final String string)
	{
		return this.changeDecoder.decode(string);
	}

	@Override
	public IGameConfig decodeGameConfig(final String string)
	{
		return this.gameConfigDecoder.decode(string);
	}

	@Override
	public IStartInfo decodeStartInfo(final String string)
	{
		return this.startInfoDecoder.decode(string);
	}

	@Override
	public String encodeAction(final IAction action)
	{
		return this.actionEncoder.encode(action);
	}
}
