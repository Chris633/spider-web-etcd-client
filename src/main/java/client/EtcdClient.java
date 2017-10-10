package client;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.Watch.Watcher;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.options.WatchOption;

public class EtcdClient {
	//要调用的服务名：spider-web
	private static final ByteSequence KEY = ByteSequence.fromString("spider-web");
	//首页的url
	private static final String APP = ":8081/spider-web/index";
	private static Client client = Client.builder()
			.endpoints("http://192.168.137.100:2379", "http://192.168.137.101:2379", "http://192.168.137.102:2379")
			.build();
	private static KV kvClient = client.getKVClient();

	//此方法与spider-web-etcd-demo中的RegisterOnEtcd.java中的myWatch一样，故不赘述。
	private static void myWatch() {
		Watcher watcher = client.getWatchClient().watch(KEY, WatchOption.newBuilder().withPrevKV(true).build());
		try {
			watcher.listen();
			System.out.println("Node has changed!Please wait 30 second for other nodes boot.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static void open(String uriString) {
		if (java.awt.Desktop.isDesktopSupported()) {
			try {
				java.net.URI uri = java.net.URI.create(uriString);
				java.awt.Desktop dp = java.awt.Desktop.getDesktop();
				if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
					dp.browse(uri);
				}
			} catch (java.lang.NullPointerException e) {
				
			} catch (java.io.IOException e) {
				
			}
		}
	}

	public static void main(String[] args) throws Exception {
		/* 从etcd中取得的数据，类型均为ByteSequence。若要获得字符串应加.toStringUtf8()方法，而不是.toString()
		 * 该demo为取得etcd中存在/spider-web节点下的ip地址，即正在提供服务的IP地址
		 */
		String node = kvClient.get(KEY).get().getKvs().get(0).getValue().toStringUtf8();
		System.out.println("Available address:" + node + APP);
		open("http://" + node + APP);
		System.out.println("Index page has been opened!");
		while (true) {
			//启动服务后，要持续监听/spider-web的变化。
			myWatch();
			//这个等待是为了等其他服务器把自己注册到etcd上。
			Thread.sleep(2000L);
			//获取新注册的服务器对应的ip地址。
			node = kvClient.get(KEY).get().getKvs().get(0).getValue().toStringUtf8();
			System.out.println("Available address:" + node + APP);
			//等待30秒，即spring-boot的启动时间
			Thread.sleep(30000L);
			//用系统默认浏览器打开首页，推荐用Windows系统、默认Chrome浏览器。经测试Edge打不开。
			open("http://" + node + APP);
			System.out.println("Index page has been opened!");
		}

	}

}
