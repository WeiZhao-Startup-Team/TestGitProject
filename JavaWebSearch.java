package basics;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

public class Assignment{
	static boolean has_result;
	public static String getResults(Document htmldoc){
		if (htmldoc.getElementsByClass("weFound BodyXLBold").isEmpty()) {
			has_result = true;
			return htmldoc.getElementsByClass("numResults").text(); 
		} else {
			has_result = false;
			return htmldoc.getElementsByClass("BodyXLBoldOrg").text();	
		}
	}
	public static void main(String[] args) throws IOException {

		int numpage = 16;
		String page_start = "0";

		if (args.length >= 2) {
			page_start=String.valueOf(numpage*(Integer.parseInt(args[1])-1));	
		}

		String url = "http://www.walmart.com/search/search-ng.do?ic=16_"+page_start+"&search_query="+args[0]+"&Find=Find&search_constraint=0";
		Document doc = Jsoup.connect(url).get();
		if (doc == null) {
			System.out.println("error"); // or throw a exception?
//			return 0; //0?
		} else {
		String total_result = getResults(doc);
		if (args.length == 1) {   			
			if (total_result == null) { // better way for expression?
				System.out.println("get failed."); // or throw a exception?
//				return 0; //0?
			}
			System.out.println("There are "+total_result+" found.");	
		} else if (args.length == 2) {
			if (has_result) {
				
				Elements script = doc.getElementsByClass("ProdInfo");
				
				if (script == null) { // better way for expression?
					System.out.println("get failed."); // or throw a exception?
//					return 0; //0?
				}
		    	
				ResObj[] res_list = new ResObj[16];
		    	res_list[1] = new ResObj();
		    	
				//get numpage from script TODO; check if succeed, if not, return.
				
				for (int prod_num = 0; prod_num < numpage; prod_num++) {
		    		Element item = script.get(prod_num);
		    		res_list[prod_num] = new ResObj();
		    		res_list[prod_num].title = item.getElementsByClass("ListItemLink").text();   
		    		Elements price_con = item.getElementsByClass("PriceContent");
					
		    		if (!price_con.select("div.camelPrice").isEmpty()) {
		    			res_list[prod_num].price = price_con.select("div.camelPrice").text();
		    		} else if (!price_con.select("span.camelPrice").isEmpty()) {
		    			res_list[prod_num].price = price_con.select("span.camelPrice").text();
		    		} else if (!price_con.select("span.PriceSItalicStrikethruLtgry").isEmpty()) {
		    			res_list[prod_num].price = price_con.select("span.PriceSItalicStrikethruLtgry").text();
		    		}
		    		res_list[prod_num].printContent();
		    	}
			} else {
				System.out.println("There are no result found, please check spelling.");
			}
    	} else {
			System.out.println("Please input an argument.");
		}
		}
	}
}