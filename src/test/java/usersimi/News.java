package usersimi;

import java.util.HashMap;

public class News {
    public static int itemSerialNumber = 0;
    public static HashMap<String, News> docIdORM = new HashMap();
    String document_id;
    int matrixNumber; // 标识矩阵中News的编号

    /**
     * 如果不存在对应的News对象才添加新闻对象，否则不添加仅返回当前存在的
     * @param documentId
     */
    public static News addNewsInfo(String documentId){
        if (!docIdORM.containsKey(documentId)){
            News obj = new News(documentId);
            docIdORM.put(documentId, obj);
            return obj;
        }
        else {
            return docIdORM.get(documentId);
        }
    }

    private News(String documentId) {
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
