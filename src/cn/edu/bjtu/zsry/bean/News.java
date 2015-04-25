package cn.edu.bjtu.zsry.bean;


public class News {
	private String title;
	private String date;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "News [title=" + title + ", date=" + date + "]";
	}

	public News() {
		super();
	}

}
