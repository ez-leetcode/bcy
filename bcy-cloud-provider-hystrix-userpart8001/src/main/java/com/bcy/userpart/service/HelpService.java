package com.bcy.userpart.service;

import com.alibaba.fastjson.JSONObject;

public interface HelpService {

    String addHelp(String question,String answer,Integer type);

    String deleteHelp(Long number);

    JSONObject getHelpList(Long cnt,Long page,Integer type);

    JSONObject getHelp(Long number);

    String addSolve(Long number,Integer isSolved);
}
