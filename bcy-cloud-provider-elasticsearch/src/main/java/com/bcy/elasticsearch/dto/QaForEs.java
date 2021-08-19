package com.bcy.elasticsearch.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class QaForEs {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String title;

    private String description;

    //标签内容
    private String label;

    private String photo;

    private Date createTime;

}
/* es索引创建如下 拼音分词器 + 细粒度ik分词器
PUT qa
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
        "label" : {
          "type" : "text",
          "analyzer" : "ik_pinyin_analyzer"
        },
        "id" : {
          "type" : "long"
        },
        "number" : {
          "type" : "long"
        },
        "photo" : {
          "type" : "keyword"
        },
        "title" :{
          "type" : "text",
          "analyzer" : "ik_pinyin_analyzer"
        }
      }
  }
}
 */