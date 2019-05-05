package cn.edu.usst.algorithm.item;

import java.util.ArrayList;

public class RecResult {

    public String userId;
    public ArrayList<CandidateNews> recList = new ArrayList<>();

    public RecResult(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<CandidateNews> getRecList() {
        return recList;
    }

    public void setRecList(ArrayList<CandidateNews> recList) {
        this.recList = recList;
    }
}
