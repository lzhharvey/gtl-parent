package com.gtl.fastdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.gtl.utils.FastDFSClient;

public class TestFastDFS {

//	@Test
//	public void uploadFile() throws Exception {
//		//1、向工程中添加jar包
//		//2、创建一个配置文件。配置tracker服务器地址
//		//3、加载配置文件
//		ClientGlobal.init("F:\\电商\\Java SSM淘淘商城12天电商项目（更多视频教程关注微信公众号菜鸟要飞）\\day03_文件上传&富文本框\\source\\gtl-manager-web\\src\\main\\resources\\resource\\client.conf");
//		//4、创建一个TrackerClient对象。
//		TrackerClient trackerClient = new TrackerClient();
//		//5、使用TrackerClient对象获得trackerserver对象。
//		TrackerServer trackerServer = trackerClient.getConnection();
//		//6、创建一个StorageServer的引用null就可以。
//		StorageServer storageServer = null;
//		//7、创建一个StorageClient对象。trackerserver、StorageServer两个参数。
//		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
//		//8、使用StorageClient对象上传文件。
//		String[] strings = storageClient.upload_file("C:/Users/Administrator/Desktop/fish3.jpg", "jpg", null);
//		for (String string : strings) {
//			System.out.println(string);
//
//		}
//	}
	
	@Test
	public void testFastDfsClient() throws Exception {
		FastDFSClient fastDFSClient = new FastDFSClient("F:\\MyProject\\JavaSSM\\day03\\source\\gtl-manager-web\\src\\main\\resources\\resource\\client.conf");
		String string = fastDFSClient.uploadFile("C:\\Users\\Administrator\\Desktop\\fish2.jpg");
		System.out.println(string);
	}
}
