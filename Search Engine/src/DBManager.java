import java.sql.*;

public class DBManager {
    static final private String username = "root";
    static final private String password = "1234";
    static final private String dbName = "SearchEngine";
    static final private String dbName2 = "content";
    static final private String host = "localhost";
    static final private String port = "3306";
    static final private String connection_url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
    static final private String connection_url2 = "jdbc:mysql://" + host + ":" + port + "/" + dbName2;
    static private Connection conn;
    static private Connection connection;
    static public Connection getDBConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(connection_url, username, password);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        return conn;
    }
    static public Connection getDBConnectionC() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(connection_url2, username, password);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        return connection;
    }
    public static void close() throws SQLException {
        if (conn != null) {
            conn.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
    // This main is just for example
    public static void main(String[] args) throws SQLException {
        // Get connection instance
        Connection conn = getDBConnection();
        assert conn != null;

        // INSERT STATEMENTS
        conn.createStatement().executeUpdate("INSERT INTO `crawledurls`(`URL`, `Title`, `CrawlDate`," +
                " `Paragraph`) VALUES (\"google.com\",\"Google\",CURRENT_DATE,\"Search Google\");");

        // OR bind parameters
        String sql = "INSERT INTO `crawledurls`(`URL`, `Title`, `CrawlDate`," +
                " `Paragraph`) VALUES (?,?,?,?);";
        PreparedStatement prep = conn.prepareStatement(sql);
        prep.setString(1, "Facebook.com");
        prep.setString(2, "Facebook");
        // Current Date
        prep.setDate(3, new Date(System.currentTimeMillis()));
        prep.setString(4, "Connecting people");
        prep.executeUpdate();

        // UPDATE
        conn.createStatement().executeUpdate("UPDATE `crawledurls` SET `URL`='yahoo.com'" +
                " WHERE Title = 'Google';");

        // DELETE
        conn.createStatement().executeUpdate("DELETE FROM `crawledurls` WHERE Title = 'Facebook';");

        // SELECT STATEMENTS
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM crawledurls");
        while (result.next()) {
            System.out.print(result.getString("id")+" ");
            System.out.print(result.getString("title")+" ");
            System.out.println(result.getString("URL")+" ");
        }
        result.close();

        // DELETE
        conn.createStatement().executeUpdate("DELETE FROM `crawledurls` WHERE Title = 'Google';");

        conn.close();

    }
}