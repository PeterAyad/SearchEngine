import java.sql.*;
import java.util.*;
import java.util.Map.Entry;
public class IndexerDB {
    static private Connection connection;
    static private Connection contentConnection;
    public static ArrayList<String>preview;
    public static void open() {
        connection=DBManager.getDBConnection();
        contentConnection=DBManager.getDBConnectionC();
    }
    public static void close() throws SQLException {
        DBManager.close();
    }
    public static void updateURL(String URL) throws SQLException {
        String sql = "UPDATE urls set indexed = true WHERE URL = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, URL);
        ps.executeUpdate();
        sql = "UPDATE urls set titles = ? WHERE titles=null";
        ps = connection.prepareStatement(sql);
        ps.setString(1, URL);
        ps.executeUpdate();
    }
    public static String getNonIndexedURL() throws SQLException {
        String sql = "SELECT URL FROM urls WHERE indexed IS false and crawldate is not null and crawldate > '2001-01-01' LIMIT 1";
        ResultSet result = connection.createStatement().executeQuery(sql);
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }
    private static int URLid(String URL) throws SQLException {
        String sql = "SELECT id FROM urls WHERE URL =  ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,URL);
        ResultSet result =  ps.executeQuery();
        if (result.next()) {
            return result.getInt(1);
        }
        return -1;
    }
    public static void indexWords(LinkedHashMap<String, Double> TF,String URL) throws SQLException {
        int URLid=URLid(URL);
        String sql = "INSERT ignore INTO Words(stem,TF,preview,URLID) VALUES "+helper(TF.size());
        PreparedStatement ps = connection.prepareStatement(sql);
        int i=0;
        int counter=1;
        for (Entry<String, Double> entry : TF.entrySet()) {
            ps.setString(counter, entry.getKey());
            ps.setDouble(counter+1, entry.getValue());
            ps.setString(counter+2,preview.get(i));
            ps.setInt(counter+3, URLid);
            counter+=4;
            i++;
        }
        ps.executeUpdate();
    }
    public static String helper(int size){
        StringBuilder s=new StringBuilder();
        s.append("(?,?,?,?)");
        for (int i=0;i<size-1;i++){
            s.append(",(?,?,?,?)");
        }
        return s.toString();
    }
    public static void clean() throws SQLException {
        connection.createStatement().executeUpdate("delete FROM words where char_length(stem)=1;");
        connection.createStatement().executeUpdate("delete FROM urls where indexed = 0");
    }
    public static void startOver() throws SQLException {
        connection.createStatement().executeUpdate("update urls set indexed=0");
        connection.createStatement().executeUpdate("delete from words");
    }
    public static String getDocument(String URL) throws SQLException {
        StringBuilder s =new StringBuilder();
        String sql = "SELECT content FROM urlcontent WHERE URL =  ?";
        PreparedStatement ps = contentConnection.prepareStatement(sql);
        ps.setString(1,URL);
        ResultSet result =  ps.executeQuery();
        if (result.next()) {
           s.append(result.getString(1));
        }
        return s.toString();
    }
    public static LinkedHashMap<String,Double> calcTF(ArrayList<String> listOfWords,ArrayList<String> listOfWordsP ,int countOfWords){
        preview=new ArrayList<>();
        LinkedHashMap<String,Double> TF = new LinkedHashMap<>();
        if(countOfWords!=0) {
            for (String word : listOfWords) {
                String sword=Extract.stemS(word);
                StringBuilder s=new StringBuilder();
                if (TF.containsKey(sword)) {
                    TF.put(sword, TF.get(sword) + 1.0);
                }
                else {
                    TF.put(sword, 1.0);
                    int count=listOfWordsP.indexOf(word);
                    int i;
                    if(count>8){
                        i=count-8;
                    }
                    else{
                        i=0;
                    }
                    while(i<listOfWordsP.size()&&i<count+7){
                        s.append(listOfWordsP.get(i));
                        s.append(" ");
                        i++;
                    }
                    preview.add(s.toString());
                }

            }
            //Normalize TF
            for (Map.Entry<String, Double> entry : TF.entrySet()) {
                entry.setValue(entry.getValue() / countOfWords);
            }
        }
        return TF;
    }

     /*
    public static void indexWords(HashMap<String, Double> TF,String URL) throws SQLException {
        int URLid=URLid(URL);
        for (Entry<String, Double> entry : TF.entrySet()) {
        String sql = "INSERT ignore INTO Words(word,TF,URLID) VALUES (?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, entry.getKey());
        ps.setDouble(2, entry.getValue());
        ps.setInt(3, URLid);
        ps.executeUpdate();
        }
    }
     */
}
