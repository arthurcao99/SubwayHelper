package Main;

import Control.InformationProcessing;
import Control.ShortestPath;
import Model.Graph;
import Model.Line;
import Model.Station;

import java.io.IOException;
import java.util.ArrayList;

public class Subway{
	
	public static void main(String[] args) throws Exception {
		switch (args[0]){
            case "-map":
                //-map subway.txt
                if(args.length==2){
                    InformationProcessing ii=new InformationProcessing();
                    ii.inputInformation(args[1]);
                    System.out.println("成功读取subway.txt文件");
        			ArrayList<Line> lines=new ArrayList<Line>();
        			lines=ii.inputInformation(args[1]);
        			for (int i=0;i<lines.size();i++){
        				System.out.println(lines.get(i).getLineName());
        				for (int j=0;j<lines.get(i).getStations().size();j++){
        					System.out.print(lines.get(i).getStations().get(j).getStationName()+" ");
        				}
        				System.out.println();
        			}
                }else{
                    System.out.println("验证参数格式！");
                }
                break;

            case "-a":
                //-a 1号线 -map subway.txt -o station.txt
                if(args.length==6){
                    InformationProcessing ii=new InformationProcessing();
                    ArrayList<Line> lines=new ArrayList<Line>();
                    lines=ii.inputInformation(args[3]);
                    if (ii.isLineExisted(args[1], ii.generateMap(ii.getLines()))==true){
                    	ii.showLine(args[1],ii.generateMap(ii.getLines()),args[5]);
                    }
                    else {
                    	throw new Exception("线路不存在");
                    }
                    System.out.println();
                    System.out.println("已将结果写入station.txt文件");
                }else{

                    System.out.println("验证参数格式！");
                }
                break;
            case "-b":
                //-b 洪湖里 复兴路 -map subway.txt -o routine.txt

                if(args.length==7){
                    ArrayList<Line> lines=new ArrayList<Line>();
                    InformationProcessing ii=new InformationProcessing();
                    lines=ii.inputInformation(args[4]);
                    Graph g=ii.generateGraph(ii.getAllStations());
                    if (args[1].equals(args[2])){
                    	throw new Exception("请输入两个不同站点");
                    }
                   
                    if (ii.Search(args[1], ii.getAllStations())==null){
                    	throw new Exception("站点1不存在，请重新输入");
                    }
                    if (ii.Search(args[2], ii.getAllStations())==null){
                    	throw new Exception("站点2不存在，请重新输入");
                    }
                    ShortestPath sp=new ShortestPath(ii.Search(args[1], ii.getAllStations()),g,lines);
                    ArrayList<Station> path=new ArrayList<>();
                    path=(ArrayList<Station>)sp.path(ii.Search(args[2],ii.getAllStations()));
                    sp.showShortestPath(path,args[6]);

                    System.out.println("已将结果写入routine. txt文件");
                }else{
                    System.out.println("验证参数格式！");
                }
                break;
            default:
            	throw new Exception("参数输入错误");
        }

	}



}
