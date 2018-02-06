/*
 * 2013-2017 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.echo

import com.datalingvo.mdllib._
import com.datalingvo.mdllib.tools.builder.DLModelBuilder
import com.datalingvo.mdllib.tools.scala.DLScalaSupport._ /* IDEA wrongly marks it as unused. */

import scala.collection.JavaConverters._

/**
  * Echo example model provider.
  */
@DLActiveModelProvider
class EchoProvider extends DLSingleModelProviderAdapter {
    private final val MODEL_ID = "dl.echo.ex"
    
    /**
      * Escapes given string for JSON according to RFC 4627 http://www.ietf.org/rfc/rfc4627.txt.
      *
      * @param s String to escape.
      * @return Escaped string.
      */
    def escapeJson(s: String): String = {
        val len = s.length
        
        if (len == 0)
            ""
        else {
            val sb = new StringBuilder
            
            for (ch ← s.toCharArray)
                ch match {
                    case '\\' | '"' ⇒ sb += '\\' += ch
                    case '/' ⇒ sb += '\\' += ch
                    case '\b' ⇒ sb ++= "\\b"
                    case '\t' ⇒ sb ++= "\\t"
                    case '\n' ⇒ sb ++= "\\n"
                    case '\f' ⇒ sb ++= "\\f"
                    case '\r' ⇒ sb ++= "\\r"
                    case _ ⇒
                        if (ch < ' ') {
                            val t = "000" + Integer.toHexString(ch)
                            
                            sb ++= "\\u" ++= t.substring(t.length - 4)
                        }
                        else
                            sb += ch
                }
            
            sb.toString()
        }
    }
    
    /**
      *
      * @param v
      * @return
      */
    private def mkJsonVal(v: java.io.Serializable): String =
        if (v == null)
            "null"
        else if (v.isInstanceOf[java.lang.Number] || v.isInstanceOf[java.lang.Boolean])
            v.toString
        else
            s""""${escapeJson(v.toString)}""""
    
    /**
      *
      * @param map
      * @return
      */
    private def mkMapJson(map: Map[String, java.io.Serializable]): String =
        // Skip very long line keys for prettier display...
        // Is this necessary?
        map.toList.filter(p ⇒ p._1 != "AVATAR_URL" && p._1 != "USER_AGENT").
            sortBy(_._1).map(t ⇒ s""""${t._1}": ${mkJsonVal(t._2)}""").mkString("{", ",", "}")
    
    /**
      * 
      * @param ctx
      * @return
      */
    private def mkDataSourceJson(ctx: DLQueryContext): String = {
        val ds = ctx.getDataSource
        
        s"""
           | {
           |    "name": ${mkJsonVal(ds.getName)},
           |    "config": ${mkJsonVal(ds.getConfig)},
           |    "metadata": ${mkMapJson(ds.getMetadata.asScala.toMap)}
           | }
         """.stripMargin
    }
    
    /**
      *
      * @param tok
      * @return
      */
    private def mkTokenJson(tok: DLToken): String = {
        s"""
           | {
           |    "id": ${mkJsonVal(tok.getId)},
           |    "group": ${mkJsonVal(tok.getGroup)},
           |    "type": ${mkJsonVal(tok.getType)},
           |    "parentId": ${mkJsonVal(tok.getParentId)},
           |    "value": ${mkJsonVal(tok.getValue)},
           |    "group": ${mkJsonVal(tok.getGroup)},
           |    "isUserDefined": ${mkJsonVal(tok.isUserDefined)},
           |    "isFreeWord": ${mkJsonVal(tok.isFreeWord)},
           |    "metadata": ${mkMapJson(tok.getMetadata.asScala.toMap)}
           | }
         """.stripMargin
    }

    /**
      *
      * @param ctx
      * @return
      */
    private def mkSentenceJson(ctx: DLQueryContext): String = {
        val sen = ctx.getSentence
        
        s"""
           | {
           |    "metadata": ${mkMapJson(sen.getMetadata.asScala.toMap)},
           |    "tokens": [
           |        ${sen.getTokens.asScala.map(mkTokenJson).mkString(",")}
           |    ]
           | }
         """.stripMargin
    }

    setup(
        MODEL_ID,
        DLModelBuilder.newJsonStringModel(
            s"""
              | {
              |    "id": "$MODEL_ID",
              |    "name": "Echo Example Model",
              |    "version": "1.0",
              |    "metadata": {
              |        "DESCRIPTION": "Echo example model.",
              |        "VENDOR_NAME": "DataLingvo, Inc",
              |        "VENDOR_URL": "https://www.datalingvo.com",
              |        "VENDOR_CONTACT": "Support",
              |        "VENDOR_EMAIL": "info@datalingvo.com",
              |        "DOCS_URL": "https://www.datalingvo.com",
              |        "ALLOW_NO_USER_TOKENS": true
              |    },
              |    "defaultTrivia": "false"
              | }
            """.stripMargin)
            .setQueryFunction((ctx: DLQueryContext) ⇒ {
                DLQueryResult.json(
                    s"""
                       |{
                       |    "srvReqId": ${mkJsonVal(ctx.getServerRequestId)},
                       |    "modelMetadata": ${mkMapJson(ctx.getModelMetadata.asScala.toMap)},
                       |    "dataSource": ${mkDataSourceJson(ctx)},
                       |    "sentence": ${mkSentenceJson(ctx)},
                       |    "hint": ${mkJsonVal(ctx.getHint)}
                       |}
                     """.stripMargin
                )
            })
            .build()
    )
}
