// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package com.cloud.api.response;

import com.cloud.api.ApiConstants;
import com.cloud.utils.IdentityProxy;
import com.cloud.serializer.Param;
import com.google.gson.annotations.SerializedName;

public class GuestOSResponse extends BaseResponse {
    @SerializedName(ApiConstants.ID) @Param(description="the ID of the OS type")
    private IdentityProxy id = new IdentityProxy("guest_os");

    @SerializedName(ApiConstants.OS_CATEGORY_ID) @Param(description="the ID of the OS category")
    private IdentityProxy osCategoryId = new IdentityProxy("guest_os_category");

    @SerializedName(ApiConstants.DESCRIPTION) @Param(description="the name/description of the OS type")
    private String description;

    public Long getId() {
        return id.getValue();
    }

    public void setId(Long id) {
        this.id.setValue(id);
    }

    public Long getOsCategoryId() {
        return osCategoryId.getValue();
    }

    public void setOsCategoryId(Long osCategoryId) {
        this.osCategoryId.setValue(osCategoryId);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
