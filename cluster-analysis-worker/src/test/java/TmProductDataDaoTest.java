import dao.TmProductDataDao;
import help.BaseTest;
import model.TmProductDataModel;
import org.junit.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@MapperScan("**.dao")
@EnableAutoConfiguration
public class TmProductDataDaoTest  extends BaseTest {

    @Autowired
    private TmProductDataDao tmProductDataDao;

    @Test
    public void testSelectById(){
        TmProductDataModel tmProductDataModel = tmProductDataDao.getById(3);
        System.out.println(tmProductDataModel);
    }

}
