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
package com.cloud.api;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.cloud.uuididentity.dao.IdentityDao;
import com.cloud.uuididentity.dao.IdentityDaoImpl;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.cloud.utils.IdentityProxy;


public class IdentityTypeAdapter implements JsonSerializer<IdentityProxy>, JsonDeserializer<IdentityProxy> {
	
	@Override
	public JsonElement serialize(IdentityProxy src, Type srcType, JsonSerializationContext context) {
		if(SerializationContext.current().getUuidTranslation()) {
			assert(src != null);
			if(src.getValue() == null)
				return context.serialize(null);
	
			IdentityDao identityDao = new IdentityDaoImpl();
			if(src.getTableName() != null) {
				String uuid = identityDao.getIdentityUuid(src.getTableName(), String.valueOf(src.getValue()));
				if(uuid == null)
					return context.serialize(null);
				
				// Exceptions set the _idFieldName in the IdentityProxy structure. So if this field is not
				// null, prepare a structure of uuid and idFieldName and return the json representation of that.
				String idName = src.getidFieldName();
				if (idName != null) {
					// Prepare a structure.
					JsonObject jsonObj = new JsonObject();
					jsonObj.add("uuid", new JsonPrimitive(uuid));
					jsonObj.add("uuidProperty", new JsonPrimitive(idName));
					return jsonObj;
				}
				return new JsonPrimitive(uuid);
			} else {
				return new JsonPrimitive(String.valueOf(src.getValue()));
			}
		} else {
	        return new Gson().toJsonTree(src);
		}
	}
	
	@Override
	public IdentityProxy deserialize(JsonElement src, Type srcType,
			JsonDeserializationContext context) throws JsonParseException {

		IdentityProxy obj = new IdentityProxy();
		JsonObject json = src.getAsJsonObject();
		obj.setTableName(json.get("_tableName").getAsString());
		if(json.get("_value") != null)
			obj.setValue(json.get("_value").getAsLong());
		return obj;
	}
}
