package usersimi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    public static int userConstructSerialNumber = 0;
    public static Map<String, User> userIdORM = new HashMap();
    // 可以使用ArrayList建立用户uuid和matrixIndex的映射关系
    public static List<String> uuidMatrix = new ArrayList<>();
    int matrixNumber; // 用于标识矩阵中用户的编号
    String userId;
    List<News> readList = new ArrayList(); // 我们建立用户持有物品的这一一个对应关系
    List<SimilarUser> similarList = new ArrayList<>();


    public static void addUserInfo(String uuid, News news){
        if (!userIdORM.containsKey(uuid)){
            User us = new User(uuid);
            us.getReadList().add(news);
            userIdORM.put(uuid, us);
        }
        else {
            userIdORM.get(uuid).getReadList().add(news);
        }
    }

    public User(String uuid) {
        this.userId = uuid;
        uuidMatrix.add(userConstructSerialNumber, uuid);
        matrixNumber = userConstructSerialNumber++;
    }


    public int getMatrixNumber() {
        return matrixNumber;
    }

    public void setMatrixNumber(int matrixNumber) {
        this.matrixNumber = matrixNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<News> getReadList() {
        return readList;
    }

    public void setReadList(List<News> readList) {
        this.readList = readList;
    }

    public List<SimilarUser> getSimilarList() {
        return similarList;
    }

    public void setSimilarList(List<SimilarUser> similarList) {
        this.similarList = similarList;
    }
}
