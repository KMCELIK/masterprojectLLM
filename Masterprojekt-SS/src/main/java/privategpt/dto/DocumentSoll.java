package privategpt.dto;

public class DocumentSoll {
	private String path;
	private String sollJson;
	private String art;

	public DocumentSoll(String path, String sollJson, String art) {
		this.path = path;
		this.sollJson = sollJson;
		this.art = art;
	}
	
	@Override
	public String toString() {
		return "DocumentSoll [path=" + path + ", sollJson=" + sollJson + ", art=" + art + "]";
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

	public String getArt() {
		return art;
	}

	public void setArt(String art) {
		this.art = art;
	}

}
