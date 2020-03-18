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
package com.aspectran.daemon.adapter;

import com.aspectran.core.activity.request.parameter.ParameterMap;
import com.aspectran.core.adapter.BasicRequestAdapter;

/**
 * The Class DaemonRequestAdapter.
 */
public class DaemonRequestAdapter extends BasicRequestAdapter {

    /**
     * Instantiates a new DaemonRequestAdapter.
     */
    public DaemonRequestAdapter() {
        super(null);
    }

    /**
     * Instantiates a new DaemonRequestAdapter.
     *
     * @param parameterMap the parameter map
     */
    public DaemonRequestAdapter(ParameterMap parameterMap) {
        super(null, parameterMap);
    }

}