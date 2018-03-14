/*
 * 2014-2017 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.whereami

import com.datalingvo.mdllib._
import com.datalingvo.mdllib.tools.builder.DLModelBuilder
import com.datalingvo.mdllib.tools.scala.DLScalaSupport._ /* IDEA wrongly marks it as unused. */
import com.datalingvo.mdllib.DLTokenSolver._

/**
  * "Where Am I" example model provider.
  * <p>
  * Simple Scala example that answers "where am I" question about user's
  * current location responding with a Google map.
  */
@DLActiveModelProvider
class WhereAmIProvider extends DLSingleModelProviderAdapter {
    /**
      * Callback on matching intent.
      *
      * @param ctx Token solver context.
      * @return Static Google map query result.
      */
    private def onMatch(ctx: DLTokenSolverContext): DLQueryResult = {
        val meta = ctx.getSentence.getMetadata
        
        // Default to some Silicon Valley location in case user's coordinates
        // cannot be determines.
        val lat = meta.getDoubleOrElse("LATITUDE", 37.7749)
        val lon = meta.getDoubleOrElse("LONGITUDE", 122.4194)
    
        DLQueryResult.jsonGmap(
            s"""
                |{
                |   "cssStyle": {
                |        "width": "600px", 
                |        "height": "300px"
                |   },
                |   "gmap": {
                |        "center": "$lat,$lon",
                |        "zoom": 14,
                |        "scale": 2,
                |        "size": "600x300",
                |        "maptype": "terrain",
                |        "markers": "color:red|$lat,$lon"
                |    }
                |}
            """.stripMargin
        )
    }
    
    private val solver = new DLTokenSolver().addIntent(
        new INTENT(
            /* Default is to include conversation context. */
            /* Default is to do an exact match. */
            5, // Max number of free words.
            new TERM(new RULE("id", "==", "wai:where"), 1, 1)
        ),
        onMatch _ // Callback on match.
    )
    
    setup(
        "dl.whereami.ex",
        DLModelBuilder.newJsonModel(DLModelBuilder.classPathFile("whereami_model.json")).
            setQueryFunction(solver.solve _).
            build()
    )
}