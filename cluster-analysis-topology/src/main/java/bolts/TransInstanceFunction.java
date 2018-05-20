package bolts;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import core.Instance;
import org.apache.storm.trident.operation.MapFunction;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;
import vo.TmProductDataVO;

import java.util.function.Function;

public class TransInstanceFunction implements MapFunction {

    protected Boolean withLabel = false;

    public TransInstanceFunction(){}

    public TransInstanceFunction( boolean withLabel ){
        this.withLabel = withLabel;
    }
    @Override
    public Values execute(TridentTuple input) {
        if( input == null ){
            return null;
        }
        Instance<Integer> instance = this.createInstance( input );
        Values values = new Values( instance );
        return values;
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
            instance = new Instance<Integer>(label,features);
        }else{
            instance = new Instance<Integer>(features);
        }
        return instance;
    }

    protected double[] getFeatures(TmProductDataVO vo){
        double[] features = new double[3];
        Function<Integer,Integer> ifNull = new Function() {
            @Override
            public Object apply(Object o) {
                if(o == null){
                    return 0;
                }else
                    return o;
            }
        };
        if(vo.getCommentNum() == null){
            return null;
        }
        if(vo.getCommentNum().intValue() == 0){
            vo.setCommentNum(1);
        }
//        double goodRate = ifNull.apply(vo.getGoodCommentNum())/vo.getCommentNum();
//        double midRate = ifNull.apply(vo.getMidCommentNum())/vo.getCommentNum();
//        double badRate = ifNull.apply(vo.getBadCommentNum())/vo.getCommentNum();
//        features[0] = goodRate;
//        features[1] = midRate;
//        features[2] = badRate;
        features[0] = ifNull.apply(vo.getGoodCommentNum());
        features[1] = ifNull.apply(vo.getMidCommentNum());
        features[2] = ifNull.apply(vo.getBadCommentNum());
        return features;
    }

}
