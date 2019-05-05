package cn.edu.usst.algorithm.item;

public class News implements Comparable<News> {

    public String newsId;
    public long timeStamp;

    public News(String newsId, long timeStamp) {
        this.newsId = newsId;
        this.timeStamp = timeStamp;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int compareTo(News o) {
        if (this.getTimeStamp() - o.getTimeStamp() < 0)
            return -1;
        else
            return 1;
    }

    @Override
    public boolean equals(Object obj) {
        News item = (News)obj;
        return item.getNewsId().equals(this.getNewsId());
    }
}
