package cn.edu.usst.model;

public class NewsListRequest {
    String requestType;
    String categoryName;
    Integer requestPageNumber;

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
}
