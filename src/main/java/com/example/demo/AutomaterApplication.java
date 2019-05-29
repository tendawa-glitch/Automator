package com.example.demo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AutomaterApplication {

	private final static String URL = "https://mp.weixin.qq.com/s?__biz=MzI0MTAzMjM3Ng==&mid=2651146626&idx=1&sn=c78f22d3a2c770c9b15c4e7d5e055c97&chksm=f2e02455c597ad43fa96214f20b8c2ed046a51befe55ae6a7910633fe694a4b3fa15b7fc2ac2&mpshare=1&scene=1&srcid=0811CP9DnTGJXGIbZkLOcpbZ#rd";
	private final static int totalNumberOfAudios = 125;
	private final static String fileSourceDownloadUrl = "https://res.wx.qq.com/voice/getvoice?mediaid=";

	public static void main(String[] args) throws Exception {
		System.setProperty("webdriver.chrome.driver", "E:\\chromedriver.exe");
		WebDriver webDriver = new ChromeDriver();
		webDriver.get(URL);
		try {
			for (int i = 1; i < totalNumberOfAudios; i++) {
				System.out.println("Link for audio : " + i);
				if (i < 10) {
					navigatePage(String.valueOf(i), webDriver, "00");
				} else if (i < 100) {
					navigatePage(String.valueOf(i), webDriver, "0");
				} else {
					navigatePage(String.valueOf(i), webDriver, "");
				}
			}
		} catch (Exception exception) {
			System.out.println("exception caught : " + exception);
		}
		System.out.println("all files downloaded successfully...");
	}

	private static void navigatePage(String increment, WebDriver webDriver, String element) throws IOException {
		WebDriverWait WebDriverWait = new WebDriverWait(webDriver, 7000);
		// webDriver.findElement(By.partialLinkText(element + increment)).click();
		WebDriverWait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(element + increment))).click();
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, 7000);
			wait.until(ExpectedConditions.elementToBeClickable(By.className("share_audio_title"))).click();
			System.out.println("voice ID  from the URL : " + webDriver.getCurrentUrl().substring(109, 137));
			downloadUsingNIO(fileSourceDownloadUrl + webDriver.getCurrentUrl().substring(109, 137),
					"E:/mp3/audio" + element + increment + ".mp3");
			webDriver.navigate().back();
		} catch (Exception e) {
			e.printStackTrace();
		}
		webDriver.navigate().back();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void downloadUsingNIO(String fileUrl, String file) throws IOException {
		URL url = new URL(fileUrl);
		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		FileOutputStream fos = new FileOutputStream(file);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
		rbc.close();
		System.out.println("file saved successfully...at : " + file);
	}
}
