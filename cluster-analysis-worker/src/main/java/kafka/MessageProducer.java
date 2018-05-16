package kafka;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author jet.shi
 */

@Component
public class MessageProducer {

    protected final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void test(){

    }


}
