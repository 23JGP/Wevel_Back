package com.wevel.wevel_server.memo.service;

import com.wevel.wevel_server.memo.entity.Memo;
import com.wevel.wevel_server.memo.dto.MemoResponse;
import com.wevel.wevel_server.memo.repository.MemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemoService {
    @Autowired
    private MemoRepository memoRepository;

    public List<MemoResponse> getMemoDetails(Long userId, String tripName) {
        List<Memo> memos = memoRepository.findByUserIdAndTripName(userId, tripName);

        return memos.stream()
                .map(this::convertToMemoResponse)
                .collect(Collectors.toList());
    }

    private MemoResponse convertToMemoResponse(Memo memo) {
        MemoResponse memoResponse = new MemoResponse();
        memoResponse.setAmountGiven(memo.getAmountGiven());
        memoResponse.setAmountReceived(memo.getAmountReceived());
        memoResponse.setRcompleted(memo.getRcompleted());
        memoResponse.setGcompleted(memo.getGcompleted());
        memoResponse.setDate(memo.getDate());
        return memoResponse;
    }
}