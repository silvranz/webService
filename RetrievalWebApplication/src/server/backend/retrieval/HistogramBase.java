package server.backend.retrieval;

public class HistogramBase {

	private int objectID;
	private double histogramValue[];
	private int clusterClass;
	private int lengthBinary = 16;

	public HistogramBase(){
		objectID = -1;
		histogramValue = new double[3*16];
		clusterClass = -1;
	} 
	public HistogramBase(int objectID,double histogramValue[],int clusterClass){
		this.objectID = objectID;
		this.histogramValue = histogramValue;
		this.clusterClass = clusterClass;
	} 
	
	public HistogramBase(int objectID,double histogramValue[]){
		this.objectID = objectID;
		this.histogramValue = histogramValue;
		this.clusterClass = -1;
	}
	public HistogramBase(String tempString){
		String[] split1 = tempString.split(";");
		this.clusterClass = -1;//Integer.parseInt(split1[1],2);
		String[] split2 = split1[0].split(":");
		this.objectID = Integer.parseInt(split2[0]);
		this.histogramValue = new double[3*16];
		String[] listValue = split2[1].split(",");
		for(int i=0;i<listValue.length;i++)
			this.histogramValue[i] = Double.parseDouble(listValue[i]);
	} 
	public HistogramBase(double[] h,int objectID,int cluster){
		this.objectID = objectID;
		this.histogramValue = h;
		this.clusterClass = cluster;
	} 
	
	public double[] getHistogramValue() {
		return histogramValue;
	}
	public void setHistogramValue(double histogramValue[]) {
		this.histogramValue = histogramValue;
	}
	public int getObjectID() {
		return objectID;
	}
	public void setObjectID(int objectID) {
		this.objectID = objectID;
	}
	public int getClusterClass() {
		return clusterClass;
	}
	public void setClusterClass(int clusterClass) {
		this.clusterClass = clusterClass;
	}
	
	public byte[] convertToByte(String binary)
	{
		byte[] temp = new byte[lengthBinary/8];
		char[] listChar = binary.toCharArray();
		Integer tempInt = 0;
		for(int i=0;i<listChar.length;i++)
		{
			tempInt = (int)listChar[i];
			temp[i] = tempInt.byteValue();
		}
		return temp;
	}
}
