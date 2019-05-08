package cn.edu.usst.algorithm.item;

public class CandidateNews implements Comparable<CandidateNews>{

    public String newsId;
    public int weight;

    public CandidateNews(String newsId, int weight) {
        this.newsId = newsId;
        this.weight = weight;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(CandidateNews o) {
        return o.getWeight() - this.getWeight();
    }

    @Override
    public boolean equals(Object obj) {
        CandidateNews candidateNews = (CandidateNews)obj;
        return this.newsId.equals(candidateNews.getNewsId());
    }
}
