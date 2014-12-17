/**
 * Copyright 2014 Skubit
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.skubit.bitid;

public class ResultCode {

    public static final int NO_CONNECTION = -1;

    public static final int OK = 0;

    public static final int USER_CANCELED = 1;

    public static final int INVALID_BITID = 100;

    public static final int INVALID_NONCE = 101;

    public static final int NONCE_EXPIRED = 102;

    public static final int INVALID_SIGNATURE = 103;

    public static final int INVALID_ADDRESS = 104;

    public static final int CALLBACK_DOES_NOT_EXIST = 105;

    public static final int UNKNOWN_ERROR = 199;

}
