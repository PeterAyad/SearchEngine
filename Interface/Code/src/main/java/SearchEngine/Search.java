package SearchEngine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/////////////////////////////////////////////////


@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Search() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter out=null;
		try {
			
		  out=response.getWriter();
		  String SearchTopic =request.getParameter("topic");
		  String ShowSearchTopic=SearchTopic;
		  String IsItFirstPage = request.getParameter("page");
		  String addpage="";
		  //System.out.println(IsItFirstPage);
		  if(IsItFirstPage == null) //user just searched for a new topic 
		  {  IsItFirstPage="1";	
		     sedb.setsearched(SearchTopic);
		     addpage="+\"&page=1\"";
		  }
		  
		  int Page = Integer.parseInt(IsItFirstPage);
		  //check on the SearchTopic for english and stop words and meta chars
		  ArrayList<String> mylist = new ArrayList<>();		
		  mylist= Extract.removeStoppingWords(Extract.splitSentence(Extract.escapeMetaCharacters(SearchTopic)));
		  if( (mylist.size()) == 0 || mylist.get(0)=="")	//stop words
		  {
			  out.println("\r\n"
			  		+ "<!DOCTYPE html>\r\n"
			  		+ "<html lang=\"en\">\r\n"
			  		+ "\r\n"
			  		+ "<head>\r\n"
			  		+ "    <meta charset=\"UTF-8\">\r\n"
			  		+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
			  		+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\r\n"
			  		+ "    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css\" integrity=\"sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB\"\r\n"
			  		+ "        crossorigin=\"anonymous\">\r\n"
			  		+ "\r\n"
			  		+ "    <title>stop words</title>\r\n"
			  		+ "</head>\r\n"
			  		+ "\r\n"
			  		+ "<body >\r\n"
			  		+ "\r\n"
			  		+ "    <header>\r\n"
			  		+ "        <h1 class=\"display-1 text-center  my-0 bg-light\"><a class=\"text-dark\" href=\"BOOM.html\">BOOM</a></h1>\r\n"
			  		+ "        </br>\r\n"
			  		+ "        </br>\r\n"
			  		+ "    </header>\r\n"
			  		+ "\r\n"
			  		+ "    <div class=\"container\">\r\n"
			  		+ "        <div class=\"alert alert-warning \">\r\n"
			  		+ "            <h1><strong>BOOM...That's not Right</strong></h1>\r\n"
			  		+ "            You may have entered some stop words (meaningless words) in the search box.</br>\r\n"
			  		+ "            In order to get results you must enter a normal word<strong>(not a stop word)</strong> .\r\n"
			  		+ "        </div>\r\n"
			  		+ "    </div>\r\n"
			  		+ "    <!-- ./container -->\r\n"
			  		+ "\r\n"
			  		+ "    <script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\" integrity=\"sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo\"\r\n"
			  		+ "        crossorigin=\"anonymous\"></script>\r\n"
			  		+ "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js\" integrity=\"sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49\"\r\n"
			  		+ "        crossorigin=\"anonymous\"></script>\r\n"
			  		+ "    <script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js\" integrity=\"sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T\"\r\n"
			  		+ "        crossorigin=\"anonymous\"></script>\r\n"
			  		+ "</body>\r\n"
			  		+ "\r\n"
			  		+ "</html>\r\n"
			  		+ "");
			  return;
		  }
		  else if( (mylist.size()) > 1 ) 	//more than one word
		  {
			  out.println("<!DOCTYPE html>\r\n"
			  		+ "<html lang=\"en\">\r\n"
			  		+ "\r\n"
			  		+ "<head>\r\n"
			  		+ "    <meta charset=\"UTF-8\">\r\n"
			  		+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
			  		+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\r\n"
			  		+ "    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css\" integrity=\"sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB\"\r\n"
			  		+ "        crossorigin=\"anonymous\">\r\n"
			  		+ "\r\n"
			  		+ "    <title>Not Supported</title>\r\n"
			  		+ "</head>\r\n"
			  		+ "\r\n"
			  		+ "<body >\r\n"
			  		+ "\r\n"
			  		+ "    <header>\r\n"
			  		+ "        <h1 class=\"display-1 text-center  bg-light\"><a class=\"text-dark\" href=\"BOOM.html\">BOOM</a></h1>\r\n"
			  		+ "        <br/>\r\n"
			  		+ "        <br/>\r\n"
			  		+ "    </header>\r\n"
			  		+ "    <div class=\"container\">\r\n"
			  		+ "        <div class=\"alert alert-warning\">\r\n"
			  		+ "            <h1><strong>BOOM...That's not Supported yet</strong></h1>\r\n"
			  		+ "            You may have entered more than one word in the search box.</br>\r\n"
			  		+ "            Currently you can search on <strong>one word</strong> only.\r\n"
			  		+ "        </div>\r\n"
			  		+ "    </div>\r\n"
			  		+ "    <!-- ./container -->\r\n"
			  		+ "    <script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\" integrity=\"sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo\"\r\n"
			  		+ "        crossorigin=\"anonymous\"></script>\r\n"
			  		+ "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js\" integrity=\"sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49\"\r\n"
			  		+ "        crossorigin=\"anonymous\"></script>\r\n"
			  		+ "    <script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js\" integrity=\"sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T\"\r\n"
			  		+ "        crossorigin=\"anonymous\"></script>\r\n"
			  		+ "</body>\r\n"
			  		+ "\r\n"
			  		+ "</html>\r\n"
			  		+ "\r\n"
			  		+ "");
			  return;
		  }
		  else
		  {
			  SearchTopic=mylist.get(0);
		  }
		  String stemed=Extract.stemS(SearchTopic);
		  //////////////////////////////////////////////////////////////////////////////////////////////////////
		 // String message = "Your Searched about  (" + stemed + "), and this is page no." + Page + " thank you<br>";
		  //out.println(message);
		  
		  
		  
		  
		  String PageStart="<!DOCTYPE html>\r\n"
		  		+ "<html lang=\"en\">\r\n"
		  		+ "\r\n"
		  		+ "<head>\r\n"
		  		+ "    <meta charset=\"UTF-8\">\r\n"
		  		+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
		  		+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\r\n"
		  		+ "    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css\" integrity=\"sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB\"\r\n"
		  		+ "        crossorigin=\"anonymous\">\r\n"
		  		+ "\r\n"
		  		+ "    <title>Results Page</title>\r\n"
		  		+ "</head>\r\n"
		  		+ "\r\n"
		  		+ "<body>\r\n"
		  		+ "\r\n"
		  		+ " \r\n"
		  		+ " <nav class=\"navbar sticky-top navbar-expand-sm navbar-light bg-light mb-3\">\r\n"
		  		+ "        <a class=\"navbar-brand\" href=\"BOOM.html\">BOOM</a>\r\n"
		  		+ "     <dev class=\"ml-auto\">\r\n"
		  		+ "            <button class=\"navbar-toggler\" data-toggle=\"collapse\" data-target=\"#navbarNav\">\r\n"
		  		+ "                <span class=\"navbar-toggler-icon\"></span>\r\n"
		  		+ "            </button>\r\n"
		  		+ "            <div class=\"collapse navbar-collapse\" id=\"navbarNav\">\r\n"
		  		+ "                <form class=\"form-inline\" action=Search?page=1 method=get id=Search>\r\n"
		  		+ "                    <div class=\"input-group \">\r\n"
		  		+ "                        <input class=\"form-control\" type=\"text\" placeholder=\"Search Boom\" name=\"topic\" value=\"" +ShowSearchTopic + "\"autocomplete=\"off\" list=\"suggestions\" id=\"topic\" oninput=\"Suggest()\" required>\r\n"
		  		+"                                            <datalist id=\"suggestions\">\r\n"
		  		+ "                                            </datalist>"
		  		+ "                        <div class=\"input-group-append\">\r\n"
		  		+ "                            <button class=\"btn btn-outline-dark\" type=submit value=\"Submit\">Search</button>\r\n"
		  		+ "                        </div>\r\n"
		  		+ "                    </div>\r\n"
		  		+ "                </form>\r\n"
		  		+ "            </div>\r\n"
		  		+ "        </dev>\r\n"
		  		+ "    </nav>\r\n"
		  		+ "    <br>\r\n"
		  		+ "    <div class=\"container \">";
		  out.println(PageStart);
		  /////////////////////////////////////////////////////////////////////////////////////////////////////////printing results count
		  int ResultMax=sedb.getRecordCount(stemed);
		  int PageMax = (ResultMax/10);
		  if((ResultMax%10) > 0 )
			  PageMax++;
		  String ResultCount = (String.valueOf(ResultMax))+" Results" ;
		  out.println("<h6><small>Total " + ResultCount + "</small></h6>");

		  List<Records> myResults=sedb.getRecord(stemed,(((Page-1)*10)+1), 10);
		  /////////////////////////////////////////////////////////////////////////////////////////////////////////printing results
		  out.println("<div>");
		  for(Records r:myResults)
		  {
		  String title =r.getTitle();
		  String URL =r.getUrl();
		  String paragraph = r.getParagraphs()+ "......";
		  /////////////////////////////////////////////////////////////////////////////////////////////////////////
		  
		  String PageContent="        <div class=\"card border-white my-0\">\r\n"
		  		+ "            <div class=\"card-body\"> \r\n"
		  		+ "                <h5 class=\"card-title my-0\"><a href=" + URL + ">" + title + "</a><br/><h6 class=\" text-success\">" + URL + "</h6></h5>\r\n"
		  				+ "                <p class=\"card-text\" >" + paragraph + "</p>\r\n"
		  						+ "            </div>\r\n"
		  						+ "        </div>\r\n"
		  						+ "";
		 
		  out.println(PageContent);
		  }
		  out.println("</div>");
		  ///////////////////////////////////////////////////////////////////////////////////////////////////////// Pagings
		  String navbar="";
		  if(Page == 1)
			  navbar="        <nav>\r\n"
			  		+ "            <ul class=\"pagination justify-content-center mt-5\" >\r\n"
			  		+ "                <li class=\"page-item disabled\">\r\n"
			  		+ "                    <a class=\"page-link\" href=\"#\">Previous</a>\r\n"
			  		+ "                </li>";
		  else
			  navbar="                <nav>\r\n"
			  		+ "                    <ul class=\"pagination justify-content-center mt-5\" >\r\n"
			  		+ "                        <li class=\"page-item\">\r\n"
			  		+ "                            <a class=\"page-link\" href=\"Search?page=" + String.valueOf(Page-1) + "&topic="+ShowSearchTopic+"\">Previous</a>\r\n"
			  				+ "                        </li>";		  
			  
		  
		  int start=0,end=0;
		  
		  if((Page%5) > 3 || (Page%5)==0) //make it new center
		  {
			  start=Page-2;
			  end=Page+2;
			  if(end > PageMax)
				  end=PageMax;
		  }
		  else //leave it
		  {
			  start = (Page - (Page%5) )+1;
			  end=start+4;
			  if(end > PageMax)
				  end=PageMax;
		  }

		  while( ((end-start) < 4) && (start > 1))
				start=start-1;

		  
		  for(int i=start;i<=end;i++)
		  {
			  if(i==Page)
				  navbar+="<li class=\"page-item active\">";
			  else
				  navbar+="<li class=\"page-item\">";
			  String pg = String.valueOf(i);
			  navbar +="<a class=\"page-link\" href=\"Search?page=" + pg + "&topic="+ShowSearchTopic+"\">" + pg + "</a>\r\n"
			  		+ "                </li>";
		  }
		  if(Page >= PageMax)
			  navbar+="                <li class=\"page-item disabled\">\r\n"
			  		+ "                    <a class=\"page-link\" href=\"#\">Next</a>\r\n"
			  		+ "                </li>\r\n"
			  		+ "            </ul>\r\n"
			  		+ "        </nav>";
		  else
			  navbar+="               <li class=\"page-item\">\r\n"
			  		+ "                    <a class=\"page-link\" href=\"Search?page=" + String.valueOf(Page+1) + "&topic="+ShowSearchTopic+"\">Next</a>\r\n"
			  		+ "                </li>\r\n"
			  		+ "            </ul>\r\n"
			  		+ "        </nav>";
		  
		  
		  out.println(navbar);
		  /////////////////////////////////////////////////////////////////////////////////////////////////////////
		  String PageEnd="   </div>\r\n"
		  		+ "    <!-- ./container    <div style=\"margin-top:500px;\"></div>  -->\r\n"
		  		+ "    <script src=\"http://code.jquery.com/jquery-latest.min.js\"></script>\r\n"
		  		+ "    <script>\r\n"
		  		+ "    var request;  \r\n"
		  		+ " function Suggest() {\r\n"
		  		+ "      var v = document.getElementById(\"topic\").value;\r\n"
		  		+ "      var url=\"HandelSuggestions?value=\"+v;  \r\n"
		  		+ "      if(window.XMLHttpRequest){  \r\n"
		  		+ "    	  request=new XMLHttpRequest();  \r\n"
		  		+ "    	  }  \r\n"
		  		+ "    	  else if(window.ActiveXObject){  \r\n"
		  		+ "    	  request=new ActiveXObject(\"Microsoft.XMLHTTP\");  \r\n"
		  		+ "    	  }  \r\n"
		  		+ "      try  \r\n"
		  		+ "      {  \r\n"
		  		+ "      request.onreadystatechange=getInfo;  \r\n"
		  		+ "      request.open(\"GET\",url,true);  \r\n"
		  		+ "      request.send();  \r\n"
		  		+ "      }  \r\n"
		  		+ "      catch(e)  \r\n"
		  		+ "      {  \r\n"
		  		+ "      alert(\"Unable to connect to server\");  \r\n"
		  		+ "      }  \r\n"
		  		+ "          \r\n"
		  		+ "      //document.getElementById(\"suggestions\").innerHTML = '<option value=\"' + x + '\" />';\r\n"
		  		+ "    }\r\n"
		  		+ " function getInfo(){  \r\n"
		  		+ "	 if(request.readyState==4){  \r\n"
		  		+ "	 var val=request.responseText;  \r\n"
		  		+ "	 document.getElementById('suggestions').innerHTML=val;  \r\n"
		  		+ "	 } \r\n"
		  		+ " }\r\n"
		  		+ "</script>\r\n"
		  		+"<script>var x=document.getElementsByClassName(\"card-text\");for(var i=0;i< x.length;i++){x[i].innerHTML = x[i].innerHTML.replace(/" + ShowSearchTopic +"/g, '<strong>" +ShowSearchTopic+"</strong>');} </script>"
		  		+"<script>var x=document.getElementsByClassName(\"card-text\");for(var i=0;i< x.length;i++){x[i].innerHTML = x[i].innerHTML.replace(/" + stemed +"/g, '<strong>" +stemed+"</strong>');} </script>"
		  		+"<script>\r\n"
		  		+ "if ( window.history.replaceState ) {\r\n"
		  		+ "  window.history.replaceState( null, null,window.location.href" +addpage+ ");\r\n"
		  		+ "}\r\n"
		  		+ "</script>"
		  		+ "    <script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\" integrity=\"sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo\"\r\n"
		  		+ "        crossorigin=\"anonymous\"></script>\r\n"
		  		+ "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js\" integrity=\"sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49\"\r\n"
		  		+ "        crossorigin=\"anonymous\"></script>\r\n"
		  		+ "    <script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js\" integrity=\"sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T\"\r\n"
		  		+ "        crossorigin=\"anonymous\"></script>\r\n"
		  		+ "</body>\r\n"
		  		+ "\r\n"
		  		+ "</html>\r\n"
		  		+ "";
		  out.println(PageEnd);
		  
		  
		}
		catch(Exception e)
		{
			out.println("Error" + e.getMessage());
		}


	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
