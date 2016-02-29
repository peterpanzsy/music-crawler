package com.keystone.analytics.music.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.keystone.analytics.music.entity.Album;
import com.keystone.analytics.music.entity.Review;
import com.keystone.analytics.music.entity.Reviewer;
import com.keystone.analytics.music.entity.Singer;
import com.keystone.analytics.music.entity.Song;

public class XiamiCrawler {
	
	public static void main(String args[]) throws IOException{
		String song ="海阔天空";
    	String singer = "beyond";
		Document doc = null;
		doc = searchSong(song, singer);
		//搜索歌曲
		Element songTable = doc.getElementsByClass("search_result").get(0).getElementsByTag("table").get(0);
		Element songbody = songTable.getElementsByTag("tbody").get(0);
		String songUrl = songbody.getElementsByClass("song_name").get(0).getElementsByTag("a").get(1).attr("href");
		String songid = songUrl.substring(songUrl.lastIndexOf("/")+1, songUrl.length());
		singer = songbody.getElementsByClass("song_artist").get(0).getElementsByTag("b").get(0).text();//歌手
		String singerUrl = songbody.getElementsByClass("song_artist").get(0).getElementsByTag("a").first().attr("href");
		String singerId = singerUrl.substring(singerUrl.lastIndexOf("/")+1, singerUrl.length());
		String albumUrl = songbody.getElementsByClass("song_album").first().getElementsByTag("a").first().attr("href");
		String albumId = albumUrl.substring(albumUrl.lastIndexOf("/")+1, albumUrl.length());
		String albumName =songbody.getElementsByClass("song_album").first().getElementsByTag("b").first().text();//专辑名称 
		//歌曲信息
		Document songDoc = getSongInfo(songid);
		Element albumTable = songDoc.getElementsByAttributeValue("id", "albums_info").first();
		String lyricsBy = albumTable.getElementsByTag("tr").get(2).getElementsByTag("div").text();//作词
		String composer = albumTable.getElementsByTag("tr").get(3).getElementsByTag("div").text();//作曲
		int shareTimes = Integer.parseInt(songDoc.getElementsByClass("music_counts").first().getElementsByTag("li").get(1).ownText());//分享次数
		int songReviewCount = Integer.parseInt(songDoc.getElementsByClass("music_counts").first().getElementsByTag("a").get(0).ownText());//评论条数
		int listencount = getListenCount(songid);//收听次数
		//歌曲评论信息和评论者信息
		ArrayList<Review> reArray = new ArrayList<Review>();//评论
		ArrayList<Reviewer> reviewerArray = new ArrayList<Reviewer>();//评论者
		Elements commentlis = songDoc.getElementsByClass("hotComment").first().getElementsByTag("li");//热门评论
		for(Element e: commentlis){
			Review r = analyReview(e, songid);
			reArray.add(r);
		}
		//getReviewWallList(songid, songReviewCount, reArray, reviewerArray);//2000简评
		//歌手信息
		Document singerDoc = getSingerInfo(singerId);
		Elements singerInfos = singerDoc.getElementsByAttributeValue("id","artist_info").first().getElementsByTag("tr");
		String singerArea = singerInfos.get(0).getElementsByTag("td").get(1).text();//歌手地区
		String singerDesc = singerInfos.get(2).getElementsByClass("record").text();//歌手介绍
		int singerListenCount = getSingerListenCount(singerId);//收听次数
		String fanCount = singerDoc.getElementsByClass("music_counts").first().getElementsByTag("a").get(0).ownText();//粉丝数量
		String reviewCount = singerDoc.getElementsByClass("music_counts").first().getElementsByTag("a").get(1).ownText();//评论次数
		ArrayList<Singer> simSingers = getSimSingers(singerId);//获取相似歌手	
		ArrayList<Reviewer> fans = getFans(singerId);//获取粉丝
		ArrayList<Album> allist = getAlbums(singerId);//歌手的专辑
		//专辑信息
		Document albumDoc = getAlbumInfo(albumId);
		Element albumdiv = albumDoc.getElementsByAttributeValue("id","album_info").first();
		String score = albumdiv.getElementsByAttributeValue("id", "album_rank").first().getElementsByTag("em").text();//评分
		Elements trs = albumdiv.getElementsByTag("tr");
		String language = trs.get(1).getElementsByAttributeValue("valign", "top").first().text();//语言
		String company = trs.get(2).getElementsByTag("a").text();//发行公司
		String date = trs.get(3).getElementsByAttributeValue("valign", "top").first().text();//发行日期
		String type = trs.get(4).getElementsByAttributeValue("valign", "top").first().text();//类型
		String style = trs.get(5).getElementsByTag("a").first().text();//风格
		String collectedCount = albumDoc.getElementsByClass("music_counts").first().getElementsByTag("li").get(1).ownText();//收藏数
		String alreviewCount = albumDoc.getElementsByClass("music_counts").first().getElementsByTag("i").first().ownText();//评论数
		Elements songhots = albumDoc.getElementsByClass("song_hot");
		int playCount = 0;//试听数
		for(Element e: songhots){
			playCount += Integer.parseInt(e.text());
		}
	}
	//歌曲的专辑信息
	public static Document getAlbumInfo(String albumid) throws IOException{
		String url = "http://www.xiami.com/album/"+albumid;
		Document doc = getHtmlContent(url);
		return doc;
	}
	//获取歌手的专辑
	public static ArrayList<Album> getAlbums(String singerid){
		ArrayList<Album> albums = new ArrayList<Album>();
		String url = "http://www.xiami.com/artist/album-"+singerid+"?spm=0.0.0.0.Vgi82t";
		try {
			Document doc = getHtmlContent(url);
			write(doc.toString());
			int albumCou = Integer.parseInt(doc.getElementsByClass("cate_viewmode").first().getElementsByTag("p").first().text().replace("共", "").replace("张专辑", ""));
			for(int i=1;i<=albumCou; i++){
				String aurl = "http://www.xiami.com/artist/album-"+singerid+"?spm=0.0.0.0.NwE1eK&d=&p=&page="+i;
				Document adoc = getHtmlContent(aurl);
				Elements lis = adoc.getElementsByClass("albumThread_list").first().getElementsByTag("li");
				for(Element li: lis){
					Elements ps = li.getElementsByClass("detail").first().getElementsByTag("p");
					String name = ps.get(0).getElementsByTag("a").first().text();
					String albumUrl = ps.get(0).getElementsByTag("a").first().attr("href");
					String albumId = albumUrl.split("\\/")[2];
					String company = ps.get(1).getElementsByTag("a").first().text();
					String date = ps.get(1).getElementsByTag("a").get(1).text();
					String type = ps.get(1).ownText().split("类别：")[1].trim();
					String score = li.getElementsByClass("album_rank").get(0).getElementsByTag("em").text();
					Album a = new Album();
					a.setAlbumid(albumId);
					a.setName(name);
					a.setCompany(company);
					a.setDate(date);
					a.setType(type);
					a.setScore(score);
					albums.add(a);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return albums;	
	}
	//获取歌手的粉丝
	public static ArrayList<Reviewer> getFans(String singerid){
		ArrayList<Reviewer> fans = new ArrayList<Reviewer>();
		for(int i = 1; i <= 100; i++){//一共显示2000个粉丝
			String url = "http://www.xiami.com/artist/fans-"+singerid+"?page="+i;
			try{				
				Document doc = getHtmlContent(url);
				Elements fanElements = doc.getElementsByAttributeValue("id", "artist_fans").first().getElementsByClass("user_list").first().getElementsByClass("user_info");
				for(Element fan: fanElements){
					String genderInfo = fan.ownText();
					String name = fan.getElementsByClass("name").text();//昵称
					String other = fan.getElementsByTag("span").text();
					int followCount = Integer.parseInt(other.split("关注\\| ")[0]);
					//String[] s=other.split("关注\\| ");
					int fansCount = Integer.parseInt(other.split("关注\\| ")[1].replace("粉丝", "").trim());
					String newTweet = fan.getElementsByTag("p").get(1).text();
					Reviewer r = new Reviewer();
					r.setNick(name);
					r.setGender(genderInfo.split("\\/").length>1?genderInfo.split("\\/")[1]:"");
					r.setFansCount(fansCount);
					r.setFollowCount(followCount);
					fans.add(r);
				}
			}catch( Exception e){
				System.out.println(i);
				System.out.println(url);
				System.out.println(e);
			}
		}
		return fans;
	}
	//获取歌手的相似歌手
	public static ArrayList<Singer> getSimSingers(String singerid) throws IOException{
		ArrayList<Singer> simSingers = new ArrayList<Singer>();
		for(int i=1;i<=5;i++){//50个相似歌手
			String url = "http://www.xiami.com/artist/similar-"+singerid+"?page="+i;
			Document doc = getHtmlContent(url);
			Elements singers = doc.getElementsByAttributeValue("id", "artist_similar").first().getElementsByClass("item_content");
			for(Element singer : singers){
				String name = singer.getElementsByTag("a").first().text();
				String singerUrl = singer.getElementsByTag("a").first().attr("href");
				String id = singerUrl.substring(singerUrl.lastIndexOf("/")+1,singerUrl.length());
				int fancount = Integer.parseInt(singer.getElementsByClass("sep").first().text().replace("位粉丝", ""));
				String desc = singer.getElementsByClass("minor").get(1).ownText();
				Singer s = new Singer();
				s.setSingerid(id);
				s.setName(name);
				s.setDesc(desc);
				s.setFanCount(fancount);
				simSingers.add(s);
			}
		}
		return simSingers;
	}
	//歌手的收听次数
	public static int getSingerListenCount(String singerid) throws IOException{
		String url = "http://www.xiami.com/count/getplaycount?id="+singerid+"&type=artist";
		Document doc = null;
		doc = getHtmlContent(url);
		String res = doc.body().text();
		JSONObject obj = JSONObject.fromObject(res);
		int count = obj.getInt("plays");
		return count;
	}
	//歌手的信息
	public static Document getSingerInfo(String singerid) throws IOException{
		String url = "http://www.xiami.com/artist/"+singerid;
		Document doc = null;
		doc = getHtmlContent(url);
		return doc;
	}
	//获取歌曲的n条简评
	public static void getReviewWallList(String songid, int songReviewCount, ArrayList<Review> rlist, ArrayList<Reviewer> reviewerArray ){
		//2000， 10*200
		for(int i=1; i<=songReviewCount/10; i++){
			String url = "http://www.xiami.com/commentlist/turnpage/id/"+songid+"/page/"+i+"/ajax/1";
			Document doc = null;
	    	try {
				doc = Jsoup.connect(url)
						.userAgent("Mozilla/5.0 (Windows NT 5.1; zh-CN) AppleWebKit/535.12 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/535.12")
						.header("Referer", "http://www.xiami.com/song/"+songid+"?spm=a1z1s.6843761.226669510.9.0YA8F5&from=search_popup_song")//002pUZT93gF4Cu
						.data("type", "4")
						.timeout(10*1000).post();		
				Elements lis = doc.getElementsByTag("li");
				for(Element e: lis){
					Review r = analyReview(e, songid);
					Reviewer rer = getReviewer(r.getReviewerid());
					rlist.add(r);
					reviewerArray.add(rer);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	//获取评论者个人主页信息
	public static Reviewer getReviewer(String reviewerid){
		String url = "http://www.xiami.com/u/"+reviewerid;
		Reviewer rer = new Reviewer();
		try {
			Document doc = getHtmlContent(url);
			Element pinfodiv = doc.getElementsByAttributeValue("id", "p_infoCount").first();
			Elements ps = pinfodiv.getElementsByTag("p");
			String pintro = ps.get(0).text();//来自哪里的90后女生
			String date = ps.get(1).text().split(" ")[0];
			int playcount = Integer.parseInt(pinfodiv.getElementsByAttributeValue("id", "num").first().text());//累計播放歌曲數量
			String level = pinfodiv.getElementsContainingOwnText("等级").get(0).getElementsByTag("a").text();//等級
			int visitedCount = Integer.parseInt(pinfodiv.getElementsByClass("p_stat").first().ownText().replace("次访问", "").trim());//被访问次数
			Elements lis = pinfodiv.getElementsByClass("counts").first().getElementsByTag("li");
			int followingCount = Integer.parseInt(lis.get(0).getElementsByTag("span").text());//关注人数
			int fansCount = Integer.parseInt(lis.get(1).getElementsByTag("span").text());//粉丝数量
			int shareCount = Integer.parseInt(lis.get(2).getElementsByTag("span").text());//分享次数
			Elements trs = doc.getElementsByAttributeValue("id", "p_tracks").first().getElementsByTag("tr");//最近在听
			ArrayList<Song> songArr = new ArrayList<Song>();//最近在听
			for(Element tr: trs){
				Elements songname = tr.getElementsByClass("song_name").first().getElementsByTag("a");
				String name = songname.get(0).text();//歌名
				String songUrl = songname.get(0).attr("href");
				String songid = songUrl.split("\\/")[2];//songid
				String singer = songname.get(1).text();
				String singerid = songname.get(1).attr("href").split("\\/")[2];
				Song s = new Song();
				s.setId(songid);
				s.setName(name);
				s.setSinger(singer);
				s.setSingerid(singerid);
				songArr.add(s);
			} 
			rer.setId(reviewerid);
			rer.setGender(pintro);
			rer.setJoinDate(date);
			rer.setPlayCount(playcount);
			rer.setLevel(level);
			rer.setVisitedCount(visitedCount);
			rer.setFollowCount(followingCount);
			rer.setShareCount(shareCount);
			rer.setFansCount(fansCount);
			rer.setRecentSongs(songArr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rer;
	}
	//解析每个评论的li
	public static Review analyReview(Element e,String songid){
		Element author = e.getElementsByClass("author").first().getElementsByTag("a").first();
		String reviewer = author.text();//评论者
		String reviewerUrl = author.attr("href");
		String reviewerId = author.attr("name_card");
		String date = e.getElementsByClass("time").text();//评论时间
		int agreeCount = Integer.parseInt(e.getElementsByClass("ageree").first().getElementsByTag("a").first().attr("rel"));
		int disagreeCount = Integer.parseInt(e.getElementsByClass("disageree").first().getElementsByTag("a").first().attr("rel"));
		String content = e.getElementsByClass("brief").first().text();
		Review r = new Review();
		r.setContent(content);
		r.setAgreeCount(agreeCount);
		r.setDisagreeCount(disagreeCount);
		r.setDate(date);
		r.setReviewer(reviewer);
		r.setReviewerid(reviewerId);
		r.setSongid(songid);
		return r;
	}
	
	//获取歌曲的收听次数
	public static int getListenCount(String songid) throws IOException{
		String url = "http://www.xiami.com/count/getplaycount?id="+songid+"&type=song";
		Document doc = null;
		doc = getHtmlContent(url);
		String res = doc.getElementsByTag("body").get(0).text();
		JSONObject obj = JSONObject.fromObject(res);
		int count = obj.getInt("plays");
		return count;
	}
	//获取歌曲的信息页面
	public static Document getSongInfo(String songid) throws IOException{
		Document doc = null;
		String url = "http://www.xiami.com/song/"+songid;
		doc = getHtmlContent(url);
		return doc;
	}
	public static Document searchSong(String song, String singer) throws IOException{
	  	String searchKey=""; 
    	if(singer=="" && song!=""){
    		searchKey = URLEncoder.encode(song, "utf-8");
    	}else if(song=="" && singer != ""){
    		searchKey = URLEncoder.encode(singer, "utf-8");
    	}else if(singer!="" && song!=""){
    		searchKey = URLEncoder.encode(song, "utf-8")+"+"+URLEncoder.encode(singer,"utf-8");
    	}  
    	String baseUrl = "http://www.xiami.com/search?key=";
		String searchUrl = baseUrl + searchKey;
		Document doc = null;
		doc = getHtmlContent(searchUrl);
		return doc;
	}
	public static Document getHtmlContent(String url) throws IOException {
		Document doc = null;
		//while(true){
			doc = Jsoup.connect(url).ignoreContentType(true)
					.userAgent("Mozilla/5.0 (Windows NT 5.1; zh-CN) AppleWebKit/535.12 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/535.12")
					.timeout(10*1000)
					.get();
			//break;
		//}
		return doc;
	}
	
    public static void write(String data){
    	try{
	      File file =new File("xiami.txt");
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
