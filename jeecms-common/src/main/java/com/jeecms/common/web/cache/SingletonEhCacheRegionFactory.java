package com.jeecms.common.web.cache;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.ehcache.EhCacheMessageLogger;
import org.hibernate.cache.ehcache.internal.nonstop.NonstopAccessStrategyFactory;
import org.hibernate.cache.ehcache.internal.regions.EhcacheCollectionRegion;
import org.hibernate.cache.ehcache.internal.regions.EhcacheEntityRegion;
import org.hibernate.cache.ehcache.internal.regions.EhcacheNaturalIdRegion;
import org.hibernate.cache.ehcache.internal.regions.EhcacheQueryResultsRegion;
import org.hibernate.cache.ehcache.internal.regions.EhcacheTimestampsRegion;
import org.hibernate.cache.ehcache.internal.strategy.EhcacheAccessStrategyFactory;
import org.hibernate.cache.ehcache.internal.strategy.EhcacheAccessStrategyFactoryImpl;
import org.hibernate.cache.ehcache.internal.util.HibernateEhcacheUtils;
import org.hibernate.cache.ehcache.management.impl.ProviderMBeanRegistrationHelper;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.QueryResultsRegion;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cache.spi.TimestampsRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.service.spi.InjectService;
import org.jboss.logging.Logger;
import org.springframework.util.StringUtils;

import com.jeecms.common.util.PortUtil;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.FactoryConfiguration;
import net.sf.ehcache.config.CacheConfiguration.CacheEventListenerFactoryConfiguration;
import net.sf.ehcache.distribution.RMICacheManagerPeerListenerFactory;
import net.sf.ehcache.distribution.RMICacheManagerPeerProviderFactory;
import net.sf.ehcache.distribution.RMICacheReplicatorFactory;
import net.sf.ehcache.util.PropertyUtil;

/**
 * 扩展 SingletonEhCacheRegionFactory 自动/手动设置EHcache集群
 * 
 * @author: tom
 * @date: 2018年11月23日 下午1:46:58
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class SingletonEhCacheRegionFactory implements RegionFactory {

	private static final long serialVersionUID = -8177979933990834098L;
	private static final String SPT = "/";
	private static final String PEER_DISCOVERY_AUTOMATIC = "automatic";
	private static final String PEER_DISCOVERY_MANUAL = "manual";

	/**
	 * The Hibernate system property specifying the location of the ehcache
	 * configuration file name.
	 * <p/>
	 * If not set, ehcache.xml will be looked for in the root of the classpath.
	 * <p/>
	 * If set to say ehcache-1.xml, ehcache-1.xml will be looked for in the root
	 * of the classpath.
	 */
	public static final String NET_SF_EHCACHE_CONFIGURATION_RESOURCE_NAME = "net.sf.ehcache.configurationResourceName";

	/** 组播缓存提供者广播组地址 */
	public static final String NET_SF_EHCACHE_CONFIGURATION_CLUSTER_MULTICAST_GROUP_ADDRESS = "ehcache.cluster.provider.automatic.multicast-group-address";
	/** 组播缓存提供者广播组端口 */
	public static final String NET_SF_EHCACHE_CONFIGURATION_CLUSTER_MULTICAST_GROUP_PORT = "ehcache.cluster.provider.automatic.multicast-group-port";
	/** Ehcache 集群自动配置 */
	public static final String NET_SF_EHCACHE_CONFIGURATION_CLUSTER_ENABLED = "ehcache.cluster-enabled";
	/** manual手动 automatic自动 */
	public static final String NET_SF_EHCACHE_CONFIGURATION_CLUSTER_PEER_DISCOVERY = "ehcache.cluster.provider.peer-discovery";
	public static final String NET_SF_EHCACHE_CONFIGURATION_CLUSTER_TIME_TO_LIVE = "ehcache.cluster.provider.automatic.time-to-live";
	/** 缓存监听者端口地址 */
	public static final String NET_SF_EHCACHE_CONFIGURATION_CLUSTER_HOSTNAME = "ehcache.cluster.listener.host-name";
	/** 开启随机监听端口 */
	public static final String NET_SF_EHCACHE_CONFIGURATION_CLUSTER_PORT_MANDOM_ENABLED = "ehcache.cluster.listener.random-port-enabled";
	/** 随机端口范围上限 */
	public static final String NET_SF_EHCACHE_CONFIGURATION_CLUSTER_PORT_MAX = "ehcache.cluster.listener.max-random-port";
	/** 随机端口范围下限 */
	public static final String NET_SF_EHCACHE_CONFIGURATION_CLUSTER_PORT_MIN = "ehcache.cluster.listener.min-random-port";
	/** 监听socket连接超时时间 默认2000毫秒 */
	public static final String NET_SF_EHCACHE_CONFIGURATION_CLUSTER_SOCKET_TIMEOUT = "ehcache.cluster.listener.socket-timeout-millis";
	/** Ehcache集群手工配置 缓存提供者需要通知的rmi地址 多个 | 分隔 */
	public static final String NET_SF_EHCACHE_CONFIGURATION_CLUSTER_RMI_UILS = "ehcache.cluster.provider.manual.rmi-urls";
	/** 缓存监听者端口地址 */
	public static final String NET_SF_EHCACHE_CONFIGURATION_CLUSTER_PORT = "ehcache.cluster.listener.port";
	/** 不同步的缓存 */
	public static final String NET_SF_EHCACHE_CONFIGURATION_CLUSTER_UN_SYNC_CACHE_NAME = "ehcache.cluster.unSyncCacheName";

	private String defaultRMICacheReplicatorFactoryPropertiesName = "replicatePuts=true,replicateUpdates=true,replicateRemovals=true,"
			+ "replicateAsynchronously=true,replicatePutsViaCopy=true,"
			+ "replicateUpdatesViaCopy=true,asynchronousReplicationIntervalMillis=3000";

	private static final EhCacheMessageLogger LOG = Logger.getMessageLogger(EhCacheMessageLogger.class,
			SingletonEhCacheRegionFactory.class.getName());

	private static final AtomicInteger REFERENCE_COUNT = new AtomicInteger();

	public ClusterEhCacheConfig ehClusterConfig;

	/**
	 * 创建 ClusterEhCacheConfig
	 * 
	 * @Title: createEhCacheConfigBean
	 * @param properties
	 *            Properties
	 * @return: ClusterEhCacheConfig
	 */
	public ClusterEhCacheConfig createEhCacheConfigBean(Properties properties) {
		Boolean enableCluster = Boolean.valueOf((String) properties.get(NET_SF_EHCACHE_CONFIGURATION_CLUSTER_ENABLED));
		String peerDiscovery = (String) properties.get(NET_SF_EHCACHE_CONFIGURATION_CLUSTER_PEER_DISCOVERY);
		String multicastGroupAddress = (String) properties
				.get(NET_SF_EHCACHE_CONFIGURATION_CLUSTER_MULTICAST_GROUP_ADDRESS);
		Integer multicastGroupPort = Integer
				.parseInt(((String) properties.get(NET_SF_EHCACHE_CONFIGURATION_CLUSTER_MULTICAST_GROUP_PORT)).trim());
		String rmiUrls = (String) properties.get(NET_SF_EHCACHE_CONFIGURATION_CLUSTER_RMI_UILS);
		Boolean randomPort = Boolean
				.valueOf((String) properties.get(NET_SF_EHCACHE_CONFIGURATION_CLUSTER_PORT_MANDOM_ENABLED));
		String hostName = (String) properties.get(NET_SF_EHCACHE_CONFIGURATION_CLUSTER_HOSTNAME);
		Integer minRandomPort = Integer
				.parseInt(((String) properties.get(NET_SF_EHCACHE_CONFIGURATION_CLUSTER_PORT_MIN)).trim());
		Integer maxRandomPort = Integer
				.parseInt(((String) properties.get(NET_SF_EHCACHE_CONFIGURATION_CLUSTER_PORT_MAX)).trim());
		String unSyncCacheName = (String) properties.get(NET_SF_EHCACHE_CONFIGURATION_CLUSTER_UN_SYNC_CACHE_NAME);

		Object clusterPort = properties.get(NET_SF_EHCACHE_CONFIGURATION_CLUSTER_PORT);
		Integer port = 40001;
		if (clusterPort != null) {
			port = Integer.parseInt(((String) clusterPort).trim());
		}
		ehClusterConfig = new ClusterEhCacheConfig(enableCluster, unSyncCacheName, peerDiscovery, multicastGroupAddress,
				multicastGroupPort, rmiUrls, randomPort, hostName, port, minRandomPort, maxRandomPort);
		return ehClusterConfig;
	}

	/**
	 * Constructs a SingletonEhCacheRegionFactory
	 */
	public SingletonEhCacheRegionFactory() {
	}

	/**
	 * Constructs a SingletonEhCacheRegionFactory
	 *
	 * @param prop
	 *            Not used
	 */
	public SingletonEhCacheRegionFactory(Properties prop) {
		super();
	}

	@Override
	public void start(SessionFactoryOptions settings, Properties properties) throws CacheException {
		this.settings = settings;
		if (properties != null) {
			createEhCacheConfigBean(properties);
		}
		try {
			String configurationResourceName = null;
			if (properties != null) {
				configurationResourceName = (String) properties.get(NET_SF_EHCACHE_CONFIGURATION_RESOURCE_NAME);
			}
			if (configurationResourceName == null || configurationResourceName.length() == 0) {
				manager = CacheManager.create();
				REFERENCE_COUNT.incrementAndGet();
			} else {
				URL url;
				try {
					url = new URL(configurationResourceName);
				} catch (MalformedURLException e) {
					if (!configurationResourceName.startsWith(SPT)) {
						configurationResourceName = SPT + configurationResourceName;
						LOG.debugf("prepending / to %s. It should be placed in the "
								+ "root of the classpath rather than in a package.", configurationResourceName);
					}
					url = loadResource(configurationResourceName);
				}
				Configuration configuration = HibernateEhcacheUtils.loadAndCorrectConfiguration(url);

				if (this.ehClusterConfig.getEnableCluster()) {
					configCacheManagerPeerProviderFactory(configuration);
					configCacheManagerPeerListenerFactory(configuration);
					configCacheEventListenerFactory(configuration);
				}
				manager = CacheManager.create(configuration);
				REFERENCE_COUNT.incrementAndGet();
			}
			mbeanRegistrationHelper.registerMBean(manager, properties);
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException(e);
		}
	}

	@Override
	public void stop() {
		try {
			if (manager != null) {
				if (REFERENCE_COUNT.decrementAndGet() == 0) {
					manager.shutdown();
				}
				manager = null;
			}
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException(e);
		}
	}

	/**
	 * 配置缓存管理注册提供者工厂
	 * 
	 * @Title: configCacheManagerPeerProviderFactory
	 * @param configuration
	 *            Configuration
	 */
	public void configCacheManagerPeerProviderFactory(Configuration configuration) {
		LOG.debug("ehClusterConfig:" + ehClusterConfig.toString());
		String peerDiscovery = ehClusterConfig.getPeerDiscovery();
		LOG.debug("rmi方式：" + peerDiscovery);
		if (StringUtils.isEmpty(peerDiscovery)) {
			return;
		}
		if (PEER_DISCOVERY_AUTOMATIC.equals(peerDiscovery)) {
			configAutomaticCacheManagerPeerProviderFactory(configuration);
		} else if (PEER_DISCOVERY_MANUAL.equals(peerDiscovery)) {
			configManualCacheManagerPeerProviderFactory(configuration);
		} else {
			LOG.error("invalid peerDiscovery：" + peerDiscovery);
			return;
		}
	}

	/**
	 * 配置缓存管理手工注册提供者工厂
	 * 
	 * @Title: configManualCacheManagerPeerProviderFactory
	 * @param configuration
	 *            Configuration
	 */
	private void configManualCacheManagerPeerProviderFactory(Configuration configuration) {
		String rmiUrls = ehClusterConfig.getRmiUrls().trim();
		StringTokenizer stringTokenizer = new StringTokenizer(rmiUrls, "|");
		Set<String> cacheConfigurationsKeySet = configuration.getCacheConfigurationsKeySet();
		StringBuilder rmiUrlsStr = new StringBuilder();
		while (stringTokenizer.hasMoreTokens()) {
			String rmiUrl = stringTokenizer.nextToken();
			rmiUrl = rmiUrl.trim();
			for (String key : cacheConfigurationsKeySet) {
				rmiUrlsStr.append("//").append(rmiUrl).append("/").append(key).append("|");
			}
		}
		rmiUrlsStr = rmiUrlsStr.deleteCharAt(rmiUrlsStr.length() - 1);
		LOG.debug("last rmiUrls：" + rmiUrlsStr.toString());
		configuration.cacheManagerPeerProviderFactory(new FactoryConfiguration<FactoryConfiguration<?>>()
				.className(RMICacheManagerPeerProviderFactory.class.getName())
				.properties("peerDiscovery=manual,rmiUrls=" + rmiUrlsStr));
	}

	/**
	 * 配置缓存管理自动注册提供者工厂
	 * 
	 * @Title: configAutomaticCacheManagerPeerProviderFactory
	 * @param configuration
	 *            Configuration
	 */
	private void configAutomaticCacheManagerPeerProviderFactory(Configuration configuration) {
		configuration.cacheManagerPeerProviderFactory(new FactoryConfiguration<FactoryConfiguration<?>>()
				.className(RMICacheManagerPeerProviderFactory.class.getName())
				.properties("peerDiscovery=automatic,multicastGroupAddress="
						+ ehClusterConfig.getMulticastGroupAddress().trim() + ",multicastGroupPort="
						+ ehClusterConfig.getMulticastGroupPort() + ",timeToLive=32"));
	}

	/**
	 * 配置缓存同步管理监听工厂
	 * 
	 * @Title: configCacheManagerPeerListenerFactory
	 * @param configuration
	 *            Configuration
	 */
	private void configCacheManagerPeerListenerFactory(Configuration configuration) {
		configuration.cacheManagerPeerListenerFactory(new FactoryConfiguration<FactoryConfiguration<?>>()
				.className(RMICacheManagerPeerListenerFactory.class.getName())
				.properties("hostName=" + ehClusterConfig.getHostName().trim() + ",port=" + ehClusterConfig.getPort()
						+ ",socketTimeoutMillis=2000"));
	}

	/**
	 * 配置缓存监听工厂
	 * 
	 * @Title: configCacheEventListenerFactory
	 * @param configuration
	 *            Configuration
	 */
	private void configCacheEventListenerFactory(Configuration configuration) {
		Properties defaultRMICacheReplicatorFactoryProperties = PropertyUtil
				.parseProperties(defaultRMICacheReplicatorFactoryPropertiesName, ",");
		Map<String, CacheConfiguration> cacheConfigurations = configuration.getCacheConfigurations();

		Set<String> filterNoSyncCache = filterNoSyncCache(configuration.getCacheConfigurationsKeySet());

		for (String key : filterNoSyncCache) {

			CacheConfiguration cacheConfiguration = cacheConfigurations.get(key);

			boolean hasRMICacheReplicatorFactory = false;
			@SuppressWarnings("unchecked")
			List<CacheEventListenerFactoryConfiguration> cacheEventListenerConfigurations = cacheConfiguration
					.getCacheEventListenerConfigurations();
			// 已经配置缓存监听情况
			if (cacheEventListenerConfigurations != null && !cacheEventListenerConfigurations.isEmpty()) {
				for (CacheEventListenerFactoryConfiguration c1 : cacheEventListenerConfigurations) {
					String className = c1.getFullyQualifiedClassPath();
					if (className.equals(RMICacheReplicatorFactory.class.getName())) {
						hasRMICacheReplicatorFactory = true;
						Properties parseProperties = PropertyUtil.parseProperties(c1.getProperties(),
								c1.getPropertySeparator());
						// 属性为空 设置默认
						if (parseProperties == null) {
							c1.properties(defaultRMICacheReplicatorFactoryPropertiesName);
							continue;
						}
						// 属性不为空 ，和默认属性合并
						Enumeration<?> propertyNames = parseProperties.propertyNames();
						while (propertyNames.hasMoreElements()) {
							String key1 = (String) propertyNames.nextElement();
							String property = parseProperties.getProperty(key1);
							if (StringUtils.hasText(property)) {
								defaultRMICacheReplicatorFactoryProperties.put(key1, property);
							}
						}
						// 重新设置合并后的属性
						Enumeration<?> propertyNames1 = defaultRMICacheReplicatorFactoryProperties.propertyNames();
						StringBuilder sb = new StringBuilder();
						while (propertyNames1.hasMoreElements()) {
							String key1 = (String) propertyNames1.nextElement();
							String property = defaultRMICacheReplicatorFactoryProperties.getProperty(key1);
							sb.append(key1).append("=").append(property).append(",");
						}
						c1.setProperties(sb.substring(0, sb.length() - 1).toString());
					}
				}
			}
			// 未配置缓存监听情况 设置默认RMICacheReplicatorFactory即属性
			if (!hasRMICacheReplicatorFactory) {
				CacheEventListenerFactoryConfiguration cacheEventListenerFactoryConfiguration = new CacheEventListenerFactoryConfiguration();
				cacheEventListenerFactoryConfiguration.className(RMICacheReplicatorFactory.class.getName());
				cacheEventListenerFactoryConfiguration.properties(defaultRMICacheReplicatorFactoryPropertiesName);
				cacheConfiguration.addCacheEventListenerFactory(cacheEventListenerFactoryConfiguration);
			}

		}
	}

	/**
	 * 过滤掉不同步的缓存key 返回类型 过滤后的缓存配置key
	 * 
	 * @Title: filterNoSyncCache
	 * @param cacheConfigurationsKeySet
	 *            Set
	 * @return
	 */
	private Set<String> filterNoSyncCache(Set<String> cacheConfigurationsKeySet) {
		Set<String> filteredCacheConfigurationKeySet = new HashSet<String>();
		filteredCacheConfigurationKeySet.addAll(cacheConfigurationsKeySet);
		String unSyncCacheName = ehClusterConfig.getUnSyncCacheName();
		StringTokenizer unSyncCacheNameTokenizer = new StringTokenizer(unSyncCacheName, "|");
		while (unSyncCacheNameTokenizer.hasMoreTokens()) {
			String nextToken = unSyncCacheNameTokenizer.nextToken();
			for (String key : cacheConfigurationsKeySet) {
				if (key.equals(nextToken)) {
					filteredCacheConfigurationKeySet.remove(nextToken);
					continue;
				}
			}
		}
		return filteredCacheConfigurationKeySet;
	}

	public class ClusterEhCacheConfig {

		/**
		 * 是否启用集群
		 */
		private boolean enableCluster;

		/**
		 * 无需同步的缓存名称 |分隔
		 */
		private String unSyncCacheName;

		/**
		 * 缓存提供者rmi通信方式 自动和手动
		 */
		private String peerDiscovery;

		/**
		 * 缓存提供者广播组地址
		 */
		private String multicastGroupAddress;

		/**
		 * 缓存提供者广播组端口
		 */
		private int multicastGroupPort;

		/**
		 * 缓存提供者需要通知的rmi地址
		 */
		private String rmiUrls;

		/**
		 * 缓存监听者端口是否随机 （自动配置时生效）
		 */
		private boolean randomPort;

		/**
		 * 缓存监听者端口地址
		 */
		private String hostName;

		/**
		 * 缓存监听者默认端口
		 */
		private int port;

		/**
		 * 随机最下端口号
		 */
		private int minRandomPort;

		/**
		 * 随机最下端口号
		 */
		private int maxRandomPort;

		/**
		 * 初始化端口
		 * 
		 * @Title: init
		 * @return: void
		 */
		public void init() {
			if (randomPort) {
				// 41000 默认广播随机端口号 不能随机
				PortUtil p = new PortUtil();
				// 42000 -43000监听随机端口号
				int ramPort = p.getUnAvailableRandomPort(minRandomPort, maxRandomPort);
				/** 未成功获取到随机端口 */
				if (ramPort == 0) {
					ramPort = port;
				}
				port = ramPort;
			}
		}

		public String getPeerDiscovery() {
			return peerDiscovery;
		}

		public void setPeerDiscovery(String peerDiscovery) {
			this.peerDiscovery = peerDiscovery;
		}

		public String getMulticastGroupAddress() {
			return multicastGroupAddress;
		}

		public void setMulticastGroupAddress(String multicastGroupAddress) {
			this.multicastGroupAddress = multicastGroupAddress;
		}

		public int getMulticastGroupPort() {
			return multicastGroupPort;
		}

		public void setMulticastGroupPort(int multicastGroupPort) {
			this.multicastGroupPort = multicastGroupPort;
		}

		public String getRmiUrls() {
			return rmiUrls;
		}

		public void setRmiUrls(String rmiUrls) {
			this.rmiUrls = rmiUrls;
		}

		public String getHostName() {
			return hostName;
		}

		public void setHostName(String hostName) {
			this.hostName = hostName;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public boolean getRandomPort() {
			return randomPort;
		}

		public void setRandomPort(boolean randomPort) {
			this.randomPort = randomPort;
		}

		public boolean getEnableCluster() {
			return enableCluster;
		}

		public void setEnableCluster(boolean enableCluster) {
			this.enableCluster = enableCluster;
		}

		public String getUnSyncCacheName() {
			return unSyncCacheName;
		}

		public void setUnSyncCacheName(String unSyncCacheName) {
			this.unSyncCacheName = unSyncCacheName;
		}

		@Override
		public String toString() {
			return "EhClusterConfig [enableCluster=" + enableCluster + ", unSyncCacheName=" + unSyncCacheName
					+ ",randomPort=" + randomPort + ", peerDiscovery=" + peerDiscovery + ", multicastGroupAddress="
					+ multicastGroupAddress + ", multicastGroupPort=" + multicastGroupPort + ", rmiUrls=" + rmiUrls
					+ ", hostName=" + hostName + ", port=" + port + "]";
		}

		public ClusterEhCacheConfig() {
			super();
		}

		/**
		 * 构造器
		 * 
		 * @param enableCluster
		 *            是否集群
		 * @param unSyncCacheName
		 *            不同步的缓存区块名 逗号,分隔
		 * @param peerDiscovery
		 *            节点发现模式支持manual手动 automatic 自动两种模式
		 * @param multicastGroupAddress
		 *            组播缓存提供者广播组地址
		 * @param multicastGroupPort
		 *            组播端口
		 * @param rmiUrls
		 *            Ehcache集群手工配置 缓存提供者需要通知的rmi地址 多个 | 分隔
		 * @param randomPort
		 *            是否动态端口
		 * @param hostName
		 *            缓存监听者端口地址
		 * @param port
		 *            缓存监听者端口地址
		 * @param minRandomPort
		 *            动态端口随机最小值
		 * @param maxRandomPort
		 *            动态端口随机最大值
		 */
		public ClusterEhCacheConfig(boolean enableCluster, String unSyncCacheName, String peerDiscovery,
				String multicastGroupAddress, int multicastGroupPort, String rmiUrls, boolean randomPort,
				String hostName, int port, int minRandomPort, int maxRandomPort) {
			super();
			this.enableCluster = enableCluster;
			this.unSyncCacheName = unSyncCacheName;
			this.peerDiscovery = peerDiscovery;
			this.multicastGroupAddress = multicastGroupAddress;
			this.multicastGroupPort = multicastGroupPort;
			this.rmiUrls = rmiUrls;
			this.randomPort = randomPort;
			this.hostName = hostName;
			this.port = port;
			this.minRandomPort = minRandomPort;
			this.maxRandomPort = maxRandomPort;
			init();
		}

	}

	/**
	 * MBean registration helper class instance for Ehcache Hibernate MBeans.
	 */
	protected final ProviderMBeanRegistrationHelper mbeanRegistrationHelper = new ProviderMBeanRegistrationHelper();

	/**
	 * Ehcache CacheManager that supplied Ehcache instances for this Hibernate
	 * RegionFactory.
	 */
	protected volatile CacheManager manager;

	/**
	 * Settings object for the Hibernate persistence unit.
	 */
	protected SessionFactoryOptions settings;

	/**
	 * {@link EhcacheAccessStrategyFactory} for creating various access
	 * strategies
	 */
	protected final EhcacheAccessStrategyFactory accessStrategyFactory = new NonstopAccessStrategyFactory(
			new EhcacheAccessStrategyFactoryImpl());

	/**
	 * {@inheritDoc}
	 * <p/>
	 * In Ehcache we default to minimal puts since this should have minimal to
	 * no affect on unclustered users, and has great benefit for clustered
	 * users.
	 *
	 * @return true, optimize for minimal puts
	 */
	@Override
	public boolean isMinimalPutsEnabledByDefault() {

		return true;
	}

	@Override
	public long nextTimestamp() {
		return net.sf.ehcache.util.Timestamper.next();
	}

	@Override
	public EntityRegion buildEntityRegion(String regionName, Properties properties, CacheDataDescription metadata)
			throws CacheException {
		return new EhcacheEntityRegion(accessStrategyFactory, getCache(regionName), settings, metadata, properties);
	}

	@Override
	public NaturalIdRegion buildNaturalIdRegion(String regionName, Properties properties, CacheDataDescription metadata)
			throws CacheException {
		return new EhcacheNaturalIdRegion(accessStrategyFactory, getCache(regionName), settings, metadata, properties);
	}

	@Override
	public CollectionRegion buildCollectionRegion(String regionName, Properties properties,
			CacheDataDescription metadata) throws CacheException {
		return new EhcacheCollectionRegion(accessStrategyFactory, getCache(regionName), settings, metadata, properties);
	}

	@Override
	public QueryResultsRegion buildQueryResultsRegion(String regionName, Properties properties) throws CacheException {
		return new EhcacheQueryResultsRegion(accessStrategyFactory, getCache(regionName), properties);
	}

	@InjectService
	public void setClassLoaderService(ClassLoaderService classLoaderService) {
		this.classLoaderService = classLoaderService;
	}

	private ClassLoaderService classLoaderService;

	@Override
	public TimestampsRegion buildTimestampsRegion(String regionName, Properties properties) throws CacheException {
		return new EhcacheTimestampsRegion(accessStrategyFactory, getCache(regionName), properties);
	}

	private Ehcache getCache(String name) throws CacheException {
		try {
			Ehcache cache = manager.getEhcache(name);
			if (cache == null) {
				LOG.unableToFindEhCacheConfiguration(name);
				manager.addCache(name);
				cache = manager.getEhcache(name);
				LOG.debug("started EHCache region: " + name);
			}
			return cache;
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException(e);
		}

	}

	/**
	 * Load a resource from the classpath.
	 */
	protected URL loadResource(String configurationResourceName) {
		URL url = null;
		if (classLoaderService != null) {
			url = classLoaderService.locateResource(configurationResourceName);
		}
		if (url == null) {
			final ClassLoader standardClassloader = Thread.currentThread().getContextClassLoader();
			if (standardClassloader != null) {
				url = standardClassloader.getResource(configurationResourceName);
			}
			if (url == null) {
				url = SingletonEhCacheRegionFactory.class.getResource(configurationResourceName);
			}
			if (url == null) {
				try {
					url = new URL(configurationResourceName);
				} catch (MalformedURLException e) {
					// ignore
				}
			}
		}
		if (LOG.isDebugEnabled()) {
			LOG.debugf("Creating EhCacheRegionFactory from a specified resource: %s.  Resolved to URL: %s",
					configurationResourceName, url);
		}
		if (url == null) {

			LOG.unableToLoadConfiguration(configurationResourceName);
		}
		return url;
	}

	/**
	 * Default access-type used when the configured using JPA 2.0 config. JPA
	 * 2.0 allows <code>@Cacheable(true)</code> to be attached to an entity
	 * without any access type or usage qualification.
	 * <p/>
	 * We are conservative here in specifying {@link AccessType#READ_WRITE} so
	 * as to follow the mantra of "do no harm".
	 * <p/>
	 * This is a Hibernate 3.5 method.
	 */
	@Override
	public AccessType getDefaultAccessType() {
		return AccessType.READ_WRITE;
	}

}
