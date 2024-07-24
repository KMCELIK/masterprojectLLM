package privategpt.dto;

public class DocumentEvaluation {
	private String path;
	private String sollJson;
	private String istJson;
	private String reader;
	private int mistakes;
	private int correctness;
	private Order order;

	
	public DocumentEvaluation() {
		
	}
	
	public DocumentEvaluation(String path, String sollJson, String istJson, String reader, int mistakes,
			int correctness) {
		super();
		this.path = path;
		this.sollJson = sollJson;
		this.istJson = istJson;
		this.reader = reader;
		this.mistakes = mistakes;
		this.correctness = correctness;
	}

	@Override
	public String toString() {
		return "DocumentEvaluation [path=" + path + ", sollJson=" + sollJson + ", istJson=" + istJson + ", reader="
				+ reader + ", mistakes=" + mistakes + ", correctness=" + correctness + "]";
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSollJson() {
		return sollJson;
	}

	public void setSollJson(String sollJson) {
		this.sollJson = sollJson;
	}

	public String getIstJson() {
		return istJson;
	}

	public void setIstJson(String istJson) {
		this.istJson = istJson;
	}

	public String getReader() {
		return reader;
	}

	public void setReader(String reader) {
		this.reader = reader;
	}

	public int getMistakes() {
		return mistakes;
	}

	public void setMistakes(int mistakes) {
		this.mistakes = mistakes;
	}

	public int getCorrectness() {
		return correctness;
	}

	public void setCorrectness(int correctness) {
		this.correctness = correctness;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}