package com.wevel.wevel_server.domain.tripInfo.service;

import com.wevel.wevel_server.domain.alarm.entity.Notify;
import com.wevel.wevel_server.domain.alarm.repository.NotifyRepository;
import com.wevel.wevel_server.domain.tripInfo.entity.TripInfo;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TripInfoListener implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Autowired
    private NotifyRepository notifyRepository;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        TripInfoListener.applicationContext = applicationContext;
    }

    @PostPersist
    @PostUpdate
    public void checkBudget(TripInfo tripInfo) {
        notifyRepository = applicationContext.getBean(NotifyRepository.class);

        System.out.println("checkBudget method called for TripInfo: " + tripInfo.getTripId());

        if (tripInfo.getSpentAmount() > tripInfo.getTotalBudget()) {
            System.out.println("Spent amount exceeds total budget for TripInfo: " + tripInfo.getTripId());

            Notify notify = new Notify();
            notify.setUserId(tripInfo.getUserId());
            notify.setTripId(tripInfo.getTripId());
            notify.setDate(new Date());
            notify.setMessage("[알림] " + tripInfo.getTripName() + " 여행 예산 금액을 초과하였습니다.");
            notify.setIsLead(false);
            notifyRepository.save(notify);
        }
    }
}