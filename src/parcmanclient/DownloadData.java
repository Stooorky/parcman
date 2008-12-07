package parcmanclient;

import database.beans.ShareBean;
import java.io.Serializable;

/**
 * Contenitore per la trasmissione dei dati relativi al file che si vuole scaricare.
 *
 * @author Parcman Tm
 */
public class DownloadData implements Serializable
{
	private ShareBean bean;
	private RemoteParcmanClientUser rclient;

	public DownloadData(RemoteParcmanClientUser rclient, ShareBean bean)
	{
		this.bean = bean;
		this.rclient = rclient;
	}

	public ShareBean getShareBean()
	{
		return this.bean;
	}

	public RemoteParcmanClientUser getStub()
	{
		return this.rclient;
	}
}
