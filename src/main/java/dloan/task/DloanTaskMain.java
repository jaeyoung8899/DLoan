package dloan.task;

import dloan.common.handler.DLoanEnvService;
import dloan.common.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DloanTaskMain {

	private Logger log = LoggerFactory.getLogger(DloanTaskMain.class);
	
	@Autowired
	private DLoanTaskService dLoanTaskService;

	@Autowired
	private DLoanEnvService dLoanEnvService;
	
	@Scheduled(cron="0 1 0 * * *")
	public void taskScheduler() {
		log.debug("Task Scheduler START");
		
		// 대출대기자료 중에서 대출대기만료일이 지난자료 '미대출취소' 상태변경
		this.dLoanTaskService.taskExpiredLoanWait();
		
		// 환불불가 2019-12-20 환불불가 일시 제한
		//this.dLoanTaskService.taskNoRefund();
		String storeYn = "";
		for(Map<String,Object> map : dLoanEnvService.getEtcMap()) {
			if(map.get("classCode").equals("004")) {
				storeYn = map.get("value").toString();
			}
		}

		if(storeYn.equals("Y")){
			this.dLoanTaskService.taskNoReturn();
		}else{
			this.dLoanTaskService.taskNoRefund();
		}

		log.debug("Task Scheduler END");
	}

	@Scheduled(cron = "0 9 10 * * *")
	public void loanScheduler() {
		log.debug("반납독촉 문자 시작");

		// 반납독촉문자알림
		this.dLoanTaskService.loanScheduler();

		log.debug("반납독촉 문자 종료");
	}
}