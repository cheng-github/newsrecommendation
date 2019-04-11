package languagetest;

import java.util.ArrayList;

public class UserPair {

    public static ArrayList<UserPair> userPairs = new ArrayList<>();
    public int firstMatrixId;
    public int secondMatrixId;


    public static void addUserPair(int i, int j){
        UserPair userPair = new UserPair(i, j);
        if (!userPairs.contains(userPair))
            userPairs.add(userPair);
    }

    private UserPair(int i, int j){
        this.firstMatrixId = i;
        this.secondMatrixId = j;
    }

    @Override
    public boolean equals(Object obj) {
        UserPair anotherUserPair = (UserPair)obj;
        if((this.getFirstMatrixId() == anotherUserPair.getFirstMatrixId())
                && (this.getSecondMatrixId() == anotherUserPair.getSecondMatrixId()))
            return true;
        else
            return false;
    }

    public int getFirstMatrixId() {
        return firstMatrixId;
    }

    public void setFirstMatrixId(int firstMatrixId) {
        this.firstMatrixId = firstMatrixId;
    }

    public int getSecondMatrixId() {
        return secondMatrixId;
    }

    public void setSecondMatrixId(int secondMatrixId) {
        this.secondMatrixId = secondMatrixId;
    }
}
