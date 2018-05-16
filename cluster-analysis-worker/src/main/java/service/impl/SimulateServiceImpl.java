package service.impl;

import dao.TmProductDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.SimulateService;

@Service
public class SimulateServiceImpl implements SimulateService{

    @Autowired
    private TmProductDataDao tmProductDataDao;

    public  void sendData(){

    }

}
