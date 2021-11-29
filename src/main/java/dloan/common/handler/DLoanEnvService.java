package dloan.common.handler;

import dloan.common.dao.CommonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DLoanEnvService {

    private static final String COMMON_SPACE = "common.env.";

    public DLoanEnvService(  CommonDao commonDao) {
        this.setCommonDao(commonDao);
        this.etcMap = new ArrayList<>();
        this.confTblMap = new HashMap<>();
        this.makeEnv();
    }

    private CommonDao commonDao = null;

    public void setCommonDao(CommonDao commonDao){
        this.commonDao = commonDao;
    }

    /** viewoption */
    private List<Map<String,Object>> etcMap = null;

    /** conf테이블 맵*/
    private Map<String,Object> confTblMap = null;

    public Map<String,Object> getConfTblMap(){
        return confTblMap;
    }

    public List<Map<String,Object>> getEtcMap() {
        return etcMap;
    }


    public void makeEnv() {
        //CONF 테이블 유무조회
        int tblCnt = (Integer) commonDao.selectOne(COMMON_SPACE.concat("isExistTbl"),"DLOAN_CONF_TBL");

        if(tblCnt > 0) {
            List<Map<String,Object>> confList = (List<Map<String, Object>>) commonDao.selectList(COMMON_SPACE.concat("getDLoanConf"));

            for(Map<String,Object> confMap : confList) {
                confTblMap.put(confMap.get("confId").toString(),confMap.get("confValue").toString());
            }
        }

        tblCnt = (Integer) commonDao.selectOne(COMMON_SPACE.concat("isExistTbl"),"DLOAN_VIEW_OPTION_TBL");

        if(tblCnt > 0) {
            etcMap = (List<Map<String, Object>>) commonDao.selectList(COMMON_SPACE.concat("getViewOption"));
        }
    }

}
