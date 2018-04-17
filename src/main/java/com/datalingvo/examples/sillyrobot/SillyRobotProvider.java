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
 * <p>
 * This simple example provides the model that allows you to "start" and "stop"
 * arbitrary objects. You can say "start the car", "stop the car", or "can you turn on a washing machine?".
 * The model will voice-reply the acknowledgement of the operation: "car has been started", etc.
 */
@DLActiveModelProvider
public class SillyRobotProvider extends DLSingleModelProviderAdapter {
    // Robot's memory.
    private final Set<String> mem = new HashSet<>();

    /**
     * Convenient filtering routine.
     *
     * @param items Items to filter.
     * @param predicate Predicate to filter by.
     * @return Filtered list.
     */
    static private <T> List<T> filter(List<T> items, Predicate<? super T> predicate) {
        return items.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * Since the model operates on any arbitrary object we provide this method that is called
     * to determine the model's element (instead of providing them via simple synonyms which is quite
     * impossible for arbitrary objects). Even though this is a pretty laborious way to get elements,
     * in some cases this is the only way out.
     * <p>
     * Note that this method is referenced in the JSON model configuration - that's how DataLingvo
     * knows to call this method to parse input string for specific element.
     *
     * @param toks Set of tokens to check of they constitute an element.
     * @return {@code true} if all these tokens represent a known element for this model.
     */
    public static Boolean selectToken(List<DLElementSelectorToken> toks) {
        // Filter all stopwords out.
        List<DLElementSelectorToken> all = filter(toks, p -> !p.isStopWord());
        // Gets all nouns.
        List<DLElementSelectorToken> nouns = filter(all, DLPosBase::isNoun);
        // Get all adjectives.
        List<DLElementSelectorToken> adjs = filter(all, DLPosBase::isAdjective);

        // Require one optional adjective and one mandatory noun.
        if (nouns.size() != 1 || adjs.size() > 1 || nouns.size() + adjs.size() != all.size())
            return false;
        
        // If there is an adjective - it should be before the noun.
        return adjs.isEmpty() || all.indexOf(adjs.get(0)) < all.indexOf(nouns.get(0));
    }

    /**
     * A shortcut to string capitalization.
     *
     * @param s String to capitalize.
     * @return Capitalized string.
     */
    private String cap(String s) {
        return StringUtils.capitalize(s);
    }

    /**
     * Gets the subject out of the solver context (1st token in the 2nd term - see solver definition).
     * The subject is what technically found by {@link #selectToken(List)} method.
     *
     * @param ctx Solver context.
     * @return Subject of the input string.
     */
    private String getSubject(DLTokenSolverContext ctx) {
        return ctx.getToken(1, 0).getNormalizedText();
    }

    /**
     * Callback on state inquiry.
     *
     * @param ctx Solver context.
     * @return Query result.
     */
    private DLQueryResult doState(DLTokenSolverContext ctx) {
        // Subject of the sentence.
        String subj = getSubject(ctx);

        // US English voice reply.
        return DLQueryResult.enUsSpeak(cap(subj) + (mem.contains(subj) ? " is started." : " is not started."));
    }

    /**
     * Callback on start inquiry.
     *
     * @param ctx Solver context.
     * @return Query result.
     */
    private DLQueryResult doStart(DLTokenSolverContext ctx) {
        // Subject of the sentence.
        String subj = getSubject(ctx);

        // US English voice reply.
        return DLQueryResult.enUsSpeak(cap(subj) + (!mem.add(subj) ? " is already started." : " has been started."));
    }

    /**
     * Callback on stop inquiry.
     *
     * @param ctx Solver context.
     * @return Query result.
     */
    private DLQueryResult doStop(DLTokenSolverContext ctx) {
        // Subject of the sentence.
        String subj = getSubject(ctx);

        // US English voice reply.
        return DLQueryResult.enUsSpeak(cap(subj) + (!mem.remove(subj) ? " has not been started." : " has been stopped."));
    }

    /**
     * Initializes provider.
     *
     * @throws DLException If any errors occur.
     */
    public SillyRobotProvider() throws DLException {
        String path = DLModelBuilder.classPathFile("silly_robot_model.json");

        // Create default token solver for intent-based matching.
        DLTokenSolver solver = new DLTokenSolver();

        // Lambda for adding intent to the solver.
        BiConsumer<String, IntentCallback> intentMaker =
            (id, f/* Callback. */) ->
                solver.addIntent(
                    new CONV_INTENT(
                        // Term idx=0:
                        // A non-interactive term that is either 'state', 'start' or 'stop'.
                        // ID of the element should be 'ctrl:start', 'ctrl:state', or 'ctrl:stop'.
                        new TERM("id == ctrl:" + id, 1, 1),
                        // Term idx=1:
                        // An interactive object term. If it's missing the system will ask for it.
                        // ID of the element should be 'ctrl:subject'
                        new TERM("an object to " + id, "id == ctrl:subject", 1, 1)
                    ),
                    f
                );

        // Add three intents for 'state', 'start' and 'stop' commands.
        intentMaker.accept("state", this::doState);
        intentMaker.accept("start", this::doStart);
        intentMaker.accept("stop", this::doStop);

        // Load model form JSON configuration and set query function implementation based
        // on intent-based token solver.
        DLModel model = DLModelBuilder.newJsonModel(path).setQueryFunction(solver::solve).build();

        // Initialize adapter with constructed model.
        setup("dl.control.ex", model);
    }
}
