import java.util.ArrayList;
import java.util.List;

public class RecomResult {
    public String userUUID;
    public List<News> articleIdList = new ArrayList<>();

    public RecomResult(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public List<News> getArticleIdList() {
        return articleIdList;
    }

    public void setArticleIdList(List<News> articleIdList) {
        this.articleIdList = articleIdList;
    }
}
