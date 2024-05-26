package com.wevel.wevel_server.domain.memo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemoAllResponse {
    private List<GivenMemoResponse> givenMemos;
    private List<ReceivedMemoResponse> receivedMemos;

}
