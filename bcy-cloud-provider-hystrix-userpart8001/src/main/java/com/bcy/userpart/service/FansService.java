package com.bcy.userpart.service;


public interface FansService {

    String addFollow(Long fromId,Long toId);

    String deleteFollow(Long fromId,Long toId);

}
