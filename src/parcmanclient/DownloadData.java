package parcmanclient;

import database.beans.ShareBean;

public class DownloadData
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
