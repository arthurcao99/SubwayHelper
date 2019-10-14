package Model;

import java.util.LinkedList;

public class Station {
	private String stationName;
	private LinkedList<Station> connectedStation;

	public Boolean getChangingStation() {
		return isChangingStation;
	}

	public void setChangingStation(Boolean changingStation) {
		isChangingStation = changingStation;
	}

	private Boolean isChangingStation=false;
	
	public void addConnection(Station station){
		if (station!=null){
			this.connectedStation.add(station);
		}
		
	}
	
	public LinkedList<Station> getConnectedStation() {
		return connectedStation;
	}
	
	public void setConnectedStation(LinkedList<Station> connectedStation) {
		this.connectedStation = connectedStation;
	}

	public Station() {
		// TODO Auto-generated constructor stub
		connectedStation=new LinkedList<Station>();
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	

}
