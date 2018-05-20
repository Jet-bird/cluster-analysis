package bolts;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import core.Instance;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;
import vo.TmProductDataVO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TransMsgFunction extends BaseFunction {

    private static final long serialVersionUID = 2242376024410720639L;

    protected Boolean withLabel = false;

    public TransMsgFunction(){}

    public TransMsgFunction( boolean withLabel ){
        this.withLabel = withLabel;
    }

    @Override
    public void execute(TridentTuple tridentTuple, TridentCollector collector) {
        if( tridentTuple == null ){
            return;
        }
        Instance<Integer> instance = this.createInstance( tridentTuple );
        Values values = new Values( instance );
        collector.emit( values);
    }

    /**
     * create an instance from trident tuple.
     * @param tridentTuple
     * @return
     */
    protected Instance<Integer> createInstance(TridentTuple tridentTuple) {
        Instance<Integer> instance = null;
        String word = (String) tridentTuple.getValue(0);
        JSONObject obj = JSON.parseObject(word);
        TmProductDataVO vo= (TmProductDataVO) JSONObject.toJavaObject(obj,vo.TmProductDataVO.class);
        double[] features = getFeatures(vo);
        if( this.withLabel == true){
            Integer label = vo.getId();
            System.out.println(label);
            try {
                writeFile(label.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            instance = new Instance<Integer>(label,features);
        }else{
            instance = new Instance<Integer>(features);
        }
        return instance;
    }

    protected double[] getFeatures(TmProductDataVO vo){
        double[] features = new double[3];
        if(vo.getCommentNum() == 0){
            return features;
        }
        double goodRate = vo.getGoodCommentNum()/vo.getCommentNum();
        double midRate = vo.getMidCommentNum()/vo.getCommentNum();
        double badRate = vo.getBadCommentNum()/vo.getCommentNum();
        features[0] = goodRate;
        features[1] = midRate;
        features[2] = badRate;
        return features;
    }

    private void writeFile(String txt) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File("D:\\add.txt"));
            long begin = System.currentTimeMillis();
                out.write(txt.getBytes());
        }catch (Exception exception){

        }finally {
            if(out!=null){
                out.close();
            }
        }
    }
}
