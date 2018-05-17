package service.impl;

import dao.TmProductDataDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.SimulateService;

/**
 * @author jet.shi
 */

@Service
public class SimulateServiceImpl implements SimulateService{

    protected final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private TmProductDataDao tmProductDataDao;

    public  void sendData(){

    }

}
