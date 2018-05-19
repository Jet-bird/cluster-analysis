package service;

import service.impl.BaseTest;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;


@ComponentScan
@EnableAutoConfiguration
public class SimulateServiceImplTest extends BaseTest {

    @Resource
    private SimulateService simulateServiceImpl;

    @Test
    public void testSend(){
        simulateServiceImpl.sendData();
    }
}
