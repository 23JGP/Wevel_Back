package com.wevel.wevel_server.domain.alarm.service;

import com.wevel.wevel_server.domain.alarm.entity.Alarm;
import com.wevel.wevel_server.domain.alarm.repository.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AlarmService {
    private final AlarmRepository alarmRepository;
    @Autowired
    public AlarmService(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    public Alarm createAlarmForUser(Long userId) {
        Optional<Alarm> existingAlarm = alarmRepository.findByUserId(userId);
        if (existingAlarm.isPresent()) {
            return existingAlarm.get();
        } else {
            Alarm alarm = new Alarm(userId);
            return alarmRepository.save(alarm);
        }
    }

    public void toggleOverBudgetAlarm(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new RuntimeException("Alarm not found with id : " + alarmId));
        alarm.setOverBudgetAlarm(!alarm.isOverBudgetAlarm());
        alarmRepository.save(alarm);
    }

    public void toggleSpendAlarm(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new RuntimeException("Alarm not found with id : " + alarmId));
        alarm.setSpendAlarm(!alarm.isSpendAlarm());
        alarmRepository.save(alarm);
    }

    public Alarm getAlarm(Long userId) {
        Alarm alarm = alarmRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Alarm not found with userId : " + userId));

        return alarm;
    }
}
