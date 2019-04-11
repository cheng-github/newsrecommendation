package usersimi;

public class SimilarUser implements Comparable<SimilarUser> {

    public String uuid;
    public float similarValue;

    public SimilarUser(String uuid, float similarValue) {
        this.uuid = uuid;
        this.similarValue = similarValue;
    }

    @Override
    public int compareTo(SimilarUser o) {
        if (this.similarValue - o.similarValue < 0)
            return 1;
        else
            return -1;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public float getSimilarValue() {
        return similarValue;
    }

    public void setSimilarValue(float similarValue) {
        this.similarValue = similarValue;
    }
}
