package cluster.Birch;

/**
 * Created by jet.shi on 2018/3/26.
 */
public interface BirchCluster {

    /**
     * using the new features to update the birch cluster model.
     * actually,the new feature will build a b tree in memory.
     * i don't know whether right to implement the birch cluster
     * algorithm in storm platform.but i think it will work.
     * @param features
     * @return
     */
    TreeNode update(double[] features);

}
