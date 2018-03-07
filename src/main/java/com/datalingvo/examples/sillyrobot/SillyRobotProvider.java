/*
 * 2017 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.sillyrobot;

import com.datalingvo.*;
import com.datalingvo.mdllib.*;
import com.datalingvo.mdllib.DLTokenSolver.*;
import com.datalingvo.mdllib.tools.builder.*;
import org.apache.commons.lang3.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Silly Robot example model provider.
 */
@DLActiveModelProvider
public class SillyRobotProvider extends DLSingleModelProviderAdapter {
    // Robot's memory.
    private final Set<String> mem = new HashSet<>();

    /**
     *
     * @param toks
     * @return
     */
    public static Boolean selectToken(List<DLElementSelectorToken> toks) {
        List<DLElementSelectorToken> all = toks.stream().filter(p -> !p.isStopWord()).collect(Collectors.toList());

        List<DLElementSelectorToken> nouns = all.stream().filter(DLPosBase::isNoun).collect(Collectors.toList());
        List<DLElementSelectorToken> adjs = all.stream().filter(DLPosBase::isAdjective).collect(Collectors.toList());

        // Required: one optional adjective and one mandatory noun.
        if (nouns.size() != 1 || adjs.size() > 1 || nouns.size() + adjs.size() != all.size())
            return false;
        
        // If there is an adjective - it should be before the noun.
        return adjs.isEmpty() || all.indexOf(adjs.get(0)) < all.indexOf(nouns.get(0));
    }

    /**
     *
     * @param s
     * @return
     */
    private String cap(String s) {
        return StringUtils.capitalize(s);
    }

    /**
     *
     * @param ctx
     * @return
     */
    private String getSubject(DLTokenSolverContext ctx) {
        return ctx.getToken(1, 0).getNormalizedText();
    }

    /**
     *
     * @param ctx
     * @return
     */
    private DLQueryResult doState(DLTokenSolverContext ctx) {
        String subj = getSubject(ctx);

        return DLQueryResult.enUsSpeak(cap(subj) + (mem.contains(subj) ? " is started." : " is not started."));
    }

    /**
     *
     * @param ctx
     * @return
     */
    private DLQueryResult doStart(DLTokenSolverContext ctx) {
        String subj = getSubject(ctx);

        return DLQueryResult.enUsSpeak(cap(subj) + (!mem.add(subj) ? " is already started." : " has been started."));
    }

    /**
     *
     * @param ctx
     * @return
     */
    private DLQueryResult doStop(DLTokenSolverContext ctx) {
        String subj = getSubject(ctx);

        return DLQueryResult.enUsSpeak(cap(subj) + (!mem.remove(subj) ? " has not been started." : " has been stopped."));
    }

    /**
     * Initializes provider.
     *
     * @throws DLException If any errors occur.
     */
    public SillyRobotProvider() throws DLException {
        String path = DLModelBuilder.classPathFile("silly_robot_model.json");

        DLTokenSolver solver = new DLTokenSolver();

        BiConsumer<String, IntentCallback> idNounMaker =
            (verb, f) ->
                solver.addIntent(
                    new INTENT(
                        5, // Max unused words count.
                        // Index 0: non-interactive term that is either state, start or stop.
                        new TERM(new RULE("id", "==", "ctrl:" + verb), 1, 1),
                        // Index 1: interactive object term.
                        new TERM("an object to " + verb, new RULE("id", "==", "ctrl:robot"), 1, 1)
                    ),
                    f
                );

        idNounMaker.accept("state", this::doState);
        idNounMaker.accept("start", this::doStart);
        idNounMaker.accept("stop", this::doStop);

        DLModel model = DLModelBuilder.newJsonModel(path).setQueryFunction(solver::solve).build();

        // Initialize adapter.
        setup("dl.control.ex", model);
    }
}
