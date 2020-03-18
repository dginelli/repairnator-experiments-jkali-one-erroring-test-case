/*
 * Copyright (c) 2008-2018 The Aspectran Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aspectran.core.context.rule.params;

import com.aspectran.core.util.apon.AbstractParameters;
import com.aspectran.core.util.apon.ParameterDefinition;
import com.aspectran.core.util.apon.ParameterValueType;

public class AdviceParameters extends AbstractParameters {

    public static final ParameterDefinition bean;
    public static final ParameterDefinition beforeAdvice;
    public static final ParameterDefinition afterAdvice;
    public static final ParameterDefinition aroundAdvice;
    public static final ParameterDefinition finallyAdvice;

    private static final ParameterDefinition[] parameterDefinitions;

    static {
        bean = new ParameterDefinition("bean", ParameterValueType.STRING);
        beforeAdvice = new ParameterDefinition("before", AdviceActionParameters.class);
        afterAdvice = new ParameterDefinition("after", AdviceActionParameters.class);
        aroundAdvice = new ParameterDefinition("around", AdviceActionParameters.class);
        finallyAdvice = new ParameterDefinition("finally", AdviceActionParameters.class);

        parameterDefinitions = new ParameterDefinition[] {
                bean,
                beforeAdvice,
                afterAdvice,
                aroundAdvice,
                finallyAdvice
        };
    }

    public AdviceParameters() {
        super(parameterDefinitions);
    }

}