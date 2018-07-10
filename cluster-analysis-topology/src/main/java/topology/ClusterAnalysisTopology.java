package topology;

import bolts.TransInstanceFunction;
import cluster.Kmeans.ClusterModelUpdater;
import cluster.Kmeans.Kmeans;
import org.apache.kafka.common.utils.Utils;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.LocalDRPC;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.kafka.trident.OpaqueTridentKafkaSpout;
import org.apache.storm.kafka.trident.TridentKafkaConfig;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.testing.MemoryMapState;
import org.apache.storm.tuple.Fields;

import java.util.Properties;

public class ClusterAnalysisTopology {

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {

        LocalCluster cluster = new LocalCluster();
        LocalDRPC drpc = new LocalDRPC();

        BrokerHosts brokerHosts = new ZkHosts("120.25.91.38:2181,47.106.144.72:2181");
        TridentKafkaConfig kafkaConfig = new TridentKafkaConfig(brokerHosts, "cluster_analysis_test_0");
        kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        OpaqueTridentKafkaSpout kafkaSpout = new OpaqueTridentKafkaSpout(kafkaConfig);

        final TridentTopology tridentTopology = new TridentTopology();
        final Stream spoutStream = tridentTopology.newStream("kafkaSpout", kafkaSpout).parallelismHint(1);

        TridentState kmeansState = spoutStream.map(new TransInstanceFunction(),new Fields("instance")).parallelismHint(6).partitionPersist(new MemoryMapState.Factory(), new Fields("instance"), new ClusterModelUpdater("kmeans", new Kmeans(3))).parallelismHint(1);
//        TridentState kmeansState = spoutStream.map(new TransInstanceFunction(),new Fields("instance")).parallelismHint(1).partitionPersist(new MemoryMapState.Factory(), new Fields("instance"), new BirchClusterModelUpdater("birch", new BirchClusterIml(3))).parallelismHint(1);

        //storm config
        Config conf = new Config();
        Properties props = new Properties();
        // 配置Kafka broker地址
//        props.put("metadata.broker.list", "localhost:9092");
        props.put("metadata.broker.list", "120.25.91.38:9091");
        // serializer.class为消息的序列化类
        props.put("serializer.class", "kafka.serializer.StringEncoder");
//        props.put("bootstrap.servers", "localhost:9091");
//        conf.put(Config.NIMBUS_HOST,"47.106.144.72"); //配置nimbus连接主机地址，比如：192.168.10.1
//        conf.put(Config.STORM_ZOOKEEPER_SERVERS,"47.106.144.72:2181,47.104.193.8:2181,120.25.91.38:2181"); //配置nimbus连接主机地址，比如：192.168.10.1

//        conf.put(Config.NIMBUS_THRIFT_PORT,6627);//配置nimbus连接端口，默认 6627
        props.put("acks", "1");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        conf.put("kafka.broker.properties", props);
        conf.put("topic", "cluster_analysis_topic");
        System.setProperty("storm.jar","C:\\Users\\jetbi\\IdeaProjects\\毕设\\cluster-analysis\\out\\artifacts\\cluster_analysis_topology_jar\\cluster-analysis-topology.jar");
        //TODO
        tridentTopology.newDRPCStream("instance",drpc);

        StormTopology stormTopology = tridentTopology.build();
        String topologyName = "kafkaTopicTopology";
        cluster.submitTopology(topologyName, conf, stormTopology);
//        StormSubmitter.submitTopology(topologyName, conf, stormTopology);

        Utils.sleep(100000000);
        cluster.killTopology(topologyName);
        cluster.shutdown();
    }

}
