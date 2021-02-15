/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.net.http.actions.httpclient;

import io.ballerina.runtime.api.Environment;
import io.ballerina.runtime.api.Future;
import io.ballerina.runtime.api.async.Callback;
import io.ballerina.runtime.api.values.BError;
import io.ballerina.runtime.api.values.BObject;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.runtime.api.values.BTypedesc;

/**
 * Contains the external functions related to HTTP client remote methods.
 *
 * @since SL-alpha3
 */
public class ExternRemoteMethod {

    public static Object post(Environment env, BObject clientObj, BString path, Object message,
                               BTypedesc targetType) {

        Future balFuture = env.markAsync();
        Object[] paramFeed = new Object[6];
        paramFeed[0] = path;
        paramFeed[1] = true;
        paramFeed[2] = message;
        paramFeed[3] = true;
        paramFeed[4] = targetType;
        paramFeed[5] = true;

        env.getRuntime().invokeMethodAsync(clientObj, "privatePost", null, null, new Callback() {
            @Override
            public void notifySuccess(Object result) {
                balFuture.complete(result);
            }

            @Override
            public void notifyFailure(BError bError) {
                balFuture.complete(bError);
            }
        }, paramFeed);

//        Type type = typedesc.getDescribingType();
//        MapValue map;
//
//        if (type.getTag() == INT_TAG) {
//            map = new MapValueImpl(new BMapType(type));
//            map.put(new BmpStringValue("one"), 10);
//            map.put(new BmpStringValue("two"), 20);
//        } else if (type.getTag() == STRING_TAG) {
//            map = new MapValueImpl(new BMapType(type));
//            map.put(NAME, new BmpStringValue("Pubudu"));
//            map.put(CITY, new BmpStringValue("Panadura"));
//        } else {
//            map = new MapValueImpl(new BMapType(PredefinedTypes.TYPE_ANY));
//        }
//
//        return map;
        return null;
    }

}
