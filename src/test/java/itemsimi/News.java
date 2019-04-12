package itemsimi;

public class News implements Comparable<News> {

    public String newsId;
    public int timeStamp;

    public News(String newsId, int timeStamp) {
        this.newsId = newsId;
        this.timeStamp = timeStamp;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int compareTo(News o) {
        return this.getTimeStamp() - o.getTimeStamp();
    }

    @Override
    public boolean equals(Object obj) {
        News item = (News)obj;
        return item.getNewsId().equals(this.getNewsId());
    }
}
