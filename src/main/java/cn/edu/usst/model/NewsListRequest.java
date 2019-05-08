package cn.edu.usst.model;

public class NewsListRequest {
    String requestType;
    String categoryName;
    Integer requestPageNumber;
    Integer lastDays;
    Integer minViews;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Integer getRequestPageNumber() {
        return requestPageNumber;
    }

    public void setRequestPageNumber(Integer requestPageNumber) {
        this.requestPageNumber = requestPageNumber;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getLastDays() {
        return lastDays;
    }

    public void setLastDays(Integer lastDays) {
        this.lastDays = lastDays;
    }

    public Integer getMinViews() {
        return minViews;
    }

    public void setMinViews(Integer minViews) {
        this.minViews = minViews;
    }
}
