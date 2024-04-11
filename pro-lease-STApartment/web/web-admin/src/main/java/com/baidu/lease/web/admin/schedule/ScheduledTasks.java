package com.baidu.lease.web.admin.schedule;

import com.baidu.lease.model.entity.LeaseAgreement;
import com.baidu.lease.model.enums.LeaseStatus;
import com.baidu.lease.web.admin.service.LeaseAgreementService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

//每天定时巡查，若是租期已经到期，则进行租期状态的更改
@Component
public class ScheduledTasks {
    @Autowired
    private LeaseAgreementService leaseAgreementService;

    @Scheduled(cron = "0 0 0 * * *")
    public void cheLeaseStatus(){
        leaseAgreementService.update(new LambdaUpdateWrapper<LeaseAgreement>()
                //若是租期结束时间小于现在的时间，则将状态改成已到期
                .le(LeaseAgreement::getLeaseEndDate,new Date())
                //条件是已签约和退组待确定的
                .in(LeaseAgreement::getStatus, LeaseStatus.SIGNED,LeaseStatus.WITHDRAWING)
                //若是满足以上条件，则进行签约状态的更改
                .set(LeaseAgreement::getStatus,LeaseStatus.EXPIRED));
    }
}
