/*
 * Copyright (c) 2021 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.net.http.nativeimpl;

import io.ballerina.runtime.api.Environment;
import io.ballerina.runtime.api.Module;
import io.ballerina.runtime.api.Runtime;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BObject;
import io.ballerina.runtime.api.values.BString;
import org.ballerinalang.net.http.ValueCreatorUtils;

/**
 * This class will hold module related utility functions.
 *
 * @since 2.0.0
 */
public class ModuleUtils {

    private static Runtime runtime;
    private static Module httpModule;
    private static BObject errorObj;

    private ModuleUtils() {}

    public static void setModule(Environment env) {
        runtime = env.getRuntime();
        httpModule = env.getCurrentModule();
        errorObj = ValueCreatorUtils.createErrorObject();
    }

    public static BString getModuleIdentifier() {
        return StringUtils.fromString(httpModule.toString());
    }

    /**
     * Gets ballerina error object.
     *
     * @return http error object.
     */
    public static BObject getHttpErrorObject() {
        return errorObj;
    }

    public static Runtime getRuntime() {
        return runtime;
    }
}
