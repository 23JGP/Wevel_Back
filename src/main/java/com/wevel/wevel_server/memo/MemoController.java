package com.wevel.wevel_server.memo;

import com.wevel.wevel_server.memo.dto.GivenMemoResponse;
import com.wevel.wevel_server.memo.dto.MemoResponse;
import com.wevel.wevel_server.memo.dto.ReceivedMemoResponse;
import com.wevel.wevel_server.memo.repository.MemoRepository;
import com.wevel.wevel_server.memo.service.MemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/memo")
public class MemoController {

    @Autowired
    private final MemoRepository memoRepository;

    @Autowired
    private final MemoService memoService;

    public MemoController(MemoRepository memoRepository, MemoService memoService) {
        this.memoRepository = memoRepository;
        this.memoService = memoService;
    }

    // 홈페이지에서 전체 메모 불러오기 get = /api/memo/all/:id/:tripName
    @GetMapping("/all/{userId}/{tripName}")
    public List<MemoResponse> getMemoDetails(@PathVariable Long userId, @PathVariable String tripName) {
        return memoService.getMemoDetails(userId, tripName);
    }

    // 홈페이지에서 줘야하는 돈 메모 불러오기 get = /api/memo/given/:id/:tripName
    @GetMapping("/given/{userId}/{tripName}")
    public List<GivenMemoResponse> getGivenMemos(@PathVariable Long userId, @PathVariable String tripName) {
        return memoService.getMemoGiven(userId, tripName);
    }

    // 홈페이지에서 받아야하는 돈 메모 불러오기 get = /api/memo/received/:id/:tripName
    @GetMapping("/received/{userId}/{tripName}")
    public List<ReceivedMemoResponse> getReceived(@PathVariable Long userId, @PathVariable String tripName) {
        return memoService.getMemoReceived(userId, tripName);
    }
}
