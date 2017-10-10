package client;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.Watch.Watcher;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.options.WatchOption;

public class EtcdClient {

	private static final ByteSequence KEY = ByteSequence.fromString("spider-web");
	private static final String APP = ":8081/spider-web/index";
	private static Client client = Client.builder()
			.endpoints("http://192.168.137.100:2379", "http://192.168.137.101:2379", "http://192.168.137.102:2379")
			.build();
	private static KV kvClient = client.getKVClient();

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
		String node = kvClient.get(KEY).get().getKvs().get(0).getValue().toStringUtf8();
		System.out.println("Available address:" + node + APP);
		open("http://" + node + APP);
		System.out.println("Index page has benn opened!");
		while (true) {
			myWatch();
			Thread.sleep(2000L);
			node = kvClient.get(KEY).get().getKvs().get(0).getValue().toStringUtf8();
			System.out.println("Available address:" + node + APP);
			Thread.sleep(30000L);
			open("http://" + node + APP);
			System.out.println("Index page has benn opened!");
		}

	}

}
