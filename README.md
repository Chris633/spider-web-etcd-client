# spider-web-etcd-client
## spider-web-etcd-demo的简单客户端

* etcd的配置与spider-web-etcd-demo中相同

* 直接用eclipse打开并运行EtcdClient.java即可。

* 原理：


1.先获取一次spider-web节点中存储的服务器ip地址，并输出。

2.持续监听spider-web节点。

3.监听到对spider-web节点的修改，等待10秒（另一服务器spring-boot的启动时间）后，再次获取spider-web节点中存储的服务器ip地址，并输出。

4.返回步2。

**Note:**

    我的配置方法是
   
    1.开三个Vmware Ubuntu虚拟机。将其ip依次改为:192.168.137.100、192.168.137.101、192.168.137.102。
    并分别启动etcd与spider-web-etcd-demo
    (详见[spider-web-etcd-demo](https://github.com/Chris633/spider-web-etcd-demo))
    
    2.在本机运行spider-web-etcd-client中的EtcdClient.java
    
    3.查看输出结果。当提供spider-web服务的服务器更换时，会有如下输出。
   <pre>
Node has changed!Please wait 30 second for other nodes boot.
Available address:192.168.137.102:8081/spider-web/index
Index page has benn opened!
 </pre>
 
 意为
 
 1.监听到spider-web结点发生变化，其他虚拟机启动spring-boot需要10-20秒所以要等待30秒才能提供服务。
 2.输出可用的服务器地址
 3.用本机默认浏览器打开http://192.168.137.102:8081/spider-web/index
