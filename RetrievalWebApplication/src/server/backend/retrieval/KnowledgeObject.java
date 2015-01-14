package server.backend.retrieval;

public class KnowledgeObject {
	private String knowledgeName;
	private String knowledgeDetail;
	public KnowledgeObject(){};
	public KnowledgeObject(String knowledgeName, String knowledgeDetail){
		this.knowledgeName = knowledgeName;
		this.knowledgeDetail = knowledgeDetail;
	};
	
	public String getKnowledgeName() {
		return knowledgeName;
	}
	public void setKnowledgeName(String knowledgeName) {
		this.knowledgeName = knowledgeName;
	}
	public String getKnowledgeDetail() {
		return knowledgeDetail;
	}
	public void setKnowledgeDetail(String knowledgeDetail) {
		this.knowledgeDetail = knowledgeDetail;
	}
}
