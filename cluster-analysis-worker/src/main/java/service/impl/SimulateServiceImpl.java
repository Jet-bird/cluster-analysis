package service.impl;

import com.alibaba.fastjson.JSONObject;
import dao.TmProductDataDao;
import kafka.KafkaUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import service.SimulateService;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author jet.shi
 */

@Service("simulateServiceImpl")
@ConfigurationProperties(prefix = "kafka")
public class SimulateServiceImpl implements SimulateService{

    protected final Logger logger = LogManager.getLogger(this.getClass());

    @Value("${kafka.host}")
    String host;

    @Autowired
    private TmProductDataDao tmProductDataDao;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${kafka.simulateServiceTopicName}")
    private String topicName;

    public  void sendData(){
        int start = tmProductDataDao.selectMinId();
        int end = tmProductDataDao.selectMaxId();
        logger.info("start sending message,num:{}",end-start+1);
        long starTime = System.currentTimeMillis();
        int step = 1000;
        try{
            for(;;){
                if(start > end){
                    logger.info("end sending message");
                    break;
                }
                for(int i = start;i<start+step;i++){
                    String msg  = JSONObject.toJSONString(tmProductDataDao.getById(i));
                    kafkaTemplate.send(topicName,msg);
                }
                start = start+step+1;
            }
        }catch (Exception e){
            logger.error("send message error:{}",e.getMessage());
        }
        logger.info("end sending message,costTime:{}",System.currentTimeMillis()-starTime);
    }

    @Override
    public void createTopic(String topic) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        try (AdminClient client = AdminClient.create(props)){
            KafkaUtils.createTopics(client,topic);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
