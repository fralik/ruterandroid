package com.neuron.trafikanten.views.route;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.neuron.trafikanten.dataProviders.IGenericProviderHandler;
import com.neuron.trafikanten.dataProviders.trafikanten.TrafikantenDevi;
import com.neuron.trafikanten.dataSets.DeviData;
import com.neuron.trafikanten.dataSets.RouteData;
import com.neuron.trafikanten.dataSets.RouteProposal;

public class RouteDeviLoader {
	private final static String TAG = "Trafikanten-RouteDeviLoader";
	private ArrayList<RouteProposal> routeProposalList;
	private TrafikantenDevi deviProvider = null;
	private Context context;
	IGenericProviderHandler<Void> handler;
	private HashMap<String, ArrayList<DeviData> > deviList;
	
	private String deviKey;

	
	public RouteDeviLoader(Context context, ArrayList<RouteProposal> routeProposalList, HashMap<String, ArrayList<DeviData> > deviList, IGenericProviderHandler<Void> handler) {
		this.routeProposalList = routeProposalList;
		this.context = context;
		this.handler = handler;
		this.deviList = deviList;
	}
	
	/*
	 * Load devi, returns false if all devi is loaded
	 */
	public boolean load() {
		for (RouteProposal routeProposal : routeProposalList) {
			for (RouteData routeData : routeProposal.travelStageList) {
				/*
				 * if tourId = 0 we're walking, no devi for that ;)
				 */
				if (!routeData.deviLoaded && routeData.tourID > 0) {
					/*
					 * TODO, come up with a better way of id'ing the different values, using a string for this is dumb.
					 */
					deviKey = "" + routeData.fromStation.stationId + "-" + routeData.line;
					/*
					 * if the deviList contains the key we've already asked.
					 */
					if (deviList.containsKey(deviKey)) {
						Log.i(TAG,"Found " + deviKey + " in cache");
						routeData.deviLoaded = true;
					} else {
						Log.i(TAG,"Loading route devi " + deviKey);
						loadDevi(routeData, routeData.fromStation.stationId, routeData.line);
						return true;
					}
				}
			}
		}
		Log.i(TAG,"Done loading route devi");
		return false;
	}
	
	private void loadDevi(final RouteData routeData, int stationId, String line) {
		deviProvider = new TrafikantenDevi(context, stationId, line, new IGenericProviderHandler<DeviData>() {
			private ArrayList<DeviData> list = new ArrayList<DeviData>();

    		@Override
    		public void onExtra(int what, Object obj) {
    			/* Class has no extra data */
    		}

			@Override
			public void onData(DeviData deviData) {
				list.add(deviData);
			}

			@Override
			public void onPostExecute(Exception exception) {
				if (exception == null) {
					routeData.deviLoaded = true;
					deviList.put(deviKey, list);
				}
				deviProvider = null;
				handler.onPostExecute(exception);
			}

			@Override
			public void onPreExecute() {}
    	});
	}
	
	public void kill() {
		if (deviProvider != null) {
			deviProvider.kill();
		}
		deviProvider = null;
	}
}
