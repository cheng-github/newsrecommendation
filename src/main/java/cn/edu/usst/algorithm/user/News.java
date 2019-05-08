package cn.edu.usst.algorithm.user;

public class News {
    public static int itemSerialNumber = 0; // 算法执行完后应该将其置为0
    String document_id;
    int matrixNumber; // 标识矩阵中News的编号

    public News(String documentId) {
        this.document_id = documentId;
        matrixNumber = itemSerialNumber++;
    }

    public String getDocument_id() {
        return document_id;
    }

    public void setDocument_id(String document_id) {
        this.document_id = document_id;
    }

    public int getMatrixNumber() {
        return matrixNumber;
    }

    public void setMatrixNumber(int matrixNumber) {
        this.matrixNumber = matrixNumber;
    }
}
