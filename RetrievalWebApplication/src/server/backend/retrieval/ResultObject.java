package server.backend.retrieval;

import java.util.List;

public class ResultObject {
	private String objectName;
	private KnowledgeObject listKnowledge[];
	public ResultObject(){};
	public ResultObject(String objectName, KnowledgeObject listKnowledge[]){
		this.objectName = objectName;
		this.listKnowledge = listKnowledge;
	};
	
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public KnowledgeObject[] getListKnowledge() {
		return listKnowledge;
	}
	public void setListKnowledge(KnowledgeObject listKnowledge[]) {
		this.listKnowledge = listKnowledge;
	}
	public void setListKnowledge(List<KnowledgeObject> listKnowledge) {
		this.listKnowledge = new KnowledgeObject[listKnowledge.size()];
		for(int i=0;i<this.listKnowledge.length;i++)
		{
			this.listKnowledge[i] = listKnowledge.get(i);
		}
	}
}
