package com.wevel.wevel_server.domain.alarm.service;

import com.wevel.wevel_server.domain.alarm.entity.Alarm;
import com.wevel.wevel_server.domain.alarm.entity.Notify;
import com.wevel.wevel_server.domain.alarm.repository.AlarmRepository;
import com.wevel.wevel_server.domain.alarm.repository.NotifyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotifyService {

    private final NotifyRepository notifyRepository;
    private final AlarmRepository alarmRepository;

    @Autowired
    public NotifyService(NotifyRepository notifyRepository, AlarmRepository alarmRepository) {
        this.notifyRepository = notifyRepository;
        this.alarmRepository = alarmRepository;
    }

    public List<Notify> getNotificationsByUserId(Long userId) {
        Optional<Alarm> alarmOptional = alarmRepository.findByUserId(userId);
        if (alarmOptional.isPresent() && alarmOptional.get().isSpendAlarm()) {
            return notifyRepository.findByUserId(userId);
        } else {
            return List.of();
        }
    }
}