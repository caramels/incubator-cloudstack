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
import com.cloud.api.response.ListResponse;
import com.cloud.api.response.VpcOfferingResponse;
import com.cloud.network.vpc.VpcOffering;

@Implementation(description="Lists VPC offerings", responseObject=VpcOfferingResponse.class)
public class ListVPCOfferingsCmd extends BaseListCmd{
    public static final Logger s_logger = Logger.getLogger(ListVPCOfferingsCmd.class.getName());
    private static final String _name = "listvpcofferingsresponse";
    
    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////
    @IdentityMapper(entityTableName="vpc_offerings")
    @Parameter(name=ApiConstants.ID, type=CommandType.LONG, description="list VPC offerings by id")
    private Long id;
    
    @Parameter(name=ApiConstants.NAME, type=CommandType.STRING, description="list VPC offerings by name")
    private String vpcOffName;
    
    @Parameter(name=ApiConstants.DISPLAY_TEXT, type=CommandType.STRING, description="list VPC offerings by display text")
    private String displayText;
    
    @Parameter(name=ApiConstants.IS_DEFAULT, type=CommandType.BOOLEAN, description="true if need to list only default " +
    		"VPC offerings. Default value is false")
    private Boolean isDefault;
    
    @Parameter(name=ApiConstants.SUPPORTED_SERVICES, type=CommandType.LIST, collectionType=CommandType.STRING, 
            description="list VPC offerings supporting certain services")
    private List<String> supportedServices;
    
    @Parameter(name=ApiConstants.STATE, type=CommandType.STRING, description="list VPC offerings by state")
    private String state;
    
    
    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////
    public Long getId() {
        return id;
    }

    public String getVpcOffName() {
        return vpcOffName;
    }

    public String getDisplayText() {
        return displayText;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public List<String> getSupportedServices() {
        return supportedServices;
    }
    
    public String getState() {
        return state;
    }
    
    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @Override
    public void execute(){
        List<? extends VpcOffering> offerings = _vpcService.listVpcOfferings(getId(), getVpcOffName(), getDisplayText(),
                getSupportedServices(), isDefault, this.getKeyword(), getState(), this.getStartIndex(), this.getPageSizeVal());
        ListResponse<VpcOfferingResponse> response = new ListResponse<VpcOfferingResponse>();
        List<VpcOfferingResponse> offeringResponses = new ArrayList<VpcOfferingResponse>();
        for (VpcOffering offering : offerings) {
            VpcOfferingResponse offeringResponse = _responseGenerator.createVpcOfferingResponse(offering);
            offeringResponses.add(offeringResponse);
        }

        response.setResponses(offeringResponses);
        response.setResponseName(getCommandName());
        this.setResponseObject(response);
    }

    
    @Override
    public String getCommandName() {
        return _name;
    }

}
