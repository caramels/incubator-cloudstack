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
package com.cloud.api.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cloud.api.ApiConstants;
import com.cloud.api.BaseListCmd;
import com.cloud.api.IdentityMapper;
import com.cloud.api.Implementation;
import com.cloud.api.Parameter;
import com.cloud.api.PlugService;
import com.cloud.api.ServerApiException;
import com.cloud.api.response.ListResponse;
import com.cloud.api.response.VirtualRouterProviderResponse;
import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.ResourceAllocationException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.network.VirtualRouterProvider;
import com.cloud.network.element.VirtualRouterElementService;

@Implementation(description="Lists all available virtual router elements.", responseObject=VirtualRouterProviderResponse.class)
public class ListVirtualRouterElementsCmd extends BaseListCmd {
    public static final Logger s_logger = Logger.getLogger(ListNetworkOfferingsCmd.class.getName());
    private static final String _name = "listvirtualrouterelementsresponse";

    @PlugService
    private VirtualRouterElementService _service;
    
    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////
    @IdentityMapper(entityTableName = "virtual_router_providers")
    @Parameter(name=ApiConstants.ID, type=CommandType.LONG, description="list virtual router elements by id")
    private Long id;
    
    @IdentityMapper(entityTableName = "physical_network_service_providers")
    @Parameter(name=ApiConstants.NSP_ID, type=CommandType.LONG, description="list virtual router elements by network service provider id")
    private Long nspId;
    
    @Parameter(name=ApiConstants.ENABLED, type=CommandType.BOOLEAN, description="list network offerings by enabled state")
    private Boolean enabled;
    
    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////
    
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setNspId(Long nspId) {
        this.nspId = nspId;
    }

    public Long getNspId() {
        return nspId;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    @Override
    public String getCommandName() {
        return _name;
    }
    
    @Override
    public void execute() throws ResourceUnavailableException, InsufficientCapacityException, ServerApiException, ConcurrentOperationException, ResourceAllocationException {
        List<? extends VirtualRouterProvider> providers = _service.searchForVirtualRouterElement(this);
        ListResponse<VirtualRouterProviderResponse> response = new ListResponse<VirtualRouterProviderResponse>();
        List<VirtualRouterProviderResponse> providerResponses = new ArrayList<VirtualRouterProviderResponse>();
        for (VirtualRouterProvider provider : providers) {
            VirtualRouterProviderResponse providerResponse = _responseGenerator.createVirtualRouterProviderResponse(provider);
            providerResponses.add(providerResponse);
        }
        response.setResponses(providerResponses);
        response.setResponseName(getCommandName());
        this.setResponseObject(response);

    }
}
