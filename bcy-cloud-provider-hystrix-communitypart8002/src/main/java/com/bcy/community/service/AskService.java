package com.bcy.community.service;

public interface AskService {

    String deleteAsk(Long id,Long number);

    String addAsk(Long fromId,Long toId,String question);

    String addAnswer(Long id,Long number,String answer);
}
