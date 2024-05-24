package com.wevel.wevel_server.memo;

import com.wevel.wevel_server.memo.dto.GivenMemoResponse;
import com.wevel.wevel_server.memo.dto.MemoAllResponse;
import com.wevel.wevel_server.memo.dto.ReceivedMemoResponse;
import com.wevel.wevel_server.memo.repository.MemoRepository;
import com.wevel.wevel_server.memo.service.MemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    @GetMapping("/{memoId}")
//    public ResponseEntity<MemoDTO> getMemo(@PathVariable Long memoId) {
//        MemoDTO memoDTO = memoService.getMemoById(memoId);
//        return ResponseEntity.ok(memoDTO);
//    }

    // 홈페이지에서 전체 메모 불러오기 get = /api/memo/all/:id/:tripName
    // TODO : 성공하면 메모 삭제 기능 추가
//    @GetMapping("/all/{userId}/{tripName}")
//    public List<MemoResponse> getMemoDetails(@PathVariable Long userId, @PathVariable String tripName) {
//        return memoService.getMemoDetails(userId, tripName);
//    }

    @GetMapping("/all/{userId}/{tripName}")
    public MemoAllResponse getCombinedMemos(@PathVariable Long userId, @PathVariable String tripName) {
        List<GivenMemoResponse> givenMemos = memoService.getMemoGiven(userId, tripName);
        List<ReceivedMemoResponse> receivedMemos = memoService.getMemoReceived(userId, tripName);

        return new MemoAllResponse(givenMemos, receivedMemos);
    }

    // 홈페이지에서 줘야하는 돈 메모 불러오기 get = /api/memo/given/:id/:tripName
    @GetMapping("/give/{userId}/{tripName}")
    public List<GivenMemoResponse> getGivenMemos(@PathVariable Long userId, @PathVariable String tripName) {
        return memoService.getMemoGiven(userId, tripName);
    }

    // 홈페이지에서 받아야하는 돈 메모 불러오기 get = /api/memo/received/:id/:tripName
    @Operation(summary = "Get received memos", description = "홈페이지에서 받아야하는 돈 메모를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "해당 ID 또는 여행 이름에 해당하는 메모가 없습니다.")
    })
    @GetMapping("/receive/{userId}/{tripName}")
    public List<ReceivedMemoResponse> getReceived(@PathVariable Long userId, @PathVariable String tripName) {
        return memoService.getMemoReceived(userId, tripName);
    }
}
