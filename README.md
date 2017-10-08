# spider-web-etcd-client
## spider-web-etcd-demo的简单客户端

* etcd的配置与spider-web-etcd-demo中相同

* 直接用eclipse

* 原理：


1.先获取一次spider-web节点中存储的服务器ip地址，并输出。

2.持续监听spider-web节点。

3.监听到对spider-web节点的修改，等待10秒（另一服务器spring-boot的启动时间）后，再次获取spider-web节点中存储的服务器ip地址，并输出。

4.返回步2。
