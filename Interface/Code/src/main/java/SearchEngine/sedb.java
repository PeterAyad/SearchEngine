package SearchEngine;
import java.sql.*;
import java.util.*;
public class sedb {
	
	public static Connection getConnection()
	{
		Connection con =null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			con= DriverManager.getConnection("jdbc:mysql://localhost:3306/sedb","root","");
			/*if(!con.isClosed())
				System.out.println("success connected");*/
		}
		catch(Exception e)
		{
			System.err.println("erorr" + e);
		}
			
		return con;	
	}
	public static int getRecordCount (String word)
	{
		int count=0;
		try {
			Connection con =getConnection();
			String myquery = "SELECT COUNT( DISTINCT URLID) FROM words WHERE stem='" + word + "'";
			PreparedStatement ps = con.prepareStatement(myquery);
			ResultSet results = ps.executeQuery();
			results.next();
			count = results.getInt(1);
			con.close();
		}
		catch(Exception e)
		{
			System.err.println("erorr" + e);
		}
		
		return count;
	}
	
	public static void setsearched(String word)
	{
		int count;

		try {
			Connection con =getConnection();
			String myquery="SELECT count from searched where word='" + word +  "'";
			PreparedStatement ps = con.prepareStatement(myquery);
			ResultSet results = ps.executeQuery();

			if(results.isBeforeFirst()) //update
			{
				results.next();
				count = results.getInt(1);
				count +=1;
				myquery="UPDATE searched SET count = " + String.valueOf(count) + " WHERE word='" + word + "'" ;
				PreparedStatement pps = con.prepareStatement(myquery);
				pps.executeUpdate();
			}
			else //insert
			{
				myquery="INSERT INTO searched ( word,count ) VALUES ('" + word + "',1)";
				PreparedStatement pps = con.prepareStatement(myquery);
				pps.execute();
			}
		
			ps.close();
			con.close();
		}
		catch(Exception e)
		{
			System.err.println("erorr" + e);
		}
	}
	
	public static String getStringOfSuggestions (String word)
	{
		String mylist="" ;
		try {
			Connection con =getConnection();
			String myquery = "Select word from searched WHERE word LIKE '" + word + "%' ORDER BY count DESC limit 0,5" ;
			PreparedStatement ps = con.prepareStatement(myquery);
			ResultSet results = ps.executeQuery();
			
			while(results.next())
			{
				mylist+=" <option value='";
				mylist+=(results.getString(1));
				mylist+="' />";
			}
			con.close();
		}
		catch(Exception e)
		{
			System.err.println("erorr" + e);
		}
		
		return mylist;
	}
	
	public static List<Records> getRecord (String word,int start,int total)
	{
		List<Records> mylist = new ArrayList<Records>();
		try {
			Connection con =getConnection();
			String myquery1 = "SELECT titles,url From urls WHERE id in (SELECT URLID FROM words WHERE stem='" + word + "') LIMIT " + (start-1)+","+total;
			String myquery2 ="SELECT preview FROM words WHERE stem='" + word +"' LIMIT " + (start-1)+","+total;
			
			
			PreparedStatement ps1 = con.prepareStatement(myquery1);
			PreparedStatement ps2 = con.prepareStatement(myquery2);
			ResultSet results1 = ps1.executeQuery();
			ResultSet results2 = ps2.executeQuery();
			
			
			while(results1.next() && results2.next())
			{
				Records r =new Records();
				r.setTitle(results1.getString(1));
				r.setUrl(results1.getString(2));
				r.setParagraphs(results2.getString(1));
				mylist.add(r);
			}
			con.close();
		}
		catch(Exception e)
		{
			System.err.println("erorr" + e);
		}
		
		return mylist;
	}
	

}

