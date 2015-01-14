package server.backend.retrieval;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/retrieval")
public class Retrieval {
	
	private String featureDB = new File(System.getProperty("user.dir")).getParentFile().getParent() + "/Android/FeatureDatabase/histogram.txt";
	private Connection dbConn;
	
	@GET
	@Path("/sample")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultObject responseJSON() {
		return new ResultObject();
	}
	
	public Retrieval(){
		String dbURL = "jdbc:mysql://localhost/retrieval";
		String username = "root";
		String password = "";

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			dbConn = DriverManager.getConnection(dbURL, username, password);
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	}
	
	@POST
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public ResultObject responseJSON(Histogram hist) {
		double[] postData = hist.histVal;
		String tempLine = "";
		/* Load Feature Database */		
		List<HistogramBase> cluster = new ArrayList<HistogramBase>();
		try {
			FileReader fr = new FileReader(featureDB);
			BufferedReader br = new BufferedReader(fr);
			while((tempLine = br.readLine())!= null)
			{
				cluster.add(new HistogramBase(tempLine));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Load Feature Database END */
		
		int objectID = nearestNeighbor(cluster,postData);
		ResultObject queryResult = new ResultObject();
		List<KnowledgeObject> listK = new ArrayList<KnowledgeObject>();
		String sql = "SELECT b.objectName,c.knowledgeName,a.knowledgeDetail " +
						"FROM trknowledgeobject a " +
						"JOIN msobject b ON a.objectID = b.objectID AND b.objectID="+objectID+" " +
						"JOIN msknowledge c ON a.knowledgeID = c.knowledgeID";		 
		Statement statement;		
		try {
			statement = dbConn.prepareStatement(sql);
			ResultSet result = statement.executeQuery(sql);
			if(result.next())
			{
				queryResult.setObjectName(result.getString(1));
				listK.add(new KnowledgeObject(result.getString(2), result.getString(3)));
			}
			while (result.next()){
				listK.add(new KnowledgeObject(result.getString(2), result.getString(3)));
			}
			queryResult.setListKnowledge(listK);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    return queryResult;
	}
	public class minDis {
	    public int id = -1;
	    public double distance;
	}
	
	public int nearestNeighbor(List<HistogramBase> listData,double[] queryHist)
	{
		double histVal[] = new double[queryHist.length];
		double minDistance = -1;
		double flag = -1;
		minDis flagList[] = new minDis[10];
		for(int i=0;i<flagList.length;i++)
		{
			flagList[i] = new minDis();
		}
		int flagID = -1;
		for(HistogramBase h:listData)
		{
			histVal = h.getHistogramValue();
			flag = vcad(queryHist, histVal);
			//flag = euclideanDistance(queryHist, histVal);
			//flag = manhattan(queryHist, histVal);
			//flag = modeuclideanDistance(queryHist, histVal);
			//System.out.println(flag+":"+h.getObjectID()+" ; "+minDistance+":"+flagID);
			//flagList = checkListMin(h.getObjectID(),flag,flagList);
			if((minDistance == -1 || flag < minDistance)&& flag != Double.NaN )
			{
				flagID = h.getObjectID();
				minDistance = flag;
			}
		}
		/*
		for(minDis x:flagList)
		{
			System.out.println(x.id+":"+x.distance);
		}
		*/
		return flagID;
	}
	/*
	public minDis[] checkListMin(int a,double dis,minDis[] b)
	{
		int x = 0;
		for(int i=0;i<b.length;i++)
		{
			if(b[i].id == -1){
				b[i].id = a;
				b[i].distance = dis;
			}
			else if(b[i].distance > dis)
			{
				x = i;
				while(b[i].distance > dis && i<b.length-1)
				{
					i++;
				}
				if(b[i].distance > dis){
					b[i].id = a;
					b[i].distance = dis;
				}
				else if(i==b.length){
					b[x].id = a;
					b[x].distance = dis;
				}
				else{
					b[i-1].id = a;
					b[i-1].distance = dis;
				}
				
			}
		}
		return b;
	};
	*/
	public double euclideanDistance(double[] a,double[] b){

		double euclideanDistance = 0;
		for(int i=0;i<a.length;i++)
		{
			euclideanDistance += Math.pow(a[i]-b[i], 2);
		}
		
		euclideanDistance = (double) Math.sqrt(euclideanDistance);
		return euclideanDistance;
	}
	
	public double modeuclideanDistance(double[] a,double[] b){

		double euclideanDistance = 0;
		for(int i=0;i<a.length;i++)
		{
			euclideanDistance += Math.pow(a[i]-b[i], 2);
		}
		
		euclideanDistance = (double) Math.sqrt(euclideanDistance)/(2*3);
		euclideanDistance = 1-euclideanDistance;
		return euclideanDistance;
	}

	public double vcad(double[] a,double[] b){

		double vector = 0;
		double sumab = 0;
		double sumas = 0;
		double sumbs = 0;
		for(int i=0;i<a.length;i++)
		{
			sumab += a[i]*b[i];
			sumas += Math.pow(a[i], 2);
			sumbs += Math.pow(b[i], 2);
		}
		sumas = Math.sqrt(sumas);
		sumas *= Math.sqrt(sumbs);
		vector = sumab/sumas;
		//return vector;
		double degree = Math.acos(vector)* (180/Math.PI);
		degree = Math.pow(degree, 2);
		degree = Math.sqrt(degree);
		return degree;
	}
	
	public double manhattan(double[] a,double[] b)
	{
		double mh = 0;
		double tempDiff = 0;
		for(int i=0;i<a.length;i++)
		{
			tempDiff = Math.sqrt(Math.pow(a[i]-b[i], 2));
			mh += tempDiff;
		}
		return mh;
	}
}
