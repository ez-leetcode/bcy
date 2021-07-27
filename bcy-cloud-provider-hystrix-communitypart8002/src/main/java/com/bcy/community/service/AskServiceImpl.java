package com.bcy.community.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AskServiceImpl implements AskService{

    @Override
    public String deleteAsk(Long id, Long number) {
        return null;
    }

    @Override
    public String addAsk(Long fromId, Long toId, String question) {
        return null;
    }
}
