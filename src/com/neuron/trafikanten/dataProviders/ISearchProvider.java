/**
 *     Copyright (C) 2009 Anders Aagaard <aagaande@gmail.com>
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.neuron.trafikanten.dataProviders;

import android.os.Handler;

import com.neuron.trafikanten.dataSets.SearchStationData;


/*
 * Search Provider
 */
public interface ISearchProvider {
	public void Search(String query);
	public void Search(double latitude, double longitude);
	public void Stop();
	
	abstract class SearchProviderHandler extends Handler {
		public abstract void onStarted();
	    public abstract void onData(SearchStationData searchData);
	    public abstract void onError(Exception e);
	    public abstract void onFinished();
	}
}