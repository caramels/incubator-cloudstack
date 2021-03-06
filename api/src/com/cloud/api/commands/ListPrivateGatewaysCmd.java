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
import com.cloud.api.BaseListProjectAndAccountResourcesCmd;
import com.cloud.api.IdentityMapper;
import com.cloud.api.Implementation;
import com.cloud.api.Parameter;
import com.cloud.api.response.ListResponse;
import com.cloud.api.response.PrivateGatewayResponse;
import com.cloud.network.vpc.PrivateGateway;
import com.cloud.utils.Pair;

@Implementation(description="List private gateways", responseObject=PrivateGatewayResponse.class)
public class ListPrivateGatewaysCmd extends BaseListProjectAndAccountResourcesCmd{
    public static final Logger s_logger = Logger.getLogger(ListPrivateGatewaysCmd.class.getName());

    private static final String s_name = "listprivategatewaysresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////
    @IdentityMapper(entityTableName="vpc_gateways")
    @Parameter(name=ApiConstants.ID, type=CommandType.LONG, description="list private gateway by id")
    private Long id;
    
    @Parameter(name=ApiConstants.IP_ADDRESS, type=CommandType.STRING, description="list gateways by ip address")
    private String ipAddress;
    
    @Parameter(name=ApiConstants.VLAN, type=CommandType.STRING, description="list gateways by vlan")
    private String vlan;
    
    @IdentityMapper(entityTableName="vpc")
    @Parameter(name=ApiConstants.VPC_ID, type=CommandType.LONG, description="list gateways by vpc")
    private Long vpcId;
    
    @Parameter(name=ApiConstants.STATE, type=CommandType.STRING, description="list gateways by state")
    private String state;
    
    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////


    public String getVlan() {
        return vlan;
    }

    public String getIpAddress() {
        return ipAddress;
    }
    
    public Long getVpcId() {
        return vpcId;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getState() {
        return state;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////
    @Override
    public String getCommandName() {
        return s_name;
    }
    
    @Override
    public void execute() {
        Pair<List<PrivateGateway>, Integer> gateways = _vpcService.listPrivateGateway(this);
        ListResponse<PrivateGatewayResponse> response = new ListResponse<PrivateGatewayResponse>();
        List<PrivateGatewayResponse> projectResponses = new ArrayList<PrivateGatewayResponse>();
        for (PrivateGateway gateway : gateways.first()) {
            PrivateGatewayResponse gatewayResponse = _responseGenerator.createPrivateGatewayResponse(gateway);
            projectResponses.add(gatewayResponse);
        }
        response.setResponses(projectResponses, gateways.second());
        response.setResponseName(getCommandName());
        
        this.setResponseObject(response);
    }
}
