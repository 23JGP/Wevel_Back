package com.wevel.wevel_server.memo.service;

import com.wevel.wevel_server.memo.dto.GivenMemoResponse;
import com.wevel.wevel_server.memo.dto.ReceivedMemoResponse;
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

    public List<GivenMemoResponse> getMemoGiven(Long userId, String tripName) {
        List<Memo> memos = memoRepository.findByUserIdAndTripName(userId, tripName);

        return memos.stream()
                .map(this::convertToSelectedGivenMemoResponse)
                .collect(Collectors.toList());
    }

    public List<ReceivedMemoResponse> getMemoReceived(Long userId, String tripName) {
        List<Memo> memos = memoRepository.findByUserIdAndTripName(userId, tripName);

        return memos.stream()
                .map(this::convertToSelectedReceivedMemoResponse)
                .collect(Collectors.toList());
    }

    private MemoResponse convertToMemoResponse(Memo memo) {
        MemoResponse memoResponse = new MemoResponse();
        memoResponse.setAmountGiven(memo.getAmountGiven());
        memoResponse.setAmountReceived(memo.getAmountReceived());
        memoResponse.setRcompleted(false); // 처음 값 false로 셋팅
        memoResponse.setGcompleted(false);
        memoResponse.setDate(memo.getDate());
        return memoResponse;
    }

    private GivenMemoResponse convertToSelectedGivenMemoResponse(Memo memo) {
        GivenMemoResponse givenMemoResponse = new GivenMemoResponse();
        givenMemoResponse.setAmountGiven(memo.getAmountGiven());
        givenMemoResponse.setGcompleted(memo.getGcompleted());
        givenMemoResponse.setDate(memo.getDate());
        return givenMemoResponse;
    }

    private ReceivedMemoResponse convertToSelectedReceivedMemoResponse(Memo memo) {
        ReceivedMemoResponse receivedMemoResponse = new ReceivedMemoResponse();
        receivedMemoResponse.setAmountReceived(memo.getAmountReceived());
        receivedMemoResponse.setRcompleted(memo.getRcompleted());
        receivedMemoResponse.setDate(memo.getDate());
        return receivedMemoResponse;
    }
}