/*
 * Parcman Project, the Italian Arcade Network
 * Copyright (C) 2008-2009 Parcman Tm (Marchi Sirio, Marcantoni Francesco, Emanuele Dona')
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/.
 *
 * E-Mail:	ramyel [at] gmail [dot] com
 * 			sirio.marchi [at] gmail [dot] com
 * 			emanuele.dona [at] gmail [dot] com
 */

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
