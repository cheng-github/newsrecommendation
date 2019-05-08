package cn.edu.usst.algorithm.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecomResult {
    public String userUUID;
    public Set<String> articleIdList = new HashSet<>();

    public RecomResult(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public Set<String> getArticleIdList() {
        return articleIdList;
    }

    public void setArticleIdList(Set<String> articleIdList) {
        this.articleIdList = articleIdList;
    }
}
