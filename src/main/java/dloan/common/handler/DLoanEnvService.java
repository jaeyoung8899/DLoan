package dloan.common.handler;

import dloan.common.dao.CommonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DLoanEnvService {

    private static final String COMMON_SPACE = "common.env.";

    @Autowired
    protected CommonDao commonDao;
    /** 기타정보, 테이블, 필드 존재유무 */
    private Map<String, Object> etcMap = null;

    /** conf테이블 맵*/
    private Map<String,Object> confTblMap = null;




    public void makeEnv() {
        //CONF 테이블 유무조회
        int tblCnt = (Integer) commonDao.selectOne(COMMON_SPACE.concat("isExistTbl"),"DLOAN_CONF_TBL");     	       //책 제적 사유
        if(tblCnt > 0) {
            List<Map<String,Object>> confList = (List<Map<String, Object>>) commonDao.selectList(COMMON_SPACE.concat("getDLoanConf"));

            for(Map<String,Object> confMap : confList) {
                confTblMap.put(confMap.get("CONF_ID").toString(),confMap);
            }
        }
    }

}
