/*
 * 2014-2017 Copyright (C) DataLingvo, Inc. All Rights Reserved.
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
  * <p>
  * This example for any user input replies with JSON representation query context
  * corresponding to that input. This is a simple demonstration of the JSON output
  * and all DataLingvo provided information that a user defined model can operate on.
  */
@DLActiveModelProvider
class EchoProvider extends DLSingleModelProviderAdapter {
    // Any immutable user defined ID.
    private final val MODEL_ID = "dl.echo.ex"
    
    /**
      * Escapes given string for JSON according to RFC 4627 http://www.ietf.org/rfc/rfc4627.txt.
      *
      * @param s String to escape.
      * @return Escaped string.
      */
    // TODO: instead of coding it here - borrow it from some well known library...
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
      * Converts Java serializable into JSON value.
      *
      * @param v Value to convert.
      * @return Converted value.
      */
    private def mkJsonVal(v: java.io.Serializable): String =
        if (v == null)
            "null"
        else if (v.isInstanceOf[java.lang.Number] || v.isInstanceOf[java.lang.Boolean])
            v.toString
        else
            s""""${escapeJson(v.toString)}""""
    
    /**
      * Converts Java map into JSON value.
      *
      * @param map Map to convert.
      * @return Converted value.
      */
    private def mkMapJson(map: Map[String, java.io.Serializable]): String =
        // Skip very long line keys for prettier display...
        // Is this necessary?
        map.toList.filter(p ⇒ p._1 != "AVATAR_URL" && p._1 != "USER_AGENT").
            sortBy(_._1).map(t ⇒ s""""${t._1}": ${mkJsonVal(t._2)}""").mkString("{", ",", "}")
    
    /**
      * Makes JSON presentation of data source from given query context.
      *
      * @param ctx Query context.
      * @return JSON presentation of data source.
      */
    private def mkDataSourceJson(ctx: DLQueryContext): String = {
        val ds = ctx.getDataSource
    
        // Hand-rolled JSON for simplicity...
        s"""
           | {
           |    "name": ${mkJsonVal(ds.getName)},
           |    "config": ${mkJsonVal(ds.getConfig)},
           |    "metadata": ${mkMapJson(ds.getMetadata.asScala.toMap)}
           | }
         """.stripMargin
    }
    
    /**
      * Makes JSON presentation of the given token.
      *
      * @param tok A token.
      * @return JSON presentation of data source.
      */
    private def mkTokenJson(tok: DLToken): String =
        // Hand-rolled JSON for simplicity...
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

    /**
      * Makes JSON presentation of the NLP sentence from given query context.
      *
      * @param ctx Query context.
      * @return JSON presentation of NLP sentence.
      */
    private def mkSentenceJson(ctx: DLQueryContext): String = {
        val sen = ctx.getSentence
    
        // Hand-rolled JSON for simplicity...
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
        // Using inline JSON model.
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
                // Hand-rolled JSON for simplicity...
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
