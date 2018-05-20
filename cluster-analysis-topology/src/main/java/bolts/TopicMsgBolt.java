package bolts;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import vo.TmProductDataVO;

public class TopicMsgBolt extends BaseBasicBolt {

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String word = (String) input.getValue(0);
        JSONObject obj = JSON.parseObject(word);
        TmProductDataVO vo= (TmProductDataVO) JSONObject.toJavaObject(obj,vo.TmProductDataVO.class);
        System.out.println(vo);
//        String out = "Message got is '" + word + "'!";
//        System.out.println("out={}"+ out);
        collector.emit(new Values(vo));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("message"));
    }
}