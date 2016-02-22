package com.keystone.analytics.music.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;




public class test {
    private static String HOST = "y.qq.com";
    private static String BASE_URL = "http://"+HOST+"/";
    private static String url = "http://y.qq.com/";
	
    public static void main(String args[])  throws  ClientProtocolException, IOException, URISyntaxException {
    	String song ="海阔天空";
    	String singer = "beyond";
    	String searchKey=""; 
    	if(singer=="" && song!=""){
    		searchKey = URLEncoder.encode(song, "utf-8");
    	}else if(song=="" && singer != ""){
    		searchKey = URLEncoder.encode(singer, "utf-8");
    	}else if(singer!="" && song!=""){
    		searchKey = URLEncoder.encode(song, "utf-8")+"+"+URLEncoder.encode(singer,"utf-8");
    	}  
    	String baseUrl = "http://s.music.qq.com/fcgi-bin/yqq_search_v8_cp?p=1&catZhida=1&lossless=0&t=100&aggr=0&searchid=30677856250929057&remoteplace=txt.yqqlist.top&utf8=1&tab=all%7c";
		String searchUrl = baseUrl + searchKey +"&w="+searchKey;
		
		Document doc = null;
		doc = getHtmlContent(searchUrl);
		//String s = parseDoc(doc);		
		Elements scripts = doc.getElementsByTag("script");
    	Element script = scripts.get(4);
    	String[] vars = script.html().split("var");
    	String v8SongData = vars[4].trim().split(" = ")[1];   	
    	JSONObject songsObject = JSONObject.fromObject(v8SongData);
    	JSONArray songsArr = songsObject.getJSONArray("list");
    	System.out.println(songsArr);    	
    	JSONObject songObj = (JSONObject) songsArr.get(0);
    	System.out.println(songObj);
    	String songname = (String) songObj.get("songname");
    	String songmid = (String) songObj.get("songmid");
    	String songid = songObj.get("songid").toString();
    	String songurl = (String) songObj.get("songurl");
		    	
		Document songDetailDoc = getSongInfo(songid);
		write(songDetailDoc.toString());
		Elements songDs = songDetailDoc.getElementsByClass("song_info");
		String singerName = songDetailDoc.getElementsByClass("singer_name").first().getElementsByTag("a").first().text();
		String singerLang = songDetailDoc.getElementsByClass("singer_lang").first().parent().ownText();
		String album = songDetailDoc.getElementsByClass("album_name").first().getElementsByTag("a").first().ownText();
		String issueDate = songDetailDoc.getElementsByClass("issue_date").first().text().split("：")[1].trim();
    	CloseableHttpClient httpClient  = HttpClients.createDefault();
		// 设置GET请求参数，URL一定要以"http://"开头
		HttpGet getReq = new HttpGet(searchUrl);
		// 设置请求报头，模拟Chrome浏览器

//		getReq.setHeader("Set-Cookie", "cookiename=test;_qz_referrer=; expires=Mon, 26 Jul 2097 05:00:00 GMT; PATH=/; DOMAIN=qq.com");
//		getReq.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//		getReq.addHeader("Accept-Encoding", "gzip,deflate,sdch");
//		getReq.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		getReq.addHeader("Content-Type", "text/html; charset=UTF-8");
//		getReq.addHeader("Host", HOST);
//		getReq.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.76 Safari/537.36");
		// 发送GET请求
		CloseableHttpResponse rep = httpClient.execute(getReq);
		// 从HTTP响应中取出页面内容
		HttpEntity repEntity = rep.getEntity();
		String content = EntityUtils.toString(repEntity);
		// 打印出页面的内容：
		
		
		// 关闭连接
		rep.close();
		httpClient.close();
	}
    public static Document getSongInfo(String id) throws UnsupportedEncodingException{ 
    	String searchUrl = "http://i.y.qq.com/s.plcloud/fcgi-bin/fcg_yqq_song_detail_info_cp.fcg?songid="+id+"&play=0";
		Document doc = null;
		doc = getHtmlContent(searchUrl);
		return doc;
    }
    public static String parseDoc(Document doc){
    	Elements scripts = doc.getElementsByTag("script");
/*    	 for (Element script: scripts ) {     
    	     String scriptText = script.text(); 
    	}*/
    	Element script = scripts.get(4);
    	String[] vars = script.html().split("var");
    	String v8SongData = vars[4].trim().split(" = ")[1];
    	
    	JSONObject songsObject = JSONObject.fromObject(v8SongData);
    	JSONArray songsArr = songsObject.getJSONArray("list");
    	System.out.println(songsArr);
    	
    	JSONObject songObj = (JSONObject) songsArr.get(0);
    	System.out.println(songObj);
    	String songname = (String) songObj.get("songname");
    	String songmid = (String) songObj.get("songmid");
    	String songid = songObj.get("songid").toString();
    	String songurl = (String) songObj.get("songurl");
    	

		return songid;
    }
    public static String parseHtml(String html){
    	Document doc = Jsoup.parse(html);
    	
    	return "";
    }
	public static Document getHtmlContent(String url){
		Document doc = null;
		while(true){
			
			try {
				doc = Jsoup.connect(url)
						.userAgent("Mozilla")
						.timeout(10*1000)
						.get();
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return doc;
	}
    public static void write(String data){
	 try{
	      File file =new File("test.txt");

	      //if file doesnt exists, then create it
	      if(!file.exists()){
	       file.createNewFile();
	      }

	      //true = append file
	     FileWriter fileWritter = new FileWriter(file.getName(),false);
         BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
         bufferWritter.write(data);
         bufferWritter.close();

	     System.out.println("Done");

	     }catch(IOException e){
	      e.printStackTrace();
	     }
    }

}
