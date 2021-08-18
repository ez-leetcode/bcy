package com.bcy.elasticsearch.service;

import java.io.IOException;

public interface CosService {

    String test(String indexName) throws IOException;

    void searchCos(String keyword,Integer cnt,Integer page)throws IOException;

}
