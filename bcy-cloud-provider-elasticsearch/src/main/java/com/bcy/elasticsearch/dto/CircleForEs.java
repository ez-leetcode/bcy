package com.bcy.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class CircleForEs {

    private String circleName;

    private String description;

    private String photo;

    private String nickName;

    private Integer postCounts;

    private Integer followCounts;

    private Date createTime;

}
// es索引创建如下 拼音分词器 + 细粒度ik分词器
/*
PUT circle
{
      "settings": {
        "analysis": {
            "analyzer": {
                "ik_pinyin_analyzer": {
                    "type": "custom",
                    "tokenizer": "ik_max_word",
                    "filter": "pinyin_filter"
                }
            },
            "filter": {
                "pinyin_filter": {
                    "type": "pinyin",
                    "keep_first_letter": false
                }
            }
        }
    },

    "mappings" : {
      "dynamic_templates" : [
        {
          "message_full" : {
            "match" : "message_full",
            "mapping" : {
              "fields" : {
                "keyword" : {
                  "ignore_above" : 2048,
                  "type" : "keyword"
                }
              },
              "type" : "text"
            }
          }
        },
        {
          "message" : {
            "match" : "message",
            "mapping" : {
              "type" : "text"
            }
          }
        },
        {
          "strings" : {
            "match_mapping_type" : "string",
            "mapping" : {
              "type" : "keyword"
            }
          }
        }
      ],
      "properties" : {
        "createTime" : {
          "type" : "long"
        },
        "description" : {
          "type" : "text",
          "analyzer" : "ik_pinyin_analyzer"
        },
        "nickName" : {
          "type" : "text",
          "analyzer" : "ik_pinyin_analyzer"
        },
        "circleName" : {
          "type" : "text",
          "analyzer" : "ik_pinyin_analyzer"
        },
        "photo" : {
          "type" : "keyword"
        },
        "postCounts" : {
          "type" : "long"
        },
        "followCounts" : {
          "type" : "long"
        }
      }
  }
}

 */
