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

import com.datalingvo.DLException;
import com.datalingvo.mdllib.*;
import com.datalingvo.mdllib.DLTokenSolver.CONV_INTENT;
import com.datalingvo.mdllib.DLTokenSolver.IntentCallback;
import com.datalingvo.mdllib.DLTokenSolver.TERM;
import com.datalingvo.mdllib.tools.builder.DLModelBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

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
    SillyRobotProvider() throws DLException {
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
        // on intent-based token solver. Initialize adapter with constructed model.
        setup(DLModelBuilder.newJsonModel(path).setQueryFunction(solver::solve).build());
    }
}
