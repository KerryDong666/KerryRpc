package com.kerry.rpc.registry;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * 服务注册 ，ZK 在该架构中扮演了“服务注册表”的角色，用于注册所有服务器的地址与端口，并对客户端提供服务发现的功能
 *
 * @author Kerry Dong
 */
public class ServiceRegistry {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);

	private CountDownLatch latch = new CountDownLatch(1);

	private String registryAddress;

	public ServiceRegistry(String registryAddress) {
		//zookeeper的地址
		this.registryAddress = registryAddress;
	}

	/**
	 * 创建zookeeper链接
	 *
	 * @param data
	 */
	public void register(String data) {
		if (StringUtils.isNotBlank(data)) {
			ZooKeeper zk = connectServer();
			if (zk != null) {
				createNode(zk, data);
			}
		}
	}

	/**
	 * 创建zookeeper链接，监听
	 *
	 * @return
	 */
	private ZooKeeper connectServer() {
		ZooKeeper zk = null;
		try {
			zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT,
					//判断ZK连接是否完成
					new Watcher() {
						@Override
						public void process(WatchedEvent event) {
							if (event.getState() == Event.KeeperState.SyncConnected) {
								latch.countDown();
								LOGGER.info("-------------zk connect success!---------------");
							}
						}
					});
			latch.await();
		} catch (Exception e) {
			LOGGER.error("zk connect failed", e);
		}
		return zk;
	}

	/**
	 * 创建节点
	 *
	 * @param zk
	 * @param data
	 */
	private void createNode(ZooKeeper zk, String data) {
		try {
			byte[] bytes = data.getBytes();
			if (zk.exists(Constant.ZK_REGISTRY_PATH, null) == null) {
				zk.create(Constant.ZK_REGISTRY_PATH, null, Ids.OPEN_ACL_UNSAFE,
						CreateMode.PERSISTENT);
			}
			//CreateMode.EPHEMERAL_SEQUENTIAL 模式表示临时有序列的节点,同一个业务可以启动多个服务
			String path = zk.create(Constant.ZK_DATA_PATH, bytes,
					Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			LOGGER.debug("create zookeeper node ({} => {})", path, data);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}
}