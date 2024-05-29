package com.wevel.wevel_server.domain.memo;

import com.wevel.wevel_server.domain.memo.dto.GivenMemoResponse;
import com.wevel.wevel_server.domain.memo.dto.MemoAllResponse;
import com.wevel.wevel_server.domain.memo.dto.ReceivedMemoResponse;
import com.wevel.wevel_server.domain.memo.repository.MemoRepository;
import com.wevel.wevel_server.domain.memo.service.MemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/memo")
@Tag(name = "Memo", description = "메모 관련 API")
public class MemoController {

    @Autowired
    private final MemoRepository memoRepository;

    @Autowired
    private final MemoService memoService;

    public MemoController(MemoRepository memoRepository, MemoService memoService) {
        this.memoRepository = memoRepository;
        this.memoService = memoService;
    }


    @GetMapping("/all/{userId}/{tripId}")
    public MemoAllResponse getCombinedMemos(@PathVariable Long userId, @PathVariable Long tripId) {
        List<GivenMemoResponse> givenMemos = memoService.getMemoGiven(userId, tripId);
        List<ReceivedMemoResponse> receivedMemos = memoService.getMemoReceived(userId, tripId);

        return new MemoAllResponse(givenMemos, receivedMemos);
    }

    // 홈페이지에서 줘야하는 돈 메모 불러오기 get = /api/memo/given/:id/:tripId
    @GetMapping("/give/{userId}/{tripId}")
    public List<GivenMemoResponse> getGivenMemos(@PathVariable Long userId, @PathVariable Long tripId) {
        return memoService.getMemoGiven(userId, tripId);
    }

    // 홈페이지에서 받아야하는 돈 메모 불러오기 get = /api/memo/received/:id/:tripId
    @Operation(summary = "Get received memos", description = "홈페이지에서 받아야하는 돈 메모를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "해당 ID 또는 여행 이름에 해당하는 메모가 없습니다.")
    })
    @GetMapping("/receive/{userId}/{tripId}")
    public List<ReceivedMemoResponse> getReceived(@PathVariable Long userId, @PathVariable Long tripId) {
        return memoService.getMemoReceived(userId, tripId);
    }

    @PatchMapping("/check/receive/{memoId}")
    public ResponseEntity<?> checkReceiveCompleted(@PathVariable Long memoId) {
        memoService.toggleRcompleted(memoId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/check/give/{memoId}")
    public ResponseEntity<?> checkGiveCompleted(@PathVariable Long memoId) {
        memoService.toggleGcompleted(memoId);
        return ResponseEntity.ok().build();
    }


}
