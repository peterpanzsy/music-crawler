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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;




public class QQCrawler {

    public static void main(String args[])  throws  Exception {
    	String song ="海阔天空";
    	String singer = "beyond";
		Document doc = null;
		doc = searchSong(song, singer);
		Elements scripts = doc.getElementsByTag("script");
    	Element script = scripts.get(4);
    	String[] vars = script.html().split("var");
    	String v8SongData = vars[4].trim().split(" = ")[1];   	
    	JSONObject songsObject = JSONObject.fromObject(v8SongData);
    	JSONArray songsArr = songsObject.getJSONArray("list"); 	
    	JSONObject songObj = (JSONObject) songsArr.get(0);
    	String songname = (String) songObj.get("songname");//歌名
    	String songmid = (String) songObj.get("songmid");//001fhSpB0P7buZ
    	String songid = songObj.get("songid").toString();//4833285
    	String songurl = (String) songObj.get("songurl");
		String albumid = songObj.get("albumid").toString();   
		String albummid = (String) songObj.get("albummid"); 
		//歌曲信息
		Document songDetailDoc = getSongInfo(songid);
		Elements songDs = songDetailDoc.getElementsByClass("song_info");
		String singerName = songDetailDoc.getElementsByClass("singer_name").first().getElementsByTag("a").first().text();//歌手
		String singerUrl = songDetailDoc.getElementsByClass("singer_name").first().getElementsByTag("a").first().attr("href");
		String singermid = singerUrl.split("singermid=")[1];
		String singerLang = songDetailDoc.getElementsByClass("singer_lang").first().parent().ownText();//语言
		String album = songDetailDoc.getElementsByClass("album_name").first().getElementsByTag("a").first().ownText();//专辑
		String albumUrl = songDetailDoc.getElementsByClass("album_name").first().getElementsByTag("a").first().attr("href");
		//String albummid = albumUrl.split("albummid=")[1];
		String issueDate = songDetailDoc.getElementsByClass("issue_date").first().text().split("：")[1].trim();//发行时间
		int songhot = getSongHot(songmid);//歌曲听众数量
		//歌手信息
		String singerid = getSingerInfo(singermid);    	
		int lnum = getListenerNum(singermid);//听众数量
		JSONArray simsingers = getSimSingers(singermid);//相似歌手
		Document singerDescDoc = getSingerDesc(singerid);//歌手介绍
		String singerdesc = singerDescDoc.getElementsByTag("desc").text();//歌手简介
		//专辑信息
		Document aldoc = getAlbumInfo(albummid);//00449cf44ccf8n 
		Element albuminfo = aldoc.getElementsByClass("album_text").get(0);
		String albumSinger = aldoc.getElementsContainingOwnText("歌手").first().getElementsByTag("a").first().text().trim();//歌手
		String albumType = aldoc.getElementsContainingOwnText("流派").get(0).text().split("：")[1].trim();//流派
		String albumLaguage = aldoc.getElementsContainingOwnText("语言").get(0).text().split("：")[1].trim();//语言
		String albumPubdate = aldoc.getElementsContainingOwnText("发行时间").get(0).text().split("：")[1].trim();//发行日期		
		String comment = getAlbumComment(albumid);//评分
		
	}
    
    //搜索歌曲
    public static Document searchSong(String song, String singer) throws UnsupportedEncodingException{
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
		return doc;
    }
    
    //获取歌曲信息
    public static Document getSongInfo(String id) throws UnsupportedEncodingException{ 
    	String searchUrl = "http://i.y.qq.com/s.plcloud/fcgi-bin/fcg_yqq_song_detail_info_cp.fcg?songid="+id+"&play=0";
		Document doc = null;
		doc = getHtmlContent(searchUrl);
		return doc;
    }
    //获取歌曲热度值,歌曲听众数量
    public static int getSongHot(String songmid){
    	String url = "http://i.y.qq.com/s.plcloud/fcgi-bin/fcg_getsonglistenstatistic.fcg?utf8=1&songmidlist="+songmid+"&_=0.6329051251523197&g_tk=938407465&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&jsonpCallback=songlisten1456298401243&needNewCode=0";
    	Document doc = null;
		doc = getHtmlContent(url);
		String res = doc.getElementsByTag("body").get(0).text();
		JSONObject obj = JSONObject.fromObject(res.split("\\(")[1].split("\\)")[0]);
		JSONArray jarr = (JSONArray)obj.get("songlist");
		JSONObject songobj = (JSONObject)jarr.get(0);
		int count = (Integer)songobj.get("count");
		return count;
    }
    //获取歌手听众数量
    public static int getListenerNum(String singermid){
    	String url = "http://i.y.qq.com/s.plcloud/fcgi-bin/fcg_order_singer_getnum.fcg?singermid="+singermid+"&rnd=1456122785659&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=gb2312&notice=0&platform=yqq&jsonpCallback=MusicJsonCallback&needNewCode=0";
    	Document doc = null;
		doc = getHtmlContent(url);
		String res = doc.getElementsByTag("body").get(0).text();
		JSONObject lo = JSONObject.fromObject(res.split("\\(")[1].split("\\)")[0]);
		int num = (Integer)lo.get("num");
		return num;
    }
    
    //获取相似歌手
    public static JSONArray getSimSingers(String singermid){
    	String url = "http://i.y.qq.com/v8/fcg-bin/fcg_v8_simsinger.fcg?utf8=1&singer_mid="+singermid+"&start=0&num=6&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&jsonpCallback=SingerSimCallback&needNewCode=0";
    	Document doc = null;
    	doc = getHtmlContent(url);
    	String res = doc.getElementsByTag("body").get(0).text();
		JSONObject jobj = JSONObject.fromObject(res.split("\\(")[1].split("\\)")[0]);
		JSONObject singers = (JSONObject) jobj.get("singers");
		JSONArray items = singers.getJSONArray("items");
		return items;
    }
    //获取歌手singerid
    public static String getSingerInfo(String singermid) throws UnsupportedEncodingException{ 
    	String searchUrl = "http://i.y.qq.com/v8/fcg-bin/fcg_v8_singer_detail_cp.fcg?tpl=20&singermid="+singermid;
		Document doc = null;
		doc = getHtmlContent(searchUrl);
		Elements scripts = doc.getElementsByTag("script");
    	Element script = scripts.get(9);
    	String vars = script.html().split("g_singerChn.init\\(")[1].split("\\);\n\\}")[0];
    	System.out.print(vars);	
    	JSONObject songsObject = JSONObject.fromObject(vars);
    	String singerid = (String)songsObject.get("singerid"); 	
		return singerid;
    }
    //获取歌手介绍
    public static Document getSingerDesc(String singerid){
    	String url = "http://i.y.qq.com/s.plcloud/fcgi-bin/fcg_get_singer_desc.fcg?utf8=1&outCharset=utf-8&format=xml&singerid="+singerid+"&r=0.4638219918124378";
    	Document doc = null;
    	try {
			doc = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Windows NT 5.1; zh-CN) AppleWebKit/535.12 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/535.12")
					.header("Referer", "http://i.y.qq.com/v8/fcg-bin/fcg_v8_singer_detail_cp.fcg?tpl=20&singermid=")//002pUZT93gF4Cu
					.timeout(10*1000).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return doc;
    }
    //获取专辑信息
    public static Document getAlbumInfo(String id){
    	String url = "http://i.y.qq.com/v8/fcg-bin/fcg_v8_album_detail_cp.fcg?tpl=20&albummid="+id;
    	Document doc = null;
    	doc = getHtmlContent(url);
    	return doc;
    }
    //获取专辑评分
    public static String getAlbumComment(String albumid){//62660
    	String url = "http://i.y.qq.com/portalcgi/fcgi-bin/music_mini_portal/cgi_query_album_comment.fcg?id="+albumid+"&num=1&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=GB2312&notice=0&platform=yqq&jsonpCallback=MusicJsonCallback&needNewCode=0";
    	Document doc = null;
    	doc = getHtmlContent(url);
     	String res = doc.getElementsByTag("body").get(0).text();
		JSONObject jobj = JSONObject.fromObject(res.split("\\(")[1].split("\\)")[0]);
		String comment = (String) jobj.get("comment");
    	return comment;
    }
	public static Document getHtmlContent(String url){
		Document doc = null;
		while(true){
			
			try {
				doc = Jsoup.connect(url)
						.userAgent("Mozilla/5.0 (Windows NT 5.1; zh-CN) AppleWebKit/535.12 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/535.12")
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
    //没用
    public static String parseHtml(String html){
    	Document doc = Jsoup.parse(html);
    	return "";
    }
    //测试htmlunit，并没有什么卵用
    public static void testCrawler(String singermid) throws Exception {          
    	/**HtmlUnit请求web页面*/          
    	WebClient wc = new WebClient(BrowserVersion.INTERNET_EXPLORER_11);          
    	wc.getOptions().setJavaScriptEnabled(true);//启用JS解释器，默认为true          
    	wc.getOptions().setCssEnabled(false);//禁用css支持          
    	wc.setAjaxController(new NicelyResynchronizingAjaxController());
    	wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常          
    	wc.getOptions().setTimeout(10000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待          
    	HtmlPage page = wc.getPage("http://i.y.qq.com/v8/fcg-bin/fcg_v8_singer_detail_cp.fcg?tpl=20&singermid="+singermid);      
    	System.out.println("为了获取js执行的数据 线程开始沉睡等待");
    	Thread.sleep(30000);//主要是这个线程的等待 因为js加载也是需要时间的
    	System.out.println("线程结束沉睡");
    	write(page.asText());
    	String pageXml = page.asXml(); //以xml的形式获取响应文本            
    	/**jsoup解析文档*/          
    	Document doc = Jsoup.parse(pageXml, "http://i.y.qq.com");     
    	//write(doc.toString());
    	Element pv = doc.select("#feed_content span").get(1);          
    	System.out.println(pv.text());                  
    	System.out.println("Thank God!");      
    } 

}
