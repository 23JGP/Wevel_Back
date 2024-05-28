package com.wevel.wevel_server.domain.memo.service;

import com.wevel.wevel_server.domain.memo.dto.MemoDTO;
import com.wevel.wevel_server.domain.memo.dto.MemoResponse;
import com.wevel.wevel_server.domain.memo.dto.ReceivedMemoResponse;
import com.wevel.wevel_server.domain.memo.entity.Memo;
import com.wevel.wevel_server.domain.memo.dto.GivenMemoResponse;
import com.wevel.wevel_server.domain.memo.repository.MemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemoService {
    @Autowired
    private MemoRepository memoRepository;

    public List<MemoResponse> getMemoDetails(Long userId, Long tripId) {
        List<Memo> memos = memoRepository.findByUserIdAndTripId(userId, tripId);

        return memos.stream()
                .map(this::convertToMemoResponse)
                .collect(Collectors.toList());
    }

    public List<GivenMemoResponse> getMemoGiven(Long userId, Long tripId) {
        List<Memo> memos = memoRepository.findByUserIdAndTripId(userId, tripId);

        return memos.stream()
                .map(this::convertToSelectedGivenMemoResponse)
                .collect(Collectors.toList());
    }

    public List<ReceivedMemoResponse> getMemoReceived(Long userId, Long tripId) {
        List<Memo> memos = memoRepository.findByUserIdAndTripId(userId, tripId);

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
        givenMemoResponse.setMemoId(memo.getMemoId());
        givenMemoResponse.setAmountGiven(memo.getAmountGiven());
        givenMemoResponse.setGcompleted(memo.getGcompleted());
        givenMemoResponse.setDate(memo.getDate());
        return givenMemoResponse;
    }

    private ReceivedMemoResponse convertToSelectedReceivedMemoResponse(Memo memo) {
        ReceivedMemoResponse receivedMemoResponse = new ReceivedMemoResponse();
        receivedMemoResponse.setMemoId(memo.getMemoId());
        receivedMemoResponse.setAmountReceived(memo.getAmountReceived());
        receivedMemoResponse.setRcompleted(memo.getRcompleted());
        receivedMemoResponse.setDate(memo.getDate());
        return receivedMemoResponse;
    }

    public void updateMemo(MemoDTO memoDTO) {
        Optional<Memo> optionalMemo = memoRepository.findById(memoDTO.getMemoId());
        if (optionalMemo.isPresent()) {
            Memo existingMemo = optionalMemo.get();

            existingMemo.setTripId(memoDTO.getTripId());
            existingMemo.setDate(memoDTO.getDate());
            existingMemo.setAmountGiven(memoDTO.getAmountGiven());
            existingMemo.setAmountReceived(memoDTO.getAmountReceived());

            // 저장
            memoRepository.save(existingMemo);
        } else {
            // 적절한 처리 (예외 처리 등)
        }
    }

    public void checkRcompleted(Long memoId) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new RuntimeException("Memo not found with id: " + memoId));
        memo.setRcompleted(true);
        memoRepository.save(memo);
    }

    public void checkGcompleted(Long memoId) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new RuntimeException("Memo not found with id: " + memoId));
        memo.setGcompleted(true);
        memoRepository.save(memo);
    }

}