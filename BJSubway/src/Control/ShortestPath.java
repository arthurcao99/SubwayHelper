package Control;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import Model.Graph;
import Model.Line;
import Model.Station;

public class ShortestPath {
	private Graph g;
	private ArrayList<Line> lines=new ArrayList<Line>();
	private Map<Station,Boolean> visited=new HashMap<>();
	private Map<Station,Station> pre=new HashMap<>();
	private Station startStation;
	
	public ShortestPath(Station startStation, Graph graph, ArrayList<Line> lines){
		this.g=graph;
		this.startStation=startStation;
		this.lines=lines;
		for(int i = 0; i < g.getAll().size(); i ++) {
			pre.put(g.getAll().get(i), g.getAll().get(i));
			visited.put(g.getAll().get(i),false);
        }
		bfs(startStation);
	}
	public void bfs(Station s){
		Queue<Station> queue=new LinkedList<>();
		queue.add(s);
		pre.put(s, s);
		visited.put(s, true);
		while(!queue.isEmpty()){
			Station v=queue.remove();
			for (Station w: g.getAdj().get(v)) {
				if (!visited.get(w)) {
					queue.add(w);
					visited.put(w, true);
					pre.put(w, v);
				}
			}
		}
		
	}
	
	public Iterable<Station> path(Station endStation){
		ArrayList<Station> res = new ArrayList<Station>();
		Station cur=endStation;
		while (cur!=startStation){
			res.add(cur);
			cur=pre.get(cur);
		}
		res.add(startStation);
		Collections.reverse(res);;
		return res;
	}
	public ArrayList<Line> inLine(Station station){
		ArrayList<Line> inLine=new ArrayList<>();
		for (int i=0;i<lines.size();i++){
			if (lines.get(i).getStations().contains(station)){
				inLine.add(lines.get(i));
			}
		}
		return inLine;
	}
	public String isInTheSameLine(Station s1,Station s2){
		ArrayList<Line> s1Line=this.inLine(s1);
		ArrayList<Line> s2Line=this.inLine(s2);
		for (int i=0;i<s1Line.size();i++){
			if (s2Line.contains(s1Line.get(i))){
				return s1Line.get(i).getLineName();
			}
		}
		return null;
	}
	
	public void showShortestPath(ArrayList<Station> path, String filepath) throws IOException {
		File f=new File(filepath);
		FileOutputStream fos1=new FileOutputStream(f);
		OutputStreamWriter dos1=new OutputStreamWriter(fos1);



		System.out.println("总站点数："+path.size()+"站");
		dos1.write("总站点数："+path.size()+"站"+"\n");
		System.out.println();
		System.out.println("具体线路：");
		dos1.write("具体线路："+"\n");
		Station start=path.get(0);
		Station second=path.get(1);
		
		String lineName="";
		if (start.getChangingStation()==false&&second.getChangingStation()==false){
			lineName=this.inLine(start).get(0).getLineName();
		}
		else if (start.getChangingStation()==true&&second.getChangingStation()==false){
			lineName=this.inLine(second).get(0).getLineName();
		}
		else if(start.getChangingStation()==true&&second.getChangingStation()==true){
			lineName=this.isInTheSameLine(start,second);
		}
		else{
			lineName=this.inLine(start).get(0).getLineName();
		}
		System.out.println("--->乘坐："+lineName);
		dos1.write("--->乘坐："+lineName+"\n");
		System.out.println(start.getStationName());
		dos1.write(start.getStationName()+"\n");
		for (int i=1;i<path.size();i++){
			Station nowStation=path.get(i);
			String curLineName="";
			if (i<path.size()-1){
				Station nextStation=path.get(i+1);
				if (nowStation.getChangingStation()==false&&nextStation.getChangingStation()==false){
					curLineName=this.inLine(nowStation).get(0).getLineName();
				}
				else if (nowStation.getChangingStation()==true&&nextStation.getChangingStation()==false){
					curLineName=this.inLine(nowStation).get(0).getLineName();
				}
				else if(nowStation.getChangingStation()==true&&nextStation.getChangingStation()==true){
					curLineName=this.isInTheSameLine(nowStation,nextStation);
				}
				else{
					curLineName=this.inLine(nowStation).get(0).getLineName();
				}
				System.out.println(nowStation.getStationName());
				dos1.write(nowStation.getStationName()+"\n");
				if (!lineName.equals(curLineName)){
					lineName=curLineName;
					System.out.println("--->换乘："+lineName);
					dos1.write("--->换乘："+lineName+"\n");

				}

			}
			else{
				Station preStation=path.get(i-1);
				if (preStation.getChangingStation()==true&&nowStation.getChangingStation()==false){
					if (!this.inLine(nowStation).get(0).getLineName().equals(lineName)){
						System.out.println("--->换乘："+this.inLine(nowStation).get(0).getLineName());
						dos1.write("--->换乘："+this.inLine(nowStation).get(0).getLineName()+"\n");
					}
					
				}

				System.out.println(nowStation.getStationName());
				dos1.write(nowStation.getStationName()+"\n");
			}


		}
		dos1.close();

	}

	public void showLine(String lineName,Map<String, Line> lineMap,String filepath) throws IOException {
		File f=new File(filepath);
		FileOutputStream fos1=new FileOutputStream(f);
		OutputStreamWriter dos1=new OutputStreamWriter(fos1);
		dos1.write(lineName+": ");
		dos1.write(lineMap.get(lineName).toString());
		dos1.close();
		System.out.print(lineName+": "+lineMap.get(lineName).toString());

	}


	public static void main(String[] args){
		ArrayList<Line> lines=new ArrayList<Line>();
		InformationProcessing ii=new InformationProcessing();
		try {
			lines=ii.inputInformation("subway.txt");
			Graph g=ii.generateGraph(ii.getAllStations());
			
			ShortestPath sp=new ShortestPath(ii.Search("车公庄西", ii.getAllStations()),g,lines);
			ArrayList<Station> path=new ArrayList<>();
			path=(ArrayList<Station>)sp.path(ii.Search("前门",ii.getAllStations()));
			//sp.showShortestPath(path,"station.txt");
			sp.showLine("1号线",ii.generateMap(ii.getLines()),"station.txt");
//			for (int i=0;i<path.size();i++){
//				System.out.print(path.get(i).getStationName()+" ");
//			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    }
}
