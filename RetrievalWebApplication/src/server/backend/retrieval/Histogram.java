package server.backend.retrieval;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Histogram {

	double histVal[] = new double[3*16];
	public double[] getHistVal() {
		return histVal;
	}

	public void setHistVal(double[] histVal) {
		this.histVal = histVal;
	}
}

