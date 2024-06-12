package com.wevel.wevel_server.domain.alarm;

import com.wevel.wevel_server.domain.alarm.entity.Alarm;
import com.wevel.wevel_server.domain.alarm.service.AlarmService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/alarm")
@AllArgsConstructor
@Tag(name = "Alarm", description = "알람 관련 API")
public class AlarmController {

    @Autowired
    private final AlarmService alarmService;

    @GetMapping("/{userId}")
    public Alarm getAlarm(@PathVariable Long userId) {
        return alarmService.getAlarm(userId);
    }

    @PatchMapping("/budget/{alarmId}")
    public ResponseEntity<?> toggleOverBudgetAlarm(@PathVariable Long alarmId) {
        alarmService.toggleOverBudgetAlarm(alarmId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/spend/{alarmId}")
    public ResponseEntity<?> toggleSpendAlarm(@PathVariable Long alarmId){
        alarmService.toggleSpendAlarm(alarmId);
        return ResponseEntity.ok().build();
    }
}
