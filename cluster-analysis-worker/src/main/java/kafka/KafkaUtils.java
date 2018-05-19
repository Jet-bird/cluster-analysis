package kafka;

import kafka.admin.AdminUtils;
import kafka.utils.ZkUtils;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.security.JaasUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author jet.shi
 * KafkaUtils provides functions to operate kafka cluster.
 */
public class KafkaUtils {

    protected final static Logger logger = LogManager.getLogger(KafkaUtils.class);

    public static void createTopic(String topic, int partionNum, int repilca, Properties properties) {

        ZkUtils zkUtils = null;
        try {
            String ZK_CONNECT = properties.getProperty("ZK_CONNECT");
            int SESSION_TIMEOUT = Integer.parseInt(properties.getProperty("SESSION_TIMEOUT"));
            int CONNECT_TIMEOUT = Integer.parseInt(properties.getProperty("CONNECT_TIMEOUT"));
            zkUtils = ZkUtils.apply(ZK_CONNECT,SESSION_TIMEOUT,CONNECT_TIMEOUT, JaasUtils.isZkSecurityEnabled());
            if(!AdminUtils.topicExists(zkUtils,topic)){
                AdminUtils.createTopic(zkUtils,topic,partionNum,repilca,properties,AdminUtils.getBrokerMetadatas$default$2());
            }else{
                //TODO 打印日志
               logger.info("Topic {} exists",topic);
            }
        }catch (Exception e){
            logger.info("Create topic {} error:{}",topic,e.getMessage());
        }finally{
            zkUtils.close();
        }
    }

    /**
     * describe the cluster
     * @param client
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void describeCluster(AdminClient client) throws ExecutionException, InterruptedException {
        DescribeClusterResult ret = client.describeCluster();
        System.out.println(String.format("Cluster id: %s, controller: %s", ret.clusterId().get(), ret.controller().get()));
        System.out.println("Current cluster nodes info: ");
        for (Node node : ret.nodes().get()) {
            logger.info(node);
        }
    }

    /**
     * describe topic's config
     * @param client
     */
    private static void describeConfig(AdminClient client,String topicName) throws ExecutionException, InterruptedException {
        DescribeConfigsResult ret = client.describeConfigs(Collections.singleton(new ConfigResource(ConfigResource.Type.TOPIC, topicName)));
        Map<ConfigResource, Config> configs = ret.all().get();
        for (Map.Entry<ConfigResource, Config> entry : configs.entrySet()) {
            ConfigResource key = entry.getKey();
            Config value = entry.getValue();
            logger.info(String.format("Resource type: %s, resource name: %s", key.type(), key.name()));
            Collection<ConfigEntry> configEntries = value.entries();
            for (ConfigEntry each : configEntries) {
                logger.info(each.name() + " = " + each.value());
            }
        }

    }

    /**
     * alter config for topics
     * @param client
     */
    public static void alterConfigs(AdminClient client,String topicName) throws ExecutionException, InterruptedException {
        Config topicConfig = new Config(Arrays.asList(new ConfigEntry("cleanup.policy", "compact")));
        client.alterConfigs(Collections.singletonMap(
                new ConfigResource(ConfigResource.Type.TOPIC, topicName), topicConfig)).all().get();
    }

    /**
     * delete the given topics
     * @param client
     */
    private static void deleteTopics(AdminClient client,String topicName) throws ExecutionException, InterruptedException {
        KafkaFuture<Void> futures = client.deleteTopics(Arrays.asList(topicName)).all();
        futures.get();
    }

    /**
     * describe the given topics
     * @param client
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void describeTopics(AdminClient client,String topicName) throws ExecutionException, InterruptedException {
        DescribeTopicsResult ret = client.describeTopics(Arrays.asList(topicName, "__consumer_offsets"));
        Map<String, TopicDescription> topics = ret.all().get();
        for (Map.Entry<String, TopicDescription> entry : topics.entrySet()) {
            logger.info(entry.getKey() + " ===> " + entry.getValue());
        }
    }

    /**
     * create multiple sample topics
     * @param client
     */
    public static void createTopics(AdminClient client,String topicName) throws ExecutionException, InterruptedException {
        NewTopic newTopic = new NewTopic(topicName, 1, (short)1);
        CreateTopicsResult ret = client.createTopics(Arrays.asList(newTopic));
        ret.all().get();
    }

    /**
     * print all topics in the cluster
     * @param client
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void listAllTopics(AdminClient client) throws ExecutionException, InterruptedException {
        ListTopicsOptions options = new ListTopicsOptions();
        options.listInternal(true); // includes internal topics such as __consumer_offsets
        ListTopicsResult topics = client.listTopics(options);
        Set<String> topicNames = topics.names().get();
        logger.info("Current topics in this cluster: " + topicNames);
    }

}
