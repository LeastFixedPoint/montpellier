package info.reflectionsofmind.connexion.platform.client.game;

public interface IClientGame<TII extends IInitInfo<?>, TGC extends IChange<?>, TGR extends IResult<?>>
{
	void start(TII initInfo);
	void onChange(TGC change);
	boolean isFinished();
	TGR getResult();
}
