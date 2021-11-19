package dloan.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DloanTaskMain {

	private Logger log = LoggerFactory.getLogger(DloanTaskMain.class);
	
	@Autowired
	private DLoanTaskService dLoanTaskService;
	
	@Scheduled(cron="0 1 0 * * *")
	public void taskScheduler() {
		log.debug("Task Scheduler START");
		
		// 대출대기자료 중에서 대출대기만료일이 지난자료 '미대출취소' 상태변경
		this.dLoanTaskService.taskExpiredLoanWait();
		
		// 환불불가 2019-12-20 환불불가 일시 제한
		//this.dLoanTaskService.taskNoRefund();
		
		
		log.debug("Task Scheduler END");
	}
}