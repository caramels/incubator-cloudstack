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

package com.cloud.network.element;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;

import javax.ejb.Local;

import org.apache.log4j.Logger;

import com.cloud.api.commands.DeleteCiscoNexusVSMCmd;
import com.cloud.api.commands.EnableCiscoNexusVSMCmd;
import com.cloud.api.commands.DisableCiscoNexusVSMCmd;
import com.cloud.api.commands.ListCiscoNexusVSMsCmd;
import com.cloud.api.response.CiscoNexusVSMResponse;
import com.cloud.deploy.DeployDestination;
import com.cloud.event.ActionEvent;
import com.cloud.event.EventTypes;
import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.network.CiscoNexusVSMDeviceVO;
import com.cloud.network.CiscoNexusVSMDevice;
import com.cloud.network.CiscoNexusVSMDeviceManagerImpl;
import com.cloud.network.Network;
import com.cloud.network.PhysicalNetworkServiceProvider;
import com.cloud.network.Network.Capability;
import com.cloud.network.Network.Provider;
import com.cloud.network.Network.Service;
import com.cloud.network.dao.CiscoNexusVSMDeviceDao;
import com.cloud.utils.component.Inject;
import com.cloud.vm.NicProfile;
import com.cloud.vm.ReservationContext;
import com.cloud.vm.VirtualMachine;
import com.cloud.vm.VirtualMachineProfile;
import com.cloud.network.element.NetworkElement;
import com.cloud.offering.NetworkOffering;
import com.cloud.org.Cluster;
import com.cloud.utils.component.Manager;
import com.cloud.exception.ResourceInUseException;
import com.cloud.utils.exception.CloudRuntimeException;
import com.cloud.server.ManagementService;

@Local(value = NetworkElement.class)
public class CiscoNexusVSMElement extends CiscoNexusVSMDeviceManagerImpl implements CiscoNexusVSMElementService, NetworkElement, Manager {

    private static final Logger s_logger = Logger.getLogger(CiscoNexusVSMElement.class);

    @Inject
    CiscoNexusVSMDeviceDao _vsmDao;    

    @Override
    public Map<Service, Map<Capability, String>> getCapabilities() {
    	return null;
    }
    
    @Override
    public Provider getProvider() {
        return null;
    }
    
    @Override
    public boolean implement(Network network, NetworkOffering offering,
            DeployDestination dest, ReservationContext context)
            throws ConcurrentOperationException, ResourceUnavailableException,
            InsufficientCapacityException {
        return true;
    }
    
    @Override
    public boolean prepare(Network network, NicProfile nic,
            VirtualMachineProfile<? extends VirtualMachine> vm,
            DeployDestination dest, ReservationContext context)
            throws ConcurrentOperationException, ResourceUnavailableException,
            InsufficientCapacityException {
        return true;
    }
    
    @Override
    public boolean release(Network network, NicProfile nic,
            VirtualMachineProfile<? extends VirtualMachine> vm,
            ReservationContext context) throws ConcurrentOperationException,
            ResourceUnavailableException {
        return true;
    }
    
    @Override
    public boolean shutdown(Network network, ReservationContext context,
    		boolean cleanup) throws ConcurrentOperationException,
    		ResourceUnavailableException {
        return true;
    }

    @Override
    public boolean destroy(Network network, ReservationContext context)
            throws ConcurrentOperationException, ResourceUnavailableException {
        return true;
    }
    
    @Override
    public boolean isReady(PhysicalNetworkServiceProvider provider) {
        return true;
    }

    @Override
    public boolean shutdownProviderInstances(PhysicalNetworkServiceProvider provider,
    		ReservationContext context) throws ConcurrentOperationException,
    		ResourceUnavailableException {
    	return true;
    }
    
    @Override
    public boolean canEnableIndividualServices() {
    	return true;
    }
    
    @Override
    public boolean verifyServicesCombination(Set<Service> services) {
    	return true;
    }

    @Override
    @ActionEvent(eventType = EventTypes.EVENT_EXTERNAL_SWITCH_MGMT_DEVICE_DELETE, eventDescription = "deleting VSM", async = true)   
    public boolean deleteCiscoNexusVSM(DeleteCiscoNexusVSMCmd cmd) {
    	boolean result;
    	try {
    		result = deleteCiscoNexusVSM(cmd.getCiscoNexusVSMDeviceId());
    	} catch (ResourceInUseException e) {
    		s_logger.info("VSM could not be deleted");
    		// TODO: Throw a better exception here.
    		throw new CloudRuntimeException("Failed to delete specified VSM");
    	}
    	return result;
    }    

    @Override
    @ActionEvent(eventType = EventTypes.EVENT_EXTERNAL_SWITCH_MGMT_DEVICE_ENABLE, eventDescription = "deleting VSM", async = true)  
    public CiscoNexusVSMDeviceVO enableCiscoNexusVSM(EnableCiscoNexusVSMCmd cmd) {
    	CiscoNexusVSMDeviceVO result;
    	result = enableCiscoNexusVSM(cmd.getCiscoNexusVSMDeviceId());
    	return result;
    }
    
    @Override
    @ActionEvent(eventType = EventTypes.EVENT_EXTERNAL_SWITCH_MGMT_DEVICE_DISABLE, eventDescription = "deleting VSM", async = true)
    public CiscoNexusVSMDeviceVO disableCiscoNexusVSM(DisableCiscoNexusVSMCmd cmd) {
    	CiscoNexusVSMDeviceVO result;
    	result = disableCiscoNexusVSM(cmd.getCiscoNexusVSMDeviceId());
    	return result;
    }
    
    @Override
    public List<CiscoNexusVSMDeviceVO> getCiscoNexusVSMs(ListCiscoNexusVSMsCmd cmd) {
    	// If clusterId is defined, then it takes precedence, and we will return
    	// the VSM associated with this cluster.    	

    	Long clusterId = cmd.getClusterId();
    	Long zoneId = cmd.getZoneId();
    	List<CiscoNexusVSMDeviceVO> result = new ArrayList<CiscoNexusVSMDeviceVO>();
    	if (clusterId != null && clusterId.longValue() != 0) {    		
    		// Find the VSM associated with this clusterId and return a list.
    		CiscoNexusVSMDeviceVO vsm = getCiscoVSMbyClusId(cmd.getClusterId());
    		if (vsm == null) {
        		throw new CloudRuntimeException("No Cisco VSM associated with specified Cluster Id");
        	}
    		// Else, add it to a list and return the list.
    		result.add(vsm);
    		return result;
    	}    	
    	// Else if there is only a zoneId defined, get a list of all vmware clusters
    	// in the zone, and then for each cluster, pull the VSM and prepare a list.
    	if (zoneId != null && zoneId.longValue() != 0) {
    		ManagementService ref = cmd.getMgmtServiceRef();    	
    		List<? extends Cluster> clusterList = ref.searchForClusters(zoneId, cmd.getStartIndex(), cmd.getPageSizeVal(), "VMware");
    	
    		if (clusterList.size() == 0) {
    			throw new CloudRuntimeException("No VMWare clusters found in the specified zone!");
    		}
    		// Else, iterate through each vmware cluster, pull its VSM if it has one, and add to the list.
    		for (Cluster clus : clusterList) {
    			CiscoNexusVSMDeviceVO vsm = getCiscoVSMbyClusId(clus.getId());
    			if (vsm != null)
    				result.add(vsm);
    		}
    		return result;
    	}
    	
    	// If neither is defined, we will simply return the entire list of VSMs
    	// configured in the management server.
    	// TODO: Is this a safe thing to do? Only ROOT admin can invoke this call.
    	result = _vsmDao.listAllVSMs();    	
    	return result;
    }
    
    @Override
    public CiscoNexusVSMResponse createCiscoNexusVSMResponse(CiscoNexusVSMDevice vsmDeviceVO) {
            CiscoNexusVSMResponse response = new CiscoNexusVSMResponse();
            response.setId(vsmDeviceVO.getId());            
            response.setMgmtIpAddress(vsmDeviceVO.getipaddr());
            return response;
        }
    
    public CiscoNexusVSMResponse createCiscoNexusVSMDetailedResponse(CiscoNexusVSMDevice vsmDeviceVO) {
    	CiscoNexusVSMResponse response = new CiscoNexusVSMResponse();
    	response.setId(vsmDeviceVO.getId());
    	response.setDeviceName(vsmDeviceVO.getvsmName());
    	response.setDeviceState(vsmDeviceVO.getvsmDeviceState().toString());    	
    	response.setMgmtIpAddress(vsmDeviceVO.getipaddr());
    	// The following values can be null, so check for that.
    	if(vsmDeviceVO.getvsmConfigMode() != null)
    		response.setVSMConfigMode(vsmDeviceVO.getvsmConfigMode().toString());
    	if(vsmDeviceVO.getvsmConfigState() != null)
    		response.setVSMConfigState(vsmDeviceVO.getvsmConfigState().toString());
    	if(vsmDeviceVO.getvsmDeviceState() != null)
    		response.setVSMDeviceState(vsmDeviceVO.getvsmDeviceState().toString());
    	response.setVSMCtrlVlanId(vsmDeviceVO.getManagementVlan());
    	response.setVSMPktVlanId(vsmDeviceVO.getPacketVlan());
    	response.setVSMStorageVlanId(vsmDeviceVO.getStorageVlan());
    	return response;
    }

    @Override
    public String getPropertiesFile() {
    	return "cisconexusvsm_commands.properties";
    }
}
